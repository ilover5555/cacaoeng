package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Student.Level;

@WebServlet("/teacher/adminLevel.edit")
public class AdminLevelEditServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String studentId = req.getParameter("studentId");
		Level level = Level.valueOf(req.getParameter("level"));
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		
		studentDAO.updateLevel(studentId, level);
		Util.sendSuccess(resp, "레벨을 성공적으로 변경하였습니다.");
	}

}
