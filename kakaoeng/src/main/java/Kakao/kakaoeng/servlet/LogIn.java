package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import Kakao.kakaoeng.HttpServletDataGetterFromRegister;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.field.IdField;
import Kakao.kakaoeng.field.PasswordField;

@SuppressWarnings("serial")
@WebServlet("/teacher/login.do")
@ContextConfiguration("../beanConfig.xml")
public class LogIn extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("./LogIn.jsp");
	}

	public static boolean isLogInTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("teacher");
		if(user != null)
		{
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(isLogInTeacher(req, resp)){
			resp.sendRedirect("./index.jsp");
			return;
		}
		
		List<String> messages = new ArrayList<>();
		HttpServletDataGetterFromRegister dataGetter = new HttpServletDataGetterFromRegister(req);
		
		IdField<String> id = new IdField<String>("id", dataGetter, String.class);
		PasswordField pw = new PasswordField("pw", dataGetter, String.class);
		pw.setPasswordConfirm(pw.getOriginalPW());
		
		messages.addAll(id.checkValidation());
		messages.addAll(pw.checkValidation());
		
		if(!messages.isEmpty())
		{
			req.setAttribute("messages", messages);
			req.getRequestDispatcher("./log_in.jsp").forward(req, resp);
			return;
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		Teacher teacher = teacherDAO.getTeacherWithIdAndPw(id.getValue(), pw.getValue());
		if(teacher == null){
			messages.add("Log in Fail. Check ID and Password");
			req.setAttribute("messages", messages);
			req.getRequestDispatcher("./log_in.jsp").forward(req, resp);
			return;
		}
		else if(teacher.getConfirm() == false){
			messages.add("Insky is reviewing your registration request.\nEmail will be sent with the result.");
			req.setAttribute("messages", messages);
			req.getRequestDispatcher("./log_in.jsp").forward(req, resp);
			return;
		}
		else{
			req.getSession().setAttribute("teacher", teacher);
		}
		
		resp.sendRedirect("./index.jsp");
	}
}
