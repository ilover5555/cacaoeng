package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.HttpServletDataGetterFromRegister;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/registerTeacher.do")
@MultipartConfig
public class RegisterTeacher extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		HttpServletDataGetterFromRegister dataGetter = new HttpServletDataGetterFromRegister(req);
		Teacher teacher = (Teacher) dataGetter.makeTeacherInstance();
		List<String> messages = dataGetter.getMessages();
		
		if(teacherDAO.findTeacherByClssName(teacher.getClassName()))
			messages.add("Class Name is already used.");
		
		if(!messages.isEmpty())
		{
			JSONObject obj = new JSONObject();
			JSONArray array = new JSONArray();
			for(String msg : messages)
				array.add(msg);
			obj.put("msg", array);
			Util.sendError(resp, obj.toJSONString());
			return;
		}
		
		try{
			teacherDAO.register(teacher);
		}catch(RuntimeException e){
			messages.add("Unknown exception occured. While registering.");
			JSONObject obj = new JSONObject();
			JSONArray array = new JSONArray();
			for(String msg : messages)
				array.add(msg);
			obj.put("msg", array);
			Util.sendError(resp, obj.toJSONString());
			return;
		}
		Util.sendSuccess(resp, "Welcome. Your registeration will be commited soon by administarator.");
	}

}
