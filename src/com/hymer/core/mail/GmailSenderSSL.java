package com.hymer.core.mail;

import java.util.Properties;

import com.hymer.core.Configuration;

/**
 * 邮件发送的Gmail SSL实现。
 * 
 * @author hymer
 * 
 */
public class GmailSenderSSL extends MailSender {

	@Override
	public Properties prepare() {
		Properties props = new Properties();
		props.put("mail.smtp.host", Configuration.get("mail.smtp.host"));
		props.put("mail.smtp.socketFactory.port",
				Configuration.get("mail.smtp.socketFactory.port"));
		props.put("mail.smtp.socketFactory.class",
				Configuration.get("mail.smtp.socketFactory.class"));
		props.put("mail.smtp.auth", Configuration.get("mail.smtp.auth"));
		props.put("mail.smtp.port", Configuration.get("mail.smtp.port"));
		return props;
	}
	
}
