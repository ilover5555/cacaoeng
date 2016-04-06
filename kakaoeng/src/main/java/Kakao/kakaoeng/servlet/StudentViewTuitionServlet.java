package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.FixedCalculateTuition;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Tuition;

@WebServlet("/m4/studentTuition.view")
public class StudentViewTuitionServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int month = 1;
		
		try{
			month = Integer.parseInt(req.getParameter("month"));
		}catch(NumberFormatException e)
		{
			month = 1;
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		FixedCalculateTuition ct = new FixedCalculateTuition(environDAO, month, 2, 1);
		Tuition t = ct.calculate();
		req.setAttribute("FixPrice1", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("FixFinalPrice1", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(2);
		ct.setDuration(2);
		t = ct.calculate();
		req.setAttribute("FixPrice2", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("FixFinalPrice2", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(3);
		ct.setDuration(1);
		t = ct.calculate();
		req.setAttribute("FixPrice3", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("FixFinalPrice3", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(3);
		ct.setDuration(2);
		t = ct.calculate();
		req.setAttribute("FixPrice4", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("FixFinalPrice4", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(5);
		ct.setDuration(1);
		t = ct.calculate();
		req.setAttribute("FixPrice5", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("FixFinalPrice5", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(5);
		ct.setDuration(2);
		t = ct.calculate();
		req.setAttribute("FixPrice6", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("FixFinalPrice6", Util.getFormattedPrice(t.getPurchasePrice()));
		
		req.setAttribute("DayDiscount", environDAO.getDayDiscountPercentString());
		req.setAttribute("MonthDiscount", environDAO.getMonthDiscountPercentString(month));
		
		ct.setDuration(1);
		
		ct.setTimes(2);
		t = ct.calculate();
		req.setAttribute("Smart1", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("SmartFinal1", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(3);
		t = ct.calculate();
		req.setAttribute("Smart2", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("SmartFinal2", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(4);
		t = ct.calculate();
		req.setAttribute("Smart3", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("SmartFinal3", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(5);
		t = ct.calculate();
		req.setAttribute("Smart4", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("SmartFinal4", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(6);
		t = ct.calculate();
		req.setAttribute("Smart5", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("SmartFinal5", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(7);
		t = ct.calculate();
		req.setAttribute("Smart6", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("SmartFinal6", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(8);
		t = ct.calculate();
		req.setAttribute("Smart7", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("SmartFinal7", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(9);
		t = ct.calculate();
		req.setAttribute("Smart8", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("SmartFinal8", Util.getFormattedPrice(t.getPurchasePrice()));
		ct.setTimes(10);
		t = ct.calculate();
		req.setAttribute("Smart9", Util.getFormattedPrice(t.getOriginalPrice()));
		req.setAttribute("SmartFinal9", Util.getFormattedPrice(t.getPurchasePrice()));
		
		req.getRequestDispatcher("./course.jsp?month="+month+"&k=a#sec2").forward(req, resp);
	}

}
