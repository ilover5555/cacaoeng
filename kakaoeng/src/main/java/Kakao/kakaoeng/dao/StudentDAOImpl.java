package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.ResultSetDataGetterFromDataBase;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Student.Level;

public class StudentDAOImpl implements StudentDAO {
	DataSource dataSource;
	Logger logger = Logger.getLogger(StudentDAOImpl.class);
	
	static final String fields = " id, pw, name, className, birth, gender, ZIP, address, detailAddress, "
			+"cellPhone, homePhone, lastLogin, registerDate, parentName, parentPhone, level, skype, note, coupon,  "
			+ "email,primaryProfile ";
	
	static final String selectQueryById = "select " + fields + " from student where id=?";
	static final String selectQueryAfterLastLogin = "select " + fields + " from student where lastLogin >= ?";
	static final String selectQueryByIdAndPw = "select " + fields + " from student where id=? and pw=?";
	static final String selectQueryByName = "select " + fields + " from student where name like ?";
	static final String selectQueryByNameAndCellPhone = "select " + fields + " from student where name=? and cellPhone=?";
	static final String selectQuery = "select " + fields + " from student";
	public static final String insertQuery = 
			"insert into student (id, pw, name, className, birth, gender, ZIP, address, detailAddress, cellPhone, homePhone, "
			+ " parentName, parentPhone, level, skype, note, coupon, email, primaryProfile) VALUES "
			+ "("
			+ "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?"
			+ ")";
	static final String updateQuery = 
			"update student set pw=?, name=?, className=?, birth=?, zip=?, address=?, detailAddress=?, cellPhone=?, homePhone=?, "
			+ "parentName=?, parentPhone=?, level=?, skype=?, email=? where id = ?";
	static final String selectClassNameById = "select className from student where id=?";
	static final String selectNameById = "select name from student where id=?";
	public static String setNote = "update student set note=? where id=?";
	public static String updateNote = "update student set note=concat(note, ?) where id=?";
	public static String updateLevel = "update student set level=? where id=?";
	public static String updateCoupon = "update student set coupon=? where id=?";
	public static String updatePw = "update student set pw=? where id=?";
	public static String deleteQuery = "delete from student where id=?";
	
	@Override
	public List<Student> findStudentListAfterLastLogin(java.util.Date baseDate){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Student> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryAfterLastLogin);
			ps.setDate(1, new Date(baseDate.getTime()));
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeStudentInstance());
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
	public String findNameById(String id){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectNameById);
			ps.setString(1, id);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			rs.next();
			
			result =rs.getString(1);
			
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
	public String findClassNameById(String id){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectClassNameById);
			ps.setString(1, id);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			rs.next();
			
			result =rs.getString(1);
			
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
	public void delete(String studentId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, studentId);

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
	
	@Override
	public void register(Student student)
	{
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, student.getId());
			ps.setString(2, student.getPw());
			ps.setString(3, student.getName());
			ps.setString(4, student.getClassName());
			ps.setDate(5,new Date(student.getBirth().getTime()));
			ps.setString(6, student.getGender().name());
			ps.setString(7, student.getZIP());
			ps.setString(8, student.getAddress());
			ps.setString(9, student.getDetailAddress());
			ps.setString(10, student.getCellPhone());
			ps.setString(11, student.getHomePhone());
			ps.setString(12, student.getParentName());
			ps.setString(13, student.getParentPhone());
			ps.setString(14, student.getLevel().name());
			ps.setString(15, student.getSkypeId());
			ps.setString(16, student.getNote());
			ps.setInt(17, 3);
			ps.setString(18, student.getEmail());
			ps.setString(19, student.getPrimaryProfilePicture());
			
			
			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally{
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	@Override
	public void setNote(String studentId, String newNote){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(setNote);
			ps.setString(1, newNote);
			ps.setString(2, studentId);

			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	@Override
	public void updateNote(String studentId, String newNote){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateNote);
			ps.setString(1, newNote);
			ps.setString(2, studentId);

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
	public void updateLevel(String studentId, Level level){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateLevel);
			ps.setString(1, level.name());
			ps.setString(2, studentId);

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
	public void updatePw(String studentId, String pw){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updatePw);
			ps.setString(1, pw);
			ps.setString(2, studentId);

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
	public void updateCoupon(String studentId, int coupon){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateCoupon);
			ps.setInt(1, coupon);
			ps.setString(2, studentId);

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
	public List<Student> getAllStudentList(){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Student> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQuery);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeStudentInstance());
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
	public Student findStudentById(String id){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Student> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryById);
			ps.setString(1, id);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeStudentInstance());
			}
			if(result.size() == 0)
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
	public List<Student> findStudentByName(String name){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Student> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByName);
			ps.setString(1, "%"+name+"%");
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeStudentInstance());
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
	public List<Student> findStudentByNameAndCellPhone(String name, String cellPhone){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Student> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByNameAndCellPhone);
			ps.setString(1, name);
			ps.setString(2, cellPhone);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeStudentInstance());
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
	public Student findStudentByIdAndPw(String id, String pw){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Student> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByIdAndPw);
			ps.setString(1, id);
			ps.setString(2, pw);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeStudentInstance());
			}
			if(result.size() == 0)
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
	
	public void setDataSource(DataSource ds){
		this.dataSource = ds;
	}
	
	@Override
	public void update(Student student){
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateQuery);
			
			ps.setString(1, student.getPw());
			ps.setString(2, student.getName());
			ps.setString(3, student.getClassName());
			ps.setDate(4, new Date(student.getBirth().getTime()));
			ps.setString(5, student.getZIP());
			ps.setString(6, student.getAddress());
			ps.setString(7, student.getDetailAddress());
			ps.setString(8, student.getCellPhone());
			ps.setString(9, student.getHomePhone());
			ps.setString(10, student.getParentName());
			ps.setString(11, student.getParentPhone());
			ps.setString(12, student.getLevel().name());
			ps.setString(13, student.getSkype());
			ps.setString(14, student.getEmail());
			ps.setString(15, student.getId());
			
			logger.info(ps.toString());
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally{
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
}
