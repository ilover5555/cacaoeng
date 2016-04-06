package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.BoardDAO;
import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.Board;
import Kakao.kakaoeng.domain.model.Student;

@SuppressWarnings("serial")
@WebServlet("/m5/studentBoardList.view")
public class StudentBoardListServlet extends AbstractBoardListServlet {

	
	
	@Override
	public int getBoardList(List<Board> boardList, BoardDAO boardDAO, HttpServletRequest req) {
		LoginChecker lc = new LoginChecker(req);
		if(lc.getAdminLogin()){
			return super.getBoardList(boardList, boardDAO, req);
		}
		else if(lc.getStudentLogin()){
			Student student = lc.getLoginStudentObject();
			int page = this.getPage(req);
			int viewPerPage = this.getViewPerPage(req);
			boardList.addAll(boardDAO.getBoardListByWriter(page, viewPerPage, student.getId()));
			int count = boardDAO.getNormalBoardCountByWriter(student.getId());
			return count;
		}
		return -1;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			process(req, resp, "student");
		}catch(LoginRequiredException e){
			resp.sendRedirect("./main/main.jsp");
			return;
		}
		req.getRequestDispatcher("./student_board_list.jsp").include(req, resp);
	}

}