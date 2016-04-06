package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.User;

@SuppressWarnings("serial")
@WebServlet("/m7/notifyNotice.do")
public class NotifyNoticeBoardServlet extends AbstractBoardNoticeServlet {
	public User qualified(HttpServletRequest req){
		return (Admin) req.getSession().getAttribute("admin");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, "notify");
	}

}
