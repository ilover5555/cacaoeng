package Kakao.kakaoeng.servlet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.BoardDAO;
import Kakao.kakaoeng.dao.UserDAO;
import Kakao.kakaoeng.domain.model.Board;
import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.domain.model.Board.ArticleSort;

@SuppressWarnings("serial")
public class AbstractBoardViewServlet extends HttpServlet {
	public static BoardDAO getBoardDAO(ApplicationContext applicationContext, String boardName){
		return (BoardDAO) applicationContext.getBean(boardName+"BoardDAO");
	}
	public boolean qualified(HttpServletRequest req){
		return true;
	}
	
	public boolean process(HttpServletRequest req, HttpServletResponse resp, Class<? extends AbstractBoardListServlet> klass, String boardName){
		if(!this.qualified(req))
			throw new LoginRequiredException();
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		String boardIdString = req.getParameter("boardId");
		int boardId = -1;
		try{
			boardId = Integer.parseInt(boardIdString);
		}catch(RuntimeException e){
			e.printStackTrace();
			return false;
		}
		
		BoardDAO baordDAO = getBoardDAO(applicationContext, boardName);
		UserDAO userDAO = (UserDAO) applicationContext.getBean("userDAO");
		
		Board board = baordDAO.getBoardById(boardId);
		if(board == null){
			board = new Board(-1, "", "", "", new Date(), -1, new ArrayList<>(), 0, ArticleSort.Normal);
		}
		board.loadAuthor(userDAO);
		List<Board> comments = baordDAO.getBoardByParent(board.getId());
		for(Board comment : comments){
			comment.loadAuthor(userDAO);
		}
		req.setAttribute("Board", board);
		req.setAttribute("comments", comments);
		
		Class<?>[] parameter = new Class<?>[3];
		parameter[0] = HttpServletRequest.class;
		parameter[1] = HttpServletResponse.class;
		parameter[2] = String.class;
		Method processMethod = null;
		try {
			AbstractBoardListServlet o = klass.newInstance();
			processMethod = klass.getMethod("process", parameter);
			processMethod.invoke(o, req, resp, boardName);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		baordDAO.increaseCount(board.getId());
		return true;
	}
}
