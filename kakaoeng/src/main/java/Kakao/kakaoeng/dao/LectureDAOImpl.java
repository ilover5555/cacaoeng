package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.log4j.pattern.LogEvent;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.DataGetter;
import Kakao.kakaoeng.ResultSetDataGetterFromDataBase;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Book;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Lecture.Status;

public class LectureDAOImpl implements LectureDAO {
	DataSource dataSource;
	Logger logger = Logger.getLogger(LectureDAOImpl.class);
	public static String insertQuery = "insert into lecture (teacherId, studentId,  startDate, endDate, purchaseNumber, fullClass, weeks, note, course, book) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?)";
	public static String selectQuerySpecificRange = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus,"
			+ " note, course, book, align, beforeNotified, afterNotified from lecture where teacherId=? and startDate <= ? and ((endDate is NULL) or endDate >= ?)";
	public static String selectQuerySpecificRangeAndStudentId = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus,"
			+ " note, course, book, align, beforeNotified, afterNotified from lecture where studentId=? and startDate <= ? and ((endDate is NULL) or endDate >= ?)";
	public static String selectQueryRange = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus,"
			+ " note, course, book, align, beforeNotified, afterNotified from lecture where startDate <= ? and ((endDate is NULL) or endDate >= ?)";
	public static String selectQueryByTeacher = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus,"
			+ " note, course, book, align, beforeNotified, afterNotified from lecture where teacherId=?";
	public static String selectQueryByStudentId = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus,"
			+ "note, course, book, align, beforeNotified, afterNotified from lecture where studentId=?";
	public static String selectLevelTestLog = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus,"
			+ "note, course, book, align, beforeNotified, afterNotified from lecture where studentId=? and course='LevelTest' order by id desc";
	public static String selectQueryById = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus, note, course, "
			+ " book, align, beforeNotified, afterNotified from lecture where id=?";
	public static String selectQueryByPurchase = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus , note,"
			+ " course, book, align, beforeNotified, afterNotified from lecture where purchaseNumber=? and lectureStatus='OnGoing'";
	public static String selectQueryAllOnGoing = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus , note,"
			+ " course, book, align, beforeNotified, afterNotified from lecture where lectureStatus='OnGoing'";
	public static String selectQueryAllOnGoingAndNotified = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus , note,"
			+ " course, book, align, beforeNotified, afterNotified from lecture where lectureStatus='OnGoing' and (beforeNotified=? or beforeNotified=false)";
	public static String selectQueryAllDoneAndNotified = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus , note,"
			+ " course, book, align, beforeNotified, afterNotified from lecture where lectureStatus='Done' and (afterNotified=? or beforeNotified=false)";
	public static String selectLevelTestOnGoing= "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus , note,"
			+ " course, book, align, beforeNotified, afterNotified from lecture where lectureStatus='OnGoing' and course='LevelTest'";
	public static String selectLevelTestAll= "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus , note,"
			+ " course, book, align, beforeNotified, afterNotified from lecture where course='LevelTest'";
	public static String selectQueryByPurchaseAll = "select id, teacherId, studentId, startDate, endDate, purchaseNumber, fullClass, weeks, lectureStatus, note, "
			+ "course, book, align, beforeNotified, afterNotified from lecture where purchaseNumber like ?";
	public static String selectQueryByTeacherClassName = "select A.id, A.teacherId, A.studentId, A.startDate, A.endDate, A.purchaseNumber, "
			+ "A.fullClass, A.weeks, A.lectureStatus, A.note, "
			+ "A.course, A.book, A.align, A.beforeNotified, A.afterNotified from lecture as A inner join user as B on A.teacherId = B.id where B.className=?";
	public static String updateNote = "update lecture set note=? where id=?";
	public static String updateLectureState = "update lecture set lectureStatus=? where id=?";
	public static String finishLectureQuery = "update lecture set lectureStatus=?, endDate=now() where id=?";
	public static String finishLectureQueryEndDate = "update lecture set lectureStatus=?, endDate=? where id=?";
	public static String finishLectureInSpecificDateQuery = "update lecture set lectureStatus=?, endDate=? where id=?";
	public static String updateBookQuery = "update lecture set book=?, course=? where id=?";
	public static String updateAlign = "update lecture set align=? where id=?";
	public static String updateBeforeNotified = "update lecture set beforeNotified=? where id=?";
	public static String updateAfterNotified = "update lecture set afterNotified=? where id=?";
	public static String deleteQuery = "delete from lecture where id=?";
	
