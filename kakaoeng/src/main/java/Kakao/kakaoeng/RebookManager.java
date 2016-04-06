package Kakao.kakaoeng;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.Lecture.Status;
import Kakao.kakaoeng.domain.model.Purchase.Method;

public class RebookManager {

	@Autowired DataSource dataSource;
	@Autowired BookManager bookManager;
	@Autowired OneClassDAO oneClassDAO;
	@Autowired HolidayDAO holidayDAO;
	@Autowired ClassLogDAO classLogDAO;
	
	public String rebookTransaction(int purchaseNumber, Status status, Lecture lecture, Date date, String id, List<ClassSearchUnit> bookList, Student student, Course course, String book, String method, Purchase purchase, String remotAddr){
		Connection connection = null;
		
		connection = DataSourceUtils.getConnection(dataSource);
		
		String msg = "Successfully rebook";
		try{
			msg += bookManager.cancelTransaction(purchaseNumber, status);
			List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			int passed = lecture.passed(date, oneClassList, holidayDAO, classLogDAO);
			msg += bookManager.bookTransaction(id, bookList, student, bookList.get(0).getWeeks(), date, id, course, book, Method.valueOf(method), purchase, lecture.getFullClass()-passed , remotAddr, null);
			
		}catch(RuntimeException e){
			msg = e.getMessage();
		}
		
		try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {}
		
		return msg;
	}
}
