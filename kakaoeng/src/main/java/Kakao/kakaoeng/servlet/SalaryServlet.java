package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.LectureDAO;

@SuppressWarnings("serial")
public class SalaryServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		String yearString = req.getParameter("year");
		String monthString = req.getParameter("month");
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		
		if(!(yearString == null || yearString.equals(""))){
			try{
				year = Integer.parseInt(yearString);
			}catch(Exception e){}
		}
		if(!(monthString == null || monthString.equals(""))){
			try{
				month = Integer.parseInt(monthString);
			}catch(Exception e){}
		}
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date startDate = cal.getTime();
		cal.set(Calendar.MONDAY, month);
		Date endDate = cal.getTime();
		
		//lectureDAO.getLectureListByTeacherId(teacherId, firstDate, lastDate)
	}

}
