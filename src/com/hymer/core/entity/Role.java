package com.hymer.core.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import com.hymer.core.BaseEntity;

@Entity
@Table(name = "tb_security_role")
@JsonIgnoreProperties({ "authorities", "menus" })
public class Role extends BaseEntity {
	public static final String SUPER_ROLE_FLAG = "__SUPER_ROLE_FLAG__"; // 超级管理员

	private static final long serialVersionUID = 1L;

	public Role() {
		super();
	}

	public Role(String code, String name) {
		this();
		this.code = code;
		this.name = name;
	}

	@Length(min = 1, max = 100)
	@Column(name = "role_code", length = 100)
	private String code;
	@Length(min = 1, max = 200)
	@Column(length = 200)
	private String name;
	@Column(length = 1024)
	private String description;
	@Column
	private boolean disabled = false;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tb_security_role_authority", joinColumns = { @JoinColumn(name = "roleId") }, inverseJoinColumns = { @JoinColumn(name = "authorityId") })
	private Set<Authority> authorities = new HashSet<Authority>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tb_security_role_menu", joinColumns = { @JoinColumn(name = "roleId") }, inverseJoinColumns = { @JoinColumn(name = "menuId") })
	@OrderBy("order")
	private Set<Menu> menus = new HashSet<Menu>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

}
