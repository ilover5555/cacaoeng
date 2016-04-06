package Kakao.kakaoeng.servlet;

import java.io.IOException;
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
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminTeacherLectureList.view")
public class AdminTeacherLectureListServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin() && !lc.getExecLogin())
			throw new LoginRequiredException("./index.jsp");
		String teacherId = req.getParameter("teacherId");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		List<Lecture> lectureList = lectureDAO.getLectureListByTeacherId(teacherId);
		for(Lecture lecture : lectureList){
			lecture.loadStudent(studentDAO);
			List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			for(OneClass oneClass : oneClassList){
				List<ClassLog> classLogList = classLogDAO.getAllClassLogListByOneClassTransaction(oneClass.getId());
				oneClass.setDone(classLogList);
			}
			lecture.setDone(oneClassList);
		}
		
		Count count = new Count();
		count.setIndex(1);
		req.setAttribute("lectureList", lectureList);
		req.setAttribute("count", count);
		
		req.getRequestDispatcher("./admin_teacher_lecture_list.jsp").forward(req, resp);
	}

	
}
