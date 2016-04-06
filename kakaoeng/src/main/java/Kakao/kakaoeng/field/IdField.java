package Kakao.kakaoeng.field;

import Kakao.kakaoeng.DataGetter;
import Kakao.kakaoeng.validator.EmailValidator;
import Kakao.kakaoeng.validator.MaxLengthValidator;
import Kakao.kakaoeng.validator.OmitValidator;

public class IdField<T> extends Field<T> {
	
	public IdField(String fieldName, DataGetter dataGetter, Class<T> klass) {
		super(fieldName, dataGetter, klass);
		addValidator(new OmitValidator<T>());
		addValidator(new EmailValidator<T>());
		addValidator(new MaxLengthValidator<T>(100));
	}
}
