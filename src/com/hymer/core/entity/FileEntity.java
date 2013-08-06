package com.hymer.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hymer.core.BaseEntity;

@Entity
@Table(name = "tb_file_file")
//@JsonIgnoreProperties("owner")
public class FileEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String TYPE_NOTICE = "notice";
	
	@Column(length = 100)
	private String attachmentType; // 附件类型，如：notice等
	@Column(length = 1024)
	private String fileName; // 附件原名，如：测试文档.pdf
	@Column(length = 20)
	private String fileType; // 后缀名，如：.pdf
	@Column(length = 200)
	private String contentType; // 文件类型，如：application/pdf，image/png，application/vnd.ms-excel，application/vnd.openxmlformats-officedocument.spreadsheetml.sheet等
	@Column(length = 1024)
	private String realPath; // 上传后的真实路径，如：D:/uploads/2012/8/28/1234234243.pdf
	private long size; // 文件大小
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User owner; //文件拥有者，即上传者

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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
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

}
