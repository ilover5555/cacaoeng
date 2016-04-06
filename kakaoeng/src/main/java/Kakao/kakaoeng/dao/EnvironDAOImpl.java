package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import Kakao.kakaoeng.Util;


public class EnvironDAOImpl implements EnvironDAO {

	DataSource dataSource;
	Logger logger = Logger.getLogger(EnvironDAOImpl.class);
	
	public static String query = "select `value` from `environment` where `key`=?";
	public static String save = "update `environment` set `value`=? where `key`=?";
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public static abstract class getTemplateFunc<T>{
		private String key;
		
		public getTemplateFunc(String key) {
			super();
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		abstract T processResult(ResultSet rs) throws NumberFormatException, SQLException;
	}
	
	public static class setTemplateFunc{
		private String key;
		private String value;
		public setTemplateFunc(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}
		void setKeyValue(String key, String value){
			this.key = key;
			this.value = value;
		}
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
		
	}
	
	private <T> T getTemplate(getTemplateFunc<T> f){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		T result = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(query);
			ps.setString(1, f.getKey());
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			rs.next();
			result = f.processResult(rs);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
		
	}
	
	private Object saveTemplate(setTemplateFunc f){
		Connection connection = null;
		PreparedStatement ps = null;
		Object result = 0;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(save);
			ps.setString(1, f.getValue());
			ps.setString(2, f.getKey());
			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
		
	}
	
	@Override
	public void saveFixPrice(int times, int minute, int price){
		saveTemplate(new setTemplateFunc(String.format("FIX_PRICE_%d_%d", times, minute), String.format("%d", price)));
	}
	
	@Override
	public int getFixPrice(int times, int minute){
		return getTemplate(new getTemplateFunc<Integer>(String.format("FIX_PRICE_%d_%d", times, minute)) {
			@Override
			Integer processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Integer.parseInt(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveSmartFinalPrice(int duration, int price){
		if(!((2<=duration) && (duration<=40))){
			throw new IllegalArgumentException("Invlaid Duration For getSmartFinalPrice : " + duration);
		}
		saveTemplate(new setTemplateFunc("SMART_FINAL_"+duration, String.format("%d", price)));
	}
	
	@Override
	public int getSmartFinalPrice(int duration){
		if(!((2<=duration) && (duration<=40))){
			throw new IllegalArgumentException("Invlaid Duration For getSmartFinalPrice : " + duration);
		}
		return getTemplate(new getTemplateFunc<Integer>("SMART_FINAL_"+duration) {
			@Override
			Integer processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Integer.parseInt(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveSmartAdjust(double adjust){
		saveTemplate(new setTemplateFunc("SMART_ADUST", String.format("%.3f", adjust)));
	}
	
	@Override
	public double getSmartAdjust(){
		return getTemplate(new getTemplateFunc<Double>("SMART_ADUST") {
			@Override
			Double processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Double.parseDouble(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveSmartDiscountPercent(double percent){
		saveTemplate(new setTemplateFunc("SMART_DISCOUNT_PERCENT", Util.getPercentStringFromDouble(percent)));
	}
	
	@Override
	public double getSmartDiscountPercent(){
		return getTemplate(new getTemplateFunc<Double>("SMART_DISCOUNT_PERCENT") {
			@Override
			Double processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Util.getDoubleFromPercentString(rs.getString(1));
			}
		});
	}
	
	@Override
	public String getSmartDiscountPercentString(){
		return getTemplate(new getTemplateFunc<String>("SMART_DISCOUNT_PERCENT") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveSmartBasePrice(int price){
		saveTemplate(new setTemplateFunc("SMART_BASE_PRICE", String.format("%d", price)));
	}
	
	@Override
	public int getSmartBasePrice(){
		return getTemplate(new getTemplateFunc<Integer>("SMART_BASE_PRICE") {
			@Override
			Integer processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Integer.parseInt(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveFixDiscountPercent(double percent){
		saveTemplate(new setTemplateFunc("FIX_DISCOUNT_PERCENT", Util.getPercentStringFromDouble(percent)));
	}
	
	@Override
	public String getFixDiscountPercentString(){
		return getTemplate(new getTemplateFunc<String>("FIX_DISCOUNT_PERCENT") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public double getFixDiscountPercent(){
		return getTemplate(new getTemplateFunc<Double>("FIX_DISCOUNT_PERCENT") {
			@Override
			Double processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Util.getDoubleFromPercentString(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveSmartDiscountName(String smartDiscountName){
		saveTemplate(new setTemplateFunc("SMART_DISCOUNT_NAME", smartDiscountName));
	}
	
	@Override
	public String getSmartDiscoutName(){
		return getTemplate(new getTemplateFunc<String>("SMART_DISCOUNT_NAME") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveFixDiscountName(String fixDiscountName){
		saveTemplate(new setTemplateFunc("FIX_DISCOUNT_NAME", fixDiscountName));
	}
	
	@Override
	public String getFixDiscoutName(){
		return getTemplate(new getTemplateFunc<String>("FIX_DISCOUNT_NAME") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveDayDiscountPercent(double percent){
		saveTemplate(new setTemplateFunc("DAY_DISCOUNT_PERCENT", Util.getPercentStringFromDouble(percent)));
	}
	
	@Override
	public String getDayDiscountPercentString(){
		return getTemplate(new getTemplateFunc<String>("DAY_DISCOUNT_PERCENT") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public double getDayDiscountPercent(){
		return getTemplate(new getTemplateFunc<Double>("DAY_DISCOUNT_PERCENT") {
			@Override
			Double processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Util.getDoubleFromPercentString(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveMonthDiscountPercent(int month, double percent){
		if(!(month == 1 || month == 3 || month == 6 || month == 12)){
			RuntimeException r = new IllegalArgumentException("getMonthDiscountPercent invalid month : " + month);
			throw r;
		}
		saveTemplate(new setTemplateFunc(month+"MONTH_DISCOUNT_PERCENT", Util.getPercentStringFromDouble(percent)));
	}
	
	@Override
	public String getMonthDiscountPercentString(int month){
		if(!(month == 1 || month == 3 || month == 6 || month == 12)){
			RuntimeException r = new IllegalArgumentException("getMonthDiscountPercent invalid month : " + month);
			throw r;
		}
		return getTemplate(new getTemplateFunc<String>(month+"MONTH_DISCOUNT_PERCENT") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public double getMonthDiscountPercent(int month){
		if(!(month == 1 || month == 3 || month == 6 || month == 12)){
			RuntimeException r = new IllegalArgumentException("getMonthDiscountPercent invalid month : " + month);
			throw r;
		}
		return getTemplate(new getTemplateFunc<Double>(month+"MONTH_DISCOUNT_PERCENT") {
			@Override
			Double processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Util.getDoubleFromPercentString(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveBeforeRequestLectureDiffer(int differ){
		saveTemplate(new setTemplateFunc("REQUEST_LECTURE_DIFFER", String.format("%d", differ)));
	}
	
	@Override
	public int getBeforeRequestLectureDiffer(){
		return getTemplate(new getTemplateFunc<Integer>("REQUEST_LECTURE_DIFFER") {
			@Override
			Integer processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Integer.parseInt(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveAfterRequestLectureDiffer(int differ){
		saveTemplate(new setTemplateFunc("AFTER_REQUEST_LECTURE_DIFFER", String.format("%d", differ)));
	}
	
	@Override
	public int getAfterRequestLectureDiffer(){
		return getTemplate(new getTemplateFunc<Integer>("AFTER_REQUEST_LECTURE_DIFFER") {
			@Override
			Integer processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Integer.parseInt(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveQueryPwSMSMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_SMS_QUERY_PW", message));
	}
	
	@Override
	public String getQueryPwSMSMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_SMS_QUERY_PW") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveAfterRequestLectureSMSMode(String mode){
		saveTemplate(new setTemplateFunc("AFTER_REQUEST_LECTURE_MODE", mode));
	}
	
	
	
	@Override
	public String getAfterRequestLectureSMSMode(){
		return getTemplate(new getTemplateFunc<String>("AFTER_REQUEST_LECTURE_MODE") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveAfterRequestLectureSMSMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_SMS_AFTER_REQUEST_LECTURE", message));
	}
	
	@Override
	public String getAfterRequestLectureSMSMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_SMS_AFTER_REQUEST_LECTURE") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveRequestLectureSMSMode(String mode){
		saveTemplate(new setTemplateFunc("REQUEST_LECTURE_MODE", mode));
	}
	
	
	
	@Override
	public String getRequestLectureSMSMode(){
		return getTemplate(new getTemplateFunc<String>("REQUEST_LECTURE_MODE") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveRequestLectureSMSMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_SMS_REQUEST_LECTURE", message));
	}
	
	@Override
	public String getRequestLectureSMSMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_SMS_REQUEST_LECTURE") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveNewLectureSMSMode(String mode){
		saveTemplate(new setTemplateFunc("LECTURE_REGISTER_MODE", mode));
	}
	
	
	
	@Override
	public String getNewLectureSMSMode(){
		return getTemplate(new getTemplateFunc<String>("LECTURE_REGISTER_MODE") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveNewLectureSMSMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_SMS_LECTURE_REGISTER", message));
	}
	
	@Override
	public String getNewLectureSMSMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_SMS_LECTURE_REGISTER") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveNewRegisterSMSMode(String mode){
		saveTemplate(new setTemplateFunc("NEW_REGISTER_MODE", mode));
	}
	
	
	
	@Override
	public String getNewRegisterSMSMode(){
		return getTemplate(new getTemplateFunc<String>("NEW_REGISTER_MODE") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveNewRegisterSMSMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_SMS_NEW_REGISTER", message));
	}
	
	
	
	@Override
	public String getNewRegisterSMSMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_SMS_NEW_REGISTER") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveAllSMSMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_SMS_ALL", message));
	}
	
	@Override
	public String getAllSMSMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_SMS_ALL") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveLevelTestMailMode(String mode){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_MODE_LEVEL_TEST", mode));
	}
	
	@Override
	public String getLevelTestMailMode(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_MODE_LEVEL_TEST") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveLevelTestMailSubject(String subject){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_SUBJECT_LEVEL_TEST", subject));
	}
	
	@Override
	public String getLevelTestMailSubject(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_SUBJECT_LEVEL_TEST") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveLevelTestMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_TEXT_LEVEL_TEST", message));
	}
	
	@Override
	public String getLevelTestMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_TEXT_LEVEL_TEST") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void savePayedMailMode(String mode){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_MODE_PAYED", mode));
	}
	
	@Override
	public String getPayedMailMode(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_MODE_PAYED") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void savePayedMailSubject(String subject){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_SUBJECT_PAYED", subject));
	}
	
	@Override
	public String getPayedMailSubject(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_SUBJECT_PAYED") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void savePayedMailMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_TEXT_PAYED", message));
	}
	
	@Override
	public String getPayedMailMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_TEXT_PAYED") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveLectureRegisteredMailMode(String mode){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_MODE_LECTURE_REGISTERED", mode));
	}
	
	@Override
	public String getLectureRegisteredMailMode(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_MODE_LECTURE_REGISTERED") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveLectureRegisteredMailSubject(String subject){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_SUBJECT_LECTURE_REGISTERED", subject));
	}
	
	@Override
	public String getLectureRegisteredMailSubject(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_SUBJECT_LECTURE_REGISTERED") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveLectureRegistredMailMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_TEXT_LECTURE_REGISTERED", message));
	}
	
	@Override
	public String getLectureRegisteredMailMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_TEXT_LECTURE_REGISTERED") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}

	@Override
	public void saveTeacherConfirmMailMode(String mode){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_MODE_TEACHER_CONFIRM", mode));
	}
	
	@Override
	public String getTeacherConfirmMailMode(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_MODE_TEACHER_CONFIRM") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveTeacherConfirmMailSubject(String subject){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_SUBJECT_TEACHER_CONFIRM", subject));
	}
	
	@Override
	public String getTeacherConfirmMailSubject(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_SUBJECT_TEACHER_CONFIRM") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public void saveTeacherConfirmMailMessage(String message){
		saveTemplate(new setTemplateFunc("EVENT_MAIL_TEXT_TEACHER_CONFIRM", message));
	}
	
	@Override
	public String getTeacherConfirmMailMessage(){
		return getTemplate(new getTemplateFunc<String>("EVENT_MAIL_TEXT_TEACHER_CONFIRM") {
			@Override
			String processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return rs.getString(1);
			}
		});
	}
	
	@Override
	public float getUSD(){
		return getTemplate(new getTemplateFunc<Float>("USD") {
			@Override
			Float processResult(ResultSet rs) throws NumberFormatException, SQLException {
				return Float.parseFloat(rs.getString(1));
			}
		});
	}
	
	@Override
	public void saveUSD(float USD){
		saveTemplate(new setTemplateFunc("USD", String.format("%f", USD)));
	}
}
