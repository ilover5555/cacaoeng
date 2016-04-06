package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.domain.model.Lecture;

@WebServlet("/teacher/adminSMSDifferSet.do")
public class AdminSMSDifferSetServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String type = req.getParameter("type");
		int differ = Integer.parseInt(req.getParameter("differ"));
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
		
		JSONObject obj = new JSONObject();
		List<Lecture> lectureList = null;
		if(type.equals("re")){
			environDAO.saveBeforeRequestLectureDiffer(differ);
			lectureList = AdminBeforeRequestLectureSMSServlet.getList(applicationContext, true);
			
		}else if(type.equals("re_after")){
			environDAO.saveAfterRequestLectureDiffer(differ);
			lectureList = AdminAfterRequestLectureSMSServlet.getList(applicationContext, true);
		}else{
			obj.put("result", "Invalid type for AdminSMSDifferSetServlet : " + type);
			obj.put("list", "");
			Util.sendError(resp, obj.toJSONString());
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<lectureList.size(); i++){
			sb.append(lectureList.get(i).getStudent().getName());
			sb.append("(");
			sb.append(lectureList.get(i).getStudent().getCellPhone());
			sb.append(")");
			if(i<lectureList.size()-1)
				sb.append(',');
		}
		obj.put("result", "성공적으로 변경하였습니다.");
		obj.put("list", sb.toString());
		Util.sendSuccess(resp, obj.toJSONString());
	}

}
