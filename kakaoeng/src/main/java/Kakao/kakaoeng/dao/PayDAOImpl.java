package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import Kakao.kakaoeng.ResultSetDataGetterFromDataBase;
import Kakao.kakaoeng.domain.model.Pay;

public class PayDAOImpl implements PayDAO {

	Logger logger = Logger.getLogger(PayDAOImpl.class);
	DataSource dataSource;
	
	public static String fields = " id, teacherId, month, payed ";
	
	public static String insertQuery = "insert into pay (teacherId, month, payed) VALUES (?,?,?)";
	public static String selectQueryByTeacher = "select "+ fields +" from pay where teacherId = ?";
	public static String selectQueryByMonth = "select "+ fields +" from pay where month = ?";
	public static String selectQueryByTeacherAndMonth = "select "+ fields +" from pay where teacherId = ? and month = ?";
	public static String updatePayed = "update pay set payed=? where id = ?";
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	@Override
	public void updatePayed(int payId, boolean payed){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updatePayed);
			ps.setBoolean(1, payed);
			ps.setInt(2, payId);

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
	public Pay getPayListByTeacherIdAndMonth(String teacherId, int month) {
		List<Pay> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByTeacherAndMonth);
			ps.setString(1, teacherId);
			ps.setInt(2, month);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePayInstance());
			}
			
			if(result.size() == 0)
				result.add(new Pay(teacherId, month, false));
			if(result.size() > 1)
				throw new IllegalStateException("teacherId : " + teacherId + ", month:"+month + " has duplicate pay log");
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
	public List<Pay> getPayListByMonth(int month) {
		List<Pay> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByMonth);
			ps.setInt(1, month);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePayInstance());
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
	public List<Pay> getPayListByTeacherId(String teacherId) {
		List<Pay> result = new ArrayList<>();
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
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makePayInstance());
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
	public void register(Pay pay){
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, pay.getTeacherId());
			ps.setInt(2, pay.getMonth());
			ps.setBoolean(3, pay.getPayed());
			
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
}
