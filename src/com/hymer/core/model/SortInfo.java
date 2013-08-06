package com.hymer.core.model;

public class SortInfo {

	private String variableName;
	private int direction = 1;

	private String sortString;

	public SortInfo() {
	}

	public SortInfo(String variableName, int direction) {
		this.variableName = variableName;
		this.direction = direction;
	}

	public SortInfo(String sortString) {
		this.sortString = sortString;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		if (this.sortString != null) {
			return " order by " + this.sortString;
		}
		return " order by " + variableName
				+ ((direction < 0) ? " desc " : " asc ");
	}

}
