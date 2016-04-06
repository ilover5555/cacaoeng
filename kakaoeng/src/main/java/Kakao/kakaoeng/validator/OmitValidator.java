package Kakao.kakaoeng.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OmitValidator<T> extends AbstractValidator<T> {
	@Override
	public List<String> check(T value, Map<String, Object> param, String fieldName) {
		List<String> result = new ArrayList<>();
		if(value.toString().equals(""))
			if(bEnglish)
				result.add(fieldName + " is omitted");
			else
				result.add(fieldName + "은(는) 생략될 수 없습니다.");
		return result;
	}

}
