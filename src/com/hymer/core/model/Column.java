package com.hymer.core.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({ "value", "type" })
public class Column {

	/** The key. */
	private String key;

	/** The header. */
	private String header;

	/** The value. */
	private Object value;

	/** The type. */
	private String type = "java.lang.String";

	/** The format. */
	private String format;

	/** The width. */
	private String width = "10%";

	/** The align. */
	private String align = "left";

	/** The display. */
	private boolean display = true;

	/** The allow sort. */
	private boolean allowSort = false;

	/** The allow search. */
	private boolean allowSearch = false;

	/** The operator. */
	private String operator = Condition.EQ;

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the align.
	 * 
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * Gets the format.
	 * 
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * Checks if is allow search.
	 * 
	 * @return the allowSearch
	 */
	public boolean isAllowSearch() {
		return allowSearch;
	}

	/**
	 * Checks if is allow sort.
	 * 
	 * @return the allowSort
	 */
	public boolean isAllowSort() {
		return allowSort;
	}

	/**
	 * Checks if is display.
	 * 
	 * @return the display
	 */
	public boolean isDisplay() {
		return display;
	}

	/**
	 * Sets the align.
	 * 
	 * @param align
	 *            the align to set
	 */
	public void setAlign(String align) {
		this.align = align;
	}

	/**
	 * Sets the allow search.
	 * 
	 * @param allowSearch
	 *            the allowSearch to set
	 */
	public void setAllowSearch(boolean allowSearch) {
		this.allowSearch = allowSearch;
	}

	/**
	 * Sets the allow sort.
	 * 
	 * @param allowSort
	 *            the allowSort to set
	 */
	public void setAllowSort(boolean allowSort) {
		this.allowSort = allowSort;
	}

	/**
	 * Sets the display.
	 * 
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(boolean display) {
		this.display = display;
	}

	/**
	 * Sets the format.
	 * 
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Sets the header.
	 * 
	 * @param header
	 *            the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Sets the key.
	 * 
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Sets the width.
	 * 
	 * @param width
	 *            the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
}
