package com.hymer.core.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hymer.core.BaseEntity;
import com.hymer.core.DTO;
import com.hymer.core.entity.FileEntity;
import com.hymer.core.util.JsonDateTimeSerializer;

public class FileDTO extends DTO {

	private String attachmentType; // 附件类型，如：notice等
	private String fileName; // 附件原名，如：测试文档.pdf
	private String fileType; // 后缀名，如：.pdf
	private String contentType; // 文件类型，如：application/pdf，image/png，application/vnd.ms-excel，application/vnd.openxmlformats-officedocument.spreadsheetml.sheet等
	private String realPath; // 上传后的真实路径，如：D:/uploads/2012/8/28/1234234243.pdf
	private long size; // 文件大小

	private long id;
	private Date createTime;

	private String userName;

	public FileDTO() {

	}

	public FileDTO(FileEntity entity) {
		fromEntity(entity);
	}

	@Override
	public BaseEntity toEntity() {
		// TODO Auto-generated method stub
		return super.toEntity();
	}

	@Override
	public void fromEntity(BaseEntity entity) {
		FileEntity file = (FileEntity) entity;
		this.attachmentType = file.getAttachmentType();
		this.contentType = file.getContentType();
		this.createTime = file.getCreateTime();
		this.fileName = file.getFileName();
		this.fileType = file.getFileType();
		this.id = file.getId();
		this.realPath = file.getRealPath();
		this.size = file.getSize();
		this.userName = file.getOwner().getUserName();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
