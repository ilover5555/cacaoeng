package Kakao.kakaoeng.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.LoginRequiredException;

@SuppressWarnings("serial")
@WebServlet("/m7/freeBoardList.view")
public class FreeBoardListServlet extends AbstractBoardListServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			process(req, resp, "free");
		}catch(LoginRequiredException e){
			resp.sendRedirect("./main/main.jsp");
			return;
		}
		req.getRequestDispatcher("./free_board_list.jsp").include(req, resp);
	}

}