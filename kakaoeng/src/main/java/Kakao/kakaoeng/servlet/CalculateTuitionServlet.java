package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.CalculateTuition;
import Kakao.kakaoeng.ClassSearchUnit;
import Kakao.kakaoeng.FixedCalculateTuition;
import Kakao.kakaoeng.LevelTestCalculateTuition;
import Kakao.kakaoeng.SmartCalculateTuition;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.RegisterTime;
import Kakao.kakaoeng.domain.model.Tuition;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;
import Kakao.kakaoeng.domain.model.RegisterTime.TimeType;

@WebServlet("/calculateTuition.do")
public class CalculateTuitionServlet extends HttpServlet {

	public static CalculateTuition getCalculator(String matchSort, List<ClassSearchUnit> bookList, EnvironDAO environDAO){
		CalculateTuition ct = null;
		switch(matchSort){
		case "smart":
		case "teachr":
			ct = new SmartCalculateTuition(bookList, environDAO);
			break;
		case "fixed":
			ct = new SmartCalculateTuition(bookList, environDAO);
			break;
		case "level":
			ct = new LevelTestCalculateTuition(bookList, environDAO);
			break;
		default:
			break;
		}
		return ct;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");

		String id = req.getParameter("id");
		int length = Integer.parseInt(req.getParameter("length"));
		String baseDateString = req.getParameter("baseDate");
		
		Date date = Util.parseDate(baseDateString);
		
		List<ClassSearchUnit> bookList = new ArrayList<>();
		for(int i=0; i<length; i++){
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = null;
				try {
					jsonObject = (JSONObject)jsonParser.parse(req.getParameter("index"+i));
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			String dayOfWeekString = (String) jsonObject.get("dayOfWeek");
			DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekString);
			int hour = (int)(long) jsonObject.get("hour");
			int minute =(int)(long) jsonObject.get("minute");
			int duration = (int)(long) jsonObject.get("duration");
			int weeks = (int)(long) jsonObject.get("weeks");
			
			List<DayOfWeek> tested = new ArrayList<>();
			tested.add(dayOfWeek);
			Map<DayOfWeek, Date> lateDate = Util.getLatestDateForSpecificDayOfWeek(tested, date);
			
			
			RegisterTime rt = new RegisterTime(dayOfWeek, hour, TimeType.getInstanceFromMinute(minute));
			Duration d = new Duration(rt, duration);
			bookList.add(new ClassSearchUnit(d, lateDate.get(dayOfWeek), weeks));
		}
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		String matchSort = req.getParameter("matchSort");
		CalculateTuition ct = null;
		ct = getCalculator(matchSort, bookList, environDAO);
		
		Tuition tuition = null;
		try{
			tuition = ct.calculate();
		}catch(RuntimeException e){
			Util.sendError(resp, e.getMessage());
			return;
		}
		JSONObject obj = new JSONObject();
		
		obj.put("result", "급여 계산 성공");
		obj.put("finalPrice", tuition.getFinalPrice());
		obj.put("monthDiscountPercent", tuition.getMonthDiscountPercentString());
		obj.put("typeDiscountName", tuition.getTypeDiscountName());
		obj.put("typeDiscountPercent", tuition.getTypeDisCountPercentString());
		obj.put("purchasePrice", tuition.getPurchasePrice());
		obj.put("dayDiscountPercent", tuition.getDayDiscountPercentString());
		
		Util.sendSuccess(resp, obj.toJSONString());
	}
}
