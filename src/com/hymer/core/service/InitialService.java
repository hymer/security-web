package com.hymer.core.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hymer.core.CommonService;
import com.hymer.core.SpringHelper;
import com.hymer.core.dao.MenuDAO;
import com.hymer.core.dao.ResourceDAO;
import com.hymer.core.dao.RoleDAO;
import com.hymer.core.dao.UserDAO;
import com.hymer.core.entity.Authority;
import com.hymer.core.entity.Menu;
import com.hymer.core.entity.Resource;
import com.hymer.core.entity.Role;
import com.hymer.core.entity.User;
import com.hymer.core.util.MD5Utils;

/**
 * @author hymer
 * 
 */
@Service
public class InitialService extends CommonService {

	public synchronized void init() {
		if (menuDAO.getAll().isEmpty()) {
			Set<Menu> menus = initMenu(); //初始化菜单
			Set<Resource> resources = initResource(); //初始化资源文件
			initUserAndRole(); //初始化角色和用户（超级用户）
			initDefaultRole(menus, resources);
		} else {
			throw new RuntimeException("您的系统已经初始化过了，如果需要重新初始化，请先清空相关表中的数据。");
		}
	}
	
	@Autowired
	private MenuDAO menuDAO;
	@Autowired
	private ResourceDAO resourceDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private UserDAO userDAO;

	private Set<Menu> initMenu() {
		Set<Menu> defaultRoleMenus = new HashSet<Menu>();
		Menu root = new Menu();
		root.setName("ROOT");
		menuDAO.save(root);
		Menu manager = new Menu("后台管理", "admin/admin.jsp", 1, 1, root);
		Menu password = new Menu("修改密码", "password.jsp", 1, 2, root);
		Menu information = new Menu("个人资料", "information.jsp", 1, 3, root);
		menuDAO.save(manager);
		menuDAO.save(password);
		menuDAO.save(information);
		defaultRoleMenus.add(password);
		defaultRoleMenus.add(information);
		
		//后台管理菜单
		Menu user = new Menu("用户管理", "admin/user.jsp", 2, 1, manager);
		Menu role = new Menu("角色管理", "admin/role.jsp", 2, 2, manager);
		Menu authority = new Menu("权限管理", "admin/authority.jsp", 2, 3, manager);
		Menu resource = new Menu("资源管理", "admin/resource.jsp", 2, 4, manager);
		Menu menu = new Menu("菜单管理", "admin/menus.jsp", 2, 5, manager);
		Menu config = new Menu("系统配置", "admin/configurations.jsp", 2, 6, manager);
		Menu file = new Menu("文件管理", "admin/file.jsp", 2, 7, manager);
		Menu workflow = new Menu("流程管理", "admin/workflow.jsp", 2, 8, manager);
		menuDAO.save(user);
		menuDAO.save(role);
		menuDAO.save(authority);
		menuDAO.save(resource);
		menuDAO.save(menu);
		menuDAO.save(config);
		menuDAO.save(file);
		menuDAO.save(workflow);
		return defaultRoleMenus;
	}
	
	private Set<Resource> initResource() {
		Set<Resource> defaultRoleResources = new HashSet<Resource>();
		Resource password = new Resource("修改密码", "/password.jsp", "system");
		Resource information = new Resource("个人资料", "/information.jsp", "system");
		defaultRoleResources.add(password);
		defaultRoleResources.add(information);
		
		Resource admin = new Resource("后台管理", "/admin/admin.jsp", "system");
		Resource user = new Resource("用户管理", "/admin/user*.*", "system");
		Resource role = new Resource("角色管理", "/admin/role*.*", "system");
		Resource authority = new Resource("权限管理", "/admin/authority*.*", "system");
		Resource resource = new Resource("资源管理", "/admin/resource*.*", "system");
		Resource menu = new Resource("菜单管理", "/admin/menu*.*", "system");
		Resource config = new Resource("系统配置", "(/admin/configurations.jsp|/core/config*.*)", "system");
		Resource file = new Resource("文件管理", "(/admin)?/file*.*", "system");
		Resource workflow = new Resource("流程管理", "(/admin)?/workflow*.*", "system");
		resourceDAO.save(password);
		resourceDAO.save(information);
		resourceDAO.save(admin);
		resourceDAO.save(user);
		resourceDAO.save(role);
		resourceDAO.save(authority);
		resourceDAO.save(resource);
		resourceDAO.save(menu);
		resourceDAO.save(config);
		resourceDAO.save(file);
		resourceDAO.save(workflow);
		try {
			SpringHelper.getBean(ResourceService.class).refreshProtectedResources();
		} catch (Exception e) {
			log.info("刷新资源失败，请重启应用服务器!");
		}
		return defaultRoleResources;
	}
	
	private void initUserAndRole() {
		User admin = new User();
		admin.setUserName("admin");
		admin.setPassword(MD5Utils.encode("admin"));
		Role superRole = new Role(Role.SUPER_ROLE_FLAG, "SUPER USER");
		admin.setRole(superRole);
		roleDAO.save(superRole);
		userDAO.save(admin);
	}
	
	private void initDefaultRole(Set<Menu> menus, Set<Resource> resources) {
		Role defaultRole = new Role("_default_role_", "普通用户");
		defaultRole.setDescription("注册后默认用户组");
		defaultRole.setMenus(menus);
		Authority authority = new Authority();
		authority.setName("基本权限");
		authority.setDescription("注册用户默认权限");
		authority.setResources(resources);
		authority.setCreateTime(new Date());
		defaultRole.getAuthorities().add(authority);
		roleDAO.save(defaultRole);
	}

}
