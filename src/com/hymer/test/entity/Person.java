package com.hymer.test.entity;

import javax.persistence.Entity;

import com.hymer.core.BaseEntity;

@Entity
public class Person extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;
	private String sex;
	private String email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
