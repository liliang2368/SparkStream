import java.io.InputStream;
import java.util.StringTokenizer;
public class LyHttpServletRequest   {

	private String requestURI;//资源的地址
	private String requestURLparams;//就是请求的文本 我爱中国
	private InputStream iis;
	
	
	public LyHttpServletRequest(InputStream iis){
		this.iis=iis;
		parse();//开始解析
	}

	public String getRequestURLparams() {
		return requestURLparams;
	}

	public void setRequestURLparams(String requestURLparams) {
		this.requestURLparams = requestURLparams;
	}

	public void parse() {
		String requestInfoString=readFromInputStream();//从输入流中读取浏览器发送过来的文本
		if(requestInfoString==null || "".equals(requestInfoString)){
			return ;
		}
		//开始解析request的字符串
		parseRequestInfoString(requestInfoString);
	}

	private void parseRequestInfoString(String requestInfoString) {
		//System.out.println(requestInfoString);
		StringTokenizer st=new StringTokenizer(requestInfoString);
		if(st.hasMoreTokens()){
			String method=st.nextToken();//解析成GET
			String urr=st.nextToken();//开始解析  token?spark="我爱中国"
			if(urr.contains("?")){
				this.requestURI=urr.substring(1,urr.indexOf("?"));//解析token
				this.requestURLparams=urr.substring(urr.indexOf("?")+1);//截取我爱中国这个文本
			}else{
				this.requestURI=urr;
			}

		}
		//TODO：后面暂时不管
//		parseparame(requestInfoString);
	}
	private String readFromInputStream() {
		//input中读出所有的内容(http请求协议 ==》protocal)
		//TODO:从流中取protocal
		StringBuffer sb=new StringBuffer(1024*10);//10k
		int length=-1;
		byte [] bs=new byte[1024*10];
		try {
			length=this.iis.read(bs);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			length=-1;
		}
		for(int j=0;j<length;j++){
			sb.append((char)bs[j]);
		}
		return sb.toString();
	}


	public String getRequestURI() {
		// TODO Auto-generated method stub
		return requestURI;
	}






	
	
	
	
	
	
	
	
	
	
	
	
	

}
