package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;

@MultipartConfig
@WebServlet("/teacher/adminSetTuition.do")
public class AdminTuitionSetServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		environDAO.saveMonthDiscountPercent(1, Util.getDoubleFromPercentString(req.getParameter("ONE_MONTH_DISCOUNT_PERCENT")));
		environDAO.saveMonthDiscountPercent(3, Util.getDoubleFromPercentString(req.getParameter("THREE_MONTH_DISCOUNT_PERCENT")));
		environDAO.saveMonthDiscountPercent(6, Util.getDoubleFromPercentString(req.getParameter("SIX_MONTH_DISCOUNT_PERCENT")));
		environDAO.saveMonthDiscountPercent(12, Util.getDoubleFromPercentString(req.getParameter("TWELVE_MONTH_DISCOUNT_PERCENT")));
		environDAO.saveDayDiscountPercent(Util.getDoubleFromPercentString(req.getParameter("DAY_DISCOUNT_PERCENT")));
		
		environDAO.saveSmartAdjust(Double.parseDouble(req.getParameter("SMART_ADJUST_PERCENT")));
		
		environDAO.saveSmartDiscountName(req.getParameter("SMART_DISCOUNT_NAME"));
		environDAO.saveSmartDiscountPercent(Util.getDoubleFromPercentString(req.getParameter("SMART_DISCOUNT_PERCENT")));
		
		environDAO.saveSmartBasePrice(Integer.parseInt(req.getParameter("SMART_BASE_PRICE")));
		
		for(int i=2; i<=40; i++){
			environDAO.saveSmartFinalPrice(i, Integer.parseInt(req.getParameter("SMART_FINAL_"+i)));
		}
		
		Util.sendSuccess(resp, "Success");
	}

}
