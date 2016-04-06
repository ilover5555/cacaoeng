package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.ClassCountVO;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.Teacher.Rate;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminTeacherBasic.view")
public class AdminTeacherBasicServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		
		
		String search = req.getParameter("searchText");
		if(search == null)
			search="";
		List<Teacher> teacherList = teacherDAO.searchActiveTeacherByClassName(search);
		
		Map<String, ClassCountVO> map = new HashMap<>();
		int A = 0;
		int B = 0;
		int C = 0;
		int Wait = 0;
		for(Teacher teacher : teacherList){
			switch(teacher.getRate()){
			case A:
				A++;
				break;
			case B:
				B++;
				break;
			case C:
				C++;
				break;
			case Wait:
				Wait++;
				break;
			default:
				break;
			}
			int full=0;
			int done=0;
			List<Purchase> purchaseList = purchaseDAO.getPurchaseListWithTeacherClassName(teacher.getClassName());
			for(Purchase purchase : purchaseList){
				List<Lecture> lectureList = lectureDAO.getLectureListByPurchase(purchase.getId());
				for(Lecture lecture : lectureList){
					if(lecture.getTeacherId().equals(teacher.getId())){
						full++;
					}
				}
				for(Lecture lecture : lectureList){
					if(lecture.getEndDate() != null)
						done++;
				}
			}
			map.put(teacher.getId(), new ClassCountVO(full, done));
		}
		
		Map<Rate, Integer> countMap = new EnumMap<>(Rate.class);
		
		for(Rate rate : Rate.values()){
			int count = teacherDAO.findTeacherCountByRate(rate);
			countMap.put(rate, count);
		}
		
		req.setAttribute("active", teacherList);
		req.setAttribute("classCountSet", map);
		req.setAttribute("countMap", countMap);
		
		req.setAttribute("A", A);
		req.setAttribute("B", B);
		req.setAttribute("C", C);
		req.setAttribute("Wait", Wait);
		
		req.getRequestDispatcher("./admin_teacher_basic.jsp").forward(req, resp);
	}

}
