package com.hymer.core.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hymer.core.CommonService;
import com.hymer.core.dao.MenuDAO;
import com.hymer.core.dao.RoleDAO;
import com.hymer.core.dto.MenuDTO;
import com.hymer.core.entity.Menu;
import com.hymer.core.entity.Role;
import com.hymer.core.model.Condition;

/**
 * @author hymer
 * 
 */
@Service
public class MenuService extends CommonService {

	@Autowired
	private MenuDAO menuDAO;
	@Autowired
	private RoleDAO roleDAO;

	public List<MenuDTO> query(Long pid) {
		Condition condition = null;
		if (null == pid) {
			condition = new Condition("level", 0);
		} else {
			condition = new Condition("parent.id", pid);
		}
		List<Menu> menus = menuDAO.getByCondition(condition, "order asc");
		List<MenuDTO> dtos = new ArrayList<MenuDTO>();
		for (Menu menu : menus) {
			MenuDTO dto = new MenuDTO();
			dto.fromEntity(menu);
			dto.setName(dto.getOrder() + "." + dto.getName());
			dtos.add(dto);
		}
		return dtos;
	}

	public List<MenuDTO> queryByRoleId(Long pid, Long roleId) {
		Role role = roleDAO.getById(roleId);
		Set<Menu> hasMenus = role.getMenus();
		Condition condition = null;
		if (null == pid) {
			condition = new Condition("level", 1);
		} else {
			condition = new Condition("parent.id", pid);
		}
		List<Menu> menus = menuDAO.getByCondition(condition, "order asc");
		List<MenuDTO> dtos = new ArrayList<MenuDTO>();
		for (Menu menu : menus) {
			MenuDTO dto = new MenuDTO();
			dto.fromEntity(menu);

			for (Menu has : hasMenus) {
				if (menu.getId().equals(has.getId())) {
					dto.setChecked(true);
					break;
				}
			}

			dtos.add(dto);
		}
		return dtos;
	}

	public void addSubMenu(Long pid, Menu entity) {
		Menu parent = menuDAO.getById(pid);
		entity.setParent(parent);
		entity.setLevel(parent.getLevel() + 1);
		entity.setOrder(parent.getChildren().size() + 1);
		menuDAO.save(entity);
	}

	public void deleteMenuById(Long id) {
		Menu model = menuDAO.getById(id);
		menuDAO.delete(model);
	}

	public Menu getById(Long id) {
		return menuDAO.getById(id);
	}

	public void update(MenuDTO dto) {
		Menu menu = menuDAO.getById(dto.getId());
		menu.setName(dto.getName());
		menu.setLink(dto.getLink());
		int order = dto.getOrder();
		if (menu.getOrder() != order) {
			List<Menu> all = menu.getParent().getChildren();
			if (order <= 0) {
				order = 1;
			} else if (order > all.size()) {
				order = all.size();
			}
			for (Menu other : all) {
				if (!other.getId().equals(menu.getId())) {
					int tmp = other.getOrder();
					if (order > menu.getOrder()) {
						if (tmp > menu.getOrder() && tmp <= order) {
							other.setOrder(tmp - 1);
						}
					} else {
						if (tmp >= order && tmp <= menu.getOrder()) {
							other.setOrder(tmp + 1);
						}
					}
					menuDAO.update(other);
				}
			}
			menu.setOrder(order);
		}
		menuDAO.update(menu);
	}

	public Set<Menu> getMenusByRoleId(Long id) {
		Role role = roleDAO.getById(id);
		if (role != null) {
			return role.getMenus();
		}
		return new HashSet<Menu>();
	}

	public void saveAssignedMenus(Long roleId, Set<Long> menuIds) {
		Role role = roleDAO.getById(roleId);
		if (!menuIds.isEmpty()) {
			Condition condition = new Condition("id", menuIds);
			condition.setOperator(Condition.IN);
			condition.setValueType(Long.class);
			List<Menu> menus = menuDAO.getByCondition(condition);
			Set<Menu> hasMenus = role.getMenus();
			for (Menu changed : menus) {
				boolean toRemove = false;
				for (Menu has : hasMenus) {
					if (has.getId().equals(changed.getId())) {
						toRemove = true;
						break;
					}
				}
				if (toRemove) {
					hasMenus.remove(changed);
				} else {
					hasMenus.add(changed);
				}
			}
			role.setMenus(hasMenus);
			roleDAO.update(role);
		}
	}

}
