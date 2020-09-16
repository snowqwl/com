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
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

/**
 * 使用多线程接受消息
 */
@Transactional
public class AlarmsureTopicConsumer  {
	
	private static final Log log = LogFactory.getLog(AlarmsureTopicConsumer.class);
	
	@Autowired
	private VehAlarmManager vehAlarmManager;
	@Autowired
	private SuspinfoManager suspinfoManager;
	@Autowired
	private SysparaDao sysparaDao;
	
	ExecutorService exec = Executors.newFixedThreadPool(5);    
	
	public void receiveAlarmsure(final TransObj message) {
		exec.submit((new Runnable() {
			public void run() {
				log.info("**********地市接收 预警签收信息Topic : " + ((TransAlarm)message.getObj()).getBkxh());
				try {
					Syspara sysparam = sysparaDao.getSyspara("xzqh", "1", "");
					String csz = sysparam.getCsz();	//本地行政区
					String[] bkfw = message.getJsdw().split(",");
					List<String> bkfwList = Arrays.asList(bkfw);
					if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)&&csz!="430000000000"){
						vehAlarmManager.saveAlarmsureLink((TransAlarm)message.getObj());
						
						VehSuspinfo veh = new VehSuspinfo();
						veh.setBkxh(((TransAlarm)message.getObj()).getBkxh());
						veh.setBjzt("1");
						veh.setBjsj(((TransAlarm)message.getObj()).getBjsj());
						suspinfoManager.updateSuspinfo(veh);
						
					}/*else if(bkfwList.contains(csz) && message.getCsdw().equals(csz)){
						//如果为原布控地市，更新布控表的报警状态为1
						VehSuspinfo veh = new VehSuspinfo();
						veh.setBkxh(((TransAlarm)message.getObj()).getBkxh());
						veh.setBjzt("1");
						veh.setBjsj(((TransAlarm)message.getObj()).getBjsj());
						suspinfoManager.updateSuspinfo(veh);
					}*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}
	
	/*public void receiveAlarmsure(final AlarmrecMessage message) {
		exec.submit((new Runnable() {
			public void run() {
				System.out.println("**********地市接收 预警签收信息Topic : " + message.getAlarminfo().getBkxh());
				try {
					vehAlarmManager.saveAlarmsureLink(message.getAlarminfo());					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}*/

}
