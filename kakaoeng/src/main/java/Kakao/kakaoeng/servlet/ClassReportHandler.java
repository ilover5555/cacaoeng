package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.ClassLogManager;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;

@SuppressWarnings("serial")
@WebServlet("/classReportHandler.do")
public class ClassReportHandler extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Teacher teacher = (Teacher) req.getSession().getAttribute("teacher");
		
		if(teacher == null){
			req.getRequestDispatcher("./log_in.jsp").forward(req, resp);
			return;
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		
		
		String oneClassIdString = req.getParameter("oneClassId");
		String statusString = req.getParameter("status");
		String reason = req.getParameter("reason");
		String classDateString = req.getParameter("classDate");
		Date classDate = Util.parseDate(classDateString);
		
		int oneClassId = Integer.parseInt(oneClassIdString);
		ClassState classState = ClassState.valueOf(statusString);
		
		ClassLogManager classLogManager = (ClassLogManager) applicationContext.getBean("classLogManager");
		classLogManager.registerClassLogTransaction(teacher, oneClassId, classDate, classState, reason);
		
		resp.setStatus(200);
		resp.setHeader("Content-Type", "text/plain");
		resp.getWriter().write("Successfully Reported to Company.");
	}

}
