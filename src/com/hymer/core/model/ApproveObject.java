package com.hymer.core.model;

/**
 * 审批信息的数据载体
 * 
 * @author hymer
 * 
 */
public class ApproveObject {

	public boolean pass = false; // 审批结果，yes / no
	public String content; // 审批内容，附带的意见或者是结果说明等

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
