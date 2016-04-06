package Kakao.kakaoeng.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MaxLengthValidator<T> extends AbstractValidator<T> {

	int maxLength;

	public MaxLengthValidator( int maxLength){
		this.maxLength = maxLength;
	}
	
	@Override
	public List<String> check(T value, Map<String, Object> param, String fieldName) {
		List<String> result = new ArrayList<>();
		
		if(value.toString().length() > maxLength)
			if(bEnglish)
				result.add(fieldName + " is should be shorter than " + maxLength);
			else
				result.add(fieldName + "은(는) " + maxLength + " 보다 길 수 없습니다.");
		
		return result;
	}

}
