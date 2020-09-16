package com.sunshine.monitor.system.analysis.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.analysis.action.NightCarController;
import com.sunshine.monitor.system.analysis.action.NightCarController.DateCache;
import com.sunshine.monitor.system.analysis.action.NightCarController.DateCacheInMen;
import com.sunshine.monitor.system.analysis.service.NightCarService;

public class NightCarCacheInitListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent paramServletContextEvent) {

	}

	public void contextInitialized(ServletContextEvent event) {
		Map<String, DateCache> obj = new HashMap<String, DateCache>();
		event.getServletContext().setAttribute(
				NightCarController.NIGHTCAR_ATTR, obj);
		NightCarService nightCarManager = (NightCarService) SpringApplicationContext
				.getStaticApplicationContext().getBean("nightCarManager");
		try {
			// 第一次进入 创建表
			nightCarManager.createTempTable("");
			//初始化缓存
			Map<String, List<Date>> allGroupIdCarDate = nightCarManager
					.getAllGroupIdCarDate();
			for (Map.Entry<String, List<Date>> temp : allGroupIdCarDate
					.entrySet()) {
				DateCache dateCache = new DateCacheInMen(Calendar.DATE);
				obj.put(temp.getKey(), dateCache);
				List<Date> cacheDates = temp.getValue();
				for (Date cacheDate : cacheDates) {
					dateCache.noCacheDate(temp.getKey(), cacheDate, cacheDate);
					dateCache.complate();
				}
			}
		} catch (Throwable t) {
			event.getServletContext().setAttribute(
					NightCarController.NIGHTCAR_ATTR, null);
			throw new RuntimeException(t);
		}
	}

}
