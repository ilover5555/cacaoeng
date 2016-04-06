package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Student;

@WebServlet("/getStudentListByLastLogion.get")
public class GetStudentListAfterLastLoginServlet extends HttpServlet {

	static List<Student> getList(StudentDAO studentDAO, Date date){
		return studentDAO.findStudentListAfterLastLogin(date);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO  = (StudentDAO) applicationContext.getBean("studentDAO");
		Date date = Util.parseDate(req.getParameter("baseDate"));
		List<Student> list = getList(studentDAO, date);
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
		StudentDAO studentDAO  = (StudentDAO) applicationContext.getBean("studentDAO");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		Date date = Util.parseDate(req.getParameter("baseDate"));
		List<Student> list = getList(studentDAO, date);
		
		req.setAttribute("list", list);
		req.setAttribute("msg", environDAO.getAllSMSMessage());
		
		req.getRequestDispatcher("./adminSMS.send").forward(req, resp);
	}

	
}
