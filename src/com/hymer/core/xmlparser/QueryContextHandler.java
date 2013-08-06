package com.hymer.core.xmlparser;

import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hymer.core.model.Column;
import com.hymer.core.model.Condition;
import com.hymer.core.model.Query;
import com.hymer.core.model.QueryContext;

/**
 * @author hymer
 * 
 */
public class QueryContextHandler extends DefaultHandler {
	private Query query = null;
	private Column column = null;
	
	private String tagName = null;
	private static final String QUERY_TAGNAME = "query";
	private static final String COLUMN_TAGNAME = "column";

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = qName;
		if (QUERY_TAGNAME.equals(tagName)) {
			query = new Query();
			String id = attributes.getValue("id");
			String header = attributes.getValue("header");
			String clazz = attributes.getValue("clazz");
			String handler = attributes.getValue("handler");
			String orderBy = attributes.getValue("orderby");
			String pageSize = attributes.getValue("pagesize");
			String filter = attributes.getValue("filter");
			query.setId(id);
			query.setClazz(clazz);
			query.setHeader(header);
			if (StringUtils.hasText(handler)) {
				query.setHandler(handler);
			}
			if (StringUtils.hasText(orderBy)) {
				query.setOrder(orderBy);
			}
			query.setFilter(filter);
			if (StringUtils.hasText(pageSize)) {
				query.setPagesize(Integer.parseInt(pageSize));
			}
		} else if(COLUMN_TAGNAME.equals(tagName)) {
			column = new Column();
			String key = attributes.getValue("key");
			String header = attributes.getValue("header");
			String width = attributes.getValue("width");
			String type = attributes.getValue("type");
			String operator = attributes.getValue("operator");
			String display = attributes.getValue("display");
			String allowSearch = attributes.getValue("allowSearch");
			String allowSort = attributes.getValue("allowSort");
			column.setKey(key);
			column.setHeader(header);
			column.setWidth(null == width ? "10%" : width);
			column.setType(null == type ? "java.lang.String" : type);
			column.setOperator(null == operator ? Condition.EQ : operator);
			column.setDisplay("false".equals(display) ? false : true);
			column.setAllowSearch("true".equals(allowSearch) ? true : false);
			column.setAllowSort("true".equals(allowSort) ? true : false);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (QUERY_TAGNAME.equals(qName)) {
			QueryContext.addQuery(query);
		} else if(COLUMN_TAGNAME.equals(qName)) {
			query.addColumn(column);
		}
		this.tagName = "";
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}
	
	@Override
	public void endDocument() throws SAXException {

	}
}
