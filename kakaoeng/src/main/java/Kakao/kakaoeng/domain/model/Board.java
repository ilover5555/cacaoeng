package Kakao.kakaoeng.domain.model;

import java.util.Date;
import java.util.List;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BoardDAO;
import Kakao.kakaoeng.dao.UserDAO;

public class Board {
	public enum ArticleSort{
		Notice, Admin, Best, Teacher, Epilogue, Propose, Normal
	}
	
	int id;
	String title;
	String writer;
	String contents;
	Date date;
	int parent;
	List<UploadedFile> fileList;
	int count;
	ArticleSort sort;
	List<Board> commentList;
	User author;
	
	
	public Board(int id, String title, String writer, String contents, Date date, int parent, List<UploadedFile> fileList, int count, ArticleSort sort) {
		super();
		this.id = id;
		this.title = title;
		this.writer = writer;
		this.contents = contents;
		this.date = date;
		this.parent = parent;
		this.fileList = fileList;
		this.count = count;
		this.sort = sort;
	}
	
	public User getAuthor(){
		return this.author;
	}
	
	public void loadAuthor(UserDAO userDAO){
		author = userDAO.findUserById(writer);
	}
	
	public void loadCommentList(BoardDAO boardDAO){
		commentList = boardDAO.getBoardByParent(this.id);
	}
	
	public List<Board> getCommentList(){
		if(commentList == null)
			throw new IllegalStateException("comment is not initialized yet.");
		return commentList;
	}
	
	public ArticleSort getSort() {
		return sort;
	}

	public int getCount() {
		return count;
	}

	public String getFileListString(){
		StringBuilder sb = new StringBuilder("");
		for(int i=0; i<fileList.size(); i++){
			sb.append(fileList.get(i).getDBPath());
			if(i < fileList.size()-1)
				sb.append(BoardDAO.deli);
		}
		return sb.toString();
	}
	
	public List<UploadedFile> getFileList() {
		return fileList;
	}

	public int getParent() {
		return parent;
	}

	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getWriter() {
		return writer;
	}
	public String getContents() {
		return contents;
	}
	public Date getDate() {
		return date;
	}
	public String getDateForm(){
		return Util.dateFormatting(date);
	}
	public String getDetailDateForm(){
		return Util.detailDateFormatting(date);
	}
	public String getStudentBoardDateForm(){
		return Util.dateFormatting(date).replace('-', '.');
	}
}
