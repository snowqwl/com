package com.sunshine.monitor.comm.vehicle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SystemDao;

public class VehQuery implements KeyConfig{
	
	private Logger log = LoggerFactory.getLogger(VehQuery.class);
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	/**
	 * 机动车请求链引用
	 */
	private IRequestHandler iRequestTableHandler;
	
	public VehQuery(){
		iRequestTableHandler = SpringApplicationContext.getBean("iRequestTableHandler", IRequestHandler.class);
		if(iRequestTableHandler == null)
			log.error("请求链对象获取失败!BeanName=iRequestTableHandler", 
					new IllegalArgumentException("请求链对象获取失败!BeanName=iRequestTableHandler"));
	}
	
	public void setiRequestTableHandler(IRequestHandler iRequestTableHandler) {
		this.iRequestTableHandler = iRequestTableHandler;
	}

	/**
	 * 查询组名，对应rbsp_setup.ini文件配置
	 */
	private static final String QUERY_GROUP_NAME = "QueryPJDC";
	
	
	public String getVehilceInfo(HttpServletRequest request, String hpzl,
			String hphm) {
		String isQueryPic = request.getParameter("flag");
		if ((hpzl == null) || (hphm == null) || (hpzl.length() != 2)
				|| ("".equals(hphm))) {
			return "{'result':'输入的号牌种类和号牌号码错误！'}";
		}
		UserSession userSession = (UserSession) WebUtils.getSessionAttribute(
				request, "userSession");
		List<Syspara> sysparaList = userSession.getSyspara();
		Syspara vehQueryStyle = null;
		boolean isQueryFzjg = true;
		for (Iterator<Syspara> iterator = sysparaList.iterator(); iterator
				.hasNext();) {
			Syspara para = (Syspara) iterator.next();
			if ("VehQueryStyle".equalsIgnoreCase(para.getGjz()))
				vehQueryStyle = para;
			else if ("IsQueryFzjg".equalsIgnoreCase(para.getGjz())) {
				isQueryFzjg = new Boolean(para.getCsz()).booleanValue();
			}
		}
		SysUser sysUser = userSession.getSysuser();
		if(!"RBSP".equals(vehQueryStyle.getCsz()))
			return "{'result':'机动车查询方式配置错误，请联系系统管理员！'}";
	  return  getVehMsg(hphm, hpzl, isQueryPic, isQueryFzjg,sysUser);
	  
	}
	
	public String getVehMsg(String hphm,String hpzl,
			String isQueryPic,Boolean isQueryFzjg,SysUser sysUser){
		log.info("机动车查询：HPHM="+ hphm+", HPZL=" + hpzl);
		// 组织查询条件
		Map<String, String> conditionMap = new HashMap<String,String>();
		conditionMap.put(KEY_HPHM, chgHphm(hphm,isQueryFzjg));
		conditionMap.put(KEY_HPZL, hpzl);
		conditionMap.put(KEY_JH,sysUser.getJh());
		conditionMap.put(KEY_CARD_ID,sysUser.getSfzmhm());
		conditionMap.put(KEY_DEPTNO,sysUser.getGlbm());
		conditionMap.put(KEY_QUERY_GROUP_NAME, QUERY_GROUP_NAME);
		// 请求处理链
		try {
			Object result = null;
			result = iRequestTableHandler.doHandler(conditionMap);
			if(result != null){ 
				return result.toString();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "{result:'未查询到机动车信息!'}";
	}

	public String chgHphm(String hphm, boolean hasFzjg) {
		String resHphm = "";
		char c = hphm.charAt(hphm.length() - 1);
		if ((c == '挂') || (c == '学') || (c == 35686) || (c == '教'))
			resHphm = hphm.substring(0, hphm.length() - 1);
		else {
			resHphm = hphm.substring(0, hphm.length());
		}
		return !hasFzjg ? resHphm.substring(1) : resHphm;
	}
}
