package Kakao.kakaoeng.field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import Kakao.kakaoeng.HttpServletDataGetterFromRegister;
import Kakao.kakaoeng.validator.AbstractValidator;

public class PartField<T> extends Field<T> {
	List<String> result = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public PartField(String fieldName, HttpServletDataGetterFromRegister dataGetter, String paramName) {
		super(fieldName, null, null);
		try {
			this.setValue((T) dataGetter.getPart(paramName));
		} catch (IOException | ServletException e) {
			result.add("Load File Error : " + fieldName);
		}
		addValidator(new AbstractValidator<T>() {

			@Override
			public List<String> check(T value, Map<String, Object> param, String fieldName) {
				return result;
			}
		});
	}

}
