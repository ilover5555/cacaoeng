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
@WebServlet("/teacher/adminRequestSMSPopup.do")
public class AdminRquestSMSPopupServlet extends HttpServlet {

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
		
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<selectedList.size() ; i++){
			Student student = studentDAO.findStudentById(selectedList.get(i));
			sb.append(student.getName());
			sb.append('(');
			sb.append(student.getCellPhone());
			sb.append(')');
			if(i< selectedList.size()-1)
				sb.append(',');
		}
		
		JSONObject obj = new JSONObject();
		
		obj.put("sendList", sb.toString());
		obj.put("selectedJSON", selectedString);
		
		Util.sendSuccess(resp, obj.toJSONString());
	}

}
