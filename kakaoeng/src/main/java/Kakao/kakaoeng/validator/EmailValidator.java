package Kakao.kakaoeng.validator;


public class EmailValidator<T> extends PatternValidator<T>{
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private EmailValidator(String regex, String formatName) {
		super(regex, formatName);
	}
	
	public EmailValidator() {
		this(EMAIL_PATTERN, "Email");
	}
}
