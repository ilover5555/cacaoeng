package Kakao.kakaoeng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;

import Kakao.kakaoeng.DataGetter;
import Kakao.kakaoeng.ResultSetDataGetterFromDataBase;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.Teacher.Quality;
import Kakao.kakaoeng.domain.model.Teacher.Rate;

public class TeacherDAOImpl implements TeacherDAO {
	DataSource dataSource = null;
	Logger logger = Logger.getLogger(TeacherDAOImpl.class);
	static final String fields = " id, pw, name, birth, gender, address, "
			+"cellPhone, homePhone, lastLogin, registerDate, className, univ, univDetail, education, competency, experience, toefl, ilets, specImage,"
			+"internetSpeed, internetProvider, internetType, upSpeed, downSpeed, os, bankName, bankAccount,"
			+ "primaryProfile, primaryVoice, skype, lineId, confirm, rate, representitive, salary, pronunciation, accent,"
			+ "`comment`, retirement ";
	static final String selectQuery = "select " + fields + " from teacher";
	static final String insertQuery = 
			"insert into teacher (id, pw, name, birth, gender, address, "
					+"cellPhone, homePhone, className, univ, univDetail, education, competency, experience, toefl, ilets, specImage,"
					+ "internetSpeed, internetProvider, internetType, upSpeed, downSpeed, os, bankName, bankAccount,"
					+ "primaryProfile, primaryVoice, skype, lineId, confirm, rate, representitive, salary, "
					+ "pronunciation, accent, comment, retirement, lastLogin) values"
					+" ("
					+ "?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?"
					+ ")";
	static final String updateQuery = 
			"update teacher set pw=?, name=?, birth=?,"
			+ "gender=?, address=?, cellPhone=?, homePhone=?, className=?,"
			+ "univ=?, univDetail=?, education=?, competency=?, experience=?, toefl=?, ilets=?, specImage=?, "
			+ "internetSpeed=?, internetProvider=?, internetType=?, upSpeed=?, downSpeed=?, os=?, bankName=?, bankAccount=?,"
			+ "primaryProfile=?, primaryVoice=?, skype=?, lineId=? "
			+ "where id = ?";
	public static String setConfirm = "update teacher set confirm=? where id=?";
	public static String setRepresentitive = "update teacher set representitive=? where id=?";
	public static String setRetirement = "update teacher set retirement=? where id=?";
	public static String setRate = "update teacher set rate=? where id=?";
	final String findQuery = "select count(*) from teacher where id = ?";
	final String findByClassNameQuery = "select count(*) from teacher where className = ?";
	final String findRateCountQuery = "select count(*) from teacher where rate=? and confirm=1";
	public static String deleteQuery = "delete from teacher where id=?";
	
	static final String findActiveByName = "select " + fields + " from teacher where retirement=0 and confirm=1 and className like ?";
	static final String findRetireByName = "select " + fields + " from teacher where retirement=1 and className like ?";
	
	static final String updateForAdminQuery = 
			"update teacher set salary=?, pronunciation=?, accent=?, comment=? "
			+ "where id = ?";
	
	static final String selectClassNameById = "select className from teacher where id=?";
	
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
			
