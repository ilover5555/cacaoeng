package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.ClassSearchUnit;
import Kakao.kakaoeng.ResultSetDataGetterFromDataBase;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.ClassTimeUsageLog;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.RegisterTime.TimeType;

public class ClassTimeUsageLogDAOImpl implements ClassTimeUsageLogDAO {
	DataSource dataSource = null;
	Logger logger = Logger.getLogger(ClassTimeUsageLogDAOImpl.class);

	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
	}

	public static String selectQuery = "select id, classTimeId, tacherId, startDate, endDate from classtimeusagelog where teacherId = ? and classTimeId=?";
	public static String selectRangeQuery = "select id, classTimeId, teacherId, startDate, endDate from classtimeusagelog "
			+ "where classTimeId = ? and (endDate >= ? and startDate <= ?)";
	public static String checkQuery = "select count(*) from classtimeusagelog where classTimeId = ? and (endDate >= ? and startDate <= ?)";
	public static String bookQuery = "insert into classtimeusagelog (classTimeId, teacherId, startDate, endDate) VALUES (?, ?, ?, ?)";
	public static String procrastinate = "update classtimeusagelog set endDate=ADDDATE(endDate,?) where id = ?";
	public static String deleteQuery = "delete from classtimeusagelog where id=?";
	public static String deleteQueryByClassTimeId = "delete from classtimeusagelog where classTimeId=?";
	
	@Override
	public ClassTimeUsageLog selectClassTimeUsageLog(int classTimeId, String teacherId) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ClassTimeUsageLog> result = new ArrayList<>();
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectRangeQuery);
			ps.setString(1, teacherId);
			ps.setInt(2, classTimeId);

			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			if (rs.next()) {
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassTimeUsageLogInstance());
			}
			if(result.size() == 0)
				result.add(null);
			if(result.size() > 1)
				throw new IllegalStateException("find duplicate ClassTimeUsageLog in selectClassTimeUsageLog");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {e.printStackTrace();}
		}
		return result.get(0);
	}
	
	@SuppressWarnings("serial")
	public class ConcurrentBookException extends RuntimeException {
		public ConcurrentBookException() {
			super();
		}

		public ConcurrentBookException(String msg) {
			super(msg);
		}

		public ConcurrentBookException(Exception e) {
			super(e);
		}
	}

	@Override
	public void deleteByClassTimeIdTransaction(int classTimeId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(deleteQueryByClassTimeId);
			ps.setInt(1, classTimeId);

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
	
	public void bookInterface(String teacherId, List<ClassSearchUnit> a, ClassTimeDAO classTimeDAO, ClassTimeUsageLogDAO classTimeUsageLogDAO) {
		Connection connection = null;
		connection = DataSourceUtils.getConnection(dataSource);
		for (ClassSearchUnit item : a)
			classTimeUsageLogDAO.bookOneClassInterface(teacherId, item.getDuration(), item.getWeeks(), item.getStartDate(), classTimeDAO, classTimeUsageLogDAO);
		try {
			DataSourceUtils.doReleaseConnection(connection, dataSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void bookOneClassInterface(String teacherId, Duration duration, int weeks, Date startDate,
		ClassTimeDAO classTimeDAO, ClassTimeUsageLogDAO classTimeUsageLogDAO) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.WEEK_OF_YEAR, weeks - 1);
		Date endDate = cal.getTime();
		List<ClassTime> booked = new ArrayList<>();
		int curStamp = duration.getRt().getStamp();
		for (int i = 0; i < duration.getDuration(); i++) {
			ClassTime classTime = classTimeDAO.getClassTimeListByTeacherIdAndStamp(teacherId, curStamp);
			curStamp = TimeType.getNextStamp(curStamp);
			booked.add(classTime);
		}
		classTimeUsageLogDAO.book(booked, teacherId, startDate, endDate, classTimeUsageLogDAO);

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * english.dao.ClassTimeUsageLogDAO#bookOneClassTime(english.domain.model.
	 * ClassTime, java.lang.String, java.util.Date, java.util.Date,
	 * java.sql.Connection)
	 */
	public void bookOneClassTime(ClassTime element, String teacherId, Date startDate, Date endDate) {
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(bookQuery);
			java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
			java.sql.Date endDateSql = new java.sql.Date(endDate.getTime());
			ps.setInt(1, element.getId());
			ps.setString(2, element.getTeacherId());
			ps.setDate(3, startDateSql);
			ps.setDate(4, endDateSql);

			logger.info(ps.toString());
			ps.executeUpdate();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see english.dao.ClassTimeUsageLogDAO#bookCheck(java.util.List,
	 * java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public void bookCheck(List<ClassTime> booked, String teacherId, Date startDate, Date endDate, ClassTimeUsageLogDAO classTimeUsageLogDAO) {
		for (ClassTime ct : booked) {
			if (!classTimeUsageLogDAO.checkClassTimeCanBeUsedStateTransaction(ct.getId(), startDate, endDate))
				throw new ConcurrentBookException(
						"Already booked Class Time required to be booked. : " + booked + " " + teacherId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see english.dao.ClassTimeUsageLogDAO#book(java.util.List,
	 * java.lang.String, java.util.Date, java.util.Date, java.sql.Connection)
	 */
	@Override
	public void book(List<ClassTime> booked, String teacherId, Date startDate, Date endDate, ClassTimeUsageLogDAO classTimeUsageLogDAO) {
		classTimeUsageLogDAO.bookCheck(booked, teacherId, startDate, endDate, classTimeUsageLogDAO);
		for (ClassTime ct : booked) {
			classTimeUsageLogDAO.bookOneClassTime(ct, teacherId, startDate, endDate);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see english.dao.ClassTimeUsageLogDAO#checkClassTimeCanBeUsedState(int,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public boolean checkClassTimeCanBeUsedStateTransaction(int classTimeId, Date startDate, Date endDate) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(checkQuery);
			java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
			java.sql.Date endDateSql = new java.sql.Date(endDate.getTime());
			ps.setInt(1, classTimeId);
			ps.setDate(2, startDateSql);
			ps.setDate(3, endDateSql);

			logger.info(ps.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) == 0)
					return true;
			}

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				ps.close();
			} catch (SQLException e) {
			}
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Override
	public ClassTimeUsageLog selectRangeTransaction(int classTimeId, Date startDate, Date endDate) {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ClassTimeUsageLog> result = new ArrayList<>();
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectRangeQuery);
			java.sql.Date startDateSql = new java.sql.Date(startDate.getTime());
			java.sql.Date endDateSql = new java.sql.Date(endDate.getTime());
			ps.setInt(1, classTimeId);
			ps.setDate(2, startDateSql);
			ps.setDate(3, endDateSql);

			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			if (rs.next()) {
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassTimeUsageLogInstance());
			}
			if(result.size() == 0)
				result.add(null);
			if(result.size() > 1)
				throw new IllegalStateException("selectRangeTransaction find duplicate ClassTimeUsageLog - classTimeId:" + classTimeId + ",startDate:" + startDate + ",endDate:" + endDate);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {e.printStackTrace();}
		}
		return result.get(0);
	}

	@Override
	public void procrastinateByIdTransaction(int classTimeUsageLogId, int weeks){
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(procrastinate);
			ps.setInt(1,weeks*7);
			ps.setInt(2, classTimeUsageLogId);
			
			logger.info(ps.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {ps.close();} catch (SQLException e) {}
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
			}
		}
	}
	
	@Override
	public void deleteByClassTimeUsageLogIdTransaction(int CTULId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(deleteQuery);
			ps.setInt(1, CTULId);

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
}
