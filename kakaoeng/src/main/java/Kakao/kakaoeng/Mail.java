package Kakao.kakaoeng;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
	public static final String username = "proinfotec@naver.com";// change
																	// accordingly
	public static final String password = "unrainy5234";// change
																// accordingly

	public static void send(String from, String toS, String subject, String text) {

		/*
		 * // Assuming you are sending email through relay.jangosmtp.net String
		 * host = "smtp.gmail.com";
		 * 
		 * Properties props = new Properties(); props.put("mail.smtp.auth",
		 * "true"); props.put("mail.smtp.starttls.enable", "true");
		 * props.put("mail.smtp.host", host); props.put("mail.smtp.port",
		 * "587");
		 * 
		 * // Get the Session object. Session session =
		 * Session.getInstance(props, new javax.mail.Authenticator() { protected
		 * PasswordAuthentication getPasswordAuthentication() { return new
		 * PasswordAuthentication(username, password); } });
		 * 
		 * try { // Create a default MimeMessage object. Message message = new
		 * MimeMessage(session);
		 * 
		 * // Set From: header field of the header. message.setFrom(new
		 * InternetAddress(from));
		 * 
		 * // Set To: header field of the header.
		 * message.setRecipients(Message.RecipientType.TO,
		 * InternetAddress.parse(to));
		 * 
		 * // Set Subject: header field message.setSubject(subject);
		 * 
		 * // Now set the actual message message.setText(text);
		 * 
		 * // Send message Transport.send(message);
		 * 
		 * System.out.println("Sent message successfully....");
		 * 
		 * } catch (MessagingException e) { throw new RuntimeException(e); }
		 */

		class SmtpAuthenticator extends Authenticator {
			public SmtpAuthenticator() {
				super();
			}

			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("proinfotec@naver.com", "unrainy5234");
			}
		}
		
		Properties props = System.getProperties();
		props.put("mail.smtp.user", "proinfotec");
		props.put("mail.smtp.host", "smtp.naver.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.debug", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		
		try {
			Session mailSession = Session.getInstance(props, null);
			Message msg = new MimeMessage(mailSession.getDefaultInstance(props, new SmtpAuthenticator()));

			InternetAddress to = new InternetAddress(toS); // 수신자 주소 생성
			msg.setFrom(new InternetAddress(from)); // 송신자 설정
			msg.setRecipient(javax.mail.Message.RecipientType.TO, to);
			msg.setSubject(subject); // 제목 설정
			msg.setSentDate(new java.util.Date()); // 보내는 날짜 설정
			msg.setContent(text, "text/html;charset=euc-kr"); // 내용 설정 (HTML 형식)

			Transport.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 수신자 설정
		

		
	}
}
