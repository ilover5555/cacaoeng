package Kakao.kakaoeng.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExclusionValidator<T> extends AbstractValidator<T> {

	T exclusion;
	
	public ExclusionValidator(T exclusion) {
		this.exclusion = exclusion;
	}
	
	@Override
	public List<String> check(T value, Map<String, Object> param, String fieldName) {
		List<String> result = new ArrayList<>();
		
		if(value.equals(exclusion))
			result.add(fieldName + " can't be " + exclusion);
		
		return result;
	}

}
