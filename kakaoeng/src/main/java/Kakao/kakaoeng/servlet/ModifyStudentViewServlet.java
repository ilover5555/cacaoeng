package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.Util;

@WebServlet("/modifyStudent.view")
public class ModifyStudentViewServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getStudentLogin()){
			Util.sendError(resp, "로그인 후 다시 시도해 주세요.");
			return;
		}
		req.getRequestDispatcher("./modify_student.jsp").forward(req, resp);
	}

}
