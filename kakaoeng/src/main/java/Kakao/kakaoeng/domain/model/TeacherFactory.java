package Kakao.kakaoeng.domain.model;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.DataGetter;
import Kakao.kakaoeng.FieldSet;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Teacher.Quality;
import Kakao.kakaoeng.domain.model.Teacher.Rate;
import Kakao.kakaoeng.domain.model.User.Gender;
import Kakao.kakaoeng.domain.model.User.UserType;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.field.GeneralField;
import Kakao.kakaoeng.validator.DateValidator;
import Kakao.kakaoeng.validator.OmitValidator;

public class TeacherFactory
{
	String id = null;
	String pw = null;
	String name = null;
	Date birth = null;
	UserType userType = UserType.Teacher;
	Gender gender = Gender.Male;
	String address = null;
	String cellPhone = null;
	String homePhone = null;
	
	
	public void setId(String id) {this.id = id;}
	public void setPw(String pw) {this.pw = pw;}
	public void setName(String nameame) {this.name = nameame;}
	public void setBirth(Date birth) {this.birth = birth;}
	public void setUserType(UserType userType) {this.userType = userType;}
	public void setGender(Gender gender) {this.gender = gender;}
	public void setAddress(String address) {this.address = address;}
	public void setCellPhone(String cellPhone) {this.cellPhone = cellPhone;}
	public void setHomePhone(String homePhone) {this.homePhone = homePhone;}
	public void setLastLogin(Date lastLogin){this.lastLogin = lastLogin;}
	public void setRegisterDate(Date registerDate){this.registerDate = registerDate;}
	
	String primaryProfilePicture = null;
	String primaryVoice = null;
	boolean confirm = false;
	Rate rate = null;
	boolean representitive = false;
	String className = null;
	String lineId;
	String competency;
	String univ;
	String univDetail;
	String education;
	String experience;
	boolean toefl;
	boolean ilets;
	String specImage;
	String internetSpeed;
	String internetProvider;
	String internetType;
	String upSpeed;
	String downSpeed;
	String os;
	String bankName;
	String bankAccount;
	int salary;
	Quality pronunciation;
	Quality accent;
	String comment;
	boolean retirement;
	Date lastLogin;
	Date registerDate;
	String skype;
	
	public void setPrimaryProfilePicture(String primaryProfilePicture) {this.primaryProfilePicture = primaryProfilePicture;}
	public void setConfirm(boolean confirm){this.confirm = confirm;}
	public void setRate(Rate rate){this.rate = rate;}
	public void setRepresentitve(boolean r){this.representitive = r;}
	public void setClassName(String className){this.className = className;}
	public void setLineId(String lineId){this.lineId = lineId;}
	public void setCompetency(String competency){this.competency = competency;}
	public void setUniv(String univ){this.univ = univ;}
	public void setUnivDetail(String univDetail){this.univDetail = univDetail;}
	public void setEducation(String education){this.education = education;}
	public void setExperience(String experience){this.experience = experience;}
	public void setToefl(boolean canToefl){this.toefl = canToefl;}
	public void setIlets(boolean canIlets){this.ilets = canIlets;}
	public void setSpecImage(String specImage) {this.specImage = specImage;}
	public void setInternetSpeed(String internetSpeed){this.internetSpeed = internetSpeed;}
	public void setInternetProvider(String internetProvider){this.internetProvider = internetProvider;}
	public void setInternetType(String internetType){this.internetType = internetType;}
	public void setUpSpeed(String upSpeed){this.upSpeed = upSpeed;}
	public void setDownSpeed(String downSpeed){this.downSpeed = downSpeed;}
	public void setOs(String os){this.os = os;}
	public void setBankName(String bankName){this.bankName = bankName;}
	public void setBankAccount(String bankAccount){this.bankAccount = bankAccount;}
	public void setSalary(int salary){this.salary = salary;}
	public void setPronunciation(Quality pronunciation){this.pronunciation = pronunciation;}
	public void setAccent(Quality accent){this.accent = accent;}
	public void setComment(String comment){this.comment = comment;}
	public void setRetirement(boolean retirement){this.retirement = retirement;}
	public void setSkype(String skype){this.skype = skype;}
	public void setPrimaryVoice(String primaryVoice) {this.primaryVoice = primaryVoice;}
	
