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

import Kakao.kakaoeng.ClassCountVO;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminTeacherRetireManage.do")
public class AdminTeacherRetireManageServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		
		String search = req.getParameter("searchText");
		if(search == null)
			search="";
		List<Teacher> retireList = teacherDAO.searchRetireTeacherByClassName(search);
		
		Map<String, ClassCountVO> map = new HashMap<>();
		for(Teacher teacher : retireList){
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
		
		req.setAttribute("retireList", retireList);
		req.setAttribute("classCountSet", map);
		
		req.getRequestDispatcher("./admin_teacher_retire_manage.jsp").forward(req, resp);
	}

}
