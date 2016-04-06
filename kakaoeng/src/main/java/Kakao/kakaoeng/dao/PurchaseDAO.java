package Kakao.kakaoeng.dao;

import java.util.List;

import Kakao.kakaoeng.domain.model.Purchase;

public interface PurchaseDAO {

	void registerTransaction(Purchase purchase);

	void updateApprovedNumber(int id);

	List<Purchase> getPurchaseListWhichIsUnconfirm();

	void updateConfirm(int id, boolean confirm);

	void updateRejected(int id, boolean rejected);

	List<Purchase> getPurchaseListNotRejected();

	List<Purchase> getPurchaseListWithTeacherClassName(String teacherClassName);

	List<Purchase> getPurchaseListWithStudentClassName(String studentClassName);

	List<Purchase> getPurchaseListWithBook(String book);

	Purchase getPurchaseById(int id);

	void updateProcrastinateTransaction(int id, int procrastinate);

	List<Purchase> getPurchaseListWithStudentId(String studentId);

	void deleteByIdTransaction(int id);

}