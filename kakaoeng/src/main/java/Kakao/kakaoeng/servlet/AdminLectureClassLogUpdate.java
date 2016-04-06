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
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminLectureClassLogUpdate.do")
public class AdminLectureClassLogUpdate extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String type = req.getParameter("type");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		ClassLogManager classLogManager = (ClassLogManager) applicationContext.getBean("classLogManager");
		if(type == null || type.equals("") || type.equals("modify")){	
			int classLogId = -1;
			ClassState classState =null;
			String reason = null;
			
			try{
				classLogId = Integer.parseInt(req.getParameter("classLogId"));
				classState = ClassState.valueOf(req.getParameter("classState"));
				reason = req.getParameter("reason");
			}catch(RuntimeException e){}
			
			ClassLog classLog = classLogDAO.getByIdTransaction(classLogId);
			
			if(classLogId == -1 || classState == null || reason == null){
				Util.sendError(resp, "Invalid ClassLogUpdate argument \n classLogId : " + classLogId + "\nclassState : " + classState + "\nreason:"+reason);
				return;
			}
			
			if(classLog == null){
				Util.sendError(resp, "No Class Log Matched With ClassLogId : " + classLogId);
				return;
			}
			
			classLogManager.registerClassLogTransaction(classLog.getOneClassId(), classLog.getClassDate(), classState, reason);
			
			Util.sendSuccess(resp, "Successfully updated");
			return;
		}else if(type.equals("new")){
			Date classDate = Util.parseDate(req.getParameter("baseDate"));
			ClassState classState = ClassState.valueOf(req.getParameter("classState"));
			int oneClassId = Integer.parseInt(req.getParameter("oneClassId"));
			
			classLogManager.registerClassLogTransaction(oneClassId, classDate, classState, "");
			Util.sendSuccess(resp, "성공적으로 상태를 적용하였습니다.");
		}
		
	}

}
