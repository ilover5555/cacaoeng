package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.BookManager;
import Kakao.kakaoeng.CalculateTuition;
import Kakao.kakaoeng.ClassSearchUnit;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.RegisterTime;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.Purchase.Method;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;
import Kakao.kakaoeng.domain.model.RegisterTime.TimeType;

@WebServlet("/book.do")
public class BookTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Student student = (Student) req.getSession().getAttribute("student");
		if(student == null){
			Util.sendError(resp, "다시 로그인 후 시도해 주세요.");
			return;
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");

		String id = req.getParameter("id");
		int length = Integer.parseInt(req.getParameter("length"));
		String baseDateString = req.getParameter("baseDate");
		Course course = Course.valueOf(req.getParameter("sort"));
		String book = req.getParameter("book");
		String method = req.getParameter("method");
		String matchSort = req.getParameter("matchSort");
		
		Date date = Util.parseDate(baseDateString);
		
		List<ClassSearchUnit> bookList = new ArrayList<>();
		for(int i=0; i<length; i++){
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = null;
				try {
					jsonObject = (JSONObject)jsonParser.parse(req.getParameter("index"+i));
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			String dayOfWeekString = (String) jsonObject.get("dayOfWeek");
			DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekString);
			int hour = (int)(long) jsonObject.get("hour");
			int minute =(int)(long) jsonObject.get("minute");
			int duration = (int)(long) jsonObject.get("duration");
			int weeks = (int)(long) jsonObject.get("weeks");
			
			List<DayOfWeek> tested = new ArrayList<>();
			tested.add(dayOfWeek);
			Map<DayOfWeek, Date> lateDate = Util.getLatestDateForSpecificDayOfWeek(tested, date);
			
			
			RegisterTime rt = new RegisterTime(dayOfWeek, hour, TimeType.getInstanceFromMinute(minute));
			Duration d = new Duration(rt, duration);
			bookList.add(new ClassSearchUnit(d, lateDate.get(dayOfWeek), weeks));
			
		}
		BookManager bookManager = (BookManager) applicationContext.getBean("bookManager");
		String msg = "";
		try{
			EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
			CalculateTuition ct = CalculateTuitionServlet.getCalculator(matchSort, bookList, environDAO);
			msg = bookManager.bookTransaction(id, bookList, student, bookList.get(0).getWeeks(), date, id, course, book, Method.valueOf(method), null, -1, req.getRemoteAddr(), ct.calculate());
			
		}catch(RuntimeException e){
			msg = e.getMessage();
		}
		
		resp.getWriter().write(msg);
	}
}