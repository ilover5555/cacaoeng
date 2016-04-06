package Kakao.kakaoeng.listener;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		req.setAttribute("lc", new LoginChecker((HttpServletRequest) req));
		try{
			chain.doFilter(req, res);
		}catch(LoginRequiredException e){
			((HttpServletResponse)res).sendRedirect(e.getMessage());
			
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
