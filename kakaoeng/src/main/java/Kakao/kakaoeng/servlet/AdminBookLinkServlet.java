package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BookDAO;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminBookLink.edit")
public class AdminBookLinkServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		int bookId = Integer.parseInt(req.getParameter("bookId"));
		String link = req.getParameter("link");
		String encodedLink = URLEncoder.encode(link, "utf-8");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookDAO bookDAO = (BookDAO) applicationContext.getBean("bookDAO");
		
		bookDAO.updateBookLink(bookId, encodedLink);
		
		Util.sendSuccess(resp, "링크를 성공적으로 수정하였습니다.");
	}

	
}
