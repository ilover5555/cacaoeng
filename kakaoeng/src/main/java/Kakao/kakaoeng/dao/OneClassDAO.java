package Kakao.kakaoeng.dao;

import java.util.List;

import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

public interface OneClassDAO {

	void registerTransaction(OneClass oneClass);
	List<OneClass> getOneClassListGroupedByPurchaseNumber(int PurchaseNumber);
	void setParentForNewOneClassTransaction(int oneClassId, int parent);
	List<OneClass> getOneClassListGroupedByLectureIdAfterExcludingDayOfWeek(int lectureId, DayOfWeek dayOfWeek);
	OneClass getOneClassByIdTransaction(int oneClassId);
	List<OneClass> getOneClassListGroupedByLectureIdTransaction(int lectureId);
	List<OneClass> getRootOneClassListGroupedByPurchaseNumber(int PurchaseNumber);
	List<OneClass> getOneClassListGroupedByParentTransaction(int parent);
	List<OneClass> getOneClassListByDayOfWeek(int lectureId, DayOfWeek dayOfWeek);
	void deleteByIdTransaction(int id);
}