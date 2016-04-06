package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.FileSaver;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BookDAO;
import Kakao.kakaoeng.domain.model.Book;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.Student.Level;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet("/teacher/adminBoookRegister.do")
public class AdminBookRegisterServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		Course course = Course.valueOf(req.getParameter("course"));
		Level level = Level.valueOf(req.getParameter("level"));
		String title = URLDecoder.decode(req.getParameter("title"), "utf-8");
		Part imagePart = req.getPart("image");
		
		String DBName = FileSaver.fileSave(imagePart, Util.getPartFileName(imagePart), "book_image", new Date().getTime(), true,-1);
		String link = URLDecoder.decode(req.getParameter("link"), "utf-8");
		String encodedLink = URLEncoder.encode(link, "utf-8");
		
		ApplicationContext applicationContext  = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookDAO bookDAO = (BookDAO) applicationContext.getBean("bookDAO");
		
		Book newBook = new Book(-1, course, level, title, false, encodedLink, DBName);
		bookDAO.register(newBook);
		
		resp.sendRedirect("./adminBookList.view");
	}

}
