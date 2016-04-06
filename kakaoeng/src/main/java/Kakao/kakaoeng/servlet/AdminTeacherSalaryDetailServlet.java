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

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Teacher;

@WebServlet("/teacher/adminTeacherSalaryDetail.view")
public class AdminTeacherSalaryDetailServlet extends HttpServlet {

	public static class DetailSalaryVO{
		ClassLog classLog;
		OneClass oneClass;
		int workTime=0;
		double deduct=0;
		public DetailSalaryVO() {
			super();
		}
		
		public DetailSalaryVO(ClassLog classLog, OneClass oneClass, int workTime, double deduct) {
			super();
			this.classLog = classLog;
			this.oneClass = oneClass;
			this.workTime = workTime;
			this.deduct = deduct;
		}
		public ClassLog getClassLog() {
			return classLog;
		}
		public void setClassLog(ClassLog classLog) {
			this.classLog = classLog;
		}
		public OneClass getOneClass() {
			return oneClass;
		}
		public void setOneClass(OneClass oneClass) {
			this.oneClass = oneClass;
		}
		public int getWorkTime() {
			return workTime;
		}
		public void setWorkTime(int workTime) {
			this.workTime = workTime;
		}
		public double getDeduct() {
			return deduct;
		}
		public void setDeduct(double deduct) {
			this.deduct = deduct;
		}
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String teacherId = req.getParameter("teacherId");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		
		Teacher teacher = teacherDAO.getTeacherWithId(teacherId);
		if(teacher == null){
			Util.sendError(resp, "Invalid teacherId : " + teacherId);
			return;
		}
		
		TeacherSalaryDetailServlet.process(req, resp, teacher);
		
		req.getRequestDispatcher("./teacher_salary_detail.jsp").forward(req, resp);
	}

}
