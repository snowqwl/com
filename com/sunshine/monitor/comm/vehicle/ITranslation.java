package com.sunshine.monitor.comm.vehicle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.service.SystemManager;

/**
 * 翻译接口
 * @author Administrator
 *
 */
public abstract class ITranslation {
	
	private SystemManager systemManager;
	
	private Logger log = LoggerFactory.getLogger(ITranslation.class);

	private String dmlb;
	
	public ITranslation(String dmlb) {
		this.dmlb = dmlb;
		setSystemManager(SpringApplicationContext.getBean("systemManager",
				SystemManager.class));
		if(this.systemManager == null)
			log.error("SystemManager对象注入失败!", 
					new IllegalArgumentException("SystemManager对象注入失败!"));
	}
	
	public String getDmlb() {
		return dmlb;
	}

	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}
	
	public SystemManager getSystemManager() {
		return systemManager;
	}

	public abstract String getMC(String dmz) throws Exception;
}
