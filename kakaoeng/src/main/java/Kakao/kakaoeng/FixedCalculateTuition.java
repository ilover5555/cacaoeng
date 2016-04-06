package Kakao.kakaoeng;

import java.util.List;

import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Tuition;

public class FixedCalculateTuition extends CalculateTuition {

	protected int month;
	protected int times;
	protected int duration;
	
	public void setMonth(int month) {
		this.month = month;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public FixedCalculateTuition(EnvironDAO environDAO, int month, int times, int duration) {
		super(environDAO);
		this.month = month;
		this.times = times;
		this.duration = duration;
	}

	@Override
	public Tuition calculate() {
		int d = duration * month*times;
		if(d<=40){
		
			int finalPrice = environDAO.getSmartFinalPrice(times*duration*month);
		
			return new Tuition(typeDiscountName, typeDiscountPercent, finalPrice, monthDiscountPercent, 0);
		}
		else{
			return this.calculatedLogicalPrice(d);
		}
	}

}
