package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Lecture.Status;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminLectureGeneral.view")
public class AdminLectureGeneral extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(AdminLectureGeneral.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		logger.info(req.getRemoteAddr() + "AdminLectureGeneral doGet");
		String searchMethod=req.getParameter("searchMethod");
		String searchText=req.getParameter("searchText");
		
		if(searchMethod == null)
			searchMethod = "";
		
		if(searchText == null)
			searchText = "";
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		List<Purchase> notRejectedList = null;
		
		switch(searchMethod){
		case "teacher":
			notRejectedList = purchaseDAO.getPurchaseListWithTeacherClassName(searchText);
			break;
		case "student":
			notRejectedList = purchaseDAO.getPurchaseListWithStudentClassName(searchText);
			break;
		case "book":
			notRejectedList = purchaseDAO.getPurchaseListWithBook(searchText);
			break;
		default:
			notRejectedList = purchaseDAO.getPurchaseListNotRejected();
			break;
		}
		
		
		int onGoingCount = 0;
		
		List<Lecture> result = new ArrayList<Lecture>();
		for(Purchase purchase : notRejectedList){
			List<Lecture> lectureList = lectureDAO.getLectureListByPurchase(purchase.getId());
			for(Lecture lecture : lectureList){
				if(lecture.getStatus().equals(Status.OnGoing))
					onGoingCount++;
				lecture.loadStudent(studentDAO);
				lecture.loadTeacher(teacherDAO);
				List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
				Map<OneClass, List<ClassLog>> map = new HashMap<>();
				for(OneClass oneClass : oneClassList){
					List<ClassLog> classLogList = classLogDAO.getAllClassLogListByParentUntilLecture(oneClass.getParent(), lecture.getId());
					map.put(oneClass, classLogList);
					oneClass.setDone(classLogList);
				} 
				lecture.setDone(oneClassList);
				lecture.loadPurchase(purchaseDAO);
			}
			result.addAll(lectureList);
		}
		
		req.setAttribute("notRejectedList", result);
		req.setAttribute("onGoingCount", onGoingCount);
		
		req.getRequestDispatcher("./admin_lecture_general.jsp").forward(req, resp);
	}

}