	@Override
	public void updateAfterNotified(int lectureId, boolean afterNotified){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateAfterNotified);
			ps.setBoolean(1, afterNotified);
			ps.setInt(2, lectureId);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	@Override
	public void updateBeforeNotified(int lectureId, boolean beforeNotified){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateBeforeNotified);
			ps.setBoolean(1, beforeNotified);
			ps.setInt(2, lectureId);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	@Override
	public void deleteByIdTransaction(int id){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(deleteQuery);
			ps.setInt(1, id);

			logger.info(ps.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch(SQLException e) {}
		}
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	
	
	@Override
	public void updateAlign(int lectureId, boolean align){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateAlign);
			ps.setBoolean(1, align);
			ps.setInt(2, lectureId);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	@Override
	public void updateBook(Book book, int lectureId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateBookQuery);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getCourse().name());
			ps.setInt(3, lectureId);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	@Override
	public List<Lecture> getLectureListByTeacherClassName(String className) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByTeacherClassName);
			ps.setString(1, "%"+className+"%");
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public void finishLectureTransaction(int lectureId, Status lectureStatus){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(finishLectureQuery);
			ps.setString(1, lectureStatus.name());
			ps.setInt(2, lectureId);

			logger.info(ps.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {}
		}
	}
	
	@Override
	public void finishLectureTransaction(int lectureId, Status lectureStatus, Date date){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(finishLectureQueryEndDate);
			ps.setString(1, lectureStatus.name());
			ps.setDate(2, new java.sql.Date(date.getTime()));
			ps.setInt(3, lectureId);

			logger.info(ps.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {}
		}
	}
	
	@Override
	public void updateLectureState(int lectureId, Status lectureStatus){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateLectureState);
			ps.setString(1, lectureStatus.name());
			ps.setInt(2, lectureId);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	@Override
	public void updateNote(int lectureId, String newNote){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateNote);
			ps.setString(1, newNote);
			ps.setInt(2, lectureId);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	/* (non-Javadoc)
	 * @see english.dao.LectureDAO#register(english.domain.model.Lecture)
	 */
	@Override
	public void registerTransaction(Lecture lecture){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			java.sql.Date startDateSql = new java.sql.Date(lecture.getStartDate().getTime());
			java.sql.Date endDateSql = null;
			if(lecture.getEndDate() != null)
				endDateSql = new java.sql.Date(lecture.getEndDate().getTime());
			ps.setString(1, lecture.getTeacherId());
			ps.setString(2, lecture.getStudentId());
			ps.setDate(3, startDateSql);
			ps.setDate(4, endDateSql);
			ps.setInt(5, lecture.getPurchaseNumber());
			ps.setInt(6, lecture.getFullClass());
			ps.setInt(7, lecture.getWeeks());
			ps.setString(8, lecture.getNote());
			ps.setString(9, lecture.getCourse().name());
			ps.setString(10, lecture.getBook());

			logger.info(ps.toString());
			ps.executeUpdate();
			
			try(ResultSet generatedKey = ps.getGeneratedKeys()){
				if(generatedKey.next()){
					lecture.setId((int) generatedKey.getLong(1));
				}
				else
				{
					throw new SQLException("Purchase register failed, no ID obtained.");
				}
			}
			
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public List<Lecture> getLectureListByTeacherId(String teacherId) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByTeacher);
			ps.setString(1, teacherId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public List<Lecture> getLectureListByRange(Date firstDate, Date lastDate) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryRange);
			java.sql.Date firstDateSql = new java.sql.Date(firstDate.getTime());
			java.sql.Date lastDateSql = new java.sql.Date(lastDate.getTime());
			ps.setDate(1, firstDateSql);
			ps.setDate(2, lastDateSql);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public List<Lecture> getLectureListByStudentId(String studentId, Date firstDate, Date lastDate) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQuerySpecificRangeAndStudentId);
			java.sql.Date firstDateSql = new java.sql.Date(firstDate.getTime());
			java.sql.Date lastDateSql = new java.sql.Date(lastDate.getTime());
			ps.setString(1, studentId);
			ps.setDate(2, firstDateSql);
			ps.setDate(3, lastDateSql);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public List<Lecture> getLectureListByTeacherId(String teacherId, Date firstDate, Date lastDate) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQuerySpecificRange);
			java.sql.Date firstDateSql = new java.sql.Date(firstDate.getTime());
			java.sql.Date lastDateSql = new java.sql.Date(lastDate.getTime());
			ps.setString(1, teacherId);
			ps.setDate(2, firstDateSql);
			ps.setDate(3, lastDateSql);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public Lecture getLevelTestLogByStudentId(String studentId) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectLevelTestLog);
			ps.setString(1, studentId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
			if(result.size() == 0)
				result.add(null);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result.get(0);
	}
	
	@Override
	public List<Lecture> getLectureListByStudentId(String studentId) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByStudentId);
			ps.setString(1, studentId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public Lecture getLectureByIdTransaction(int lectureId) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryById);
			ps.setInt(1, lectureId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result.get(0);
	}
	
	@Override
	public List<Lecture> getAllLevelTestLectureTransaction() {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectLevelTestAll);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
			
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public List<Lecture> getDoneWithAfterNotifiedLectureListTransaction(boolean afterNotified) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryAllDoneAndNotified);
			ps.setBoolean(1, afterNotified);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
			
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public List<Lecture> getOnGoingWithBeforeNotifiedLectureListTransaction(boolean beforeNotified) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryAllOnGoingAndNotified);
			ps.setBoolean(1, beforeNotified);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
			
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public List<Lecture> getAllOnGoingLectureListTransaction() {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryAllOnGoing);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
			
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public Lecture getOnGoingLectureByPurchase(int purchase) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByPurchase);
			ps.setInt(1, purchase);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
			if(result.size()==0)
				result.add(null);
			
			if(result.size()>1)
				throw new IllegalStateException("getOnGoingLectureByPurchase get duplicate lecture");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result.get(0);
	}
	
	
	
	@Override
	public List<Lecture> getLectureListByPurchase(int purchase) {
		List<Lecture> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByPurchaseAll);
			ps.setInt(1, purchase);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeLectureInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
}
