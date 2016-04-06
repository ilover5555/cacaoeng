package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.domain.model.User;

@SuppressWarnings("serial")
@WebServlet("/m7/freeComment.do")
public class FreeCommentServlet extends AbstractCommentServlet {

	@Override
	public User qualified(HttpServletRequest req) {
		LoginChecker lc = new LoginChecker(req);
		if(lc.getAdminLogin())
			return lc.getLoginAdminObject();
		else if(lc.getStudentLogin())
			return lc.getLoginStudentObject();
		else
			return null;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.process(req, resp, "free");
	}
}
