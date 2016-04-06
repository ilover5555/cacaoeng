package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.AdminDAO;
import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.User.UserType;

@MultipartConfig
@WebServlet("/teacher/adminAccountRegister.do")
public class AdminAccountRegisterServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		
		String id = req.getParameter("id");
		String pw = req.getParameter("pw");
		String name = req.getParameter("name");
		String cellPhone = req.getParameter("cellPhone");
		String address = req.getParameter("address");
		UserType userType = UserType.valueOf(req.getParameter("userType"));
		
		if(!(userType.equals(UserType.Admin) || userType.equals(UserType.Executor))){
			Util.sendError(resp, "AdminAccountRegisterServlet must get UserType Admin or Executor : " + userType);
			return;
		}
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		AdminDAO adminDAO = (AdminDAO) applicationContext.getBean("adminDAO");
		
		Admin admin = new Admin(id, DigestUtils.sha256Hex(pw), name, cellPhone, address, null, null, userType);
		adminDAO.register(admin);
		
		Util.sendSuccess(resp, "성공적으로 등록하였습니다.");
	}

}
