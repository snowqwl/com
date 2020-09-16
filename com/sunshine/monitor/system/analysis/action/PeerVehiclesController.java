package com.sunshine.monitor.system.analysis.action;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

import com.sunshine.core.util.ClassUtil;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.service.PeerVehiclesManager;
import com.sunshine.monitor.system.analysis.service.SubjectManager;
import com.sunshine.monitor.system.analysis.zyk.bean.CaseInfo;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/peerVeh.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PeerVehiclesController {

	private static String sessionId;

	@Autowired
	@Qualifier("peerManager")
	private PeerVehiclesManager peerManager;
	
	@Autowired
	@Qualifier("subjectManager")
	private SubjectManager subjectManager;
	
	@Autowired
	private SystemManager systemManager;

	@RequestMapping
	public String findForward(HttpServletRequest request,HttpServletResponse response) {
		sessionId = request.getSession().getId().replaceAll("-","_");
		//号牌种类集合
		try {
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "analysis/peervehiclesmain";
	}
	
	@RequestMapping
	public String findForwardVehContrail(HttpServletRequest request,HttpServletResponse response){
		sessionId = request.getSession().getId().replaceAll("-","_");
		return "analysis/vehicleContrailMain";
	}

	/**
	 * 同行车分析
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> queryForPeers(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			ScsVehPassrec veh) {
		Map<String, Object> map = null;
		if(sessionId==null){
			sessionId = request.getSession().getId().replaceAll("-","_");
		}
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id",sessionId);
		try {
			map = this.peerManager.queryForPeers(veh,filter);
			map.put("pagesign", veh.getPageSign());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 同行车分析
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<String, Object>> queryForPeersExt(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			ScsVehPassrec veh) {
		List<Map<String, Object>> list = null;
		if(sessionId==null){
			sessionId = request.getSession().getId().replaceAll("-","_");
		}
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id",sessionId);
		try {
			list = this.peerManager.queryForPeersExt(veh,filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 同行车分析
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Integer queryForPeersTotal(HttpServletRequest request,
			HttpServletResponse response, String page, String rows,
			ScsVehPassrec veh) {
		int total = 0;
		if(sessionId==null){
			sessionId = request.getSession().getId().replaceAll("-","_");
		}
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id",sessionId);
		try {
			total = this.peerManager.queryForPeersTotal(veh,filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 * 查询轨迹信息
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<String, Object>> queryForContrail(HttpServletRequest request,
			HttpServletResponse response, ScsVehPassrec veh, String page,
			String rows) {
		List<Map<String, Object>> list = null;
		if(sessionId==null){
			sessionId = request.getSession().getId().replaceAll("-","_");
		}
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id", sessionId);
		try {
			list = this.peerManager.queryForContrailExt(veh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查询轨迹信息记录总数
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Integer queryForContrailTotal(HttpServletRequest request,
			HttpServletResponse response, ScsVehPassrec veh, String page,
			String rows) {
		Integer total = 0;
		if(sessionId==null){
			sessionId = request.getSession().getId().replaceAll("-","_");
		}
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id", sessionId);
		try {
			total = this.peerManager.queryForContrailTotal(veh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 * 查询轨迹列表（pgis地图使用）
	 * @param request
	 * @param response
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map getContrailList(HttpServletRequest request,
			HttpServletResponse response, ScsVehPassrec veh) {
		Map result = null;
		if(sessionId==null){
			sessionId = request.getSession().getId().replaceAll("-","_");
		}
		try {
			result = this.peerManager.getContrailList(sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询已保存分析的轨迹信息（pgis）
	 * @param request
	 * @param response
	 * @param ztbh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map getContrailListBySave(HttpServletRequest request,HttpServletResponse response,String ztbh){
		Map result = null;
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("page",1);
		filter.put("rows",1000);
		try {
			result = this.peerManager.getContrailListBySave(ztbh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询同行车分析明细
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> queryPeerDetail(HttpServletRequest request,
			HttpServletResponse response, ScsVehPassrec veh, String page,
			String rows) {
		Map<String, Object> map = null;
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id", sessionId);
		try {
			map = this.peerManager.queryPeerDetail(veh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * <2016-10-20> 同行改造，根据详情查询同行次数详情, 原有方法为queryPeerDetail(...)
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> queryPeerCsDetail(HttpServletRequest request,
			HttpServletResponse response, ScsVehPassrec veh, String page,
			String rows){
		Map<String, Object> map = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id", sessionId);
		try {
			map = this.peerManager.queryPeerDetail(veh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 查询同行车目标车对比图片
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> queryComparePic(HttpServletRequest request,
	        HttpServletResponse response, ScsVehPassrec veh, String page,
	        String rows){
		Map<String, Object> map = null;
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id", sessionId);
		try {
			map = this.peerManager.queryComparePic(veh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 更新临时表轨迹信息状态
	 * @param request
	 * @param response
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public int updateCheck(HttpServletRequest request,
	        HttpServletResponse response, ScsVehPassrec veh){
		int result = 0;
		try {
		 	result=this.peerManager.updateCheck(veh, sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 导出轨迹xls
	 * @param request
	 * @param response
	 * @param veh
	 */ 
	@RequestMapping
	public void getContrailXls(HttpServletRequest request,HttpServletResponse response) {
		ScsVehPassrec veh = new ScsVehPassrec();
		veh.setPageSign("1");
		String[] columnName=new String[]{"号牌号码","号牌颜色","过车时间","卡口名称","方向名称","行政区划"};
		String[] methodName=new String[]{"Hphm","Hpysmc","Gcsj","Kdmc","Fxmc","Xzqhmc"};
		short[] columnWidth=new short[]{3000,2000,6000,9000,9000,5000};
        Map<String,Object> filter = new HashMap<String,Object>();
        filter.put("page",1);
        filter.put("rows",500);
		filter.put("id", sessionId);
        try {
          //查询轨迹信息
		  List<ScsVehPassrec> list=(List<ScsVehPassrec>)this.peerManager.queryForContrail(veh,filter).get("rows");
	      HSSFWorkbook book = new HSSFWorkbook();
	      HSSFSheet sheet = book.createSheet();
		  HSSFRow header = sheet.createRow(0);
		  header.setHeight((short)400);
		  for(int i=0;i<columnName.length;i++){
				sheet.setColumnWidth((short)i,columnWidth[i]);
				sheet.setVerticallyCenter(true);
				HSSFCell cell=header.createCell((short)i);
				cell.setCellValue(columnName[i]);
			    cell.setCellStyle(ExcelStyle.excelStyle3(book));
		  }
		  for(int i =1;i<=list.size();i++){
			   ScsVehPassrec scs = list.get(i-1);
			   HSSFRow row = sheet.createRow(i);
			   row.setHeight((short) 500);
			   for(int j=0;j<methodName.length;j++){
       	    	    // 创建Excel的单元格   
	        	    HSSFCell cell = row.createCell((short)j);
		        	// 给Excel的单元格设置样式和赋值   
	        	    Object  value = ClassUtil.invokegetMethod(scs,"get"+methodName[j]);
	        	    System.out.println(methodName[j]);
	        	    cell.setCellValue(value.toString());
			   }
		  }
		  //对下载内容记录日志文件
		  response.setCharacterEncoding("UTF-8");
		  response.reset();
	      response.setContentType("application/msexcel");
		  response.setHeader("Content-Disposition", "attachment;Filename="+new Date().getTime()+new String("精确车牌轨迹列表".getBytes(), "ISO_8859_1")+".xls");
		  OutputStream os = response.getOutputStream();
		  book.write(os);
		  os.flush();
		  os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询已保存分析的轨迹列表
	 * @param ztbh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> loadContail(String ztbh,String page,String rows){
		Map<String,Object> result = null;
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("page",page);
		filter.put("rows",rows);
		try {
			result =  this.subjectManager.loadContrail(ztbh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询已保存分析的同行车列表
	 * @param ztbh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> loadPeerInfo(String ztbh,String page,String rows){
		Map<String,Object> result = null;
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put("page",page);
		filter.put("rows",rows);
		try {
			result =  this.subjectManager.loadPeerInfo(ztbh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询已保存分析的同行车明细信息
	 * @param request
	 * @param response
	 * @param veh
	 * @param ztbh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> queryPeerDetailBySave(HttpServletRequest request,
			HttpServletResponse response, ScsVehPassrec veh,String ztbh, String page,
			String rows) {
		Map<String, Object> map = null;
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id", sessionId);
		try {
			map = this.subjectManager.getPeerContrail(ztbh, veh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 查询已保存分析的同行车目标车对比图片
	 * @param request
	 * @param response
	 * @param veh
	 * @param ztbh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> queryComparePicBySave(HttpServletRequest request,
	        HttpServletResponse response, ScsVehPassrec veh,String ztbh, String page,
	        String rows){
		Map<String, Object> map = null;
		
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("id", sessionId);
		try {
			map = this.subjectManager.getPeerComparePic(ztbh, veh, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * <2016-10-15> 从YDB中查询同行车列表
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param veh
	 * @param sign
	 * @param cs
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> loadPeer(HttpServletRequest request,HttpServletResponse response, String page, String rows,
			VehPassrec veh,String sign,String cs,String symbol, String gaptime){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = this.peerManager.loadPeer(page, rows, veh, sign, cs, symbol, gaptime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 导出同行车牌信息Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getCaseInfoXls(HttpServletRequest request,HttpServletResponse response, String page, String rows,
			VehPassrec veh,String sign,String cs,String symbol, String gaptime) {
		String[] headName = new String[]{"号牌号码", "号牌颜色", "号牌种类", "违法信息", "同行次数"};
		short[] columnWidth = new short[]{5000,5000,5000,9000,3000}; 
		String[] method = new String[]{"hphm","hpysmc","hpzlmc","wfxx","cs"};
		String name = "同行车牌分析";
		try {
			Map<String, Object> resultMap = this.loadPeer(request, response, page, rows, veh, sign, cs, symbol, gaptime);
			List<Object> infos= (List<Object>) resultMap.get("rows");
			exportCommon.exportExecl1(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
