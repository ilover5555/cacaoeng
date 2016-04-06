package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/updateNote.do")
public class UpdateNoteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Teacher t = (Teacher) req.getSession().getAttribute("teacher");
		if(t==null){
			resp.sendRedirect("./log_in.jsp");
			return;
		}
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		String lectureIdString = req.getParameter("lectureId");
		int lectureId = Integer.parseInt(lectureIdString);
		String note = req.getParameter("note");
		
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		Lecture lecture = lectureDAO.getLectureByIdTransaction(lectureId);
		if(!t.getId().equals(lecture.getTeacherId())){
			resp.setStatus(401);
			resp.setContentType("text/plain");
			resp.getWriter().write("This lecture is not in your hand.");
			return;
		}
		lectureDAO.updateNote(lectureId, note);
		
		resp.setStatus(200);
		resp.setContentType("text/plain");
		resp.getWriter().write("Update Note Success");
	}

}
