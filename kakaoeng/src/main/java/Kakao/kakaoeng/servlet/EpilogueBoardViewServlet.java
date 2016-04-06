package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;

@SuppressWarnings("serial")
@WebServlet("/m7/epilogueBoardView.view")
public class EpilogueBoardViewServlet extends AbstractBoardViewServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			if(process(req, resp, EpilogueBoardListServlet.class , "epilogue"))
				req.getRequestDispatcher("./epilogue_board_view.jsp").include(req, resp);
			else
				req.getRequestDispatcher("./epilogueBoardList.view").include(req, resp);
		}catch(LoginRequiredException e){
			resp.sendRedirect("./main/main.jsp");
			return;
		}
	}

}
