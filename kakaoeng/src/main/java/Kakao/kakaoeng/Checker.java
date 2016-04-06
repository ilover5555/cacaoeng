package Kakao.kakaoeng;

import java.util.ArrayList;
import java.util.List;

import Kakao.kakaoeng.domain.model.OneClass;

public class Checker {
	List<OneClass> list = new ArrayList<>();
	List<Integer> codeList = new ArrayList<>();
	
	public void add(OneClass oneClass){
		list.add(oneClass);
		codeList.add(oneClass.getCustomCalendarCode());
	}
	
	public boolean contains(int i){
		return codeList.contains((i-1)%7);
	}
	
	public List<OneClass> getOneClassListByCode(int code){
		List<OneClass> result = new ArrayList<>();
		code = (code-1)%7;
		
		for(OneClass oneClass : list){
			if(oneClass.getCustomCalendarCode() == code)
				result.add(oneClass);
		}
		
		return result;
	}
}
