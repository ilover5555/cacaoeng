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
@WebServlet("/teacher/adminTeacherRetire.edit")
public class AdminTeacherRetirementServlet extends HttpServlet {

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
		String retire = req.getParameter("retire");
		if(retire.equals("true")){
			for(String teacherId : selectedList)
				teacherDAO.updateRetirement(teacherId, true);
		}else if(retire.equals("false")){
			for(String teacherId : selectedList)
				teacherDAO.updateRetirement(teacherId, false);
		}
		else{
			Util.sendError(resp, "유효하지 않은 처리 명령어입니다. retire:"+retire);
			return;
		}
		
		Util.sendSuccess(resp, "체크된 선생님들을 퇴사대기상태로 설정했습니다.");
	}

}
