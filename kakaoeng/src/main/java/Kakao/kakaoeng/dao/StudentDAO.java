package Kakao.kakaoeng.dao;

import java.sql.Date;
import java.util.List;

import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Student.Level;

public interface StudentDAO {

	Student findStudentById(String id);

	void updateNote(String studentId, String newNote);

	void register(Student student);

	Student findStudentByIdAndPw(String id, String pw);

	List<Student> getAllStudentList();

	List<Student> findStudentByName(String name);

	void update(Student student);

	void updateLevel(String studentId, Level level);

	void updateCoupon(String studentId, int coupon);

	void delete(String studentId);

	String findClassNameById(String id);

	String findNameById(String id);

	void setNote(String studentId, String newNote);

	List<Student> findStudentListAfterLastLogin(java.util.Date baseDate);

	void updatePw(String studentId, String pw);

	List<Student> findStudentByNameAndCellPhone(String name, String cellPhone);

}
