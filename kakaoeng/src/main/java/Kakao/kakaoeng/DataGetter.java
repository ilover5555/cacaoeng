package Kakao.kakaoeng;


import java.util.List;

import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.ClassTimeUsageLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.Teacher.Rate;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.field.PasswordField;

public interface DataGetter  {
	public <T> T get(String key, Class<T> klass);
	public Teacher makeTeacherInstance();
	public ClassTime makeClassTimeInstance();
	public ClassTimeUsageLog makeClassTimeUsageLogInstance();
	public Lecture makeLectureInstance();
	public List<Field<?>> getFields();
	public String getPassword();
	public String getPasswordConfirm();
	public String getBirth();
	public Field<String> getIdField();
	public PasswordField getPasswordField();
	public Rate getRate();
	public boolean getToefl();
	public boolean getIlets();
	@SuppressWarnings("serial")
	class InvalidKeyException extends RuntimeException{

		public InvalidKeyException() {
			super();
		}
		
		public InvalidKeyException(String msg){
			super(msg);
		}
		
		public InvalidKeyException(Throwable t){
			super(t);
		}
	}
}
