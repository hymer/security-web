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

import sun.misc.BASE64Encoder;

import com.hymer.core.Configuration;

public abstract class MailSender {
	private static Log log = LogFactory.getLog(MailSender.class);

	private Properties properties = null;
	private static MailSender INSTANCE = null; // 默认实例

	/**
	 * 创建Session所需的一些配置信息
	 * 
	 * @return
	 */
	public abstract Properties initProperties();

	public static MailSender getDefaultInstance() {
		try {
			if (INSTANCE == null) {
				String senderClass = Configuration.get("mail.sender");
				if (senderClass != null && senderClass.trim().length() > 0) {
					INSTANCE = (MailSender) Class.forName(senderClass)
							.newInstance();
				} else {
					INSTANCE = new GMailSSLSender();
				}
			}
		} catch (Exception e) {
			log.error("Initialize MailSender Failed!", e);
		}
		return INSTANCE;
	}

	public void send(MailBean mail) {
		if (properties == null) {
			properties = initProperties();
		}
		final String userName = properties.getProperty("mail.username");
		final String password = properties.getProperty("mail.password");
		// 如果只使用一个邮箱发送邮件，推荐使用Session.getDefaultInstance
		// 如果可能用多个邮箱发送邮件，必须使用Session.getInstance
		// 因为getDefaultInstance会读取缓存的Properties
		Session session = Session.getDefaultInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName, password);
					}
				});
		try {
			Message message = new MimeMessage(session);
			String from = properties.getProperty("mail.from");
			message.setFrom(new InternetAddress(
					StringUtils.hasText(from) ? from : userName));
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
			mimeBodyPart.setContent(mail.getContent(),
					"text/html; charset=utf-8");
			multipart.addBodyPart(mimeBodyPart);
			List<String> fileList = mail.getAttachments();
			// 加入附件
			for (String filePath : fileList) {
				MimeBodyPart bodyPart = new MimeBodyPart();
				// 得到数据源
				FileDataSource fileDataSource = new FileDataSource(filePath);
				// 附件如果不存在，抛异常
				if (!fileDataSource.getFile().exists()) {
					throw new RuntimeException("The attachment file["
							+ filePath + "] not exists.");
				}
				bodyPart.setDataHandler(new DataHandler(fileDataSource));
				bodyPart.setDisposition(Part.ATTACHMENT);
				// 处理中文
				BASE64Encoder encoder = new BASE64Encoder();
				String attachName = "=?UTF-8?B?"
						+ encoder.encode(fileDataSource.getName().getBytes())
						+ "?=";
				bodyPart.setFileName(attachName);
				multipart.addBodyPart(bodyPart);
			}
			message.setContent(multipart);
			Transport.send(message);
			log.info("Mail has been sent successfully.");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
