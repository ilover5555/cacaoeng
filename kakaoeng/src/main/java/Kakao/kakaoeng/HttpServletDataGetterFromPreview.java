package Kakao.kakaoeng;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.TeacherFactory;

public class HttpServletDataGetterFromPreview extends HttpServletDataGetterFromRegister {

	public HttpServletDataGetterFromPreview(HttpServletRequest req) {
		super(req);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Teacher makeTeacherInstance() {
		FieldSet fieldSet = TeacherFactory.makeTeacherFieldSet(this);
		
		Teacher teacher = TeacherFactory.makeInstanceFromFieldSet(fieldSet);
		
		return teacher;
	}

}
