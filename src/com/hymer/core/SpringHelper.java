package com.hymer.core;

import java.util.Locale;

import org.springframework.context.ApplicationContext;

public class SpringHelper {
	private static ApplicationContext context;

	public static ApplicationContext getContext() {
		return context;
	}

	public static Object getBean(String beanId) throws Exception {
		return context.getBean(beanId);
	}

	public static <T> T getBean(Class<T> beanClass) throws Exception {
		return context.getBean(beanClass);
	}
	
	public static String getMessage(String key, Object[] args, Locale locale) throws Exception {
		return context.getMessage(key, args, locale);
	}

	public static void setContext(ApplicationContext ctx) {
		context = ctx;
	}
}
