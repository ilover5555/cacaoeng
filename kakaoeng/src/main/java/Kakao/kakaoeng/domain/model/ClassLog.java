package Kakao.kakaoeng.domain.model;

import java.util.Date;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.OneClassDAO;

public class ClassLog implements Comparable<ClassLog> {

	public enum ClassState{
		Completed(1), PostponeStudent(0), PostponeTeacher(0), AbsentStudent(0.5), AbsentTeacher(0),
		Uncompleted(0), Uncompleted_0(0), Uncompleted_30(0.3), Uncompleted_50(0.5), Uncompleted_100(1), Holiday(0),
		LevelTestReserved(0), LevelTestCompleted(0), LevelTestUncompleted(0);
		
		private double rate;
		
		private ClassState(double rate) {
			this.rate = rate;
		}
		
		public double getRate(){
			return this.rate;
		}
	}
	
	int id;
	int oneClassId;
	String teacherId;
	String studentId;
	Date classDate;
	ClassState classState;
	String reason;
	OneClass oneClass;
	public ClassLog(int id, int oneClassId, String teacherId, String studentId, Date classDate, ClassState classState,
			String reason) {
		super();
		this.id = id;
		this.oneClassId = oneClassId;
		this.teacherId = teacherId;
		this.studentId = studentId;
		this.classDate = classDate;
		this.classState = classState;
		this.reason = reason;
	}
	public void loadOneClass(OneClassDAO oneClassDAO){
		oneClass = oneClassDAO.getOneClassByIdTransaction(oneClassId);
	}
	public void loadOneClass(OneClass oneClass){
		this.oneClass = oneClass;
	}
	public OneClass getOneClass(){
		if(oneClass == null)
			throw new IllegalStateException("oneClass is not yet initialized");
		return oneClass;
	}
	public int getId() {
		return id;
	}
	public int getOneClassId() {
		return oneClassId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public String getStudentId() {
		return studentId;
	}
	public Date getClassDate() {
		return classDate;
	}
	public ClassState getClassState() {
		return classState;
	}
	public String getReason() {
		return reason;
	}
	public String getClassDateForm(){
		return Util.dateFormatting(classDate);
	}
	@Override
	public int compareTo(ClassLog o) {
		if(this.classDate.equals(o.classDate))
			return 0;
		else if(this.classDate.before(o.classDate))
			return -1;
		else
			return 1;
	}
	@Override
	public boolean equals(Object obj) {
		ClassLog o = (ClassLog)obj;
		if(this.getOneClassId() == o.getOneClassId() &&
				this.getTeacherId().equals(o.getTeacherId()) &&
				this.getStudentId().equals(o.getStudentId()) &&
				this.getClassDate().equals(o.getClassDate()) &&
				this.getClassState().equals(o.getClassState()))
			return true;
		else
			return false;
	}
	
}
