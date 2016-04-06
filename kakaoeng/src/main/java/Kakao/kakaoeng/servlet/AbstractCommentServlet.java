package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BoardDAO;
import Kakao.kakaoeng.domain.model.Board;
import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.domain.model.Board.ArticleSort;

@SuppressWarnings("serial")
public abstract class AbstractCommentServlet extends HttpServlet {
	abstract public User qualified(HttpServletRequest req);
	
	public void process(HttpServletRequest req, HttpServletResponse resp, String boardName) throws IOException{
		User user = qualified(req);
		
		if(user == null){
			if(boardName.equals("admin"))
				Util.sendError(resp, "Admin Login Required");
			else
				Util.sendError(resp, "로그인이 유효하지 않습니다.");
			return;
		}
		
		String boardIdString = req.getParameter("boardId");
		String comment = req.getParameter("comment");
		int boardId = Integer.parseInt(boardIdString);
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BoardDAO boardDAO = (BoardDAO) applicationContext.getBean(boardName+"BoardDAO");
		
		Board board = new Board(-1, "", user.getId(), comment, null, boardId, new ArrayList<>(), 0, ArticleSort.Normal);
		boardDAO.register(board);
		
		Util.sendSuccess(resp, "Successfully comment");
	}
}
