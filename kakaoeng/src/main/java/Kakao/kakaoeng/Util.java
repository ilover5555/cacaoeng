package Kakao.kakaoeng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.dao.ClassTimeUsageLogDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;
import Kakao.kakaoeng.domain.model.Teacher.Rate;
import Kakao.kakaoeng.field.Field;

public class Util {

	public static String base = "";
	public final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat detailFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
	public static List<ClassState> doneClassStateList;
	public static Logger logger = Logger.getLogger(Util.class);
	
	static{
		List<ClassState> temp = new ArrayList<>();
		temp.add(ClassState.Completed);
		temp.add(ClassState.AbsentStudent);
		temp.add(ClassState.LevelTestReserved);
		temp.add(ClassState.LevelTestCompleted);
		temp.add(ClassState.LevelTestUncompleted);
		doneClassStateList = Collections.unmodifiableList(temp);
	}
	
	public static String getFormattedPrice(int price){
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(price)+"원";
	}
	
	public static String getFullURL(HttpServletRequest request) {
	    StringBuffer requestURL = request.getRequestURL();
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
	
	public static String getPartFileName(Part part){
		
		if(part.getName() == null)
			return null;
		String disposition = part.getHeader("content-disposition");
		int index = disposition.indexOf("filename=\"");
		String filename = disposition.substring(index);
		String[] splitted = filename.split("\"");
		String file = splitted[1];
		String[] splitted2 = file.split("[\\\\/]");
		return splitted2[splitted2.length-1];
	}
	
	public static double getDoubleFromPercentString(String percentString){
		Number n = null;
		try {
			n = new DecimalFormat("0.0#%").parse(percentString);
		} catch (ParseException e) {
			String temp = percentString + "%";
			try {
				n = new DecimalFormat("0.0#%").parse(temp);
			} catch (ParseException e1) {
				throw new RuntimeException(e);
			}	
		}
		
		return n.doubleValue();
	}
	
	public static String getPercentStringFromDouble(double d, int round){
		if(d == 0)
			return "0%";
		long q = 1;
		for(int i=0; i<round; i++){
			q *= 10;
		}
		long k = (long) (d*q);
		int r_round = 0;
		for(int i=0; i<round; i++){
			int m = (int) (k % 10);
			k = k/10;
			if(m != 0)
				break;
			r_round++;
		}
		return String.format("%."+r_round+"f%%", d*q);
	}
	
	public static String getPercentStringFromDouble(double d){
		return getPercentStringFromDouble(d, 2);
	}
	
	public String fileToString(File file) {
	    String fileString = new String();
	    FileInputStream inputStream =  null;
	    ByteArrayOutputStream byteOutStream = null;

	    try {
	        inputStream = new FileInputStream(file);
		byteOutStream = new ByteArrayOutputStream();
	    
		int len = 0;
		byte[] buf = new byte[1024];
	        while ((len = inputStream.read(buf)) != -1) {
	             byteOutStream.write(buf, 0, len);
	        }

	        byte[] fileArray = byteOutStream.toByteArray();
	        fileString = new String(Base64.encodeBase64(fileArray));
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	    	try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				byteOutStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    return fileString;
	}
	
	public static String fillContent(String s, Object o){
		Pattern pattern = Pattern.compile("([{][{])([a-zA-Z0-9]+)([}][}])");
		Matcher matcher = pattern.matcher(s);
		
		Class<?> c = o.getClass();
		
		StringBuffer sb = new StringBuffer();
		while(matcher.find()){
			matcher.appendReplacement(sb, getter(o, matcher.group(2)).toString());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public static Object getter(Object o, String name){
		Class<?> c = o.getClass();
			Method method = null;
			try {
				String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
				method = c.getMethod("get"+cap, null);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			}
			Object result = null;
			try {
				result = method.invoke(o, null);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
			return result;
	}
	
	public static String decode(String value){
		String decode = null;
		try {
			decode = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decode;
	}
	
	public static int getYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	public static int getMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH)+1;
	}
	
	public static int getDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}
	
	public static void debugRequstParameter(HttpServletRequest req){
		/*
		for(Enumeration<String> e = req.getHeaderNames(); e.hasMoreElements();){
			String name = e.nextElement();
			System.out.println("name:"+name+",value:"+req.getHeader(name));
		}
		System.out.println("noHeader:"+req.getHeader("noHeader"));
		*/
		for(String key : req.getParameterMap().keySet()){
			logger.info("key:"+key+",value:"+req.getParameter(key));
		}
	}
	
	public static Date normalize(Date date){
		Date result = new Date();
		result.setTime((date.getTime()/100000)*100000);
		
		return result;
	}
	
	public static boolean isCounted(Date date, HolidayDAO holidayDAO, ClassLogDAO classLogDAO, OneClass oneClass, Lecture lecture){
		if(date.before(lecture.getStartDate()))
			return false;
		if(holidayDAO.checkHoliday(date))
			return false;
		ClassLog classLog = classLogDAO.getClassLogByOneClassTransaction(oneClass.getId(), date);
		if(classLog==null)
			return false;
		if(Util.doneClassStateList.contains(classLog.getClassState()))
			return true;
		return false;
	}
	
	public static boolean isPro(Date date, HolidayDAO holidayDAO, ClassLogDAO classLogDAO, OneClass oneClass, Lecture lecture){
		if(date.before(lecture.getStartDate()))
			return false;
		if(holidayDAO.checkHoliday(date))
			return true;
		ClassLog classLog = classLogDAO.getClassLogByOneClassTransaction(oneClass.getId(), date);
		if(classLog==null)
			return false;
		if(Util.doneClassStateList.contains(classLog.getClassState()))
			return false;
		return true;
	}
	
	public static Date addDate(Date date, int d){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, d);
		return cal.getTime();
	}
	
	public static int getDayOfWeek(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Date getFirstDateOfMonth(int year, int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date getLastDateOfMonth(int year, int month){
		Date nextFirst = Util.getFirstDateOfMonth(year, month+1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(nextFirst);
		cal.add(Calendar.SECOND, -1);
		return cal.getTime();
	}
	
	public static List<Date> getFullMonthCalendar(int year, int month) {
		List<Date> result = new ArrayList<>();
		Date base = Util.getFirstDateOfMonth(year, month);
		Date nextMonth = Util.getFirstDateOfMonth(year, month+1);
		while (base.before(nextMonth) || base.equals(nextMonth)) {
			List<Date> list = Util.getWeekDateList(base);
			result.addAll(list);
			base.setTime(list.get(list.size() - 1).getTime() + (1000 * 60 * 60 * 24));
		}

		return result;
	}
	
	public static void sendError(int sc, HttpServletResponse resp, String error) throws IOException{
		resp.setStatus(sc);
		resp.setContentType("text/plain");
		resp.getWriter().write(error);
	}
	
	public static void sendError(HttpServletResponse resp, String error) throws IOException{
		Util.sendError(500, resp, error);
	}
	
	public static void sendSuccess(HttpServletResponse resp, String msg) throws IOException{
		resp.setStatus(200);
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
		pw.write(msg);
	}
	
	public static String generateState(){
		SecureRandom random = new SecureRandom();
		return new BigInteger(130,random).toString(32);
	}
	
	public static Lecture selectLecture(List<Lecture> lecture, OneClass oneClass){
		if(lecture == null || lecture.size() == 0)
			throw new IllegalArgumentException("selectLecture get empty lecture list.");
		
		if(lecture.size() == 1)
			return lecture.get(0);
			
		for(Lecture l : lecture){
			if(oneClass.getLectureId() == l.getId())
				return l;
		}
		return null;
	}
	
	public static Date parseDateIgnoreNull(String baseDateString){
		Date baseDate = null;
		if(baseDateString == null){
			baseDate = new Date();
			String parsed = Util.dateFormatting(baseDate);
			baseDate = Util.parseDate(parsed);
		}
		else
			baseDate = Util.parseDate(baseDateString);
		return baseDate;
	}
	
	public static List<ClassLog> getClassLogFromOneClass(List<Date> dateList, List<OneClass> oneClassList, ClassLogDAO classLogDAO){
		List<ClassLog> classLogList = new ArrayList<>();
		for(OneClass oc : oneClassList){
			int oneClassId = oc.getId();
			classLogList.addAll(classLogDAO.getClassLogListByOneClassTransaction(oneClassId, dateList.get(dateList.size()-1), dateList.get(0)));
		}
		return classLogList;
	}
	
	public static List<ClassTime> getClassListFromOneClassList(List<OneClass> oneClassList){
		
		List<ClassTime> reservedClassTime = new ArrayList<>();
		for(OneClass oc : oneClassList){
			reservedClassTime.addAll(oc.getClassTimeList());
		}
		return reservedClassTime;
	}
	
	public static List<OneClass> removeBeforeStartDate(Lecture lecture, List<OneClass> oneClass, Date baseDate){
		List<OneClass> result = new ArrayList<>(oneClass);
		for(Iterator<OneClass> iter = result.iterator(); iter.hasNext();){
			OneClass oc = iter.next();
			if(!oc.isAfterStartDate(baseDate, lecture))
				iter.remove();
		}
		
		return result;
	}
	
	public static List<OneClass> oneClassMapToList(Map<Lecture, List<OneClass>> oneClassMap){
		List<OneClass> result = new ArrayList<>();
		
		for(Lecture lecture : oneClassMap.keySet()){
			result.addAll(oneClassMap.get(lecture));
		}
		
		return result;
	}
	
	public static Map<Lecture, List<OneClass>> getReservedOneClass(OneClassDAO oneClassDAO, List<Lecture> lectureList){
		
		Map<Lecture, List<OneClass>> result = new HashMap<>();
		for(Lecture lecture : lectureList){
			List<OneClass> list = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			result.put(lecture, list);
		}
		return result;
	}
	
	public static List<Lecture> getLectureList(List<Date> dateList, LectureDAO lectureDAO,  String teacherId){
		
		List<Lecture> lectureList = lectureDAO.getLectureListByTeacherId(teacherId, dateList.get(dateList.size()-1), dateList.get(0));
		return lectureList;
	}
	
	public static List<ClassTime> getAvailClassTime(List<ClassTime> classTimesWithinMatching, ClassTimeUsageLogDAO classTimeUsageLogDAO,
			Date startDate, Date endDate){
		List<ClassTime> result = new ArrayList<>();
		for (Iterator<ClassTime> iter = classTimesWithinMatching.iterator(); iter.hasNext();) {
			ClassTime checked = iter.next();
			if (classTimeUsageLogDAO.checkClassTimeCanBeUsedStateTransaction(checked.getId(), startDate,endDate))
				result.add(checked);
		}
		return result;
	}
	
	public static List<Date> getWeekDateList(Date base){
		Calendar cal = Calendar.getInstance();
		cal.setTime(base);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int start = 0;
		start = 1-dayOfWeek;
		List<Date> weekList = new ArrayList<>();
		for(int i=0; i<7; i++){
			cal.add(Calendar.DATE, start);
			weekList.add(cal.getTime());
			start++;
			cal.setTime(base);
		}
		Collections.sort(weekList);
		
		return weekList;
	}
	
	public static String detailDateFormatting(Date date){
		return detailFormat.format(date);
	}
	
	public static String dateFormatting(Date date){
		return format.format(date);
	}
	
	public static Date findLatestDate(Map<DayOfWeek, Date> dateMap){
		Date result = null;
		if(dateMap.values().size() == 0){
			throw new IllegalArgumentException("findLatestDate get empty dateMap");
		}
		Collection<Date> dateCollection = dateMap.values();
		int i=0;
		for(Date element : dateCollection){
			i++;
			if(i==1){
				result = element;
				continue;
			}
			if(element.after(result))
				result = element;
		}
		
		return result;
	}
	
	public static Date findEariestDate(Map<DayOfWeek, Date> dateMap){
		Date result = null;
		if(dateMap.values().size() == 0){
			throw new IllegalArgumentException("findEariestDate get empty dateMap");
		}
		Collection<Date> dateCollection = dateMap.values();
		int i=0;
		for(Date element : dateCollection){
			i++;
			if(i==1){
				result = element;
				continue;
			}
			if(element.before(result))
				result = element;
		}
		
		return result;
	}
	
	public static Date getEndDate(Date startDate, int weeks){
		return addWeeksToDate(startDate, weeks);
	}
	
	public static Map<DayOfWeek, Date> getEndDateMap(Map<DayOfWeek, Date> startList, int weeks){
		Map<DayOfWeek, Date> result = new EnumMap<>(DayOfWeek.class);
		
		for(DayOfWeek dow : startList.keySet()){
			Date value = startList.get(dow);
			Date endDate = Util.getEndDate(value, weeks);
			if(result.containsKey(dow))
				throw new IllegalStateException("makeEndDate's startList has duplicate DayOfWeek.");
			result.put(dow, endDate);
		}
		
		return result;
	}
	
	public static List<Integer> parseStampJSON(String stampKeySetJSON){
		List<Integer> result = new ArrayList<>();
		String[] parsed = stampKeySetJSON.split("\"");
		int i = -1;
		for(String e : parsed){
			try{
				i = Integer.parseInt(e);
			}
			catch(RuntimeException except){
				continue;
			}
			result.add(i);
		}
		return result;
	}
	
	public static void printMap(Map<String, Field<?>> fieldMap){
		for(String key : fieldMap.keySet()){
			System.out.println("key : " + key + ", value : " + fieldMap.get(key).getValue());
		}
	}
	
	public static int getWeeksFromMonthDuration(String monthDuration){
		int weeks = -1;
		switch (monthDuration) {
		case "1month":
			weeks = 1 * 4;
			break;
		case "3month":
			weeks = 3 * 4;
			break;
		case "6month":
			weeks = 6 * 4;
			break;
		case "12month":
			weeks = 12 * 4;
			break;
		default:
			throw new IllegalArgumentException();
		}
		return weeks;
	}
	
	public static Date parseDate(String dateString){
		if(dateString == null || dateString.equals(""))
			throw new IllegalArgumentException("dateString is invalid : " + dateString);
		Date date = null;
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
		
		return date;
	}
	
	public static Date parseTimeStampDate(String dateString){
		if(dateString == null || dateString.equals(""))
			throw new IllegalArgumentException("dateString is invalid : " + dateString);
		Date date = null;
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
		try {
			date = timeStampFormat.parse(dateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
		
		return date;
	}
	
	public static Date addWeeksToDate(Date baseDate, int weeks){
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.WEEK_OF_YEAR, weeks);
		Date endDate = cal.getTime();
		return endDate;
	}
	
	public static List<ClassTime> getClassTimeCanBeUsedStateList(ClassTimeDAO classTimeDAO,
			ClassTimeUsageLogDAO classTimeUsageLogDAO, ClassSearchUnit item, Rate rate) {
		List<ClassTime> classTimesWithinMatching = classTimeDAO
				.getClassTimeWithinStampInSpecificRate(item.getStartStamp(), item.getEndStamp(), 2, rate);

		Util.removeUnavailable(classTimesWithinMatching, classTimeUsageLogDAO, item.getStartDate(), item.getEndDate());

		return classTimesWithinMatching;
	}
	
	public static void removeUnavailable(List<ClassTime> classTimesWithinMatching, ClassTimeUsageLogDAO classTimeUsageLogDAO,
			Date startDate, Date endDate){
		for (Iterator<ClassTime> iter = classTimesWithinMatching.iterator(); iter.hasNext();) {
			ClassTime checked = iter.next();
			if (!classTimeUsageLogDAO.checkClassTimeCanBeUsedStateTransaction(checked.getId(), startDate,endDate))
				iter.remove();
		}
	}

	public static Map<DayOfWeek, Date> getLatestDateForSpecificDayOfWeek(List<DayOfWeek> tested, Date baseDate) {
		Map<DayOfWeek, Date> result = new EnumMap<>(DayOfWeek.class);
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		for (int i = 0; i < 7; i++) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			DayOfWeek checked = DayOfWeek.getInstanceFromCalendarCode(dayOfWeek);
			if (tested.contains(checked))
				result.put(checked, cal.getTime());
			cal.add(Calendar.DATE, 1);
		}
		return result;
	}
}
