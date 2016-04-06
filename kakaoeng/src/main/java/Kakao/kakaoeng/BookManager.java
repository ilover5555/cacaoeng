package Kakao.kakaoeng;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.dao.ClassTimeUsageLogDAO;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.Tuition;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;
import Kakao.kakaoeng.domain.model.Lecture.Status;
import Kakao.kakaoeng.domain.model.Purchase.Method;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

public class BookManager {

	public static class bookVO{
		Teacher teacher;
		Lecture lecture;
		Student student;
		List<ClassSearchUnit> list;
		public bookVO(Teacher teacher, Lecture lecture, Student student,
				List<ClassSearchUnit> list) {
			super();
			this.teacher = teacher;
			this.lecture = lecture;
			this.student = student;
			this.list = list;
		}
		
		public String getName(){
			return teacher.getName();
		}
		
		public String getStartDate(){
			return lecture.getStartDateForm();
		}
		
		public int getWeeks(){
			return lecture.getWeeks();
		}
		
		public int getMinutes(){
			int min=0;
			for(ClassSearchUnit csu : list){
				min += csu.getDuration().getDuration();
			}
			return min*25;
		}
		
		public String getStudentClassName(){
			return student.getClassName();
		}
		
		public String getBook(){
			return lecture.getBook();
		}
		
		public String getTime(){
			return list.get(0).getTimeString();
		}
	}
	
	@Autowired ApplicationContext applicationContext;
	@Autowired ClassLogManager classLogManager;
	@Autowired DataSource dataSource;
	@Autowired PurchaseDAO purchaseDAO;
	@Autowired LectureDAO lectureDAO;
	@Autowired OneClassDAO oneClassDAO;
	@Autowired ClassTimeDAO classTimeDAO;
	@Autowired ClassTimeUsageLogDAO classTimeUsageLogDAO;
	@Autowired StudentDAO studentDAO;
	@Autowired EnvironDAO environDAO;
	@Autowired TeacherDAO teacherDAO;
	Logger logger = Logger.getLogger(BookManager.class);

	public String bookLevelTestTransaction(ClassSearchUnit csu, Student student, Date baseDate, String teacherId, String remoteAddr) throws RuntimeException{
		Connection connection = null;
		
		String msg = "레벨테스트 신청이 완료되었습니다.\n감사합니다!!";
		connection = DataSourceUtils.getConnection(dataSource);
		logger.info(remoteAddr+":open levelTest booking Transaction");
		
		student = studentDAO.findStudentById(student.getId());
		
		if(student.getCoupon() <= 0){
			msg = "쿠폰이 모두 소진되었습니다. 관리자에게 추가쿠폰을 요청바랍니다.";
			throw new RuntimeException(msg);
		}
		
		studentDAO.updateCoupon(student.getId(), student.getCoupon()-1);
		
		int fullClass=1;

		logger.info("New Purchase event is invoked.");
		Purchase purchase = new Purchase(student.getId(), 0, 0, Method.Coupon, false, false, fullClass);
		try{
			purchaseDAO.registerTransaction(purchase);
		}catch(RuntimeException e){
			logger.error("error", e);
			msg = "Purchase error. Call Center";
			throw new RuntimeException(msg);
		}
		logger.info("Purchase succesfully registered.");

		List<DayOfWeek> dayOfWeekList = new ArrayList<>();
		dayOfWeekList.add(csu.getDuration().getRt().getDayOfWeek());
		Map<DayOfWeek, Date> findDate = Util.getLatestDateForSpecificDayOfWeek(dayOfWeekList, baseDate);
		
		Date realStartDate = Util.findEariestDate(findDate);
		Lecture lecture = new Lecture(-1, purchase.getId(), fullClass, 
				realStartDate, null, 1, teacherId, student.getId(), Status.OnGoing, "", Course.LevelTest, "LevelTest", false, false, false);
		try{
			lectureDAO.registerTransaction(lecture);
		}
		catch(RuntimeException e){
			logger.error("error", e);
			msg = "Lecture Register error. call center";
			throw new RuntimeException(msg);
		}
		logger.info("New lecture is registered. Lecture : " + lecture.getId());
		List<OneClass> oneClassList = new ArrayList<>();
		OneClass oneClass = new OneClass(-1, purchase.getId(), teacherId, student.getId(), "LevelTest", csu.getDuration(), lecture.getId(), csu.getParent());
		try{
			oneClassDAO.registerTransaction(oneClass);
			if(csu.getParent() != -1)
				oneClassDAO.setParentForNewOneClassTransaction(oneClass.getId(),csu.getParent());
			else
				oneClassDAO.setParentForNewOneClassTransaction(oneClass.getId(),oneClass.getId());
		}
		catch(RuntimeException e){
			logger.error("error", e);
			msg = "register One Class Error call Center";
			throw new RuntimeException(msg);
		}
		oneClassList.add(oneClass);
		try{
			List<ClassSearchUnit> bookList = new ArrayList<>();
			ClassSearchUnit r = new ClassSearchUnit(csu.getDuration(), lecture.getStartDate(), 1);
			bookList.add(r);
			classTimeUsageLogDAO.bookInterface(teacherId, bookList, classTimeDAO, classTimeUsageLogDAO);
		}catch(RuntimeException e){
			e.printStackTrace();
			msg = "Request time is already booked.";
			throw new RuntimeException(msg);
		}

		classLogManager.registerClassLogTransaction(oneClass.getId(), lecture.getStartDate(), ClassState.LevelTestReserved, "");
		
		try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {}
		
		
		
		return msg;
	}
	
