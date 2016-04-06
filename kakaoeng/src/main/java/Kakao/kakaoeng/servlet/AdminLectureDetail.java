package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Count;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BookDAO;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Book;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Purchase;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminLectrueDetail.edit")
public class AdminLectureDetail extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		int purchaseNumber = -1;
		int lectureId = -1;
		
		try{purchaseNumber=Integer.parseInt(req.getParameter("purchaseNumber"));}catch(RuntimeException e){}
		try{lectureId = Integer.parseInt(req.getParameter("lectureId"));}catch(RuntimeException e) {}
		if(purchaseNumber == -1 || lectureId == -1){
			Util.sendError(resp, "Invalid Argument, purchaseNumber : " + purchaseNumber + ", lectureId : " + lectureId);
			return;
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		Purchase purchase = purchaseDAO.getPurchaseById(purchaseNumber);
		
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		Lecture lecture = lectureDAO.getLectureByIdTransaction(lectureId);
		
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		
		lecture.loadStudent(studentDAO);
		lecture.loadTeacher(teacherDAO);
		
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
		
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		for(OneClass oneClass : oneClassList){
			List<ClassLog> classLogList = classLogDAO.getAllClassLogListByParent(oneClass.getParent());
			oneClass.setDone(classLogList);
		}
		
		BookDAO bookDAO = (BookDAO) applicationContext.getBean("bookDAO");
		List<Book> bookList = bookDAO.getAllBookList();
		Count count = new Count();
		count.setIndex(1);
		req.setAttribute("purchase", purchase);
		req.setAttribute("lecture", lecture);
		req.setAttribute("oneClassList", oneClassList);
		req.setAttribute("count", count);
		req.setAttribute("bookList", bookList);
		
		req.getRequestDispatcher("./admin_lecture_detail.jsp").forward(req, resp);
	}

}
