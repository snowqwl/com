package com.sunshine.monitor.comm.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelStyle {
	
	/**
	 * Excel的字体style
	 * @param wb
	 * @return
	 */
	public static HSSFFont excelFont(HSSFWorkbook wb){
		HSSFFont  font = wb.createFont();   
	    font.setFontName("Verdana");   
	    font.setBoldweight((short) 100);   
	    font.setFontHeight((short) 300);   
	    font.setColor(HSSFColor.BLUE.index); 
		return font;
	}
	
	/**
	 * 样式1
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle excelStyle(HSSFWorkbook wb){
		 // 创建单元格样式   
	    HSSFCellStyle style = wb.createCellStyle();   
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);   
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);   
	    style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);   
	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
	    
	 // 设置边框   
	    style.setBottomBorderColor(HSSFColor.RED.index);   
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);   
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);   
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);   
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	   // style.setFont(ExcelStyle.excelFont(wb));
	    return style;
	}
	
	/**
	 * 样式2
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle excelStyle2(HSSFWorkbook wb){
		 // 创建单元格样式   
	    HSSFCellStyle style = wb.createCellStyle();   
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);   
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);   
	    style.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);   
	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
	    
	 // 设置边框   
	    style.setBottomBorderColor(HSSFColor.RED.index);   
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);   
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);   
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);   
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	   // style.setFont(ExcelStyle.excelFont(wb));
	    return style;
	}
	
	/**
	 * 样式3
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle excelStyle3(HSSFWorkbook wb){
		 // 创建单元格样式   
	    HSSFCellStyle style = wb.createCellStyle();   
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);   
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);   
	    style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);   
	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
	    
	 // 设置边框   
	    style.setBottomBorderColor(HSSFColor.RED.index);   
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);   
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);   
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);   
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	   // style.setFont(ExcelStyle.excelFont(wb));
	    return style;
	}
	
	public static HSSFCellStyle excelStyle5(HSSFWorkbook wb){
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}

}
