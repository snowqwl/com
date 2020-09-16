package com.sunshine.monitor.system.manager.quartz;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.sunshine.monitor.comm.util.Ping;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.veh.dao.PassrecDao;

public class StStatFlow {
	/**
	 * 省厅库定时抽取各地市的过车量
	 */
	public void stat() {
		PassrecDao passrecDao = (PassrecDao) SpringApplicationContext.getStaticApplicationContext().getBean("passrecDao");
		SystemDao systemDao = (SystemDao) SpringApplicationContext.getStaticApplicationContext().getBean("systemDao");
		UrlDao urlDao = (UrlDao ) SpringApplicationContext.getStaticApplicationContext().getBean("urlDao");
		
		// 判断操作系统类型
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name").toLowerCase();
		String ostype;
		if (os.startsWith("win")) {
			// windows操作系统
			ostype = "1";
		} else {
			// Linux操作系统
			ostype = "2";
		}
		try {
			List<Code> citylist = systemDao.getCode("10500");
			List<CodeUrl> cuList= urlDao.getCodeUrls();
			for(Code code:citylist) {
				for(CodeUrl cu : cuList) {
					if(code.getDmz().equals(cu.getDwdm().substring(0, 6))) {
						if("1".equals(Ping.test(cu.getUrl(), ostype))) {
							passrecDao.updateSTFlow(code.getDmsm1());
						}
					}
				}
				//passrecDao.updateSTFlow(code.getDmsm1());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
