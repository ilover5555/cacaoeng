package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.HttpServletDataGetterFromPreview;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/profilePreview.view")
@MultipartConfig
public class ProfilePreviewViewServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Util.debugRequstParameter(req);
		
		
		Teacher teacher = new Teacher("", "", "", req.getParameter("className"),
				null, null, null, "", "", "", null, null, "", "", "", req.getParameter("competency"),
				"", false, false, "", "", "", "", "", "", "", "", "", "dummy",
				"dummy", "", "", false, null, false, -1, null, null, "", false);
				
		
		List<Teacher> teacherList = new ArrayList<>();
		teacherList.add(teacher);
		
		req.setAttribute("repList", teacherList);
		
		req.getRequestDispatcher("./TeacherCard.jsp").forward(req, resp);;
	}

}
