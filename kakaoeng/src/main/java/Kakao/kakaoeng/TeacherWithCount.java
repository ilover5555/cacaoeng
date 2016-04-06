package Kakao.kakaoeng;

import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.Teacher.Rate;

public class TeacherWithCount implements Comparable<TeacherWithCount>{
	String teacherId;
	Teacher teacher;
	int count;
	boolean noResult=false;
	
	public void setNoResult(){
		this.noResult = true;
	}
	
	public TeacherWithCount(String teacherId) {
		this(teacherId, 0);
	}
	
	public TeacherWithCount(String teacherId, int count) {
		super();
		this.teacherId = teacherId;
		this.count = count;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public int getCount() {
		return count;
	}
	public void addCount(){
		this.count++;
	}

	@Override
	public boolean equals(Object obj) {
		TeacherWithCount twc = (TeacherWithCount) obj;
		if(this.teacherId.equals(twc.teacherId))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return this.teacherId.hashCode();
	}
	public boolean isNoResult(){
		return this.noResult;
	}

	@Override
	public String toString() {
		return this.teacherId;
	}
	public void loadTeacher(TeacherDAO teacherDAO){
		teacher = teacherDAO.getTeacherWithId(teacherId);
	}
	public Teacher getTeacher(){
		return this.teacher;
	}

	@Override
	public int compareTo(TeacherWithCount o) {
		int countCompare = Integer.compare(this.getCount(), o.getCount())*-1;
		if(countCompare != 0)
			return countCompare;
		int rateCompare = Rate.compare(this.teacher.getRate(), o.teacher.getRate());
		if(rateCompare != 0)
			return rateCompare;
		return this.teacherId.compareTo(o.teacherId);
	}
	
}