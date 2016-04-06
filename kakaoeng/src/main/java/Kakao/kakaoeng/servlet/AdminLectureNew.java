package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Purchase;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminLectureNew.view")
public class AdminLectureNew extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		
		List<Purchase> purchaseList = purchaseDAO.getPurchaseListWhichIsUnconfirm();

		
		for(Iterator<Purchase> iter = purchaseList.iterator(); iter.hasNext();){
			Purchase purchase = iter.next();
			purchase.loadLecture(lectureDAO);
			if(purchase.getLecture() == null){
				iter.remove();
				continue;
			}
			purchase.getLecture().loadStudent(studentDAO);
			purchase.getLecture().loadTeacher(teacherDAO);
			
			purchase.loadWeeks(oneClassDAO.getOneClassListGroupedByLectureIdTransaction(purchase.getLecture().getId()).size());
		}
		req.setAttribute("unconfirmedPurchaseList", purchaseList);
		
		req.getRequestDispatcher("./admin_lecture_new.jsp").forward(req, resp);
	}

}
