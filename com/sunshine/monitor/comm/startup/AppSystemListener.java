package com.sunshine.monitor.comm.startup;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.CacheManager;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sunshine.monitor.comm.quart.AbstractScheduler;
import com.sunshine.monitor.comm.service.MenuManager;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.redlist.service.RedListManager;

/**
 * 初始系统资源用户、部门、字典、红名单、调度器
 * 
 * @author Administrator
 * 
 */
public class AppSystemListener implements ServletContextListener {

	private Logger log = LoggerFactory.getLogger(AppSystemListener.class);

	private ServletContext context = null;

	private <T> T getBean(ApplicationContext context, Class<T> cls) {
		return (T) BeanFactoryUtils.beanOfType(context, cls);
	}

	public void initScheduler(ApplicationContext context) {
		AbstractScheduler schedulerDao = getBean(context,
				AbstractScheduler.class);
		try {
			schedulerDao.initScheduler(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadAppMenus(ApplicationContext context) {
		MenuManager menuManager = getBean(context, MenuManager.class);
		try {
			menuManager.initMenuTree();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadAppCodes(ApplicationContext context) {
		SystemManager systemManager = getBean(context, SystemManager.class);
		int countCodes = 0;
		int deptCodes = 0;
		int userCodes = 0;
		int directCodes = 0;
		try {
			countCodes = systemManager.loadCode();
			log.debug("系统代码值加载成功！共加载:" + countCodes + "条记录");
			deptCodes = systemManager.loadDepartment();
			log.debug("系统部门加载成功！共加载:" + deptCodes + "条记录");
			userCodes = systemManager.loadUser();
			log.debug("系统用户加载成功！共加载:" + userCodes + "条记录");
			directCodes = systemManager.loadDirect();
			log.debug("系统卡口方向加载成功！共加载：" + directCodes + "条记录");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadRedList(ApplicationContext context) {
		RedListManager redListManager = getBean(context, RedListManager.class);
		redListManager.initRedList();
	}

	public void loadGateTree(ApplicationContext context) {
		SystemManager systemManager = getBean(context, SystemManager.class);
		try {
			systemManager.loadGateTree();
			systemManager.loadGateTreeFilterDirect();
			log.info("卡口选择树加载成功");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contextDestroyed(ServletContextEvent event) {
		try {
			Scheduler scheduler = (Scheduler) event.getServletContext()
					.getAttribute("SCHEDULERS");
			scheduler.shutdown(true);
			log.info("[trans]scheduler:" + scheduler.isShutdown());
			this.context.removeAttribute("SCHEDULERS");
			// Shutdown cache
			CacheManager cacheManager = CacheManager.getInstance();
			cacheManager.shutdown();
			log.info("Ehcache缓存关闭!");

		} catch (Exception ex) {
			log.info("[trans]Schedule Destroyed Error:" + ex.getMessage());
		}
	}

	public void contextInitialized(ServletContextEvent event) {
		this.context = event.getServletContext();
		// SPRINT CONTENT
		ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(this.context);
		loadAppMenus(context);
		loadAppCodes(context);
		initScheduler(context);
		loadRedList(context);
		SysparaManager sysparaManager = (SysparaManager)SpringApplicationContext
		                                .getStaticApplicationContext().getBean("sysparaManager");
		try {
			String xzqh = sysparaManager.getSyspara("xzqh","1",null).getCsz();
			if(xzqh.startsWith("4300")){
				loadGateTree(context);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Scheduler scheduler = (Scheduler) context.getBean("quarzScheduler");
		try {
			scheduler.startDelayed(20);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		this.context.setAttribute("SCHEDULERS", scheduler);
	}
}