package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Student;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminHandleSMSPopup.do")
public class AdminHandleSMSPopupServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		String selectedString = req.getParameter("selected");
		JSONParser parser = new JSONParser();
		JSONArray jArray = null;
		try {
			jArray = (JSONArray) parser.parse(selectedString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> selectedList = new ArrayList<>();
		
		for(int i=0; i<jArray.size(); i++){
			selectedList.add((String) jArray.get(i));
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		
		List<Student> result = new ArrayList<>();
		for(String studentId : selectedList){
			Student student = studentDAO.findStudentById(studentId);
			result.add(student);
		}
		
		req.setAttribute("list", result);
		req.setAttribute("msg", req.getParameter("msg"));
		req.getRequestDispatcher("./adminSMS.send").forward(req, resp);
	}

}
