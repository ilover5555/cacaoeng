package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.field.GeneralField;
import Kakao.kakaoeng.validator.IdValidator;

@SuppressWarnings("serial")
@WebServlet("/duplicateIdCheck.do")
public class DuplicateIdCheckServlet extends HttpServlet {

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		Field<String> idField = new GeneralField<String>("id", id);
		idField.setbEnglish(false);
		idField.addValidator(new IdValidator<>());
		List<String> msg = idField.checkValidation();
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		
		Student student = studentDAO.findStudentById(idField.getValue());
		
		if(msg.size()>0){
			String error="";
			for(String m : msg)
				error += m + "\n";
			Util.sendError(resp, error);
			return;
		}
		
		if(student == null){
			Util.sendSuccess(resp, "사용 가능한 아이디입니다.");
			return;
		}
		else{
			Util.sendError(resp, "사용 불가능한 아이디입니다.");
			return;
		}
	}

}
