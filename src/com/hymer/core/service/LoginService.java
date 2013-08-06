package com.hymer.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hymer.core.CommonService;
import com.hymer.core.SessionContext;
import com.hymer.core.dao.MenuDAO;
import com.hymer.core.dao.UserDAO;
import com.hymer.core.dto.MenuDTO;
import com.hymer.core.entity.Authority;
import com.hymer.core.entity.Menu;
import com.hymer.core.entity.Resource;
import com.hymer.core.entity.Role;
import com.hymer.core.entity.User;
import com.hymer.core.util.MD5Utils;

@Service
public class LoginService extends CommonService {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private MenuDAO menuDAO;
	
	public User login(String userName, String password, HttpServletRequest request) {
		User user =  null;
		List<User> users = userDAO.getByProperty("userName", userName);
		if ( ! users.isEmpty()) {
			User temp = users.get(0);
			if (temp.isDisabled()) {
				throw new RuntimeException("该用户账号已停用。");
			} else if (temp.getRole().isDisabled()) {
				throw new RuntimeException("该角色已停用。");
			}
			String encodePassword = MD5Utils.encode(password);
			if (encodePassword.equals(temp.getPassword())) {
				user = users.get(0);
				//在此,将角色/权限/资源都取出来，防止filter中出懒加载错。
				Set<Authority> authorities = user.getRole().getAuthorities();
				Set<String> userAuthorizedUrls = new HashSet<String>();
				for (Authority authority : authorities) {
					for (Resource resource : authority.getResources()) {
						userAuthorizedUrls.add(resource.getUrl());
					}
				}
				
				// 菜单
				Collection<Menu> menus = null;
				List<MenuDTO> dtos = new ArrayList<MenuDTO>();
				if (Role.SUPER_ROLE_FLAG.equals(user.getRole().getCode())) {
					menus = menuDAO.getAll("order asc");
				} else {
					menus = user.getRole().getMenus();
				}
				for (Menu menu : menus) {
					MenuDTO dto = new MenuDTO();
					dto.setName(menu.getName());
					dto.setLink(request.getContextPath() + "/" + menu.getLink());
					dto.setLevel(menu.getLevel());
					dtos.add(dto);
				}
				HttpSession session = request.getSession();
				session.setAttribute("menus", dtos);
				session.setAttribute("user", user);
				session.setAttribute("role_code", user.getRole().getCode());
				session.setAttribute("authorized_resources", userAuthorizedUrls);
				SessionContext.addSession(session);
			}
		}
		return user;
	}
	
}
