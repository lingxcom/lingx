package com.lingx.plugin.email.send;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
@Component(value="lingxEmailSendService")
public class EmailSendService implements IEmailSendService {
	private String host;
	private String username;
	private String password;

	public boolean sendText(String email[], String title, String text) {
		try {
			JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
			// 设定mail server
			senderImpl.setHost(this.host);

			// 建立邮件消息
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			// 设置收件人，寄件人 用数组发送多个邮件
			// String[] array = new String[]
			// {"sun111@163.com","sun222@sohu.com"};
			// mailMessage.setTo(array);
			mailMessage.setTo(email);
			mailMessage.setFrom(IDNMailHelper.toIdnAddress(this.username));
			mailMessage.setSubject(title);
			mailMessage.setText(text);

			senderImpl.setUsername(this.username); // 根据自己的情况,设置username
			senderImpl.setPassword(this.password); // 根据自己的情况, 设置password

			Properties prop = new Properties();
			prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
			prop.put("mail.smtp.timeout", "25000");
			senderImpl.setJavaMailProperties(prop);
			// 发送邮件
			// senderImpl.send(mailMessage);
			senderImpl.send(mailMessage);
			//System.out.println(" 邮件发送成功.. ");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendHtml(String email[], String title, String html) {
		try {
			JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

			// 设定mail server
			senderImpl.setHost(this.host);

			// 建立邮件消息,发送简单邮件和html邮件的区别
			MimeMessage mailMessage = senderImpl.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);

			// 设置收件人，寄件人
			messageHelper.setTo(email);
			messageHelper.setFrom(this.username);
			messageHelper.setSubject(title);
			// true 表示启动HTML格式的邮件
			messageHelper.setText(html, true);
			senderImpl.setUsername(this.username); // 根据自己的情况,设置username
			senderImpl.setPassword(this.password); // 根据自己的情况, 设置password
			Properties prop = new Properties();
			prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
			prop.put("mail.smtp.timeout", "25000");
			senderImpl.setJavaMailProperties(prop);
			// 发送邮件
			senderImpl.send(mailMessage);

			//System.out.println("邮件发送成功..");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean sendText(String email, String title, String text) {
		return this.sendText(new String[]{email}, title, text);
	}

	public boolean sendHtml(String email, String title, String html) {
		return this.sendHtml(new String[]{email}, title, html);
	}

}
