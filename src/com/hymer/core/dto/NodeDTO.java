package com.hymer.core.dto;

import com.hymer.core.BaseEntity;
import com.hymer.core.DTO;
import com.hymer.core.entity.Node;

public class NodeDTO extends DTO {

	private Long id;
	private Long workflowId;
	private String name;
	private boolean startPoint;
	private boolean endPoint;
	private Long userId;
	private String userName;
	private Long yNodeId;
	private String yNodeName;
	private Long nNodeId;
	private String nNodeName;

	@Override
	public BaseEntity toEntity() {
		Node entity = new Node();
		entity.setId(id);
		entity.setEndPoint(endPoint);
		entity.setName(name);
		entity.setStartPoint(startPoint);
		return entity;
	}

	public NodeDTO() {
		super();
	}

	public NodeDTO(Node node) {
		fromEntity(node);
	}

	@Override
	public void fromEntity(BaseEntity entity) {
		Node node = (Node) entity;
		this.setId(node.getId());
		this.setWorkflowId(node.getWorkflow().getId());
		this.setName(node.getName());
		this.setEndPoint(node.isEndPoint());
		this.setStartPoint(node.isStartPoint());
		if (node.getnNode() != null) {
			this.setnNodeId(node.getnNode().getId());
			this.setnNodeName(node.getnNode().getName());
		}
		if (node.getyNode() != null) {
			this.setyNodeId(node.getyNode().getId());
			this.setyNodeName(node.getyNode().getName());
		}
		if (node.getUser() != null) {
			this.setUserId(node.getUser().getId());
			this.setUserName(node.getUser().getUserName());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStartPoint() {
		return startPoint;
	}

	public void setStartPoint(boolean startPoint) {
		this.startPoint = startPoint;
	}

	public boolean isEndPoint() {
		return endPoint;
	}

	public void setEndPoint(boolean endPoint) {
		this.endPoint = endPoint;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getyNodeId() {
		return yNodeId;
	}

	public void setyNodeId(Long yNodeId) {
		this.yNodeId = yNodeId;
	}

	public String getyNodeName() {
		return yNodeName;
	}

	public void setyNodeName(String yNodeName) {
		this.yNodeName = yNodeName;
	}

	public Long getnNodeId() {
		return nNodeId;
	}

	public void setnNodeId(Long nNodeId) {
		this.nNodeId = nNodeId;
	}

	public String getnNodeName() {
		return nNodeName;
	}

	public void setnNodeName(String nNodeName) {
		this.nNodeName = nNodeName;
	}

}
