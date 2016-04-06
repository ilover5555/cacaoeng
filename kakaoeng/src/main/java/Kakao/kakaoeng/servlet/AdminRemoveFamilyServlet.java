package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.RemoveAllPurchaseManager;
import Kakao.kakaoeng.Util;

@WebServlet("/teacher/adminRemoveFamily.do")
public class AdminRemoveFamilyServlet extends HttpServlet {

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		int purchaseNumber = Integer.parseInt(req.getParameter("purchaseNumber"));
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		RemoveAllPurchaseManager removeAllPurchaseManager = (RemoveAllPurchaseManager) applicationContext.getBean("removeAllPurchaseManager");
		try{
			removeAllPurchaseManager.removeTransaction(purchaseNumber);
		}catch(RuntimeException e){
			Util.sendError(resp, "강의 "+purchaseNumber + "를 삭제하는데 실패했습니다.");
			return;
		}
		
		Util.sendSuccess(resp, "강의 "+purchaseNumber + "를 삭제하는데 성공했습니다.");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getAdminLogin())
			throw new LoginRequiredException("./index.jsp");
		String o =  req.getParameter("list");
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonObject = null;
		try {
			jsonObject = (JSONArray)jsonParser.parse(o);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Integer> purchaseList = new ArrayList<>();
		
		for(int i=0; i<jsonObject.size(); i++)
			purchaseList.add(Integer.parseInt((String) jsonObject.get(i)));
		
		
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		RemoveAllPurchaseManager removeAllPurchaseManager = (RemoveAllPurchaseManager) applicationContext.getBean("removeAllPurchaseManager");
		
		for(Integer i : purchaseList){
			try{
				removeAllPurchaseManager.removeTransaction(i);
			}catch(RuntimeException e){
				Util.sendError(resp, "강의 "+i+"의 삭제를 실패했습니다.");
				continue;
			}
			Util.sendSuccess(resp, "강의 "+i+"의 삭제를 성공하였습니다!!!");
		}
	}

}
