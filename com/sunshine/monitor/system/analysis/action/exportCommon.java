package com.sunshine.monitor.system.analysis.action;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.util.ExcelStyle;

public class exportCommon {

	private static Logger log = LoggerFactory.getLogger(exportCommon.class);

	// 返回List<Map<String ,Object>>的导出
	public static void exportExecl(List<Map<String, Object>> infos, String name, HttpServletResponse response,
			String[] headName, short[] columnWidth, String[] method) {
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet("sheet1");
		HSSFRow header = sheet.createRow(0);
		header.setHeight((short) 400);
		sheet.setVerticallyCenter(true);
		for (int i = 0; i < headName.length; i++) {
			sheet.setColumnWidth(i, columnWidth[i]);
			HSSFCell cell = header.createCell(i);
			cell.setCellValue(headName[i]);
			cell.setCellStyle(ExcelStyle.excelStyle3(book));
		}
		try {
			short rowIndex = 1;
			if (infos.size() > 0) {
				/** 解决导出失败的问题 
				 *  modify by huanghaip 20180801
				 * **/
				CellStyle style = book.createCellStyle();
				style.setAlignment(CellStyle.ALIGN_CENTER);
				style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				/** end **/
				for (int j = 0; j < infos.size(); j++) {
					int r_col = 0;
					HSSFRow r_row = sheet.createRow(rowIndex);
					r_row.setHeight((short) 400);
					for (int i = 0; i < headName.length; i++) {
						Object s = infos.get(j).get(method[i]);
						HSSFCell cell = r_row.createCell(r_col);
						if (s != null) {
							cell.setCellValue(s.toString());
							cell.setCellStyle(style);
						}
						r_col++;
					}
					rowIndex++;
				}
			}
			// 输出Excel工作薄
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			response.setDateHeader("Expires", 0);
			// response.setCharacterEncoding("UTF-8");
			response.reset();
			response.setContentType("application/xml;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;Filename=" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + "_"
							+ new String(name.getBytes("GBK"), "ISO8859-1") + ".xls");
			ServletOutputStream stream = response.getOutputStream();
			book.write(stream);
			stream.flush();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void exportExecl1(List<Object> infos, String name, HttpServletResponse response, String[] headName,
			short[] columnWidth, String[] method) {
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet("sheet1");
		HSSFRow header = sheet.createRow(0);
		header.setHeight((short) 400);
		for (int i = 0; i < headName.length; i++) {
			sheet.setColumnWidth((short) i, (short) columnWidth[i]);
			sheet.setVerticallyCenter(true);
			HSSFCell cell = header.createCell((short) i);
			cell.setCellValue(headName[i]);
			cell.setCellStyle(ExcelStyle.excelStyle3(book));
		}
		try {
			short rowIndex = 1;
			if (infos.size() > 0) {
				for (int j = 0; j < infos.size(); j++) {
					short r_col = 00;
					Object r = infos.get(j);
					HSSFRow r_row = sheet.createRow(rowIndex);
					r_row.setHeight((short) 400);
					for (int i = 0; i < method.length; i++) {
						Object s = ((Map) r).get(method[i]);
						HSSFCell cell = r_row.createCell(r_col);
						if (s != null) {
							cell.setCellValue(s.toString());
						}
						cell.setCellStyle(ExcelStyle.excelStyle5(book));
						r_col++;
					}
					rowIndex++;
				}

			}
			// 输出Excel工作薄
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			response.setDateHeader("Expires", 0);
			// response.setCharacterEncoding("UTF-8");
			response.reset();
			response.setContentType("application/xml;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;Filename=" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + "_"
							+ new String(name.getBytes("GBK"), "ISO8859-1") + ".xls");
			ServletOutputStream stream = response.getOutputStream();
			book.write(stream);
			stream.flush();
			stream.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 导出excel包括图片
	 * 
	 * @param infos
	 * @param name
	 *            导出的excel的名字
	 * @param response
	 * @param headName
	 *            第一行的字段的中文名
	 * @param columnWidth
	 * @param method
	 *            字段
	 */
	public static void exportExecl2(List<Map<String, Object>> infos, String name, HttpServletResponse response,
			String[] headName, short[] columnWidth, String[] method) {
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet("sheet1");
		HSSFPatriarch pa = sheet.createDrawingPatriarch();
		HSSFRow header = sheet.createRow(0);
		header.setHeight((short) 400);
		for (int i = 0; i < headName.length; i++) {
			sheet.setColumnWidth((short) i, (short) columnWidth[i]);
			sheet.setVerticallyCenter(true);
			HSSFCell cell = header.createCell((short) i);
			cell.setCellValue(headName[i]);
			cell.setCellStyle(ExcelStyle.excelStyle3(book));
		}
		try {
			short rowIndex = 1;
			if (infos.size() > 0) {
				for (int j = 0; j < infos.size(); j++) {
					short r_col = 00;
					HSSFRow r_row = sheet.createRow(rowIndex);
					r_row.setHeight((short) 800);
					for (int i = 0; i < headName.length; i++) {
						Object s = infos.get(j).get(method[i]);
						HSSFCell cell = r_row.createCell(r_col);
						if (s != null) {
							if (!method[i].startsWith("gctp")) {
								cell.setCellValue(s.toString());
							} else {
								if (s != null && !"".equals(s)) {
									HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1000, 255, (short) i, j + 1,(short) i, j + 1);
									URLConnection picUrl = null;
									String code = "";
									URL url = null;
									try {
										log.info("。。。。。。。。。。开始获取图片url，进行图片的加载。。。。。。。。。。。");
										url = new URL(s.toString());
										picUrl = url.openConnection();
										picUrl.setConnectTimeout(200);
										picUrl.setReadTimeout(200);
										String type = picUrl.getContentType();
										if (type != null) {
											byte[] pic = getPictureBytes(picUrl);// 获取图片二进制数组
											anchor.setAnchorType(2);
											if (pic != null && pic.length > 0) {
												pa.createPicture(anchor,book.addPicture(pic, HSSFWorkbook.PICTURE_TYPE_JPEG));
											}

										} else {
											HSSFCell c = r_row.createCell((short) i);
											c.setCellStyle(ExcelStyle.excelStyle3(book));
											c.setCellValue("链接异常，图片无法下载.");
										}
									} catch (Exception e) {
										HSSFCell c = r_row.createCell((short) i);
										c.setCellStyle(ExcelStyle.excelStyle3(book));
										c.setCellValue("网络链接异常，文件无法下载。");

									}

								}

							}
							cell.setCellStyle(ExcelStyle.excelStyle5(book));
							r_col++;
						}

					}
					rowIndex++;
				}
			}
			// 输出Excel工作薄
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			response.setDateHeader("Expires", 0);
			// response.setCharacterEncoding("UTF-8");
			response.reset();
			response.setContentType("application/xml;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;Filename=" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + "_"
							+ new String(name.getBytes("GBK"), "ISO8859-1") + ".xls");
			ServletOutputStream stream = response.getOutputStream();
			book.write(stream);
			stream.flush();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取图片二进制流
	 * 
	 * @param destUrl
	 * @return
	 */
	private static byte[] getPictureBytes(URLConnection destUrl) {
		byte[] content = null;
		try {
			destUrl.connect();
			content = IOUtils.toByteArray(destUrl.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
}
