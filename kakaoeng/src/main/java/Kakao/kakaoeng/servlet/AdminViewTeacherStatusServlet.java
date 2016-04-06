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
import Kakao.kakaoeng.TeacherScheduleStateReader;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminViewTeacherStatus.view")
public class AdminViewTeacherStatusServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		String teacherId = req.getParameter("teacherId");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		Teacher teacher = teacherDAO.getTeacherWithId(teacherId);
		if(teacher == null){
			Util.sendError(resp, teacherId+"(teacherId) is not exists.");
			return;
		}
		TeacherScheduleStateReader reader = new TeacherScheduleStateReader();
		reader.process(applicationContext, req, resp, teacher);
		
		req.setAttribute("teacher", teacher);
		req.getRequestDispatcher("./ScheduleStatusTableTeacherForAdmin.jsp").forward(req, resp);
	}

}
