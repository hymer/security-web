package com.hymer.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hymer.core.entity.Role;
import com.hymer.core.entity.User;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.service.ConfigurationService;
import com.hymer.core.service.ResourceService;

@Repository("securityFilter")
public class SecurityFilter implements Filter {
	Log log = LogFactory.getLog(getClass());

	@Autowired
	private ResourceService resourceService;
	@Autowired
	private ConfigurationService coreService;

	public SecurityFilter() {
		super();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		uri = uri.substring(ctx.length());
		log.info("uri=" + uri);
		boolean isAuthorized = false;
		boolean isNeedAuthorized = false;
		boolean isNotLogin = true;
		String currentResource = null;
		Pattern pattern = null;
		try {
			for (String resource : ResourceService.PROTECTED_RESOURCES) {
				try {
					pattern = Pattern.compile(resource);
					Matcher matcher = pattern.matcher(uri);
					if (matcher.matches()) {
						// then , need authorized.
						isNeedAuthorized = true;
						log.info("  === 受保护(通过" + resource + "匹配) ");
						currentResource = resource;
						break;
					}
				} catch (PatternSyntaxException e) {
					log.error("资源路径正则表达式非法，请仔细检查。resource=" + resource, e);
					continue;
				}
			}

			if (isNeedAuthorized) {
				HttpSession session = request.getSession();
				String sessionId = session.getId();
				HttpSession userSession = SessionContext.getSession(sessionId);
				if (userSession == null) {
					isAuthorized = false;
					log.info("  === 用户未登录 ");
				} else {
					User user = (User) userSession.getAttribute("user");
					String roleCode = (String) userSession
							.getAttribute("role_code");
					@SuppressWarnings("unchecked")
					Set<String> userAuthorizedUrls = (HashSet<String>) userSession
							.getAttribute("authorized_resources");
					if (user == null) {
						isAuthorized = false;
					} else {
						isNotLogin = false;
					}
					if (Role.SUPER_ROLE_FLAG.equals(roleCode)) {
						isAuthorized = true;
					} else {
						for (String auth : userAuthorizedUrls) {
							if (auth.equals(currentResource)) {
								isAuthorized = true;
								log.info("  === 验证通过");
								break;
							}
						}
						if (!isAuthorized) {
							log.info("  === 访问被拒绝 ");
						}
					}
				}

			} else {
				log.info("  === 未保护资源 ");
			}
		} catch (Exception e) {
			// catch any exception, authorized failed.
			log.error("权限过滤出现异常，请仔细检查。", e);
			e.printStackTrace();
		}

		// do not need authorized or is authorized success, then let it pass.
		if ((!isNeedAuthorized) || isAuthorized) {
			chain.doFilter(request, response);
		} else {
			if (uri.endsWith(".ajax")) {
				ResponseJSON json = new ResponseJSON();
				json.setError(true);
				json.setResult(false);
				if (isNotLogin) {
					json.setMsg("对不起，请先登录系统！");
				} else {
					json.setMsg("对不起，您没有权限访问该资源！");
				}
				log.info(json.toJSONString());
				response.getWriter().print(json.toJSONString());
			} else {
				if (isNotLogin) {
					((HttpServletResponse) response).sendRedirect(ctx
							+ "/login.jsp");
				} else {
					((HttpServletResponse) response).sendRedirect(ctx
							+ "/decline.jsp");
				}
			}
			// PrintWriter out = response.getWriter();
			// out.print("is not authorized");
			return;
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		coreService.initPreferences();
		resourceService.refreshProtectedResources();
	}

}
