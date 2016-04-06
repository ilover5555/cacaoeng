package Kakao.kakaoeng.dao;

import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.domain.model.Book;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Lecture.Status;

public interface LectureDAO {

	void registerTransaction(Lecture lecture);
	List<Lecture> getLectureListByTeacherId(String teacherId, Date firstDate, Date lastDate);
	Lecture getOnGoingLectureByPurchase(int purchase);
	List<Lecture> getLectureListByPurchase(int purchase);
	void updateNote(int lectureId, String newNote);
	List<Lecture> getLectureListByStudentId(String studentId);
	void updateLectureState(int lectureId, Status lectureStatus);
	List<Lecture> getLectureListByTeacherClassName(String className);
	void finishLectureTransaction(int lectureId, Status lectureStatus);
	Lecture getLectureByIdTransaction(int lectureId);
	List<Lecture> getAllOnGoingLectureListTransaction();
	List<Lecture> getLectureListByTeacherId(String teacherId);
	void finishLectureTransaction(int lectureId, Status lectureStatus, Date date);
	void updateBook(Book book, int lectureId);
	List<Lecture> getLectureListByRange(Date firstDate, Date lastDate);
	List<Lecture> getAllLevelTestLectureTransaction();
	void updateAlign(int lectureId, boolean align);
	void deleteByIdTransaction(int id);
	Lecture getLevelTestLogByStudentId(String studentId);
	void updateBeforeNotified(int lectureId, boolean beforeNotified);
	void updateAfterNotified(int lectureId, boolean afterNotified);
	List<Lecture> getDoneWithAfterNotifiedLectureListTransaction(boolean afterNotified);
	List<Lecture> getOnGoingWithBeforeNotifiedLectureListTransaction(boolean beforeNotified);
	List<Lecture> getLectureListByStudentId(String studentId, Date firstDate, Date lastDate);
}