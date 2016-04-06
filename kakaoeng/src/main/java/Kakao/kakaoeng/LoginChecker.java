package Kakao.kakaoeng;

import javax.servlet.http.HttpServletRequest;

import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.domain.model.User.UserType;

public class LoginChecker {

	HttpServletRequest req;

	public LoginChecker(HttpServletRequest req) {
		super();
		this.req = req;
	}
	
	public boolean getAdminLogin(){
		Admin admin = (Admin) req.getSession().getAttribute("admin");
		if(admin == null){
			return false;
		}
		if(!admin.getUserType().equals(UserType.Admin))
			throw new IllegalStateException("admin in session's user Type is not admin");
		return true;
	}
	public boolean getExecLogin(){
		Admin executor = (Admin) req.getSession().getAttribute("executor");
		if(executor == null){
			return false;
		}
		if(!executor.getUserType().equals(UserType.Executor))
			throw new IllegalStateException("executor in session's user Type is not executor");
		return true;
	}
	public boolean getStudentLogin(){
		Student student = (Student) req.getSession().getAttribute("student");
		if(student == null){
			return false;
		}
		if(!student.getUserType().equals(UserType.Student))
			throw new IllegalStateException("student in session's user Type is not student");
		return true;
	}
	public boolean getTeacherLogin(){
		Teacher teacher = (Teacher) req.getSession().getAttribute("teacher");
		if(teacher == null){
			return false;
		}
		if(!teacher.getUserType().equals(UserType.Teacher))
			throw new IllegalStateException("teacher in session's user Type is not teacher");
		return true;
	}
	public Admin getLoginAdminObject(){
		if(getAdminLogin()==false)
			return null;
		
		return (Admin) req.getSession().getAttribute("admin");
	}
	public Admin getLoginExecObject(){
		if(getExecLogin()==false)
			return null;
		
		return (Admin) req.getSession().getAttribute("executor");
	}
	public Student getLoginStudentObject(){
		if(getStudentLogin()==false)
			return null;
		
		return (Student) req.getSession().getAttribute("student");
	}
	public Teacher getLoginTeacherObject(){
		if(getTeacherLogin()==false)
			return null;
		
		return (Teacher) req.getSession().getAttribute("teacher");
	}
}
