package com.hymer.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.hymer.core.model.Condition;

@Repository
public class CommonDAO implements ICommonDAO {
	
	protected Log log = LogFactory.getLog(getClass());
	
	protected final String DEFAULT_ALIAS = "_default_";
	protected String FROM;
	protected final String WHERE = " where 1=1 ";
	protected final String COUNT_PRE = "select count(*) ";

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}

	@Override
	public <T extends BaseEntity> T save(T model) {
		if (model.getCreateTime() == null) {
			model.setCreateTime(new Date());
		}
		getSession().save(model);
		return model;
	}

	@Override
	public <T extends BaseEntity> void saveOrUpdate(T model) {
		if (model.getCreateTime() == null) {
			model.setCreateTime(new Date());
		}
		getSession().saveOrUpdate(model);
	}

	@Override
	public <T extends BaseEntity> void update(T model) {
		getSession().update(model);
	}

	/**
	 * 物理删除
	 */
	@Override
	public <T extends BaseEntity> void delete(T model) {
		getSession().delete(model);
	}
	
	/**
	 * 将flag设置为-1，标记为已删除，用于业务数据 
	 */
	@Override
	public <T extends BaseEntity> void deleteLogic(T model) {
		model.setFlag(-1);
		getSession().update(model);
	}

	public List<?> executeHqlQuery(String hql) {
		Query query = getSession().createQuery(hql);
		return query.list();
	}

	public Object executeHqlUnique(String hql) {
		Query query = getSession().createQuery(hql);
		return query.uniqueResult();
	}

	public List<?> executeHqlQuery(String hql, Map<String, Object> parameters) {
		Query query = getSession().createQuery(hql);
		setParameters(query, parameters);
		return query.list();
	}

	public int executeHqlUpdate(String hql, Map<String, Object> parameters) {
		Query query = getSession().createQuery(hql);
		setParameters(query, parameters);
		return query.executeUpdate();
	}

	public Object executeSqlQuery(String sql) {
		SQLQuery query = getSession().createSQLQuery(sql);
		return query.list();
	}

	public Object executeSqlUnique(String sql) {
		SQLQuery query = getSession().createSQLQuery(sql);
		return query.uniqueResult();
	}

	public Object executeSqlQuery(String sql, Map<String, Object> parameters) {
		SQLQuery query = getSession().createSQLQuery(sql);
		setParameters(query, parameters);
		return query.list();
	}

	public int executeSqlUpdate(String sql, Map<String, Object> parameters) {
		SQLQuery query = getSession().createSQLQuery(sql);
		setParameters(query, parameters);
		return query.executeUpdate();
	}

	protected void setParameter(Query query, String paramName, Object paramValue) {
		if (paramValue instanceof String) {
			query.setString(paramName, (String) paramValue);
		} else if (paramValue instanceof Integer) {
			query.setInteger(paramName, (Integer) paramValue);
		} else if (paramValue instanceof Long) {
			query.setLong(paramName, (Long) paramValue);
		} else if (paramValue instanceof Double) {
			query.setDouble(paramName, (Double) paramValue);
		} else if (paramValue instanceof Boolean) {
			query.setBoolean(paramName, (Boolean) paramValue);
		} else if (paramValue instanceof Date) {
			// TODO 难道这是bug 使用setParameter不行？？
			query.setTimestamp(paramName, (Date) paramValue);
		} else if (paramValue instanceof Collection) {
			query.setParameterList(paramName, (Collection<?>) paramValue);
		} else {
			query.setParameter(paramName, paramValue);
		}
	}

	protected void setParameters(Query query, Map<String, Object> paramMap) {
		if (paramMap != null) {
			Iterator<String> keys = paramMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				Object value = paramMap.get(key);
				setParameter(query, key, value);
			}
		}
	}
	
	/**
	 * 如果key形如：user.id， 则处理成：user_id_0形式， 如果Condition有多个，则传入index，
	 * 处理成：user_id_index形式
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	protected String formatParamKey(String key, int index) {
		String paramKey = null;
		if (key.indexOf('.') != -1) {
			paramKey = key.replace(".", "_") + "_" + index;
		} else {
			paramKey = key + "_" + index;
		}
		return paramKey;
	}
	
	@SuppressWarnings("rawtypes")
	protected String processConditions(List<Condition> conditions,
			Map<String, Object> paramMap) {
		StringBuffer temp = new StringBuffer("");
		int index = 0;
		for (Condition condition : conditions) {
			String key = condition.getKey();
			Object value = condition.getValue();
			if (StringUtils.hasText(key) && value != null) {
				String paramKey = formatParamKey(key, index);
				// if operator is ' between ', deal with it.
				if (condition.getOperator().equals(Condition.BETWEEN)) {
					String param1 = paramKey + "0";
					String param2 = paramKey + "1";
					temp.append(" and " + DEFAULT_ALIAS + "."
							+ condition.getKey() + condition.getOperator()
							+ ":" + param1 + " and :" + param2);
					if (value instanceof String) {
						Object[] values = value.toString().split(
								Condition.JOIN_SYMBOL);
						Object[] vs = new Object[2];
						if (condition.getValueType().equals(Integer.class)) {
							vs[0] = Integer.parseInt(values[0].toString());
							vs[1] = Integer.parseInt(values[1].toString());
						} else if (condition.getValueType().equals(Long.class)) {
							vs[0] = Long.parseLong(values[0].toString());
							vs[1] = Long.parseLong(values[1].toString());
						} else if (condition.getValueType()
								.equals(Double.class)) {
							vs[0] = Double.parseDouble(values[0].toString());
							vs[1] = Double.parseDouble(values[1].toString());
						} else {
							vs = values;
						}
						paramMap.put(param1, values[0]);
						paramMap.put(param2, values[1]);
					} else if (value instanceof Collection) {
						paramMap.put(param1, ((Collection) value).toArray()[0]);
						paramMap.put(param2, ((Collection) value).toArray()[1]);
					}
				} else {
					// if operator is ' like ', add pre-end symbol '%'.
					if (condition.getOperator().equals(Condition.LIKE)) {
						value = Condition.LIKE_SYMBOL + value.toString()
								+ Condition.LIKE_SYMBOL;
						// if operator is ' in ', change the string(joined with
						// commas) to a collection.
					} else if (condition.getOperator().equals(Condition.IN)) {
						if (value instanceof String) {
							String[] arrays = value.toString().split(
									Condition.JOIN_SYMBOL);
							List<Object> list = new ArrayList<Object>();
							if (condition.getValueType().equals(Long.class)) {
								for (String string : arrays) {
									list.add(Long.parseLong(string));
								}
							} else if (condition.getValueType().equals(
									Integer.class)) {
								for (String string : arrays) {
									list.add(Integer.parseInt(string));
								}
							} else if (condition.getValueType().equals(
									Double.class)) {
								for (String string : arrays) {
									list.add(Double.parseDouble(string));
								}
							} else {
								for (String string : arrays) {
									list.add(string);
								}
							}
							value = list;
						}
						// if operator is ' between ', do sth.
					} else {
						if (condition.getValueType().equals(Long.class)
								&& (condition.getValue() instanceof String)) {
							condition.setValue(Long
									.parseLong((String) condition.getValue()));
						} else if (condition.getValueType().equals(
								Integer.class)
								&& (condition.getValue() instanceof String)) {
							condition.setValue(Integer
									.parseInt((String) condition.getValue()));
						} else if (condition.getValueType()
								.equals(Double.class)
								&& (condition.getValue() instanceof String)) {
							condition
									.setValue(Double
											.parseDouble((String) condition
													.getValue()));
						} else if (condition.getValueType().equals(
								Boolean.class)
								&& (condition.getValue() instanceof String)) {
							condition
									.setValue(Boolean
											.parseBoolean((String) condition
													.getValue()));
						}
					}
					temp.append(" and " + DEFAULT_ALIAS + "."
							+ condition.getKey() + condition.getOperator()
							+ ":" + paramKey);
					paramMap.put(paramKey, value);
				}
			}
			index++;
		}
		return temp.toString();
	}
	
	/**
	 * 根据查询条件返回唯一一条记录
	 */
	@SuppressWarnings("unchecked")
	protected <O> O unique(final String hql, Class<O> resultType,
			final Map<String, Object> paramMap) {
		Query query = getSession().createQuery(hql);
		setParameters(query, paramMap);
		return (O) query.setMaxResults(1).uniqueResult();
	}
	
}
