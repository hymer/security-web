package com.hymer.core.dao;

import org.springframework.stereotype.Repository;

import com.hymer.core.DAOHibernate;
import com.hymer.core.entity.FileEntity;

@Repository
public class FileDAO extends DAOHibernate<FileEntity, Long> {

}
