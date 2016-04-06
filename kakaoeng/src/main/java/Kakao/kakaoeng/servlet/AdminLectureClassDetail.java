package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.Count;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminLectureClassDetail.edit")
public class AdminLectureClassDetail extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		int purchaseNumber = -1;
		int lectureId = -1;
		try{
			purchaseNumber = Integer.parseInt(req.getParameter("purchaseNumber"));
			lectureId = Integer.parseInt(req.getParameter("lectureId"));
		}catch(RuntimeException e){}
		if(purchaseNumber == -1 || lectureId == -1){
			Util.sendError(resp, "Invalid purchase Number : " + req.getParameter("purchaseNumber") + ", lectureId : " + req.getParameter("lectureId"));
			return;
		}
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		
		
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		Lecture lecture = lectureDAO.getLectureByIdTransaction(lectureId);
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		
		List<ClassLog> result = new ArrayList<>();
		List<ClassLog> classLogList = classLogDAO.getAllClassLogListByLectureId(lecture.getId());
		for(ClassLog classLog : classLogList)
			classLog.loadOneClass(oneClassDAO);
		result.addAll(classLogList);
		
		Collections.sort(result);
		
		Count count = new Count();
		count.setIndex(1);
		req.setAttribute("classLogList", result);
		req.setAttribute("count", count);
		
		req.getRequestDispatcher("./admin_lecture_class_detail.jsp").forward(req, resp);
	}

}
