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
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Student.Level;
import Kakao.kakaoeng.domain.model.Teacher.Rate;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminChagneStudentAdminInfo.edit")
public class AdminChangeStudentAdminInfoServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		Level level = Level.valueOf(req.getParameter("level"));
		int coupon = Integer.parseInt(req.getParameter("coupon"));
		String note = req.getParameter("note");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		
		String studentId = req.getParameter("studentId");
		Student student = studentDAO.findStudentById(studentId);
		if(student == null){
			Util.sendError(resp, studentId + " is not valid or not exist");
			return;
		}
		
		studentDAO.updateLevel(studentId, level);
		studentDAO.updateCoupon(studentId, coupon);
		studentDAO.setNote(student.getId(), note);
		
		Util.sendSuccess(resp, "성공적으로 완료 되었습니다.");
	}

}
