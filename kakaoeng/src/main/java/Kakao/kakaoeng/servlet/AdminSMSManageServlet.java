package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Student;

@WebServlet("/teacher/adminSMSManage.view")
public class AdminSMSManageServlet extends HttpServlet {

	public static List<Student> getInLectureStudentList(ApplicationContext applicationContext){
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		List<Lecture> onGoingList = lectureDAO.getAllOnGoingLectureListTransaction();
		Set<Student> studentSet = new HashSet<>();
		
		for(Lecture lecture : onGoingList){
			studentSet.add(studentDAO.findStudentById(lecture.getStudentId()));
		}
		
		List<Student> result = new ArrayList<>();
		for(Iterator<Student> iter =  studentSet.iterator(); iter.hasNext(); ){
			Student student = iter.next();
			result.add(student);
		}
		
		return result;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		
		req.setAttribute("ALL_MESSAGE", environDAO.getAllSMSMessage());
		req.setAttribute("NEW_REGISTER_MESSAGE", environDAO.getNewRegisterSMSMessage());
		req.setAttribute("NEW_LECTURE_MESSAGE", environDAO.getNewLectureSMSMessage());
		req.setAttribute("REQUEST_LECTURE_MESSAGE", environDAO.getRequestLectureSMSMessage());
		req.setAttribute("QUERY_PW_MESSAGE", environDAO.getQueryPwSMSMessage());
		req.setAttribute("AFETER_REQUEST_LECTURE_MESSAGE", environDAO.getAfterRequestLectureSMSMessage());
		
		req.setAttribute("NEW_REGISTER_MODE", environDAO.getNewRegisterSMSMode());
		req.setAttribute("LECTURE_REGISTER_MODE", environDAO.getNewLectureSMSMode());
		req.setAttribute("REQUEST_LECTURE", environDAO.getRequestLectureSMSMode());
		req.setAttribute("AFTER_REQUEST_LECTURE_MODE", environDAO.getAfterRequestLectureSMSMode());
		
		req.setAttribute("LECTURE_REGISTER_DIFFER", environDAO.getBeforeRequestLectureDiffer());
		req.setAttribute("AFTER_LECTURE_REGISTER_DIFFER", environDAO.getAfterRequestLectureDiffer());
		
		req.setAttribute("inLectureStudentList", getInLectureStudentList(applicationContext));
		
		req.getRequestDispatcher("./admin_sms_manage.jsp").forward(req, resp);
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		List<Student> list = getInLectureStudentList(applicationContext);
		
		req.setAttribute("list", list);
		req.setAttribute("msg", environDAO.getAllSMSMessage());
		
		req.getRequestDispatcher("./adminSMS.send").forward(req, resp);
	}

}
