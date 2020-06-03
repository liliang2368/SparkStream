import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Test1 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf =new SparkConf().setMaster("local[*]").setAppName("word")
    val ssc=new StreamingContext(conf,Seconds(2))
    //实时流监听
    val lines=ssc.socketTextStream("localhost",9999)
    //进行单词计数
    val words=lines.flatMap(_.split(" "))
    val pairs=words.map(word=>(word,1))
    val wordsCounts=pairs.reduceByKey(_+_)
    wordsCounts.print()
    ssc.start()
    ssc.awaitTermination()//等待系统发出 退出ha
  }
}
