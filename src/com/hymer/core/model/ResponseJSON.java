package com.hymer.core.model;

import java.util.HashMap;
import java.util.Map;

import com.hymer.core.util.JsonUtils;

/**
 * Ajax请求返回的JSON数据标准格式
 * 
 * @author hymer
 * 
 */
public class ResponseJSON {

	private boolean error = false;
	private boolean result = true;
	private String msg;
	private Map<String, Object> data = new HashMap<String, Object>();

	public ResponseJSON() {
	}

	public ResponseJSON(boolean result) {
		this.result = result;
	}

	public ResponseJSON(String msg) {
		this.msg = msg;
	}

	public ResponseJSON(boolean result, String msg) {
		this.result = result;
		this.msg = msg;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void put(String key, Object value) {
		this.data.put(key, value);
	}

	public String toJSONString() {
		try {
			return JsonUtils.toJson(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
