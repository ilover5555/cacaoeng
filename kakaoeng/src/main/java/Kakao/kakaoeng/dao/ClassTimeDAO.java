package Kakao.kakaoeng.dao;

import java.util.List;

import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Teacher.Rate;

public interface ClassTimeDAO {

	List<ClassTime> getClassTimeWithinStampInSpecificRate(int startStamp, int endStamp, int range, Rate rate);

	ClassTime getClassTimeListByTeacherIdAndStamp(String teacherId, int stamp);

	List<ClassTime> getClassTimeListByTeacherId(String teacherId);

	void register(ClassTime classTime);

	int findIdFromClassTimeInstance(ClassTime classTime);

	void disable(ClassTime classTime);

	void enable(ClassTime classTime);

	void delete(int classTimeId);

}