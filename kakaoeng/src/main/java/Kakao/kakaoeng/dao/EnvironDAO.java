package Kakao.kakaoeng.dao;

public interface EnvironDAO {

	float getUSD();

	String getTeacherConfirmMailMessage();

	String getAllSMSMessage();

	String getNewRegisterSMSMessage();

	String getNewLectureSMSMessage();

	String getRequestLectureSMSMessage();

	String getQueryPwSMSMessage();

	void saveAllSMSMessage(String message);

	void saveNewRegisterSMSMessage(String message);

	void saveNewLectureSMSMessage(String message);

	void saveRequestLectureSMSMessage(String message);

	void saveQueryPwSMSMessage(String message);

	String getNewRegisterSMSMode();

	void saveRequestLectureSMSMode(String mode);

	String getRequestLectureSMSMode();

	void saveNewLectureSMSMode(String mode);

	String getNewLectureSMSMode();

	void saveNewRegisterSMSMode(String mode);

	String getAfterRequestLectureSMSMessage();

	void saveAfterRequestLectureSMSMessage(String message);

	String getAfterRequestLectureSMSMode();

	void saveAfterRequestLectureSMSMode(String mode);

	void saveBeforeRequestLectureDiffer(int differ);

	int getBeforeRequestLectureDiffer();

	void saveAfterRequestLectureDiffer(int differ);

	int getAfterRequestLectureDiffer();

	double getMonthDiscountPercent(int month);

	double getDayDiscountPercent();

	void saveMonthDiscountPercent(int month, double percent);

	void saveDayDiscountPercent(double percent);

	String getFixDiscoutName();

	void saveFixDiscountName(String fixDiscountName);

	double getFixDiscountPercent();

	void saveFixDiscountPercent(double percent);

	int getSmartBasePrice();


	double getSmartAdjust();

	void saveSmartAdjust(double adjust);

	int getSmartFinalPrice(int duration);

	void saveSmartFinalPrice(int duration, int price);

	double getSmartDiscountPercent();

	void saveSmartDiscountPercent(double percent);

	void saveSmartBasePrice(int price);

	void saveSmartDiscountName(String smartDiscountName);

	String getSmartDiscoutName();

	void saveUSD(float USD);

	String getMonthDiscountPercentString(int month);

	String getDayDiscountPercentString();

	int getFixPrice(int times, int minute);

	void saveFixPrice(int times, int minute, int price);

	String getSmartDiscountPercentString();

	String getFixDiscountPercentString();

	String getTeacherConfirmMailSubject();

	String getTeacherConfirmMailMode();

	void saveTeacherConfirmMailMessage(String message);

	void saveTeacherConfirmMailSubject(String subject);

	void saveTeacherConfirmMailMode(String mode);

	void saveLectureRegistredMailMessage(String message);

	String getLectureRegisteredMailMessage();

	String getLectureRegisteredMailSubject();

	void saveLectureRegisteredMailSubject(String subject);

	void saveLectureRegisteredMailMode(String mode);

	String getLectureRegisteredMailMode();

	String getPayedMailMessage();

	void savePayedMailMessage(String message);

	String getPayedMailSubject();

	void savePayedMailSubject(String subject);

	void savePayedMailMode(String mode);

	String getPayedMailMode();

	void saveLevelTestMessage(String message);

	String getLevelTestMessage();

	String getLevelTestMailSubject();

	void saveLevelTestMailSubject(String subject);

	String getLevelTestMailMode();

	void saveLevelTestMailMode(String mode);

}
