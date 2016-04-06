package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.TeacherScheduleStateReader;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/viewTeacherScheduleState.do")
public class ViewTeacherScheduleState extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		Teacher teacher = (Teacher) req.getSession().getAttribute("teacher");
		if(teacher == null)
		{
			req.getRequestDispatcher("./log_in.jsp").forward(req, resp);
			return;
		}
		TeacherScheduleStateReader reader = new TeacherScheduleStateReader();
		reader.process(applicationContext, req, resp, teacher);
		
		req.getRequestDispatcher("./view_schedule_status.jsp").include(req, resp);;
	}
}
