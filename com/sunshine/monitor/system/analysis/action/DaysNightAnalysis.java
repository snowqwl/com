package com.sunshine.monitor.system.analysis.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.analysis.bean.DayNightAnalysis;
import com.sunshine.monitor.system.analysis.bean.DayNigntNewAnaiysis;
import com.sunshine.monitor.system.analysis.service.DaysNightAnalysisManager;

@Controller
@RequestMapping(value = "/daysNightAnalysis.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DaysNightAnalysis {
	
	@Autowired
	private DaysNightAnalysisManager daysNightAnalysisManager;
	
	@RequestMapping
	public ModelAndView forward(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("analysis/dayNightNewAnaiysis");
	}
	
	@ResponseBody
	@RequestMapping
	public Object saveDayNightGz(HttpServletRequest request,
			HttpServletResponse response, DayNightAnalysis daynight) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			DayNightAnalysis info = daynight;			
			int xh = this.daysNightAnalysisManager.saveDayNightGz(info);
			if (xh != 1) {
				_map.put("msg", "保存成功!");
			} else {
				_map.put("msg", "保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	
	@ResponseBody
	@RequestMapping
	public Object getDqValid(HttpServletRequest request,
			HttpServletResponse response) {
		List<DayNightAnalysis> result = null;
		try {			
				result = this.daysNightAnalysisManager.getDqValid();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public Object queryGZList(HttpServletRequest request) {
		try {
			Map<String, Object> conditions = Common.getParamentersMap(request);
			Map<String, Object> map = this.daysNightAnalysisManager.queryGZList(conditions);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping
	public Object delDayNightAna(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			String gzxh = request.getParameter("gzxh");
			int fh = this.daysNightAnalysisManager.delDayNightAna(gzxh);
			boolean flag = false;
			if (fh != 0) {
				flag = true;
				_map.put("msg", "删除成功!");
			} else {
				_map.put("msg", "删除失败!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	
	@ResponseBody
	@RequestMapping
	public Object getEditDaynightInfo(HttpServletRequest request) throws Exception {
		String gzxh = request.getParameter("gzxh");
		DayNightAnalysis result = null;
		try {
			result = this.daysNightAnalysisManager.getEditDaynightInfo(gzxh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@ResponseBody
	@RequestMapping
	public Object editDayNightGz(HttpServletRequest request,
			HttpServletResponse response,DayNightAnalysis info) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			//String gzxh = request.getParameter("gzxh");
			int fh = this.daysNightAnalysisManager.editDayNightGz(info);
			boolean flag = false;
			if (fh != 0) {
				flag = true;
				_map.put("msg", "更新成功!");
			} else {
				_map.put("msg", "更新失败!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	
	@ResponseBody
	@RequestMapping
	public Object editZt(HttpServletRequest request,
			HttpServletResponse response,DayNightAnalysis info) {
		Map<String, String> _map = null;
		try {
			_map = new HashMap<String, String>();
			String zt = request.getParameter("zt");
			if("1".equals(zt)){
				int gxfh = this.daysNightAnalysisManager.editOtherZt();
			}
			int fh = this.daysNightAnalysisManager.editZt(info,zt);
			boolean flag = false;
			if (fh != 0) {
				flag = true;
				_map.put("msg", "状态更新成功!");
			} else {
				_map.put("msg", "状态更新失败!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}
	
	@ResponseBody
	@RequestMapping
	public Object getZyxx(
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = null;
		try {
			result = new HashMap<String, Object>();
			DayNightAnalysis info = this.daysNightAnalysisManager.getZyxx();			
				result.put("info", info);											
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/*
	 * 昼伏夜出分页查询
	 */
	@ResponseBody
	@RequestMapping
	public List<Map<String,Object>> queryList(HttpServletRequest request,HttpServletResponse response,String page,String rows,String sort,String order,DayNigntNewAnaiysis dn){
		List<Map<String,Object>> list =null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("order", order); 
		filter.put("sort", sort);
		try{
			list = this.daysNightAnalysisManager.queryForDayNightListExt(dn,filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping
	@ResponseBody
	public Integer queryForDayNightTotal(HttpServletRequest request,HttpServletResponse response,String page,String rows,String sort,String order,DayNigntNewAnaiysis dn) throws Exception{
		int total = 0;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		filter.put("order", order); 
		filter.put("sort", sort);
		try{
			total = this.daysNightAnalysisManager.getForDayNightTotal(dn,filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return total;
	}
	
	@ResponseBody
	@RequestMapping
	public Map<String,Object> queryDayNightPageList(HttpServletRequest rquest,HttpServletResponse response,DayNigntNewAnaiysis dn,String page,String rows){
		Map<String,Object> map =null;
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("page", page);
		filter.put("rows", rows);
		try{
			map = this.daysNightAnalysisManager.queryDayNightPageList(dn,filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
}
