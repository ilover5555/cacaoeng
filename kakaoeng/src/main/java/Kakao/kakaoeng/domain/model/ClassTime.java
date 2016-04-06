package Kakao.kakaoeng.domain.model;

public class ClassTime {
	int id;
	String teacherId;
	RegisterTime time;
	int stamp;
	boolean disabled;
	
	@Override
	public boolean equals(Object obj) {
		ClassTime r = (ClassTime) obj;
		boolean result = this.teacherId.equals(r.getTeacherId());
		if(result == false)
			return false;
		else
			return this.time.equals(r.time);
	}

	public ClassTime(String teacherId, String stamp){
		this(teacherId, Integer.parseInt(stamp));
	}
	
	public ClassTime(String teacherId, int stamp){
		this(-1, teacherId, new RegisterTime(stamp), stamp, false);
	}
	
	public ClassTime(RegisterTime time){
		this(-1, null, time, time.getStamp(), false);
	}
	
	public ClassTime(int id, String teacherId, RegisterTime time, int stamp, boolean disabled) {
		super();
		this.id = id;
		this.time = time;
		this.stamp = stamp;
		this.teacherId = teacherId;
		this.disabled = disabled;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	public RegisterTime getRegisterTime(){
		return time;
	}
	public int getHour(){
		return time.getHour();
	}
	public RegisterTime.TimeType getTimeType() {
		return time.getTime();
	}
	public RegisterTime.DayOfWeek getDayOfWeek() {
		return time.getDayOfWeek();
	}
	public String getStamp(){
		String result = String.format("%04d", stamp);
		return result;
	}
	public int getStampInteger(){
		return this.stamp;
	}
	public String getTeacherId(){
		return this.teacherId;
	}

	@Override
	public String toString() {
		return String.format("%s %d", this.teacherId, this.stamp);
	}
	
	public static boolean isNextTime(int l, int r){
		int type = l%10;
		if(type == 0)
			return l+1 == r;
		else
			return l+9 == r;
	}
}
