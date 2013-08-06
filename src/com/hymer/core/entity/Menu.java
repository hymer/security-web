package com.hymer.core.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.hymer.core.BaseEntity;

@Entity
@Table(name = "tb_security_menu")
@JsonIgnoreProperties("children")
public class Menu extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public Menu() {
		super();
	}

	public Menu(String name, String link, int level, int order) {
		this();
		this.name = name;
		this.link = link;
		this.level = level;
		this.order = order;
	}

	public Menu(String name, String link, int level, int order, Menu parent) {
		this(name, link, level, order);
		this.parent = parent;
	}

	@Column(name = "name", nullable = false, unique = true)
	private String name;
	@Column(name = "link")
	private String link;
	@Column(name = "mlevel")
	private int level = 0;
	@Column(name = "morder")
	private int order = 0;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	private Menu parent;
	@OneToMany(targetEntity = Menu.class, cascade = CascadeType.ALL, mappedBy = "parent")
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("order")
	private List<Menu> children = new ArrayList<Menu>();

	private boolean disabled = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
