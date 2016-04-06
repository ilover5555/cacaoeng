package Kakao.kakaoeng.dao;

import java.util.List;

import Kakao.kakaoeng.domain.model.Admin;

public interface AdminDAO {

	Admin findAdminById(String id);

	Admin findAdminByIdAndPw(String id, String pw);

	void register(Admin admin);

	List<Admin> getAllManagerList();

	void delete(String id);

	void update(Admin admin);

}