	public Teacher makeTeacherInstance()
	{
		return new Teacher(id, pw, name, className, birth, userType, gender, 
				address, cellPhone, homePhone, lastLogin, registerDate, univ, 
				univDetail, education, competency, experience, toefl, ilets, specImage,
				internetSpeed, internetProvider, internetType, upSpeed, downSpeed, os, 
				bankName, bankAccount, primaryProfilePicture, primaryVoice, skype, lineId, confirm, 
				rate, representitive, salary, pronunciation, accent, comment, retirement);
	}
	
	public static Teacher makeInstanceFromFieldSet(FieldSet teacherFieldSet)
	{
		TeacherFactory tf = TeacherFactory.fillTeacherFactory(teacherFieldSet);
		
		return tf.makeTeacherInstance();
	}

	public static FieldSet makeTeacherFieldSet(DataGetter dataGetter)
	{
		FieldSet fieldSet = new FieldSet(true);
		Field<String> id = dataGetter.getIdField();
		Field<String> pw = dataGetter.getPasswordField();
		fieldSet.addField(id).addField(pw);
		
		Field<String> name = new GeneralField<String>("name", Util.decode(dataGetter.get("name", String.class)));
		Field<String> className = new GeneralField<String>("className", Util.decode(dataGetter.get("className", String.class)));
		Field<User.Gender> gender = new GeneralField<User.Gender>("gender", User.Gender.valueOf(dataGetter.get("gender", String.class)));
		Field<String> cellPhone = new GeneralField<String>("cellPhone", dataGetter, String.class);
		Field<String> homePhone = new Field<String>("homePhone", dataGetter, String.class);
		Field<String> address = new Field<String>("address", Util.decode(dataGetter.get("address", String.class)));
		Field<String> birth = new GeneralField<String>("birth", dataGetter.getBirth());
		birth.addValidator(new DateValidator<String>());
		fieldSet.addField(name).addField(gender).addField(className)
		.addField(cellPhone).addField(homePhone).addField(address).addField(birth);
		
		Field<String> internetProvider = new Field<String>("internetProvider",  Util.decode(dataGetter.get("internetProvider", String.class)));
		Field<String> internetSpeed = new Field<String>("internetSpeed", dataGetter, String.class);
		Field<String> internetType = new Field<String>("internetType", dataGetter, String.class);
		Field<String> upSpeed = new Field<String>("upSpeed",  Util.decode(dataGetter.get("upSpeed", String.class)));
		Field<String> downSpeed = new Field<String>("downSpeed",  Util.decode(dataGetter.get("downSpeed", String.class)));
		Field<String> os = new Field<String>("os",  Util.decode(dataGetter.get("os", String.class)));
		fieldSet.addField(internetProvider).addField(internetSpeed).addField(internetType)
		.addField(upSpeed).addField(downSpeed).addField(os);
		
		Field<String> univ = new GeneralField<String>("univ", Util.decode(dataGetter.get("univ", String.class)));
		Field<String> univDetail = new GeneralField<String>("univDetail", Util.decode(dataGetter.get("univDetail", String.class)));
		Field<String> education = new Field<String>("education", Util.decode(dataGetter.get("education", String.class)));
		Field<String> experience = new Field<String>("experience", Util.decode(dataGetter.get("experience", String.class)));
		Field<String> competency = new Field<String>("competency", Util.decode(dataGetter.get("competency", String.class)));
		competency.addValidator(new OmitValidator<>());
		Field<Boolean> toefl = new GeneralField<Boolean>("toefl", dataGetter.getToefl());
		Field<Boolean> ilets = new GeneralField<Boolean>("ilets", dataGetter.getIlets());
		fieldSet.addField(univ).addField(univDetail).addField(experience).addField(education).addField(competency)
		.addField(toefl).addField(ilets);
		
		Field<String> bankName = new Field<String>("bankName",  Util.decode(dataGetter.get("bankName", String.class)));
		Field<String> bankAccount = new Field<String>("bankAccount",  Util.decode(dataGetter.get("bankAccount", String.class)));
		Field<String> skypeId = new GeneralField<String>("skype", dataGetter, String.class);
		Field<String> lineId = new Field<String>("lineId", dataGetter, String.class);
		fieldSet.addField(bankName).addField(bankAccount).addField(skypeId).addField(lineId);
		
		List<Field<?>> list = dataGetter.getFields();
		for(Field<?> field : list){
			fieldSet.addField(field);
		}
		
		return fieldSet;
	}
	
