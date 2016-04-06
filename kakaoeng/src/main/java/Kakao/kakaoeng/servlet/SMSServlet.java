package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.SMS;
import Kakao.kakaoeng.whoisSMS;

@WebServlet("/sms.do")
public class SMSServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		SMS.sms(request.getParameter("sms_to"), request.getParameter("sms_msg"));
	}

}
