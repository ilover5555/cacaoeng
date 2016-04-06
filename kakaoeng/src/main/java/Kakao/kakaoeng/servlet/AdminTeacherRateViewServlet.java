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
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminTeacherRateList.view")
public class AdminTeacherRateViewServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		
		List<Teacher> teacherList = teacherDAO.getActivedTeacherList();
		
		List<Teacher> A = new ArrayList<>();
		List<Teacher> B = new ArrayList<>();
		List<Teacher> C = new ArrayList<>();
		List<Teacher> Wait = new ArrayList<>();
		
		for(Teacher teacher :teacherList){
			switch(teacher.getRate()){
			case A:
				A.add(teacher);
				break;
			case B:
				B.add(teacher);
				break;
			case C:
				C.add(teacher);
				break;
			case Wait:
				Wait.add(teacher);
				break;
			default:
				throw new IllegalStateException(teacher.getId() + "'s rate is invalid : " + teacher.getRate());
			}
		}
		
		req.setAttribute("A", A);
		req.setAttribute("B", B);
		req.setAttribute("C", C);
		req.setAttribute("Wait", Wait);
		
		req.getRequestDispatcher("./admin_teacher_rate_viewer.jsp").forward(req, resp);
		
	}

}
