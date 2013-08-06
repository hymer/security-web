package com.hymer.core.util;

import jxl.Cell;
import jxl.Sheet;

import org.apache.commons.lang.StringUtils;

/**
 * @author hymer
 * 
 */
public class ExcelUtils {

	public static String[][] getDataFromSheet(Sheet sheet) {
		int rowsCount = sheet.getRows();
		String[][] datas = new String[rowsCount][];
		for (int row = 0; row < rowsCount; row++) {
			Cell[] cells = sheet.getRow(row);
			int colsCount = cells.length;
			datas[row] = new String[colsCount];
			for (int col = 0; col < colsCount; col++) {
				String value = cells[col].getContents();
				datas[row][col] = StringUtils.isBlank(value) ? "" : value;
			}
		}
		return datas;
	}

}
