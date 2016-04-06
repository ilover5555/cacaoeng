package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;

@WebServlet("/teacher/adminMailMessageSet.do")
public class AdminMailMessageSetServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String type = req.getParameter("type");
		String msg = req.getParameter("msg");
		String subject = req.getParameter("subject");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		if(type.equals("confirm")){
			environDAO.saveTeacherConfirmMailSubject(subject);
			environDAO.saveTeacherConfirmMailMessage(msg);
		}else if(type.equals("lecture")){
			environDAO.saveLectureRegisteredMailSubject(subject);
			environDAO.saveLectureRegistredMailMessage(msg);
		}else if(type.equals("payed")){
			environDAO.savePayedMailSubject(subject);
			environDAO.savePayedMailMessage(msg);
		}else if(type.equals("level")){
			environDAO.saveLevelTestMailSubject(subject);
			environDAO.saveLevelTestMessage(msg);
		}else{
			Util.sendError(resp, "Invalid Type for AdminSMSMessageSetServlet : " + type);
			return;
		}
		
		Util.sendSuccess(resp, "메시지를 성공적으로 변경하였습니다.");
	}

}
