package com.hymer.core.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式化工具类
 * 
 * @author hymer
 * 
 */
public class Formatters {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat SDF = new SimpleDateFormat(
			DEFAULT_DATE_FORMAT);
	public static final SimpleDateFormat TSDF = new SimpleDateFormat(
			DEFAULT_TIME_FORMAT);

	public static final String formatDate(Date date) {
		if (date == null)
			return "";
		return SDF.format(date);
	}

	public static final String formatDate(Date date, String format) {
		if (date == null)
			return "";
		return new SimpleDateFormat(format).format(date);
	}

	public static final String formatTime(Date time) {
		if (time == null)
			return "";
		return TSDF.format(time);
	}

	public static final Date parseDate(String dateString) {
		try {
			return SDF.parse(dateString);
		} catch (ParseException e) {
		}
		return null;
	}

	public static final Date parseDate(String dateString, String format)
			throws ParseException {
		return new SimpleDateFormat(format).parse(dateString);
	}

	/**
	 * 保留{setPrecision}位小数，四舍五入
	 * 
	 * @param src
	 * @param setPrecision
	 * @return
	 */
	public static final Double formatDouble(Double src, int setPrecision) {
		MathContext v = new MathContext(setPrecision, RoundingMode.HALF_DOWN);
		BigDecimal a = new BigDecimal(src, v);
		return a.doubleValue();
	}

	public static String formatNumber(Number src) {
		// TODO Auto-generated method stub
		return null;
	}

}
