package wordSetGener

import java.util.{Properties, Random}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.{BufferedSource, Source}

object Product {
  def main(args: Array[String]): Unit = {
    val event=10
    val topic = "comment"
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    props.put("acks", "all") // 确认的级别  ISR
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer") //生产端用序列化
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val line:BufferedSource=Source.fromFile("data/hanzi.txt")
    val rd=new Random();
    val li=line.mkString
    //随机发送10条消息
    for (i<-Range(0,event)){
      val sb=new StringBuilder()
      //随机生成20个数据
      for (ind <- Range(0,rd.nextInt(20))){
        sb+= li.charAt(rd.nextInt(li.length))
      }
      val userkey="User"+rd.nextInt(100)
      //发送消息
      val producer = new KafkaProducer[String, String](props);
      val pr = new ProducerRecord[String, String](topic, userkey,sb.toString()) // 0表示消息的类型  name 地址 类型
      producer.send(pr)


    }

//    println(line.toString().substring(0,10).toString)






//
//    val rnd = new Random()
//      val pr = new ProducerRecord[String, String](topic, nameAddr._1, s"${nameAddr._1}\t${nameAddr._2}\t0") // 0表示消息的类型  name 地址 类型
//


//      Thread.sleep(rnd.nextInt(10))
    }

}