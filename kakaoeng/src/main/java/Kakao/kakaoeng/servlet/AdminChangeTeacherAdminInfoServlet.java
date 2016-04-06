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

import Kakao.kakaoeng.FieldSet;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.Teacher.Quality;
import Kakao.kakaoeng.domain.model.Teacher.Rate;
import Kakao.kakaoeng.field.Field;
import Kakao.kakaoeng.validator.MaxLengthValidator;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminChangeTeacherAdminInfo.do")
public class AdminChangeTeacherAdminInfoServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		
		String teacherId = req.getParameter("teacherId");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		Teacher teacher = teacherDAO.getTeacherWithId(teacherId);
		if(teacher == null){
			Util.sendError(resp, teacherId + " is invalid or dont exist.");
			return;
		}
		
		Util.debugRequstParameter(req);
		
		int salary = Integer.parseInt(req.getParameter("salary"));
		Quality pronunciation = Quality.valueOf(req.getParameter("pronunciation"));
		Quality accent = Quality.valueOf(req.getParameter("accent"));
		String briefDescription = req.getParameter("briefDescription");
		String comment = req.getParameter("comment");
		Rate rate = Rate.valueOf(req.getParameter("rate"));
		boolean representitive = Boolean.parseBoolean(req.getParameter("representitive"));
		
		Field<String> commentField = new Field<String>("comment", comment);
		commentField.addValidator(new MaxLengthValidator<>(300));
		
		FieldSet fieldSet = new FieldSet(true);
		fieldSet.addField(commentField);
		
		List<String> msg = new ArrayList<>();
		
		msg.addAll(fieldSet.validateAllField());
		
		if(!msg.isEmpty())
		{
			req.setAttribute("messages", msg);
			req.setAttribute("user", teacher);
			req.getRequestDispatcher("./admin_change_teacher_info.jsp?teacherId="+teacherId).forward(req, resp);
			return;
		}
		
		teacherDAO.updateForAdmin(teacherId, salary, pronunciation, accent, comment);
		teacherDAO.updateRate(teacherId, rate);
		teacherDAO.updateRepresentitive(teacherId, representitive);
		
		resp.sendRedirect("./adminChangeTeacherInfo.do?updated=true&teacherId="+teacherId);
	}

}
