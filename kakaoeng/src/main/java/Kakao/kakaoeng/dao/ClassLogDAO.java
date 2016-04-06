package Kakao.kakaoeng.dao;

import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;

public interface ClassLogDAO {

	void registerTransaction(ClassLog classLog);

	List<ClassLog> getAllClassLogListByOneClassTransaction(int oneClassId);

	void updateTransaction(int id, ClassState state, String reason);

	List<ClassLog> getAllClassLogListByPurchase(int purchaseNumber);

	List<ClassLog> getAllClassLogListByParent(int parent);

	List<ClassLog> getClassLogListByParent(int parent, Date firstDate, Date lastDate);

	List<ClassLog> getAllClassLogListByLectureId(int lectureId);

	List<ClassLog> getAllClassLogListByParentUntilLecture(int parent, int lectureId);

	List<ClassLog> getClassLogListByOneClassTransaction(int oneClassId, Date firstDate, Date lastDate);

	List<ClassLog> getClassLogListByRangeTransaction(Date firstDate, Date lastDate);

	void deleteByClassLogIdTransaction(int classLogId);

	ClassLog getClassLogByOneClassTransaction(int oneClassId, Date date);

	ClassLog getByIdTransaction(int classLogId);

}