package Kakao.kakaoeng.field;

import org.apache.commons.codec.digest.DigestUtils;

import Kakao.kakaoeng.DataGetter;
import Kakao.kakaoeng.validator.OmitValidator;
import Kakao.kakaoeng.validator.PasswordConfirmValidator;

public class PasswordField extends Field<String> {

	private void init( ){
		addValidator(new OmitValidator<String>());
		addValidator(new PasswordConfirmValidator<>());
	}
	
	public PasswordField(String fieldName, String value) {
		super(fieldName, value);
		init();
	}

	public PasswordField(String fieldName, DataGetter dataGetter, Class<String> klass) {
		super(fieldName, dataGetter, klass);
		init();
	}
	
	public String getOriginalPW(){
		return super.getValue();
	}
	
	@Override
	public String getValue() {
		return DigestUtils.sha256Hex(super.getValue());
	}

	public void setPasswordConfirm(String pwConfirm){
		this.addParam("passwordConfirm", pwConfirm);
	}
}
