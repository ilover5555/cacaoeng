package Kakao.kakaoeng;

import java.util.List;

import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Tuition;

public class LevelTestCalculateTuition extends CalculateTuition {
	
	public LevelTestCalculateTuition(List<ClassSearchUnit> bookList, EnvironDAO environDAO) {
		super(bookList, environDAO);
	}

	@Override
	public Tuition calculate() {
		double monthDiscountPercent = 0;
		String typeDiscountName = "";
		double typeDiscountPercent = 0;
		
		int totalDuration=0;
		for(ClassSearchUnit csu : bookList){
			totalDuration += csu.getDuration().getDuration();
		}
		
		int finalPrice = 0;
		
		Tuition tuition = new Tuition(typeDiscountName, typeDiscountPercent, finalPrice, monthDiscountPercent, this.getDayDiscount());
		
		return tuition;
	}

}