			while(rs.next()){
			
				result =rs.getString(1);
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
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
	public List<Teacher> searchActiveTeacherByClassName(String className){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Teacher> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findActiveByName);
			ps.setString(1, "%"+className+"%");
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add((Teacher) dataGetter.makeTeacherInstance());
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
	public List<Teacher> searchRetireTeacherByClassName(String className){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Teacher> result = new ArrayList<>();
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findRetireByName);
			ps.setString(1, "%"+className+"%");
			logger.info(ps.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add((Teacher) dataGetter.makeTeacherInstance());
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
	public void updateRate(String teacherId, Rate rate){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(setRate);
			ps.setString(1, rate.name());
			ps.setString(2, teacherId);

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
	public void updateConfirm(String teacherId, boolean confirm){
		if(confirm == false)
			throw new UnsupportedOperationException("update Confirm cannot back");
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(setConfirm);
			ps.setBoolean(1, confirm);
			ps.setString(2, teacherId);

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
	public void updateRepresentitive(String teacherId, boolean representitive){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(setRepresentitive);
			ps.setBoolean(1, representitive);
			ps.setString(2, teacherId);

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
	public void updateRetirement(String teacherId, boolean retirement){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(setRetirement);
			ps.setBoolean(1, retirement);
			ps.setString(2, teacherId);

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
	 * @see english.dao.TeacherDAO#findTeacherById(java.lang.String)
	 */
	@Override
	public boolean findTeacherById(String id){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findQuery);
			ps.setString(1, id);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			if(count == 0)
				return false;
			else
				return true;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally{
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	@Override
	public boolean findTeacherByClssName(String className){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findByClassNameQuery);
			ps.setString(1, className);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			if(count == 0)
				return false;
			else
				return true;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally{
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
	}
	
	@Override
	public int findTeacherCountByRate(Rate rate){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = -1;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findRateCountQuery);
			ps.setString(1, rate.name());
			logger.info(ps.toString());
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally{
			try {rs.close();} catch (SQLException e) {}
			try {ps.close();} catch (SQLException e) {}
			try {connection.close();} catch (SQLException e) {}
		}
		return count;
	}
	
	public void setDataSource(DataSource ds){
		this.dataSource = ds;
	}
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#getSpecificRateTeacher(java.lang.String)
	 */
	@Override
	public List<Teacher> getSpecificRateTeacher(String rate){
		Map<String, String> condition = new HashMap<>();
		condition.put("rate", rate);
		return getTeacherList(condition);
	}
	
	@Override
	public void updateForAdmin(String id, int salary, Quality pronunciation, Quality accent, String comment){
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateForAdminQuery);
			
			ps.setInt(1, salary);
			ps.setString(2, pronunciation.name());
			ps.setString(3, accent.name());
			ps.setString(4, comment);
			ps.setString(5, id);
			
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
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#update(english.domain.model.Teacher)
	 */
	@Override
	public void update(Teacher teacher){
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateQuery);
			
			ps.setString(1, teacher.getPw());
			ps.setString(2, teacher.getName());
			ps.setDate(3, new java.sql.Date(teacher.getBirth().getTime()));
			ps.setString(4, teacher.getGender().name());
			ps.setString(5, teacher.getAddress());
			ps.setString(6, teacher.getCellPhone());
			ps.setString(7, teacher.getHomePhone());
			ps.setString(8, teacher.getClassName());
			ps.setString(9, teacher.getUniv() );
			ps.setString(10, teacher.getUnivDetail());
			ps.setString(11, teacher.getEducation());
			ps.setString(12, teacher.getCompetency());
			ps.setString(13, teacher.getExperience());
			ps.setBoolean(14, teacher.getToefl());
			ps.setBoolean(15, teacher.getIlets());
			ps.setString(16, teacher.getSpecImage());
			ps.setString(17, teacher.getInternetSpeed());
			ps.setString(18, teacher.getInternetProvider());
			ps.setString(19, teacher.getInternetType());
			ps.setString(20, teacher.getUpSpeed());
			ps.setString(21, teacher.getDownSpeed());
			ps.setString(22, teacher.getOs());
			ps.setString(23, teacher.getBankName());
			ps.setString(24, teacher.getBankAccount());
			ps.setString(25, teacher.getPrimaryProfilePicture());
			ps.setString(26, teacher.getPrimaryVoice());
			ps.setString(27, teacher.getSkype());
			ps.setString(28, teacher.getLineId());
			ps.setString(29, teacher.getId());
			
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
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#register(english.domain.model.Teacher)
	 */
	@Override
	public void register(Teacher teacher)
	{
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, teacher.getId());
			ps.setString(2, teacher.getPw());
			ps.setString(3, teacher.getName());
			ps.setDate(4, new java.sql.Date(teacher.getBirth().getTime()));
			ps.setString(5, teacher.getGender().name());
			ps.setString(6, teacher.getAddress());
			ps.setString(7, teacher.getCellPhone());
			ps.setString(8, teacher.getHomePhone());
			ps.setString(9, teacher.getClassName());
			ps.setString(10, teacher.getUniv());
			ps.setString(11, teacher.getUnivDetail());
			ps.setString(12, teacher.getEducation());
			ps.setString(13, teacher.getCompetency());
			ps.setString(14, teacher.getExperience());
			ps.setBoolean(15, teacher.getToefl());
			ps.setBoolean(16, teacher.getIlets());
			ps.setString(17, teacher.getSpecImage());
			ps.setString(18, teacher.getInternetSpeed());
			ps.setString(19, teacher.getInternetProvider());
			ps.setString(20, teacher.getInternetType());
			ps.setString(21, teacher.getUpSpeed());
			ps.setString(22, teacher.getDownSpeed());
			ps.setString(23, teacher.getOs());
			ps.setString(24, teacher.getBankName());
			ps.setString(25, teacher.getBankAccount());
			ps.setString(26, teacher.getPrimaryProfilePicture());
			ps.setString(27, teacher.getPrimaryVoice());
			ps.setString(28, teacher.getSkype());
			ps.setString(29, teacher.getLineId());
			ps.setBoolean(30, teacher.getConfirm());
			ps.setString(31, teacher.getRate().name());
			ps.setBoolean(32, teacher.getRepresentitive());
			ps.setInt(33, teacher.getSalary());
			ps.setString(34, teacher.getPronunciation().name());
			ps.setString(35, teacher.getAccent().name());
			ps.setString(36, teacher.getComment());
			ps.setBoolean(37, teacher.getRetirement());
			ps.setDate(38, new java.sql.Date(teacher.getLastLogin().getTime()));
			
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
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#getConfirmedTeacherList()
	 */
	@Override
	public List<Teacher> getConfirmedTeacherList(){
		Map<String, String> condition = new HashMap<>();
		condition.put("confirm", "1");
		return getTeacherList(condition);
	}
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#getUnConfirmedTeacherList()
	 */
	@Override
	public List<Teacher> getUnConfirmedTeacherList(){
		Map<String, String> condition = new HashMap<>();
		condition.put("confirm", "0");
		return getTeacherList(condition);
	}
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#getUnConfirmedTeacherList()
	 */
	@Override
	public List<Teacher> getActivedTeacherList(){
		Map<String, String> condition = new HashMap<>();
		condition.put("confirm", "1");
		condition.put("retirement", "0");
		return getTeacherList(condition);
	}
	
	@Override
	public List<Teacher> getRepresentitiveTeacherList(){
		Map<String, String> condition = new HashMap<>();
		//condition.put("confirm", "1");
		condition.put("representitive", "1");
		return getTeacherList(condition);
	}
	
	private List<Teacher> getTeacherListTemplate(String query){
		List<Teacher> result = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = dataSource.getConnection();
			stmt = connection.createStatement();
			logger.info(query);
			rs = stmt.executeQuery(query);
			while(rs.next()){
				DataGetter dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add((Teacher) dataGetter.makeTeacherInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally {
			try {if(rs!=null) rs.close();} catch (SQLException e) {e.printStackTrace();}
			try {if(stmt!=null) stmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {if(connection!=null) connection.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#getTeacherWithId(java.lang.String)
	 */
	@Override
	public Teacher getTeacherWithId(String id){
		Map<String, String> condition = new HashMap<>();
		condition.put("id", id);
		List<Teacher> teacherList = getTeacherList(condition);
		if(teacherList.isEmpty())
			return null;
		return teacherList.get(0);
	}
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#getTeacherWithIdAndPw(java.lang.String, java.lang.String)
	 */
	@Override
	public Teacher getTeacherWithIdAndPw(String id, String pw){
		Map<String, String> condition = new HashMap<>();
		condition.put("id", id);
		condition.put("pw", pw);
		List<Teacher> teacherList = getTeacherList(condition);
		if(teacherList.isEmpty())
			return null;
		return teacherList.get(0);
	}
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#getTeacherList(java.util.Map)
	 */
	@Override
	public List<Teacher> getTeacherList(Map<String, String> where){
		if((where == null) || where.isEmpty())
			return getTeacherList();
		
		StringBuilder sbWhereClause = new StringBuilder("");
		Set<String> keyString = where.keySet();
		Iterator<String> iter = keyString.iterator();
		if(iter.hasNext())
			sbWhereClause.append(" where ");
		while(iter.hasNext()){
			String key = iter.next();
			sbWhereClause.append(key);
			sbWhereClause.append('=');
			sbWhereClause.append('\'');
			sbWhereClause.append(where.get(key));
			sbWhereClause.append('\'');
			if(iter.hasNext()){
				sbWhereClause.append(" and ");
			}
		
		}
		
		String query = selectQuery + sbWhereClause.toString();
			
		return this.getTeacherListTemplate(query);
	}
	
	/* (non-Javadoc)
	 * @see english.dao.TeacherDAO#getTeacherList()
	 */
	@Override
	public List<Teacher> getTeacherList(){
		return getTeacherListTemplate(selectQuery);
	}
	
	@Override
	public void delete(String teacherId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, teacherId);

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
	
	
}
