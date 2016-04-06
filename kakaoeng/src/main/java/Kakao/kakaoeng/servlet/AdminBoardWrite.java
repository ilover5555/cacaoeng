package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.User;


@SuppressWarnings("serial")
@WebServlet("/teacher/adminWrite.do")
public class AdminBoardWrite extends AbstractBoardWriteServlet {

	public User qualified(HttpServletRequest req){
		return (Admin) req.getSession().getAttribute("admin");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, "admin");
	}
}
