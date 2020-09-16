package com.sunshine.monitor.system.analysis.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.comm.util.orm.helper.QueryCondition.Op;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.service.ScsVehPassrecService;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Controller
@RequestMapping(value = "/scsVehPassrec.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
public class ScsVehPassrecController {
	@Autowired
	@Qualifier("scsVehPassrecService")
	private ScsVehPassrecService scsVehPassrecService;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	/**
	 * rows,page 必须传值
	 * @param request
	 * @param response
	 * @param kdbhs
	 * @param startTime
	 * @param endTime
	 * @param licenseColor
	 * @param licenseType
	 * @param carNo
	 * @param rows
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping
	public Map<String,Object> list(HttpServletRequest request , 
			HttpServletResponse response, String kdbhs , 
			String startTime , String endTime , 
			String licenseColor, String licenseType,
			String carNo,
			Integer rows, Integer page){
		Map<String,Object> map = new HashMap<String,Object>();
		Cnd cnd = Cnd.where();
		if(startTime!=null)cnd.and("gcsj", Op.GT, startTime);
		if(endTime!=null)cnd.and("gcsj", Op.LT, endTime);
		if(licenseColor!=null)cnd.and("hpys",Op.EQ,licenseColor);
		if(licenseType!=null)cnd.and("hpzl", Op.EQ, licenseType);
		if(kdbhs!=null)cnd.and("kdbh", Op.IN, kdbhs.split(","));
		if(carNo!=null)cnd.and("hphm", Op.EQ, carNo);
		Pager pager = new Pager(page,rows);
		List<ScsVehPassrec> data = null;
		try {
			data = scsVehPassrecService.findList(cnd, pager);
			for(int i=0;i<data.size();i++){
				ScsVehPassrec scs = data.get(i);
				scs.setXzqhmc(this.systemManager.getDistrictNameByXzqh(scs.getKdbh().substring(0,6)));			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("rows", data);
		map.put("total", pager.getPageCount());
		return map;
	}
	
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object> getVehDetail(HttpServletRequest request,HttpServletResponse response,
			ScsVehPassrec veh) {
        Map<String,Object> result = null;
		try {
			result = this.scsVehPassrecService.getVehDetail(veh.getGcxh());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public Map queryForLikeLast(HttpServletRequest request,HttpServletResponse response ,VehPassrec veh){
	
		return null;
	}
	
}
