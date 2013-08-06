package com.hymer.core.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.util.StringUtils;

import com.hymer.core.BaseEntity;
import com.hymer.core.IQueryHandler;
import com.hymer.core.SpringHelper;

@JsonIgnoreProperties({ "clazz", "queryHandler" })
public class Query {

	private Log log = LogFactory.getLog(getClass());

	/** The id. */
	private String id;

	private Class<? extends BaseEntity> clazz;

	private String handler;

	private IQueryHandler queryHandler;

	/** The pagesize. */
	private int pagesize = 10;

	/** The header. */
	private String header;

	/** The filter. */
	private String filter;

	/** The order. */
	private String order = "id desc";

	/** The distinct. */
	private String distinct;

	private final Map<String, Column> columnsMap = new LinkedHashMap<String, Column>();
	private final StringBuffer statString = new StringBuffer();

	public void addColumn(Column column) {
		this.columnsMap.put(column.getKey(), column);
	}

	/**
	 * Gets the base class.
	 * 
	 * @return the baseClass
	 */
	public Class<? extends BaseEntity> getClazz() {
		return clazz;
	}

	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public String getHeader() {
		if (header != null) {
			return header;
		} else {
			return id;
		}
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the pagesize.
	 * 
	 * @return the pagesize
	 */
	public int getPagesize() {
		return pagesize;
	}

	/**
	 * Sets the base class.
	 * 
	 * @param baseClass
	 *            the baseClass to set
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public void setClazz(String clazz) {
		try {
			this.clazz = (Class<? extends BaseEntity>) Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the pagesize.
	 * 
	 * @param pagesize
	 *            the pagesize to set
	 */
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public Column getColumn(String key) {
		return columnsMap.get(key);
	}

	public Map<String, Column> getColumnsMap() {
		return columnsMap;
	}

	public String getStatString() {
		return statString.toString();
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getOrder() {
		return order;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getDistinct() {
		return distinct;
	}

	public void setDistinct(String distinct) {
		this.distinct = distinct;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	@SuppressWarnings("unchecked")
	public IQueryHandler getQueryHandler() {
		if (queryHandler == null && StringUtils.hasText(handler)) {
			Class<IQueryHandler> handlerClass = null;
			try {
				// 先通过名字取bean
				queryHandler = (IQueryHandler) SpringHelper.getBean(handler);
			} catch (Exception e) {
				log.info(e.getMessage());
				try {
					// 再通过类名取bean
					handlerClass = (Class<IQueryHandler>) Class
							.forName(handler);
					queryHandler = SpringHelper.getBean(handlerClass);
				} catch (Exception e2) {
					log.info(e2.getMessage());
					if (handlerClass != null) {
						try {
							// 最后通过类生成实例
							queryHandler = handlerClass.newInstance();
						} catch (Exception e1) {
							log.info(e1.getMessage());
						}
					}
				}
			}
		}
		return queryHandler;
	}

	public void setQueryHandler(IQueryHandler queryHandler) {
		this.queryHandler = queryHandler;
	}

}
