package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.SMS;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.servlet.AdminAfterRequestLectureSMSServlet.DifferVO;

@WebServlet("/teacher/adminSMSRequestLEctureManual.do")
public class AdminSMSRequestLectureManualServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String type = req.getParameter("type");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		String msg = null;
		List<Lecture> lectureList = null;
		DifferVO vo = null;
		if(type.equals("re")){
			lectureList = AdminBeforeRequestLectureSMSServlet.getList(applicationContext, true);
			msg = environDAO.getRequestLectureSMSMessage();
			vo = new DifferVO(environDAO.getBeforeRequestLectureDiffer());
		}else if(type.equals("re_after")){
			lectureList = AdminAfterRequestLectureSMSServlet.getList(applicationContext, true);
			msg = environDAO.getAfterRequestLectureSMSMessage();
			vo = new DifferVO(environDAO.getAfterRequestLectureDiffer());
		}else{
			Util.sendError(resp, "Invalid type for AdminSMSRequestLectureManualServlet : " + type);
			return;
		}
		
		
		for(Lecture lecture : lectureList){
			String smsMsg = Util.fillContent(msg, vo);
			SMS.sms(lecture.getStudent().getCellPhone(), smsMsg);
		}
		Util.sendSuccess(resp, "성공적으로 메시지를 전송하였습니다.");
	}

}
