package Kakao.kakaoeng.domain.model;

import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.User.Gender;

public class NaverLoginResult {
	int resultCode;
	String message;
	String encId;
	String nickName;
	String id;
	Gender gender;
	int age;
	Date birthday;
	String profileImage;
	String email;
	
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}
	
	public NaverLoginResult(Document document){
		NodeList resultList = document.getElementsByTagName("result");
		Node resultNode =resultList.item(0);
		resultCode = Integer.parseInt(getTagValue("resultcode", (Element)resultNode));
		message = getTagValue("message", (Element)resultNode);
		NodeList responseList = document.getElementsByTagName("response");
		Node responseNode = responseList.item(0);
		encId = getTagValue("enc_id", (Element)responseNode);
		nickName = getTagValue("nickname", (Element)responseNode);
		id = getTagValue("id", (Element)responseNode);
		switch(getTagValue("gender", (Element)responseNode)){
		case "F":
			gender = Gender.Female;
			break;
		default:
			gender = Gender.Male;
			break;
		}
		age = Integer.parseInt(getTagValue("age", (Element)responseNode).split("-")[0]);
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int birthYear = year - age + 1;
		String birthDayFormat = birthYear + "-" + getTagValue("birthday", (Element)responseNode);
		birthday = Util.parseDate(birthDayFormat);
		profileImage = getTagValue("profile_image", (Element)responseNode);
		email = getTagValue("email", (Element)responseNode);
	}

	public int getResultCode() {
		return resultCode;
	}

	public String getMessage() {
		return message;
	}

	public String getEncId() {
		return encId;
	}

	public String getNickName() {
		return nickName;
	}

	public String getId() {
		return id;
	}

	public Gender getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public String getEmail() {
		return email;
	}
	
	
}
