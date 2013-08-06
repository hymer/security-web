package com.hymer.core.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hymer.core.BaseContoller;
import com.hymer.core.IQueryHandler;
import com.hymer.core.entity.User;
import com.hymer.core.model.Column;
import com.hymer.core.model.Query;
import com.hymer.core.model.QueryContext;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.service.QueryService;
import com.hymer.core.util.JsonUtils;

@Controller
public class QueryController extends BaseContoller {

	@Autowired
	private QueryService queryService;

	@RequestMapping(value = "/load_query.ajax", method = RequestMethod.GET)
	public @ResponseBody
	Collection<Column> prepare(HttpServletRequest request) {
		String queryId = request.getParameter("queryId");
		Collection<Column> columns = null;
		try {
			Query query = QueryContext.getQuery(queryId);
			if (query != null) {
				IQueryHandler handler = query.getQueryHandler();
				if (handler != null) {
					User user = (User) request.getSession().getAttribute("user");
					columns = handler.filterColumns(query, user);
				} else {
					columns = query.getColumnsMap().values();
				}
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return columns;
	}
	
	/**
	 * 如果配置为不带后缀，则通过query.html/query.ajax均可访问
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON query(HttpServletRequest request) {
		String queryString = request.getParameter("query");
		ResponseJSON json = null;
		try {
			QueryObject queryObject = null;
			queryObject = JsonUtils.fromJson(queryString, QueryObject.class);
			User user = (User) request.getSession().getAttribute("user");
			json = queryService.query(queryObject, user);
			log.info("json = " + JsonUtils.toJson(json));
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return json;
	}
	
}
