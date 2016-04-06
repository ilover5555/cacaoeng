package Kakao.kakaoeng.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MinLengthValidator<T> extends AbstractValidator<T> {
	
	int minLength;
	
	public MinLengthValidator( int minLength) {
		this.minLength = minLength;
	}
	
	@Override
	public List<String> check(T value, Map<String, Object> param, String fieldName) {
		List<String> result = new ArrayList<>();
		if(value.toString().length() < minLength)
			if(bEnglish)
				result.add(fieldName+" is should be longer than " + minLength);
			else
				result.add(fieldName+" 은(는) " + minLength + " 보다 짧을 수 없습니다.");
		return result;
	}

}
