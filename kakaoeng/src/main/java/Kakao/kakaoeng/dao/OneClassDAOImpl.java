package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.ResultSetDataGetterFromDataBase;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

public class OneClassDAOImpl implements OneClassDAO {
	Logger logger = Logger.getLogger(OneClassDAOImpl.class);
	DataSource dataSource;
	public static String insertQuery = "insert into OneClass (purchaseNumber, teacherId, studentId, className, duration, stamp, lectureId)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
	public static String setParentQuery = "update oneclass set parent=? where id=?";
	public static String selectQueryByPurchaseNumber = "select id, purchaseNumber, teacherId, studentId, className, duration, stamp, lectureId, parent "
			+ "from oneclass where purchaseNumber=?";
	public static String selectRootQueryByPurchaseNumber = "select id, purchaseNumber, teacherId, studentId, className, duration, stamp, lectureId, parent "
			+ "from oneclass where purchaseNumber=? and parent=id";
	public static String selectQueryByParent = "select id, purchaseNumber, teacherId, studentId, className, duration, stamp, lectureId, parent "
			+ "from oneclass where parent=?";
	public static String selectQueryById =  "select id, purchaseNumber, teacherId, studentId, className, duration, stamp, lectureId, parent "
			+ "from oneclass where id=?";
	public static String selectQueryByLectureId = "select id, purchaseNumber, teacherId, studentId, className, duration, stamp, lectureId, parent "
			+ "from oneclass where lectureId=?";
	public static String selectRootQueryByLectureId = "select id, purchaseNumber, teacherId, studentId, className, duration, stamp, lectureId, parent "
			+ "from oneclass where lectureId=? and id=parent";
	public static String selectQueryByLectureIdAfterExcludingDayOfWeek = "select id, purchaseNumber, teacherId, studentId, className, duration, stamp, "
			+ "lectureId, parent "
			+ "from oneclass where lectureId=? and ? <= stamp";
	public static String selectQueryByDayOfWeek = "select id, purchaseNumber, teacherId, studentId, className, duration, stamp, "
			+ "lectureId, parent "
			+ "from oneclass where ? <= stamp and stamp < ? and lectureId = ?";
	public static String deleteQuery = "delete from oneclass where id=?";
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
	public List<OneClass> getOneClassListByDayOfWeek(int lectureId, DayOfWeek dayOfWeek) {
		List<OneClass> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByDayOfWeek);
			ps.setInt(1, dayOfWeek.getCode()*1000);
			ps.setInt(2, (dayOfWeek.getCode()+1)*1000);
			ps.setInt(3, lectureId);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeOneClassInstance());
			}
			
		} catch (SQLException e) {
			logger.error("error", e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public List<OneClass> getOneClassListGroupedByLectureIdAfterExcludingDayOfWeek(int lectureId, DayOfWeek dayOfWeek) {
		List<OneClass> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByLectureIdAfterExcludingDayOfWeek);
			ps.setInt(1, lectureId);
			ps.setInt(2, (dayOfWeek.getCode()+1)*1000);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeOneClassInstance());
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
	public void setParentForNewOneClassTransaction(int oneClassId, int parent){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(setParentQuery);
			ps.setInt(1, parent);
			ps.setInt(2, oneClassId);

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
	public OneClass getOneClassByIdTransaction(int oneClassId) {
		List<OneClass> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryById);
			ps.setInt(1, oneClassId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeOneClassInstance());
			}
			if(result.size()==0)
				result.add(null);
			if(result.size() > 1)
				throw new IllegalStateException("oneClassId is duplicated");
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
				logger.error("error", e);
			}
		}
		return result.get(0);
	}
	
	/* (non-Javadoc)
	 * @see english.dao.OneClassDAO#register(english.domain.model.OneClass)
	 */
	@Override
	public void registerTransaction(OneClass oneClass){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, oneClass.getPurchaseNumber());
			ps.setString(2, oneClass.getTeacherId());
			ps.setString(3, oneClass.getStudentId());
			ps.setString(4, oneClass.getClassName());
			ps.setInt(5, oneClass.getDuration().getDuration());
			ps.setInt(6, oneClass.getDuration().getRt().getStamp());
			ps.setInt(7, oneClass.getLectureId());

			logger.info(ps.toString());
			ps.executeUpdate();
			try(ResultSet generatedKey = ps.getGeneratedKeys()){
				if(generatedKey.next()){
					oneClass.setId((int) generatedKey.getLong(1));
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
	public List<OneClass> getOneClassListGroupedByParentTransaction(int parent) {
		List<OneClass> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryByParent);
			ps.setInt(1, parent);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeOneClassInstance());
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
	public List<OneClass> getOneClassListGroupedByPurchaseNumber(int PurchaseNumber) {
		List<OneClass> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByPurchaseNumber);
			ps.setInt(1, PurchaseNumber);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeOneClassInstance());
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
	public List<OneClass> getRootOneClassListGroupedByPurchaseNumber(int PurchaseNumber) {
		List<OneClass> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectRootQueryByPurchaseNumber);
			ps.setInt(1, PurchaseNumber);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeOneClassInstance());
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
	public List<OneClass> getOneClassListGroupedByLectureIdTransaction(int lectureId) {
		List<OneClass> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(selectQueryByLectureId);
			ps.setInt(1, lectureId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeOneClassInstance());
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
