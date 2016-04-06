package Kakao.kakaoeng.dao;

import java.util.List;
import java.util.Map;

import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.Teacher.Quality;
import Kakao.kakaoeng.domain.model.Teacher.Rate;

public interface TeacherDAO {

	boolean findTeacherById(String id);

	List<Teacher> getSpecificRateTeacher(String rate);

	void update(Teacher teacher);

	void register(Teacher teacher);

	List<Teacher> getConfirmedTeacherList();

	List<Teacher> getUnConfirmedTeacherList();

	Teacher getTeacherWithId(String id);

	Teacher getTeacherWithIdAndPw(String id, String pw);

	List<Teacher> getTeacherList(Map<String, String> where);

	List<Teacher> getTeacherList();

	List<Teacher> getRepresentitiveTeacherList();

	void updateConfirm(String teacherId, boolean confirm);

	void delete(String teacherId);

	List<Teacher> getActivedTeacherList();

	List<Teacher> searchActiveTeacherByClassName(String className);

	void updateRepresentitive(String teacherId, boolean representitive);

	void updateRetirement(String teacherId, boolean retirement);

	void updateRate(String teacherId, Rate rate);

	int findTeacherCountByRate(Rate rate);

	List<Teacher> searchRetireTeacherByClassName(String className);

	void updateForAdmin(String id, int salary, Quality pronunciation, Quality accent, String comment);

	String findClassNameById(String id);

	boolean findTeacherByClssName(String className);

}