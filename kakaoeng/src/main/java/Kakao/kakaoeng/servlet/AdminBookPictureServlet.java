package Kakao.kakaoeng.servlet;

import java.io.IOException;
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
import Kakao.kakaoeng.domain.model.UploadedFile;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet("/teacher/adminBookPicture.do")
public class AdminBookPictureServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		int bookId = Integer.parseInt(req.getParameter("bookId"));
		Part imagePart = req.getPart("image");
		
		String DBName = FileSaver.fileSave(imagePart, Util.getPartFileName(imagePart), "book_image", new Date().getTime(), true, -1);
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookDAO bookDAO = (BookDAO) applicationContext.getBean("bookDAO");
		Book book = bookDAO.getBookById(bookId);
		if(book == null){
			resp.sendRedirect("./adminBookList.view");
			return;
		}
		if(book.getBookPicture().length() > 0){
			UploadedFile f = new UploadedFile(book.getBookPicture());
			f.removeFileFromStorage(req.getServletContext().getRealPath("/"));
		}
			
		bookDAO.updateBookPicture(bookId, DBName);
		
		Util.sendSuccess(resp, "책의 소개 이미지를 변경하였습니다.");
	}

	
}
