package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.BoardDAO;
import Kakao.kakaoeng.domain.model.Board;
import Kakao.kakaoeng.domain.model.UploadedFile;
import Kakao.kakaoeng.domain.model.User;
import Kakao.kakaoeng.domain.model.Board.ArticleSort;

@SuppressWarnings("serial")
public abstract class AbstractBoardWriteServlet extends HttpServlet {
	
	abstract public User qualified(HttpServletRequest req);
	public void process(HttpServletRequest req, HttpServletResponse resp, String boardName) throws IOException{
		User user = qualified(req);
		if(user == null){
			if(boardName.equals("admin"))
				Util.sendError(resp, "Log in session expired. try again");
			else
				Util.sendError(resp, "로그인 후 다시 시도해주세요.");
			return;
		}
		String title = req.getParameter("title");
		String contents = req.getParameter("contents");
		String o =  req.getParameter("holderDataList");
		JSONParser jsonParser = new JSONParser();
		JSONArray holderJSONList = null;
		try {
			holderJSONList = (JSONArray)jsonParser.parse(o);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<UploadedFile> holderList = new ArrayList<>();
		
		for(int i=0; i<holderJSONList.size(); i++)
			holderList.add(new UploadedFile((String) holderJSONList.get(i)));
		
		Board newBoard = new Board(-1, title, user.getId(), contents, null, -1, holderList, 0, ArticleSort.Normal);
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BoardDAO boardDAO = (BoardDAO) applicationContext.getBean(boardName+"BoardDAO");
		
		boardDAO.register(newBoard);
		
		if(boardName.equals("admin"))
			Util.sendSuccess(resp, "Success.");
		else
			Util.sendSuccess(resp, "성공적으로 글을 작성했습니다.");
	}
}
