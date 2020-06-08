package wordpython
import scala.sys.process._
object Test1Python {
  def main(args: Array[String]): Unit = {
//    val proc1 = Runtime.getRuntime().exec("python spark.py")
    new Thread(new Runnable {
      override def run(): Unit = {
        "python /Users/mac/IdeaProjects/com.ly.sparkStream/src/main/scala/wordpython/spark.py args" !
      }
    }).start()//开启爬虫

  }
}
