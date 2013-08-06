package com.hymer.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.hymer.core.BaseEntity;

@Entity
@Table(name = "tb_security_resource")
public class Resource extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public Resource() {
		super();
	}

	public Resource(String name, String url) {
		this();
		this.name = name;
		this.url = url;
	}
	public Resource(String name, String url, String type) {
		this(name, url);
		this.type = type;
	}

	@Length(min = 1, max = 200)
	@Column(length = 200)
	private String name;
	@Length(min = 1, max = 50)
	@Column(length = 50)
	private String type;
	@Range(min = 0, max = 9)
	@Column
	private int priority = 0;
	@Length(min = 1, max = 1024)
	@Column(length = 1024)
	private String url;
	@Column(length = 1024)
	private String description;
	@Column
	private boolean disabled = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
