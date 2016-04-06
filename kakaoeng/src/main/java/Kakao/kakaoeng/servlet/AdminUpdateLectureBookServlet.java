package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BookDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.domain.model.Book;

@WebServlet("/teacher/adminUpdateLectureBook.do")
public class AdminUpdateLectureBookServlet extends HttpServlet {

	Logger logger = Logger.getLogger(AdminUpdateLectureBookServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		int lectureId = Integer.parseInt(req.getParameter("lectureId"));
		String title = req.getParameter("title");
		title = URLDecoder.decode(title, "utf-8");
		
		logger.info("LectureBookUpdate - title:"+title+",lectureId:"+lectureId);
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookDAO bookDAO = (BookDAO) applicationContext.getBean("bookDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		
		Book book = bookDAO.getBookByTitle(title);
		lectureDAO.updateBook(book, lectureId);
		
		Util.sendSuccess(resp, "책정보를 성공적으로 수정하였습니다.");
	}

}
