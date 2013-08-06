package com.hymer.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

/**
 * action 基类
 */
public abstract class BaseContoller {

	protected Log log = LogFactory.getLog(getClass());

	public static final String COMMON_FAIL_PAGE = "result";
	public static final String COMMON_RESULT_KEY = "result";
	public static final String COMMON_MESSAGE_KEY = "msg";
	public static final String COMMON_URL_KEY = "url";
	public static final String COMMON_URL_DISPLAY_KEY = "url_display";
	public static final String COMMON_AUTO_REDIRECT = "auto_redirect";
	public static final String COMMON_ERRORS_KEY = "errors";

	public static final String USER_SESSION_KEY = "_user_session_key_";

	protected ModelAndView getResultMV(HttpServletRequest request, boolean result, boolean autoRedirect,
			String... args) {
		ModelAndView modelAndView = new ModelAndView(COMMON_RESULT_KEY);
		modelAndView.addObject(COMMON_RESULT_KEY, result);
		modelAndView.addObject(COMMON_AUTO_REDIRECT, autoRedirect);
		
		String msg = result ? "您的请求已成功处理!" : "您的请求处理失败，请重试!";
		String url = result ? "index.jsp" : "javascript:history.go(-1)";
		String display = result ? "首页" : "后退";
		if (args.length > 0) {
			msg = args[0];
		}
		if (args.length > 1) {
			url = args[1];
		}
		if (args.length > 2) {
			display = args[2];
		}
		modelAndView.addObject(COMMON_MESSAGE_KEY, msg);
		modelAndView.addObject(COMMON_URL_KEY, url);
		modelAndView.addObject(COMMON_URL_DISPLAY_KEY, display);
		return modelAndView;
	}

	protected ModelAndView getSuccessMV(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(COMMON_RESULT_KEY);
		modelAndView.addObject(COMMON_RESULT_KEY, true);
		modelAndView.addObject(COMMON_MESSAGE_KEY, "您的请求已成功处理!");
		modelAndView.addObject(COMMON_URL_KEY, "index.jsp");
		modelAndView.addObject(COMMON_URL_DISPLAY_KEY, "返回首页");
		return modelAndView;
	}

	protected ModelAndView getFailureMV(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(COMMON_RESULT_KEY);
		modelAndView.addObject(COMMON_RESULT_KEY, false);
		modelAndView.addObject(COMMON_MESSAGE_KEY, "您的请求处理失败，请重试!");
		modelAndView.addObject(COMMON_URL_KEY, "javascript:history.go(-1)");
		modelAndView.addObject(COMMON_URL_DISPLAY_KEY, "后退");
		modelAndView.addObject(COMMON_AUTO_REDIRECT, false);
		return modelAndView;
	}

	protected ModelAndView getErrorsMV(HttpServletRequest request, List<FieldError> errors) {
		ModelAndView modelAndView = new ModelAndView(COMMON_RESULT_KEY);
		modelAndView.addObject(COMMON_RESULT_KEY, false);
		modelAndView.addObject(COMMON_MESSAGE_KEY, "您的填写有错误，请仔细检查!");
		modelAndView.addObject(COMMON_URL_KEY, "javascript:history.go(-1)");
		modelAndView.addObject(COMMON_URL_DISPLAY_KEY, "返回重填");
		modelAndView.addObject(COMMON_AUTO_REDIRECT, false);
		modelAndView.addObject(COMMON_ERRORS_KEY, errors);
		return modelAndView;
	}

}
