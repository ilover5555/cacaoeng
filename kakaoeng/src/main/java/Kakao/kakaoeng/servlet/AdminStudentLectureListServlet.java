package Kakao.kakaoeng.servlet;

import java.io.IOException;
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

import Kakao.kakaoeng.Count;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminStudentLectureList.view")
public class AdminStudentLectureListServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		String studentId = req.getParameter("studentId");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		Student student = studentDAO.findStudentById(studentId);
		if(student == null){
			Util.sendError(resp, studentId + "is inavlid or dont exit.");
			return;
		}
		
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		List<Lecture> lectureList = lectureDAO.getLectureListByStudentId(student.getId());
		
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		Map<Integer, Integer> absentMap = new HashMap<>();
		
		for(Iterator<Lecture> iter = lectureList.iterator(); iter.hasNext(); ){
			Lecture lecture = iter.next();
			
			lecture.loadPurchase(purchaseDAO);
			if(lecture.isPurchaseLoaded() == false)
				iter.remove();
			lecture.loadTeacher(teacherDAO);
			
			int absence=0;
			List<ClassLog> classLogList = classLogDAO.getAllClassLogListByLectureId(lecture.getId());
			for(ClassLog classLog : classLogList){
				if(classLog.getClassState().equals(ClassState.AbsentStudent))
					absence++;
			}
			absentMap.put(lecture.getId(), absence);
		}

		Count count = new Count();
		count.setIndex(1);
		
		req.setAttribute("lectureList", lectureList);
		req.setAttribute("absentMap", absentMap);
		req.setAttribute("count", count);
		
		req.getRequestDispatcher("./admin_student_lecture_list.jsp").forward(req, resp);
	}

}
