package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BoardDAO;
import Kakao.kakaoeng.domain.model.Board;
import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.domain.model.User.UserType;

@SuppressWarnings("serial")
public abstract class AbstractBoardUpdateServlet extends HttpServlet {
	public abstract User qualified(HttpServletRequest req);
	
	public boolean processGet(HttpServletRequest req, HttpServletResponse resp, String boardName, String errorRedirectPage) throws IOException{
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		String boardIdString = req.getParameter("boardId");
		int boardId = Integer.parseInt(boardIdString);
		BoardDAO boardDAO = (BoardDAO) applicationContext.getBean(boardName+"BoardDAO");
		Board board = boardDAO.getBoardById(boardId);
		User user = this.qualified(req);
		if(user == null){
			resp.sendRedirect(errorRedirectPage);
			return false;
		}
		if(!(user.getUserType().equals(UserType.Admin) || user.getId().equals(board.getWriter()))){
			resp.sendRedirect(errorRedirectPage);
			return false;
		}
		req.setAttribute("Board", board);
		return true;
	}
	
	public void processPost(HttpServletRequest req, HttpServletResponse resp, String boardName) throws IOException{
		String boardIdString = req.getParameter("boardId");
		int boardId = Integer.parseInt(boardIdString);
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BoardDAO boardDAO = (BoardDAO) applicationContext.getBean(boardName+"BoardDAO");
		Board board = boardDAO.getBoardById(boardId);
		User user = this.qualified(req);
		if(user == null){
			if(boardName.equals("admin"))
				Util.sendError(resp, "Your log in invalid");
			else
				Util.sendError(resp, "로그인 후 다시 시도 하세요.");
			return;
		}
		if(!(user.getUserType().equals(UserType.Admin) || user.getId().equals(board.getWriter()))){
			if(boardName.equals("admin"))
				Util.sendError(resp, "This article is not in your hand.");
			else
				Util.sendError(resp, "권한이 없습니다.");
			return;
		}
		String title = req.getParameter("title");
		String contents = req.getParameter("contents");
		Board updateBoard = new Board(boardId, title, null, contents, null, 0, null, 0, null);
		boardDAO.update(updateBoard);
		if(boardName.equals("admin"))
			Util.sendSuccess(resp, "Update Success.");
		else
			Util.sendSuccess(resp, "성공적으로 수정 되었습니다.");
	}
}
