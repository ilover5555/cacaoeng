package Kakao.kakaoeng.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Kakao.kakaoeng.DataGetter;

public class ExceptionField<T> extends Field<T> {

	protected List<String> msg = new ArrayList<>();
	
	public ExceptionField(String fieldName, String ErrorMessage) {
		super(fieldName, null);
		this.msg.add(ErrorMessage);
	}

	@Override
	public List<String> checkValidation() {
		return Collections.unmodifiableList(msg);
	}

}
