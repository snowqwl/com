package com.sunshine.monitor.system.manager.action;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.core.util.ClassUtil;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.manager.bean.UserLoginXls;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.manager.service.UserLoginManager;

@Controller
@RequestMapping(value = "/userLoginCtrl.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserLoginCtroller {
		
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("userLoginManager")
	private UserLoginManager userLoginManager;
	
	/**
	 * 跳转用户登陆次数查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping()
	public ModelAndView findForward(HttpServletRequest request,
			HttpServletResponse response) {
		
		return new ModelAndView("manager/userloginmain");
	}
	
	/**
	 * 查询用户登陆次数
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			
				Map<String, Object> conditions = Common
						.getParamentersMap(request);
				result = this.userLoginManager.queryList(conditions);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询用户登陆次数
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Object queryForDetail(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			
				Map<String, Object> conditions = Common
						.getParamentersMap(request);
				result = this.userLoginManager.queryForDetail(conditions);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 * 导出用户登录次数的Execl
	 * @param request
	 * @param response
	 * @param info
	 */
	@RequestMapping
	public void getuserLoginXls(HttpServletRequest request,HttpServletResponse response,UserLoginXls info) {
		String[] columnName = new String[]{"用户名", "姓名", "警号", "管理部门", "登录次数"};
		short[] columnWidth = new short[]{3000,3000,3000,9000,3000}; 
		String[] suspinfoMethod = new String[]{"Yhdh","Yhmc","Jh","Bmmc","Cs"};
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
			List<UserLoginXls> infos= this.userLoginManager.getUserLoginList(info);
			   short rowIndex=1;				      	
   		   for(int j=0;j<infos.size();j++){
   			   short r_col=00;
   			UserLoginXls r=infos.get(j);
   			   HSSFRow r_row=sheet.createRow(rowIndex);
   			   r_row.setHeight((short)400);
   			   for(int i = 0 ;i<suspinfoMethod.length;i++){
  			         Object s=ClassUtil.invokeMethod(r, "get" + suspinfoMethod[i]);
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
					+ new Date().getTime()
					+ new String("用户授权查询".getBytes("GBK"),"ISO8859-1") + ".xls");
			ServletOutputStream stream = response.getOutputStream();
			book.write(stream);
			stream.flush();
			stream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
