package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.BookManager;
import Kakao.kakaoeng.ClassSearchUnit;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.Student;

@SuppressWarnings("serial")
@WebServlet("/bookLevelTest.do")
public class BookLevelTestServlet extends HttpServlet{

	Logger logger = Logger.getLogger(BookLevelTestServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String teacherId = req.getParameter("teacherId");
		int stamp = Integer.parseInt(req.getParameter("stamp"));
		Date baseDate = Util.parseDate(req.getParameter("baseDate"));
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookManager bookManager = (BookManager) applicationContext.getBean("bookManager");
		Student student = (Student)req.getSession().getAttribute("student");
		
		if(student == null){
			Util.sendError(resp, "로그인이 만료되었습니다.\n로그인 후 다시시도해주세요");
			return;
		}
		
		ClassSearchUnit csu = new ClassSearchUnit(new Duration(stamp, 1), new Date(), 1);
		try{
			bookManager.bookLevelTestTransaction(csu, student, baseDate, teacherId, req.getRemoteAddr());
		}catch(RuntimeException e){
			logger.error(e.getMessage(), e);
			Util.sendError(resp, e.getMessage());
			return;
		}
		
		Util.sendSuccess(resp, "레벨테스트 요청이 완료되었습니다.");
	}

}
