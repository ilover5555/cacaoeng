package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.SMS;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.SMS.SmsResult;
import Kakao.kakaoeng.domain.model.Student;

@WebServlet("/teacher/adminSMS.send")
public class AdminSMSSendServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		List<Student> list = (List<Student>) req.getAttribute("list");
		String msg = (String) req.getAttribute("msg");
		SmsResult result = null;
		StringBuilder sb = new StringBuilder();
		for(Student student : list){
			result = SMS.sms(student.getCellPhone(), Util.fillContent(msg, student));
			if(result.getRetCode() != 0){
				sb.append(result.getRetMessage());
			}
		}
		sb.append(result.getRetLastPoint());
		sb.append("포인트 남았습니다.");
		sb.append("전송이 완료되었습니다.");
		Util.sendSuccess(resp, sb.toString());
	}

}
