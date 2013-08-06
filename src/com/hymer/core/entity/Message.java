package com.hymer.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.hymer.core.BaseEntity;

@Entity
@Table(name = "tb_message_message")
public class Message extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Length(max = 100)
	@Column(length = 100)
	private String type; // 类型
	@Length(max = 200)
	@Column(length = 200)
	private String title; // 标题
	@Length(max = 1024)
	@Column(length = 1024)
	private String content; // 内容
	@ManyToOne
	@JoinColumn(name = "fromUserId")
	private User fromUser; // 消息由谁发出
	@ManyToOne
	@JoinColumn(name = "toUserId")
	private User toUser; // 谁能收到消息
	private boolean systemMessage = false; // 是否系统消息
	private boolean readed = false; // 是否已读

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(boolean systemMessage) {
		this.systemMessage = systemMessage;
	}

	public boolean isReaded() {
		return readed;
	}

	public void setReaded(boolean readed) {
		this.readed = readed;
	}

}
