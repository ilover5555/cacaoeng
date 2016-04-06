package Kakao.kakaoeng.dao;

import java.util.List;

import Kakao.kakaoeng.domain.model.Pay;

public interface PayDAO {

	void register(Pay pay);

	List<Pay> getPayListByTeacherId(String teacherId);

	List<Pay> getPayListByMonth(int month);

	Pay getPayListByTeacherIdAndMonth(String teacherId, int month);

	void updatePayed(int payId, boolean payed);

}
