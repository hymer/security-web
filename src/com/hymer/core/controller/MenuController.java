package com.hymer.core.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hymer.core.BaseContoller;
import com.hymer.core.ServiceException;
import com.hymer.core.dto.MenuDTO;
import com.hymer.core.entity.Menu;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.service.MenuService;

@Controller
public class MenuController extends BaseContoller {

	@Autowired
	private MenuService menuService;

	@RequestMapping(value = "/menu/query.ajax", method = RequestMethod.POST)
	public @ResponseBody
	List<MenuDTO> queryMenu(HttpServletRequest request,
			HttpServletResponse response) {
		List<MenuDTO> menus = null;
		try {
			String idString = request.getParameter("id");
			Long pid = null;
			if (StringUtils.hasText(idString)) {
				pid = Long.parseLong(idString);
			}
			menus = menuService.query(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menus;
	}

	@RequestMapping(value = "/menu/addsubmenu.ajax", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON addSubMenu(HttpServletRequest request,
			HttpServletResponse response, MenuDTO dto) throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		Long pid = dto.getPid();
		if (pid != null) {
			menuService.addSubMenu(pid, (Menu) dto.toEntity());
		}
		json.setMsg("保存成功！");
		return json;
	}

	@RequestMapping(value = "/menu/editmenu.ajax", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON editMenu(HttpServletRequest request,
			HttpServletResponse response, MenuDTO dto) throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		menuService.update(dto);
		json.setMsg("保存成功！");
		return json;
	}

	@RequestMapping(value = "/menu/delete.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON deleteConfig(HttpServletRequest request,
			HttpServletResponse response) {
		ResponseJSON json = new ResponseJSON();
		String idsString = request.getParameter("id");
		if (StringUtils.hasText(idsString)) {
			Long id = Long.parseLong(idsString);
			try {
				menuService.deleteMenuById(id);
				json.setMsg("删除成功!");
			} catch (Exception e) {
				json.setResult(false);
				json.setMsg("无法删除，请先删除所有子菜单!");
			}
		} else {
			json.setMsg("没有删除任何资源.");
		}
		return json;
	}
	
	@RequestMapping(value = "/admin/role/menu/get.ajax", method = RequestMethod.POST)
	public @ResponseBody
	List<MenuDTO> queryMenuByRole(HttpServletRequest request,
			HttpServletResponse response) {
		List<MenuDTO> menus = null;
		try {
			String roleIdString = request.getParameter("roleId");
			String  idString = request.getParameter("id");
			Long roleId = null, pid = null;
			if (StringUtils.hasText(roleIdString)) {
				roleId = Long.parseLong(roleIdString);
			}
			if (StringUtils.hasText(idString)) {
				pid = Long.parseLong(idString);
			}
			menus = menuService.queryByRoleId(pid, roleId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menus;
	}

	@RequestMapping(value = "/admin/role/assignMenu.ajax", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON assignMenus(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		String idString = request.getParameter("id");
		String menusIdString = request.getParameter("menus");
		if (StringUtils.hasText(idString)) {
			Long roleId = Long.parseLong(idString);
			Set<Long> menuIds = new HashSet<Long>();
			if (StringUtils.hasText(menusIdString)) {
				for (String id : menusIdString.split(",")) {
					menuIds.add(Long.parseLong(id));
				}
			}
			menuService.saveAssignedMenus(roleId, menuIds);
			json.setMsg("保存成功！");
		} else {
			json.setResult(false);
			json.setMsg("保存失败！");
		}
		return json;
	}

}
