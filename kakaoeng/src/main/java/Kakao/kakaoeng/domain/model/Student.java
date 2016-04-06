package Kakao.kakaoeng.domain.model;

import java.util.Date;

public class Student extends User{
	public Student(String id, String pw, String name, String className, Date birth, UserType userType,
			Gender gender, String ZIP, String address, String detailAddress, String cellPhone, String homePhone, String primaryProfilePicture,
			Date lastLogin, Date registerDate ,String parentName, String parentPhone,Level level, String skypeId, String note, int coupon, String email) {
		super(id, pw, name, className, birth, userType, gender, address, cellPhone, homePhone, lastLogin, registerDate, skypeId, primaryProfilePicture);
		this.parentName = parentName;
		this.parentPhone = parentPhone;
		this.level = level;
		this.note = note;
		this.coupon = coupon;
		this.email = email;
		this.ZIP = ZIP;
		this.detailAddress = detailAddress;
	}
	public enum Level{
		Untested(-1), Level1(1), Level2(2), Level3(3), Level4(4), Level5(5), Level6(6), Level7(7);
		private int level;
		private Level(int level){
			this.level = level;
		}
		
		public int getLevel(){
			return this.level;
		}
	}
	String parentName;
	String parentPhone;
	Level level;
	String note;
	int coupon;
	String email;
	String ZIP;
	String detailAddress;
	
	public String getZIP() {
		return ZIP;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public String getEmail() {
		return email;
	}
	public String getParentName() {
		return parentName;
	}
	public int getCoupon() {
		return coupon;
	}
	public String getNote() {
		return note;
	}
	public String getParentPhone() {
		return parentPhone;
	}
	public Level getLevel() {
		return level;
	}
	public String getEmailId(){
		return this.email.split("@")[0];
	}
	
	public String getEmailHost(){
		return this.email.split("@")[1];
	}
	@Override
	public boolean equals(Object obj) {
		return this.getId().equals(((Student)obj).getId());
	}
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	
}
