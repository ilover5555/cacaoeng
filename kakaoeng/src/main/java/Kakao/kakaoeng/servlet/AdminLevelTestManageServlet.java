package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Student;


@WebServlet("/teacher/adminLevelTestManage.view")
public class AdminLevelTestManageServlet extends HttpServlet {

	public static class LevelTestVO{
		Purchase purchase;
		Student student;
		Lecture lecture;
		OneClass oneClass;
		public Purchase getPurchase() {
			return purchase;
		}
		public void setPurchase(Purchase purchase) {
			this.purchase = purchase;
		}
		public Student getStudent() {
			return student;
		}
		public void setStudent(Student student) {
			this.student = student;
		}
		public Lecture getLecture() {
			return lecture;
		}
		public void setLecture(Lecture lecture) {
			this.lecture = lecture;
		}
		public OneClass getOneClass() {
			return oneClass;
		}
		public void setOneClass(OneClass oneClass) {
			this.oneClass = oneClass;
		}
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		List<Lecture> levelTestList = lectureDAO.getAllLevelTestLectureTransaction();
		List<LevelTestVO> result = new ArrayList<>();
		for(Lecture lecture : levelTestList){
			LevelTestVO vo = new LevelTestVO();
			vo.setLecture(lecture);
			vo.setStudent(studentDAO.findStudentById(lecture.getStudentId()));
			vo.setPurchase(purchaseDAO.getPurchaseById(lecture.getPurchaseNumber()));
			vo.setOneClass(oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId()).get(0));
			result.add(vo);
		}
		
		req.setAttribute("levelTestVOList", result);
		
		req.getRequestDispatcher("./admin_level_test_status.jsp").forward(req, resp);
	}

}
