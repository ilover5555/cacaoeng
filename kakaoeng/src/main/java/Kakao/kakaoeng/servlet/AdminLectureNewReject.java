package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.dao.ClassTimeUsageLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Lecture.Status;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminLectureReject.do")
public class AdminLectureNewReject extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		int purchaseId = -1;
		try{
			purchaseId = Integer.parseInt(req.getParameter("purchaseId"));
		}catch(NumberFormatException e){
			Util.sendError(resp, "Invalid Purchase Id");
			return;
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		Purchase purchase = purchaseDAO.getPurchaseById(purchaseId);
		if(purchase == null){
			Util.sendError(resp, "Invalid Purchase Id : " +purchaseId);
			return;
		}
		purchaseDAO.updateRejected(purchase.getId(), true);
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		Lecture lecture = lectureDAO.getOnGoingLectureByPurchase(purchase.getId());
		lectureDAO.updateLectureState(lecture.getId(), Status.Done);
		
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassTimeDAO classTimeDAO = (ClassTimeDAO) applicationContext.getBean("classTimeDAO");
		ClassTimeUsageLogDAO classTimeUsageLogDAO = (ClassTimeUsageLogDAO) applicationContext.getBean("classTimeUsageLogDAO");
		List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByPurchaseNumber(purchase.getId());
		for(OneClass oneClass : oneClassList){
			List<ClassTime> classTimeList = oneClass.getClassTimeList();
			for(ClassTime classTime : classTimeList){
				int id = classTimeDAO.findIdFromClassTimeInstance(classTime);
				classTimeUsageLogDAO.deleteByClassTimeIdTransaction(id);
			}
		}
		
		Util.sendSuccess(resp, "Successfully Rejected");
	}

}
