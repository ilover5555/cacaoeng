package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.ClassSearchUnit;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.MatchLibrary;
import Kakao.kakaoeng.TeacherWithCount;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminTeacherSmartMatch.do")
public class AdminTeacherSmartMatch extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		int purchaseNumber = -1;
		try{purchaseNumber = Integer.parseInt(req.getParameter("purchaseNumber"));}catch(RuntimeException e){}
		if(purchaseNumber == -1){
			Util.sendError(resp, "Invalid purchase Number : " + req.getParameter("purchaseNumber"));
			return;
		}
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		MatchLibrary match = new MatchLibrary();
		List<ClassSearchUnit> findList = new ArrayList<>();
		
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		Lecture lecture = lectureDAO.getOnGoingLectureByPurchase(purchaseNumber);
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		
		for(OneClass oneClass : oneClassList){
			List<ClassLog> classLogList = classLogDAO.getAllClassLogListByParent(oneClass.getParent());
			oneClass.setDone(classLogList);
			ClassSearchUnit csu = new ClassSearchUnit(
					new Duration(oneClass.getDuration().getRt().getStamp(), oneClass.getDuration().getDuration()),
					new Date(), (lecture.getFullClass()/4)-oneClass.getDone());
			findList.add(csu);
		}

		
		final Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> exactResult = new TreeMap<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>>();
		final Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> similarResult = new TreeMap<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>>();

		match.process(exactResult, similarResult, applicationContext, findList);

		req.setAttribute("exactMap", exactResult);
		req.setAttribute("similarMap", similarResult);
		req.getRequestDispatcher("./TeacherMatchingResult.jsp").include(req, resp);
	}

}
