package com.hymer.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hymer.core.BaseContoller;
import com.hymer.core.service.InitialService;

@Controller
public class InitialController extends BaseContoller {

	@Autowired
	private InitialService initialService;
	
	@RequestMapping(value = "/init.html", method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request) {
		ModelAndView mv = null;
		try {
			initialService.init();
			mv = getSuccessMV(request);
			mv.addObject(BaseContoller.COMMON_MESSAGE_KEY, "系统初始化成功!现在请使用[admin/admin]登录系统。");
			mv.addObject(BaseContoller.COMMON_URL_KEY, "login.jsp");
			mv.addObject(BaseContoller.COMMON_URL_DISPLAY_KEY, "马上登录");
			mv.addObject(BaseContoller.COMMON_AUTO_REDIRECT, false);
		} catch (Exception e) {
			mv = getFailureMV(request);
			mv.addObject(BaseContoller.COMMON_MESSAGE_KEY, e.getMessage());
			mv.addObject(BaseContoller.COMMON_AUTO_REDIRECT, false);
		}
		return mv;
	}
	
}
