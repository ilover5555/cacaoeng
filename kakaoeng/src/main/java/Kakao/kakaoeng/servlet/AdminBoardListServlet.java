package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminBoardList.view")
public class AdminBoardListServlet extends AbstractBoardListServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		process(req, resp, "admin");
		req.getRequestDispatcher("./admin_board_list.jsp").include(req, resp);
	}
	
}
