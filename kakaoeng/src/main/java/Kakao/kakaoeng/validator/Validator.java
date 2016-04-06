package Kakao.kakaoeng.validator;

import java.util.List;
import java.util.Map;

public interface Validator<T> {
	public List<String> check(T value, Map<String, Object> param, String fieldName);
	
}
