package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BoardDAO;
import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.domain.model.Board.ArticleSort;

@SuppressWarnings("serial")
public abstract class AbstractBoardNoticeServlet extends HttpServlet {
	
	public abstract User qualified(HttpServletRequest req);
	public void process(HttpServletRequest req, HttpServletResponse resp, String boardName) throws IOException{
		User user = this.qualified(req);
		if(user == null){
			if(boardName.equals("admin"))
				Util.sendError(resp, "Log in Invalid");
			else
				Util.sendError(resp, "로그인 후 다시시도해주세요");
			return;
		}
		String boardIdString = req.getParameter("boardId");
		int boardId = Integer.parseInt(boardIdString);
		String notice = req.getParameter("notice");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BoardDAO boardDAO = (BoardDAO) applicationContext.getBean(boardName + "BoardDAO");
		if(notice.equals("notice")){
			boardDAO.notice(boardId, ArticleSort.Notice);
		}else if(notice.equals("unnotice")){
			boardDAO.unnotice(boardId);
		}else{
			throw new IllegalArgumentException(boardName+"NoticeBoard get Invalid notice.");
		}
		
		Util.sendSuccess(resp, notice + " is is success. : " + boardId);
	}
}
