package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.DateValueObject;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminClassReport.do")
public class AdminClassReportServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		int classLogId = Integer.parseInt(req.getParameter("classLogId"));
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		ClassLog classLog = classLogDAO.getByIdTransaction(classLogId);
		String oneClassId = req.getParameter("oneClassId");
		OneClass oneClass = oneClassDAO.getOneClassByIdTransaction(Integer.parseInt(oneClassId));
		oneClass.loadLecture(lectureDAO);
		String date = req.getParameter("date");

		Teacher teacher = teacherDAO.getTeacherWithId(oneClass.getTeacherId());
		Student student = studentDAO.findStudentById(oneClass.getStudentId());
		
		DateValueObject dvo = new DateValueObject(Util.parseDate(date));
		req.setAttribute("dvo", dvo);
		req.setAttribute("teacher", teacher);
		req.setAttribute("student", student);
		req.setAttribute("oneClass", oneClass);
		req.setAttribute("classLog", classLog);
		req.setAttribute("mode", "admin");
		
		req.getRequestDispatcher("./class_report.jsp").include(req, resp);
	}

}
