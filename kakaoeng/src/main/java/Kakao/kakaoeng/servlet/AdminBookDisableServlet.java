package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BookDAO;
import Kakao.kakaoeng.domain.model.Book;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminBookDisable.do")
public class AdminBookDisableServlet extends HttpServlet {
	static Logger logger = Logger.getLogger(AdminBookDisableServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		int bookId = Integer.parseInt(req.getParameter("bookId"));
		boolean disabled = Boolean.parseBoolean(req.getParameter("disabled"));
		
		logger.info("bookId:"+bookId+",disabled:"+disabled);
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookDAO bookDAO = (BookDAO) applicationContext.getBean("bookDAO");
		
		Book book = bookDAO.getBookById(bookId);
		if(book == null){
			Util.sendError(resp, bookId + " do not exist");
			return;
		}
		
		bookDAO.updateDisabled(bookId, disabled);
		Util.sendSuccess(resp, "책의 사용여부를 수정했습니다.");
	}

}
