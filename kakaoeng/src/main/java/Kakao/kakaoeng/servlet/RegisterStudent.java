package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.DataGetter;
import Kakao.kakaoeng.FieldSet;
import Kakao.kakaoeng.HttpServletDataGetterFromRegister;
import Kakao.kakaoeng.SMS;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Student.Level;
import Kakao.kakaoeng.domain.model.User.Gender;
import Kakao.kakaoeng.domain.model.User.UserType;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.field.GeneralField;
import Kakao.kakaoeng.field.PasswordField;
import Kakao.kakaoeng.validator.DateValidator;
import Kakao.kakaoeng.validator.EmailValidator;
import Kakao.kakaoeng.validator.IdValidator;

@SuppressWarnings("serial")
@WebServlet("/registerStudent.do")
public class RegisterStudent extends HttpServlet {

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
		DataGetter dataGetter = new HttpServletDataGetterFromRegister(req);
		
		Field<String> idField = new GeneralField<>("id", dataGetter, String.class);
		idField.addValidator(new IdValidator<>());
		PasswordField pwField = new PasswordField("pw", dataGetter, String.class);
		pwField.setPasswordConfirm(dataGetter.get("pwConfirm", String.class));
		Field<String> nameField = new GeneralField<>("이름", dataGetter.get("name", String.class));
		Field<String> classNameField = new Field<>("수업이름", dataGetter.get("className", String.class));
		String emailId = dataGetter.get("emailId", String.class);
		String emailHost = dataGetter.get("emailHost", String.class);
		String email = String.format("%s@%s", emailId, emailHost);
		Field<String> emailField = new GeneralField<>("email", email);
		emailField.addValidator(new EmailValidator<>());
		Field<String> skypeField = new Field<>("skype", dataGetter, String.class);
		String phone1 = dataGetter.get("phone1", String.class);
		String phone2 = dataGetter.get("phone2", String.class);
		String phone3 = dataGetter.get("phone3", String.class);
		String phone = "";
		if(!(phone1.length() == 0) && !(phone2.length() == 0) && !(phone3.length() == 0))
			phone = phone1 + "-" + phone2 + "-" + phone3;
		Field<String> phoneField = new GeneralField<String>("연락처", phone);
		String yearString = dataGetter.get("year", String.class);
		String monthString = dataGetter.get("month", String.class);
		String dateString = dataGetter.get("date", String.class);
		String date = null;
		if(!yearString.equals("default") && !monthString.equals("default") && !dateString.equals("default"))
			date = String.format("%s-%s-%s", yearString, monthString, dateString);
		Field<String> birthField = new Field<>("생년월일", date);
		if(date != null && !date.equals(""))
			birthField.addValidator(new DateValidator<>());
		String zip = dataGetter.get("zip", String.class);
		String address = dataGetter.get("address", String.class);
		String detailAddress = dataGetter.get("detailAddress", String.class);
		
		FieldSet fieldSet = new FieldSet(false);
		fieldSet.addField(idField).addField(pwField).addField(nameField).addField(classNameField).addField(emailField).addField(skypeField)
		.addField(phoneField).addField(birthField);
		List<String> messages = fieldSet.validateAllField();
		if(messages.size() > 0){
			String error = "";
			for(String message : messages)
				error += message + "\n";
			sendError(resp, error);
			return;
		}
		else{
			ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
			StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
			Student student = studentDAO.findStudentById(idField.getValue());
			if(student != null){
				sendError(resp, "아이디가 이미 존재합니다.");
				return;
			}
			student = new Student(idField.getValue(), pwField.getValue(), nameField.getValue(), classNameField.getValue(), 
					Util.parseDateIgnoreNull(birthField.getValue()), UserType.Student, Gender.Male, zip, address, detailAddress, phoneField.getValue(), 
					"", "", null, null, "", "", Level.Untested, skypeField.getValue(), "", 0, emailField.getValue());
			studentDAO.register(student);
			EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
			String mode = environDAO.getNewRegisterSMSMode();
			if(mode.equals("auto")){
				String smsMsg = Util.fillContent(environDAO.getNewRegisterSMSMessage(), student);
				SMS.sms(student.getCellPhone(), smsMsg);
			}
			sendSuccess(resp, "축하드립니다.\n회원가입 되셨습니다.\n로그인 후 서비스를 이용해주세요.");
		}
		
		
	}

}
