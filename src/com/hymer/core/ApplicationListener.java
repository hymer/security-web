package com.hymer.core;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hymer.core.model.QueryContext;

public class ApplicationListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		try {
			initContextUtil(context);
			initQueryContext(context);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	private void initQueryContext(ServletContext context) throws Exception {
		QueryContext.parseQuerys();
	}

	private void initContextUtil(ServletContext context) throws Exception {
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		SpringHelper.setContext(ctx);
	}
}
