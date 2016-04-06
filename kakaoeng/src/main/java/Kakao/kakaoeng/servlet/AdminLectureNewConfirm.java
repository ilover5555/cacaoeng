package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Purchase.Method;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminLectureConfirm.do")
public class AdminLectureNewConfirm extends HttpServlet {

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
			Util.sendError(resp, "Invalid Purchase Id");
			return;
		}
		purchaseDAO.updateConfirm(purchaseId, true);
		
		if(purchase.getMethod().equals(Method.Account))
			purchaseDAO.updateApprovedNumber(purchaseId);
		
		Util.sendSuccess(resp, "Successfully confirm");
	}

}
