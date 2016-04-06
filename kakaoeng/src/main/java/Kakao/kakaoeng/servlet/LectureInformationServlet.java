package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

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
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/lectureInformation.view")
public class LectureInformationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Teacher t = (Teacher) req.getSession().getAttribute("teacher");
		if(t==null){
			resp.sendRedirect("./log_in.jsp");
			return;
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		
		String oneClassIdString = req.getParameter("oneClassId");
		int oneClassId = Integer.parseInt(oneClassIdString);
		
		OneClass oneClass = oneClassDAO.getOneClassByIdTransaction(oneClassId);
		
		Lecture lecture = lectureDAO.getLectureByIdTransaction(oneClass.getLectureId());
		
		List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
		List<ClassLog> classLogList = new ArrayList<>();
		
		Map<OneClass, List<ClassLog>> map = new HashMap<>();
		for(OneClass oc : oneClassList){
			List<ClassLog> list = classLogDAO.getAllClassLogListByOneClassTransaction(oc.getId());
			oc.setDone(classLogList);
			classLogList.addAll(list);
			map.put(oc, list);
		}
		
		lecture.setDone(oneClassList);
		lecture.setEndDate(oneClassList, classLogDAO, holidayDAO);
		
		int completed = 0;
		int absentTeacher = 0;
		int absentStudent = 0;
		int postponeStudent = 0;
		int postponeTeacher = 0;
		int uncompleted = 0;
		for(ClassLog cl : classLogList){
			switch(cl.getClassState()){
			case Completed:
				completed++;
				break;
			case AbsentTeacher:
				absentTeacher++;
				break;
			case AbsentStudent:
				absentStudent++;
				break;
			case PostponeStudent:
				postponeStudent++;
				break;
			case PostponeTeacher:
				postponeTeacher++;
				break;
			case Uncompleted:
			case Uncompleted_0:
			case Uncompleted_100:
			case Uncompleted_30:
			case Uncompleted_50:
				uncompleted++;
				break;
			default:
				break;
			}
		}
		
		lecture.loadStudent(studentDAO);
		lecture.loadTeacher(teacherDAO);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int currentYear = cal.get(Calendar.YEAR);
		cal.setTime(lecture.getStudent().getBirth());
		int birthYear =   cal.get(Calendar.YEAR);
		int age = currentYear  - birthYear;
		
		req.setAttribute("age", age);
		req.setAttribute("lecture", lecture);
		req.setAttribute("completed", completed);
		req.setAttribute("absentTeacher", absentTeacher);
		req.setAttribute("absentStudent", absentStudent);
		req.setAttribute("postponeStudent", postponeStudent);
		req.setAttribute("postponeTeacher", postponeTeacher);
		req.setAttribute("uncompleted", uncompleted);
		req.setAttribute("oneClassList", oneClassList);
		req.setAttribute("classLogList", classLogList);
		
		req.getRequestDispatcher("./lecture_information.jsp").include(req, resp);
	}

}
