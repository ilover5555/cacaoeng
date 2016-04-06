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
@WebServlet("/teacher/adminAccountUpdate.do")
public class AdminAccountUpdateServlet extends HttpServlet {

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String id = req.getParameter("id");
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		AdminDAO adminDAO = (AdminDAO) applicationContext.getBean("adminDAO");
		Admin admin = adminDAO.findAdminById(id);
		req.setAttribute("manager", admin);
		req.getRequestDispatcher("./admin_account_update.jsp").forward(req, resp);
	}

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
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		AdminDAO adminDAO = (AdminDAO) applicationContext.getBean("adminDAO");
		Admin admin = adminDAO.findAdminById(id);
		
		if(pw.equals("")){
			pw = admin.getPw();
		}else{
			pw = DigestUtils.sha256Hex(pw);
		}
		
		Admin newManager = new Admin(id, pw, name, cellPhone, address, null, null, userType);
		adminDAO.update(newManager);
		
		Util.sendSuccess(resp, "정보를 성공적으로 변경하였습니다.");
	}

}
