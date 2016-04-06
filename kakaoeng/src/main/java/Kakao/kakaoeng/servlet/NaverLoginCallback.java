package Kakao.kakaoeng.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import Kakao.kakaoeng.SMS;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.EnvironDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.dao.UserDAO;
import Kakao.kakaoeng.domain.model.NaverLoginResult;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Student.Level;
import Kakao.kakaoeng.domain.model.User.UserType;

@SuppressWarnings({ "serial", "deprecation" })
@WebServlet("/naverLogin.do")
public class NaverLoginCallback extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String state = req.getParameter("state");
		String storedState = (String) req.getSession().getAttribute("state");
		if(!state.equals(storedState)){
			req.getSession().removeAttribute("state");
			resp.setStatus(401);
			resp.setContentType("text/plain");
			resp.getWriter().write("Invalid Login State.");
			return;
		}
		String code = req.getParameter("code");
		
		URL url = new URL("https://nid.naver.com/oauth2.0/token?client_id=C1xqnuR9I8m9XBiJg0L2&client_secret=qqPtCFlEV1&grant_type=authorization_code&state="+state+"&code="+code);
		URLConnection con = url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		JSONParser parser = new JSONParser();
		JSONObject object = null;
		try {
			object = (JSONObject) parser.parse(br);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String accessToken = (String) object.get("access_token");
		//String refreshToken = (String) object.get("refresh_token");
		String tokenType = (String) object.get("token_type");
		//String expiresIn = (String) object.get("expires_in");
		
		URI infoUrl = null;
		try {
			infoUrl = new URI("https://openapi.naver.com/v1/nid/getUserProfile.xml");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		HttpGet httpGet = new HttpGet(infoUrl);
		httpGet.addHeader("Authorization", tokenType + " " + accessToken);
		
		@SuppressWarnings({"resource" })
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpGet);
		
		BufferedReader resultBr = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder sb = new StringBuilder();
		String inputLine = null;
		while((inputLine = resultBr.readLine()) != null)
			sb.append(inputLine);
		String xml = sb.toString();
		
		InputSource is = new InputSource(new StringReader(xml));
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		document.getDocumentElement().normalize();
		NaverLoginResult naverLogin = new NaverLoginResult(document);
		
		
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		UserDAO userDAO = (UserDAO) applicationContext.getBean("userDAO");
		Student student = studentDAO.findStudentById("naver:" + naverLogin.getEmail());
		if(student == null){
			student = new Student("naver:"+naverLogin.getEmail(), DigestUtils.sha256Hex(naverLogin.getEncId()), "", naverLogin.getNickName(), naverLogin.getBirthday(), 
					UserType.Student, naverLogin.getGender(),"", "", "", "", "", naverLogin.getProfileImage(), null, null, "", "", Level.Untested, "", "", 0, naverLogin.getEmail());
			studentDAO.register(student);
			EnvironDAO environDAO = (EnvironDAO) applicationContext.getBean("environDAO");
			String mode = environDAO.getNewRegisterSMSMode();
			if(mode.equals("auto")){
				String smsMsg = Util.fillContent(environDAO.getNewRegisterSMSMessage(), student);
				SMS.sms(student.getCellPhone(), smsMsg);
			}
		}
		req.getSession().setAttribute("student", student);
		
		userDAO.updateLastLogin(student.getId(), student.getPw(), student.getUserType());
		
		resp.sendRedirect("./refresh_parent.jsp");
	}

}
