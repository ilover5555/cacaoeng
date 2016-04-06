package Kakao.kakaoeng.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.FileChecker;



public class Teacher extends User implements Comparable<Teacher> {
	
	public enum Quality{
		Normal, Better, Best;
	}
	
	public Teacher(){
		
	}
	
	public Teacher(String id, String pw, String name, String className, Date birth, UserType userType,
			Gender gender, String address, String cellPhone, String homePhone,
			Date lastLogin, Date registerDate, String univ, String univDetail, String education, String competency,
			String experience, boolean toefl, boolean ilets, String specImage, String internetSpeed,
			String internetProvider, String internetType, String upSpeed, String downSpeed, String os, String bankName,
			String bankAccount, String primaryProfile, String primaryVoice, String skype, String lineId, boolean confirm, Rate rate,
			boolean representitve, int salary, Quality pronunciation, Quality accent, String comment, boolean retirement) {
		super(id, pw, name, className, birth, userType, gender, address, cellPhone, homePhone, lastLogin, registerDate, skype, primaryProfile);
		this.univ = univ;
		this.univDetail = univDetail;
		this.education = education;
		this.competency = competency;
		this.experience = experience;
		this.toefl = toefl;
		this.ilets = ilets;
		this.specImage = specImage;
		this.internetSpeed = internetSpeed;
		this.internetProvider = internetProvider;
		this.internetType = internetType;
		this.upSpeed = upSpeed;
		this.downSpeed = downSpeed;
		this.os = os;
		this.bankName = bankName;
		this.bankAccount =bankAccount;
		this.primaryVoice = primaryVoice;
		this.lineId = lineId;
		this.confirm = confirm;
		this.rate  = rate;
		this.representitive = representitve;
		this.salray = salary;
		this.pronunciation = pronunciation;
		this.accent = accent;
		this.comment = comment;
		this.retirement = retirement;
	}
	public enum Rate{
		A(0),B(1),C(2),Wait(100);
		
		private int code;
		
		Rate(int code){
			this.code = code;
		}

		public int getCode() {
			return code;
		}
		
		public static int compare(Rate l, Rate r){
			if(l.code < r.code)
				return -1;
			else if(l.code > r.code)
				return 1;
			else
				return 0;
		}
		
	}
	
	
	private String primaryVoice;
	private boolean confirm;
	private Rate rate;
	private String lineId;
	private String competency;
	private String univ;
	private String univDetail;
	private String education;
	private String experience;
	private boolean toefl;
	private boolean ilets;
	private String specImage;
	private String internetSpeed;
	private String internetProvider;
	private String internetType;
	private String upSpeed;
	private String downSpeed;
	private String os;
	private String bankName;
	private String bankAccount;
	private int salray;
	private Quality pronunciation;
	private Quality accent;
	private String comment;
	private boolean retirement;
	private boolean representitive;
	private UploadedFile picture;
	private UploadedFile voice;
	private UploadedFile spec;
	
	public String getPrimaryVoice(){
		if(FileChecker.isFileExist(primaryVoice))
			return primaryVoice;
		else
			return "";
	}
	public boolean getConfirm(){return confirm;}
	public Rate getRate(){return this.rate;}
	public String getLineId(){return this.lineId;}
	public String getCompetency(){return this.competency;}
	public String getUniv(){return this.univ;}
	public String getUnivDetail(){return this.univDetail;}
	public String getEducation(){return this.education;}
	public String getExperience(){return this.experience;}
	public boolean getToefl(){return this.toefl;}
	public boolean getIlets(){return this.ilets;}
	public String getSpecImage() {return specImage;}
	public void setSpecImage(String specImage) {this.specImage = specImage;}
	public String getInternetSpeed(){return this.internetSpeed;}
	public String getInternetProvider(){return this.internetProvider;}
	public String getInternetType(){return this.internetType;}
	public String getUpSpeed(){return this.upSpeed;}
	public String getDownSpeed(){return this.downSpeed;}
	public String getOs(){return this.os;}
	public String getBankName(){return this.bankName;}
	public String getBankAccount(){return this.bankAccount;}
	public int getSalary(){return this.salray;}
	public Quality getPronunciation(){return this.pronunciation;}
	public Quality getAccent(){return this.accent;}
	public String getComment(){return this.comment;}
	public boolean getRetirement(){return this.retirement;}
	public boolean getRepresentitive(){return this.representitive;}

	public void setPrimaryVoice(String primaryVoice) {this.primaryVoice = primaryVoice;}
	public String getShortClassName(){
		return this.getClassName().split(" ")[0];
	}

	public void loadPicture(String DBPath){
		picture = new UploadedFile(DBPath);
	}

	public UploadedFile getSpec() {
		if(spec == null)
			spec = new UploadedFile(this.getSpecImage());
		return spec;
	}
	
	public UploadedFile getPicture() {
		if(picture == null)
			picture = new UploadedFile(this.getPrimaryProfilePicture());
		return picture;
	}

	public UploadedFile getVoice() {
		if(voice == null)
			voice = new UploadedFile(this.getPrimaryVoice());
		return voice;
	}
	
	private List<String> upperList = null;
	private List<String> lowerList = null;
	
	public void parseCompetency(){
		try{
			String[] bigParse = competency.split("\n");
			upperList = new ArrayList<>();
			lowerList = new ArrayList<>();
			for(int i=0; i<=2; i++){
				upperList.add(bigParse[i]);
			}
			for(int i=3; i<bigParse.length; i++){
				lowerList.add(bigParse[i]);
			}
		}catch(RuntimeException e){
			
		}
	}
	
	public List<String> getUpperList(){
		if(upperList == null)
			parseCompetency();
		return upperList;
	}
	public List<String> getLowerList(){
		if(lowerList == null)
			parseCompetency();
		return lowerList;
	}

	@Override
	public int compareTo(Teacher o) {
		return this.getRate().compareTo(o.getRate());
	}
}