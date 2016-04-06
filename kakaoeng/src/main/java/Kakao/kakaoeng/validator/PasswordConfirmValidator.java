package Kakao.kakaoeng.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PasswordConfirmValidator<T> extends AbstractValidator<T> {
	
	@Override
	public List<String> check(T value, Map<String, Object> param, String fieldName) {
		List<String> result = new ArrayList<>();
		String passwordConfirm = (String) param.get("passwordConfirm");
		if(!value.toString().equals(passwordConfirm))
			if(bEnglish)
				result.add("password and password confirm are not matched");
			else
				result.add("비밀번호가 일치하지 않습니다.");
		return result;
	}

}
