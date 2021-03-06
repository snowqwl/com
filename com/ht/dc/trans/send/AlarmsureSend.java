package com.ht.dc.trans.send;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dc.trans.bean.TransAlarm;
import com.ht.dc.trans.bean.TransObj;
import com.ht.dc.trans.dao.TransDcDao;
import com.ht.dc.trans.send.business.SendData;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.dao.UrlDao;

public class AlarmsureSend {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public AlarmsureSend() {
		super();
	}

	public void doSend() {
		TransDcDao transDcDao = (TransDcDao) SpringApplicationContext
				.getStaticApplicationContext().getBean("transDcDao");
		UrlDao urlDao = (UrlDao) SpringApplicationContext
				.getStaticApplicationContext().getBean("urlDao");
		List list = transDcDao.getTransList("JM_TRANS_ALARM", "22", "50");

		if ((list != null) && (list.size() > 0)) {
			    int size = list.size();
				logger.info("[jcbk->dc][transMQ]省厅预警签收传输（发送），本次预警签收传输取到的数量为" + size);
				for (Iterator it = list.iterator(); it.hasNext();) {
					try {
						TransObj transobj = (TransObj) it.next();
						TransAlarm alarm = transDcDao.getTransAlarmDetail(transobj.getYwxh());
						transobj.setObj(alarm);

						String[] jsdws = transobj.getJsdw().split(",");
						// 2、发送消息,调用dc transSevrlet

						for (String s : jsdws) {
							CodeUrl url = urlDao.getUrl(s);
							if("dc".equals(url.getContext())){
								SendData.send(transobj, url);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		}
	}
}
