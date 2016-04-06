package Kakao.kakaoeng;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.dao.BoardDAO;
import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.Board;
import Kakao.kakaoeng.domain.model.Book;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.ClassTimeUsageLog;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Pay;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.RegisterTime;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.TeacherFactory;
import Kakao.kakaoeng.domain.model.UploadedFile;
import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.domain.model.Board.ArticleSort;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;
import Kakao.kakaoeng.domain.model.Lecture.Status;
import Kakao.kakaoeng.domain.model.Purchase.Method;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;
import Kakao.kakaoeng.domain.model.RegisterTime.TimeType;
import Kakao.kakaoeng.domain.model.Student.Level;
import Kakao.kakaoeng.domain.model.Teacher.Quality;
import Kakao.kakaoeng.domain.model.Teacher.Rate;
import Kakao.kakaoeng.domain.model.User.Gender;
import Kakao.kakaoeng.domain.model.User.UserType;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.field.IdField;
import Kakao.kakaoeng.field.PasswordField;

public class ResultSetDataGetterFromDataBase implements DataGetter {

	ResultSet rs = null;
	
	public ResultSetDataGetterFromDataBase(ResultSet rs) {
		this.rs = rs;
	}
	
	@Override
	public <T> T get(String key, Class<T> klass) throws InvalidKeyException {
		try {
			return klass.cast(rs.getObject(key));
		} catch (SQLException e) {
			throw new InvalidKeyException(e);
		}
	}
	
	public List<Field<?>> getFields()
	{
		List<Field<?>> result = new ArrayList<>();
		
		Field<Date> lastLogin = new Field<>("lastLogin", this, Date.class);
		Field<Date> registerTime = new Field<>("registerDate", this, Date.class);
		Field<String> primaryProfile = new Field<>("primaryProfile", this, String.class);
		Field<String> primaryVoice = new Field<>("primaryVoice", this, String.class);
		Field<String> specImage = new Field<>("specImage", this, String.class);
		Field<Boolean> confirm = new Field<>("confirm", this, Boolean.class);
		Field<Rate> rate = new Field<>("rate", Rate.valueOf(this.get("rate", String.class)));
		Field<Boolean> representitve = new Field<>("representitive", this, Boolean.class);
		Field<Integer> salary = new Field<>("salary", this, Integer.class);
		Field<Quality> pronunciation = new Field<>("pronunciation", Quality.valueOf(this.get("pronunciation", String.class)));
		Field<Quality> accent = new Field<>("accent", Quality.valueOf(this.get("accent", String.class)));
		Field<String> comment = new Field<>("comment", this, String.class);
		Field<Boolean> retirement = new Field<>("retirement", this, Boolean.class);
		
		result.add(lastLogin);
		result.add(registerTime);
		result.add(primaryProfile);
		result.add(primaryVoice);
		result.add(specImage);
		result.add(confirm);
		result.add(rate);
		result.add(representitve);
		result.add(salary);
		result.add(pronunciation);
		result.add(accent);
		result.add(comment);
		result.add(retirement);
		
		return result;
	}
	
	@Override
	public boolean getToefl() {
		return this.get("toefl", Boolean.class);
	}

	@Override
	public boolean getIlets() {
		return this.get("ilets", Boolean.class);
	}

	@Override
	public Teacher makeTeacherInstance()
	{
		FieldSet fieldSet = TeacherFactory.makeTeacherFieldSet(this);
		return TeacherFactory.makeInstanceFromFieldSet(fieldSet);
	}

	@Override
	public String getPassword() {
		return this.get("pw", String.class);
	}
	
	@Override
	public String getPasswordConfirm() {
		return this.get("pw",String.class);
	}

	@Override
	public String getBirth() {
		Date date = this.get("birth", Date.class);
		return Util.dateFormatting(date);
	}

	@Override
	public Field<String> getIdField() {
		return new IdField<String>("id", this, String.class);
	}

	@Override
	public PasswordField getPasswordField() {
		PasswordField pw = new PasswordField("pw", this.getPassword());
		String pwConfirm = this.getPasswordConfirm();
		pw.addParam("passwordConfirm", pwConfirm);
		return pw;
	}

