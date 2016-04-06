package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PayDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Pay;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.servlet.TeacherSalaryViewServlet.VO;

@WebServlet("/teacher/adminSalary.view")
public class AdminSalaryViewServlet extends HttpServlet {

	public static class AdminSalaryVO{
		List<VO> voList;
		int lectureCount=0;
		int classTime=0;
		int workTime = 0;
		double deduct=0;
		Teacher teacher=null;
		boolean payed=false;

		@Override
		public String toString(){
			return String.format("%s workTime : %d deduct : %lf", teacher.getClassName(), this.getWorkTime(), this.getDeduct());
		}
		
		public void setPayed(boolean payed){
			this.payed = payed;
		}
		
		public boolean getPayed(){
			return payed;
		}
		public void setVoList(List<VO> voList) {
			this.voList = voList;
		}
		
		public void fillContent(){
			for(VO vo : voList){
				vo.fillContent();
				classTime += vo.getClassTime();
				workTime += vo.getWorkTime();
				deduct += vo.getDeduct();
			}
			lectureCount = voList.size();
		}

		public double getPayment(){
			return ((workTime - deduct)*teacher.getSalary())/60;
		}
		
		public Teacher getTeacher() {
			return teacher;
		}

		public void setTeacher(Teacher teacher) {
			this.teacher = teacher;
		}

		public List<VO> getVoList() {
			return voList;
		}

		public int getLectureCount() {
			return lectureCount;
		}

		public int getClassTime() {
			return classTime;
		}

		public int getWorkTime() {
			return workTime;
		}

		public double getDeduct() {
			return deduct;
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		
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
		
		List<AdminSalaryVO> result = new ArrayList<>();
		List<Teacher> teacherList = teacherDAO.getConfirmedTeacherList();
		
		PayDAO payDAO = (PayDAO) applicationContext.getBean("payDAO");
		
		for(Teacher teacher : teacherList){
			List<Lecture> lectureList = lectureDAO.getLectureListByTeacherId(teacher.getId(), last, first);
			List<VO> voList = new ArrayList<>();
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
				voList.add(vo);
			}
			
			AdminSalaryVO adminSalaryVO = new AdminSalaryVO();
			adminSalaryVO.setVoList(voList);
			adminSalaryVO.fillContent();
			adminSalaryVO.setTeacher(teacher);
			Pay pay = payDAO.getPayListByTeacherIdAndMonth(teacher.getId(), month);
			adminSalaryVO.setPayed(pay.getPayed());
			result.add(adminSalaryVO);
		}
		
		for(Iterator<AdminSalaryVO> iter = result.iterator(); iter.hasNext();){
			AdminSalaryVO vo = iter.next();
			if(vo.getVoList() == null || vo.getVoList().size() == 0)
				iter.remove();
		}
		
		double total = 0;
		for(AdminSalaryVO vo : result){
			total += vo.getPayment();
		}
		
		req.setAttribute("result", result);
		req.setAttribute("month", month);
		req.setAttribute("year", year);
		req.setAttribute("count", new Count());
		if(month == calendar.get(Calendar.MONTH)+1)
			req.setAttribute("end", true);
		else
			req.setAttribute("end", false);
		
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		req.setAttribute("USD", environDAO.getUSD());
		req.setAttribute("total", total);
		
		req.getRequestDispatcher("./admin_salary.jsp").forward(req, resp);
	}

}
