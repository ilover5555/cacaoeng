package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.DataGetter;
import Kakao.kakaoeng.ResultSetDataGetterFromDataBase;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Teacher.Rate;

public class ClassTimeDAOImpl implements ClassTimeDAO {


	DataSource dataSource = null;
	Logger logger = Logger.getLogger(ClassTimeDAOImpl.class);
	protected static final String insertQuery = "insert into classtime (teacher, dayofweek,"
			+ "hour, time, stamp) VALUES (?,?,?,?,?)"; 
	
	protected static final String findQuery = "select id, teacher, dayofweek, hour, time, stamp, disabled "
			+ "from classtime where teacher=? and disabled=0";
	
	protected static final String selectQuery = "select id, teacher, dayofweek, hour, time, stamp, disabled "
			+ "from classtime where teacher=? and stamp=? and disabled=0";
	
	protected static final String matchQuery = "select classtime.id, classtime.teacher, classtime.dayofweek, classtime.hour, classtime.time, classtime.stamp, classtime.disabled "
			+ "from classtime "
			+ "inner join teacher on classtime.teacher = teacher.id "
			+ "where classtime.stamp >= ? and classtime.stamp <= ? and teacher.rate = ? and classtime.disabled = 0 "
			+ "order by classtime.stamp ASC";
	
	protected static final String findIdQuery = "select id from classtime where teacher = ? and stamp = ?";
	
	protected static final String disableQuery = "update classtime set disabled=1 where id=?";
	protected static final String enableQuery = "update classtime set disabled=0 where id=?";
	public static String deleteQuery = "delete from classtime where id=?";
	
	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
	}
	
	protected int getInnerRange(int range){
		if((range%2)==0)
			return 5*range;
		else
			return 5*range+4;
	}
	
	@Override
	public void delete(int classTimeId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(deleteQuery);
			ps.setInt(1, classTimeId);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch(SQLException e) {}
		}
	}
	
	/* (non-Javadoc)
	 * @see english.dao.ClassTimeDAO#getClassTimeWithinStampInSpecificRate(int, int, int, english.domain.model.Teacher.Rate)
	 */
	@Override
	public List<ClassTime> getClassTimeWithinStampInSpecificRate(int startStamp, int endStamp, int range, Rate rate)
	{
		int innerStartStamp = startStamp-getInnerRange(range);
		int innerEndStamp = endStamp+getInnerRange(range);
		
		List<ClassTime> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(matchQuery);
			ps.setInt(1, innerStartStamp);
			ps.setInt(2, innerEndStamp);
			ps.setString(3, rate.name());
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassTimeInstance());
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
	
	/* (non-Javadoc)
	 * @see english.dao.ClassTimeDAO#getClassTimeListByTeacherIdAndStamp(java.lang.String, int)
	 */
	@Override
	public ClassTime getClassTimeListByTeacherIdAndStamp(String teacherId, int stamp){
		ClassTime result = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQuery);
			ps.setString(1, teacherId);
			ps.setInt(2, stamp);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				if(result != null)
					throw new RuntimeException("Duplicate class Found TeacherId : " + teacherId + " stamp : " + stamp);
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result = dataGetter.makeClassTimeInstance();
			}
			if(result == null)
				throw new RuntimeException("No class Found TeacherId : " + teacherId + " stamp : " + stamp);
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
	
	/* (non-Javadoc)
	 * @see english.dao.ClassTimeDAO#getClassTimeListByTeacherId(java.lang.String)
	 */
	@Override
	public List<ClassTime> getClassTimeListByTeacherId(String teacherId){
		List<ClassTime> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findQuery);
			ps.setString(1, teacherId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassTimeInstance());
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
	
	/* (non-Javadoc)
	 * @see english.dao.ClassTimeDAO#delete(english.domain.model.ClassTime)
	 */
	@Override
	public void disable(ClassTime classTime){
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(disableQuery);
			ps.setInt(1, classTime.getId());
			
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
	public void enable(ClassTime classTime){
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(enableQuery);
			ps.setInt(1, classTime.getId());
			
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
	 * @see english.dao.ClassTimeDAO#register(english.domain.model.ClassTime)
	 */
	@Override
	public void register(ClassTime classTime){
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, classTime.getTeacherId());
			ps.setString(2, classTime.getDayOfWeek().toString());
			ps.setInt(3, classTime.getHour());
			ps.setString(4, classTime.getTimeType().toString());
			ps.setInt(5, classTime.getStampInteger());
			
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
	public int findIdFromClassTimeInstance(ClassTime classTime){
		List<Integer> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findIdQuery);
			ps.setString(1, classTime.getTeacherId());
			ps.setString(2, classTime.getStamp());
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				result.add(rs.getInt(1));
			}
			if(result.size() == 0)
				result.add(-1);
			
			if(result.size() != 1)
				throw new IllegalStateException("Find Duplicate ClassTime From teacherId and stamp : " + classTime.getTeacherId() + ","+classTime.getStamp());
			
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
}
