package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/viewSchedule.do")
public class ViewSchedule extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		Teacher teacher = (Teacher) req.getSession().getAttribute("teacher");
		if(teacher == null)
		{
			req.getRequestDispatcher("./log_in.jsp").forward(req, resp);
			return;
		}
		
		ClassTimeDAO classTimeDAO = (ClassTimeDAO) applicationContext.getBean("classTimeDAO");
		List<ClassTime> result = classTimeDAO.getClassTimeListByTeacherId(teacher.getId());
		
		req.setAttribute("classList", result);
		
		req.getRequestDispatcher("./view_schedule.jsp").include(req, resp);;
	}

}
