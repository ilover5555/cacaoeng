package Kakao.kakaoeng.domain.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.RegisterTime.TimeType;

public class Duration {
	private RegisterTime rt;
	private int duration;
	public Duration(RegisterTime start, int duration){
		super();
		this.rt = start;
		this.duration = duration;
	}
	
	public Date getStartDateObject(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, rt.getHour());
		cal.set(Calendar.MINUTE, rt.getTime().getMinute());
		return cal.getTime();
	}
	/**
	 * If class starts at 9:00 and duration is 2 -> It will return 10:00
	 * @return
	 */
	public Date getEndDateObject(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getStartDateObject());
		cal.add(Calendar.MINUTE, 30*this.duration);
		return cal.getTime();
	}
	
	public Duration(int startStamp, int duration) {
		this(new RegisterTime(startStamp),duration);
	}
	public RegisterTime getRt() {
		return rt;
	}
	public int getDuration() {
		return duration;
	}
	public int getEndTime(){
		int result = rt.getStamp();
		for(int i=1; i<duration; i++){
			TimeType type = TimeType.getInstanceFromStamp(result);
			result = result + type.getIntervalWithNextTime();
		}
		return result;
	}
	
	public static int getDurationFromClassType(String classType){
		if(classType.equals("halfTypeSelect")){
			return 1;
		}
		else if(classType.equals("fullTypeSelect")){
			return 2;
		}
		else
			throw new IllegalArgumentException("Duration.getDurationFromClassType get illegal argument : " + classType);
	}
	
	@Override
	public String toString() {
		RegisterTime endRt = new RegisterTime(this.getEndTime());
		return String.format("(%s ~ %s)", rt.toString(), endRt.toString());
	}
	
	public List<Duration> split(int parseDuration){
		List<Duration> result = new ArrayList<>();
		
		int index = duration - parseDuration + 1;
		int selectedStamp = this.getRt().getStamp();
		for(int i=0; i<index; i++){
			Duration parsed = new Duration(selectedStamp, parseDuration);
			result.add(parsed);
			selectedStamp = TimeType.getNextStamp(selectedStamp);
		}
		
		return result;
	}
	/**
	 * grouping continuous RegisterTime as Duration.
	 * If continuous RegisterTime group's length is smaller than requiredDuration, it will be ignored(one continuous RegisterTime group)
	 * @param parsedList : List of RegisterTime for to be parsed
	 * @param requiredDuration : needed duration
	 * @return List for the duration, one duration equivalent to continuous RegisterTime
	 */
	public static List<Duration> parseRegisterTime(List<RegisterTime> parsedList, int requiredDuration){
		List<Duration> result = new ArrayList<>();
		
		int start = 0;
		int prev = 0;
		int selected = 0;
		int duration = 1;
		for(int i=0; i<parsedList.size(); i++){
			RegisterTime rt = parsedList.get(i);
			if(start == 0){
				start = rt.getStamp();
				prev = start;
				continue;
			}
			selected = rt.getStamp();
			if(TimeType.getNextStamp(prev) == selected){
				prev = selected;
				duration++;
			}
			else{
				if((duration >= requiredDuration) && (start != 0)){
					Duration parsedDuration = new Duration(start, duration);
					result.add(parsedDuration);
					i--;
					start = 0;
					duration = 1;
				}
				else{
					start = selected;
					prev = start;
					duration = 1;
				}
			}
		}
		if((duration >= requiredDuration) && (start != 0)){
			Duration parsedDuration = new Duration(start, duration);
			result.add(parsedDuration);
		}
		
		return result;
	}
	
	public List<RegisterTime> converToRegisterList(){
		List<RegisterTime> result = new ArrayList<>();
		
		int iterStamp = this.rt.getStamp();
		for(int i=0; i<duration; i++){
			RegisterTime element = new RegisterTime(iterStamp);
			result.add(element);
			iterStamp = TimeType.getNextStamp(iterStamp);
		}
		
		return result;
	}
}
