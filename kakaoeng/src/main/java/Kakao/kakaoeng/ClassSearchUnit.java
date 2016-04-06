package Kakao.kakaoeng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;
import Kakao.kakaoeng.domain.model.RegisterTime.TimeType;

public class ClassSearchUnit implements Comparable<ClassSearchUnit>{
	Duration duration;
	boolean exact = false;
	Date startDate;
	Date endDate;
	int weeks;
	int parent=-1;
	int oneClassId;

	public int getOneClassId() {
		return oneClassId;
	}

	public void setOneClassId(int oneClassId) {
		this.oneClassId = oneClassId;
	}

	public List<ClassTime> getClassTimeList(){
		List<ClassTime> result = new ArrayList<>();
		
		int stamp = getStartStamp();
		for(int i=0; i<duration.getDuration(); i++){
			result.add(new ClassTime(null, stamp));
			stamp = TimeType.getNextStamp(stamp);
		}
		
		return result;
	}
	
	public void setParent(int parent) {
		this.parent = parent;
	}

	public int getStartHour(){
		return this.duration.getRt().getHour();
	}
	
	public int getStartMinute(){
		return this.duration.getRt().getTime().getMinute();
	}
	
	public String getTimeString(){
		return String.format("%02d:%02d", duration.getRt().getHour(), duration.getRt().getTime().getCode()*30);
	}
	
	public DayOfWeek getDayOfWeek(){
		return duration.getRt().getDayOfWeek();
	}
	
	public String getDayOfWeekKorean(){
		return duration.getRt().getDayOfWeek().getDayOfWeekKorean();
	}
	
	public String getStartDateString(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(startDate);
	}
	
	public String getEndDateString(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(endDate);
	}
	
	public ClassSearchUnit(Duration duration, Date startDate, int weeks, boolean exact) {
		this(duration, startDate, weeks);
		this.exact = exact;
	}

	public int getParent() {
		return parent;
	}

	public ClassSearchUnit(Duration duration, Date startDate, int weeks) {
		this(duration, startDate, weeks, -1);
	}
	
	public ClassSearchUnit(Duration duration, Date startDate, int weeks, int parent) {
		this(duration, startDate, weeks, parent, -1);
	}
	
	public ClassSearchUnit(Duration duration, Date startDate, int weeks, int parent, int oneClassId) {
		super();
		this.duration = duration;
		this.startDate = startDate;
		this.weeks = weeks;
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.WEEK_OF_YEAR, weeks);
		endDate = cal.getTime();
		this.exact = false;
		this.parent = parent;
		this.oneClassId = oneClassId;
	}

	public int getTimeDiffer(Duration d) {
		if (d.getDuration() != this.duration.getDuration())
			throw new IllegalArgumentException(
					"sortclass$getTimeDiffer get Illegal Argument Duration. Both duration must be same");
		
		int before = -1;
		int after = -1;
		if(this.getStartStamp() < d.getRt().getStamp()){
			before = this.duration.getRt().getStamp();
			after = d.getRt().getStamp();
		}
		else if(this.getStartStamp() > d.getRt().getStamp()){
			before = d.getRt().getStamp();
			after = this.duration.getRt().getStamp();
		}
		else
			return 0;
		int differ = 1;
		
		while(true){
			if(TimeType.getNextStamp(before) == after)
				break;
			before = TimeType.getNextStamp(before);
			
			differ++;
			
			if(differ >= 100)
				throw new IllegalStateException("sortclass$getTimeDiffer is in infinite loop");
		}
		
		if(this.getStartStamp() > d.getRt().getStamp())
			differ = differ*-1;
		
		return differ;
	}

	public int getWeeks() {
		return this.weeks;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void thisIsExact() {
		this.exact = true;
	}

	public boolean getExact() {
		return this.exact;
	}

	public Duration getDuration() {
		return duration;
	}

	public int getStartStamp() {
		return this.duration.getRt().getStamp();
	}

	public int getEndStamp() {
		return duration.getEndTime();
	}

	@Override
	public String toString() {
		return String.format("%s", duration.toString());
	}

	@Override
	public int compareTo(ClassSearchUnit o) {
		int result = this.getDuration().getRt().getDayOfWeek().compareTo(o.getDuration().getRt().getDayOfWeek());
		if(result != 0)
			return result;
		
		if(this.getStartStamp() > o.getStartStamp())
			return 1;
		else if(this.getStartStamp() < o.getStartStamp())
			return -1;
		else
			return 0;
	}
}
