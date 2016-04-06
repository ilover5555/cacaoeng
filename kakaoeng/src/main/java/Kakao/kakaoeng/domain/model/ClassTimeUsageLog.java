package Kakao.kakaoeng.domain.model;

import java.util.Date;

public class ClassTimeUsageLog {
	int id;
	int classTimeId;
	String teacherId;
	Date startDate;
	Date endDate;
	public ClassTimeUsageLog(int id, int classTimeId, String teacherId, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.classTimeId = classTimeId;
		this.teacherId = teacherId;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public ClassTimeUsageLog(int classTimeId, String teacherId, Date startDate, Date endDate) {
		this(-1, classTimeId, teacherId, startDate, endDate);
	}
	public int getId() {
		return id;
	}
	public int getClassTimeId() {
		return classTimeId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	
	
}
