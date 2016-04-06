package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.CalculateTuition;
import Kakao.kakaoeng.FixedCalculateTuition;
import Kakao.kakaoeng.dao.BookDAO;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Book;
import Kakao.kakaoeng.domain.model.Tuition;
import Kakao.kakaoeng.domain.model.Book.Course;

@SuppressWarnings("serial")
@WebServlet("/m3/match.view")
public class MatchViewerServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sort = req.getParameter("sort");
		Course course = Course.valueOf(req.getParameter("course"));
		
		int month = -1;
		try{
			month = Integer.parseInt(req.getParameter("month"));
		}catch(NumberFormatException e){
			month = 1;
		}
		
		
		
		
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BookDAO bookDAO = (BookDAO) applicationContext.getBean("bookDAO");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		List<Book> bookList = bookDAO.getBookListByCourse(course);
		
		req.setAttribute("bookList", bookList);
		req.setAttribute("course", course);
		String url = "./"+sort+".jsp?sort="+sort+"&course="+course+"&month="+month;
		if(sort.equals("FixMatch")){
			int period = -1;
			try{
				period = Integer.parseInt(req.getParameter("period"));
			}catch(NumberFormatException e){
				period = -1;
			}
			if(period == -1)
				period = 2;
			url += "&period="+period;
			int duration = -1;
			try{
				duration = Integer.parseInt(req.getParameter("duration"));
			}catch(NumberFormatException e){
				duration = -1;
			}
			if(duration == -1)
				duration = 1;
			url += "&duration="+duration;
			CalculateTuition hct = new FixedCalculateTuition(environDAO, month, period, 1);
			int halfPrice = hct.calculate().getFinalPrice();
			CalculateTuition fct = new FixedCalculateTuition(environDAO, month, period, 2);
			int fullPrice = fct.calculate().getFinalPrice();
			req.setAttribute("halfPrice", halfPrice);
			req.setAttribute("fullPrice", fullPrice);
		}
		req.getRequestDispatcher(url).forward(req, resp);
	}

}
