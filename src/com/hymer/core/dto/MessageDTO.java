package com.hymer.core.dto;

import com.hymer.core.BaseEntity;
import com.hymer.core.DTO;
import com.hymer.core.entity.Message;
import com.hymer.core.util.Formatters;

public class MessageDTO extends DTO {

	private Long id;
	private String createTime;
	private String type; // 类型
	private String title; // 标题
	private String content; // 内容
	private Long fromUserId; // 消息由谁发出
	private String fromUserName; // 消息由谁发出
	private Long toUserId; // 谁能收到消息
	private String toUserName; // 谁能收到消息
	private boolean systemMessage; // 是否系统消息
	private boolean readed; // 是否已读
	private int flag; // 删除等标志

	public MessageDTO() {
	}

	public MessageDTO(Message message) {
		this.fromEntity(message);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public boolean isSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(boolean system) {
		this.systemMessage = system;
	}

	public boolean isReaded() {
		return readed;
	}

	public void setReaded(boolean read) {
		this.readed = read;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public BaseEntity toEntity() {
		// TODO Auto-generated method stub
		return super.toEntity();
	}

	@Override
	public void fromEntity(BaseEntity entity) {
		Message message = (Message) entity;
		this.setId(message.getId());
		this.setContent(message.getContent());
		this.setCreateTime(Formatters.formatTime(message.getCreateTime()));
		this.setFlag(message.getFlag());
		try {
			this.setFromUserId(message.getFromUser().getId());
			this.setFromUserName(message.getFromUser().getUserName());
			this.setToUserId(message.getToUser().getId());
			this.setToUserName(message.getToUser().getUserName());
		} catch (Exception e) {
		}
		this.setReaded(message.isReaded());
		this.setSystemMessage(message.isSystemMessage());
		this.setTitle(message.getTitle());
		this.setType(message.getType());
	}

}
