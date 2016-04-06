package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.SMS;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Student;

@WebServlet("/findStudentPw.do")
public class FindStudentPWServlet extends HttpServlet {

	public static class PWVO{
		private String pw;

		public PWVO(String pw) {
			super();
			this.pw = pw;
		}

		public String getPw() {
			return pw;
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name").trim();
		String cellPhone = req.getParameter("cellPhone").trim();
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		List<Student> studentList = studentDAO.findStudentByNameAndCellPhone(name, cellPhone);
		Student student = null;
		if(studentList.size() == 0 ){
			Util.sendError(resp, "정보가 일치하지 않습니다.");
			return;
		}
		if(studentList.size() > 1){
			Util.sendError(resp, "일치하는 아이디가 2개이상입니다.");
			return;
		}
		
		student = studentList.get(0);
		
		Random r = new Random(new Date().getTime());
		int i = r.nextInt(10)+3;
		int result = 0;
		for(int j=0; j<i; j++){
			result = r.nextInt(10000);
		}
		result = result % 10000;
		String pw = String.format("%04d", result);
		String hashPw = DigestUtils.sha256Hex(pw);
		
		studentDAO.updatePw(student.getId(), hashPw);
		
		PWVO vo = new PWVO(pw);
		
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		String msg = environDAO.getQueryPwSMSMessage();
		String smsMsg = Util.fillContent(msg, vo);
		
		SMS.sms(student.getCellPhone(), smsMsg);
		
		Util.sendSuccess(resp, "비밀번호 재설정 성공");
	}

}
