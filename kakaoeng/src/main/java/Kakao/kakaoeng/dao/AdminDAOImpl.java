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

import Kakao.kakaoeng.ResultSetDataGetterFromDataBase;
import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.Book;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.Student.Level;

public class AdminDAOImpl implements AdminDAO {

	Logger logger = Logger.getLogger(AdminDAOImpl.class);
	DataSource dataSource;
	
	static final String fields = " id, pw, name, cellPhone, address, userType, lastLogin, registerDate ";
	static final String selectQuery = "select " + fields + " from admin";
	static final String selectQueryById = "select " + fields + " from admin where id=?";
	static final String selectQueryByIdAndPw = "select " + fields + " from admin where id=? and pw=?";
	public static String deleteQuery = "delete from admin where id=?";
	public static String updateQuery = "update admin set pw=?, name=?, cellPhone=?,address=?,userType=? where id=?";
	
	public static String registerQuery = "insert into admin (id, pw, name, cellPhone, address, userType) VALUES (?,?,?,?,?,?)";
	public void setDataSource(DataSource ds){
		this.dataSource = ds;
	}
	
	@Override
	public void update(Admin admin){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(updateQuery);
			ps.setString(1, admin.getPw());
			ps.setString(2, admin.getName());
			ps.setString(3, admin.getCellPhone());
			ps.setString(4, admin.getAddress());
			ps.setString(5, admin.getUserType().name());
			ps.setString(6, admin.getId());

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
	public void delete(String id){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, id);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch(SQLException e) {}
		}
	}
	
	@Override
	public void register(Admin admin){
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(registerQuery);
			ps.setString(1, admin.getId());
			ps.setString(2, admin.getPw());
			ps.setString(3, admin.getName());
			ps.setString(4, admin.getCellPhone());
			ps.setString(5, admin.getAddress());
			ps.setString(6, admin.getUserType().name());
			
			
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
	public List<Admin> getAllManagerList(){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Admin> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQuery);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally{
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public Admin findAdminByIdAndPw(String id, String pw){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Admin> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByIdAndPw);
			ps.setString(1, id);
			ps.setString(2, pw);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminInstance());
			}
			if(result.size()==0)
				result.add(null);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally{
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result.get(0);
	}
	
	@Override
	public Admin findAdminById(String id){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Admin> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryById);
			ps.setString(1, id);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminInstance());
			}
			if(result.size()==0)
				result.add(null);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally{
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result.get(0);
	}
	
}
