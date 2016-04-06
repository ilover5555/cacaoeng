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

@WebServlet("/teacher/teacherSalaryDetail.view")
public class TeacherSalaryDetailServlet extends HttpServlet {

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
	
	public static void process(HttpServletRequest req, HttpServletResponse resp, Teacher teacher){
		int lectureId = Integer.parseInt(req.getParameter("lectureId"));
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		
		int year = 0;
		int month = 0;
		
		try{
			year = Integer.parseInt(req.getParameter("year"));
		}catch(NumberFormatException e){
			year = calendar.get(Calendar.YEAR);
		}
		
		try{
			month = Integer.parseInt(req.getParameter("month"));
			if(month > 12){
				year++;
				month = month % 12;
			}
			if(month <= 0){
				year--;
				month = month + 12;
			}
		}catch(NumberFormatException e){
			month = calendar.get(Calendar.MONTH)+1;
		}
		
		Date selected = Util.getFirstDateOfMonth(year, month);
		Date monthLast = Util.getFirstDateOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1);
		if(selected.after(monthLast)){
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH)+1;
		}
		
		Date first = Util.getFirstDateOfMonth(year, month);
		Date last = Util.getLastDateOfMonth(year, month);
		
		DetailSalaryVO total = new DetailSalaryVO();
		
		List<DetailSalaryVO> result  = new ArrayList<>();
		
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		
		Lecture lecture = lectureDAO.getLectureByIdTransaction(lectureId);
		lecture.loadStudent(studentDAO);
		lecture.loadTeacher(teacherDAO);
		List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lectureId);
		for(OneClass oneClass : oneClassList){
			List<ClassLog> classLogList = classLogDAO.getClassLogListByOneClassTransaction(oneClass.getId(), first, last);
			int workTime = oneClass.getDuration().getDuration() * 25;
			for(ClassLog classLog : classLogList){
				double deduct = (1-classLog.getClassState().getRate())*workTime;
				DetailSalaryVO vo = new DetailSalaryVO(classLog, oneClass, workTime, deduct);
				total.setWorkTime(total.getWorkTime() + vo.getWorkTime());
				total.setDeduct(total.getDeduct() + vo.getDeduct());
				result.add(vo);
			}
		}
		
		req.setAttribute("result", result);
		req.setAttribute("total", total);
		req.setAttribute("lecture", lecture);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Teacher teacher = (Teacher) req.getSession().getAttribute("teacher");
		if(teacher == null){
			Util.sendError(resp, "Expired login session.\nLogin again and retry");
			return;
		}
			
		process(req, resp, teacher);
		
		req.getRequestDispatcher("./teacher_salary_detail.jsp").forward(req, resp);
	}

}
