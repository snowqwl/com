package com.sunshine.monitor.comm.log;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.sunshine.monitor.comm.bean.Menu;
import com.sunshine.monitor.comm.bean.Role;
import com.sunshine.monitor.comm.bean.RoleMenu;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.service.MenuManager;
import com.sunshine.monitor.comm.service.RoleManager;
import com.sunshine.monitor.system.manager.bean.Log;
import com.sunshine.monitor.system.manager.service.LogManager;

/**
 * 新增日志处理的实现类
 * @author liumeng
 * 2016-4-21
 */
@Service
public class LogHandlerInterImpl extends AbstractLogHandlerInter{

	@Autowired(required=true)
	@Qualifier("logManager")
	private LogManager logManager;
	
	@Autowired(required=true)
	private RoleManager roleManager;
	
	@Autowired(required=true)
	private MenuManager menuManager;
	
	public void doHandle(Object obj,LogHandlerParam bean,HttpServletRequest request) throws Exception {
		// 写数据库
		try {
			UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request , "userSession");
			String yhdh = userSession.getSysuser().getYhdh();
			String glbm = userSession.getDepartment().getGlbm();
			Log log = new Log();
			log.setGlbm(glbm);
			log.setIp(request.getRemoteAddr());
			log.setYhdh(yhdh);
			log.setCzlx(bean.getType());
			
			System.out.println(obj.toString());
			Object[] os = (Object[])obj;
			RoleMenu roleMenu = (RoleMenu)os[0];
			//1.查询角色
			Role roleBean = roleManager.getRoleById(roleMenu.getJsdh());
			//2.查询权限
			Menu menuBean = menuManager.findMenu(roleMenu.getCxdh());
			
			String cznr = userSession.getSysuser().getYhmc()+" 对  "+roleBean.getJsmc()+" "
					+bean.getDescription().substring(0,2)+menuBean.getName(); 
			log.setCznr(cznr);
			logManager.saveLog(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void doAfterHandle(Object obj, LogHandlerParam bean,HttpServletRequest request)
			throws Exception {
		doHandle(obj, bean, request);	
	}

	
}
