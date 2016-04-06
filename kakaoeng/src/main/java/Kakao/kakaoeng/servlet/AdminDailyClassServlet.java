package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Count;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

@WebServlet("/teacher/adminDailyClass.view")
public class AdminDailyClassServlet extends HttpServlet {

	public static class DailyClassVO{
		private OneClass oneClass;
		private ClassLog classLog;
		private String studentClassName;
		private String teacherClassName;
		private String book;
		
		public String getBook() {
			return book;
		}
		public void setBook(String book) {
			this.book = book;
		}
		public OneClass getOneClass() {
			return oneClass;
		}
		public void setOneClass(OneClass oneClass) {
			this.oneClass = oneClass;
		}
		public ClassLog getClassLog() {
			return classLog;
		}
		public void setClassLog(ClassLog classLog) {
			this.classLog = classLog;
		}
		public String getStudentClassName() {
			return studentClassName;
		}
		public void setStudentClassName(String studentClassName) {
			this.studentClassName = studentClassName;
		}
		public String getTeacherClassName() {
			return teacherClassName;
		}
		public void setTeacherClassName(String teacherClassName) {
			this.teacherClassName = teacherClassName;
		}
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		Date baseDate = null;
		try{
			baseDate =  Util.parseDate(req.getParameter("baseDate"));
		}catch(RuntimeException e){
			resp.sendRedirect("./adminDailyClass.view?baseDate="+Util.dateFormatting(new Date()));
			return;
		}
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		
		Calendar calendar = Calendar.getInstance();
		req.setAttribute("baseDate", Util.dateFormatting(baseDate));
		calendar.setTime(baseDate);
		calendar.add(Calendar.DATE, -1);
		req.setAttribute("prev", Util.dateFormatting(calendar.getTime()));
		calendar.setTime(baseDate);
		calendar.add(Calendar.DATE, +1);
		req.setAttribute("next", Util.dateFormatting(calendar.getTime()));
		
		if(holidayDAO.checkHoliday(baseDate))
		{
			req.setAttribute("holiday", true);
			req.getRequestDispatcher("./admin_daily_lecture.jsp").forward(req, resp);
			return;
		}
		else{
			req.setAttribute("holiday", false);
		}
		
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		
		List<DailyClassVO> result = new ArrayList<>();
		List<Lecture> lectureList = lectureDAO.getLectureListByRange(baseDate, baseDate);
		
		for(Iterator<Lecture> iterator = lectureList.iterator(); iterator.hasNext();){
			Lecture lecture = iterator.next();
			List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			lecture.setEndDate(oneClassList, classLogDAO, holidayDAO);
			if(lecture.getEndDate().before(baseDate))
				iterator.remove();
		}
		
		for(Lecture lecture : lectureList){
			List<OneClass> oneClassList = oneClassDAO.getOneClassListByDayOfWeek(lecture.getId(), DayOfWeek.getInstanceFromDate(baseDate));
			for(OneClass oneClass : oneClassList){
				DailyClassVO vo = new DailyClassVO();
				vo.setOneClass(oneClass);
				vo.setClassLog(classLogDAO.getClassLogByOneClassTransaction(oneClass.getId(), baseDate));
				vo.setStudentClassName(studentDAO.findNameById(oneClass.getStudentId()));
				vo.setTeacherClassName(teacherDAO.findClassNameById(oneClass.getTeacherId()));
				vo.setBook(lecture.getBook());
				result.add(vo);
			}
		}
		
		
		
		
		req.setAttribute("classList", result);
		req.setAttribute("count", new Count());
		
		
		req.getRequestDispatcher("./admin_daily_lecture.jsp").forward(req, resp);
	}

	
}
