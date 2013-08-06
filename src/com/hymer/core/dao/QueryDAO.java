package com.hymer.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hymer.core.BaseEntity;
import com.hymer.core.CommonDAO;
import com.hymer.core.IQueryHandler;
import com.hymer.core.entity.User;
import com.hymer.core.model.PageInfo;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;

@Repository
public class QueryDAO extends CommonDAO {

	public ResponseJSON query(QueryObject queryObject,
			com.hymer.core.model.Query query, User user) {
		ResponseJSON json = new ResponseJSON();
		if (queryObject != null) {
			FROM = "from " + query.getClazz().getSimpleName() + " "
					+ DEFAULT_ALIAS + " ";
			String defaultFilter = null;
			if (null != query.getFilter()) {
				defaultFilter = WHERE + query.getFilter();
			} else {
				defaultFilter = WHERE;
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String conditionHql = processConditions(
					queryObject.getConditions(), paramMap);
			String countHql = COUNT_PRE + FROM + defaultFilter + conditionHql;
			String sortString = (queryObject.getSortinfo() != null) ? queryObject
					.getSortinfo().toString() : " order by " + query.getOrder();
			String queryHql = FROM + defaultFilter + conditionHql + sortString;

			int totalRecords = 0;
			totalRecords = unique(countHql, Number.class, paramMap).intValue();
			PageInfo pageInfo = queryObject.getPageinfo();
			int currentPage = pageInfo.getCurrentPage();
			int limit = pageInfo.getRecordsPerPage();
			List<?> datas = executeQuery(queryHql, currentPage, limit, paramMap);
			try {
				// 调用特定处理查询结果实体集方法
				IQueryHandler handler = query.getQueryHandler();
				if (handler != null) {
					datas = handler.handlerDatas(datas, query, user);
				}
			} catch (Exception e) {
				log.info(e.getMessage());
			}
			pageInfo.setTotalRecords(totalRecords);
			json.put("data", datas);
			json.put("pageinfo", pageInfo);
			json.put("sortinfo", queryObject.getSortinfo());
		} else {
			json.setResult(false);
			json.setMsg("the parameter is not correct, could not execute this query.");
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	private List<? extends BaseEntity> executeQuery(final String hql,
			final int currentPage, final int recordsPerPage,
			final Map<String, Object> paramMap) {
		log.info("hql=" + hql);
		Query query = getSession().createQuery(hql);
		setParameters(query, paramMap);
		if (currentPage > -1 && recordsPerPage > -1) {
			query.setMaxResults(recordsPerPage);
			int start = (currentPage - 1) * recordsPerPage;
			if (start != 0) {
				query.setFirstResult(start);
			}
		}
		if (currentPage < 0) {
			query.setFirstResult(0);
		}
		return query.list();
	}

}
