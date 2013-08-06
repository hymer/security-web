package com.hymer.core.mail;

import java.util.Properties;

import com.hymer.core.Configuration;

/**
 * 邮件发送的Gmail TLS实现。
 * 
 * @author hymer
 * 
 */
public class GmailSenderTLS extends MailSender {

	@Override
	public Properties prepare() {
		Properties props = new Properties();
		props.put("mail.smtp.host", Configuration.get("mail.smtp.host"));
		props.put("mail.smtp.starttls.enable", Configuration.get("mail.smtp.starttls.enable"));
		props.put("mail.smtp.auth", Configuration.get("mail.smtp.auth"));
		props.put("mail.smtp.port", Configuration.get("mail.smtp.port"));
		return props;
	}
	
}
