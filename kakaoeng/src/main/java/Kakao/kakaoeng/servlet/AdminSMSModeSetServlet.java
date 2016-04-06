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
import Kakao.kakaoeng.dao.EnvironDAO;

@WebServlet("/teacher/adminSMSModeSet.do")
public class AdminSMSModeSetServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String type = req.getParameter("type");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		if(type.equals("new_sort")){
			environDAO.saveNewRegisterSMSMode(req.getParameter("mode"));
		}else if(type.equals("lecture_sort")){
			environDAO.saveNewLectureSMSMode(req.getParameter("mode"));
		}else if(type.equals("re_sort")){
			environDAO.saveRequestLectureSMSMode(req.getParameter("mode"));
		}else if(type.equals("re_after_sort")){
			environDAO.saveAfterRequestLectureSMSMode(req.getParameter("mode"));
		}else{
			Util.sendError(resp, "Invalid mode for AdminSMSModeSetServlet : " + req.getParameter("mode"));
			return;
		}
		
		Util.sendSuccess(resp, "모드 변경이 성공적으로 이루어 졌습니다.");
	}

}
