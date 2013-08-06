package com.hymer.core;

/**
 * 状态机
 * @author hymer
 *
 */
public interface State {
	/**
	 * 初始状态
	 */
	public static final Integer INITIAL = 0;
	/**
	 * 未通过
	 */
	public static final Integer NOT_PASS = -3;
	
	/**
	 * 已完成
	 */
	public static final Integer FINISHED = -1;
	
	/**
	 * 已取消
	 */
	public static final Integer CANCELED = -2;
	
	/**
	 * 被退回，正在修改
	 */
	public static final Integer BACKING = -99;
	
}
