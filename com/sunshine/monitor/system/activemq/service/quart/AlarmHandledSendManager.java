package com.sunshine.monitor.system.activemq.service.quart;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dc.trans.send.AlarmHandledSend;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.activemq.bean.TransAlarmHandled;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.activemq.service.send.AlarmhandledQueueProducerManager;

public class AlarmHandledSendManager extends BaseDaoImpl{
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public void doTask() {
		//logger.info("自动出警传输-启动.");
		//获取自动报警传输信息
		try {
			TransDao transDao = (TransDao)SpringApplicationContext.getStaticApplicationContext().getBean("transDao");
			AlarmhandledQueueProducerManager alarmhandledQueueProducerManager = 
					(AlarmhandledQueueProducerManager)SpringApplicationContext.getStaticApplicationContext().getBean("alarmhandledQueueProducerManager");
			//1、获取报警传输待发送记录
			List list = transDao.getTransList("JM_TRANS_ALARM", "25", "50");
			if ((list != null) && (list.size() > 0)) {
				int size = list.size();
				logger.info("出警传输（发送），本次出警传输取到的数量为" + size);
				for (Iterator it = list.iterator(); it.hasNext();) {
					try {
						TransObj transobj = (TransObj) it.next();
						TransAlarmHandled alarmHandled = transDao.getTransAlarmHandledDetail(transobj.getYwxh());
						transobj.setObj(alarmHandled);
						//2、发送消息
						alarmhandledQueueProducerManager.send(transobj);
						try{
							new AlarmHandledSend().doSend();
						}catch(Exception e){
							e.printStackTrace();
						}
						//3、更新调度状态
						alarmhandledQueueProducerManager.updateTransStatus("JM_TRANS_ALARM", transobj.getCsxh());
						//transDao.updateTransStatus("JM_TRANS_ALARM", transobj.getCsxh());
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			} else if (list == null) {
				//logger.info("出警传输（发送），本次出警传输取到的List为Null");
			} else {
				//logger.info("出警传输（发送），本次出警传输取到的数量为0");
			}
			//logger.info("自动出警传输-结束.");
		}catch (Exception e) {
				e.printStackTrace();
		}
		
	}
	

}
