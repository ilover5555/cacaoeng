package Kakao.kakaoeng;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.HttpServletDataGetterFromUpdate.NullPart;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.TeacherFactory;
import Kakao.kakaoeng.domain.model.Teacher.Rate;
import Kakao.kakaoeng.field.ExceptionField;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.field.IdField;
import Kakao.kakaoeng.field.PasswordField;

public class HttpServletDataGetterFromRegister extends HttpServletDataGetter implements DataGetter {

	public HttpServletDataGetterFromRegister(HttpServletRequest req) {
		this.req = req;
	}

	@Override
	public Teacher makeTeacherInstance() {
		FieldSet fieldSet = TeacherFactory.makeTeacherFieldSet(this);
		messages.addAll(fieldSet.validateAllField());
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		
		messages.addAll(this.fileSave("primaryProfile_Part", fieldSet, 10*1024*1024));
		messages.addAll(this.fileSave("primaryVoice_Part", fieldSet, 10*1024*1024));
		messages.addAll(this.fileSave("specImage_Part", fieldSet, 10*1024*1024));
		
		if(teacherDAO.findTeacherById((String) fieldSet.getField("id").getValue()))
			messages.add("ID already exists. Try another one.");
		
		Teacher teacher = TeacherFactory.makeInstanceFromFieldSet(fieldSet);
		
		return teacher;
	}

	@Override
	public String getPassword() {
		return this.get("pw", String.class);
	}
	
	@Override
	public String getPasswordConfirm() {
		return this.get("pwConfirm", String.class);
	}
	
	@Override
	public Part getPart(String key) throws IOException, ServletException {
		Part part = req.getPart(key);
		if(part.getSize() <= 0 || Util.getPartFileName(part) == null || Util.getPartFileName(part).equals("")){
			String name = "";
			if(key.equals("primaryProfile")){
				name = "Profile picture";
				throw new RuntimeException(name+ " cannot be omitted");
			}
			else if(key.equals("primaryVoice")){
				name = "Profile voice";
				throw new RuntimeException(name+ " cannot be omitted");
			}
			else if(key.equals("specImage")){
				return new NullPart("");
			}
			else{
				throw new RuntimeException("Invalid FileUpload is requested.");
			}
			
		}
		else
			return part;
	}

	@Override
	public Field<String> getIdField() {
		return new IdField<String>("id", this, String.class);
		
	}

	@Override
	public PasswordField getPasswordField() {
		PasswordField pw = new PasswordField("pw", this.getPassword());
		String pwConfirm = this.getPasswordConfirm();
		pw.setPasswordConfirm(pwConfirm);
		return pw;
	}

	@Override
	public Rate getRate() {
		return Rate.Wait;
	}



	

}
