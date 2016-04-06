package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;

@SuppressWarnings("serial")
@WebServlet("/m5/studentBoardView.view")
public class StudentBoardViewServlet extends AbstractBoardViewServlet {

	@Override
	public boolean qualified(HttpServletRequest req) {
		LoginChecker lc = new LoginChecker(req);
		if(!(lc.getAdminLogin() || lc.getStudentLogin()))
			return false;
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			if(process(req, resp, StudentBoardListServlet.class , "student"))
				req.getRequestDispatcher("./student_board_view.jsp").include(req, resp);
			else
				req.getRequestDispatcher("./studentBoardList.view").include(req, resp);
		}catch(LoginRequiredException e){
			resp.sendRedirect("./main/main.jsp");
			return;
		}
	}

}
