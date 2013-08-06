package com.hymer.core;

import java.util.List;

import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;

public interface IService<T extends BaseEntity, PK extends java.io.Serializable> {

	T save(T entity);

	void saveOrUpdate(T entity);

	void update(T entity);

	void merge(T entity);

	void delete(PK id);

	void deleteObject(T entity);

	T get(PK id);

	int countAll();

	List<T> listAll();

	ResponseJSON findAll(QueryObject queryObject);

}
