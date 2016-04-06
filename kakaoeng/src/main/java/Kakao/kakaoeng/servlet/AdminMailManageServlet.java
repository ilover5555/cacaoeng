package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Student;

@WebServlet("/teacher/adminMailManage.view")
public class AdminMailManageServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		req.setAttribute("EVENT_MAIL_TEXT_TEACHER_CONFIRM", environDAO.getTeacherConfirmMailMessage());
		req.setAttribute("EVENT_MAIL_SUBJECT_TEACHER_CONFIRM", environDAO.getTeacherConfirmMailSubject());
		req.setAttribute("EVENT_MAIL_MODE_TEACHER_CONFIRM", environDAO.getTeacherConfirmMailMode());
		
		req.setAttribute("EVENT_MAIL_TEXT_LECTURE_REGISTERED", environDAO.getLectureRegisteredMailMessage());
		req.setAttribute("EVENT_MAIL_SUBJECT_LECTURE_REGISTERED", environDAO.getLectureRegisteredMailSubject());
		req.setAttribute("EVENT_MAIL_MODE_LECTURE_REGISTERED", environDAO.getLectureRegisteredMailMode());
		
		req.setAttribute("EVENT_MAIL_TEXT_LECTURE_REGISTERED", environDAO.getLectureRegisteredMailMessage());
		req.setAttribute("EVENT_MAIL_SUBJECT_LECTURE_REGISTERED", environDAO.getLectureRegisteredMailSubject());
		req.setAttribute("EVENT_MAIL_MODE_LECTURE_REGISTERED", environDAO.getLectureRegisteredMailMode());
		
		req.setAttribute("EVENT_MAIL_TEXT_PAYED", environDAO.getPayedMailMessage());
		req.setAttribute("EVENT_MAIL_SUBJECT_PAYED", environDAO.getPayedMailSubject());
		req.setAttribute("EVENT_MAIL_MODE_PAYED", environDAO.getPayedMailMode());
		
		req.setAttribute("EVENT_MAIL_TEXT_LEVEL_TEST", environDAO.getLevelTestMessage());
		req.setAttribute("EVENT_MAIL_SUBJECT_LEVEL_TEST", environDAO.getLevelTestMailSubject());
		req.setAttribute("EVENT_MAIL_MODE_LEVEL_TEST", environDAO.getLevelTestMailMode());
		
		req.getRequestDispatcher("./admin_mail_manage.jsp").forward(req, resp);
		
	}
}
