package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Teacher;

@WebServlet("/teacher/teacherCreateBBB.do")
public class TeacherCreateBBBRoom extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getTeacherLogin()){
			Util.sendError(resp, "Login First");
			return;
		}
		Teacher teacher = lc.getLoginTeacherObject();
		String url = "http://bbb.kakaoenglish.kr/demo/create.jsp?username1="+teacher.getClassName().replace(' ', '+')+"&action=create";
		Document doc = Jsoup.connect(url).get();
		Elements e = doc.select("a");
		String startLink = e.get(1).attr("href");
		Util.sendSuccess(resp, startLink);
	}

}
