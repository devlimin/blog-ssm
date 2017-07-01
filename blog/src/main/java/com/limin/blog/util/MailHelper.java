package com.limin.blog.util;

import java.net.InetAddress;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHelper  implements Runnable {

	private String host = "smtp.163.com";
	private String email = "devlimin@163.com";
	private String username = "devlimin";
	private String password = "123limin";
	
	private String userCode;
	private String userEmail;
	public MailHelper(String userCode, String userEmail) {
		this.userCode = userCode;
		this.userEmail = userEmail;
	}
	
	@Override
	public void run() {
		try {
			sendMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMail() {
		try {
			Properties prop = new Properties();
			prop.setProperty("mail.transport.protocol", "smtp");
			prop.setProperty("mail.smtp.auth", "true");
			Session session = Session.getInstance(prop);
			session.setDebug(true);

			Message message = createMessage(session, userCode,userEmail);

			Transport ts = session.getTransport();
			ts.connect(host, username, password);
			ts.sendMessage(message, message.getAllRecipients());
			ts.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Message createMessage(Session session, String userCode, String userEmail) {
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
			message.setSubject("LM BLOG 用户注册邮件");
			String content = "恭喜您注册成功，请点击下面的超链接激活账号<br />";
			content += "<a href='http://" + InetAddress.getLocalHost().getHostAddress()
					+ ":8080/user/auth?code=" + userCode + "'>"
					+ "http://" + InetAddress.getLocalHost().getHostAddress()
					+ ":8080/user/auth?code=" + userCode
					+ "</a>";
			message.setContent(content, "text/html;charset=UTF-8");
			message.saveChanges();

			return message;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
