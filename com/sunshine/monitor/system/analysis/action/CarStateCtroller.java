package com.sunshine.monitor.system.analysis.action;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.core.util.ClassUtil;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.DateJsonValueProcessor;
import com.sunshine.monitor.comm.util.ExcelUtil;
import com.sunshine.monitor.system.analysis.bean.JmYearInspection;
import com.sunshine.monitor.system.analysis.service.YearInspectionService;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.comm.vehicle.VehQuery;



@Controller
@RequestMapping(value = "/carState.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CarStateCtroller {
		
	// 报废、未年检车辆分析主页
	private String mainPage = "analysis/carStateAnalysis";
	
	@Autowired
	private YearInspectionService yearInspectionService;
	
	/**
	 * 跳转到报废、未年检车辆分析主页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,HttpServletResponse response ){
		
		
		return new ModelAndView(this.mainPage);
	}
	
	/**
	 * excel文件读取显示到页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object addCarslist(HttpServletRequest request,HttpServletResponse response){
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
		SysUser user = userSession.getSysuser();
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile excelFile = mrequest.getFile("exfilename");
		List<JmYearInspection> list = ExcelUtil.xlsToBean(excelFile, JmYearInspection.class);
		List<Object> ls = new ArrayList<Object>();
		VehQuery vq = new VehQuery();
		try {
			for (JmYearInspection car : list) {
				//调用巨龙接口查询机动车库
				JSONObject jsonObj = JSONObject.fromObject(vq.getVehilceInfo(request, car.getHpzl(), car.getHphm()));
				if(jsonObj!=null) {
					ls.add(jsonObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{'result':'出现异常！'}";
		} 
		return ls;
	}
	/**
	 * 保存到专题库
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping
	@ResponseBody
	public Map saveYearInspection(HttpServletRequest request,
			HttpServletResponse response) {
		String data = request.getParameter("jsonstr");
		//System.out.println("data:"+data);
		int result = 0;
		JSONArray array = JSONArray.fromObject(data);
		List<JmYearInspection> list = array.toList(array, JmYearInspection.class);
		try {
			for(JmYearInspection ji : list) {
				this.yearInspectionService.saveYearInspection(ji);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Common.messageBox("出现异常", "1");
		}
		return Common.messageBox("保存成功", "0");
	}
	

	/**
	 * 识别结果集的导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "deprecation" })
	@ResponseBody
	@RequestMapping(params="method=downloadCarsList",method=RequestMethod.POST)
	public void downloadCarsList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Object ars=new String(request.getParameter("aid").getBytes("UTF-8"),"UTF-8");
		// 创建Excel的工作书册 Workbook,对应到一个excel文档   
	    HSSFWorkbook wb = new HSSFWorkbook();   
	    // 创建Excel的工作sheet,对应到一个excel文档的tab   
	    HSSFSheet sheet = wb.createSheet("sheet1");   
	    try{
	    	JsonConfig jf = new JsonConfig();  
	    	//DateJsonValueProcessor d=new DateJsonValueProcessor();
	    	 jf.registerJsonValueProcessor(java.sql.Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd"));  
	         jf.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));  
	         JSONArray arry = JSONArray.fromObject(ars,jf);
			 String methodNames2[]= {"HPHM","HPZLMC","CSYSMC","FDJH","CLLXMC","JDCSYR","JDCZT"};
			 String columnNames[] = {"号牌号码","号牌种类","车身颜色","发动机号","车辆类型","机动车所有人","机动车状态"};
			 //设置excel标头字段行
			 HSSFRow columnRow = sheet.createRow(0); 
			 columnRow.setHeight((short) 500);// 设定行的高度   
			for(int i=0;i<columnNames.length;i++){
				//设置单元格宽度
				sheet.setColumnWidth((short)i, (short)5000);
				 //设置excel的标头字段列
	 	    	HSSFCell ColumnCell =columnRow.createCell((short)i);
	 	    	//设置标头样式
	 	    	ColumnCell.setCellStyle(ExcelStyle.excelStyle3(wb));
	 	    	ColumnCell.setCellValue(String.valueOf(columnNames[i]));
			}			
			 for (int i = 1; i < arry.size()+1; i++)
		        {	
		            JSONObject jsonObject = arry.getJSONObject(i-1);
		            JmYearInspection car= (JmYearInspection) jsonObject.toBean(jsonObject, JmYearInspection.class);
		        	    HSSFRow row = sheet.createRow(i);   
		        	    row.setHeight((short) 500);// 设定行的高度   
		        	    for(int p=0;p<methodNames2.length;p++){
		        	    	// 设置excel每列宽度   
			            	sheet.setColumnWidth((short)p, (short)5000);
		        	    	// 创建Excel的单元格   
			        	    HSSFCell cell = row.createCell((short) p);
			        	 // 给Excel的单元格设置样式和赋值   
			        	    String methodName = "get" + methodNames2[p];
			        	    Object  cellVal = ClassUtil.invokegetMethod(car, methodName);
			        	    if(p%2==0){
			        	    	 cell.setCellStyle(ExcelStyle.excelStyle(wb));   
			        	    }
			        	    else{
			        	    	cell.setCellStyle(ExcelStyle.excelStyle2(wb));   
			        	    }
			        	    cell.setCellValue(String.valueOf(cellVal));   
		        	    }
		         
		        }
			 //导出
			 this.yearInspectionService.downloadResult();
			  	response.setCharacterEncoding("UTF-8");
			  	response.reset();
				response.setContentType("application/msexcel");
				response.setHeader("Content-Disposition", "attachment;Filename="+new Date().getTime()+new String("车辆检测结果".getBytes(), "ISO_8859_1")+".xls");
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.flush();
				os.close();
	    }
		catch(Exception e){
			e.printStackTrace();
		}
	
	}
}
