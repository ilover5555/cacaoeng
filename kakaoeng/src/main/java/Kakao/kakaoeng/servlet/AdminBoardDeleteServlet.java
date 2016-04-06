package Kakao.kakaoeng.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.domain.model.User;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminDeleteBoard.do")
public class AdminBoardDeleteServlet extends AbstractBoardDeleteServlet {
	public User qualified(HttpServletRequest req){
		return (User) req.getSession().getAttribute("admin");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, "admin");
	}
}
