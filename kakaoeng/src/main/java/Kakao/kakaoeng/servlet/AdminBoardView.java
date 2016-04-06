package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminBoardView.view")
public class AdminBoardView extends AbstractBoardViewServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(process(req, resp, AdminBoardListServlet.class , "admin"))
			req.getRequestDispatcher("./admin_board_view.jsp").include(req, resp);
		else
			req.getRequestDispatcher("./adminBoardList.view").forward(req, resp);
	}
}
