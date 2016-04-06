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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.TeacherDAO;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminTeacherRepresentitive.edit")
public class AdminTeacherRepresentitve extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
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
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		for(String teacherId : selectedList){
			teacherDAO.updateRepresentitive(teacherId, true);
		}
		
		Util.sendSuccess(resp, "선택된 선생님들을 대표강사로 설정했습니다.");
	}

}
