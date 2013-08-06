package com.hymer.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hymer.core.entity.Role;
import com.hymer.core.entity.User;
import com.hymer.core.model.Column;
import com.hymer.core.model.Condition;
import com.hymer.core.model.Query;
import com.hymer.core.model.QueryObject;
import com.hymer.core.util.BeanUtils;

/**
 * 接口IQueryHandler的默认实现
 * 
 * @author hymer
 * 
 */
@Repository("defaultQueryHandler")
public class DefaultQueryHandler implements IQueryHandler {

	@Override
	public Collection<Column> filterColumns(Query query, User user) {
		List<Column> columns = new ArrayList<Column>();
		if (user != null) {
			for (Column column : query.getColumnsMap().values()) {
				if (!Role.SUPER_ROLE_FLAG.equals(user.getRole().getCode())) {
					if (column.getKey().equals("flag")) {
						continue;
					}
				}
				columns.add(column);
			}
		}
		return columns;
	}

	@Override
	public void beforeConditionsSetter(Query query, QueryObject queryObject,
			User user) {
		if (user == null) {
			throw new RuntimeException("必须登录方可查询");
		}
	}

	@Override
	public void afterConditionsSetter(Query query, QueryObject queryObject,
			User user) {
		if (user != null) {
			Role role = user.getRole();
			if (!Role.SUPER_ROLE_FLAG.equals(role.getCode())) {
				Condition condition = new Condition("flag", -1);
				condition.setOperator(Condition.NOT_EQ);
				queryObject.getConditions().add(condition);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List handlerDatas(List datas, Query query, User user) {
		List<Map> maps = new ArrayList<Map>();
		for (Object object : datas) {
			if (user != null) {
				Map map = null;
				if (!(object instanceof Map)) {
					try {
						map = BeanUtils.convertBean(object);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					map = (Map) object;
				}
				if (!Role.SUPER_ROLE_FLAG.equals(user.getRole().getCode())) {
					map.remove("flag");
				}
				maps.add(map);
			}
		}
		return datas;
	}

}
