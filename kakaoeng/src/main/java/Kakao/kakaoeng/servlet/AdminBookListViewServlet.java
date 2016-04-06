package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Pagination;
import Kakao.kakaoeng.dao.BookDAO;
import Kakao.kakaoeng.domain.model.Book;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminBookList.view")
public class AdminBookListViewServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookDAO bookDAO = (BookDAO) applicationContext.getBean("bookDAO");
		List<Book> bookList = bookDAO.getAllBookList();

		int page = 1;
		int viewPerPage = 20;
		
		try{
			page = Integer.parseInt(req.getParameter("page"));
		}catch(NumberFormatException e){
			page = 1;
		}
		
		try{
			viewPerPage = Integer.parseInt(req.getParameter("viewPerPage"));
		}catch(NumberFormatException e){
			viewPerPage = 20;
		}
		
		Pagination pagination = new Pagination(page, viewPerPage, bookList.size());
		
		List<Book> result = bookDAO.getAllBookList(pagination.getStart(), pagination.getViewPerPage());
		Collections.sort(result);
		req.setAttribute("bookList", result);
		req.setAttribute("pagination", pagination);
		
		req.getRequestDispatcher("./admin_book_list.jsp").forward(req, resp);
	}

}
