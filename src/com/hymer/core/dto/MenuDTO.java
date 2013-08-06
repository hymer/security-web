package com.hymer.core.dto;

import com.hymer.core.BaseEntity;
import com.hymer.core.DTO;
import com.hymer.core.entity.Menu;

public class MenuDTO extends DTO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String link;
	private int level;
	private int order;
	private Long pid;
	private String isParent = "false";
	private boolean checked = false;

	@Override
	public BaseEntity toEntity() {
		Menu menu = new Menu();
		menu.setId(id);
		menu.setName(name);
		menu.setLink(link);
		menu.setOrder(order);
		return menu;
	}

	@Override
	public void fromEntity(BaseEntity entity) {
		Menu menu = (Menu) entity;
		id = menu.getId();
		name = menu.getName();
		link = menu.getLink();
		level = menu.getLevel();
		order = menu.getOrder();
		if (null != menu.getParent()) {
			pid = menu.getParent().getId();
		}
		if ( ! menu.getChildren().isEmpty()) {
			isParent = "true";
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String text) {
		this.name = text;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
