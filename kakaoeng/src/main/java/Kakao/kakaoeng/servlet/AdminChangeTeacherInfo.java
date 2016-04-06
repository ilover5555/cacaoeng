package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.HttpServletDataGetterFromUpdate;
import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.UploadedFile;

@SuppressWarnings("serial")
@WebServlet("/teacher/adminChangeTeacherInfo.do")
@MultipartConfig
public class AdminChangeTeacherInfo extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String teacherId = req.getParameter("teacherId");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
    	TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		HttpServletDataGetterFromUpdate dataGetter = null;
		try{
			dataGetter = new HttpServletDataGetterFromUpdate(req, teacherDAO.getTeacherWithId(teacherId));
		}catch(RuntimeException e){
			Util.sendError(resp, e.getMessage());
			return;
		}
		
    	Teacher teacher = (Teacher) dataGetter.makeTeacherInstance();
		List<String> messages = dataGetter.getMessages();
		
		if(!messages.isEmpty())
		{
			req.setAttribute("messages", messages);
			req.setAttribute("teacher", teacher);
			req.getRequestDispatcher("./admin_change_teacher_info.jsp?teacherId="+teacherId).forward(req, resp);
			return;
		}
		
		String primaryPicture = teacherDAO.getTeacherWithId(teacher.getId()).getPrimaryProfilePicture();
		if(teacher.getPrimaryProfilePicture().equals("")){
			teacher.setPrimaryProfilePicture(primaryPicture);
		}else{
			UploadedFile f = new UploadedFile(primaryPicture);
			f.removeFileFromStorage(req.getServletContext().getRealPath("/"));
		}
		
		String primaryVoice = teacherDAO.getTeacherWithId(teacher.getId()).getPrimaryVoice();
		if(teacher.getPrimaryVoice().equals("")){
			teacher.setPrimaryVoice(primaryVoice);
		}else{
			UploadedFile f = new UploadedFile(primaryVoice);
			f.removeFileFromStorage(req.getServletContext().getRealPath("/"));
		}
		String spec = teacherDAO.getTeacherWithId(teacher.getId()).getSpecImage();
		if(teacher.getSpecImage().equals("")){
			teacher.setSpecImage(spec);
		}else{
			UploadedFile f = new UploadedFile(spec);
			f.removeFileFromStorage(req.getServletContext().getRealPath("/"));
		}
		
		teacherDAO.update(teacher);
		
		
		resp.sendRedirect("./adminChangeTeacherInfo.do?updated=true&teacherId="+teacherId);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String teacherId = req.getParameter("teacherId");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
    	TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
    	
    	Teacher teacher = teacherDAO.getTeacherWithId(teacherId);
    	if(teacher == null){
    		Util.sendError(resp, teacherId +" is invalid or dont exist.");
    		return;
    	}
    	
    	req.setAttribute("teacher", teacher);
    	boolean updated = Boolean.parseBoolean(req.getParameter("updated"));
    	if(updated){
    		List<String> result = new ArrayList<>();
    		result.add("Update Teacher's Information Success!!");
    		req.setAttribute("successes", result);
    	}
    	req.getRequestDispatcher("./admin_change_teacher_info.jsp?teacherId="+teacherId).forward(req, resp);
	}
	
	
}
