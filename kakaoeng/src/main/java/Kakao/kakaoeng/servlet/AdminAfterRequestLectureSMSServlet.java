package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.SMS;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.servlet.AdminBeforeRequestLectureSMSServlet.DifferVO;

@WebServlet("/teacher/adminAfterRequestLectureSMS.do")
public class AdminAfterRequestLectureSMSServlet extends HttpServlet {

	static Logger logger = Logger.getLogger(AdminAfterRequestLectureSMSServlet.class);
	
	public static class DifferVO{
		int differ;

		public DifferVO(int differ) {
			super();
			this.differ = differ;
		}

		public int getDiffer() {
			return differ;
		}
		
	}
	
	public static List<Lecture> getList(ApplicationContext applicationContext, boolean afterNotified){
		List<Lecture> result = new ArrayList<>();
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		List<Lecture> lectureList = lectureDAO.getDoneWithAfterNotifiedLectureListTransaction(afterNotified);
		int dayDiffer = environDAO.getBeforeRequestLectureDiffer();
		Calendar cal = Calendar.getInstance();
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		Date today = new Date();
		for(Lecture lecture : lectureList){
			if(lecture.getBeforeNotified() == true)
				continue;
			List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			lecture.setEndDate(oneClassList, classLogDAO, holidayDAO);
			cal.setTime(lecture.getEndDate());
			cal.add(Calendar.DATE, dayDiffer*-1);
			Date criteria = cal.getTime();
			if(criteria.equals(today) || criteria.before(today)){
				lecture.loadStudent(studentDAO);
				lecture.loadTeacher(teacherDAO);
				result.add(lecture);
			}
		}
		
		return result;
	}
	
	public static void auto(ApplicationContext applicationContext){
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		String mode = environDAO.getRequestLectureSMSMode();
		if(!mode.equals("auto")){
			logger.info("AdminBeforeRequestLectureSMSServlet mode is " + mode);
			return;
		}
		String msg = environDAO.getRequestLectureSMSMessage();
		List<Lecture> lectureList = getList(applicationContext, false);
		int dayDiffer = environDAO.getBeforeRequestLectureDiffer();
		DifferVO vo = new DifferVO(dayDiffer);

		for(Lecture lecture : lectureList){
			String smsMsg = Util.fillContent(msg, vo);
			SMS.sms(lecture.getStudent().getCellPhone(), smsMsg);
			lectureDAO.updateBeforeNotified(lecture.getId(), true);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("tacherDAO");
		String mode = environDAO.getAfterRequestLectureSMSMode();
		if(!mode.equals("auto")){
			logger.info("AdminAfeterRequestLectureSMSServlet mode is " + mode);
			return;
		}
		String msg = environDAO.getAfterRequestLectureSMSMessage();
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		List<Lecture> lectureList = lectureDAO.getDoneAndNotNotifiedLectureListTransaction();
		int dayDiffer = 3;
		DifferVO vo = new DifferVO(dayDiffer);
		Calendar cal = Calendar.getInstance();
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		Date today = new Date();
		for(Lecture lecture : lectureList){
			if(lecture.getBeforeNotified() == true)
				continue;
			List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			lecture.setEndDate(oneClassList, classLogDAO, holidayDAO);
			cal.setTime(lecture.getEndDate());
			cal.add(Calendar.DATE, dayDiffer);
			Date criteria = cal.getTime();
			if(criteria.equals(today) || criteria.before(today)){
				lecture.loadStudent(studentDAO);
				lecture.loadTeacher(teacherDAO);
				String smsMsg = Util.fillContent(msg, vo);
				SMS.sms(lecture.getStudent().getCellPhone(), smsMsg);
				lectureDAO.updateBeforeNotified(lecture.getId(), true);
			}
		}
		*/
	}

}