	@Override
	public ClassTime makeClassTimeInstance() {
		int id=-1;
		String teacherId = null;
		RegisterTime.DayOfWeek dayOfWeek=null;
		int hour=-1;
		RegisterTime.TimeType time = null;
		int stamp=-1;
		boolean disabled=false;
		try {
			id = rs.getInt(1);
			teacherId = rs.getString(2);
			dayOfWeek = DayOfWeek.valueOf(rs.getString(3));
			hour = rs.getInt(4);
			time = TimeType.valueOf(rs.getString(5));
			stamp = rs.getInt(6);
			disabled = rs.getBoolean(7);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new ClassTime(id, teacherId, new RegisterTime(dayOfWeek, hour, time), stamp, disabled);
	}

	@Override
	public ClassTimeUsageLog makeClassTimeUsageLogInstance() {
		int id = -1;
		int classTimeId = -1;
		String teacherId = null;
		Date startDate = null;
		Date endDate = null;
		try {
			id = rs.getInt(1);
			classTimeId = rs.getInt(2);
			teacherId = rs.getString(3);
			startDate = rs.getDate(4);
			endDate = rs.getDate(5);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return new ClassTimeUsageLog(id, classTimeId, teacherId, startDate, endDate);
	}

	@Override
	public Rate getRate() {
		return Rate.valueOf(this.get("rate", String.class));
		
	}
	
	@Override
	public Lecture makeLectureInstance() {
		int id=-1;
		int purchaseNumber=-1;
		int fullClass=-1;
		Date startDate=null;
		Date endDate=null;
		int weeks=-1;
		String teacherId=null;
		String studentId=null;
		Status status=null;
		String note = null;
		Course course = null;
		String book = null;
		boolean align = false;
		boolean beforeNotified = false;
		boolean afterNotified = false;
		try{
			id = rs.getInt(1);
			teacherId = rs.getString(2);
			studentId = rs.getString(3);
			startDate = rs.getDate(4);
			endDate = rs.getDate(5);
			purchaseNumber = rs.getInt(6);
			fullClass = rs.getInt(7);
			weeks = rs.getInt(8);
			status = Status.valueOf(rs.getString(9));
			note = rs.getString(10);
			course = Course.valueOf(rs.getString(11));
			book = rs.getString(12);
			align = rs.getBoolean(13);
			beforeNotified = rs.getBoolean(14);
			afterNotified = rs.getBoolean(15);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
		return new Lecture(id, purchaseNumber, fullClass, startDate, endDate, weeks, teacherId, studentId, status, note, course, book, align, beforeNotified, afterNotified);
	}
	
	public OneClass makeOneClassInstance(){
		int id=-1;
		int purchaseId=-1;
		String teacherId=null;
		String studentId=null;
		String className=null;
		int d = -1;
		int stamp = -1;
		Duration duration = null;
		int lectureId = -1;
		int parent = -1;
		try{
			id = rs.getInt(1);
			purchaseId = rs.getInt(2);
			teacherId = rs.getString(3);
			studentId = rs.getString(4);
			className = rs.getString(5);
			d = rs.getInt(6);
			stamp = rs.getInt(7);
			duration = new Duration(stamp, d);
			lectureId = rs.getInt(8);
			parent = rs.getInt(9);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
		return new OneClass(id, purchaseId, teacherId, studentId, className, duration, lectureId, parent);
	}
	
	public ClassLog makeClassLogInstance(){
		int id=-1;
		int oneClassId=-1;
		String teacherId = null;
		String studentId = null;
		Date classDate = null;
		ClassState classState = null;
		String reason = null;
		
		try{
			id = rs.getInt(1);
			oneClassId = rs.getInt(2);
			teacherId = rs.getString(3);
			studentId = rs.getString(4);
			classDate = rs.getDate(5);
			classState = ClassState.valueOf(rs.getString(6));
			reason = rs.getString(7);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
		return new ClassLog(id, oneClassId, teacherId, studentId, classDate, classState, reason);
	}
	
	
	public Board makeAdminBoardInstance(){
		int id=-1;
		String title=null;
		String writer=null;
		String contents=null;
		Date date = null;
		int parent=-1;
		String uploadedFile = null;
		List<UploadedFile> fileList = new ArrayList<>();
		int count=0;
		ArticleSort sort =null;
		try{
			id = rs.getInt(1);
			title = rs.getString(2);
			writer = rs.getString(3);
			contents = rs.getString(4);
			date = Util.parseTimeStampDate(rs.getString(5));
			parent = rs.getInt(6);
			uploadedFile = rs.getString(7);
			
			if(uploadedFile != null){
				String[] splitted = uploadedFile.split(BoardDAO.deli);
				for(String fileName : splitted){
					fileList.add(new UploadedFile(fileName));
				}
			}
			
			if(fileList.size() == 1 && fileList.get(0).getDBPath().equals(""))
				fileList.clear();
			count = rs.getInt(8);
			sort = ArticleSort.valueOf(rs.getString(9));
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
		return new Board(id, title, writer, contents, date, parent, fileList, count, sort);
	}
	
	public Admin makeAdminInstance(){
		String id=null;
		String pw=null;
		String name=null;
		String cellPhone=null;
		String address=null;
		UserType userType=null;
		Date lastLogin=null;
		Date registerDate=null;
		try{
			id = rs.getString(1);
			pw = rs.getString(2);
			name = rs.getString(3);
			cellPhone = rs.getString(4);
			address = rs.getString(5);
			userType = UserType.valueOf(rs.getString(6));
			lastLogin = rs.getDate(7);
			registerDate = rs.getDate(8);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
		return new Admin(id, pw, name, cellPhone, address, lastLogin, registerDate, userType);
	}
	
	public Student makeStudentInstance(){
		String id=null;
		String pw=null;
		String name=null;
		String className=null;
		Date birth=null;
		UserType userType=UserType.Student;
		Gender gender=null;
		String ZIP=null;
		String address=null;
		String detailAddress=null;
		String cellPhone=null;
		String homePhone=null;
		Date lastLogin=null;
		Date registerDate=null;
		String parentName=null;
		String parentPhone=null;
		Level level=null;
		String skype=null;
		String note=null;
		int coupon=0;
		String email=null;
		String primaryProfile=null;
		
		try{
			id = rs.getString(1);
			pw = rs.getString(2);
			name = rs.getString(3);
			className = rs.getString(4);
			birth = rs.getDate(5);
			gender = Gender.valueOf(rs.getString(6));
			ZIP = rs.getString(7);
			address = rs.getString(8);
			detailAddress = rs.getString(9);
			cellPhone = rs.getString(10);
			homePhone = rs.getString(11);
			lastLogin = rs.getDate(12);
			registerDate = rs.getDate(13);
			parentName = rs.getString(14);
			parentPhone = rs.getString(15);
			level = Level.valueOf(rs.getString(16));
			skype = rs.getString(17);
			note = rs.getString(18);
			coupon = rs.getInt(19);
			email = rs.getString(20);
			primaryProfile = rs.getString(21);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
		return new Student(id, pw, name, className, birth, userType, gender, ZIP, address, detailAddress, cellPhone, homePhone, primaryProfile, lastLogin, registerDate, parentName, parentPhone, level, skype, note, coupon, email);
	}
	
	public Purchase makePurchaseInstance(){
		int id = -1;
		String studentId = null;
		int price = -1;
		String approvedNumber = null;
		int procrastinate=-1;
		Method method=null;
		boolean confirm = false;
		boolean rejected =false;
		int fullClass=-1;
		Date purchaseDate = null;
		try{
			id = rs.getInt(1);
			studentId = rs.getString(2);
			price = rs.getInt(3);
			approvedNumber = rs.getString(4);
			procrastinate = rs.getInt(5);
			method = Method.valueOf(rs.getString(6));
			confirm = rs.getBoolean(7);
			rejected = rs.getBoolean(8);
			fullClass = rs.getInt(9);
			purchaseDate = rs.getDate(10);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
		return new Purchase(id, studentId, price, approvedNumber,  procrastinate, method, confirm, rejected, fullClass, purchaseDate);
	}
	
	public Book makeBookInstance(){
		int id = -1;
		Course course=null;
		Level level=null;
		String title=null;
		boolean disabled=false;
		String bookLink=null;
		String bookPicture=null;
		try{
			id = rs.getInt(1);
			course = Course.valueOf(rs.getString(2));
			level = Level.valueOf(rs.getString(3));
			title = rs.getString(4);
			disabled = rs.getBoolean(5);
			bookLink = rs.getString(6);
			bookPicture = rs.getString(7);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
		Book book = null;
		try {
			book = new Book(id, course, level, title, disabled, URLDecoder.decode(bookLink, "utf-8"), bookPicture);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		return book;
	}
	
	public Pay makePayInstance(){
		int id = -1;
		String teacherId = null;
		int month = -1;
		boolean payed = false;
		try{
			id = rs.getInt(1);
			teacherId = rs.getString(2);
			month = rs.getInt(3);
			payed = rs.getBoolean(4);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
		return new Pay(id, teacherId, month, payed);
	}
}
