package Kakao.kakaoeng.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DateValidator<T> extends AbstractValidator<T> {	
	
	

	@Override
	public List<String> check(T value, Map<String, Object> param, String fieldName) {
		List<String> result = new ArrayList<>();
		try{
			DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
			df.setLenient(false);
			df.parse(value.toString());
		} catch (ParseException e){
			if(bEnglish)
				result.add(fieldName + " is not valid date : " + value.toString());
			else
				result.add(fieldName + "은(는) 유요한 날짜형식이 아닙니다 : " + value.toString());
		}
		return result;
	}
	
	
}
