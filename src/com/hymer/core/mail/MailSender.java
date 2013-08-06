package com.hymer.core.mail;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.hymer.core.Configuration;

public abstract class MailSender {
	Log log = LogFactory.getLog(getClass());

	private static MailSender defaultInstance;

	public static MailSender getDefaultInstance() {
		String senderClass = Configuration.get("mail.sender");
		if (defaultInstance == null
				|| (!defaultInstance.getClass().getName().equals(senderClass))) {
			defaultInstance = getMailSenderByClassName(senderClass);
		}
		if (defaultInstance == null) {
			defaultInstance = new GmailSenderSSL();
		}
		return defaultInstance;
	}

	private static MailSender getMailSenderByClassName(String senderClass) {
		MailSender sender = null;
		try {
			sender = (MailSender) Class.forName(senderClass).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return sender;
	}

	public abstract Properties prepare();

	public void send(MailBean mail) {
		Properties props = prepare();

		final String userName = Configuration.get("mail.username");
		final String password = Configuration.get("mail.password");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName, password);
					}
				});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(StringUtils.hasText(mail
					.getFromAddress()) ? mail.getFromAddress() : userName));
			if (!StringUtils.hasText(mail.getToAddress())) {
				throw new RuntimeException("TO ADDRESS CAN NOT BE NULL.");
			}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(mail.getToAddress()));
			if (StringUtils.hasText(mail.getCcAddress())) {
				message.setRecipients(Message.RecipientType.CC,
						InternetAddress.parse(mail.getCcAddress()));
			}
			if (StringUtils.hasText(mail.getBccAddress())) {
				message.setRecipients(Message.RecipientType.BCC,
						InternetAddress.parse(mail.getBccAddress()));
			}
			message.setSubject(mail.getSubject());

			Multipart multipart = new MimeMultipart();
			// 加入文本内容
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			// 让其支持HTML内容
			mimeBodyPart
					.setContent(mail.getContent(), "text/html;charset=utf8");
			multipart.addBodyPart(mimeBodyPart);
			List<String> fileList = mail.getAttachments();
			// 加入附件
			for (String filePath : fileList) {
				MimeBodyPart bodyPart = new MimeBodyPart();
				// 得到数据源
				FileDataSource fileDataSource = new FileDataSource(filePath);
				bodyPart.setDataHandler(new DataHandler(fileDataSource));
				bodyPart.setDisposition(Part.ATTACHMENT);
				// 设置文件名
				bodyPart.setFileName(fileDataSource.getName());
				multipart.addBodyPart(bodyPart);
			}
			message.setContent(multipart);

			Transport.send(message);

			log.info("mail sended success.");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
