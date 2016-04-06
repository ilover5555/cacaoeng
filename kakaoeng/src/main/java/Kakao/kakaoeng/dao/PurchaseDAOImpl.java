package Kakao.kakaoeng.dao;

import java.security.KeyStore.LoadStoreParameter;
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
import Kakao.kakaoeng.domain.model.Purchase;

public class PurchaseDAOImpl implements PurchaseDAO {
	DataSource dataSource;
	Logger logger = Logger.getLogger(PurchaseDAOImpl.class);
	public static String insertQuery = "insert into purchase (studentId, price, approvedNumber, procrastinate, method, fullClass )"
			+ " VALUES (?, ?, ?, ?, ?, ?)";
	public static String selectQueryByPurchaseNumber = "select id, studentId, price, approvedNumber, procrastinate, method, confirm, rejected, fullClass, purchaseDate from purchase "
			+ "where id = ? and rejected=0";
	public static String selectQueryWhichUncofirmed = "select id, studentId, price, approvedNumber, procrastinate, method, confirm, rejected, fullClass, purchaseDate from purchase "
			+ "where confirm=0 and rejected=0";
	public static String selectQueryNotRejected = "select id, studentId, price, approvedNumber, procrastinate, method, confirm, rejected, fullClass, purchaseDate from purchase "
			+ "where rejected=0";
	public static String selectQueryWithStudentId = "select id, studentId, price, approvedNumber, procrastinate, method, confirm, rejected, fullClass, purchaseDate from purchase "
			+ "where rejected=0 and studentId=?";
	public static String selectQueryWithTeacherClassName = "select A.id, A.studentId, A.price, A.approvedNumber, A.procrastinate, A.method, A.confirm, A.rejected, A.fullClass, A.purchaseDate "
			+ "from purchase as A inner join lecture as B on A.id = B.purchaseNumber inner join teacher as C on B.teacherId = C.id where C.className like ? and A.rejected=0";
	public static String selectQueryWithStudentrClassName = "select A.id, A.studentId, A.price, A.approvedNumber, A.procrastinate, A.method, A.confirm, A.rejected, A.fullClass, A.purchaseDate "
			+ "from purchase as A inner join lecture as B on A.id = B.purchaseNumber inner join student as C on B.studentId = C.id where C.className like ? and A.rejected=0";
	public static String selectQueryWithBook = "select A.id, A.studentId, A.price, A.approvedNumber, A.procrastinate, A.method, A.confirm, A.rejected, A.fullClass, A.purchaseDate "
			+ "from purchase as A inner join lecture as B on A.id = B.purchaseNumber where B.book like ?";
	public static String updateProcrastinateQuery = "update purchase set procrastinate=? where id=?";
	public static String updateApprovedNumberQuery = "update purchase set approvedNumber=? where id=?";
	public static String updateConfirmQuery = "update purchase set confirm=? where id=?";
	public static String updateRejectedQuery = "update purchase set rejected=? where id=?";
	public static String deleteQuery = "delete from purchase where id=?";
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
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
	
	@Override
	public List<Purchase> getPurchaseListWithBook(String book) {
		List<Purchase> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryWithBook);
			ps.setString(1, "%"+book+"%");
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePurchaseInstance());
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
	public List<Purchase> getPurchaseListWithStudentClassName(String studentClassName) {
		List<Purchase> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryWithStudentrClassName);
			ps.setString(1, "%"+studentClassName+"%");
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePurchaseInstance());
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
	public List<Purchase> getPurchaseListWithTeacherClassName(String teacherClassName) {
		List<Purchase> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryWithTeacherClassName);
			ps.setString(1, "%"+teacherClassName+"%");
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePurchaseInstance());
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
	
	/* (non-Javadoc)
	 * @see english.dao.PurchaseDAO#register(english.domain.model.Purchase)
	 */
	@Override
	public void registerTransaction(Purchase purchase){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, purchase.getStudentId());
			ps.setInt(2, purchase.getPrice());
			ps.setString(3, purchase.getApprovedNumber());
			ps.setInt(4, purchase.getProcrastinate());
			ps.setString(5, purchase.getMethod().name());
			ps.setInt(6, purchase.getFullClass());

			logger.info(ps.toString());
			ps.executeUpdate();
			try(ResultSet generatedKey = ps.getGeneratedKeys()){
				if(generatedKey.next()){
					purchase.setId((int) generatedKey.getLong(1));
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
	public Purchase getPurchaseById(int id) {
		List<Purchase> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByPurchaseNumber);
			ps.setInt(1, id);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePurchaseInstance());
			}
			if(result.size() == 0)
				result.add(null);
			if(result.size() > 1)
				throw new IllegalStateException("Purchase is duplicated");
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
	public List<Purchase> getPurchaseListNotRejected() {
		List<Purchase> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryNotRejected);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePurchaseInstance());
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
	public List<Purchase> getPurchaseListWithStudentId(String studentId) {
		List<Purchase> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryWithStudentId);
			ps.setString(1, studentId);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePurchaseInstance());
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
	public List<Purchase> getPurchaseListWhichIsUnconfirm() {
		List<Purchase> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryWhichUncofirmed);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePurchaseInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public void updateProcrastinateTransaction(int id, int procrastinate){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(updateProcrastinateQuery);
			ps.setInt(1, procrastinate);
			ps.setInt(2, id);

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
	public void updateApprovedNumber(int id){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateProcrastinateQuery);
			String approvedNumber =Purchase.makeApprovedNumber(); 
			ps.setString(1, approvedNumber);
			ps.setInt(2, id);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void updateConfirm(int id, boolean confirm){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateConfirmQuery);
			ps.setBoolean(1, confirm);
			ps.setInt(2, id);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void updateRejected(int id, boolean rejected){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateRejectedQuery);
			ps.setBoolean(1, rejected);
			ps.setInt(2, id);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
