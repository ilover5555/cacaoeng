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

import Kakao.kakaoeng.Util;

public class HolidayDAOImpl implements HolidayDAO{
DataSource dataSource;
	Logger logger = Logger.getLogger(HolidayDAOImpl.class);
	static final String selectQueryById = "select holiday from holiday where ? <= holiday and holiday <= ?";
	static final String insertQuery = "insert into holiday (holiday) VALUES (?)";
	static final String checkQuery = "select count(*) from holiday where holiday=?";
	static final String deleteQuery = "delete from holiday where holiday=?";
	public void setDataSource(DataSource ds){
		this.dataSource = ds;
	}
	
	@Override
	public void deleteTransaction(Date holiday){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(deleteQuery);
			ps.setDate(1, new java.sql.Date(holiday.getTime()));

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
	public boolean checkHoliday(Date date){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(checkQuery);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			rs.next();
			int count = rs.getInt(1);
			if(count == 0){
				logger.info(Util.dateFormatting(date)+"is not holiday");
				return false;
			}
			else{
				logger.info(Util.dateFormatting(date)+"is HOLIDAY");
				return true;
			}
			
		} catch (SQLException e) {
			logger.error("error", e);
			throw new RuntimeException(e);
		}
		finally{
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	/***
	 * 
	 * find holiday list. including range start and including range end
	 * start <= holiday <= end
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	
	@Override
	public List<Date> findHolidayWithRange(Date start, Date end){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Date> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryById);
			ps.setDate(1, new java.sql.Date(start.getTime()));
			ps.setDate(2, new java.sql.Date(end.getTime()));
			
			rs = ps.executeQuery();
			logger.info(ps.toString());
			while(rs.next()){
				result.add(rs.getDate(1));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public void registerHolidayTransaction(Date date)
	{
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(insertQuery);

			ps.setDate(1, new java.sql.Date(date.getTime()));
			logger.info(ps.toString());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			logger.error("error", e);
			throw new RuntimeException(e);
		}
		finally{
			try {ps.close();} catch (SQLException e) {}
			try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {}
		}
	}
}
