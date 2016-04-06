package Kakao.kakaoeng;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Kakao.kakaoeng.field.Field;

public class FieldSet {
	public Map<String, Field<?>> fieldMap = new LinkedHashMap<String, Field<?>>();
	public boolean bEnglish = true;
	
	public FieldSet(boolean bEnglish) {
		this.bEnglish = bEnglish;
	}
	
	public FieldSet addField(Field<?> field){
		field.setbEnglish(bEnglish);
		fieldMap.put(field.getFieldName(), field);
		return this;
	}
	public List<String> validateAllField(){
		List<String> result = new ArrayList<>(); 
		
		for(String key : fieldMap.keySet()){
			result.addAll(fieldMap.get(key).checkValidation());
		}
		
		return result;
	}
	
	public Field<?> getField(String key){
		Field<?> result = fieldMap.get(key);
		return result;
	}
}
