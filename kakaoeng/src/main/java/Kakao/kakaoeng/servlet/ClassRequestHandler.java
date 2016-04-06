package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.RegisterTime;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

@SuppressWarnings("serial")
@WebServlet("/classRequest.do")
public class ClassRequestHandler extends HttpServlet {

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String clickedString = req.getParameter("stamps");
		List<Integer> stampList = Util.parseStampJSON(clickedString);
		Collections.sort(stampList);
		List<RegisterTime> rtList = new ArrayList<>();
		for(Integer stamp : stampList){
			RegisterTime rt = new RegisterTime(stamp);
			rtList.add(rt);
		}
		String monthDuration = req.getParameter("monthDuration");
		int weeks = Util.getWeeksFromMonthDuration(monthDuration);
		List<Duration> durationList = Duration.parseRegisterTime(rtList, 1);
		
		String baseDate = req.getParameter("baseDate");
		String id = req.getParameter("id");
		String name = req.getParameter("name");
		JSONObject result = new JSONObject();
		result.put("baseDate", baseDate);
		result.put("id", id);
		result.put("name", name);
		int i=0;
		for(i=0; i<durationList.size(); i++){
			Duration duration = durationList.get(i);
			JSONObject jObject = new JSONObject();
			jObject.put("dayOfWeek", duration.getRt().getDayOfWeek().toString());
			jObject.put("hour", new Integer(duration.getRt().getHour()));
			jObject.put("minute", duration.getRt().getTime().getMinute());
			jObject.put("duration", duration.getDuration());
			jObject.put("weeks", new Integer(weeks));
			result.put("index"+i, jObject.toJSONString());
		}
		result.put("length", i);
		
		Date startDate = Util.parseDate(baseDate);
		List<DayOfWeek> dowList = new ArrayList<>();
		
		for(Duration element : durationList){
			DayOfWeek dow = element.getRt().getDayOfWeek();
			if(!dowList.contains(dow))
				dowList.add(dow);
		}

		Map<DayOfWeek, Date> firstDateSet = Util.getLatestDateForSpecificDayOfWeek(dowList, startDate);
		Map<DayOfWeek, Date> lastDateSet = Util.getEndDateMap(firstDateSet, weeks);
		
		Date realStartDate = Util.findEariestDate(firstDateSet);
		Date realEndDate = Util.findLatestDate(lastDateSet);
		
		result.put("realStartDate", Util.dateFormatting(realStartDate));
		result.put("realEndDate", Util.dateFormatting(realEndDate));
		
		result.writeJSONString(resp.getWriter());
	}

}
