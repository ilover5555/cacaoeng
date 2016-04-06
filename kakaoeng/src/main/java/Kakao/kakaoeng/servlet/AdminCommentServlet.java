package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.domain.model.User;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminComment.do")
public class AdminCommentServlet extends AbstractCommentServlet {

	@Override
	public User qualified(HttpServletRequest req){
		if((User) req.getSession().getAttribute("admin") != null){
			return (User) req.getSession().getAttribute("admin");
		}
		else{
			return null;
		}
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.process(req, resp, "admin");
	}

}
