package com.hymer.core;

import java.util.Collection;
import java.util.List;

import com.hymer.core.entity.User;
import com.hymer.core.model.Column;
import com.hymer.core.model.Query;
import com.hymer.core.model.QueryObject;

/*
 * 默认使用QueryService进行列表查询，如果需要加入特定元素或者处理，实现此接口
 */
public interface IQueryHandler {
	
	/**
	 * 列过滤器，可用此方法对列数据进行过滤，比如控制前端显示的列
	 * @param query
	 * @param user
	 */
	Collection<Column> filterColumns(final Query query, final User user);

	/**
	 * 在设置查询条件前，对缓存在内存中的Query对象以及前台传过来的QueryObject对象进行预处理 此方法用来处理前台传递过来的查询条件
	 * 
	 * @param query
	 * @param queryObject
	 */
	void beforeConditionsSetter(final Query query,
			final QueryObject queryObject, final User user);

	/**
	 * 在处理了查询条件后，对缓存在内存中的Query对象以及前台传过来的QueryObject对象进行处理
	 * 此方法用来处理整理好后的查询条件，比如加上权限过滤等
	 * 
	 * @param query
	 * @param queryObject
	 */
	void afterConditionsSetter(final Query query,
			final QueryObject queryObject, final User user);

	/**
	 * 将查询得到的实体集进行处理，如不处理，请直接返回datas
	 * 
	 * @param datas
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List handlerDatas(final List datas, final Query query, final User user);

}
