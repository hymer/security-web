package com.hymer.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hymer.core.CommonService;
import com.hymer.core.IQueryHandler;
import com.hymer.core.dao.QueryDAO;
import com.hymer.core.entity.User;
import com.hymer.core.model.Column;
import com.hymer.core.model.Condition;
import com.hymer.core.model.Query;
import com.hymer.core.model.QueryContext;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;

@Service
public class QueryService extends CommonService {
	
	@Autowired
	private QueryDAO queryDAO;

	public ResponseJSON query(QueryObject queryObject, User user) {
		List<Condition> realConditions = new ArrayList<Condition>();
		Query query = QueryContext.getQuery(queryObject.getQueryId());
		IQueryHandler handler = query.getQueryHandler();
		if (handler != null) {
			handler.beforeConditionsSetter(query, queryObject, user);
		}
		for (Condition condition : queryObject.getConditions()) {
			if (condition.getValue() == null
					|| !StringUtils.hasText(condition.getValue().toString())) {
				continue;
			}
			Column column = query.getColumn(condition.getKey());
			if (null != column && column.isAllowSearch()) {
				condition.setOperator(column.getOperator());
				realConditions.add(condition);
			}
		}
		queryObject.setConditions(realConditions);
		if (handler != null) {
			handler.afterConditionsSetter(query, queryObject, user);
		}
		ResponseJSON json = queryDAO.query(queryObject, query, user);
		return json;
	}

}
