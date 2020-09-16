package com.sunshine.monitor.system.manager.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.SysparaManager;

@Controller
@RequestMapping(value = "/syspara.do", params = "method")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SysparaCtroller {
	
	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysparaManage = null;
	
	
	@RequestMapping()
	public String index() {
		return "manager/sysparalist";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping()
	@ResponseBody
	public Map<String,Object> queryList(String page, String rows, String paramString1) {
		
		Map<String,Object> queryMap = null;
		try {
			Map filter = new HashMap();
			filter.put("curPage", page);
			filter.put("pageSize", rows);
			filter.put("glbm","445300000000");
			queryMap = this.sysparaManage.getSysparas(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}

	public void setSysparaManage(SysparaManager sysparaManage) {
		this.sysparaManage = sysparaManage;
	}
}
