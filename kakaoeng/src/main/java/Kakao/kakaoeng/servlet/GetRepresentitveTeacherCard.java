package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/m3/representitive.view")
public class GetRepresentitveTeacherCard extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		List<Teacher> representitiveTeacherList = teacherDAO.getRepresentitiveTeacherList();
		Collections.sort(representitiveTeacherList);
		req.setAttribute("repList", representitiveTeacherList);
		req.getRequestDispatcher("./TeacherCard.jsp").include(req, resp);;
	}

}
