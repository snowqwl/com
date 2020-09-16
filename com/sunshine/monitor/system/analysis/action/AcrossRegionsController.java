package com.sunshine.monitor.system.analysis.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.ExcelStyle;
import com.sunshine.monitor.system.analysis.bean.ModelDetail;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.service.AcrossRegionsManager;
import com.sunshine.monitor.system.analysis.service.HphmManager;
import com.sunshine.monitor.system.manager.bean.TztbBean;
import com.sunshine.monitor.system.query.dao.QueryListDao;

/**
 * 本地跨区域车辆分析控制层
 * @author licheng
 *
 */
@Controller
@RequestMapping(value = "/acrossRegions.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AcrossRegionsController {
	@Autowired
	private AcrossRegionsManager acrossRegionsManager;
	
	@Autowired
	private HphmManager hphmManager;
	
	private static String glbm = "";
	
	@Autowired
	private QueryListDao queryListDao;
	
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
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		glbm = userSession.getDepartment().getGlbm();
		Map<String,Object> filter = new HashMap<String, Object>();
		filter.put("dwdm", glbm.subSequence(0, 2));
		request.setAttribute("localList", hphmManager.getHphmList(filter));
		return "analysis/acrossRegionsmain";
	}
	
	/**
	 * 统计
	 * @param request
	 * @param response
	 * @param rows
	 * @param page
	 * @param sign
	 * @param zcd
	 * @param kssj
	 * @param jssj
	 * @param gw
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<String,Object>> getList(HttpServletRequest request,HttpServletResponse response,
		String rows ,String page,String local,String zcd,String kssj,String jssj,String gw,String gjx,String bdx)throws Exception{
		
		Map<String,Object> filter=new HashMap<String,Object>();
		String zcdlist="";
		if(!zcd.equals("")){
			zcdlist=zcd.substring(0,zcd.length()-1);
		}
		filter.put("rows", rows);
		filter.put("page", page);
		filter.put("zcd", URLDecoder.decode(zcdlist,"utf-8"));
		filter.put("kssj", kssj);
		filter.put("jssj", jssj);
		filter.put("gjx", gjx);
		filter.put("bdx", bdx);
		filter.put("local", URLDecoder.decode(local,"utf-8"));
		filter.put("sessionid", request.getSession().getId().replace("-", "_"));
		List<Map<String,Object>> list=null;
		try{
			list=this.acrossRegionsManager.getListExt(filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public Integer queryListTotal(HttpServletRequest request,HttpServletResponse response,
			String rows ,String page,String local,String zcd,String kssj,String jssj,String gw,String gjx,String bdx) throws Exception{
		int total = 0;
		Map<String,Object> filter=new HashMap<String,Object>();
		String zcdlist="";
		if(!zcd.equals("")){
			zcdlist=zcd.substring(0,zcd.length()-1);
		}
		filter.put("rows", rows);
		filter.put("page", page);
//		filter.put("zcd", new String(zcdlist.getBytes("ISO-8859-1"),"UTF-8"));
		filter.put("zcd", URLDecoder.decode(zcdlist,"utf-8"));
		filter.put("kssj", kssj);
		filter.put("jssj", jssj);
		filter.put("gjx", gjx);
		filter.put("bdx", bdx);
//		filter.put("local", new String(local.getBytes("ISO-8859-1"),"UTF-8"));
		filter.put("local", URLDecoder.decode(local,"utf-8"));
		filter.put("sessionid", request.getSession().getId().replace("-", "_"));
		try{
			total=this.acrossRegionsManager.getListTotal(filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 * 详情
	 * @param request
	 * @param response
	 * @param rows
	 * @param page
	 * @param xh
	 * @param sign
	 * @param pageSign
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping 
	@ResponseBody
	public List<Map<String,Object>> queryDetilList(HttpServletRequest request,HttpServletResponse response,
			String rows ,String page,String sign,String local,String zcd,String kssj,String jssj,String gw,String gjx,String bdx) throws Exception{
		
		Map<String, Object> filter=new HashMap<String, Object>();
		filter.put("rows", rows);
		filter.put("page", page);
		filter.put("zcd", URLDecoder.decode(zcd,"utf-8"));
		filter.put("kssj", kssj);
		filter.put("jssj", jssj);
		filter.put("gjx", gjx);
		filter.put("bdx", bdx);
		filter.put("local", URLDecoder.decode(local,"utf-8"));
		filter.put("sessionid", request.getSession().getId().replace("-","_"));
		List<Map<String,Object>> list=null;
		try {
			list=this.acrossRegionsManager.queryDetilListExt(filter, sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	@RequestMapping
	@ResponseBody
	public Integer queryDetailListTotal(HttpServletRequest request,HttpServletResponse response,
			String rows ,String page,String sign,String local,String zcd,String kssj,String jssj,String gw,String gjx,String bdx) throws Exception{
		int total = 0;
		Map<String, Object> filter=new HashMap<String, Object>();
		filter.put("rows", rows);
		filter.put("page", page);
//		filter.put("zcd", new String(zcd.getBytes("ISO-8859-1"),"UTF-8"));
		filter.put("zcd", URLDecoder.decode(zcd,"utf-8"));
		filter.put("kssj", kssj);
		filter.put("jssj", jssj);
		filter.put("gjx", gjx);
		filter.put("bdx", bdx);
//		filter.put("local", new String(local.getBytes("ISO-8859-1"),"UTF-8"));
		filter.put("local", URLDecoder.decode(local,"utf-8"));
		filter.put("sessionid", request.getSession().getId().replace("-","_"));
		try {
			total=this.acrossRegionsManager.queryDetilListTotal(filter, sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 * 过车轨迹列表
	 * @param request
	 * @param response
	 * @param rows
	 * @param page
	 * @param xh
	 * @param hpys
	 * @param hphm
	 * @param hpzl
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public Map<String, Object> queryForDetilList(HttpServletRequest request,HttpServletResponse response,
			String rows,String page,String hpys,String hphm,String hpzl,String local,String zcd,
			String kssj,String jssj,String gw,String gjx,String bdx){
		
		Map<String, Object> filter=new HashMap<String, Object>();
		Map<String, Object> result=null;
		
		try{
			filter.put("zcd", zcd);
			filter.put("kssj", kssj);
			filter.put("jssj", jssj);
			filter.put("gjx", gjx);
			filter.put("bdx", bdx);
			filter.put("local", local);
			filter.put("hphm",java.net.URLDecoder.decode(hphm,"UTF-8"));
			if (StringUtils.isBlank(hpzl)) {
				filter.put("hpzl", "");
			} else {
				filter.put("hpzl", java.net.URLDecoder.decode(hpzl, "UTF-8"));
			}
			if (StringUtils.isBlank(hpys)) {
				filter.put("hpys", "");
			} else {
				filter.put("hpys",java.net.URLDecoder.decode(hpys,"UTF-8"));
			}
			filter.put("page",page);
			filter.put("rows", rows);
		    result = this.acrossRegionsManager.queryForDetilList(filter);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 
	 * 导出本省异地分析的Execl
	 * @param request
	 * @param response
	 */
	@RequestMapping
	public void getAcrossXls(HttpServletRequest request,HttpServletResponse response,
			String rows ,String page,String local,String zcd,String kssj,String jssj,String gw,String gjx,String bdx) {
		String[] headName = new String[]{"注册地", "过境型", "候鸟型", "本地型", "总计"};
		short[] columnWidth = new short[]{3000,5000,5000,9000,3000,3000,3000,9000,12000,3000,3000,6000}; 
		String[] method = new String[]{"hp","gjx","hnx","bdx","total"};
		String name="本省异地分析";
		try {			
			List<Map<String,Object>> infos= this.getList(request, response, rows, page, local, zcd, kssj, jssj, gw, gjx, bdx);
			exportCommon.exportExecl(infos, name, response, headName, columnWidth, method);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 研判模型介绍
	 * @param request
	 * @param Id
	 * @return
	 */
	@RequestMapping
	public ModelAndView forwardMxjs(HttpServletRequest request,String Id) {
		ModelAndView mv = new ModelAndView();
		try {
			ModelDetail bean = this.queryListDao.getById(Id);
			request.setAttribute("bean",bean);
			mv.setViewName("analysis/mxjsDetail");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	
}
