package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.BookManager;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Lecture.Status;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminLectureCancel.do")
public class AdminLectureCancelServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		int purchaseNumber = -1;
		Status status = null;
		
		try{
			purchaseNumber = Integer.parseInt(req.getParameter("purchaseNumber"));
			status = Status.valueOf(req.getParameter("lectureStatus"));
		}catch(RuntimeException e){}
		
		if(purchaseNumber == -1 || status == null){
			Util.sendError(resp, "Invalid Argument\npurchaseNumber:"+purchaseNumber+"\nstatus:"+status);
			return;
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookManager bookManager = (BookManager) applicationContext.getBean("bookManager");
		
		bookManager.cancelTransaction(purchaseNumber, status);
		Util.sendSuccess(resp, "강의가 " + status+" 상태로 성공적으로 전환되었습니다.");
	}

}
