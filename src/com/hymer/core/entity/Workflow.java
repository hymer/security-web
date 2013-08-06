package com.hymer.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.hymer.core.BaseEntity;

@Entity
@Table(name = "tb_workflow_workflow")
public class Workflow extends BaseEntity {

	public static final String ACCESS_APPLY = "access_apply";
	public static final String SALON_APPLY = "salon_apply";
	public static final String FINANCING = "financing";

	private static final long serialVersionUID = 1L;


	@Length(max = 200)
	@Column(length = 200)
	private String name;
	@Length(max = 100)
	@Column(length = 100)
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