	public static TeacherFactory fillTeacherFactory(FieldSet fieldSet) {
		TeacherFactory tf = new TeacherFactory();
		tf.setId((String) fieldSet.getField("id").getValue());
		tf.setPw((String) fieldSet.getField("pw").getValue());
		tf.setName((String) fieldSet.getField("name").getValue());
		tf.setClassName((String) fieldSet.getField("className").getValue());
		String birth = (String) fieldSet.getField("birth").getValue();
		Date birthComputed = null;
		try{
			birthComputed = Util.parseDate(birth);
		}catch(RuntimeException e){
			birthComputed = new Date();
		}
		tf.setBirth(birthComputed);
		tf.setUserType(UserType.Teacher);
		tf.setGender((Gender) fieldSet.getField("gender").getValue());
		tf.setAddress((String) fieldSet.getField("address").getValue());
		tf.setCellPhone((String) fieldSet.getField("cellPhone").getValue());
		tf.setHomePhone((String) fieldSet.getField("homePhone").getValue());
		tf.setLastLogin((Date) fieldSet.getField("lastLogin").getValue());
		tf.setRegisterDate((Date) fieldSet.getField("registerDate").getValue());
		tf.setClassName((String) fieldSet.getField("className").getValue());
		tf.setUniv((String) fieldSet.getField("univ").getValue());
		tf.setUnivDetail((String) fieldSet.getField("univDetail").getValue());
		tf.setEducation((String) fieldSet.getField("education").getValue());
		tf.setCompetency((String) fieldSet.getField("competency").getValue());
		tf.setExperience((String) fieldSet.getField("experience").getValue());
		tf.setToefl((Boolean)fieldSet.getField("toefl").getValue());
		tf.setIlets((Boolean)fieldSet.getField("ilets").getValue());
		tf.setSpecImage((String) fieldSet.getField("specImage").getValue());
		tf.setInternetSpeed((String) fieldSet.getField("internetSpeed").getValue());
		tf.setInternetProvider((String) fieldSet.getField("internetProvider").getValue());
		tf.setInternetType((String) fieldSet.getField("internetType").getValue());
		tf.setUpSpeed((String) fieldSet.getField("upSpeed").getValue());
		tf.setDownSpeed((String) fieldSet.getField("downSpeed").getValue());
		tf.setOs((String) fieldSet.getField("os").getValue());
		tf.setBankName((String) fieldSet.getField("bankName").getValue());
		tf.setBankAccount((String) fieldSet.getField("bankAccount").getValue());
		tf.setPrimaryProfilePicture((String) fieldSet.getField("primaryProfile").getValue());
		tf.setPrimaryVoice((String) fieldSet.getField("primaryVoice").getValue());
		tf.setSkype((String) fieldSet.getField("skype").getValue());
		tf.setLineId((String) fieldSet.getField("lineId").getValue());
		tf.setConfirm((Boolean) fieldSet.getField("confirm").getValue());
		tf.setRate((Rate) fieldSet.getField("rate").getValue());
		tf.setRepresentitve((Boolean) fieldSet.getField("representitive").getValue());
		tf.setSalary((Integer) fieldSet.getField("salary").getValue());
		tf.setPronunciation((Quality) fieldSet.getField("pronunciation").getValue());
		tf.setAccent((Quality) fieldSet.getField("accent").getValue());
		tf.setComment((String) fieldSet.getField("comment").getValue());
		tf.setRetirement((Boolean) fieldSet.getField("retirement").getValue());
		
		return tf;
	}
}