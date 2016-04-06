package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Checker;
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
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.StudentSchedule;
import Kakao.kakaoeng.domain.model.Lecture.Status;
import Kakao.kakaoeng.domain.model.StudentSchedule.StudentClassState;

@SuppressWarnings("serial")
@WebServlet("/m5/studentSchedule.view")
public class StudentScheduleServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Calendar calendar = Calendar.getInstance();
		
		Student student = (Student) req.getSession().getAttribute("student");
		if(student == null){
			resp.sendRedirect("./main/main.jsp");
			return;
		}
		
		String yearString = req.getParameter("year");
		String monthString = req.getParameter("month");
		
		int year = calendar.get(Calendar.YEAR); 
		int month = calendar.get(Calendar.MONTH)+1;
		int date = calendar.get(Calendar.DATE);
		int purchaseNumber = -1;
		boolean reloadFlag = false;
		try{year = Integer.parseInt(yearString);}catch(NumberFormatException e){reloadFlag=true;}
		try{month = Integer.parseInt(monthString);}catch(NumberFormatException e){reloadFlag=true;}
		try{purchaseNumber = Integer.parseInt(req.getParameter("purchaseNumber"));}catch(NumberFormatException e){}
		if(reloadFlag){
			resp.sendRedirect("./studentSchedule.view?year="+year+"&month="+month+"&purchaseNumber="+purchaseNumber);
			return;
		}
		
		Date firstOfMonth = Util.getFirstDateOfMonth(year, month);
		Date lastOfMonth = Util.getLastDateOfMonth(year, month);

		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		
		
		
		List<Lecture> lectureList = lectureDAO.getLectureListByStudentId(student.getId());
		for(Iterator<Lecture> iter = lectureList.iterator(); iter.hasNext();){
			Lecture lecture = iter.next();
			if(!lecture.getStatus().equals(Status.OnGoing))
				iter.remove();
		}
		if(purchaseNumber == -1 && lectureList.size() >= 1)
			purchaseNumber = lectureList.get(0).getPurchaseNumber();
		Lecture lecture = lectureDAO.getOnGoingLectureByPurchase(purchaseNumber);
		
		if(lectureList.size() == 0){
			resp.sendRedirect("./student_empty_schedule.jsp");
			return;
		}
		
		
		Map<String, ClassLog> classLogMap = new HashMap<>();
		Map<OneClass, List<ClassLog>> map = new HashMap<>();
		List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
		Checker customCalendarCodeList = new Checker();
		for(OneClass oneClass : oneClassList){
			List<ClassLog> classLogList = classLogDAO.getClassLogListByParent(oneClass.getParent(), firstOfMonth, lastOfMonth);
			oneClass.setStartDate(lecture);
			map.put(oneClass, classLogList);
			customCalendarCodeList.add(oneClass);
			oneClass.setDone(classLogList);
			for(ClassLog element : classLogList){
				classLogMap.put(Util.dateFormatting(element.getClassDate()), element);
			}
		}
		lecture.setDone(oneClassList);
		lecture.setEndDate(oneClassList, classLogDAO, holidayDAO);
		
		List<Date> fullCalendar = Util.getFullMonthCalendar(year, month);
		List<StudentSchedule> scheduleList = new ArrayList<>();
		for(Date element : fullCalendar){
			if(element.before(firstOfMonth) || element.after(lastOfMonth)){
				scheduleList.add(new StudentSchedule(element, null, month));
				continue;
			}
			ClassLog classLog = classLogMap.get(Util.dateFormatting(element));
			StudentClassState state = null;
			if(classLog != null){
				switch (classLog.getClassState()) {
				case Completed:
					state = StudentClassState.Completed;
					break;
				case PostponeStudent:
					state = StudentClassState.PostponeStudent;
					break;
				case PostponeTeacher:
					state = StudentClassState.PostponeTeacher;
					break;
				case AbsentStudent:
					state = StudentClassState.AbsentStudent;
					break;
				case AbsentTeacher:
					state = StudentClassState.AbsentTeacher;
					break;
				case Uncompleted:
				case Uncompleted_0:
				case Uncompleted_100:
				case Uncompleted_30:
				case Uncompleted_50:
					state = StudentClassState.Uncompleted;
					break;
				default:
					break;
				}
			}
			boolean start = false;
			if(Util.dateFormatting(lecture.getStartDate()).equals(element))
				start = true;
			scheduleList.add(new StudentSchedule(element, state, month, start));
		}
		
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		
		lecture.loadStudent(studentDAO);
		lecture.loadTeacher(teacherDAO);

		req.setAttribute("lecture", lecture);
		req.setAttribute("lectureList", lectureList);
		req.setAttribute("scheduleList", scheduleList);
		req.setAttribute("customCalendarCodeList", customCalendarCodeList);
		req.setAttribute("today", Util.dateFormatting(new Date()));
		req.setAttribute("holidayDAO", applicationContext.getBean("holidayDAO"));
		
		req.getRequestDispatcher("./student_schedule.jsp?year="+year+"&month="+month+"&purchaseNumber="+purchaseNumber).forward(req, resp);
	}
}
