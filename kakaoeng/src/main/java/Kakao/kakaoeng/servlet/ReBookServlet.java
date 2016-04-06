package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import Kakao.kakaoeng.ClassSearchUnit;
import Kakao.kakaoeng.RebookManager;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.RegisterTime;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;
import Kakao.kakaoeng.domain.model.Lecture.Status;
import Kakao.kakaoeng.domain.model.Purchase.Method;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;
import Kakao.kakaoeng.domain.model.RegisterTime.TimeType;

@SuppressWarnings("serial")
@WebServlet("/rebook.do")
public class ReBookServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");

		String id = req.getParameter("id");
		int length = Integer.parseInt(req.getParameter("length"));
		String baseDateString = req.getParameter("baseDate");
		Course course = Course.valueOf(req.getParameter("sort"));
		String book = req.getParameter("book");
		String method = req.getParameter("method");
		String studentId = req.getParameter("student");
		Status status = Status.valueOf(req.getParameter("status"));
		int purchaseNumber = Integer.parseInt(req.getParameter("purchaseNumber"));
		int lectureId = Integer.parseInt(req.getParameter("lectureId"));
		
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		Student student = studentDAO.findStudentById(studentId);
		
		Date date = Util.parseDate(baseDateString);
		
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		Purchase purchase = purchaseDAO.getPurchaseById(purchaseNumber);
		
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		Lecture lecture = lectureDAO.getLectureByIdTransaction(lectureId);
		
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		
		List<ClassLog> classLogList = classLogDAO.getAllClassLogListByLectureId(lecture.getId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		Date tommorow = cal.getTime();
		if(date.before(tommorow))
			date = tommorow;
		
		for(ClassLog classLog : classLogList){
			if(!(classLog.getClassState().equals(ClassState.PostponeStudent) || classLog.getClassState().equals(ClassState.PostponeTeacher))){
				cal.setTime(classLog.getClassDate());
				cal.add(Calendar.DATE, 1);
				Date temp = cal.getTime();
				if(date.before(temp))
					date = temp;
			}
		}
		List<ClassSearchUnit> bookList = new ArrayList<>();
		for(int i=0; i<length; i++){
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = null;
				try {
					jsonObject = (JSONObject)jsonParser.parse(req.getParameter("index"+i));
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
			
			String dayOfWeekString = (String) jsonObject.get("dayOfWeek");
			DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekString);
			int hour = (int)(long) jsonObject.get("hour");
			int minute =(int)(long) jsonObject.get("minute");
			int duration = (int)(long) jsonObject.get("duration");
			int weeks = (int)(long) jsonObject.get("weeks");
			int oneClassId = (int)(long) jsonObject.get("oneClassId");
			
			List<DayOfWeek> tested = new ArrayList<>();
			tested.add(dayOfWeek);
			Map<DayOfWeek, Date> lateDate = Util.getLatestDateForSpecificDayOfWeek(tested, date);
			
			
			RegisterTime rt = new RegisterTime(dayOfWeek, hour, TimeType.getInstanceFromMinute(minute));
			Duration d = new Duration(rt, duration);
			ClassSearchUnit csu = new ClassSearchUnit(d, lateDate.get(dayOfWeek), weeks);
			OneClass oneClass = oneClassDAO.getOneClassByIdTransaction(oneClassId);
			csu.setParent(oneClass.getParent());
			bookList.add(csu);
		}
		RebookManager rebookManager = (RebookManager) applicationContext.getBean("rebookManager");
		String msg = rebookManager.rebookTransaction(purchaseNumber, status, lecture, date, id, bookList, student, course, book, method, purchase, req.getRemoteAddr());
		
		resp.getWriter().write(msg);
	}

}
