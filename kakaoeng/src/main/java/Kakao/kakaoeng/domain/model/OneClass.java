package Kakao.kakaoeng.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.CircularList;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

public class OneClass {

	public OneClass(int id, int purchaseNumber, String teacherId, String studentId, String className, Duration duration, int lectureId, int parent) {
		super();
		this.id = id;
		this.purchaseNumber = purchaseNumber;
		this.teacherId = teacherId;
		this.studentId = studentId;
		this.className = className;
		this.duration = duration;
		this.lectureId = lectureId;
		this.parent = parent;
		
	}

	int id;
	int purchaseNumber;
	String teacherId;
	String studentId;
	String className;
	Duration duration;
	int done=-1;
	Student student;
	Date startDate;
	int lectureId;
	int parent;
	Lecture lecture;
	int order=-1;
	
	public int getOrder() {
		return order;
	}

	public void loadLecture(LectureDAO lectureDAO){
		lecture = lectureDAO.getLectureByIdTransaction(this.lectureId);
	}
	
	public Lecture getLecture(){
		if(lecture == null)
			throw new IllegalStateException("lecture is not yet loaded.");
		return this.lecture;
	}

	public int getStamp(){
		return this.getDuration().getRt().getStamp();
	}
	
	public int getParent() {
		return parent;
	}

	@Override
	public String toString() {	
		return String.format("%s %s %s", teacherId, studentId, duration.toString());
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDone(){
		if(done == -1)
			throw new RuntimeException("done is not initialized");
		return done;
	}

	public void setDone(List<ClassLog> classLogList){
		done = 0;
		for(ClassLog cl : classLogList){
			if(Util.doneClassStateList.contains(cl.getClassState())){
				done++;
			}
		}
	}
	
	public void loadStudent(StudentDAO studentDAO){
		student = studentDAO.findStudentById(this.studentId);
	}
	
	public Student getStudent() {
		return student;
	}

	public int getLectureId() {
		return lectureId;
	}

	public List<ClassTime> getClassTimeList(){
		List<ClassTime> result = new ArrayList<>();
		List<RegisterTime> registerDate = duration.converToRegisterList();
		for(RegisterTime rt : registerDate){
			ClassTime ct = new ClassTime(-1, teacherId, rt, rt.getStamp(), false);
			result.add(ct);
		}
		
		return result;
	}
	
	public boolean isAfterStartDate(Date baseDate, Lecture lecture){
		Date firstStartDate = this.getFirstStartDate(lecture.getStartDate());
		Date date = this.getClassDate(baseDate);
		if((firstStartDate.equals(date) || firstStartDate.before(date)))
			return true;
		else
			return false;
	}
	
	public Date getFirstStartDate(Date lectureStartDate){
		
		return this.getStartDate();
	}
	
	public String getClassName() {
		return className;
	}

	public int getPurchaseNumber() {
		return purchaseNumber;
	}
	
	public int getId() {
		return id;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public String getStudentId() {
		return studentId;
	}

	public Duration getDuration(){
		return this.duration;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id == ((OneClass)obj).getId();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Integer.hashCode(this.id);
	}

	public String getTeacherStartTimeString(){
		return String.format("%02d:%02d", this.getDuration().getRt().getHour()-1, this.getDuration().getRt().getTime().getMinute());
	}

	public void setStartDate(Lecture lecture){
		Date lectureStartDate = lecture.getStartDate();
		DayOfWeek oneClassStartDayOfWeek = this.duration.getRt().getDayOfWeek();
		List<DayOfWeek> dummyList = new ArrayList<>();
		dummyList.add(oneClassStartDayOfWeek);
		Date startDate = Util.getLatestDateForSpecificDayOfWeek(dummyList, lectureStartDate).get(oneClassStartDayOfWeek);
		this.startDate = startDate;
	}
	
	public Date getStartDate(){
		if(this.startDate == null)
			throw new IllegalStateException("OneClass's startDate is not yet initailized.");
		return this.startDate;
	}
	
	public String getStartDateForm(){
		return Util.dateFormatting(this.getStartDate());
	}
	
	public Date getClassDate(Date baseDate){
		List<Date> dateList = Util.getWeekDateList(baseDate);
		Date classDate = dateList.get(getDuration().getRt().getDayOfWeek().getCustomCalndarCode());
		return classDate;
	}
	
	public int getCustomCalendarCode(){
		return this.getDuration().getRt().getDayOfWeek().getCustomCalndarCode();
	}
	
	public String getKoreanStartTime(){
		int hour = duration.getRt().getHour();
		String prefix = "오전";
		if(hour >= 12){
			prefix = "오후";
			hour -= 12;
		}
		return String.format("%s %d:%02d", prefix, hour, duration.getRt().getTime().getMinute());
		
	}

	public boolean isBeforeEndDate(LectureDAO lectureDAO, Date baseDate, ClassLogDAO classLogDAO, HolidayDAO holidayDAO, OneClassDAO oneClassDAO) {
		
		Date classDate = this.getClassDate(baseDate);
		Lecture lecture = lectureDAO.getLectureByIdTransaction(this.getLectureId());
		List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
		lecture.setEndDate(oneClassList, classLogDAO, holidayDAO);
		if(classDate.before(lecture.getEndDate()) || classDate.equals(lecture.getEndDate()))
			return true;
		else
			return false;
	}

	public void setOrder(Date baseDate, HolidayDAO holidayDAO, ClassLogDAO classLogDAO, OneClassDAO oneClassDAO, LectureDAO lectureDAO) {
		Date classDate = this.getClassDate(baseDate);
		List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(this.getLectureId());
		Lecture lecture = lectureDAO.getLectureByIdTransaction(this.getLectureId());
		for(OneClass oc : oneClassList){
			oc.setStartDate(lecture);
		}
		int i=0;
		for(int j=0; j<oneClassList.size()-1; j++){
			if(oneClassList.get(j).getStartDate().after(oneClassList.get(j+1).getStartDate()))
				break;
			i++;
		}
		if(i == oneClassList.size()-1)
			i=-1;
		int count = lecture.getFullClass();
		CircularList<OneClass> circularOneClassList = new CircularList<>();
		circularOneClassList.addAll(oneClassList);
		circularOneClassList.set(i+1);
		Date date = null;
		while(count>0){
			OneClass oneClass = circularOneClassList.getNext();
			date = Util.addDate(oneClass.getStartDate(), circularOneClassList.getCircle()*7);
			if(!Util.isPro(date, holidayDAO, classLogDAO, oneClass, lecture)){
				count--;
				if(date.equals(classDate)){
					this.order = this.lecture.getFullClass() - count;
					break;
				}
			}
		}
		
	}
	
	
}
