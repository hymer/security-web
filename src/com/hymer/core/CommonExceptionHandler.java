package com.hymer.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class CommonExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception exception) {
		ModelAndView failModelAndView = null;
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		uri = uri.substring(ctx.length());
		//如果是ajax请求，则需要经过另一个Action来处理
		//see MessageController.showAjaxError
		if (uri.endsWith(".ajax")) {
			failModelAndView = new ModelAndView("redirect:/showajaxerror.ajax");
			failModelAndView.addObject("error_msg", exception.getMessage());
		} else {
			failModelAndView = new ModelAndView(
					BaseContoller.COMMON_RESULT_KEY);
			if (exception instanceof MessageAlertable || StringUtils.hasText(exception.getMessage())) {
				failModelAndView.addObject(BaseContoller.COMMON_RESULT_KEY,
						false);
				failModelAndView.addObject(BaseContoller.COMMON_MESSAGE_KEY,
						exception.getMessage());
				failModelAndView.addObject(BaseContoller.COMMON_URL_KEY,
						"javascript:history.go(-1)");
				failModelAndView.addObject(
						BaseContoller.COMMON_URL_DISPLAY_KEY, "后退");
			} else {
				failModelAndView.addObject(BaseContoller.COMMON_MESSAGE_KEY,
						"系统内部错误，请联系系统管理员");
			}
		}
		exception.printStackTrace();
		return failModelAndView;
	}

}
