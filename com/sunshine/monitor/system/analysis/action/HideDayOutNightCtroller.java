package com.sunshine.monitor.system.analysis.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.analysis.bean.HideDayOutNightDayBean;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.service.HideDayOutNightManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/dayNightCtr.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
public class HideDayOutNightCtroller {

	@Autowired
	@Qualifier("dayNightManager")
	private HideDayOutNightManager dayNightManager;
	
	@Autowired
	private SystemManager systemManager;
	
	protected Logger debugLogger = LoggerFactory.getLogger(HideDayOutNightCtroller.class);
	
	@RequestMapping
	public String forward(HttpServletRequest request,HttpServletResponse response){
		try {
			//号牌颜色集合
			request.setAttribute("hpysList", this.systemManager.getCodes("031001"));
			//车辆颜色集合
			request.setAttribute("csysList", this.systemManager.getCode("030108"));
			//号牌种类集合
			request.setAttribute("hpzlList", this.systemManager.getCode("030107"));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return "analysis/hidedayoutnightmain";
	}
	
	@RequestMapping
	@ResponseBody
	public List<Map<String, Object>> queryList(HttpServletRequest request,HttpServletResponse response, String page, String rows,HideDayOutNightDayBean bean) {
		List<Map<String, Object>> list = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		if(StringUtils.isNotBlank(bean.getHphm())){
			String hphm = bean.getHphm().replace("?", "_").replace("？", "_");
			bean.setHphm(hphm);
		}		
		try {
			list = dayNightManager.findDayNightForPageExt(filter, bean);
		} catch (Exception e) {
			debugLogger.warn("根据条件模糊查询", e);
		}
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public int queryForDayNightTotal(HttpServletRequest request,HttpServletResponse response, String page, String rows,HideDayOutNightDayBean bean) throws Exception{
		    int total = 0;
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("page", page);
			filter.put("rows", rows);
			if(StringUtils.isNotBlank(bean.getHphm())){
				String hphm = bean.getHphm().replace("?", "_").replace("？", "_");
				hphm= URLDecoder.decode(hphm,"UTF-8");
				bean.setHphm(hphm);
			}
			try {
				total = this.dayNightManager.getForLinkTotal(filter, bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return total;
	}
	
	/**
	 * 按搜索条件查询过车轨迹数据列表
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<String, Object>> queryGjPassrecList(HttpServletRequest request, HttpServletResponse response, String page, String rows, VehPassrec veh){
		List<Map<String, Object>> list = null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		try {
			hphm= URLDecoder.decode(hphm,"utf-8");
			veh.setHphm(hphm);
			list = dayNightManager.queryGjPassrecListExt(veh, filter);
		} catch (Exception e) {
			debugLogger.warn("获取昼伏夜出过车轨迹失败", e);
		}
		return list; 
	}
	
	/**
	 * 按搜索条件查询过车轨迹总数
	 */
	@RequestMapping
	@ResponseBody
	public Integer queryGjPassrecListTotal(HttpServletRequest request, HttpServletResponse response, String page, String rows, VehPassrec veh){
		Integer total = 0;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		String hphm = veh.getHphm().replace("?", "_").replace("？", "_");
		try {
			//hphm = new String(hphm.getBytes("ISO-8859-1"),"utf-8");
			hphm = URLDecoder.decode(hphm,"utf-8");
			if (hphm.contains("%")) {
				hphm = URLDecoder.decode(hphm,"utf-8");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		veh.setHphm(hphm);
		try {
			total = dayNightManager.queryGjPassrecListTotal(veh, filter);
		} catch (Exception e) {
			debugLogger.warn("根据条件模糊查询", e);
		}
		return total;
	}
	
	/**
	 * 按搜索条件查询热力图数据
	 */
	@RequestMapping
	@ResponseBody
	public Object queryHideDayHotView(HttpServletRequest request, HttpServletResponse response, VehPassrec veh){
		List list = null;
		StringBuffer sb = new StringBuffer();
		try {
			String hphm = veh.getHphm();
			hphm = URLDecoder.decode(hphm,"utf-8");
			veh.setHphm(hphm);
			list = dayNightManager.queryHideDayHotView(veh);
			// 每天每小时过车量统计
			List listcar = dayNightManager.queryCarCount(veh);
			Collections.reverse(list);
			if ((list == null) && (list.size() < 1)) {
				sb.append("[]");
			} else {
				sb.append("[");
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> vs = (Map<String, Object>) list.get(i);
					boolean flag = false;
					for(int j = 0; j < listcar.size(); j++){
						Map<String, Object> vsCar = (Map<String, Object>) listcar.get(j);
						if(vsCar.get("days")== null||vsCar.get("hours")==null){
							continue;
						}
						String hours = ((Double)Double.parseDouble(vsCar.get("hours").toString())).intValue()+"";
						if(vsCar.get("days").equals(vs.get("rowid"))&&hours.equals(vs.get("columnid").toString())){
							flag = true;
							sb.append("{\"rowid\":\"")
							.append(vs.get("rowid"))
							.append("\",\"columnid\":\"")
							.append(vs.get("columnid"))
							.append("\",\"value\":\"")
							.append(vs.get("value"))
							.append("\",\"tllabel\":\"")
							.append(vs.get("tllabel"))
							.append("\",\"trlabel\":\"")
							.append(vs.get("trlabel"))
							.append("\",\"displayValue\":\"")
							.append(vsCar.get("cs"))
							.append("\"},");
							continue;
						}
					}
					if(!flag){
						sb.append("{\"rowid\":\"")
						.append(vs.get("rowid"))
						.append("\",\"columnid\":\"")
						.append(vs.get("columnid"))
						.append("\",\"value\":\"")
						.append(vs.get("value"))
						.append("\",\"tllabel\":\"")
						.append(vs.get("tllabel"))
						.append("\",\"trlabel\":\"")
						.append(vs.get("trlabel"))
						.append("\",\"displayValue\":\"")
						.append("0")
						.append("\"},");
					}
				}
				if (sb.length() == 1)
					sb.append(']');
				else
					sb.setCharAt(sb.length() - 1, ']');
			}
		} catch (Exception e) {
			debugLogger.warn("根据条件查询热力图", e);
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * 导出昼伏夜出的Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getCaseInfoXls(HttpServletRequest request,HttpServletResponse response, String page, String rows,HideDayOutNightDayBean bean) {
		String[] headName = new String[]{"号牌号码", "车牌颜色", "车牌种类", "次数"};
		short[] columnWidth = new short[]{6000,5000,5000,9000,3000,3000,3000}; 
		String[] method = new String[]{"hphm","hpysmc","hpzlmc","cs"};
		String name = "昼伏夜出分析";
		try {			
			List<Map<String,Object>> infos= this.queryList(request, response, page, rows, bean);
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
