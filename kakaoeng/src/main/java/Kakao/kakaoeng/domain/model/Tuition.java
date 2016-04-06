package Kakao.kakaoeng.domain.model;

import Kakao.kakaoeng.Util;

public class Tuition {

	String typeDiscountName;
	double typeDiscountPercent;
	int finalPrice;
	double monthDiscountPercent;
	double dayDiscountPercent;
	public Tuition(String typeDiscountName, double typeDiscountPercent, int finalPrice, double monthDiscountPercent, double dayDiscountPercent) {
		super();
		this.typeDiscountName = typeDiscountName;
		this.typeDiscountPercent = typeDiscountPercent;
		this.finalPrice = finalPrice;
		this.monthDiscountPercent = monthDiscountPercent;
		this.dayDiscountPercent = dayDiscountPercent;
	}
	public int getOriginalPrice(){
		return this.finalPrice;
	}
	public String getTypeDiscountName() {
		return typeDiscountName;
	}
	public double getTypeDiscountPercent() {
		return typeDiscountPercent;
	}
	public int getFinalPrice() {
		return finalPrice;
	}
	public double getMonthDiscountPercent() {
		return monthDiscountPercent;
	}
	
	public String getTypeDisCountPercentString(){
		return Util.getPercentStringFromDouble(this.typeDiscountPercent);
	}
	
	public String getMonthDiscountPercentString(){
		return Util.getPercentStringFromDouble(this.monthDiscountPercent);
	}
	
	public String getDayDiscountPercentString(){
		return Util.getPercentStringFromDouble(this.dayDiscountPercent);
	}
	
	public int getPurchasePrice(){
		return (int) ((int)((this.finalPrice*(1-this.monthDiscountPercent)*(1-this.typeDiscountPercent)*(1-dayDiscountPercent))/100))*100;
	}
}
