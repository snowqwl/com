package com.sunshine.monitor.system.analysis.action;

import java.text.SimpleDateFormat;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.service.XYScrapNaiManager;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Controller
@RequestMapping(value = "/xyscrapnai.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XYScrapNaiAnalysisController {
	
	@Autowired
	@Qualifier("xyScrapNaiManager")
	private XYScrapNaiManager xyScrapNaiManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	
	private static final String main = "analysis/xyscrapnaiAnalysisMain"; 
	protected Logger debugLogger = LoggerFactory.getLogger(XYScrapNaiAnalysisController.class);
	/**
	 * 跳转到关联分析主页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping
    public String forwardMain(HttpServletRequest request,HttpServletResponse response){
		  try {
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			//request.setAttribute("bklbList", this.systemManager.getCode("120005"));//布控类别
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
			request.setAttribute("csysList", this.systemManager.getCode("030108"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
    	return main;
    }
	
	/**
	 * 报废逾期未年检车辆库分页查询
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> queryList(HttpServletRequest request,HttpServletResponse response,String page,String rows,ScsVehPassrec veh){

		Map<String, Object> map = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("sessionId", request.getSession().getId().replace("-","_"));
		String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		veh.setHphm(hphm);
		try {
			map = this.xyScrapNaiManager.findXyForPage(veh, filter);
		} catch (Exception e) {
			debugLogger.warn("根据条件查询报废逾期未年检车辆库", e);
		}
		return map;
	}
	
	/**
	 * <2016-11-23>licheng-报废逾期未年检车辆库分页查询--显示过车次数
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @param veh
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String,Object> queryCsList(HttpServletRequest request,HttpServletResponse response,String page,String rows,ScsVehPassrec veh){

		Map<String, Object> map = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("sessionId", request.getSession().getId().replace("-","_"));
		String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		veh.setHphm(hphm);
		try {
			map = this.xyScrapNaiManager.findXyCsForPage(veh, filter);
		} catch (Exception e) {
			debugLogger.warn("根据条件查询报废逾期未年检车辆库", e);
		}
		return map;
	}
	
	/**
	 * 根据号牌号码和部分条件获取嫌疑库中额过车轨迹列表
	 * 
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
//
//	@RequestMapping
//	@ResponseBody
//	public Map<String, Object> queryForXYPageList(HttpServletRequest request,
//			HttpServletResponse response, VehPassrec veh, String page,
//			String rows) {
//		Map<String, Object> map = null;
//		Map<String, Object> filter = new HashMap<String, Object>();
//		filter.put("page", page);
//		filter.put("rows", rows);
//
//		try {
//			veh.setHphm(java.net.URLDecoder.decode(veh.getHphm(), "UTF-8"));
//			map = xyScrapNaiManager.queryForXYList(veh, filter);
//		} catch (Exception e) {
//			debugLogger.warn("嫌疑库中过车轨迹列表分页查询", e);
//		}
//		return map;
//	}
	
	/**
	 * 查询报废/逾期未年检车的车辆过车轨迹详细信息
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> findVehInfoByHphm(HttpServletRequest request,
			HttpServletResponse response, String id,String hphm,String hpzl) {
		Map<String, Object> map = null;
		try {
			map = xyScrapNaiManager.findXYCarInfo(id,hphm,hpzl);
		} catch (Exception e) {
			debugLogger.warn("嫌疑库中车辆信息查询", e);
		}
		return map;
	}
	
	/**
	 * 查询报废/逾期未年检车的车辆过车轨迹详细信息
	 * @param request
	 * @param response
	 * @param veh
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> findVehInfo(HttpServletRequest request,
			HttpServletResponse response,String id,String hphm,String hpzl,String gcxh) {
		Map<String, Object> map = null;
		try {
			map = xyScrapNaiManager.findXYCarInfo(id,hphm,hpzl,gcxh);
		} catch (Exception e) {
			debugLogger.warn("嫌疑库中车辆信息查询", e);
		}
		return map;
	}
	
	/**
	 * 根据嫌疑库主键ID，修改嫌疑记录状态
	 * @param request
	 * @param response
	 * @param status 状态（1已操作，2：数据作废）
	 * @param xyids 修改记录IDs
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> editXYStatus(HttpServletRequest request,
			HttpServletResponse response, String status,String xyids) {
		Map<String, Object> map = new HashMap<String, Object>();;
		try {
			int i = xyScrapNaiManager.editXYStatus(status,xyids);
			map.put("flag", i);
		} catch (Exception e) {
			debugLogger.warn("报废未年检记录状态修改", e);
		}
		return map;
	}
	
	
	/**
	 * 
	 * 导出报废未年检Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getXyscrpXls(HttpServletRequest request,HttpServletResponse response,String page,String rows,ScsVehPassrec veh) {
		String[] headName = new String[]{"号牌号码", "号牌颜色", "车牌种类", "过车次数"};
		short[] columnWidth = new short[]{6000,5000,5000,9000,3000,3000,3000,9000,3000,6000}; 
		String[] method = new String[]{"hphm","hphmys","hpzlmc","cs"};
		String name = "报废未年检分析";
		try {			
			List<Map<String,Object>> infos=(List<Map<String,Object>>)this.queryCsList(request, response, page, rows, veh).get("rows");
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	/**
//	 * 入库-新增记录到JM_ZYK_SCRAP_NAI (报废/逾期未年检库)
//	 * @param request
//	 * @param response
//	 * @param status 状态（1已操作，2：数据作废）
//	 * @param xyids 修改记录IDs
//	 * @return
//	 */
//	@RequestMapping
//	@ResponseBody
//	public Map<String, Object> editScrapNaiInfo(HttpServletRequest request,
//			HttpServletResponse response, String status,String xyids) {
//		Map<String, Object> map = new HashMap<String, Object>();;
//		try {
//			int i = xyScrapNaiManager.editScrapNaiInfo(status,xyids);
//			map.put("flag", i);
//		} catch (Exception e) {
//			debugLogger.warn("嫌疑库中车辆信息查询", e);
//		}
//		return map;
//	}
	
}
