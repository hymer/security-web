package com.hymer.core.dao;

import org.springframework.stereotype.Repository;

import com.hymer.core.DAOHibernate;
import com.hymer.core.entity.Message;

@Repository
public class MessageDAO extends DAOHibernate<Message, Long> {

}