	public String bookTransaction(String id, List<ClassSearchUnit> bookList, Student student, 
			int weeks, Date baseDate, String teacherId, Course course, String bookName, Method method, Purchase original, int fc, String remoteAddr, Tuition t) throws RuntimeException{
		Connection connection = null;
		
		String msg = "수강신청이 완료되었습니다.\n감사합니다!!";
		connection = DataSourceUtils.getConnection(dataSource);
		logger.info(remoteAddr+":open booking Transaction");
		int fullClass=0;
		for(ClassSearchUnit csu : bookList){
			fullClass += csu.getWeeks();
		}
		Purchase purchase = null;
		if(original == null){
			logger.info("New Purchase event is invoked.");
			if(method.equals(Method.Credit) || method.equals(Method.CellPhone))
				purchase = new Purchase(student.getId(), t.getPurchasePrice(), (int)(fullClass/8), method, false, false, fullClass);
			else
				purchase = new Purchase(-1, student.getId(), t.getPurchasePrice(), Purchase.makeWaitNumber(), (int)(fullClass/8), method, false, false, fullClass);
			try{
				purchaseDAO.registerTransaction(purchase);
			}catch(RuntimeException e){
				logger.error("error", e);
				msg = "Purchase error. Call Center";
				throw new RuntimeException(msg);
			}
			logger.info("Purchase succesfully registered.");
		}else{
			logger.info("Rebooking event is invoked.\nPurchase : "+original.getId());
			purchase = original;
			fullClass = fc;
		}
		List<DayOfWeek> dayOfWeekList = new ArrayList<>();
		for(ClassSearchUnit csu : bookList){
			dayOfWeekList.add(csu.getDuration().getRt().getDayOfWeek());
		}
		Map<DayOfWeek, Date> findDate = Util.getLatestDateForSpecificDayOfWeek(dayOfWeekList, baseDate);
		
		Date realStartDate = Util.findEariestDate(findDate);
		Lecture lecture = new Lecture(-1, purchase.getId(), fullClass, 
				realStartDate, null, weeks, teacherId, student.getId(), Status.OnGoing, "", course, bookName, false, false, false);
		try{
			lectureDAO.registerTransaction(lecture);
		}
		catch(RuntimeException e){
			logger.error("error", e);
			msg = "Lecture Register error. call center";
			throw new RuntimeException(msg);
		}
		logger.info("New lecture is registered. Lecture : " + lecture.getId());
		List<OneClass> oneClassList = new ArrayList<>();
		for(ClassSearchUnit csu : bookList){
			OneClass oneClass = new OneClass(-1, purchase.getId(), teacherId, student.getId(), bookName, csu.getDuration(), lecture.getId(), csu.getParent());
			try{
				oneClassDAO.registerTransaction(oneClass);
				if(csu.getParent() != -1)
					oneClassDAO.setParentForNewOneClassTransaction(oneClass.getId(),csu.getParent());
				else
					oneClassDAO.setParentForNewOneClassTransaction(oneClass.getId(),oneClass.getId());
			}
			catch(RuntimeException e){
				logger.error("error", e);
				msg = "register One Class Error call Center";
				throw new RuntimeException(msg);
			}
			oneClassList.add(oneClass);
		}
		try{
			classTimeUsageLogDAO.bookInterface(id, bookList, classTimeDAO, classTimeUsageLogDAO);
		}catch(RuntimeException e){
			e.printStackTrace();
			msg = "Request time is already booked.";
			throw new RuntimeException(msg);
		}

		try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {}
		
		String mode = environDAO.getNewLectureSMSMode();
		if(mode.equals("auto")){
			lecture.loadStudent(studentDAO);
			lecture.loadTeacher(teacherDAO);
			String smsMsg = Util.fillContent(environDAO.getNewLectureSMSMessage(), lecture);
			SMS.sms(student.getCellPhone(), smsMsg);
		}
		mode = environDAO.getLectureRegisteredMailMode();
		if(mode.equals("auto")){
			Teacher teacher = teacherDAO.getTeacherWithId(id);
			bookVO vo = new bookVO(teacher, lecture, student, bookList);
			String mailMsg = Util.fillContent(environDAO.getLectureRegisteredMailMessage(), vo);
			Mail.send(Mail.username, id, environDAO.getLectureRegisteredMailSubject(), mailMsg);
		}
		
		return msg;
	}
	
