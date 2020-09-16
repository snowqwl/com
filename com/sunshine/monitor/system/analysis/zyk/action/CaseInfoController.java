package com.sunshine.monitor.system.analysis.zyk.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.core.util.ClassUtil;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.analysis.zyk.bean.CaseInfo;
import com.sunshine.monitor.system.analysis.zyk.service.CaseInfoManager;
import com.sunshine.monitor.system.manager.bean.UserLoginXls;
import com.sunshine.monitor.system.manager.service.SystemManager;

/**
 * 案件信息管理
 * @author huzhaoj
 * @date 2016-10-25
 *
 */
@Controller
@RequestMapping(value = "/caseInfo.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CaseInfoController {
	
	@Autowired
	private CaseInfoManager caseInfoManager;
	@Autowired
	private SystemManager systemManager;
	
	private Logger log = LoggerFactory.getLogger(SuspectController.class);
	
	private String main = "analysis/caseInfoMain";
	private String win = "analysis/caseInfoWin";
	
	/**
	 * 初始化页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
	public String findForward(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return main;
	}
	
	@RequestMapping
	public ModelAndView forwardWin(HttpServletRequest request,
			HttpServletResponse response, String model, String id) {
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					request, "userSession");
			String yhdh = userSession.getSysuser().getYhdh();
			request.setAttribute("yhdh", yhdh);
			
			if(null!=id && !"".equals(id)){
				request.setAttribute("id", id);
			}
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			request.setAttribute("model", model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(win);
	}
	
	@ResponseBody
	@RequestMapping
	public Map<String,Object> getList(HttpServletRequest request,HttpServletResponse response,
			String rows, String page, CaseInfo ci){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("rows", rows);
		filter.put("page", page);
		map = caseInfoManager.queryList(ci, filter);
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object saveCaseInfo(HttpServletRequest req,CaseInfo ci) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					req, "userSession");
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  

			ci.setGabh(userSession.getSysuser().getJh());
			ci.setLrsj(sdf.format(new Date()));

			caseInfoManager.saveCaseInfo(ci);
			map = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map = Common.messageBox("程序异常！", "0");
		}
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object getCaseInfo(HttpServletRequest request, String id){
		CaseInfo ci = new CaseInfo();
		try {
			ci = caseInfoManager.getCaseInfoById(id);
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ci;
	}
	
	@RequestMapping
	@ResponseBody
	public Object updateCaseInfo(HttpServletRequest req,CaseInfo ci) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		if(StringUtils.isBlank(ci.getId() )){
			return Common.messageBox("参数传输异常！", "0");
		}
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
					req, "userSession");
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  

			ci.setGabh(userSession.getSysuser().getJh());
			ci.setLrsj(sdf.format(new Date()));
			
			caseInfoManager.updateCaseInfo(ci);
			map = Common.messageBox("更新成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map = Common.messageBox("程序异常！", "0");
		}
		
		return map;
	}
	
	@RequestMapping
	@ResponseBody
	public Object deleteCaseInfo(HttpServletRequest request, String id){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			caseInfoManager.deleteCaseInfo(id);
			map.put("message", "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "删除失败！");
		}
		return map;
	}
	
	/**
	 * 
	 * 导出案件信息的Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getCaseInfoXls(HttpServletRequest request,HttpServletResponse response) {
		String[] columnName = new String[]{"录入人", "公安编号", "案件编号", "案件名称", "案件来源", "号牌号码", "号牌种类", "图片路径", "描述", "信息来源", "行政区划", "录入时间"};
		short[] columnWidth = new short[]{3000,5000,5000,9000,3000,3000,3000,9000,12000,3000,3000,6000}; 
		String[] caseinfoMethod = new String[]{"Lrr","Gabh","Ajbh","Ajmc","Ajly","Hphm","Hpzl","Tplj","Description","Xxly","Xzqh","Lrsj"};
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet("sheet1");
		HSSFRow header = sheet.createRow(0);
		header.setHeight((short)400);
		for(int i = 0;i<columnName.length;i++){
			sheet.setColumnWidth((short)i,(short)columnWidth[i]);
			sheet.setVerticallyCenter(true);
			HSSFCell cell=header.createCell((short)i);
			cell.setCellValue(columnName[i]);
		    cell.setCellStyle(ExcelStyle.excelStyle3(book));
		}
		try {			
			Map map = null;
			Map filter = new HashMap();
			List<CaseInfo> infos= this.caseInfoManager.getAllCaseInfo();
			   short rowIndex=1;				      	
   		   for(int j=0;j<infos.size();j++){
   			   short r_col=00;
   			CaseInfo r=infos.get(j);
   			   HSSFRow r_row=sheet.createRow(rowIndex);
   			   r_row.setHeight((short)400);
   			   for(int i = 0 ;i<caseinfoMethod.length;i++){
  			         Object s=ClassUtil.invokeMethod(r, "get" + caseinfoMethod[i]);
  			         if(null!=s){
  			        	if(caseinfoMethod[i].equals("Ajly")){
 			        		 if(s.toString().equals("01")){
 				        		 s = (Object)"人工录入";
 				        	 }else if(s.toString().equals("02")){
 				        		 s = (Object)"系统录入";
 				        	 }
				         }
	  			         if(caseinfoMethod[i].equals("Hpzl")){
	  			        	s = (Object)this.systemManager.getCodeValue("030107", s.toString());
				         }
	  			         if(caseinfoMethod[i].equals("Xxly")){
  			        		 if(s.toString().equals("01")){
  				        		 s = (Object)"人工录入";
  				        	 }else if(s.toString().equals("02")){
  				        		 s = (Object)"系统录入";
  				        	 }
				         }
  			         }
				     HSSFCell cell=r_row.createCell(r_col);
				     if(s!=null){
			           cell.setCellValue(s.toString());			    
			           }
				     cell.setCellStyle(ExcelStyle.excelStyle5(book));
   			     r_col++;
   			   }
   			   rowIndex++;
   		   }
   		   
			// 输出Excel工作薄
           response.setHeader("pragma", "no-cache");
           response.setHeader("cache-control", "no-cache");
           response.setDateHeader("Expires", 0);
			response.setCharacterEncoding("UTF-8");
		    response.reset();
			response.setContentType("application/xml;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;Filename="
					+ new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
					+ "_"
					+ new String("案件信息".getBytes("GBK"),"ISO8859-1") + ".xls");
			ServletOutputStream stream = response.getOutputStream();
			book.write(stream);
			stream.flush();
			stream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
