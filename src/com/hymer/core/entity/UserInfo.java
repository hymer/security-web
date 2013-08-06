package com.hymer.core.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.hymer.core.BaseEntity;

@Entity
@Table(name = "tb_security_userinfo")
@JsonIgnoreProperties("user")
public class UserInfo extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(length = 50)
	private String realName; // 真实姓名
	@Column(length = 2)
	@Length(max = 2)
	private String gender; // 性别
	@Column(length = 100)
	private String position; // 职位
	@Column(length = 20)
	private String telephone; // 座机
	@Column(length = 20)
	private String fax; // 传真
	@Column(length = 20)
	private String mobile; // 手机
	@Column(length = 200)
	@Email
	@Length(max = 200)
	private String email; // 电子邮箱

	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, mappedBy = "info")
	private User user;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
