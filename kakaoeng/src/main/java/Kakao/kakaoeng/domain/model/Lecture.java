package Kakao.kakaoeng.domain.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.CircularList;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

public class Lecture {
	
	int id;
	int purchaseNumber;
	int fullClass;
	
	Date StartDate;
	Date EndDate;
	int weeks;
	String teacherId;
	String studentId;
	Status status; 
	Teacher teacher;
	Student student;
	String note;
	int done=-1;
	Course course;
	String book;
	Purchase purchase;
	boolean align;
	boolean beforeNotified;
	boolean afterNotified;
	
	public boolean getBeforeNotified() {
		return beforeNotified;
	}

	public boolean getAfterNotified() {
		return afterNotified;
	}

	public String getTeacherShortName(){
		return this.getTeacher().getShortClassName();
	}
	
	public String getKoreanStartForm(){
		SimpleDateFormat f = new SimpleDateFormat("yyyy년 MM/dd");
		return f.format(this.getStartDate());
	}
	
	public String getKoreanStartDayOfWeek(){
		return DayOfWeek.getInstanceFromDate(this.getStartDate()).getShortDayOfWeek();
	}
	
	public boolean isPurchaseLoaded(){
		if(purchase == null)
			return false;
		else
			return true;
	}
	
	public Purchase getPurchase(){
		if(purchase == null)
			throw new IllegalStateException("purchase is not yet loaded");
		return purchase;
	}
	
	public void loadPurchase(PurchaseDAO purchaseDAO){
		purchase = purchaseDAO.getPurchaseById(this.getPurchaseNumber());
	}
	
	public String getCalculatedEndDateForm(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(StartDate);
		cal.add(Calendar.WEEK_OF_YEAR, weeks);
		return Util.dateFormatting(cal.getTime());
	}
	
	public String getNote() {
		return note;
	}

	public void setEndDate(List<OneClass> oneClassList, ClassLogDAO classLogDAO, HolidayDAO holidayDAO){
		if(this.EndDate != null)
			return;
		
		for(OneClass oc : oneClassList){
			oc.setStartDate(this);
		}
		int i=0;
		System.out.println(oneClassList.size());
		for(int j=0; j<oneClassList.size()-1; j++){
			if(oneClassList.get(j).getStartDate().after(oneClassList.get(j+1).getStartDate()))
				break;
			i++;
		}
		if(i == oneClassList.size()-1)
			i=-1;
		int count = this.fullClass;
		CircularList<OneClass> circularOneClassList = new CircularList<>();
		circularOneClassList.addAll(oneClassList);
		circularOneClassList.set(i+1);
		Date result = this.getStartDate();
		Date date = null;
		while(count>0){
			OneClass oneClass = circularOneClassList.getNext();
			date = Util.addDate(oneClass.getStartDate(), circularOneClassList.getCircle()*7);
			if(!Util.isPro(date, holidayDAO, classLogDAO, oneClass, this)){
				count--;
				result = date;
				System.out.println("COUNTED : " + count + ",date:"+Util.dateFormatting(date));
			}
			else{
				System.out.println("PROCRASTINATE : " + Util.dateFormatting(date));
			}
		}
		this.EndDate = result;
	}
	
	public int getDone() throws RuntimeException{
		if(done == -1)
			throw new RuntimeException("done is not initialized");
		return done;
	}
	
	public void setDone(List<OneClass> oneClassList){
		int count = 0;
		for(OneClass oc : oneClassList){
			count += oc.getDone();
		}
		this.done = count;
	}
	
	public String getStartDateForm(){
		return Util.dateFormatting(StartDate);
	}
	
	public String getEndDateForm(){
		if(EndDate == null)
			return null;
		else
			return Util.dateFormatting(EndDate);
	}
	
	public Status getStatus() {
		return status;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public Student getStudent() {
		return student;
	}

	public void loadTeacher(TeacherDAO teacherDAO){
		teacher = teacherDAO.getTeacherWithId(teacherId);
	}
	
	public void loadStudent(StudentDAO studentDAO){
		student = studentDAO.findStudentById(studentId);
	}
	
	@Override
	public boolean equals(Object obj) {
		return purchaseNumber == ((Lecture)obj).getPurchaseNumber();
	}
	@Override
	public int hashCode() {
		return Integer.hashCode(purchaseNumber);
	}
	public enum Status{
		Done, OnGoing, TeacherChange, TimeChange, Cancel;
	}
	public int getFullClass() {
		return fullClass;
	}
	public Date getEndDate() {
		return EndDate;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public String getStudentId() {
		return studentId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPurchaseNumber() {
		return purchaseNumber;
	}
	public Date getStartDate() {
		return StartDate;
	}
	public int getWeeks() {
		return weeks;
	}
	
	public Course getCourse() {
		return course;
	}

	public String getBook() {
		return book;
	}

	public boolean getAlign() {
		return align;
	}

	public Lecture(int id, int purchaseNumber, int fullClass, Date startDate,
			Date endDate, int weeks, String teacherId, String studentId, Status status, String note,
			Course course, String book, boolean align, boolean beforeNotified, boolean afterNotified) {
		super();
		this.id = id;
		this.purchaseNumber = purchaseNumber;
		this.fullClass = fullClass;
		StartDate = startDate;
		EndDate = endDate;
		this.weeks = weeks;
		this.teacherId = teacherId;
		this.studentId = studentId;
		this.status = status;
		this.note = note;
		this.course = course;
		this.book = book;
		this.align =align;
	}
	
	public void print(Object o){
		System.out.println(o);
	}
	
	public boolean inRange(String date){
		Date checkDate = Util.parseDate(date);
		Date startDate = this.getStartDate();
		Date endDate = this.getEndDate();
		boolean equalStartDate = checkDate.equals(startDate);
		boolean afterStartDate = checkDate.after(startDate);
		boolean equalEndDate = checkDate.equals(endDate);
		boolean beforeEndDate = checkDate.before(endDate);
		if(( afterStartDate ||  equalStartDate) &&
				( beforeEndDate || equalEndDate))
			return true;
		
		return false;
	}
	
	public boolean inRange(Date checkDate){
		checkDate = Util.normalize(checkDate);
		Date startDate = this.getStartDate();
		Date endDate = this.getEndDate();
		System.out.println(startDate.getTime());
		System.out.println(endDate.getTime());
		System.out.println(checkDate.getTime());
		boolean equalStartDate = checkDate.equals(startDate);
		boolean afterStartDate = checkDate.after(startDate);
		boolean equalEndDate = checkDate.equals(endDate);
		boolean beforeEndDate = checkDate.before(endDate);
		if(( afterStartDate ||  equalStartDate) &&
				( beforeEndDate || equalEndDate))
			return true;
		
		return false;
	}
	
	public int passed(Date date, List<OneClass> oneClassList, HolidayDAO holidayDAO, ClassLogDAO classLogDAO){
		for(OneClass oc : oneClassList){
			oc.setStartDate(this);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = Util.normalize(calendar.getTime());
		CircularList<OneClass> circurlarOneClass = new CircularList<>();
		circurlarOneClass.addAll(oneClassList);
		Date iter = this.getStartDate();
		int result=0;
		OneClass oneClass = circurlarOneClass.getNext();
		iter = Util.addDate(oneClass.getStartDate(), circurlarOneClass.getCircle()*7);
		while(iter.before(date)){
			if(Util.isCounted(iter, holidayDAO, classLogDAO, oneClass, this))
				result++;
			oneClass = circurlarOneClass.getNext();
			iter = Util.addDate(oneClass.getStartDate(), circurlarOneClass.getCircle()*7);
		}
		
		return result;
	}
}
