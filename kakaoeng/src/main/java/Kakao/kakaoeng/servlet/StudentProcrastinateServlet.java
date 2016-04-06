package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.ClassLogManager;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;

@SuppressWarnings("serial")
@WebServlet("/m5/studentProcrastinate.do")
public class StudentProcrastinateServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		Student student = (Student) req.getSession().getAttribute("student");
		int oneClassId = Integer.parseInt(req.getParameter("oneClassId"));
		
		String date = req.getParameter("date");
		
		ClassLogManager classLogManager = (ClassLogManager) applicationContext.getBean("classLogManager");
		String result = null;
		try{
		 result = classLogManager.registerClassLogTransaction(student, oneClassId, Util.parseDate(date), ClassState.PostponeStudent, "Student request on web");
		}catch(RuntimeException e){
			result = e.getMessage();
			Util.sendError(resp, result);
			return;
		}
		
		Util.sendSuccess(resp, result);
	}

}
