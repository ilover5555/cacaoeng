package Kakao.kakaoeng;

import java.util.List;

import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Tuition;

public class SmartCalculateTuition extends CalculateTuition {
	
	public SmartCalculateTuition(List<ClassSearchUnit> bookList, EnvironDAO environDAO) {
		super(bookList, environDAO);
	}

	@Override
	public Tuition calculate() {
		int totalDuration=0;
		for(ClassSearchUnit csu : bookList){
			totalDuration += csu.getDuration().getDuration();
		}
		
		totalDuration = totalDuration * month;
		
		if(totalDuration <= 1)
			throw new IllegalArgumentException("적어도 50분(2타임) 이상은 신청해야합니다.");
		
		if(totalDuration <= 40)
		{
			int finalPrice = environDAO.getSmartFinalPrice(totalDuration)*month;
			Tuition tuition = new Tuition(typeDiscountName, typeDiscountPercent, finalPrice, monthDiscountPercent, this.getDayDiscount());
			return tuition;
		}else{
			return calculatedLogicalPrice(totalDuration);
		}
	}

}
