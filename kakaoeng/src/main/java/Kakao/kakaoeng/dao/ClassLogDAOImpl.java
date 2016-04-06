package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.ResultSetDataGetterFromDataBase;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;

public class ClassLogDAOImpl implements ClassLogDAO {
	DataSource dataSource;
	Logger logger = Logger.getLogger(ClassLogDAOImpl.class);
	public static String insertQuery = "insert into classlog (oneClassId, teacherId, studentId, classDate, classState, reason)"
			+ " VALUES (?, ?, ?, ?, ?, ?)";
	public static String selectQueryByRange = "select id, oneClassId, teacherId, studentId, classDate, classState, reason "
			+ "from classlog where classDate >= ? and classDate <= ?";
	public static String selectQueryByOneClass = "select id, oneClassId, teacherId, studentId, classDate, classState, reason "
			+ "from classlog where oneClassId=? and classDate >= ? and classDate <= ?";
	public static String selectQueryByParent = "select A.id, A.oneClassId, A.teacherId, A.studentId, A.classDate, A.classState, A.reason "
			+ "from classlog as A inner join oneClass as B on A.oneClassId = B.id where B.parent=? and A.classDate >= ? and A.classDate <= ?";
	public static String selectQueryByOneClassAll = "select id, oneClassId, teacherId, studentId, classDate, classState, reason "
			+ "from classlog where oneClassId=?";
	public static String selectQuery = "select id, oneClassId, teacherId, studentId, classDate, classState, reason "
			+ "from classlog where id=?";
	public static String selectQueryByOneClassParentAll = "select A.id, A.oneClassId, A.teacherId, A.studentId, A.classDate, A.classState, A.reason "
			+ "from classlog as A inner join oneclass as B on A.oneClassId = B.id where B.parent = ?";
	public static String selectQueryByOneClassParentUntilLecture = "select A.id, A.oneClassId, A.teacherId, A.studentId, A.classDate, A.classState, A.reason "
			+ "from classlog as A inner join oneclass as B on A.oneClassId = B.id where B.parent = ? and B.lectureId <= ?";
	public static String selectQueryByLectureIdAll = "select A.id, A.oneClassId, A.teacherId, A.studentId, A.classDate, A.classState, A.reason "
			+ "from classlog as A inner join oneclass as B on A.oneClassId = B.id where B.lectureId = ?";
	public static String selectQueryByOneClassPurchaseAll = "select A.id, A.oneClassId, A.teacherId, A.studentId, A.classDate, A.classState, A.reason "
			+ "from classlog as A inner join oneclass as B on A.oneClassId = B.id where B.purchaseNumber = ?";
	public static String updateQuery = "update classlog set classState=?, reason=? where id=?";
	public static String deleteQuery = "delete from classlog where id=?";
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	
	@Override
	public void deleteByClassLogIdTransaction(int classLogId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(deleteQuery);
			ps.setInt(1, classLogId);

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
	
	/* (non-Javadoc)
	 * @see english.dao.ClassLogDAO#register(english.domain.model.ClassLog)
	 */
	@Override
	public void registerTransaction(ClassLog classLog){
		
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(insertQuery);
			ps.setInt(1, classLog.getOneClassId());
			ps.setString(2, classLog.getTeacherId());
			ps.setString(3, classLog.getStudentId());
			ps.setDate(4, new java.sql.Date(classLog.getClassDate().getTime()));
			ps.setString(5, classLog.getClassState().name());
			ps.setString(6, classLog.getReason());

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
	
	@Override
	public void updateTransaction(int id, ClassState state, String reason){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(updateQuery);
			ps.setString(1, state.name());
			ps.setString(2, reason);
			ps.setInt(3, id);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
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
	public List<ClassLog> getClassLogListByParent(int parent, Date firstDate, Date lastDate) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByParent);
			java.sql.Date firstDateSql = new java.sql.Date(firstDate.getTime());
			java.sql.Date lastDateSql = new java.sql.Date(lastDate.getTime());
			ps.setInt(1, parent);
			ps.setDate(2, firstDateSql);
			ps.setDate(3, lastDateSql);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
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
	public List<ClassLog> getClassLogListByRangeTransaction(Date firstDate, Date lastDate) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryByRange);
			java.sql.Date firstDateSql = new java.sql.Date(firstDate.getTime());
			java.sql.Date lastDateSql = new java.sql.Date(lastDate.getTime());
			ps.setDate(1, firstDateSql);
			ps.setDate(2, lastDateSql);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
			}
		} catch (SQLException e) {
			logger.error("error", e);
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
	public ClassLog getClassLogByOneClassTransaction(int oneClassId, Date date) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryByOneClass);
			java.sql.Date firstDateSql = new java.sql.Date(date.getTime());
			java.sql.Date lastDateSql = new java.sql.Date(date.getTime());
			ps.setInt(1, oneClassId);
			ps.setDate(2, firstDateSql);
			ps.setDate(3, lastDateSql);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
			}
			if(result.size()==0)
				result.add(null);
			if(result.size()>1)
				throw new IllegalStateException("getClassLogByOneClassTransaction get duplicate ClassLog - oneClassId : " + oneClassId+",date:"+date);
		} catch (SQLException e) {
			logger.error("error", e);
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
	public List<ClassLog> getClassLogListByOneClassTransaction(int oneClassId, Date firstDate, Date lastDate) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryByOneClass);
			java.sql.Date firstDateSql = new java.sql.Date(firstDate.getTime());
			java.sql.Date lastDateSql = new java.sql.Date(lastDate.getTime());
			ps.setInt(1, oneClassId);
			ps.setDate(2, firstDateSql);
			ps.setDate(3, lastDateSql);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
			}
		} catch (SQLException e) {
			logger.error("error", e);
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
	public ClassLog getByIdTransaction(int classLogId) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQuery);
			ps.setInt(1, classLogId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
			}
			
			if(result.size() == 0)
				result.add(null);
		} catch (SQLException e) {
			logger.error("error", e);
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
	public List<ClassLog> getAllClassLogListByOneClassTransaction(int oneClassId) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryByOneClassAll);
			ps.setInt(1, oneClassId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
			}
		} catch (SQLException e) {
			logger.error("error", e);
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
	public List<ClassLog> getAllClassLogListByParent(int parent) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByOneClassParentAll);
			ps.setInt(1, parent);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
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
	public List<ClassLog> getAllClassLogListByParentUntilLecture(int parent, int lectureId) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByOneClassParentUntilLecture);
			ps.setInt(1, parent);
			ps.setInt(2, lectureId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
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
	public List<ClassLog> getAllClassLogListByLectureId(int lectureId) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByLectureIdAll);
			ps.setInt(1, lectureId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
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
	public List<ClassLog> getAllClassLogListByPurchase(int purchaseNumber) {
		List<ClassLog> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryByOneClassPurchaseAll);
			ps.setInt(1, purchaseNumber);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeClassLogInstance());
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
}
