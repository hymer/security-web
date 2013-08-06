package com.hymer.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hymer.core.BaseContoller;
import com.hymer.core.Configuration;
import com.hymer.core.ServiceException;
import com.hymer.core.dto.UserDTO;
import com.hymer.core.entity.Role;
import com.hymer.core.entity.User;
import com.hymer.core.entity.UserInfo;
import com.hymer.core.service.RoleService;
import com.hymer.core.service.UserService;

@Controller
public class RegisterController extends BaseContoller {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/register.html", method = RequestMethod.POST)
	public ModelAndView register(HttpServletRequest request,
			HttpServletResponse response, @Valid UserDTO dto,
			Errors errors, @Valid UserInfo userInfo, Errors userInfoErrors) throws ServiceException {
		ModelAndView mv = null;
		try {
			String c = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
			String parm = (String) request.getParameter("verifyCode");
			if ( ! (StringUtils.hasText(c) && StringUtils.hasText(parm) && c.equals(parm))) {
				mv = getFailureMV(request);
				mv.addObject("msg", "验证码输入不正确!");
				return mv;
			} else if (errors.hasErrors()) {
				return getErrorsMV(request, errors.getFieldErrors());
			} else if (userInfoErrors.hasErrors()) {
				return getErrorsMV(request, userInfoErrors.getFieldErrors());
			}
			
			User temp = userService.getByUserName(dto.getUserName());
			if (temp != null) {
				mv = getFailureMV(request);
				mv.addObject("msg", "用户名已经存在!");
				mv.addObject("url_display", "返回修改注册资料");
				return mv;
			}

			User user = (User) dto.toEntity();
			Role role = null;
			role = roleService.getRoleByCode(Configuration.get("register.default.role"));
			user.setRole(role);
			userService.addPersonalUser(user, userInfo);
			mv = getSuccessMV(request);
			mv.addObject("msg", "恭喜您，注册成功！");
			mv.addObject("url", "login.jsp");
			mv.addObject("url_display", "马上登录");
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return mv;
	}

}
