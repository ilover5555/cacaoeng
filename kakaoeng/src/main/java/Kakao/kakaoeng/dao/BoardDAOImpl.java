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
import Kakao.kakaoeng.domain.model.Board;
import Kakao.kakaoeng.domain.model.Board.ArticleSort;

public class BoardDAOImpl implements BoardDAO {

	DataSource dataSource;
	String boardName;
	public String selectQuery;
	public String selectQueryById;
	public String selectQueryByParent;
	public String selectQueryByWriter;
	public String insertQuery;
	public String inceraseCount;
	public String countQuery;
	public String selectNoticeQuery;
	public String deleteQuery;
	public String noticeQuery;
	public String unnoticeQuery;
	public String updateQuery;
	public String findByTitleQuery;
	public String countByTitleQuery;
	public String findByContentsQuery;
	public String countByContentsQuery;
	public String findByNameQuery;
	public String countByNameQuery;
	public String countByWriterQuery;
	
	Logger logger = Logger.getLogger(BoardDAOImpl.class);
	
	
	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
		selectQuery = "select id, title, writer, contents, `date`, parent,uploadedFile, count, sort from "
				+ boardName + "board where parent=? and sort='Normal' order by id desc limit ?,?";
		selectNoticeQuery = "select id, title, writer, contents, `date`, parent,uploadedFile, count, sort from "
				+ boardName + "board where parent=-1 and sort!='Normal' order by id desc";
		selectQueryById = "select id, title, writer, contents, `date`, parent, uploadedFile, count, sort from "
				+ boardName + "board where id = ?";
		selectQueryByParent = "select id, title, writer, contents, `date`, parent, uploadedFile, count, sort from "
				+ boardName + "board where parent = ?";
		insertQuery = "insert into " + boardName + "board (title, writer,  contents, parent, uploadedFile)"
				+ " VALUES (?, ?, ?, ?, ?)";
		inceraseCount = "update " +boardName+ "board set count=count+1 where id=?";
		countQuery = "select count(*) from " +boardName+"board where sort='Normal' and parent=-1";
		deleteQuery = "delete from " + boardName+"board where id=?";
		noticeQuery = "update " +boardName+ "board set sort=? where id=?";
		unnoticeQuery = "update " +boardName+ "board set sort='Normal' where id=?";
		updateQuery = "update " +boardName+ "board set title=?, contents=? where id=?";
		findByTitleQuery = "select id, title, writer, contents, `date`, parent,uploadedFile, count, sort from "
				+ boardName + "board where parent=-1 and sort='Normal' and title like ? order by id desc limit ?,?";;
		countByTitleQuery = "select count(*) from " +boardName+"board where sort='Normal' and parent=-1 and title like ?";
		findByContentsQuery = "select id, title, writer, contents, `date`, parent,uploadedFile, count, sort from "
				+ boardName + "board where parent=-1 and sort='Normal' and contents like ? order by id desc limit ?,?";
		countByContentsQuery = "select count(*) from " +boardName+"board where sort='Normal' and parent=-1 and contents like ?";
		selectQueryByWriter = "select id, title, writer, contents, `date`, parent, uploadedFile, count, sort from "
				+ boardName + "board where parent = -1 and sort='Normal' and writer = ? order by id desc limit ?,?";
		countByWriterQuery = "select count(*) from " +boardName+"board where sort='Normal' and parent=-1 and writer = ?";
		findByNameQuery = "SELECT * FROM studentboard AS A "+
				"INNER JOIN user AS B ON A.writer = B.id "+
				"WHERE B.className LIKE ? and (B.userType = 'Admin' or B.userType='Student') and A.parent=-1 and A.sort='Normal' order by A.id desc limit ?,?";
		countByNameQuery = "SELECT count(*) FROM studentboard AS A "+
				"INNER JOIN user AS B ON A.writer = B.id "+
				"WHERE B.className LIKE ? and (B.userType = 'Admin' or B.userType='Student') and A.parent=-1 and A.sort='Normal'";
	}

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	@Override
	public void update(Board board){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(updateQuery);
			ps.setString(1, board.getTitle());
			ps.setString(2, board.getContents());
			ps.setInt(3, board.getId());

			
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
	public void notice(int boardId, ArticleSort sort){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(noticeQuery);
			ps.setString(1, sort.name());
			ps.setInt(2, boardId);

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
	public void unnotice(int boardId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(unnoticeQuery);
			ps.setInt(1, boardId);
			
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
	public void delete(int boardId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(deleteQuery);
			ps.setInt(1, boardId);

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
	public int getNormalBoardCount() {
		int count=0;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(countQuery);
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return count;
	}
	
	@Override
	public int getNormalBoardCountByTitle(String title) {
		int count=0;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(countByTitleQuery);
			ps.setString(1, "%"+title+"%");
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return count;
	}
	
	@Override
	public int getNormalBoardCountByWriter(String writer) {
		int count=0;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(countByWriterQuery);
			ps.setString(1, writer);
			
			logger.info(ps.toString());
			
			rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return count;
	}
	
	@Override
	public int getNormalBoardCountByContents(String contents) {
		int count=0;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(countByContentsQuery);
			ps.setString(1, "%"+contents+"%");
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return count;
	}
	
	@Override
	public int getNormalBoardCountByName(String name) {
		int count=0;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(countByNameQuery);
			ps.setString(1, "%"+name+"%");
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return count;
	}
	
	@Override
	public void increaseCount(int boardId){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(inceraseCount);
			ps.setInt(1, boardId);

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
	public void register(Board board){
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, board.getTitle());
			ps.setString(2, board.getWriter());
			ps.setString(3, board.getContents());
			ps.setInt(4, board.getParent());
			ps.setString(5, board.getFileListString());

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
	public List<Board> getBoardByParent(int parentId) {
		List<Board> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByParent);
			ps.setInt(1, parentId);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminBoardInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public Board getBoardById(int id) {
		List<Board> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryById);
			ps.setInt(1, id);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminBoardInstance());
			}
			if(result.size() == 0)
				result.add(null);
			if(result.size() > 1)
				throw new IllegalStateException("getAdminBoardById get duplicate rows : " + id);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return result.get(0);
	}
	
	@Override
	public List<Board> getNoticeBoardList() {
		List<Board> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectNoticeQuery);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminBoardInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public List<Board> getBoardList(int page, int viewPerPage, int parent) {
		List<Board> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQuery);
			ps.setInt(1, parent);
			ps.setInt(2, (page-1)*viewPerPage);
			ps.setInt(3, viewPerPage);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminBoardInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public List<Board> getBoardListByTitle(int page, int viewPerPage, String title) {
		List<Board> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findByTitleQuery);
			ps.setString(1, "%"+title+"%");
			ps.setInt(2, (page-1)*viewPerPage);
			ps.setInt(3, viewPerPage);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminBoardInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public List<Board> getBoardListByWriter(int page, int viewPerPage, String writer) {
		List<Board> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(selectQueryByWriter);
			ps.setString(1, writer);
			ps.setInt(2, (page-1)*viewPerPage);
			ps.setInt(3, viewPerPage);

			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminBoardInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public List<Board> getBoardListByContents(int page, int viewPerPage, String contents) {
		List<Board> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findByContentsQuery);
			ps.setString(1, "%"+contents+"%");
			ps.setInt(2, (page-1)*viewPerPage);
			ps.setInt(3, viewPerPage);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminBoardInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return result;
	}
	
	@Override
	public List<Board> getBoardListByName(int page, int viewPerPage, String name) {
		List<Board> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			ps = connection.prepareStatement(findByNameQuery);
			ps.setString(1, "%"+name+"%");
			ps.setInt(2, (page-1)*viewPerPage);
			ps.setInt(3, viewPerPage);
			
			logger.info(ps.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				ResultSetDataGetterFromDataBase dataGetter = new ResultSetDataGetterFromDataBase(rs);
				result.add(dataGetter.makeAdminBoardInstance());
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {if(rs!=null)rs.close();} catch (SQLException e) {}
			try {if(ps!=null)ps.close();} catch (SQLException e) {}
			try {if(connection!=null)connection.close();} catch (SQLException e) {}
		}
		return result;
	}
}
