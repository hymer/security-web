package com.hymer.core.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.hymer.core.xmlparser.QueryContextHandler;

public class QueryContext {
	
	private static final String[] QUERY_XMLS = new String[] { "querys.xml" };
	
	// 缓存所有Query于此
	public static final Map<String, Query> CACHED = new HashMap<String, Query>();

	public static void addQuery(Query query) {
		CACHED.put(query.getId(), query);
	}

	public static Query getQuery(String id) {
		return CACHED.get(id);
	}
	
	/**
	 * @param xmlPath
	 * @param dicts
	 */
	public static void parseQuerys() {
		try {
			for (String xmlPath : QUERY_XMLS) {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				XMLReader reader = factory.newSAXParser().getXMLReader();
				QueryContextHandler handler = new QueryContextHandler();
				reader.setContentHandler(handler);
				reader.parse(new InputSource(QueryContext.class.getClassLoader()
						.getResourceAsStream(xmlPath)));
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
