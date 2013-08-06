package com.hymer.core.model;

import java.util.ArrayList;
import java.util.List;


public class QueryObject {
	private String queryId;
	private List<Condition> conditions = new ArrayList<Condition>();
	private PageInfo pageinfo;
	private SortInfo sortinfo;

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public PageInfo getPageinfo() {
		return pageinfo;
	}

	public void setPageinfo(PageInfo pageinfo) {
		this.pageinfo = pageinfo;
	}

	public SortInfo getSortinfo() {
		return sortinfo;
	}

	public void setSortinfo(SortInfo sortinfo) {
		this.sortinfo = sortinfo;
	}
	
	public void setSortString(String sortString) {
		this.sortinfo = new SortInfo(sortString);
	}
	

}
