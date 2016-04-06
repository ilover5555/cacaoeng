package Kakao.kakaoeng.validator;


public abstract class AbstractValidator<T> implements Validator<T> {

	protected boolean bEnglish;
	
	public void setbEnglish(boolean bEnglish) {
		this.bEnglish = bEnglish;
	}

}
