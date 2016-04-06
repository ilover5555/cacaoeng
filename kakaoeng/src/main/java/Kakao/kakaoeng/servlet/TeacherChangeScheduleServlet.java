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

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/teacherChangeSchedule.do")
public class TeacherChangeScheduleServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Teacher t = (Teacher) req.getSession().getAttribute("teacher");
		if(t == null)
		{
			Util.sendError(403, resp, "Your login session is expired.\nLogin Again and try again.");
			return;
		}
		
		String o =  req.getParameter("classData");
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject)jsonParser.parse(o);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray avails =  (JSONArray)jsonObject.get("avail");
		JSONArray clears =  (JSONArray)jsonObject.get("clear");
		
		List<String> availList = new ArrayList<String>();
		List<String> clearList = new ArrayList<>();
		
		for(int i=0; i<avails.size(); i++)
			availList.add((String)avails.get(i));
		
		for(int i=0; i<clears.size(); i++)
			clearList.add((String)clears.get(i));
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		ClassTimeDAO classTimeDAO = (ClassTimeDAO) applicationContext.getBean("classTimeDAO");
		
		for(int i=0; i<availList.size(); i++)
		{
			ClassTime ct = new ClassTime(t.getId(), availList.get(i));
			int id = classTimeDAO.findIdFromClassTimeInstance(ct);
			ct.setId(id);
			if(id == -1)
				classTimeDAO.register(ct);
			else{
				classTimeDAO.enable(ct);
			}
		}
		
		for(int i=0; i<clearList.size(); i++)
		{
			ClassTime ct = new ClassTime(t.getId(), clearList.get(i));
			int id = classTimeDAO.findIdFromClassTimeInstance(ct);
			ct.setId(id);
			if(id == -1){
				throw new RuntimeException("Teacher " + ct.getTeacherId() + "'s " + ct.getStamp() + " is not registered. But requires disble.");
			}
			else{
				
				classTimeDAO.disable(ct);
			}
		}
		
		
		
		resp.setContentType("application/json; charset=UTF-8");
		resp.getWriter().write("{\"SUCCESS\" : \"SUCCESs\"}");
	}

}
