
package com.sunshine.monitor.system.manager.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.core.util.ClassUtil;
import com.sunshine.monitor.comm.util.DateUtils;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.comm.util.excel.OutputExcelUtils;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.service.LogManager;

@Controller
@RequestMapping(value="/log.do",params="method")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LogCtroller {

	@Autowired
	private LogManager logManager;
	
	@RequestMapping(params = "method=index")
	public String index() {
		return "manager/logmain";
	}
	
	@RequestMapping(params = "method=forwardAnalysis")
	public String forwardAnalysis() {
		return "manager/logAnalysisMain";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=getLogForPage", method = RequestMethod.POST)
	@ResponseBody
	public Map getLogForPage(String page, String rows) {
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		return this.logManager.findLogForMap(filter);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=queryLogForPage", method = RequestMethod.POST)
	@ResponseBody
	public Map queryLogForPage(HttpServletRequest request,String page, String rows, Log log) {	 
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		String hphm = request.getParameter("hphm");
		if(hphm.trim().length()>0){
			filter.put("hphm",hphm.trim());
		}
		return this.logManager.findLogForMap(filter, log);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=queryLogCountForPage", method = RequestMethod.POST)
	@ResponseBody
	public Map queryLogCountForPage(HttpServletRequest request,String page, String rows, Log log) throws Exception {	
		String fs = request.getParameter("fs");
		Map filter = new HashMap();
		filter.put("curPage", page);	//页数
		filter.put("pageSize", rows);	//每页显示行数
		Map map;
		if("1".equals(fs)) {
			map = this.logManager.findLogCountForMapByYH(filter, log);
		} else if ("2".equals(fs)) {
			map = this.logManager.findLogCountForMapByBM(filter, log);
		} else if ("3".equals(fs)) {
			map = this.logManager.findLogCountForMapByQH(filter, log);
		} else {
			map = null;
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=downloadDetail", method = RequestMethod.POST)
	@ResponseBody
	public void downloadDetail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Log log = new Log();
		String glbm = request.getParameter("downloadglbm");
		String czlx = request.getParameter("downloadczlx");
		String kssj = request.getParameter("downloadkssj");
		String jssj = request.getParameter("downloadjssj");
		log.setGlbm(glbm);
		log.setCzlx(czlx);
		log.setKssj(kssj);
		log.setJssj(jssj);
		Map map = this.logManager.findLogCountForMapByYH(null, log);
		List queryList = (List) map.get("rows");
		//创建workbook
		
		// 创建Excel的工作书册 Workbook,对应到一个excel文档   
	    HSSFWorkbook wb = new HSSFWorkbook();   
	    // 创建Excel的工作sheet,对应到一个excel文档的tab   
	    HSSFSheet sheet = wb.createSheet("sheet1");  
	    try{
	    	
			 String methodNames2[]= getMethodName2();
			 //设置excel标头字段行
			 HSSFRow columnRow = sheet.createRow(0); 
			 columnRow.setHeight((short) 500);// 设定行的高度   
			for(int i=0;i<getColumnNames().length;i++){
				//设置单元格宽度
				sheet.setColumnWidth((short)i, (short)5000);
				 //设置excel的标头字段列
	 	    	HSSFCell ColumnCell =columnRow.createCell((short)i);
	 	    	//设置标头样式
	 	    	ColumnCell.setCellStyle(ExcelStyle.excelStyle3(wb));
	 	    	ColumnCell.setCellValue(String.valueOf(getColumnNames()[i]));
			}			
			 for (int i = 0; i < queryList.size(); i++)
		        {	
		           Map  logMap =  (Map)queryList.get(i);
		           // System.out.println(vehinfo);
		        	    HSSFRow row = sheet.createRow(i+1);   
		        	    row.setHeight((short) 500);// 设定行的高度   
		        	    for(int p=0;p<getMethodName2().length;p++){
		        	    	// 设置excel每列宽度   
			            	sheet.setColumnWidth((short)p, (short)5000);
		        	    	// 创建Excel的单元格   
			        	    HSSFCell cell = row.createCell((short) p);
			        	 // 给Excel的单元格设置样式和赋值   
			        	   // String methodName = "get" + methodNames2[p];
			        	   // Object  cellVal = ClassUtil.invokegetMethod(logMap, methodName);
			        	    if(p%2==0){
			        	    	 cell.setCellStyle(ExcelStyle.excelStyle(wb));   
			        	    }
			        	    else{
			        	    	cell.setCellStyle(ExcelStyle.excelStyle2(wb));   
			        	    }
			        	    cell.setCellValue(String.valueOf(logMap.get(""+getMethodName2()[p]+"")));   
		        	    }
		         
		        }
			 	response.setHeader("pragma", "no-cache");
	            response.setHeader("cache-control", "no-cache");
	            response.setDateHeader("Expires", 0);
			  	response.setCharacterEncoding("UTF-8");
			    response.reset();
				response.setContentType("application/xml;charset=utf-8");
				response.setHeader("Content-Disposition", "attachment;Filename="+new Date().getTime()+new String("日志分析".getBytes("GBK"), "ISO8859-1")+".xls");
				ServletOutputStream stream = response.getOutputStream();
				wb.write(stream);
				stream.flush();
				stream.close();
				/*
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.flush();
				os.close();
				*/
	    }
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	@RequestMapping
	@ResponseBody
	public List<Log> queryLogList(Log log, String page, String rows) {
		//return this.logManager.findLogList(log);
		return null;
	}
	
	
	//下载的excel字段
	public String [] getColumnNames(){
		String []columnNames={
				"部门名称","操作人员","操作类型","操作次数"
		};
		return columnNames;
	}
	
	//下载方法名
	public String [] getMethodName2(){
		String []methodNames={"BMMC","YHMC","CZLXMC","CZCS"
		};
		return methodNames;
	}
	
	/*
	 * 日志信息上传Excel（前200条）
	 */
	@RequestMapping
	@ResponseBody
	public String getLogExeclName(HttpServletRequest request, Log log) throws Exception{
		Map filter = new HashMap();
		String filename = "";
		StringBuffer url = new StringBuffer();
		OutputStream output = null;
		filter.put("curPage", "1");	
		filter.put("pageSize","200");	
		String hphm = java.net.URLDecoder.decode(request.getParameter("hphm"),"utf-8");
		if(hphm.trim().length()>0){
			filter.put("hphm",hphm);
		}
		try{
		log.setGlbm(request.getParameter("glbm"));
		log.setYhmc(java.net.URLDecoder.decode(request.getParameter("yhmc"),"utf-8"));
		log.setKssj(request.getParameter("kssj"));
		log.setJssj(request.getParameter("jssj"));
		log.setCzlx(request.getParameter("czlx"));
		Map map = this.logManager.findLogForMap(filter, log);
		List<Log> data = (List<Log>) map.get("rows");	
		// data
		LinkedHashMap<String, List> sheets = new LinkedHashMap<String, List>();
		sheets.put("日志查询", data);
		// Heads
		List<String[]> heads = new ArrayList<String[]>();
		heads.add(new String[]{"部门名称","操作人员","操作类型","操作内容","操作时间","IP地址"});
		// Fields
		List<String[]> fields = new ArrayList<String[]>();
		fields.add(new String[]{"bmmc","yhmc","czlx","cznr","czsj","ip"});
		// 保存
		// 保存
		filename = DateUtils.getCurrTimeStr(1)+".xls";
		//String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		//int pos = classPath.indexOf("WEB-INF");
		String fullPath = request.getSession().getServletContext().getRealPath("/")+"/download/";
		url.append(fullPath).append(filename);
		File file = new File(url.toString());
		output = new FileOutputStream(file);  
		OutputExcelUtils.outPutExcel(heads, fields, sheets, new String[]{"日志查询信息"}, null,output);
		output.flush();
		output.close();
	} catch (Exception e) {
		e.printStackTrace();
	} finally{
		try {
			if(output != null)
				output.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return filename;
	}
	
	/**
	 * 日志类型树
	 * @return
	 */
	@RequestMapping(params = "method=getLogCategoryTree")
	@ResponseBody
	public List<Map<String,Object>> getLogCategoryTree(HttpServletRequest request) {		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> logCategoryList = new ArrayList<Map<String,Object>>();
		try {
			list = this.logManager.getLogCategoryTree("");		
			if(list.size()>0){			
				Iterator<Map<String,Object>> it = list.iterator();
				while(it.hasNext()){
					Map<String,Object> m = it.next();				
					Map<String,Object> item = new HashMap<String,Object>(); 
					if(m.get("id")!=null) {					
					  item.put("id", m.get("ID").toString());
					  item.put("name", m.get("NAME").toString());
					  item.put("pId", m.get("PID").toString());
					  boolean b = this.logManager.countLogCategoryParent((String) m.get("ID"));
					  item.put("isParent", b);
					  logCategoryList.add(item);
				      
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logCategoryList;
	}
	
/*
 * 新增用户授权日志模块	2018-08-21
 */
	
	@RequestMapping(params = "method=forwardUserAccredit")
	public String forwardUserAccredit() {
		return "manager/userAccreditLog";
	}
	
	//获取用户授权日志列表
	@RequestMapping(params = "method=getAccreditList")
	@ResponseBody
	public Map<String,Object> getAccreditList(HttpServletRequest request,String page, String rows, Log log) {
		Map<String,String> paramMap=new HashMap<String,String>();
		paramMap.put("curPage", page);
		paramMap.put("pageSize", rows);
		Map<String, Object> map = logManager.findLogForMap2(paramMap, log,"yhsq");
		return map;
	}
	
	//导出用户授权日志
	@RequestMapping(params="method=getAccreditLogExecl")
	@ResponseBody
	public String getAccreditLogExecl(HttpServletRequest request, Log log) throws Exception{
		Map<String,String> params = new HashMap<String,String>();
		String filename = "";
		StringBuffer url = new StringBuffer();
		OutputStream output = null;
		params.put("curPage", "1");	
		params.put("pageSize","200");	
		if(log.getSqr()!=null && !"".equals(log.getSqr())) {
			String sqr = java.net.URLDecoder.decode(log.getSqr(),"utf-8");
			log.setSqr(sqr);
		}
		if(log.getBsqr()!=null && !"".equals(log.getBsqr())) {
			String bsqr = java.net.URLDecoder.decode(log.getBsqr(),"utf-8");
			log.setBsqr(bsqr);
		}
		if(log.getSqjs()!=null && !"".equals(log.getSqjs())) {
			String sqjs = java.net.URLDecoder.decode(log.getSqjs(),"utf-8");
			log.setSqjs(sqjs);
		}
		try{
		Map<String,Object> map = this.logManager.findLogForMap2(params, log,"yhsq");
		List<Log> data = (List<Log>) map.get("rows");	
		// data
		LinkedHashMap<String, List> sheets = new LinkedHashMap<String, List>();
		sheets.put("日志查询", data);
		// Heads
		List<String[]> heads = new ArrayList<String[]>();
		heads.add(new String[]{"部门名称","授权人警号","授权人","操作时间","被授权人警号","被授权人","授权角色","IP地址"});
		// Fields
		List<String[]> fields = new ArrayList<String[]>();
		fields.add(new String[]{"bmmc","jh","sqr","czsj","bsqrjh","bsqr","sqjs","ip"});
		// 保存
		filename = DateUtils.getCurrTimeStr(1)+".xls";
		String fullPath = request.getSession().getServletContext().getRealPath("/")+"/download/";
		url.append(fullPath).append(filename);
		File file = new File(url.toString());
		output = new FileOutputStream(file);  
		OutputExcelUtils.outPutExcel(heads, fields, sheets, new String[]{"用户授权日志信息"}, null,output);
		output.flush();
		output.close();
	} catch (Exception e) {
		e.printStackTrace();
	} finally{
		try {
			if(output != null)
				output.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return filename;
	}
	
/*
 * 新增车辆查询日志审计模块	2018-08-22
 */
	
	@RequestMapping(params ="method=forwardVehicleSearch")
	public String forwardVehicleSearch() {
		return "manager/vehicleSearchLog";
	}
	
	//获取车辆查询日志列表
	@RequestMapping(params="method=getClSearchLogList")
	@ResponseBody
	public Map<String,Object> getClSearchLogList(HttpServletRequest request,String page, String rows, Log log) {
		Map<String,String> params=new HashMap<String,String>();
		params.put("curPage", page);
		params.put("pageSize", rows);
		Map<String, Object> map = this.logManager.findLogForMap2(params, log, "clcx");
		return map;
	}
	
	//导出车辆查询日志
		@RequestMapping(params="method=getVehicleSearchLogExecl")
		@ResponseBody
		public String getVehicleSearchLogExecl(HttpServletRequest request, Log log) throws Exception{
			Map<String,String> params = new HashMap<String,String>();
			String filename = "";
			StringBuffer url = new StringBuffer();
			OutputStream output = null;
			params.put("curPage", "1");	
			params.put("pageSize","200");	
			if(log.getSqr()!=null && !"".equals(log.getSqr())) {
				String sqr = java.net.URLDecoder.decode(log.getSqr(),"utf-8");
				log.setSqr(sqr);
			}
			if(log.getHphm()!=null && !"".equals(log.getHphm())) {
				String hphm = java.net.URLDecoder.decode(log.getHphm(),"utf-8");
				log.setHphm(hphm);
			}
			try{
			Map<String,Object> map = this.logManager.findLogForMap2(params, log,"clcx");
			List<Log> data = (List<Log>) map.get("rows");	
			// data
			LinkedHashMap<String, List> sheets = new LinkedHashMap<String, List>();
			sheets.put("日志查询", data);
			// Heads
			List<String[]> heads = new ArrayList<String[]>();
			heads.add(new String[]{"部门名称","警号","操作人员","操作时间","查询车牌","开始时间","结束时间","查询结果","IP地址"});
			// Fields
			List<String[]> fields = new ArrayList<String[]>();
			fields.add(new String[]{"bmmc","jh","sqr","czsj","hphm","kssj","jssj","cxjg","ip"});
			// 保存
			filename = DateUtils.getCurrTimeStr(1)+".xls";
			String fullPath = request.getSession().getServletContext().getRealPath("/")+"/download/";
			url.append(fullPath).append(filename);
			File file = new File(url.toString());
			output = new FileOutputStream(file);  
			OutputExcelUtils.outPutExcel(heads, fields, sheets, new String[]{"车辆查询日志信息"}, null,output);
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(output != null)
					output.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
		}
	
}
