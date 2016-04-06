package Kakao.kakaoeng.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternValidator<T> extends AbstractValidator<T> {

	private String regex;
	Pattern pattern;
	String formatName;
	
	public PatternValidator(String regex, String formatName) {
		this.regex = regex;
		pattern = Pattern.compile(this.regex);
		this.formatName = formatName;
	}
	
	@Override
	public List<String> check(T value, Map<String, Object> param, String fieldName) {
		List<String> result = new ArrayList<>();
		Matcher matcher = pattern.matcher(value.toString());
		if(!matcher.matches())
			if(bEnglish)
				result.add(fieldName + " is invalid " + formatName + " : " + value.toString());
			else
				result.add(fieldName + "은(는) 유효하지않은 " + formatName + " 형식입니다 : " + value.toString());
		return result;
	}

}
