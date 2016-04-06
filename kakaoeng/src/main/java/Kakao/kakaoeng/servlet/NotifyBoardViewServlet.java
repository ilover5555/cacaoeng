package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;

@SuppressWarnings("serial")
@WebServlet("/m7/notifyBoardView.view")
public class NotifyBoardViewServlet extends AbstractBoardViewServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			if(process(req, resp, NotifyBoardListServlet.class , "notify"))
				req.getRequestDispatcher("./notify_board_view.jsp").include(req, resp);
			else
				req.getRequestDispatcher("./notifyBoardList.view").include(req, resp);
		}catch(LoginRequiredException e){
			resp.sendRedirect("./main/main.jsp");
			return;
		}
	}

}
