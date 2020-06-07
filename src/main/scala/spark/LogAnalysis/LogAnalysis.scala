package spark.LogAnalysis

import java.util.Properties

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}


object LogAnalysis {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    //创建 sparkConf的运行目录
    val sparkConf=new SparkConf()
      .setAppName("log Analysis")
      .setMaster("local[*]")
    //因为要用到 sparkSql 所以要创建上下文
    val spark=SparkSession.builder()
      .appName("Log Analysis")
      .config(sparkConf)
      .getOrCreate()//创建
    //利用sparkSession来创建上下文
    val sc=spark.sparkContext
    //建立流式处理上下文 spark Streaming 每隔两秒去读取数据
    val ssc=new StreamingContext(sc,Seconds(2))
    //以上都表明在一个程序中 只能创建一个与spark的连接
    //mysql的配置
    val properties=new Properties()
    properties.put("user","root")
    properties.put("password","a")
    //读入日志文件目录下的日志信息流
    val logStream=ssc.textFileStream("./logs/")
    //从 DStream 中取出每个 RDD 将每一个日志 RDD信息流转换成 dataframe
    logStream.foreachRDD( (rdd:RDD[String])=>{
      import spark.implicits._//导入隐士参数
      val data=rdd.map(w=>{
        val tokens=w.split("\t")
        Record(tokens(0),tokens(1),tokens(2))
      }).toDF()
      //  创建视图
      data.createOrReplaceTempView("alldata")
      //条件筛选:只查看 error 和 warn的信息
      val logImp=spark.sql("select *from alldata where log_level='[error]'")
//      DateFoemat是可以读和写的 对数据库
      logImp.write.mode(SaveMode.Append)
        .jdbc("jdbc:mysql://node1:3306/log_analysis","important_logs",properties)

    })



  }
}
case class Record(log_level:String,method:String,content:String)