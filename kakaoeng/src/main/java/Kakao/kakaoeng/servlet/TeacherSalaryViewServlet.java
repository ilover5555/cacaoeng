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
@WebServlet("/teacher/teacherSalary.view")
public class TeacherSalaryViewServlet extends HttpServlet {

	public static class VO{
		Lecture lecture;
		Map<OneClass, List<ClassLog>> map;
		int time = -1;
		int workTime = 0;
		double deduct = 0;
		double hour = 0;
		double payment = 0;
		int classTime=0;
		
		@Override
		public String toString(){
			return String.format("%s workTime:%d deduct:%f", lecture.getTeacher().getClassName(), workTime, deduct);
		}
		
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

		public void setDeduct(double deduct) {
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
			time=-1;
			workTime = 0;
			deduct = 0;
			hour = 0;
			payment = 0;
			classTime = 0;
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
		public double getDeduct() {
			return deduct;
		}
		public double getHour() {
			return hour;
		}
		public double getPayment() {
			return payment;
		}
		
	}
	
	public static void process(HttpServletRequest req, HttpServletResponse resp, Teacher teacher){
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		
		
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
		List<VO> result = new ArrayList<>();
		List<Lecture> lectureList = lectureDAO.getLectureListByTeacherId(teacher.getId(), last, first);
		
		VO totalVO = new VO();
		totalVO.setClassTime(0);
		totalVO.setWorkTime(0);
		totalVO.setDeduct(0);
		totalVO.setHour(0);
		totalVO.setPayment(0);
		
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		for(Lecture lecture : lectureList){
			VO vo = new VO();
			lecture.loadStudent(studentDAO);
			lecture.loadTeacher(teacherDAO);
			Map<OneClass, List<ClassLog>> map = new HashMap<>();
			List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			for(OneClass oneClass : oneClassList){
				List<ClassLog> classLogList = classLogDAO.getClassLogListByOneClassTransaction(oneClass.getId(), first, last);
				map.put(oneClass, classLogList);
			}
			vo.setLecture(lecture);
			vo.setMap(map);
			vo.fillContent();

			result.add(vo);
		}
		
		for(VO vo : result){
			totalVO.setClassTime(totalVO.getClassTime() + vo.getClassTime());
			totalVO.setWorkTime(totalVO.getWorkTime() + vo.getWorkTime());
			totalVO.setHour(totalVO.getHour() + vo.getHour());
			totalVO.setPayment(totalVO.getPayment() + vo.getPayment());
		}
		
		if(month == calendar.get(Calendar.MONTH)+1)
			req.setAttribute("end", true);
		else
			req.setAttribute("end", false);
		req.setAttribute("year", year);
		req.setAttribute("month", month);
		req.setAttribute("result", result);
		req.setAttribute("count", new Count());
		req.setAttribute("total", totalVO);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Teacher teacher = (Teacher) req.getSession().getAttribute("teacher");
		if(teacher == null){
			resp.sendRedirect("./index.jsp");
			return;
		}
		
		process(req, resp, teacher);
		
		
		req.getRequestDispatcher("./teacher_salary.jsp").forward(req, resp);;
	}

}
