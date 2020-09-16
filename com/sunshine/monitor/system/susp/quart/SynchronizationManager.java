package com.sunshine.monitor.system.susp.quart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.susp.dao.SuspInfoCancelDao;

//@Component("synchronizationManager")
public class SynchronizationManager extends BaseDaoImpl {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void doTask() {
		logger.info("警综被盗抢二次同步-启动");
		//获取自动撤控的信息
		try {
			SuspInfoCancelDao suspInfoCancelDao = (SuspInfoCancelDao)SpringApplicationContext.getStaticApplicationContext().getBean("suspInfoCancelDao");
			suspInfoCancelDao.synchronizationJz();
			
			logger.info("警综被盗抢二次同步-结束");
		}catch (Exception e) {
			//logger.error(e);
			e.printStackTrace();
			
		}
		
	}


}
