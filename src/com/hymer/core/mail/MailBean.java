package com.hymer.core.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮件Bean.
 * 
 * @author hymer
 * 
 */
public class MailBean {

	// 邮件接收者，如果是多人，以,隔开
	private String toAddress = null;
	private String ccAddress = null;
	private String bccAddress = null;

	// 邮件主题
	private String subject = "No Subject";
	// 邮件内容
	private String content = null;
	// 附件路径列表
	private List<String> attachments = new ArrayList<String>();

	public void addAttachments(String... files) {
		if (files != null && files.length > 0) {
			for (String filePath : files) {
				attachments.add(filePath);
			}
		}
	}

	public List<String> getAttachments() {
		return attachments;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String getBccAddress() {
		return bccAddress;
	}

	public void setBccAddress(String bccAddress) {
		this.bccAddress = bccAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content
				+ "<div style='clear:both;'><br/><br/><br/>----------------------------------------<br/><font color='green'>系统邮件，请勿回复。</font></div>";
	}

	public void setContent(String content) {
		this.content = content;
	}

}
