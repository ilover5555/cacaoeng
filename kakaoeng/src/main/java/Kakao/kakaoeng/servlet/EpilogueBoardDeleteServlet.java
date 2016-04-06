package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Kakao.kakaoeng.domain.model.User;

@SuppressWarnings("serial")
@WebServlet("/m7/epilogueBoardDelete.do")
public class EpilogueBoardDeleteServlet extends AbstractBoardDeleteServlet {

	@Override
	public User qualified(HttpServletRequest req) {
		HttpSession session = req.getSession();
		if(session.getAttribute("admin") != null)
			return (User) session.getAttribute("admin");
		else if(session.getAttribute("student") != null)
			return (User) session.getAttribute("student");
		
		return null;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, "epilogue");
	}

}
