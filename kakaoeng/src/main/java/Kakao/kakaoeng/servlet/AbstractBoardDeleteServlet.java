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
import Kakao.kakaoeng.domain.model.User.UserType;

@SuppressWarnings("serial")
public abstract class AbstractBoardDeleteServlet extends HttpServlet {
	public abstract User qualified(HttpServletRequest req);
	
	public List<Integer> parseDeleteList(HttpServletRequest req){
		String o =  req.getParameter("deleteList");
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray)jsonParser.parse(o);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Integer> deleteList = new ArrayList<>();
		
		for(int i=0; i<jsonArray.size(); i++){
			int id = Integer.parseInt((String)jsonArray.get(i));
			deleteList.add(id);
		}
		return deleteList;
	}
	
	public void process(HttpServletRequest req, HttpServletResponse resp, String boardName) throws IOException{
		User user = qualified(req);
		if(user == null){
			if(boardName.equals("admin"))
				Util.sendError(resp, "Login is invalid");
			else
				Util.sendError(resp, "로그인 후 다시 시도해주세요.");
			return;
		}
		
		List<Integer> deleteList = this.parseDeleteList(req);
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		BoardDAO boardDAO = (BoardDAO) applicationContext.getBean(boardName + "BoardDAO");
		
		for(int id : deleteList){
			Board board = boardDAO.getBoardById(id);
			if(!(board.getWriter().equals(user.getId()) || user.getUserType().equals(UserType.Admin))){
				if(boardName.equals("admin"))
					Util.sendError(resp, board.getTitle()+"is not in your hand.");
				else
					Util.sendError(resp, board.getTitle()+"을 지울 권한이 없습니다.");
				return;
			}
			List<UploadedFile> list = board.getFileList();
			for(UploadedFile f : list){
				f.removeFileFromStorage(req.getServletContext().getRealPath("/"));
			}
			boardDAO.delete(id);
		}
		
		if(boardName.equals("admin"))
			Util.sendSuccess(resp, "Delete Success");
		else
			Util.sendSuccess(resp, "삭제되었습니다.");
	}
}
