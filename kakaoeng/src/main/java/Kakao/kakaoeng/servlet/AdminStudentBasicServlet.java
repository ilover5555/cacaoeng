package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Lecture.Status;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminStudentBasic.view")
public class AdminStudentBasicServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		
		int onGoing = 0;
		int hasLog = 0;
		
		String searchMethod=req.getParameter("searchMethod");
		String searchText=req.getParameter("searchText");
		
		if(searchMethod == null)
			searchMethod = "";
		
		if(searchText == null)
			searchText = "";
		
		List<Student> studentList = null;
		
		switch(searchMethod){
		case "name":
			studentList = studentDAO.findStudentByName(searchText);
			break;
		default:
			studentList = studentDAO.getAllStudentList();
			break;
		}
		
		
		Map<String, Integer> countMap = new HashMap<>();
		Map<String, String> stateMap = new HashMap<>();
		for(Student student : studentList){
			List<Purchase> purchaseList = purchaseDAO.getPurchaseListWithStudentId(student.getId());
			int classCount = 0;
			
			for(Purchase purchase : purchaseList){
				if(purchase.getRejected())
					continue;
				classCount+=purchase.getFullClass();
			}
			
			countMap.put(student.getId(), classCount);
			
			if(purchaseList.size() == 0){
				stateMap.put(student.getId(), "");
				continue;
			}
			
			List<Lecture> lectureList = lectureDAO.getLectureListByStudentId(student.getId());
			
			for(Lecture lecture : lectureList){
				if(lecture.getStatus().equals(Status.OnGoing)){
					onGoing++;
					stateMap.put(student.getId(), "수업중");
					break;
				}
			}
			
			if(!stateMap.containsKey(student.getId())){
				hasLog++;
				stateMap.put(student.getId(), "종료");
			}
		}
		
		req.setAttribute("studentList", studentList);
		req.setAttribute("countMap", countMap);
		req.setAttribute("stateMap", stateMap);
		
		req.setAttribute("totalStudent", studentList.size());
		req.setAttribute("onGoing", onGoing);
		req.setAttribute("hasLog", hasLog);
		
		req.getRequestDispatcher("./admin_student_basic.jsp").forward(req, resp);
	}

}
