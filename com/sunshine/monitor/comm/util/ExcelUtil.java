package com.sunshine.monitor.comm.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 对excel文件进行操作
 * 
 * @author maijial
 * 
 */
public class ExcelUtil {

	/**
	 * xls content to List<bean> 
	 * 
	 * @param excelFile
	 * @param clazz
	 * @return
	 */
	public static List xlsToBean(MultipartFile excelFile, Class clazz) {
		List<Object> list = new ArrayList<Object>();
		if (excelFile != null && !excelFile.isEmpty()) {
			try {
				Map<Integer, String> methodNames = new HashMap<Integer, String>();
				HSSFWorkbook book = new HSSFWorkbook(excelFile.getInputStream());
				HSSFSheet sheet = book.getSheetAt(0);
				for (int i = 0; i <= sheet.getLastRowNum(); i++) {
					Object obj = clazz.newInstance();
					HSSFRow row = sheet.getRow(i);
					if (row == null)
						continue;
					if (i == 0) {
						for (int j = 0; j < row.getLastCellNum(); j++) {
							HSSFCell cell = row.getCell((short) j);
							if (cell == null)
								continue;
							methodNames.put(j, setter(getValue(cell)));
						}
					} else {
						for (int k = 0; k < row.getLastCellNum(); k++) {
							HSSFCell cell = row.getCell((short) k);
							if (cell == null)
								continue;
							String methodName = methodNames.get(k);
							Method method = clazz.getMethod(methodName,String.class);
							method.invoke(obj, getValue(cell));
						}
						list.add(obj);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
    
	/**
	 * get Cell Value
	 * @param cell
	 * @return
	 */
	private static String getValue(HSSFCell cell) {
		if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			return String.valueOf((int)cell.getNumericCellValue());
		} else {
			return String.valueOf(cell.getStringCellValue());
		}
	}
    
	/**
	 * get setter name
	 * @param value
	 * @return
	 */
	private static String setter(String value) {
		StringBuffer methodName = new StringBuffer(value.substring(value.indexOf("/") + 1));
		methodName.setCharAt(0, Character.toUpperCase(methodName.charAt(0)));
		return methodName.insert(0, "set").toString();
	}
}
