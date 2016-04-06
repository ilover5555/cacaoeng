package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.UserDAO;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.field.GeneralField;
import Kakao.kakaoeng.validator.IdValidator;

@SuppressWarnings("serial")
@WebServlet("/studentLogin.do")
public class StudentLoginServlet extends HttpServlet {

	public static void sendError(HttpServletResponse resp, String error) throws IOException{
		resp.setStatus(500);
		resp.setContentType("text/plain");
		resp.getWriter().write(error);
	}
	
	public static void sendSuccess(HttpServletResponse resp, String msg) throws IOException{
		resp.setStatus(200);
		resp.setContentType("text/plain");
		resp.getWriter().write(msg);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		String pw = req.getParameter("pw");
		
		Field<String> idField = new GeneralField<String>("id", id);
		Field<String> pwField = new GeneralField<String>("pw", pw);
		
		idField.setbEnglish(false);
		pwField.setbEnglish(false);
		
		idField.addValidator(new IdValidator<>());
		pwField.addValidator(new IdValidator<>());
		
		List<String> messages = new ArrayList<>();
		
		messages.addAll(idField.checkValidation());
		messages.addAll(pwField.checkValidation());
		
		if(messages.size() > 0){
			String error="";
			for(String msg : messages){
				error += msg + "\n";
			}
			sendError(resp, error);
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		UserDAO userDAO = (UserDAO) applicationContext.getBean("userDAO");
		Student student = studentDAO.findStudentByIdAndPw(idField.getValue(), DigestUtils.sha256Hex(pwField.getValue()));
		if(student != null){
			userDAO.updateLastLogin(student.getId(), student.getPw(), student.getUserType());
			req.getSession().setAttribute("student", student);
			sendSuccess(resp, "로그인에 성공했습니다.");
			return;
		}
		else{
			sendError(resp, "아이디 또는 비밀번호를 확인하세요.");
			return;
		}
	}

}
