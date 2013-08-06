package com.hymer.core.dao;

import org.springframework.stereotype.Repository;

import com.hymer.core.DAOHibernate;
import com.hymer.core.entity.User;

@Repository
public class UserDAO extends DAOHibernate<User, Long> {
	
}
