package com.sunshine.monitor.system.manager.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.dao.SysUserDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;

/**
 * 定时同步警综情报人员
 * 
 * @author licheng
 *
 */
public class SynchrQbUser extends BaseDaoImpl{
	protected final Log logger = LogFactory.getLog(getClass());

	public void doTask(){
		try {
			SysUserDao sysUserDao = (SysUserDao)SpringApplicationContext.getStaticApplicationContext().getBean("sysUserDao");
			SystemDao systemDao = (SystemDao)SpringApplicationContext.getStaticApplicationContext().getBean("systemDao");
			int i = sysUserDao.syncQbUser();
			if (i < 0) {
				logger.info("同步警综情报人员失败!");
			} else {
				i = systemDao.loadUser();
				if (i < 0)
					logger.info("同步警综情报人员失败!");
				else {
					logger.info("同步警综情报人员成功!");
				}
			}
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
