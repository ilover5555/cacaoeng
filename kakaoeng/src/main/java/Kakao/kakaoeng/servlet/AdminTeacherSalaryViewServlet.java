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

import Kakao.kakaoeng.Count;
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

@SuppressWarnings("serial")
@WebServlet("/teacher/adminTeacherSalary.view")
public class AdminTeacherSalaryViewServlet extends HttpServlet {

	public static class VO{
		Lecture lecture;
		Map<OneClass, List<ClassLog>> map;
		int time = -1;
		int workTime = 0;
		int deduct = 0;
		double hour = 0;
		double payment = 0;
		int classTime=0;
		public void fillContent(){
			fillTime();
			hour = (workTime - deduct)/60;
			payment = ((workTime-deduct) * lecture.getTeacher().getSalary()) / 60;
		}
		
		public void setTime(int time) {
			this.time = time;
		}

		public void setWorkTime(int workTime) {
			this.workTime = workTime;
		}

		public void setDeduct(int deduct) {
			this.deduct = deduct;
		}

		public void setHour(double hour) {
			this.hour = hour;
		}

		public void setPayment(double payment) {
			this.payment = payment;
		}

		public void setClassTime(int classTime) {
			this.classTime = classTime;
		}

		public void fillTime(){
			for(OneClass oneClass : map.keySet()){
				int duration = oneClass.getDuration().getDuration()*25;
				if(time == -1){
					time = duration;
				}
				
				if(time != duration){
					time = -2;
				}
				
				List<ClassLog> classLogList = map.get(oneClass);
				for(ClassLog classLog : classLogList){
					workTime += duration;
					deduct += (1-classLog.getClassState().getRate())*duration;
					classTime++;
				}
			}
		}
		public int getClassTime(){
			return classTime;
		}
		public Lecture getLecture() {
			return lecture;
		}
		public void setLecture(Lecture lecture) {
			this.lecture = lecture;
		}
		
		public Map<OneClass, List<ClassLog>> getMap() {
			return map;
		}

		public void setMap(Map<OneClass, List<ClassLog>> map) {
			this.map = map;
		}

		public int getTime() {
			return time;
		}
		public int getWorkTime() {
			return workTime;
		}
		public int getDeduct() {
			return deduct;
		}
		public double getHour() {
			return hour;
		}
		public double getPayment() {
			return payment;
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
			Util.sendError(resp, "Invalid teacher Id : " + teacherId);
			return;
		}
		
		TeacherSalaryViewServlet.process(req, resp, teacher);
		
		req.setAttribute("teacher", teacher);
		
		req.getRequestDispatcher("./admin_teacher_salary.jsp").forward(req, resp);;
	}

}
