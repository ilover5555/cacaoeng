package Kakao.kakaoeng;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.codec.digest.DigestUtils;

import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.TeacherFactory;
import Kakao.kakaoeng.domain.model.Teacher.Rate;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.field.PasswordField;

public class HttpServletDataGetterFromUpdate extends HttpServletDataGetter {

	public Teacher user = null;
	
	public HttpServletDataGetterFromUpdate(HttpServletRequest req) {
		this.req = req;
		HttpSession session = req.getSession();
		user = (Teacher) session.getAttribute("teacher");
		if(user == null){
			throw new LoginRequiredException("Teacher Update Requires LogIn.");
		}
	}
	
	public HttpServletDataGetterFromUpdate(HttpServletRequest req, Teacher teacher) {
		this.req = req;
		user = teacher;
		if(user == null){
			throw new IllegalArgumentException("HttpServletDataGetterFromUpdate cannot be initiated with null teacher");
		}
	}
	
	@Override
	public Teacher makeTeacherInstance() {
		FieldSet fieldSet = TeacherFactory.makeTeacherFieldSet(this);
		messages.addAll(fieldSet.validateAllField());
		
		messages.addAll(this.fileSave("primaryProfile_Part", fieldSet, 10*1024*1024));
		messages.addAll(this.fileSave("primaryVoice_Part", fieldSet, 10*1024*1024));
		messages.addAll(this.fileSave("specImage_Part", fieldSet, 10*1024*1024));
		
		Teacher teacher = TeacherFactory.makeInstanceFromFieldSet(fieldSet);
		
		return teacher;
	}
	
	@Override
	public String getPassword() {
		
		String pw = this.get("pw", String.class);
		String pwConfirm = this.get("pwConfirm", String.class);
		
		if( (pw.length()) == 0 && (pwConfirm.length()==0))
			return user.getPw();
		else
			return pw;
			
	}

	@Override
	public String getPasswordConfirm() {
		String pw = this.get("pw", String.class);
		String pwConfirm = this.get("pwConfirm", String.class);
		
		if( (pw.length()) == 0 && (pwConfirm.length()==0))
			return user.getPw();
		else
			return pwConfirm;
	}
	
	
	public static class NullPart implements Part{
		protected String originValue = null;
		public NullPart(String originValue) {this.originValue = originValue;}
		@Override public void delete() throws IOException {}
		@Override public String getContentType() {return null;}
		@Override public String getHeader(String arg0) {return null;}
		@Override public Collection<String> getHeaderNames() {return null;}
		@Override public Collection<String> getHeaders(String arg0) {return null;}
		@Override public InputStream getInputStream() throws IOException {return null;}
		@Override public String getName() {return null;}
		@Override public long getSize() {return 0;}
		@Override public void write(String arg0) throws IOException {}
	}
	@Override
	public Part getPart(String key) throws IOException, ServletException {
		Part part = req.getPart(key);
		if(part.getSize() <= 0 || Util.getPartFileName(part) == null || Util.getPartFileName(part).equals("")){
			String partPath = "";
			if(key.equals("primaryProfile"))
				partPath = user.getPrimaryProfilePicture();
			else if(key.equals("primaryVoice"))
				partPath = user.getPrimaryVoice();
			else
				partPath = user.getSpecImage();
			return new NullPart(partPath);
		}
		else
			return part;
	}

	@Override
	public Field<String> getIdField() {
		return new Field<String>("id", this, String.class);
	}

	@Override
	public PasswordField getPasswordField() {
		PasswordField pw = new PasswordField("pw", this.getPassword());
		String pwConfirm = this.getPasswordConfirm();
		pw.addParam("passwordConfirm", pwConfirm);
		return pw;
	}

	@Override
	public Rate getRate() {
		return null;
	}
}
