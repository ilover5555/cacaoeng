package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.HttpServletDataGetterFromUpdate;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.UploadedFile;

@SuppressWarnings("serial")
@WebServlet("/teacher/changeTeacherInfo.do")
@MultipartConfig
public class ChangeTeacherInfo extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
    	TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		HttpServletDataGetterFromUpdate dataGetter = null;
		try{
			dataGetter = new HttpServletDataGetterFromUpdate(req);
		}catch(LoginRequiredException e){
			resp.sendRedirect("./log_in.jsp");
		}
		
    	Teacher teacher = (Teacher) dataGetter.makeTeacherInstance();
    	if(!dataGetter.user.getId().equals(teacher.getId())){
    		Util.sendError(401, resp, "Cannot modify");
    		return;
    	}
		List<String> messages = dataGetter.getMessages();
		
		if(!messages.isEmpty())
		{
			req.setAttribute("messages", messages);
			req.setAttribute("user", teacher);
			req.getRequestDispatcher("./myprofile.jsp").forward(req, resp);
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
		req.getSession().setAttribute("teacher", teacher);
		
		req.setAttribute("msg", "Successfully update your information.");
		req.setAttribute("url", "./index.jsp");
		req.getRequestDispatcher("./dialog.jsp").forward(req, resp);
	}
}
