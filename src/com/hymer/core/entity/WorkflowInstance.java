package com.hymer.core.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hymer.core.BaseEntity;

@Entity
@Table(name = "tb_workflow_instance")
public class WorkflowInstance extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "workflowId")
	private Workflow workflow;
	@OneToOne
	@JoinColumn(name = "currentNodeId")
	private Node currentNode;
	
	private boolean finished = false;

	public WorkflowInstance() {

	}

	public WorkflowInstance(Workflow workflow) {
		this.workflow = workflow;
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}
