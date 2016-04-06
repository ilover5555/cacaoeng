package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.BookDAO;
import Kakao.kakaoeng.domain.model.Book;

@SuppressWarnings("serial")
@WebServlet("/m3/books.view")
public class StudentBookViewServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookDAO bookDAO = (BookDAO) applicationContext.getBean("bookDAO");
		
		List<Book> enabledList = bookDAO.getEnabledBookList();
		
		List<Book> ESL = new ArrayList<>();
		List<Book> FreeTalk = new ArrayList<>();
		List<Book> TextBook = new ArrayList<>();
		List<Book> Business = new ArrayList<>();
		List<Book> Exam = new ArrayList<>();
		for(Book book : enabledList){
			switch(book.getCourse()){
			case TypeEasy:
			case TypeMiddle:
				ESL.add(book);
				break;
			case TypeFreeTalk:
				FreeTalk.add(book);
				break;
			case TypeElementBook:
				TextBook.add(book);
				break;
			case TypeBussiness:
				Business.add(book);
				break;
			case TypeExam:
				Exam.add(book);
				break;
			default:
				break;
			}
		}
		
		req.setAttribute("ESLList", ESL);
		req.setAttribute("FreeTalk", FreeTalk);
		req.setAttribute("TextBook", TextBook);
		req.setAttribute("Business", Business);
		req.setAttribute("Exam", Exam);
		
		req.getRequestDispatcher("./books.jsp").forward(req, resp);;
	}

}
