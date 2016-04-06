package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.AdminDAO;

@WebServlet("/teacher/adminAccountDelete.do")
public class AdminAccountDeleteServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin()){
			throw new LoginRequiredException("./index.jsp");
		}
		
		String id = req.getParameter("id");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		AdminDAO adminDAO = (AdminDAO) applicationContext.getBean("adminDAO");
		
		adminDAO.delete(id);
		
		Util.sendSuccess(resp, "성공적으로 삭제하였습니다.");
	}

}
