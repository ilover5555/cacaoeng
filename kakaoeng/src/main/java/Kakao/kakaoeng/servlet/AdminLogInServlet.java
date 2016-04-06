package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.dao.AdminDAO;
import Kakao.kakaoeng.domain.model.Admin;
import Kakao.kakaoeng.domain.model.User.UserType;

@WebServlet("/teacher/adminLogIn.do")
public class AdminLogInServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(lc.getAdminLogin() || lc.getExecLogin()){
			resp.sendRedirect("./index.jsp");
			return;
		}
		
		String id = req.getParameter("id");
		String pw = req.getParameter("pw");
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		AdminDAO adminDAO = (AdminDAO) applicationContext.getBean("adminDAO");
		
		Admin admin = adminDAO.findAdminByIdAndPw(id, DigestUtils.sha256Hex(pw));
		if(admin == null){
			List<String> messages = new ArrayList<>();
			messages.add("Log in Fail. Check ID and Password");
			req.setAttribute("messages", messages);
			req.getRequestDispatcher("./admin_log_in.jsp").forward(req, resp);
			return;
		}
		
		if(admin.getUserType().equals(UserType.Admin)){
			req.getSession().setAttribute("admin", admin);
			resp.sendRedirect("./index.jsp");
			return;
		}else if(admin.getUserType().equals(UserType.Executor)){
			req.getSession().setAttribute("executor", admin);
			resp.sendRedirect("./index.jsp");
			return;
		}else{
			throw new RuntimeException("AdminLogin Get Invalid UserType : " + admin.getUserType());
		}
	}

}
