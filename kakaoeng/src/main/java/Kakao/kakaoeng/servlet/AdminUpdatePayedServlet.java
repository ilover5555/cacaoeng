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
import Kakao.kakaoeng.Mail;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.BookManager.bookVO;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.PayDAO;
import Kakao.kakaoeng.domain.model.Pay;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;

@WebServlet("/teacher/admimUpdatePayed.do")
public class AdminUpdatePayedServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String teacherId = req.getParameter("teacherId");
		int month = Integer.parseInt(req.getParameter("month"));
		boolean payed = Boolean.parseBoolean(req.getParameter("payed"));
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		PayDAO payDAO = (PayDAO) applicationContext.getBean("payDAO");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		Pay pay = payDAO.getPayListByTeacherIdAndMonth(teacherId, month);
		
		if(payed == true){
			if(pay.getId() == -1){
				Pay newPay = new Pay(-1, teacherId, month, true);
				payDAO.register(newPay);
			}
			else{
				payDAO.updatePayed(pay.getId(), true);
			}
		}else{
			if(pay.getId() == -1){
				
			}else{
				payDAO.updatePayed(pay.getId(), false);
			}
		}
		
		String mode = environDAO.getPayedMailMode();
		if(mode.equals("auto")){
			String mailMsg = environDAO.getPayedMailMessage();
			Mail.send(Mail.username, teacherId, environDAO.getPayedMailSubject(), mailMsg);
		}
		
		Util.sendSuccess(resp, "지급여부를 병경하였습니다.");
	}

	
}
