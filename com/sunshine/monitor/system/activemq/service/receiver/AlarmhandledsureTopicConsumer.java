package com.sunshine.monitor.system.activemq.service.receiver;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.activemq.bean.TransAlarmHandled;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.alarm.service.VehAlarmHandleManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;

/**
 * 使用多线程接受消息
 */
@Transactional
public class AlarmhandledsureTopicConsumer  {
	
	private static final Log log = LogFactory.getLog(AlarmhandledsureTopicConsumer.class);
	
	@Autowired
	private VehAlarmHandleManager vehAlarmHandleManager;
	
	@Autowired
	private SysparaDao sysparaDao;
	
	ExecutorService exec = Executors.newFixedThreadPool(5);    
	
	public void receive(final TransObj message) {
		exec.submit((new Runnable() {
			public void run() {
				log.info("**********地市接收指挥中心反馈签收信息Topic : " + ((TransAlarmHandled)message.getObj()).getBkxh());
				try {
					Syspara sysparam = sysparaDao.getSyspara("xzqh", "1", "");
					String csz = sysparam.getCsz();	//本地行政区
					String[] bkfw = message.getJsdw().split(",");
					List<String> bkfwList = Arrays.asList(bkfw);
					if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)&&csz!="430000000000"){
						//保存出警反馈(联动)
						vehAlarmHandleManager.saveAlarmHandleLink((TransAlarmHandled)message.getObj());
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}
	
}
