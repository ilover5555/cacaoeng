package Kakao.kakaoeng.dao;

import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.domain.model.User.UserType;

public interface UserDAO {

	void updateLastLogin(String id, String pw, UserType userType);

	User findUserById(String id);

}
