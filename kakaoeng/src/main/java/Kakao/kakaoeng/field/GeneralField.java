package Kakao.kakaoeng.field;

import Kakao.kakaoeng.DataGetter;
import Kakao.kakaoeng.validator.OmitValidator;

public class GeneralField<T> extends Field<T> {

	private void init(){
		addValidator(new OmitValidator<T>());
	}
	
	public GeneralField(String fieldName, T value) {
		super(fieldName, value);
		init();
	}

	public GeneralField(String fieldName, DataGetter dataGetter, Class<T> klass) {
		super(fieldName, dataGetter, klass);
		init();
	}

}
