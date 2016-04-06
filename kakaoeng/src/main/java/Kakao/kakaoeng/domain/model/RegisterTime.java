package Kakao.kakaoeng.domain.model;

import java.util.Calendar;
import java.util.Date;

public class RegisterTime {

	public enum DayOfWeek {
		Sunday(0, "일요일", "일"), Monday(1, "월요일", "월"), Tuesday(2, "화요일", "화"), Wednesday(3, "수요일", "수"), Thursday(4, "목요일", "목"), Friday(5, "금요일", "금"), Saturday(6, "토요일", "토");

		private int code;
		private String dayOfWeek;
		private String shortDayOfWeek;

		private DayOfWeek(int code, String dayOfWeek, String shortDayOfWeek) {
			this.code = code;
			this.dayOfWeek = dayOfWeek;
			this.shortDayOfWeek = shortDayOfWeek;
		}

		public int getCode() {
			return this.code;
		}
		
		public int getCalendarCode(){
			return this.code+1;
		}
		
		public String getShortDayOfWeek(){
			return this.shortDayOfWeek;
		}
		
		public static DayOfWeek getInstanceFromCalendarCode(int code){
			return DayOfWeek.getInstance(code-1);
		}
		
		public int getCustomCalndarCode(){
			switch(this){
			case Sunday:
				return 0;
			case Monday:
				return 1;
			case Tuesday:
				return 2;
			case Wednesday:
				return 3;
			case Thursday:
				return 4;
			case Friday:
				return 5;
			case Saturday:
				return 6;
			default:
				return -1;
			}
		}
		
		public static DayOfWeek getInstance(int code){
			for(DayOfWeek item : DayOfWeek.values())
			{
				if(item.getCode() == code)
					return item;
			}
			return null;
		}
		
		public String getDayOfWeekKorean(){
			return this.dayOfWeek;
		}
		
		public static DayOfWeek getInstanceFromStamp(int stamp){
			return DayOfWeek.getInstance(stamp/1000);
		}
		
		public static DayOfWeek getInstanceFromDate(Date date){
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return DayOfWeek.getInstanceFromCalendarCode(cal.get(Calendar.DAY_OF_WEEK));
		}
	}

	public enum TimeType {
		FirstHalf(0) {
			@Override public int getMinute() {return 0;}
			@Override public int getIntervalWithNextTime() {return 1;}}
		, 
		SecondHalf(1) {
			@Override public int getMinute() {return 30;}
			@Override public int getIntervalWithNextTime() {return 9;}};

		private int code;

		private TimeType(int code) {
			this.code = code;
		}

		public int getCode() {
			return this.code;
		}
		
		public static TimeType getInstanceFromCode(int code){
			for(TimeType item : TimeType.values())
			{
				if(item.getCode() == code)
					return item;
			}
			return null;
		}
		
		public static TimeType getInstanceFromStamp(int stamp){
			int type = stamp%10;
			return getInstanceFromCode(type);
		}
		
		public static TimeType getInstanceFromMinute(int minute){
			if(minute == 0)
				return TimeType.FirstHalf;
			else if(minute == 30)
				return TimeType.SecondHalf;
			else
				throw new IllegalArgumentException("TimeType.getinstanceFromMinute get illegal argument : " + minute);
		}
		
		public static int getNextStamp(int stamp){
			TimeType type = getInstanceFromStamp(stamp);
			return stamp + type.getIntervalWithNextTime();
		}
		
		
		
		abstract public int getMinute();
		abstract public int getIntervalWithNextTime();
	}

	private DayOfWeek dayOfWeek;
	private int hour;
	private TimeType time;

	public RegisterTime(String stamp){
		this(Integer.parseInt(stamp));
	}
	
	public RegisterTime(int stamp){
		int time = stamp%10;
		stamp = stamp/10;
		int hour = stamp%100;
		stamp = stamp/100;
		int dayOfWeek = stamp%10;
		
		this.dayOfWeek = DayOfWeek.getInstance(dayOfWeek);
		this.hour = hour;
		this.time = TimeType.getInstanceFromCode(time);
	}
	
	public RegisterTime(DayOfWeek dayOfWeek, int hour, TimeType time) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.hour = hour;
		this.time = time;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public int getHour() {
		return hour;
	}

	public TimeType getTime() {
		return time;
	}

	public int getStamp(){
		String stamp = String.format("%d%02d%d", dayOfWeek.getCode(), hour, time.getCode());
		return Integer.parseInt(stamp);
	}
	
	public String getStringStamp(){
		return String.format("%d%02d%d", dayOfWeek.getCode(), hour, time.getCode());
	}

	@Override
	public String toString() {
		return String.format("%s %02d:%02d", this.dayOfWeek.toString(), hour, time.getMinute());
		
	}

	public String getStartTimeFormat(){
		return String.format("%02d:%02d", this.hour, this.time.getMinute());
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getStamp() == ((RegisterTime)obj).getStamp();
	}
	
	public String getEndTimeString(){
		int nextStamp = getStamp() + time.getIntervalWithNextTime();
		return new RegisterTime(nextStamp).toString();
	}
}