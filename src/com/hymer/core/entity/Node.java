package com.hymer.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.hymer.core.BaseEntity;

@Entity
@Table(name = "tb_workflow_node")
public class Node extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Length(max = 200)
	@Column(length = 200)
	private String name;

	@ManyToOne
	@JoinColumn(name = "workflowId")
	private Workflow workflow;
	@ManyToOne
	@JoinColumn(name = "yNodeId")
	private Node yNode;
	@ManyToOne
	@JoinColumn(name = "nNodeId")
	private Node nNode;
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	private boolean startPoint = false;
	private boolean endPoint = false;

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

	public Node getyNode() {
		return yNode;
	}

	public void setyNode(Node yNode) {
		this.yNode = yNode;
	}

	public Node getnNode() {
		return nNode;
	}

	public void setnNode(Node nNode) {
		this.nNode = nNode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

}
