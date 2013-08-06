package com.hymer.core;

import java.util.List;

import com.hymer.core.model.Condition;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;

public interface IDAO<T extends BaseEntity, PK extends java.io.Serializable> {

	void deleteByIdLogic(PK id);
	
	void deleteById(PK id);

	T getById(PK id);
	
	List<T> getByProperty(String propertyName, Object propertyValue);

	List<T> getByCondition(Condition... conditions);

	List<T> getByCondition(String sortString, Condition... conditions);
	
	List<T> getByCondition(Condition condition);
	
	List<T> getByConditions(List<Condition> conditions);

	List<T> getByCondition(Condition condition, String sortString);
	
	List<T> getByConditions(List<Condition> conditions, String sortString);

	List<T> getAll();

	List<T> getAll(String orderString);

	ResponseJSON getAll(QueryObject queryObject);

	boolean exists(PK id);



}
