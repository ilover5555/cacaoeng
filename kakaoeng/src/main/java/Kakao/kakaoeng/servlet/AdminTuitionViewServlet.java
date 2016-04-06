package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.EnvironDAO;

@WebServlet("/teacher/adminViewTuition.view")
public class AdminTuitionViewServlet extends HttpServlet{

	public static class SmartPriceVO{
		private Map<String, Integer> map = new HashMap<>();
		public void put(String key, Integer value){
			map.put(key, value);
		}
		public String getKey(int index){
			return "SMART_FINAL_"+index;
		}
		public int getValue(int index){
			return map.get("SMART_FINAL_"+index);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		req.setAttribute("ONE_MONTH_DISCOUNT_PERCENT", environDAO.getMonthDiscountPercentString(1));
		req.setAttribute("THREE_MONTH_DISCOUNT_PERCENT", environDAO.getMonthDiscountPercentString(3));
		req.setAttribute("SIX_MONTH_DISCOUNT_PERCENT", environDAO.getMonthDiscountPercentString(6));
		req.setAttribute("TWELVE_MONTH_DISCOUNT_PERCENT", environDAO.getMonthDiscountPercentString(12));
		req.setAttribute("DAY_DISCOUNT_PERCENT", environDAO.getDayDiscountPercentString());
		
		req.setAttribute("FIX_DISCOUNT_NAME", environDAO.getFixDiscoutName());
		req.setAttribute("FIX_DISCOUNT_PERCENT", environDAO.getFixDiscountPercentString());
		
		req.setAttribute("FIX_PRICE_2_25", environDAO.getFixPrice(2, 25));
		req.setAttribute("FIX_PRICE_2_50", environDAO.getFixPrice(2, 50));
		req.setAttribute("FIX_PRICE_3_25", environDAO.getFixPrice(3, 25));
		req.setAttribute("FIX_PRICE_3_50", environDAO.getFixPrice(3, 50));
		req.setAttribute("FIX_PRICE_5_25", environDAO.getFixPrice(5, 25));
		req.setAttribute("FIX_PRICE_5_50", environDAO.getFixPrice(5, 50));
		
		req.setAttribute("SMART_BASE_PRICE", environDAO.getSmartBasePrice());
		req.setAttribute("SMART_ADJUST_PERCENT", environDAO.getSmartAdjust());
		req.setAttribute("SMART_DISCOUNT_NAME", environDAO.getSmartDiscoutName());
		req.setAttribute("SMART_DISCOUNT_PERCENT", environDAO.getSmartDiscountPercentString());
		
		req.setAttribute("SMART_FINAL_2", environDAO.getSmartFinalPrice(2));
		SmartPriceVO vo = new SmartPriceVO();
		for(int i=3; i<=40; i++){
			vo.put("SMART_FINAL_"+i, environDAO.getSmartFinalPrice(i));
		}
		req.setAttribute("SMART_FINAL_PRICE", vo);
		
		req.getRequestDispatcher("./admin_tuition_manage.jsp").forward(req, resp);
	}

}
