package Kakao.kakaoeng.listener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import Kakao.kakaoeng.Util;

public class EncodingFilter implements Filter {

	String encoding = null;
	
	public static class EncodingRequest extends HttpServletRequestWrapper{

		public EncodingRequest(HttpServletRequest request) {
			super(request);
			// TODO Auto-generated constructor stub
		}
		
		
		public String getParameter(String key){
			boolean b1 = !this.getMethod().equals("POST");
			boolean b2 = (this.getHeader("content-type") == null);
			boolean b3 = false;
			if(b2 == false){
				b3 = (this.getHeader("content-type").indexOf("charset") != -1);
			}
			if(b1 || b2 || b3)
				return super.getParameter(key);
			String result=null;
			try {
				result = new String(super.getParameter(key).getBytes("8859_1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain nextFilter)
			throws IOException, ServletException {
		req.setCharacterEncoding("UTF8");
		res.setCharacterEncoding("UTF8");
		Util.debugRequstParameter((HttpServletRequest) req);
		nextFilter.doFilter(req, res);
	}
	
}
