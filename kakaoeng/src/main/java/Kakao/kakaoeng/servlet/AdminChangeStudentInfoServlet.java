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
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.User.UserType;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.field.GeneralField;
import Kakao.kakaoeng.field.PasswordField;
import Kakao.kakaoeng.validator.DateValidator;
import Kakao.kakaoeng.validator.EmailValidator;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminChangeStudentInformation.edit")
public class AdminChangeStudentInfoServlet extends HttpServlet {

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		String studentId = req.getParameter("studentId");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		Student student = studentDAO.findStudentById(studentId);
		if(student == null){
			Util.sendError(resp, studentId + " is not valid or not exist");
			return;
		}
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		Lecture lecture = lectureDAO.getLevelTestLogByStudentId(student.getId());
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		Teacher teacher = null;
		if(lecture != null){
			teacher = teacherDAO.getTeacherWithId(lecture.getTeacherId());
		}else{
			lecture = new Lecture(-1, -1, -1, null, null, -1, "", "", null, "", null, "", false, false, false);
			teacher = new Teacher("", "", "", "", null, null, null, "", "", "", null, null, "", "", null, null, null, false, false, "", "", "", "", 
					"", "", "", "", "", "", "", "", "", false, null, false, -1, null, null, "", false);
		}
		req.setAttribute("student", student);
		req.setAttribute("levelTestLog", lecture);
		req.setAttribute("teacher", teacher);
		req.getRequestDispatcher("./admin_modify_student.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		String studentId = req.getParameter("studentId");
		Student student = studentDAO.findStudentById(studentId);
		if(student == null){
			Util.sendError(resp, studentId + " is invalid or dont exist.");
			return;
		}
		
		DataGetter dataGetter = new HttpServletDataGetterFromRegister(req);
		
		Field<String> idField = new GeneralField<>("id", student.getId());
		String pw = dataGetter.getPassword();
		String pwConfirm = dataGetter.getPasswordConfirm();
		if(pw.equals("") && pwConfirm.equals("")){
			pw = student.getPw();
			pwConfirm = student.getPw();
		}
		PasswordField pwField = new PasswordField("pw", pw);
		pwField.setPasswordConfirm(pwConfirm);
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
			Util.sendError(resp, error);
			return;
		}
		else{
			
			Student updated = new Student(idField.getValue(), pwField.getValue(), nameField.getValue(), classNameField.getValue(), 
					Util.parseDateIgnoreNull(birthField.getValue()), UserType.Student, student.getGender(), zip, address, detailAddress, phoneField.getValue(), 
					student.getHomePhone(), student.getPrimaryProfilePicture(), null, null, student.getParentName(), student.getParentPhone(), student.getLevel(),
					skypeField.getValue(), student.getNote(), student.getCoupon(), emailField.getValue());
			studentDAO.update(updated);
			Util.sendSuccess(resp, "정보수정이 완료되었습니다.");
		}
	}

}
