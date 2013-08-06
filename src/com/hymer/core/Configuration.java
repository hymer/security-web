package com.hymer.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hymer.core.mail.GmailSenderSSL;

@Component
public class Configuration {

	private static final Map<String, String> configs = new HashMap<String, String>();
	
	public static Map<String, String> getInitConfigs() {
		//upload
		put("upload.path", "C:/uploads/");

		//mail
		put("mail.sender", GmailSenderSSL.class.getName());
		put("mail.username", "test.hymer@gmail.com");
		put("mail.password", "hymer.test");
		
		//gmail ssl
		put("mail.smtp.host", "smtp.gmail.com");
		put("mail.smtp.socketFactory.port", "465");
		put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		put("mail.smtp.auth", "true");
		put("mail.smtp.port", "465");
		
		/*
		<!-- gmail tls configuration 
		<mail.smtp.host>smtp.gmail.com</mail.smtp.host>
		<mail.smtp.starttls.enable>true</mail.smtp.starttls.enable>
		<mail.smtp.auth>true</mail.smtp.auth>
		<mail.smtp.port>587</mail.smtp.port>
		-->

		<!-- qq mail configuration 
		<mail.smtp.host>smtp.qq.com</mail.smtp.host>
		<mail.smtp.auth>true</mail.smtp.auth>
		<mail.smtp.port>25</mail.smtp.port>
		-->

		<!-- 163 mail configuration 
		<mail.smtp.host>smtp.163.com</mail.smtp.host>
		<mail.smtp.auth>true</mail.smtp.auth>
		<mail.smtp.port>25</mail.smtp.port>
		-->
		*/
		
		
		//register
		put("register.default.role", "_default_role_");
		
		return configs;
	}
	
	public static final Map<String, String> getConfigMap() {
		return configs;
	}
	
	public synchronized static final void clear() {
		configs.clear();
	}

	public synchronized static final String get(String key) {
		return configs.get(key);
	}
	
	public synchronized static final void put(String key, String value) {
		configs.put(key, value);
	}

}
