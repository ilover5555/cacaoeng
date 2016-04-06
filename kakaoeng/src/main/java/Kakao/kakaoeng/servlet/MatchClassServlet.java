package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.ClassSearchUnit;
import Kakao.kakaoeng.MatchLibrary;
import Kakao.kakaoeng.TeacherWithCount;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.RegisterTime;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;
import Kakao.kakaoeng.domain.model.RegisterTime.TimeType;

@SuppressWarnings("serial")
@WebServlet("/m3/match.do")
public class MatchClassServlet extends HttpServlet {
	
	public List<ClassSearchUnit> fixedMatch(HttpServletRequest req) {
		final List<ClassSearchUnit> findList = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String baseDateString = req.getParameter("baseDate");
		Date baseDate = null;
		try {
			baseDate = format.parse(baseDateString);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		int hour = Integer.parseInt(req.getParameter("hour"));
		int minute = Integer.parseInt(req.getParameter("minute"));
		String classDuration = req.getParameter("classDuration");
		String monthDuration = req.getParameter("monthDuration");
		String period = req.getParameter("period");
		int weeks = -1;

		List<DayOfWeek> dayOfWeeks = new ArrayList<>();

		if (period.equals("twice")) {
			dayOfWeeks.add(DayOfWeek.Tuesday);
			dayOfWeeks.add(DayOfWeek.Thursday);
		} else if (period.equals("thrice")) {
			dayOfWeeks.add(DayOfWeek.Monday);
			dayOfWeeks.add(DayOfWeek.Wednesday);
			dayOfWeeks.add(DayOfWeek.Friday);
		} else if (period.equals("fiveTimes")) {
			dayOfWeeks.add(DayOfWeek.Monday);
			dayOfWeeks.add(DayOfWeek.Tuesday);
			dayOfWeeks.add(DayOfWeek.Wednesday);
			dayOfWeeks.add(DayOfWeek.Thursday);
			dayOfWeeks.add(DayOfWeek.Friday);
		} else {
			throw new RuntimeException("MatchServlet get Invalid period");
		}

		weeks = Util.getWeeksFromMonthDuration(monthDuration);

		Map<DayOfWeek, Date> findDate = Util.getLatestDateForSpecificDayOfWeek(dayOfWeeks, baseDate);

		for (DayOfWeek element : findDate.keySet()) {
			Date startDate = findDate.get(element);
			findList.add(
					new ClassSearchUnit(new Duration(new RegisterTime(element, hour, TimeType.getInstanceFromStamp(minute)),
							Duration.getDurationFromClassType(classDuration)), startDate, weeks));
		}
		return findList;
	}

	public List<ClassSearchUnit> smartMatch(HttpServletRequest req) {

		final List<ClassSearchUnit> findList = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String baseDateString = req.getParameter("baseDate");
		Date baseDate = null;
		try {
			baseDate = format.parse(baseDateString);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		String monthDuration = req.getParameter("monthDuration");
		int weeks = -1;
		
		String o =  req.getParameter("data");
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject)jsonParser.parse(o);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray avails =  (JSONArray)jsonObject.get("avail");
		
		List<String> availList = new ArrayList<String>();
		
		for(int i=0; i<avails.size(); i++)
			availList.add((String)avails.get(i));

		weeks = Util.getWeeksFromMonthDuration(monthDuration);
		
		Map<DayOfWeek, List<RegisterTime>> timeGroupedByDayOfWeek = new HashMap<>();
		
		for(String stamp : availList){
			int stampInt = Integer.parseInt(stamp);
			DayOfWeek dow = DayOfWeek.getInstanceFromStamp(stampInt);
			if(!timeGroupedByDayOfWeek.containsKey(dow))
				timeGroupedByDayOfWeek.put(dow, new ArrayList<>());
			timeGroupedByDayOfWeek.get(dow).add(new RegisterTime(stampInt));
		}
		
		Map<DayOfWeek, List<Duration>> durationGroupedByDayOfWeek = new HashMap<>();
		
		for(DayOfWeek key : timeGroupedByDayOfWeek.keySet()){
			List<RegisterTime> registerList = timeGroupedByDayOfWeek.get(key);
			List<Duration> durationList = Duration.parseRegisterTime(registerList, 1);
			if(!durationGroupedByDayOfWeek.containsKey(key))
				durationGroupedByDayOfWeek.put(key, new ArrayList<>());
			durationGroupedByDayOfWeek.get(key).addAll(durationList);
		}
		List<DayOfWeek> dayOfWeekList = new ArrayList<>();
		for(DayOfWeek key : durationGroupedByDayOfWeek.keySet()){
			dayOfWeekList.add(key);
		}
		Map<DayOfWeek, Date> findDate = Util.getLatestDateForSpecificDayOfWeek(dayOfWeekList, baseDate);
		Map<DayOfWeek, Date> endDate = Util.getEndDateMap(findDate, weeks);
		
		Date realStartDate = Util.findEariestDate(findDate);
		Date realEndDate = Util.findLatestDate(endDate);

		req.setAttribute("realStartDate", format.format(realStartDate));
		req.setAttribute("realEndDate", format.format(realEndDate));
		
		for (DayOfWeek element : findDate.keySet()) {
			Date startDate = findDate.get(element);
			for(Duration duration : durationGroupedByDayOfWeek.get(element)){
				findList.add(new ClassSearchUnit(
						new Duration(new RegisterTime(element, duration.getRt().getHour(), duration.getRt().getTime()),
								duration.getDuration()),
						startDate, weeks));
			}
		}
		return findList;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext()
				.getAttribute("applicationContext");
		MatchLibrary match = new MatchLibrary();

		String matchSort = req.getParameter("matchSort");

		List<ClassSearchUnit> findList = null;
		switch (matchSort) {
		case "fixed":
			findList = fixedMatch(req);
			break;
		case "smart":
			findList = smartMatch(req);
			break;
		default:
			throw new IllegalArgumentException("matchSort is invalid : " + matchSort);
		}
		
		if(findList.size() < 1){
			Util.sendError(resp, "1패턴 미만");
			return;
		}
		if(findList.size() > 8){
			Util.sendError(resp, "한 주에 가능한 수업신청 회수는 8회 입니다. ");
			return;
		}
		
		int duration = 0;
		for(ClassSearchUnit csu : findList){
			duration += csu.getDuration().getDuration();
		}
		
		if(duration < 2){
			Util.sendError(resp, "적어도 50분 이상은 수업신청해야 합니다.");
			return;
		}
		
		if(duration > 40){
			Util.sendError(resp, "1000분(40타임)이상은 신청이 불가능합니다. 관리자에게 문의하세요");
			return;
		}
		
		final Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> exactResult = new TreeMap<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>>();
		final Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> similarResult = new TreeMap<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>>();

		match.process(exactResult, similarResult, applicationContext, findList);

		req.setAttribute("exactMap", exactResult);
		req.setAttribute("similarMap", similarResult);
		
		if((exactResult.size() + similarResult.size()) == 0){
			Util.sendError(resp, "죄송합니다.\n요청하신 시간과 일치하는 선생님을 찾지 못했습니다.");
			return;
		}
		System.out.println(req.getRequestURI().substring(req.getContextPath().length()));
		System.out.println(req.getHeader("referer"));
		System.out.println(req.getContextPath());
		req.getRequestDispatcher("./TeacherMatchingResult.jsp").forward(req, resp);
	}

}
