package com.hymer.core.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hymer.core.BaseContoller;
import com.hymer.core.ServiceException;
import com.hymer.core.dto.NodeDTO;
import com.hymer.core.entity.Node;
import com.hymer.core.entity.User;
import com.hymer.core.entity.Workflow;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.service.UserService;
import com.hymer.core.service.WorkflowService;
import com.hymer.core.util.JsonUtils;

@Controller
public class WorkflowController extends BaseContoller {

	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/workflow/workflow/{id}.html", method = RequestMethod.GET)
	public @ResponseBody
	Workflow getResource(@PathVariable Long id) throws ServiceException {
		Workflow workflow = null;
		workflow = workflowService.getById(id);
		return workflow;
	}

	@RequestMapping(value = "/workflow/node/{id}.html", method = RequestMethod.GET)
	public @ResponseBody
	NodeDTO getNode(@PathVariable Long id) throws ServiceException {
		Node node = null;
		node = workflowService.getNodeById(id);
		NodeDTO dto = new NodeDTO(node);
		return dto;
	}

	@RequestMapping(value = "/workflow/edit.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON saveOrUpdateWorkflow(HttpServletRequest request,
			HttpServletResponse response, Workflow workflow)
			throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		Long id = workflow.getId();
		if (id == null) {
			workflowService.save(workflow);
		} else {
			Workflow entity = workflowService.getById(id);
			entity.setName(workflow.getName());
			entity.setCode(workflow.getCode());
			workflowService.update(entity);
		}
		json.setMsg("保存成功！");
		return json;
	}

	@RequestMapping(value = "/workflow/query.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON queryWorkflow(HttpServletRequest request,
			HttpServletResponse response) {
		String queryString = request.getParameter("query");
		ResponseJSON json = null;
		try {
			QueryObject queryObject = null;
			queryObject = JsonUtils.fromJson(queryString, QueryObject.class);
			json = workflowService.query(queryObject);
			log.info("json = " + JsonUtils.toJson(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/workflow/node/query.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON queryNode(HttpServletRequest request,
			HttpServletResponse response) {
		String queryString = request.getParameter("query");
		ResponseJSON json = null;
		try {
			QueryObject queryObject = null;
			queryObject = JsonUtils.fromJson(queryString, QueryObject.class);
			json = workflowService.queryNode(queryObject);
			log.info("json = " + JsonUtils.toJson(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/workflow/node/edit.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON saveOrUpdateNode(HttpServletRequest request,
			HttpServletResponse response, NodeDTO dto) throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		Node yNode = null, nNode = null;
		User processUser = null;
		if (dto.getyNodeId() != null) {
			yNode = workflowService.getNodeById(dto.getyNodeId());
		}
		if (dto.getnNodeId() != null) {
			nNode = workflowService.getNodeById(dto.getnNodeId());
		}
		if (dto.getUserId() != null) {
			processUser = userService.getUserById(dto.getUserId());
		}
		// 如果是起始节点，则检查该流程是否已存在起始节点
		if (dto.isStartPoint()) {
			List<Node> startPoints = workflowService.getStartPoint(dto
					.getWorkflowId());
			if (startPoints.size() > 0) {
				if (startPoints.size() == 1 && dto.getId() != null
						&& startPoints.get(0).getId().equals(dto.getId())) {

				} else {
					json.setResult(false);
					json.setMsg("该流程中已存在起始节点，一个流程只能存在一个起始节点！");
					return json;
				}
			}
		}
		Node entity = null;
		if (dto.getId() == null) {
			Workflow workflow = workflowService.getById(dto.getWorkflowId());
			entity = (Node) dto.toEntity();
			entity.setWorkflow(workflow);
			entity.setyNode(yNode);
			entity.setnNode(nNode);
			entity.setUser(processUser);
			workflowService.save(entity);
		} else {
			entity = workflowService.getNodeById(dto.getId());
			entity.setName(dto.getName());
			entity.setyNode(yNode);
			entity.setnNode(nNode);
			entity.setStartPoint(dto.isStartPoint());
			entity.setEndPoint(dto.isEndPoint());
			entity.setUser(processUser);
			workflowService.update(entity);
		}
		json.setMsg("保存成功！");
		return json;
	}

	@RequestMapping(value = "/workflow/node/list.html", method = RequestMethod.GET)
	public @ResponseBody
	ResponseJSON getNodeList(HttpServletRequest request,
			HttpServletResponse response) {
		ResponseJSON json = new ResponseJSON();
		Long id = Long.parseLong((String) request.getParameter("workflowId"));
		List<Node> nodes = workflowService.getNodesByWorkflowId(id);
		json.put("nodes", nodes);
		return json;
	}

	@RequestMapping(value = "/workflow/workflow/delete.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON deleteWorkflow(HttpServletRequest request,
			HttpServletResponse response) {
		ResponseJSON json = new ResponseJSON();
		String idString = request.getParameter("id");
		if (StringUtils.hasText(idString)) {
			Long id = Long.parseLong(idString);
			try {
				workflowService.deleteWorkflowById(id);
				json.setMsg("删除成功!");
			} catch (Exception e) {
				json.setResult(false);
				json.setMsg("该流程已被占用，无法删除!");
			}
		} else {
			json.setMsg("没有删除任何流程.");
		}
		return json;
	}
	
	@RequestMapping(value = "/workflow/node/delete.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON deleteNode(HttpServletRequest request,
			HttpServletResponse response) {
		ResponseJSON json = new ResponseJSON();
		String idString = request.getParameter("id");
		if (StringUtils.hasText(idString)) {
			Long id = Long.parseLong(idString);
			try {
				workflowService.deleteNodeById(id);
				json.setMsg("删除成功!");
			} catch (Exception e) {
				json.setResult(false);
				json.setMsg("节点已与其他节点绑定，无法删除，请先将节点孤立!");
			}
		} else {
			json.setMsg("没有删除任何节点.");
		}
		return json;
	}

}
