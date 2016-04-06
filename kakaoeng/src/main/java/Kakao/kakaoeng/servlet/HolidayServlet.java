package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.HolidayDAO;

@SuppressWarnings("serial")
@WebServlet("/teacher/holidayServlet.do")
public class HolidayServlet extends HttpServlet {

	static Logger logger = Logger.getLogger(HolidayServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info(req.getRemoteAddr() + " access to GET /holidayServlet.do");
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		try{
			year = Integer.parseInt(req.getParameter("year"));
		}catch(Exception e){}
		
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		cal.set(year, 0, 1, 0, 0, 0);
		Date start =cal.getTime();
		cal.set(year, 11, 31, 0,0,0);
		Date end = cal.getTime();
		List<Date> holidayList = holidayDAO.findHolidayWithRange(start, end);
		List<String> holidayFormList = new ArrayList<>();
		
		for(Date date : holidayList){
			holidayFormList.add(Util.dateFormatting(date));
		}
		
		req.setAttribute("holidayFormList", holidayFormList);
		
		req.getRequestDispatcher("./admin_holiday.jsp?year="+year).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info(req.getRemoteAddr() + " access to POST /holidayServlet.do");
		String dateListString = req.getParameter("dateList");
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonObject = null;
		
		try {
			jsonObject = (JSONArray) jsonParser.parse(dateListString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<String> dateStringList = new ArrayList<>();
		for(int i=0; i<jsonObject.size(); i++)
			dateStringList.add((String) jsonObject.get(i));
		
		List<Date> dateList =  new ArrayList<>();
		for(String date : dateStringList){
			dateList.add(Util.parseDate(date));
		}
		
		String deselectString = req.getParameter("deselect");
		
		try {
			jsonObject = (JSONArray) jsonParser.parse(deselectString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<String> deselectStringList = new ArrayList<>();
		for(int i=0; i<jsonObject.size(); i++)
			deselectStringList.add((String) jsonObject.get(i));
		
		List<Date> deselectList =  new ArrayList<>();
		for(String date : deselectStringList){
			deselectList.add(Util.parseDate(date));
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		Collections.sort(deselectList);
		for(Date date : deselectList){
			holidayDAO.deleteTransaction(date);
		}
		
		
		Collections.sort(dateList);
		for(Date date : dateList)
			holidayDAO.registerHolidayTransaction(date);
		
		
		
		Util.sendSuccess(resp, "Succesfully registered");
	}

	
}
