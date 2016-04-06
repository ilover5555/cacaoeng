package Kakao.kakaoeng.domain.model;

import java.util.Date;

import Kakao.kakaoeng.dao.LectureDAO;

public class Purchase {

	public enum Method{
		Credit, CellPhone, Account, Coupon
	}
	
	int id;
	String studentId;
	int price;
	String approvedNumber;
	int procrastinate;
	Method method;
	boolean confirm;
	Lecture lecture;
	boolean rejected;
	int fullClass;
	int weeks=-1;
	Date purchaseDate;
	
	public int getWeeks(){
		if(weeks == -1)
			throw new RuntimeException("Weeks is not yet initialized");
		return weeks;
	}
	
	public void loadWeeks(int sizeOfOneClass){
		weeks = fullClass / sizeOfOneClass;
	}
	
	public void loadLecture(LectureDAO lectureDAO){
		lecture = lectureDAO.getOnGoingLectureByPurchase(id);
	}
	
	public Lecture getLecture(){
		return this.lecture;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public String getStudentId() {
		return studentId;
	}
	public int getPrice() {
		return price;
	}
	public String getApprovedNumber() {
		return approvedNumber;
	}
	public int getProcrastinate() {
		return procrastinate;
	}
	public boolean getConfirm() {
		return confirm;
	}
	public Method getMethod() {
		return method;
	}
	public static String makeApprovedNumber(){
		return "purchase:" + (new Date()).getTime();
	}
	public static String makeWaitNumber(){
		return "Wait:" + (new Date()).getTime();
	}
	public boolean getRejected() {
		return rejected;
	}

	public int getFullClass() {
		return fullClass;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public Purchase(int id, String studentId, int price, String approvedNumber, int procrastinate, Method method, boolean confirm, boolean rejected, int fullClass) {
		this(id, studentId, price, approvedNumber, procrastinate, method, confirm, rejected, fullClass, null);
	}
	
	public Purchase(int id, String studentId, int price, String approvedNumber, int procrastinate, Method method, boolean confirm, boolean rejected, int fullClass, Date purchaseDate) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.price = price;
		this.approvedNumber = approvedNumber;
		this.procrastinate = procrastinate;
		this.method = method;
		this.confirm = confirm;
		this.rejected = rejected;
		this.fullClass = fullClass;
		this.purchaseDate = purchaseDate;
	}
	public Purchase(String studentId, int price, int procrastinate, Method method, boolean confirm, boolean rejected, int fullClass){
		this(-1, studentId, price, makeApprovedNumber(), procrastinate, method, confirm, rejected, fullClass);
	}
}
