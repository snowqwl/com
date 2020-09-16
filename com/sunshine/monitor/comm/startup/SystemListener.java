package com.sunshine.monitor.comm.startup;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.CacheManager;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
@Deprecated
public class SystemListener implements ServletContextListener {

	private Logger log = LoggerFactory.getLogger(SystemListener.class);

	private ServletContext context = null;

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
		ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(this.context);
		Scheduler scheduler = (Scheduler) act.getBean("quarzScheduler");
		this.context.setAttribute("SCHEDULERS", scheduler);
	}
}