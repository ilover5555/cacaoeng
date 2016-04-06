package Kakao.kakaoeng.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Kakao.kakaoeng.DataGetter;
import Kakao.kakaoeng.validator.AbstractValidator;

public class Field<T> {

	private String fieldName;
	private T value;
	private List<AbstractValidator<T>> validators = new ArrayList<>();
	private Map<String, Object> param = new HashMap<String, Object>();
	private List<String> errorMsg = new ArrayList<>();
	private boolean bEnglish=true;
	
	public boolean getbEnglish() {
		return bEnglish;
	}

	public void setbEnglish(boolean bEnglish) {
		this.bEnglish = bEnglish;
	}

	public Field(String fieldName, T value)
	{
		this.fieldName = fieldName;
		this.value = value;
	}
	
	public Field(String fieldName, DataGetter dataGetter, Class<T> klass)
	{
		this.fieldName = fieldName;
		if(dataGetter == null)
			this.value = null;
		else
			this.value = dataGetter.get(fieldName,klass);
	}
	
	public void addError(String msg){
		errorMsg.add(msg);
	}
	
	public T getValue(){
		return value;
	}
	public void setValue(T value){
		this.value = value;
	}
	
	public String getFieldName(){
		return fieldName;
	}
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	}
	
	public List<String> checkValidation(){
		List<String> result = new ArrayList<String>();
		for(AbstractValidator<T> validator : validators){
			validator.setbEnglish(bEnglish);
			result.addAll(validator.check(value, param, fieldName));
		}
		result.addAll(errorMsg);
		return result;
	}
	public void addValidator(AbstractValidator<T> validator){
		validators.add(validator);
	}
	public void addParam(String key, Object param){
		this.param.put(key, param);
	}
	public Object getParam(String key){
		return this.param.get(key);
	}
}