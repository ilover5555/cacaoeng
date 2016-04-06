package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.domain.model.User.UserType;

public class UserDAOImpl implements UserDAO {
	DataSource dataSource;
	Logger logger = Logger.getLogger(UserDAOImpl.class);
	@Autowired TeacherDAO teacherDAO;
	@Autowired StudentDAO studentDAO;
	@Autowired AdminDAO adminDAO;
	public void setDataSource(DataSource ds){
		this.dataSource = ds;
	}
	
	public static final String lastLoginSetQuery = "update %s set `lastLogin` = now() where id=? and pw=?";
	
	@Override
	public void updateLastLogin(String id, String pw, UserType userType){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			String query = String.format(lastLoginSetQuery, userType.name().toLowerCase());
			ps = connection.prepareStatement(query);
			ps.setString(1, id);
			ps.setString(2, pw);

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
	public User findUserById(String id){
		Teacher teacher = teacherDAO.getTeacherWithId(id);
		if(teacher != null)
			return teacher;
		
		Student student = studentDAO.findStudentById(id);
		if(student != null)
			return student;
		
		Admin admin = adminDAO.findAdminById(id);
		if(admin != null)
			return admin;
		
		return null;
	}

}
