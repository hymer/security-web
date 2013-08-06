package com.hymer.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hymer.core.CommonService;
import com.hymer.core.dao.NodeDAO;
import com.hymer.core.dao.WorkflowDAO;
import com.hymer.core.dao.WorkflowInstanceDAO;
import com.hymer.core.dto.NodeDTO;
import com.hymer.core.dto.WorkflowDTO;
import com.hymer.core.entity.Node;
import com.hymer.core.entity.Workflow;
import com.hymer.core.entity.WorkflowInstance;
import com.hymer.core.model.Condition;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;

@Service
public class WorkflowService extends CommonService {

	@Autowired
	private NodeDAO nodeDAO;
	@Autowired
	private WorkflowDAO workflowDAO;
	@Autowired
	private WorkflowInstanceDAO workflowInstanceDAO;

	public Workflow getById(Long id) {
		return workflowDAO.getById(id);
	}

	public Workflow getByCode(String code) {
		Workflow workflow = null;
		List<Workflow> entities = workflowDAO.getByProperty("code", code);
		if (entities.size() > 0) {
			workflow = entities.get(0);
		}
		return workflow;
	}

	public WorkflowInstance getInstanceById(Long id) {
		return workflowInstanceDAO.getById(id);
	}

	public Node getNodeById(Long id) {
		return nodeDAO.getById(id);
	}

	public List<Node> getNodesByWorkflowId(Long workflowId) {
		Condition condition = new Condition("workflow.id", workflowId);
		return nodeDAO.getByCondition(condition);
	}

	@SuppressWarnings("unchecked")
	public ResponseJSON query(QueryObject queryObject) {
		List<Condition> realConditions = new ArrayList<Condition>();
		for (Condition condition : queryObject.getConditions()) {
			if (condition.getValue() == null
					|| !StringUtils.hasText(condition.getValue().toString())) {
				continue;
			}
			if (condition.getKey().equals("name")) {
				condition.setOperator(Condition.LIKE);
				realConditions.add(condition);
			} else if (condition.getKey().equals("code")) {
				condition.setOperator(Condition.LIKE);
				realConditions.add(condition);
			}
		}
		queryObject.setConditions(realConditions);
		ResponseJSON json = workflowDAO.getAll(queryObject);
		List<WorkflowDTO> dtos = new ArrayList<WorkflowDTO>();
		for (Workflow ar : (List<Workflow>) json.getData().get("data")) {
			WorkflowDTO dto = new WorkflowDTO(ar);
			dtos.add(dto);
		}
		json.put("data", dtos);
		return json;
	}

	@SuppressWarnings("unchecked")
	public ResponseJSON queryNode(QueryObject queryObject) {
		List<Condition> realConditions = new ArrayList<Condition>();
		for (Condition condition : queryObject.getConditions()) {
			if (condition.getValue() == null
					|| !StringUtils.hasText(condition.getValue().toString())) {
				continue;
			}
			if (condition.getKey().equals("workflowId")) {
				condition.setKey("workflow.id");
				realConditions.add(condition);
			} else if (condition.getKey().equals("name")) {
				condition.setOperator(Condition.LIKE);
				realConditions.add(condition);
			}
		}
		queryObject.setConditions(realConditions);
		queryObject.setSortString("id asc");
		ResponseJSON json = nodeDAO.getAll(queryObject);
		List<NodeDTO> dtos = new ArrayList<NodeDTO>();
		for (Node node : (List<Node>) json.getData().get("data")) {
			NodeDTO dto = new NodeDTO(node);
			dtos.add(dto);
		}
		json.put("data", dtos);
		return json;
	}

	public void deleteNodeById(Long id) {
		Node node = getNodeById(id);
		nodeDAO.delete(node);
	}

	public List<Node> getStartPoint(Long workflowId) {
		List<Node> nodes = null;
		Condition condition = new Condition();
		condition.setKey("workflow.id");
		condition.setOperator(Condition.EQ);
		condition.setValue(workflowId);
		Condition condition2 = new Condition();
		condition2.setKey("startPoint");
		condition2.setOperator(Condition.EQ);
		condition2.setValue(true);
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(condition);
		conditions.add(condition2);
		nodes = nodeDAO.getByConditions(conditions);
		return nodes;
	}

	public void deleteWorkflowById(Long id) {
		Workflow workflow = getById(id);
		workflowDAO.delete(workflow);
	}

}
