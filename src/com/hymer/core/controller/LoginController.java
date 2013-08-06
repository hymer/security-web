package com.hymer.core.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hymer.core.BaseContoller;
import com.hymer.core.ServiceException;
import com.hymer.core.dto.MenuDTO;
import com.hymer.core.entity.User;
import com.hymer.core.entity.UserInfo;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.service.LoginService;
import com.hymer.core.service.MessageService;
import com.hymer.core.service.UserService;
import com.hymer.core.util.MD5Utils;

@Controller
public class LoginController extends BaseContoller {

	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;
	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "/j_security_check.html", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		ModelAndView modelAndView = new ModelAndView("redirect:/");
		HttpSession session = request.getSession();
		Integer errors = (Integer) session.getAttribute("errorTimes");
		try {
			String referer = (String) request.getParameter("referer");
			String pageBeforeLogin = (String) session.getAttribute("referer");
			if (!StringUtils.hasText(pageBeforeLogin)) {
				pageBeforeLogin = referer;
				session.setAttribute("referer", referer);
			}
			if (errors != null && errors > 2) {
				String c = (String) request.getSession().getAttribute(
						com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
				String parm = (String) request.getParameter("verifyCode");
				if (!(StringUtils.hasText(c) && StringUtils.hasText(parm) && c
						.equals(parm))) {
					modelAndView = getFailureMV(request);
					modelAndView.addObject("msg", "验证码输入不正确!");
					modelAndView.addObject("url", "login.jsp?verify=true");
					return modelAndView;
				}
			}

			// get client cookies, implements the function "remember me".
			boolean isRemembered = false;
			String userName = null;
			String password = null;
			// Cookie[] cookies = request.getCookies();
			// for (Cookie cookie : cookies) {
			// log.info("cookie:" + cookie.getName() + ",maxAge:"
			// + cookie.getMaxAge());
			// if (cookie.getName().equals("j_username")
			// && cookie.getMaxAge() > 0) {
			// isRemembered = true;
			// userName = cookie.getValue();
			// log.info("userName:" + userName);
			// break;
			// }
			// }
			if (isRemembered) {

			} else {
				userName = request.getParameter("j_username");
				password = request.getParameter("j_password");
			}
			User user = loginService.login(userName, password, request);
			if (user != null) {
				// 如果登录成功
				if (referer.indexOf("/register.") != -1) {
					modelAndView = getResultMV(request, true, true, "登录成功!",
							"index.jsp", "返回首页");
				} else {
					modelAndView = getResultMV(request, true, true, "登录成功!",
							pageBeforeLogin, "返回登录前页面");
				}
				session.removeAttribute("referer");
				// 消息服务
				long newMsgCount = messageService.getNewMessageCount(user);
				session.setAttribute("newMsgs", newMsgCount);
				session.setAttribute("errorTimes", 0);
			} else {
				throw new RuntimeException("用户名或密码错误!");
			}
		} catch (Exception e) {
			if (errors == null) {
				errors = 1;
			} else {
				errors++;
			}
			session.setAttribute("errorTimes", errors);
			if (errors > 2) {
				return new ModelAndView("redirect:/login.jsp?verify=true");
			} else {
				throw new ServiceException(e.getMessage(), e);
			}
		}
		return modelAndView;
	}

	@RequestMapping(value = "/change_password.html", method = RequestMethod.POST)
	public ModelAndView resetPassword(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = null;
		User user = (User) request.getSession().getAttribute("user");
		String password = request.getParameter("password");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		boolean flag = false;
		String message = null;
		if (user != null && StringUtils.hasText(password)) {
			if (MD5Utils.encode(password).equals(user.getPassword())) {
				try {
					userService.updatePassword(user.getId(), password1,
							password2);
					user = userService.getUserById(user.getId());
					request.getSession().setAttribute("user", user);
					flag = true;
					message = "密码修改成功!";
				} catch (Exception e) {
					message = e.getMessage();
				}
			} else {
				message = "原密码输入错误!";
			}
		} else {
			message = "密码修改失败!";
		}
		if (!flag) {
			mv = getFailureMV(request);
		} else {
			mv = getSuccessMV(request);
		}
		mv.addObject(COMMON_MESSAGE_KEY, message);
		return mv;
	}

	@RequestMapping(value = "/information.html", method = RequestMethod.POST)
	public ModelAndView changeInformation(HttpServletRequest request,
			HttpServletResponse response, UserInfo info) {
		ModelAndView mv = null;
		User user = (User) request.getSession().getAttribute("user");
		userService.updateUserInfo(info, user);
		request.getSession().setAttribute("user", user);
		mv = getSuccessMV(request);
		return mv;
	}

	@RequestMapping(value = "/j_security_logout.html", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/login.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/getMenus.ajax", method = RequestMethod.POST)
	public @ResponseBody
	List<MenuDTO> queryMenuByRole(HttpServletRequest request,
			HttpServletResponse response) {
		List<MenuDTO> menus = null;
		try {
			User user = (User) request.getSession().getAttribute("user");
			String idString = request.getParameter("id");
			Long pid = null;
			if (StringUtils.hasText(idString)) {
				pid = Long.parseLong(idString);
			}
			menus = userService.queryUserMenus(user.getId(), pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menus;
	}

	@RequestMapping(value = "/showajaxerror.ajax", method = RequestMethod.GET)
	public @ResponseBody
	ResponseJSON showAjaxError(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		ResponseJSON json = new ResponseJSON();
		json.setError(true);
		json.setResult(false);
		json.setMsg(request.getParameter("error_msg"));
		return json;
	}

}
