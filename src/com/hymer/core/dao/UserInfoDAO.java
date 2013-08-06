package com.hymer.core.dao;

import org.springframework.stereotype.Repository;

import com.hymer.core.DAOHibernate;
import com.hymer.core.entity.UserInfo;

@Repository
public class UserInfoDAO extends DAOHibernate<UserInfo, Long> {
	
}
