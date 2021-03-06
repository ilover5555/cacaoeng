package Kakao.kakaoeng.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.LoginRequiredException;

@SuppressWarnings("serial")
@WebServlet("/m7/notifyBoardList.view")
public class NotifyBoardListServlet extends AbstractBoardListServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			process(req, resp, "notify");
		}catch(LoginRequiredException e){
			resp.sendRedirect("./main/main.jsp");
			return;
		}
		req.getRequestDispatcher("./notify_board_list.jsp").include(req, resp);
	}

}