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
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.Teacher.Rate;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminTeacherChangeRate.do")
public class AdminTeacherChangeRateServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String teacherId = req.getParameter("teacherId");
		Rate rate = Rate.valueOf(req.getParameter("rate"));
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		Teacher teacher = teacherDAO.getTeacherWithId(teacherId);
		if(teacher == null){
			Util.sendError(resp, teacherId + " is invalid or dont exists.");
			return;
		}
		
		try{
			teacherDAO.updateRate(teacherId, rate);
		}catch(RuntimeException e){
			Util.sendError(resp, e.getMessage());
			return;
		}
		
		Util.sendSuccess(resp, teacher.getClassName() + "의 등급을 "+rate+"로 조정했습니다.");
	}

}
