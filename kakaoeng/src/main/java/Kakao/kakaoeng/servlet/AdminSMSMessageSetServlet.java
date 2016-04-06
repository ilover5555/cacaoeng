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

@WebServlet("/teacher/adminSMSMessageSet.do")
public class AdminSMSMessageSetServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String type = req.getParameter("type");
		String msg = req.getParameter("msg");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		if(type.equals("all")){
			environDAO.saveAllSMSMessage(msg);
		}else if(type.equals("new")){
			environDAO.saveNewRegisterSMSMessage(msg);
		}else if(type.equals("lecture")){
			environDAO.saveNewLectureSMSMessage(msg);
		}else if(type.equals("re")){
			environDAO.saveRequestLectureSMSMessage(msg);
		}else if(type.equals("pw")){
			environDAO.saveQueryPwSMSMessage(msg);
		}else if(type.equals("re_after")){
			environDAO.saveAfterRequestLectureSMSMessage(msg);
		}else{
			Util.sendError(resp, "Invalid Type for AdminSMSMessageSetServlet");
			return;
		}
		
		Util.sendSuccess(resp, "메시지를 성공적으로 변경하였습니다.");
	}

}
