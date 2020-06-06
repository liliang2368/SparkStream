package Spark_Stream_window

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TextFileStream {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR) //配置日志
    val conf = new SparkConf().setMaster("local[*]").setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.checkpoint("./chpoint")
    //指定数据源为hdfs
    val lines=ssc.socketTextStream("localhost",9999,StorageLevel.MEMORY_ONLY_SER)
    val words=lines.flatMap(word=>word.split(" "))
    //每10秒统计前30秒各个单词累计的次数
    val wordCounts=words.map(word=>(word,1)).reduceByKeyAndWindow((a:Int,b:Int)=>a+b,Seconds(30),Seconds(10))
    printValues(wordCounts)
    ssc.start()
    ssc.awaitTermination()
  }
  def printValues(stream:DStream[(String,Int)]): Unit ={
    stream.foreachRDD(foreachFunc)
    def foreachFunc=(rdd:RDD[(String,Int)])=>{
      val array=rdd.collect()//采集 worker端的结果打牌driver端
      println("=======begin to show results============")
      for (res <- array){
        println(res)
      }
      println("========ending show result=============")
    }
  }
}
