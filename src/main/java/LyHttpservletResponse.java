import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;


public class LyHttpservletResponse  {
	private OutputStream oos;
	private LyHttpServletRequest request;
	private LyHttpservletResponse response;
	private String contentType=null;
	
	public LyHttpservletResponse(LyHttpServletRequest request,OutputStream out){
		this.oos=out;
		this.request=request;
	}
/**
 * 从request中取出uri  2.判断是否在本地存在这个uri指代的文件,没有 ,404有 3.以输出流读取这个文件4
 * 输出流文件写到客户端,要加入响应的协议
 */
	public void sendRedirect() {
		String responseprotocal=null;//响应头协议
		byte [] fileContent=null;//响应内容
		String uri=request.getRequestURI();//获取资源地址 token
		if(uri.equalsIgnoreCase("token")) {
			try {

				String params=request.getRequestURLparams().split("=")[1];
				params= URLDecoder.decode(params,"utf-8");
				 JiebaSegmenter segmenter = new JiebaSegmenter();
				List<String> list=segmenter.sentenceProcess(params);

				//这里使用结巴分词
				System.out.println(params);
				//这里使用结巴分词
				String str="{\"ret\":0, \"msg\":\"OK\", \"terms\":\"sentence = \" " + list.toString() +" \"\"}";
				str=URLDecoder.decode(str,"utf-8");
				responseprotocal=gen200(str.length());
				System.out.println(responseprotocal);
				oos.write(responseprotocal.getBytes());//写协议
				oos.flush();
				oos.write(str.getBytes());//写出文件
				oos.flush();


			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				if (oos != null) {
					try {
						oos.close();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		}
	}

	/**
	 * 要考虑静态文件的类型
	 * @param bodylength
	 *            , 内容的长度
	 * @return
	 */
	private String gen200(long bodylength) {

		String protocal200 = "";

			contentType="text/html";
			protocal200 = "HTTP/1.0 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "
					+ bodylength + "\r\n\r\n";

		return protocal200;
	}

	/**
	 * 产生404响应
	 * 
	 * @return
	 */
	private String gen404(long bodylength) {
		String protocal404 = "HTTP/1.1 404 File Not Found\r\nContent-Type: text/html\r\nContent-Length: "
				+ bodylength + "\r\n\r\n";
		return protocal404;
	}

	public PrintWriter getWriter() {
		//oos字节流  Writer字符流
		PrintWriter pw=new PrintWriter(this.oos);
		return pw;
	}

	public String getContentType() {
		// TODO Auto-generated method stub
		return this.contentType;
	}

}
