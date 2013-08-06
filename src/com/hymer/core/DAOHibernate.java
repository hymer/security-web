package com.hymer.core;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.metamodel.source.hbm.Helper.ValueSourcesAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.hymer.core.model.Condition;
import com.hymer.core.model.PageInfo;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;

@SuppressWarnings("all")
public abstract class DAOHibernate<T extends BaseEntity, PK extends java.io.Serializable>
		extends CommonDAO implements IDAO<T, PK> {
	private final Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public DAOHibernate() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		FROM = "from " + this.entityClass.getSimpleName() + " " + DEFAULT_ALIAS
				+ " ";
	}

	@Override
	public T getById(PK id) {
		return (T) getSession().get(this.entityClass, id);
	}

	@Override
	public List<T> getByProperty(String propertyName, Object propertyValue) {
		String paramKey = formatParamKey(propertyName, 0);
		String hql = FROM + " where " + DEFAULT_ALIAS + "." + propertyName
				+ "=:" + paramKey;
		Query query = getSession().createQuery(hql);
		setParameter(query, paramKey, propertyValue);
		return query.list();
	}
	
	@Override
	public List<T> getByCondition(Condition... conditions) {
		List<Condition> cs = new ArrayList<Condition>();
		for (Condition condition : conditions) {
			cs.add(condition);
		}
		return getByConditions(cs);
	}

	@Override
	public List<T> getByCondition(String sortString, Condition... conditions) {
		List<Condition> cs = new ArrayList<Condition>();
		for (Condition condition : conditions) {
			cs.add(condition);
		}
		return getByConditions(cs, sortString);
	}

	@Override
	public List<T> getByCondition(Condition condition) {
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(condition);
		return getByConditions(conditions);
	}

	@Override
	public List<T> getByConditions(List<Condition> conditions) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String conditionHql = processConditions(conditions, paramMap);
		return this.executeQuery(FROM + WHERE + conditionHql, -1, -1, paramMap);
	}

	@Override
	public List<T> getByCondition(Condition condition, String sortString) {
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(condition);
		return getByConditions(conditions, sortString);
	}
	
	@Override
	public List<T> getByConditions(List<Condition> conditions, String sortString) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String conditionHql = processConditions(conditions, paramMap);
		String hql = FROM + WHERE + conditionHql + " order by " + sortString;
		return this.executeQuery(hql, -1, -1, paramMap);
	}

	@Override
	public List<T> getAll() {
		Query query = getSession().createQuery(FROM);
		return query.list();
	}

	@Override
	public List<T> getAll(String orderString) {
		Query query = getSession().createQuery(FROM + " ORDER BY " + orderString);
		return query.list();
	}

	@Override
	public ResponseJSON getAll(QueryObject queryObject) {
		ResponseJSON json = new ResponseJSON();
		if (queryObject != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String conditionHql = processConditions(
					queryObject.getConditions(), paramMap);
			String countHql = COUNT_PRE + FROM + WHERE + conditionHql;
			String sortString = (queryObject.getSortinfo() != null) ? queryObject
					.getSortinfo().toString() : " order by " + DEFAULT_ALIAS
					+ ".id desc ";
			String queryHql = FROM + WHERE + conditionHql + sortString;

			int totalRecords = 0;
			totalRecords = unique(countHql, Number.class, paramMap).intValue();
			PageInfo pageInfo = queryObject.getPageinfo();
			int currentPage = pageInfo.getCurrentPage();
			int limit = pageInfo.getRecordsPerPage();
			List<T> datas = executeQuery(queryHql, currentPage, limit, paramMap);
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

	@Override
	public boolean exists(PK id) {
		return getById(id) != null;
	}

	protected <T> List<T> executeQuery(final String hql, final int currentPage,
			final int recordsPerPage, final Map<String, Object> paramMap) {
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
		List<T> results = query.list();
		return results;
	}

	@Override
	public void deleteByIdLogic(PK id) {
		T t = getById(id);
		t.setFlag(-1);
		getSession().update(t);
	}
	
	@Override
	public void deleteById(PK id) {
		getSession().delete(getById(id));
	}

}
