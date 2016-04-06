package Kakao.kakaoeng.domain.model;

import java.util.Date;

public class Admin extends User {
	public Admin(String id, String pw, String name, String cellPhone, String address, Date lastLogin, Date registerDate, UserType userType){
		super(id, pw, name, "", null, userType, Gender.Male, address, cellPhone, "", lastLogin, registerDate, "", "");
		if(!(userType.equals(UserType.Admin) || userType.equals(UserType.Executor)))
			throw new IllegalArgumentException("Admin Class Must Get UserType Admin Or Executor : " + userType);
	}
	
	public String getShortClassName(){
		return this.getClassName();
	}
}