package com.hymer.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hymer.core.CommonService;
import com.hymer.core.dao.MessageDAO;
import com.hymer.core.dto.MessageDTO;
import com.hymer.core.entity.Message;
import com.hymer.core.entity.User;
import com.hymer.core.model.Condition;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;

@Service
public class MessageService extends CommonService {
	@Autowired
	private MessageDAO messageDAO;

	public long getNewMessageCount(User user) {
		String hql = "select count(m.id) from Message m where m.toUser.id = "
				+ user.getId() + " and m.readed is FALSE";
		long counts = 0;
		counts = (Long) messageDAO.executeHqlUnique(hql);
		return counts;
	}

	public List<Message> getMessagesByToUser(User user) {
		List<Message> msgs = null;
		Condition condition = new Condition("toUser.id", user.getId());
		Condition condition2 = new Condition("readed", false);
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(condition);
		conditions.add(condition2);
		msgs = messageDAO.getByConditions(conditions,
				"readed asc, createTime desc");
		return msgs;
	}

	@SuppressWarnings("unchecked")
	public ResponseJSON query(QueryObject queryObject) {
		ResponseJSON json = messageDAO.getAll(queryObject);
		List<Message> datas = (List<Message>) json.getData().get("data");
		List<MessageDTO> dtos = new ArrayList<MessageDTO>();
		for (Message message : datas) {
			MessageDTO dto = new MessageDTO(message);
			dtos.add(dto);
		}
		json.getData().put("data", dtos);
		return json;
	}

	public void updateMessage2Readed(Long id) {
		Message message = messageDAO.getById(id);
		message.setReaded(true);
		messageDAO.update(message);
	}

	public void deleteById(Long id) {
		Message message = messageDAO.getById(id);
		messageDAO.delete(message);

	}

	public int updateMessages2Readed(List<Long> ids) {
		String hql = "update Message set readed = :readed where id in :id";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("readed", true);
		parameters.put("id", ids);
		return messageDAO.executeHqlUpdate(hql, parameters);
	}

	public int deleteByIds(List<Long> ids) {
		String hql = "delete from Message where id in :id";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", ids);
		return messageDAO.executeHqlUpdate(hql, parameters);
	}

}
