package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/studentUpdateNote.do")
public class StudentUpdateNoteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Teacher teacher = (Teacher) req.getSession().getAttribute("teacher");
		if(teacher == null){
			resp.sendRedirect("./index.jsp");
			return;
		}
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		String studentId = req.getParameter("studentId");
		String newNote = req.getParameter("note");
		
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		studentDAO.updateNote(studentId, "Teacher " + teacher.getClassName() +"/ " + Util.dateFormatting(new Date()) +"\n" + newNote+"\n\n");
		
		resp.setStatus(200);
		resp.setContentType("text/plain");
		resp.getWriter().write("Update User Note Success.");
	}


}
