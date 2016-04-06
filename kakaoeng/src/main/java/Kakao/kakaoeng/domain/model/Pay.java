package Kakao.kakaoeng.domain.model;

public class Pay {

	int id;
	String teacherId;
	int month;
	boolean payed;
	public Pay(int id, String teacherId, int month, boolean payed) {
		super();
		this.id = id;
		this.teacherId = teacherId;
		this.month = month;
		this.payed = payed;
	}
	public Pay(String teacherId, int month, boolean payed) {
		this(-1, teacherId, month, payed);
	}
	public int getId() {
		return id;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public int getMonth() {
		return month;
	}
	public boolean getPayed() {
		return payed;
	}
	
	
}
