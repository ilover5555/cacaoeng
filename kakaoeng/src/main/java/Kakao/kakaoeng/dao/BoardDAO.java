package Kakao.kakaoeng.dao;

import java.util.List;

import Kakao.kakaoeng.domain.model.Board;
import Kakao.kakaoeng.domain.model.Board.ArticleSort;

public interface BoardDAO {
	public static String deli = "<>";
	List<Board> getBoardList(int page, int viewPerPage, int parent);

	Board getBoardById(int id);

	void register(Board board);

	List<Board> getBoardByParent(int parentId);

	void increaseCount(int boardId);

	int getNormalBoardCount();

	List<Board> getNoticeBoardList();

	void delete(int boardId);

	void unnotice(int boardId);

	void update(Board board);

	void notice(int boardId, ArticleSort sort);

	List<Board> getBoardListByTitle(int page, int viewPerPage, String title);

	int getNormalBoardCountByTitle(String title);

	int getNormalBoardCountByContents(String contents);

	List<Board> getBoardListByContents(int page, int viewPerPage, String contents);

	int getNormalBoardCountByName(String name);

	List<Board> getBoardListByName(int page, int viewPerPage, String name);

	List<Board> getBoardListByWriter(int page, int viewPerPage, String writer);

	int getNormalBoardCountByWriter(String writer);

}
