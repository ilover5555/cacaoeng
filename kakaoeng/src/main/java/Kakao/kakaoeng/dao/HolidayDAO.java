package Kakao.kakaoeng.dao;

import java.util.Date;
import java.util.List;

public interface HolidayDAO {

	boolean checkHoliday(Date date);

	List<Date> findHolidayWithRange(Date start, Date end);

	void deleteTransaction(Date holiday);

	void registerHolidayTransaction(Date date);

}
