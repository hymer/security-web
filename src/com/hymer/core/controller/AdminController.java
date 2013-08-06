package com.hymer.core.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hymer.core.BaseContoller;
import com.hymer.core.ServiceException;
import com.hymer.core.dto.UserDTO;
import com.hymer.core.entity.Authority;
import com.hymer.core.entity.Resource;
import com.hymer.core.entity.Role;
import com.hymer.core.entity.User;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.service.AuthorityService;
import com.hymer.core.service.ResourceService;
import com.hymer.core.service.RoleService;
import com.hymer.core.service.UserService;
import com.hymer.core.util.JsonUtils;

@Controller
public class AdminController extends BaseContoller {

	@Autowired
	private ResourceService resourceService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/admin/resource_query.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON queryResource(HttpServletRequest request,
			HttpServletResponse response) {
		String queryString = request.getParameter("query");
		ResponseJSON json = null;
		try {
			QueryObject queryObject = null;
			queryObject = JsonUtils.fromJson(queryString, QueryObject.class);
			json = resourceService.query(queryObject);
			log.info("json = " + JsonUtils.toJson(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/admin/resource/{id}.html", method = RequestMethod.GET)
	public @ResponseBody
	Resource getResource(@PathVariable Long id) throws ServiceException {
		Resource resource = null;
		resource = resourceService.getResourceById(id);
		return resource;
	}

	@RequestMapping(value = "/admin/resource_edit.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON saveOrUpdateResource(HttpServletRequest request,
			HttpServletResponse response, Resource resource)
			throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		Long id = resource.getId();
		if (id == null) {
			resourceService.save(resource);
		} else {
			Resource entity = resourceService.getResourceById(id);
			entity.setDisabled(resource.isDisabled());
			entity.setName(resource.getName());
			entity.setPriority(resource.getPriority());
			entity.setType(resource.getType());
			entity.setUrl(resource.getUrl());
			entity.setDescription(resource.getDescription());
			resourceService.update(entity);
		}
		json.setMsg("保存成功！");
		return json;
	}

	@RequestMapping(value = "/admin/authority_query.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON queryAuthority(HttpServletRequest request,
			HttpServletResponse response) {
		String queryString = request.getParameter("query");
		log.info("query=" + queryString);
		ResponseJSON json = null;
		try {
			QueryObject queryObject = null;
			queryObject = JsonUtils.fromJson(queryString, QueryObject.class);
			json = authorityService.query(queryObject);
			log.info("json = " + JsonUtils.toJson(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/admin/authority/{id}.html", method = RequestMethod.GET)
	public @ResponseBody
	Authority getAuthority(@PathVariable Long id) throws ServiceException {
		Authority authority = null;
		authority = authorityService.getAuthorityById(id);
		return authority;
	}

	@RequestMapping(value = "/admin/authority_edit.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON saveOrUpdateAuthority(HttpServletRequest request,
			HttpServletResponse response, Authority authority)
			throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();

		Long id = authority.getId();
		if (id == null) {
			authorityService.save(authority);
		} else {
			Authority entity = authorityService.getAuthorityById(id);
			entity.setName(authority.getName());
			entity.setDisabled(authority.isDisabled());
			entity.setDescription(authority.getDescription());
			authorityService.update(entity);
		}
		json.setMsg("保存成功！");
		return json;
	}

	@RequestMapping(value = "/admin/role_query.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON queryRole(HttpServletRequest request,
			HttpServletResponse response) {
		String queryString = request.getParameter("query");
		log.info("query=" + queryString);
		ResponseJSON json = null;
		try {
			QueryObject queryObject = null;
			queryObject = JsonUtils.fromJson(queryString, QueryObject.class);
			json = roleService.query(queryObject);
			log.info("json = " + JsonUtils.toJson(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/admin/role/{id}.html", method = RequestMethod.GET)
	public @ResponseBody
	Role getRole(@PathVariable Long id) throws ServiceException {
		Role role = null;
		role = roleService.getRoleById(id);
		return role;
	}

	@RequestMapping(value = "/admin/role_edit.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON saveOrUpdateRole(HttpServletRequest request,
			HttpServletResponse response, Role role) throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();

		Long id = role.getId();
		if (id == null) {
			roleService.save(role);
		} else {
			Role entity = roleService.getRoleById(id);
			entity.setName(role.getName());
			// entity.setCode(role.getCode());
			entity.setDisabled(role.isDisabled());
			entity.setDescription(role.getDescription());
			roleService.update(entity);
		}
		json.setMsg("保存成功！");
		return json;
	}

	@RequestMapping(value = "/admin/user_query.ajax", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON queryUser(HttpServletRequest request,
			HttpServletResponse response) {
		String queryString = request.getParameter("query");
		log.info("query=" + queryString);
		ResponseJSON json = null;
		try {
			QueryObject queryObject = null;
			queryObject = JsonUtils.fromJson(queryString, QueryObject.class);
			json = userService.query(queryObject);
			log.info("json = " + JsonUtils.toJson(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/admin/user/{id}.html", method = RequestMethod.GET)
	public @ResponseBody
	UserDTO getUser(@PathVariable Long id) throws ServiceException {
		UserDTO dto = null;
		try {
			dto = userService.getUserDTOById(id);
		} catch (Exception e) {
			throw new ServiceException("hello");
		}
		return dto;
	}

	@RequestMapping(value = "/admin/check_user_exist.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON checkExist(String userName) {
		ResponseJSON json = new ResponseJSON();
		User temp = userService.getByUserName(userName);
		if (temp != null) {
			json.setMsg("Y");
		} else {
			json.setMsg("N");
		}
		return json;
	}

	@RequestMapping(value = "/admin/user_edit.ajax", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON saveOrUpdateUser(HttpServletRequest request,
			HttpServletResponse response, @Valid UserDTO dto, Errors errors) throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		if (errors.hasErrors()) {
			json.setError(true);
			json.setResult(true);
			json.setMsg(errors.getFieldError().getDefaultMessage());
		} else {
			Long id = dto.getId();
			if (id == null) {
				User user = (User) dto.toEntity();
				User temp = userService.getByUserName(user.getUserName());
				if (temp != null) {
					throw new ServiceException("用户名已经存在!");
				}
				Role role = roleService.getRoleById(dto.getRoleId());
				if (role == null) {
					throw new ServiceException("角色不为能空!");
				}
				user.setRole(role);
				userService.save(user);
			} else {
				userService.update(id, dto);
			}
			json.setMsg("保存成功！");
		}
		return json;
	}

	@RequestMapping(value = "/admin/authority/assign/{id}.html", method = RequestMethod.GET)
	public @ResponseBody
	ResponseJSON toAssignResource(@PathVariable Long id) {
		ResponseJSON json = new ResponseJSON();
		Set<Resource> hasResources = authorityService
				.getResourcesByAuthorityId(id);
		List<Long> hasResourceIds = new ArrayList<Long>();
		for (Resource resource : hasResources) {
			hasResourceIds.add(resource.getId());
		}
		List<Resource> resources = resourceService
				.getAvailableResources(hasResourceIds);
		json.put("available", resources);
		json.put("selected", hasResources);
		return json;
	}

	@RequestMapping(value = "/admin/assignResource.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON assignResource(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		String idString = request.getParameter("id");
		String resourceIdString = request.getParameter("resources");
		if (StringUtils.hasText(idString)) {
			Long authId = Long.parseLong(idString);
			Set<Long> resourceIds = new HashSet<Long>();
			if (StringUtils.hasText(resourceIdString)) {
				for (String id : resourceIdString.split(",")) {
					resourceIds.add(Long.parseLong(id));
				}
			}
			authorityService.saveAssignedResources(authId, resourceIds);
			json.setMsg("保存成功！");
		} else {
			json.setResult(false);
			json.setMsg("保存失败！");
		}
		return json;
	}

	@RequestMapping(value = "/admin/role/assign/{id}.html", method = RequestMethod.GET)
	public @ResponseBody
	ResponseJSON toAssignAuthority(@PathVariable Long id) {
		ResponseJSON json = new ResponseJSON();
		Set<Authority> hasAuthorities = roleService.getAuthoritiesByRoleId(id);
		List<Long> hasAuthorityIds = new ArrayList<Long>();
		for (Authority authority : hasAuthorities) {
			hasAuthorityIds.add(authority.getId());
		}
		List<Authority> authorities = authorityService
				.getAvailableAuthorities(hasAuthorityIds);
		json.put("available", authorities);
		json.put("selected", hasAuthorities);
		return json;
	}

	@RequestMapping(value = "/admin/assignAuthority.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON assignAuthority(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		String idString = request.getParameter("id");
		String authorityIdString = request.getParameter("authorities");
		if (StringUtils.hasText(idString)) {
			Long roleId = Long.parseLong(idString);
			Set<Long> authorityIds = new HashSet<Long>();
			if (StringUtils.hasText(authorityIdString)) {
				for (String id : authorityIdString.split(",")) {
					authorityIds.add(Long.parseLong(id));
				}
			}
			roleService.saveAssignedAuthorities(roleId, authorityIds);
			json.setMsg("保存成功！");
		} else {
			json.setResult(false);
			json.setMsg("保存失败！");
		}
		return json;
	}

	@RequestMapping(value = "/admin/user/role_list.ajax", method = RequestMethod.GET)
	public @ResponseBody
	ResponseJSON getRoleList() {
		ResponseJSON json = new ResponseJSON();
		List<Role> allRoles = roleService.getAvailables();
		json.put("roles", allRoles);
		return json;
	}

	@RequestMapping(value = "/admin/user/password_reset.ajax", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON resetPassword(HttpServletRequest request,
			HttpServletResponse response) {
		ResponseJSON json = new ResponseJSON();
		String idString = request.getParameter("id");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		if (StringUtils.hasText(idString)) {
			Long id = Long.parseLong(idString);
			try {
				userService.updatePassword(id, password1, password2);
				json.setMsg("密码重置成功!");
			} catch (Exception e) {
				json.setResult(false);
				json.setMsg(e.getMessage());
			}
		} else {
			json.setResult(false);
			json.setMsg("密码重置失败!");
		}
		return json;
	}

	@RequestMapping(value = "/admin/resource/delete.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON deleteResource(HttpServletRequest request,
			HttpServletResponse response) {
		ResponseJSON json = new ResponseJSON();
		String idsString = request.getParameter("ids");
		if (StringUtils.hasText(idsString)) {
			Set<Long> ids = new HashSet<Long>();
			for (String id : idsString.split(",")) {
				ids.add(Long.parseLong(id));
			}
			try {
				resourceService.deleteByIds(ids);
				json.setMsg("删除成功!");
			} catch (Exception e) {
				json.setResult(false);
				json.setMsg("资源正在使用，无法删除，请先将资源从相应权限中移除!");
			}
		} else {
			json.setMsg("没有删除任何资源.");
		}
		return json;
	}

}
