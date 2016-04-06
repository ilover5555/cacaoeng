package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.User;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminBoardUpdate.do")
public class AdminBoardUpdateServlet extends AbstractBoardUpdateServlet {
	public User qualified(HttpServletRequest req){
		return (Admin) req.getSession().getAttribute("admin");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processPost(req, resp, "admin");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(processGet(req, resp, "admin", "./index.jsp"))
			req.getRequestDispatcher("./notify_update.jsp").forward(req, resp);
	}

}
