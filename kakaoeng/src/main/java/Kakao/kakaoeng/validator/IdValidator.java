package Kakao.kakaoeng.validator;


public class IdValidator<T> extends PatternValidator<T>{
	private static final String ID_PATTERN = "^[a-z0-9_]*$";
	
	private IdValidator(String regex, String formatName) {
		super(regex, formatName);
	}
	
	public IdValidator() {
		this(ID_PATTERN, "Id");
	}
}