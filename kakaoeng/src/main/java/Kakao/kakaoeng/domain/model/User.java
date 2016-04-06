package Kakao.kakaoeng.domain.model;

import java.util.Calendar;
import java.util.Date;

import Kakao.kakaoeng.FileChecker;
import Kakao.kakaoeng.FileSaver;
import Kakao.kakaoeng.Util;

public class User {
	
	@Override
	public String toString() {
		return this.id;
	}
	public enum UserType{
		Admin,Teacher,Student, Executor;
	}
	public enum Gender{
		Male, Female
	}
	
	
	
	private String id;
	private String pw;
	private String name;
	private String className;
	private Date birth;
	private UserType userType;
	private Gender gender;
	private String address;
	private String cellPhone;
	private String homePhone;
	private Date lastLogin;
	private Date registerDate;
	private String skypeId;
	
	private String primaryProfilePicture;
	
	public User(){
		
	}
	
	public User(String id, String pw, String name, String className, Date birth, UserType userType, Gender gender,
			String address, String cellPhone, String homePhone, Date lastLogin, Date registerDate, String skypeId, String primaryProfilePicture) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.birth = birth;
		this.userType = userType;
		this.gender = gender;
		this.address = address;
		this.cellPhone = cellPhone;
		this.homePhone = homePhone;
		this.lastLogin = lastLogin;
		this.registerDate = registerDate;
		this.skypeId = skypeId;
		this.className = className;
		this.primaryProfilePicture = primaryProfilePicture;
	}
	
	public String getPrimaryProfilePicture() {
		if(FileChecker.isFileExist(primaryProfilePicture))
			return primaryProfilePicture;
		else
			return "";
	}

	public String getSkypeId() {
		return skypeId;
	}

	public String getClassName() {
		return className;
	}

	public Date getLastLogin(){
		return lastLogin;
	}
	public Date getRegisterDate(){
		return registerDate;
	}
	public String getRegisterDateForm(){
		return Util.dateFormatting(registerDate);
	}
	public String getId() {
		return id;
	}
	public String getPw() {
		return pw;
	}
	public String getName() {
		return name;
	}
	public Date getBirth() {
		return birth;
	}
	public int getBirthYear(){
		if(this.birth == null)
			return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(birth);
		return calendar.get(Calendar.YEAR);
	}
	public int getBirthMonth(){
		if(this.birth == null)
			return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(birth);
		return calendar.get(Calendar.MONTH)+1;
	}
	public int getBirthDate(){
		if(this.birth == null)
			return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(birth);
		return calendar.get(Calendar.DATE);
	}
	public UserType getUserType() {
		return userType;
	}
	public Gender getGender() {
		return gender;
	}
	public String getAddress() {
		return address;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public String getParsedPhone(int index){
		if(cellPhone == null || cellPhone == "")
			return "";
		String[] splitted = cellPhone.split("-");
		if(splitted.length > index)
			return splitted[index];
		else
			return "";
	}
	public String getHomePhone() {
		return homePhone;
	}
	public String getBirthFormat(){
		if(birth == null)
			return "";
		return Util.dateFormatting(birth);
	}
	public String getSkype() {
		return skypeId;
	}
	public String getBirthDateForm(){
		return Util.dateFormatting(birth);
	}

	public void setPrimaryProfilePicture(String primaryProfilePicture) {
		this.primaryProfilePicture = primaryProfilePicture;
	}
	
	
}
