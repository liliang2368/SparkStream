import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

class HttpPerse {
    public static void main(String[] args) throws  Exception{
        //先声明一个套接字
        ServerSocket ss=new ServerSocket(8083);
        boolean flag=true;
        while(flag){
            System.out.println("服务器开始监听了");
            Socket s=ss.accept();
            System.out.println("ly 服务器"+s.getInetAddress()+"is connectiing to ly");
            new Thread(new Task(s)).start();
        }
    }

static class Task implements  Runnable{
    private Socket s;
    private InputStream iis;
    private OutputStream oos;
    public Task(Socket s){
        this.s=s;
        try {
            this.iis=s.getInputStream();
            this.oos=s.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        //包装一个HttpServlet的对象  功能是iis中0那个取数据 解析请求信息 保存信息
        LyHttpServletRequest request=new LyHttpServletRequest(this.iis);
        //包装一个HttpServletResponse的对象 从request中取信息(文件的资源) 读取自观 构建响应头 会给客户端
        LyHttpservletResponse response=new LyHttpservletResponse(request, this.oos);
        response.sendRedirect();
}
 }
}