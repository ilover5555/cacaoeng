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
import Kakao.kakaoeng.Mail;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminTeacherConfirm.do")
public class AdminTeacherConfirmServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String teacherId = req.getParameter("teacherId");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		
		Teacher teacher = teacherDAO.getTeacherWithId(teacherId);
		if(teacher == null){
			Util.sendError(resp, teacherId + " is not valid or dont exist");
			return;
		}
		
		teacherDAO.updateConfirm(teacherId, true);
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		String original = environDAO.getTeacherConfirmMailMessage();
		String output = Util.fillContent(original, teacher);
		
		Mail.send(Mail.username, teacherId, "[Insky Edu] Teacher registration request has been approved", output);
		
		resp.sendRedirect("./adminTeacherAccept.do");
	}
	
}
