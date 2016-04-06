package Kakao.kakaoeng.domain.model;

import java.util.Calendar;
import java.util.Date;

import Kakao.kakaoeng.Util;

public class StudentSchedule {
	public enum StudentClassState{
		Completed, PostponeStudent, PostponeTeacher, AbsentStudent, AbsentTeacher, Uncompleted, NewClass
	}
	
	Date date;
	StudentClassState classState;
	int month;
	boolean start;
	public StudentSchedule(Date date, StudentClassState classState, int month) {
		this(date, classState, month, false);
	}
	
	public StudentSchedule(Date date, StudentClassState classState, int month, boolean start) {
		super();
		this.date = date;
		this.classState = classState;
		this.month = month;
		this.start = start;
	}
	public Date getDate() {
		return date;
	}
	public String getDateForm(){
		return Util.dateFormatting(date);
	}
	public StudentClassState getClassState() {
		return classState;
	}
	public int getMonth() {
		return month;
	}
	public boolean getValidation(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return (calendar.get(Calendar.MONTH)+1) == month;
	}
	public int getDay(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}
}
