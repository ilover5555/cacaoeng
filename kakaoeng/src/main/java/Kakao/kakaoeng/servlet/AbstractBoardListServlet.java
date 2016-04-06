package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Pagination;
import Kakao.kakaoeng.dao.BoardDAO;
import Kakao.kakaoeng.dao.UserDAO;
import Kakao.kakaoeng.domain.model.Board;

@SuppressWarnings("serial")
public class AbstractBoardListServlet extends HttpServlet {
	public int getPage(HttpServletRequest req){
		String pageString = req.getParameter("page");
		int page = 1;
		
		if(!(pageString == null || pageString.equals(""))){
			try{
				page = Integer.parseInt(pageString);
			}catch(RuntimeException e){
				page = 1;
			}
		}
		
		return page;
	}
	
	public int getViewPerPage(HttpServletRequest req){
		String viewPerPageString = req.getParameter("viewPerPage");
		int viewPerPage = 20;
		
		if(!(viewPerPageString == null || viewPerPageString.equals(""))){
			try{
				viewPerPage = Integer.parseInt(viewPerPageString);
			}catch(RuntimeException e){
				viewPerPage = 20;
			}
		}
		
		return viewPerPage;
	}
	
	public BoardDAO getBoardDAO(ApplicationContext applicationContext, String boardName){
		return (BoardDAO) applicationContext.getBean(boardName+"BoardDAO");
	}
	
	
	/***
	 * Override when you 
	 * 
	 * @param boardList output(added board list to this List<>)
	 * @param boardDAO	
	 * @param req	there's no parameter named "searchMethod", It will get All Board List\nIf has "searchMethod" It will search by "searchText"
	 * @return count of All List (not viewed on this page)
	 */
	public int getBoardList(List<Board> boardList, BoardDAO boardDAO, HttpServletRequest req){
		String searchMethod = req.getParameter("searchMethod");
		String searchText = req.getParameter("searchText");
		int page = getPage(req);
		int viewPerPage =getViewPerPage(req);
		int count = 0;
		if(searchMethod != null){
			switch(searchMethod){
			case "name":
				boardList.addAll(boardDAO.getBoardListByName(page, viewPerPage, searchText));
				count = boardDAO.getNormalBoardCountByName(searchText);
				break;
			case "title":
				boardList.addAll(boardDAO.getBoardListByTitle(page, viewPerPage, searchText));
				count = boardDAO.getNormalBoardCountByTitle(searchText);
				break;
			case "contents":
				boardList.addAll(boardDAO.getBoardListByContents(page, viewPerPage, searchText));
				count = boardDAO.getNormalBoardCountByContents(searchText);
				break;
			default:
				boardList.addAll(boardDAO.getBoardList(page, viewPerPage, -1));
				count = boardDAO.getNormalBoardCount();
				break;
			}
		}
		else{
			boardList.addAll(boardDAO.getBoardList(page, viewPerPage, -1));
			count = boardDAO.getNormalBoardCount();
		}
		return count;
	}
	
	public void process(HttpServletRequest req, HttpServletResponse resp, String boardName){
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		int page = getPage(req);
		int viewPerPage =getViewPerPage(req);
		
		BoardDAO boardDAO = getBoardDAO(applicationContext, boardName);
		UserDAO userDAO = (UserDAO) applicationContext.getBean("userDAO");
		
		
		List<Board> boardList = new ArrayList<>();
		int count = getBoardList(boardList, boardDAO, req);
		if(count == -1){
			throw new LoginRequiredException();
		}
		
		List<Board> noticeBoardList = boardDAO.getNoticeBoardList();
		for(Board board : boardList){
			board.loadCommentList(boardDAO);
			board.loadAuthor(userDAO);
		}
		for(Board board : noticeBoardList){
			board.loadCommentList(boardDAO);
			board.loadAuthor(userDAO);
		}
		
		Pagination pagination = new Pagination(page, viewPerPage, count);
		req.setAttribute("BoardList", boardList);
		req.setAttribute("NoticeBoardList", noticeBoardList);
		req.setAttribute("pagination", pagination);
	}
}
