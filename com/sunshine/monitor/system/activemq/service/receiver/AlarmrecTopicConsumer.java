package com.sunshine.monitor.system.activemq.service.receiver;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.activemq.bean.TransAlarm;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;

/**
 * 使用多线程接受消息
 */
@Transactional
public class AlarmrecTopicConsumer  {
	
	private static final Log log = LogFactory.getLog(AlarmrecTopicConsumer.class);
	
	@Autowired
	private VehAlarmManager vehAlarmManager;
	@Autowired
	private SysparaDao sysparaDao;
	
	ExecutorService exec = Executors.newFixedThreadPool(5);    
	
	public void receiveAlarmrec(final TransObj message) {
		exec.submit((new Runnable() {
			public void run() {
				log.info("**********地市接收 预警信息Topic : " + ((TransAlarm)message.getObj()).getBkxh());
				try {
					Syspara sysparam = sysparaDao.getSyspara("xzqh", "1", "");
					String csz = sysparam.getCsz();	//本地行政区
					String[] bkfw = message.getJsdw().split(",");
					List<String> bkfwList = Arrays.asList(bkfw);
					if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)&&csz!="430000000000"){
						vehAlarmManager.saveAlarmLink((TransAlarm)message.getObj());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}
	/*public void receiveAlarmrec(final AlarmrecMessage message) {
		exec.submit((new Runnable() {
			public void run() {
				System.out.println("**********地市接收 预警信息Topic : " + message.getAlarminfo().getBkxh());
				try {
					vehAlarmManager.saveAlarmLink(message.getAlarminfo());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}*/

}
