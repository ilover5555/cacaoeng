package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/viewTeachers")
public class TeacherListHandler extends HttpServlet {

	public TeacherListHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String rate = req.getParameter("rate");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		List<Teacher> teacherList = null;
		if(rate == null || rate.equals(""))
			teacherList = teacherDAO.getUnConfirmedTeacherList();
		else
			teacherList = teacherDAO.getSpecificRateTeacher(rate);
		RequestDispatcher rd = req.getRequestDispatcher("TeacherList.jsp");
		req.setAttribute("teacherList", teacherList);
		rd.include(req, resp);
	}
}
