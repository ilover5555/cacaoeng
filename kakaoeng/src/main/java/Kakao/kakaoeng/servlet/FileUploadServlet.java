package Kakao.kakaoeng.servlet;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import Kakao.kakaoeng.FileSaver;
import Kakao.kakaoeng.Util;

@SuppressWarnings("serial")
@WebServlet("/uploadFile.do")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

	static Logger logger = Logger.getLogger(FileUploadServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file = "\\Error";
		for(Part part : req.getParts()){
			file = "\\Error : " + Util.getPartFileName(part);
			String submittedName = Util.getPartFileName(part);
			if(submittedName.length() > 200)
				submittedName = submittedName.substring(0, 201);
			logger.info("["+req.getRemoteAddr()+"]"+"submittedName : " + Util.getPartFileName(part) + ",size : " + part.getSize());
			try{
				file = FileSaver.fileSave(part, submittedName, "uploaded_file", new Date().getTime(), true, 2*1024*1024);
			}catch(RuntimeException e){
				logger.error(e.getMessage(), e);
				Util.sendError(resp, "File Size Exceed");
				return;
			}
		}
		resp.setStatus(200);
		resp.setContentType("text/plain");
		resp.getWriter().write(file);
	}
}
