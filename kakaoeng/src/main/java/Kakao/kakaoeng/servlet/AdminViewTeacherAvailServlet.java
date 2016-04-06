package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminViewTeacherAvail.view")
public class AdminViewTeacherAvailServlet extends HttpServlet {

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
		
		ClassTimeDAO classTimeDAO = (ClassTimeDAO) applicationContext.getBean("classTimeDAO");
		List<ClassTime> result = classTimeDAO.getClassTimeListByTeacherId(teacher.getId());
		
		req.setAttribute("classList", result);
		
		req.getRequestDispatcher("./ScheduleTableTeacher.jsp").include(req, resp);;
	}

}
