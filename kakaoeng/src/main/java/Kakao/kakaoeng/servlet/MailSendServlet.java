package Kakao.kakaoeng.servlet;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Kakao.kakaoeng.Mail;

@SuppressWarnings("serial")
@WebServlet("/mailSend.do")
public class MailSendServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String from = req.getParameter("from");
		String to = req.getParameter("to");
		String subject = req.getParameter("subject");
		String text = req.getParameter("text");
		
		Mail.send(Mail.username, to, subject, text);
	}

}
