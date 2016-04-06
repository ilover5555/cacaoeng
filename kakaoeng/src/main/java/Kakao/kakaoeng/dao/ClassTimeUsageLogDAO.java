package Kakao.kakaoeng.dao;

import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.ClassSearchUnit;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.ClassTimeUsageLog;
import Kakao.kakaoeng.domain.model.Duration;

public interface ClassTimeUsageLogDAO {
	void bookCheck(List<ClassTime> booked, String teacherId, Date startDate, Date endDate, ClassTimeUsageLogDAO classTimeUsageLogDAO);

	void book(List<ClassTime> booked, String teacherId, Date startDate, Date endDate, ClassTimeUsageLogDAO classTimeUsageLogDAO);

	void bookOneClassTime(ClassTime element, String teacherId, Date startDate, Date endDate);
	
	public void bookInterface(String teacherId, List<ClassSearchUnit> a, ClassTimeDAO classTimeDAO, ClassTimeUsageLogDAO classTimeUsageLogDAO);
	
	public void bookOneClassInterface(String teacherId, Duration duration, int weeks, Date startDate,
			ClassTimeDAO classTimeDAO, ClassTimeUsageLogDAO classTimeUsageLogDAO);

	void deleteByClassTimeIdTransaction(int classTimeId);

	boolean checkClassTimeCanBeUsedStateTransaction(int classTimeId, Date startDate, Date endDate);

	ClassTimeUsageLog selectRangeTransaction(int classTimeId, Date startDate, Date endDate);

	void procrastinateByIdTransaction(int classTimeUsageLogId, int weeks);

	void deleteByClassTimeUsageLogIdTransaction(int CTULId);

	ClassTimeUsageLog selectClassTimeUsageLog(int classTimeId, String teacherId);

}