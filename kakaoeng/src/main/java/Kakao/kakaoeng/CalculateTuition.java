package Kakao.kakaoeng;

import java.util.List;

import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Tuition;

public abstract class CalculateTuition {

	protected List<ClassSearchUnit> bookList;
	protected int month;
	protected EnvironDAO environDAO;
	
	private int start = 11;
	private int end = 18;
	
	protected String typeDiscountName;
	protected double typeDiscountPercent;
	protected double monthDiscountPercent;
	
	public CalculateTuition(EnvironDAO environDAO){
		super();
		this.environDAO = environDAO;
	}
	
	public CalculateTuition(List<ClassSearchUnit> bookList, EnvironDAO environDAO) {
		super();
		this.bookList = bookList;
		this.month = bookList.get(0).weeks/4;
		this.environDAO =environDAO;
		
		typeDiscountName = environDAO.getSmartDiscoutName();
		typeDiscountPercent = environDAO.getSmartDiscountPercent();
		monthDiscountPercent = environDAO.getMonthDiscountPercent(month);
	}
	
	public boolean isSatisfyDayDiscount(){
		if(bookList == null)
			return false;
		boolean result = true;
		for(ClassSearchUnit csu : bookList){
			int hour = csu.getDuration().getRt().getHour();
			if(!(start <= hour && hour < end)){
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	public double getDayDiscount(){
		if(!isSatisfyDayDiscount())
			return 0;
		else
			return environDAO.getDayDiscountPercent();
	}
	
	public abstract Tuition calculate();
	
	public Tuition calculatedLogicalPrice(int duration){
		int basePrice = environDAO.getSmartBasePrice();
		double adjust = environDAO.getSmartAdjust();
		
		double differ = (duration*50)/((duration-1)*50);
		
		int finalPrice = (int) ((basePrice/2) * duration * (differ - adjust));
		
		return new Tuition(typeDiscountName, typeDiscountPercent, finalPrice, monthDiscountPercent, getDayDiscount());
	}
}
