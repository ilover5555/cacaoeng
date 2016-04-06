package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.dao.ClassTimeUsageLogDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Teacher;


@SuppressWarnings("serial")
@WebServlet("/m3/avaliableSchedule.view")
public class ViewAvailableSchedule extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		String teacherId = req.getParameter("teacherId");
		Teacher teacher = teacherDAO.getTeacherWithId(teacherId);
		if(teacher == null)
		{
			resp.getWriter().write("Requested teacher is not exist.");
			return;
		}
		String baseDateString = req.getParameter("baseDate");
		Date baseDate = null;
		try{
			baseDate = Util.parseDate(baseDateString);
		}catch(RuntimeException e){
			baseDate = new Date();
		}
		String monthDuration = req.getParameter("monthDuration");
		int weeks = Util.getWeeksFromMonthDuration(monthDuration);
		Date endDate = Util.addWeeksToDate(baseDate, weeks);
		
		
		ClassTimeDAO classTimeDAO = (ClassTimeDAO) applicationContext.getBean("classTimeDAO");
		ClassTimeUsageLogDAO classTimeUsageLogDAO = (ClassTimeUsageLogDAO) applicationContext.getBean("classTimeUsageLogDAO");
		
		List<ClassTime> classTimeList = classTimeDAO.getClassTimeListByTeacherId(teacher.getId());
		Util.removeUnavailable(classTimeList, classTimeUsageLogDAO, baseDate, endDate);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		req.setAttribute("teacherId", teacher.getId());
		req.setAttribute("teacherName", teacher.getName());
		req.setAttribute("teacherClassName", teacher.getClassName());
		req.setAttribute("baseDate", format.format(baseDate));
		req.setAttribute("classList", classTimeList);
		
		req.getRequestDispatcher("./ViewAvailableSchedule.jsp").include(req, resp);
	}

}
