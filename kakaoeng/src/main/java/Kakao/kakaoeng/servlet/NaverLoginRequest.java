package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Student;

@SuppressWarnings("serial")
@WebServlet("/naverLoginRequest.do")
public class NaverLoginRequest extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Student student = (Student) req.getSession().getAttribute("student");
		if(student != null){
			resp.sendRedirect("./refresh_parent.jsp");
			return;
		}
		String state = (String) req.getSession().getAttribute("state");
		String genereatedState = Util.generateState();
		req.getSession().setAttribute("state", genereatedState);
		String url = "https://nid.naver.com/oauth2.0/authorize?client_id=C1xqnuR9I8m9XBiJg0L2&response_type=code&redirect_uri="+URLEncoder.encode("http://www.kakaoenglish.kr:8080/EnglishManageWebProject/naverLogin.do", "UTF-8")+"&state="+req.getSession().getAttribute("state");
		resp.sendRedirect(url);
	}
}
