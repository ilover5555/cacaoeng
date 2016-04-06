package Kakao.kakaoeng;

import java.util.Calendar;
import java.util.Date;

import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

public class DateValueObject{
	private Date date;
	private int count;
	private boolean holiday = false;
	private Date today = new Date();
	
	public DateValueObject(Date date){
		this.date = date;
	}
	
	public String getColor(){
		if((Util.getYear(date) == Util.getYear(today)) &&
				(Util.getMonth(date) == Util.getMonth(today)) &&
				(Util.getDate(date) == Util.getDate(today)))
			return "white";
		
		else
			return "black";
	}
	
	public String getBackgroundColor(){
		if((Util.getYear(date) == Util.getYear(today)) &&
				(Util.getMonth(date) == Util.getMonth(today)) &&
				(Util.getDate(date) == Util.getDate(today)))
			return "rgb(56,143,212)";
		
		if(Util.getDayOfWeek(date) == Calendar.SUNDAY)
			return "rgb(255,161,161)";
		
		if(Util.getDayOfWeek(date) == Calendar.SATURDAY)
			return "rgb(219,233,246)";
		
		return "transparent";
	}
	
	public DateValueObject(Date date, boolean holiday){
		this.date = date;
		this.holiday = holiday;
	}
	
	public void addCount(){
		count++;
	}
	
	public boolean getHoliday(){
		return this.holiday;
	}
	
	public int getCount(){
		return this.count;
	}
	
	public String getDateForm(){
		return Util.dateFormatting(date);
	}
	
	public 	String getDayOfWeek(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return DayOfWeek.getInstanceFromCalendarCode(cal.get(Calendar.DAY_OF_WEEK)).name();
	}
}