	public String cancelTransaction(int purchaseNumber, Status lectureStatus){
		Connection connection = null;
		
		String msg = "Congratulations! Your cancel submitted.";
		connection = DataSourceUtils.getConnection(dataSource);
		
		Lecture lecture = null;
		try{
			lecture = lectureDAO.getOnGoingLectureByPurchase(purchaseNumber);
		}catch(RuntimeException e){
			e.printStackTrace();
			msg="getOnGoingLectureByPurchase exeception, purchaseNumber : " + purchaseNumber + "\nerrorMsg : " + e.getMessage();
			throw new RuntimeException(msg);
		}
		
		if(lecture == null){
			try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {}
			return "Purcase " + purchaseNumber + " is already done.\nOr no onGoing lecture for this purchase.";
		}
		
		/*
		Lecture newLecture = new Lecture(-1, lecture.getPurchaseNumber(), lecture.getFullClass(),
				lecture.getStartDate(), null, lecture.getWeeks(), lecture.getTeacherId(), 
				lecture.getStudentId(), Status.OnGoing, lecture.getNote(), lecture.getCourse(), lecture.getBook());
				*/
		try{
			lectureDAO.finishLectureTransaction(lecture.getId(), lectureStatus);
		}catch(RuntimeException e){
			e.printStackTrace();
			msg = "finishLectureTransaction Exeception , lectureId : " + lecture.getId() + ", lectureStatus : " + lectureStatus + "\nerrorMsg : " + e.getMessage();
			throw new RuntimeException(msg);
		}
		
		List<OneClass> oneClassList = null;
		try{
			oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
		}catch(RuntimeException e){
			e.printStackTrace();
			msg = "getRootOneClssListGroupedByPurchaseNumber error, purchaseNumber : " + purchaseNumber +"\nerrorMsg : " + e.getMessage();
			throw new RuntimeException(e);
		}
		int id = -1;
		for(OneClass oneClass : oneClassList){
			List<ClassTime> classTimeList = oneClass.getClassTimeList();
			for(ClassTime classTime : classTimeList){
				try{
					id = classTimeDAO.findIdFromClassTimeInstance(classTime);
					classTimeUsageLogDAO.deleteByClassTimeIdTransaction(id);
				}catch(RuntimeException e){
					e.printStackTrace();
					msg = "deleteByClassTimeIdTransaction error, classTime : " + classTime.getId() + "\nerrorMsg : " +e.getMessage();
					throw new RuntimeException(msg);
				}
			}
		}
		
		
		
		try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {}
		
		return msg;
	}
}
