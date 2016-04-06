package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Mail;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.BookManager.bookVO;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;

@WebServlet("/teacher/adminLevelTestAlign.do")
public class AdminLevelTestAlignSerlvet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		
		int lectureId = Integer.parseInt(req.getParameter("lectureId"));
		boolean align = Boolean.parseBoolean(req.getParameter("align"));
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		
		lectureDAO.updateAlign(lectureId, align);
		
		Lecture lecture = lectureDAO.getLectureByIdTransaction(lectureId);
		
		String mode = environDAO.getLevelTestMailMode();
		if(mode.equals("auto")){
			Teacher teacher = teacherDAO.getTeacherWithId(lecture.getTeacherId());
			Student student = studentDAO.findStudentById(lecture.getStudentId());
			bookVO vo = new bookVO(teacher, lecture, student, null);
			String mailMsg = Util.fillContent(environDAO.getLevelTestMessage(), vo);
			Mail.send(Mail.username, lecture.getTeacherId(), environDAO.getLevelTestMailSubject(), mailMsg);
		}
		
		Util.sendSuccess(resp, "배정 여부를 변경하였습니다.");
	}

}
