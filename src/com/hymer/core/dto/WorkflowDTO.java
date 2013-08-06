package com.hymer.core.dto;

import com.hymer.core.BaseEntity;
import com.hymer.core.DTO;
import com.hymer.core.entity.Workflow;

public class WorkflowDTO extends DTO {
	
	private Long id;
	private String name;
	private String code;

	@Override
	public BaseEntity toEntity() {
		return super.toEntity();
	}
	
	public WorkflowDTO(Workflow workflow) {
		fromEntity(workflow);
	}

	@Override
	public void fromEntity(BaseEntity entity) {
		Workflow workflow = (Workflow) entity;
		this.setId(workflow.getId());
		this.setName(workflow.getName());
		this.setCode(workflow.getCode());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
