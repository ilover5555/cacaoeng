package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

@WebServlet("/getStudentListByClassDate.get")
public class GetStudentListByClassDateServlet extends HttpServlet {

	static List<Student> getList(ApplicationContext applicationContext, Date date){
		DayOfWeek dayOfWeek = DayOfWeek.getInstanceFromDate(date);
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		List<Lecture> lectureList = lectureDAO.getLectureListByRange(date, date);
		
		Set<Student> studentSet = new HashSet<>();
		for(Lecture lecture : lectureList){
			List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			for(OneClass oneClass : oneClassList){
				if(oneClass.getDuration().getRt().getDayOfWeek().equals(dayOfWeek)){
					studentSet.add(studentDAO.findStudentById(oneClass.getStudentId()));
				}
			}
		}
		
		List<Student> list = new ArrayList<>();
		
		for(Iterator<Student> iter = studentSet.iterator(); iter.hasNext(); ){
			list.add(iter.next());
		}
		
		return list;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Date date = Util.parseDate(req.getParameter("baseDate"));
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		List<Student> list = getList(applicationContext, date);
		
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<list.size(); i++){
			sb.append(list.get(i).getName());
			sb.append("(");
			sb.append(list.get(i).getCellPhone());
			sb.append(")");
			if(i < list.size()-1)
				sb.append(",");
		}
		
		JSONObject obj = new JSONObject();
		obj.put("number", list.size());
		obj.put("list", sb.toString());
		
		Util.sendSuccess(resp, obj.toJSONString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		Date date = Util.parseDate(req.getParameter("baseDate"));
		List<Student> list = getList(applicationContext, date);
		
		req.setAttribute("list", list);
		req.setAttribute("msg", environDAO.getAllSMSMessage());
		
		req.getRequestDispatcher("./adminSMS.send").forward(req, resp);
	}

	
	
}
