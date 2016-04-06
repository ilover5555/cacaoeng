package Kakao.kakaoeng;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.validator.PatternValidator;

public class SMS {

	static Logger logger = Logger.getLogger(SMS.class);
	
	public static class SmsResult{
		private int retCode;
		private String retMessage;
		private int retLastPoint;
		public SmsResult(int retCode, String retMessage, int retLastPoint) {
			super();
			this.retCode = retCode;
			this.retMessage = retMessage;
			this.retLastPoint = retLastPoint;
		}
		public int getRetCode() {
			return retCode;
		}
		public String getRetMessage() {
			return retMessage;
		}
		public int getRetLastPoint() {
			return retLastPoint;
		}
		@Override
		public String toString(){
			return String.format("retCode:%d, retMessage:%s, retLastPoint:%d", retCode, retMessage, retLastPoint);
		}
	}
	
	public static SmsResult sms(String sms_to,String sms_msg){
		PatternValidator<String> validator = new PatternValidator<>("[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}", "cellPhone");
		Field<String> toField = new Field<String>("cellPhone", sms_to);
		toField.addValidator(validator);
		List<String> errorMsg = toField.checkValidation();
		
		if(errorMsg.size() !=0){
			logger.info("send SMS canceled - " + errorMsg);
			return new SmsResult(-1, "Invalid cellPhone", -1);
		}
		
		whoisSMS whois_sms = new whoisSMS();
		String sms_id = "kakaosms";
		String sms_passwd = "zkzkdh11";
		//String sms_type = "L"; // 설정 하지 않는다면 80byte 넘는 메시지는 쪼개져서 sms로 발송, L 로
								// 설정하면 80byte 넘으면 자동으로 lms 변환

		// 로그인
		whois_sms.login(sms_id, sms_passwd);

		/*
		 * 문자발송에 필요한 발송정보
		 */
		// 보내는 사람번호
		String sms_from = "010-6320-0767";
		// 발송예약시간 (현재시간보다 작거나 같으면 즉시 발송이며 현재시간보다 10분이상 큰경우는 예약발송입니다.)
		String sms_date = null;
		// 보내는 메세지
		// 발송시간을 파라메터로 받지 못한경우 현재시간을 입력해줍니다.
		if (sms_date == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
			Date cNow = new Date();
			sms_date = sdf.format(cNow);
		}
		
		logger.info(String.format("sms_to:%s,sms_from:%s,sms_msg:%s,sms_date:%s", sms_to, sms_from, sms_msg, sms_date.toString()));
		// 보내는 메시지 내용 중 한글 깨짐이 발생될 경우 아래 주석을 제거하고 사용해주세요.
		try {
			sms_msg = new String(sms_msg.getBytes(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// UTF-8 설정
		whois_sms.setUtf8();

		// 파라메터 설정
		//whois_sms.setParams(sms_to, sms_from, sms_msg, sms_date, sms_type);
		whois_sms.setParams(sms_to, sms_from, sms_msg, sms_date);
		// 문자발송
		whois_sms.emmaSend();

		// 결과값 가져오기
		int retCode = whois_sms.getRetCode();

		// 발송결과 메세지
		String retMessage = whois_sms.getRetMessage();

		// 성공적으로 발송한경우 남은 문자갯수( 종량제 사용의 경우, 남은 발송가능한 문자수를 확인합니다.)
		int retLastPoint = whois_sms.getLastPoint();
		
		
		SmsResult result = new SmsResult(retCode, retMessage, retLastPoint);
		logger.info(result.toString());
		return result;
	}
}
