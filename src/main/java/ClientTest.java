import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        ServerSocket ss=new ServerSocket(9999);
        boolean flag=true;
        while(true){
            Socket s=ss.accept();
            new Thread(new Task(s)).start();
        }
    }
    //url地址
    public static String[] url_paths = {
            "article/112.html",
            "article/113.html",
            "article/114.html",
            "article/115.html",
            "article/116.html",
            "article/117.html",
            "article/118.html",
            "article/119.html",
            "video/821",
            "tag/list"
    };

    //ip数字
    public static String[] ip_splices = {"102","71","145","33","67","54","164","121"};

    //http网址
    public static String[] http_referers = {
            "https://www.baidu.com/s?wd=%s",
            "https://www.sogou.com/web?query=%s",
            "https://cn.bing.com/search?q=%s",
            "https://search.yahoo.com/search?p=%s"
    };

    //搜索关键字
    public static String[] search_keyword = {
            "复制粘贴玩大数据",
            "Bootstrap全局css样式的使用",
            "Elasticsearch的安装（windows）",
            "Kafka的安装及发布订阅消息系统（windows）",
            "window7系统上Centos7的安装",
            "复制粘贴玩大数据系列教程说明",
            "Docker搭建Spark集群（实践篇）"
    };

    //状态码
    public static String[] status_codes = {"200","404","500"};

    //随机生成url
    public static String sample_url(){
        int urlNum = new Random().nextInt(10);
        return url_paths[urlNum];
    }

    //随机生成ip
    public static String sample_ip(){
        int ipNum;
        String ip = "";
        for (int i=0; i<4; i++){
            ipNum = new Random().nextInt(8);
            ip += "."+ip_splices[ipNum];
        }
        return ip.substring(1);
    }

    //随机生成检索
    public static String sample_referer(){
        Random random = new Random();
        int refNum = random.nextInt(4);
        int queryNum = random.nextInt(7);
        if (random.nextDouble() < 0.2){
            return "-";
        }
        String query_str = search_keyword[queryNum];

        String referQuery = String.format(http_referers[refNum], query_str);
        return referQuery;
    }

    //随机生成状态码
    public static String sample_status_code(){
        int codeNum = new Random().nextInt(3);
        return status_codes[codeNum];
    }

    //格式化时间样式
    public static String formatTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    //生成日志方法
}
class Task implements  Runnable{
    private Socket s;
    public Task(Socket s){
       this.s=s;
    }
    @Override
    public void run() {
        Random  r=new Random();
        boolean flag=true;
        try {
            OutputStream oos=s.getOutputStream();
            PrintWriter out=new PrintWriter(oos,true);
            while(true){
                String url = ClientTest.sample_url();
                String ip = ClientTest.sample_ip();
                String referer = ClientTest.sample_referer();
                String code = ClientTest.sample_status_code();
                String newTime = ClientTest.formatTime();
                String log = ip+"\t"+newTime+"\t"+"\"GET /"+url+" HTTP/1.1 \""+"\t"+referer+"\t"+code;
                Thread.sleep(1000);
            //这里生成日志
            System.out.println(log);
            out.println(log);
            out.flush();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}