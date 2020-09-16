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
import com.sunshine.monitor.system.activemq.bean.TransCmd;
import com.sunshine.monitor.system.activemq.bean.TransLivetrace;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.VehAlarmHandleManager;
import com.sunshine.monitor.system.alarm.service.VehAlarmLiveTraceManager;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;

/**
 * 使用多线程接受消息
 */
@Transactional
public class AlarmhandledTopicConsumer  {
	
	private static final Log log = LogFactory.getLog(AlarmhandledTopicConsumer.class);
	
	@Autowired
	private VehAlarmHandleManager vehAlarmHandleManager;
	@Autowired
	private VehAlarmManager vehAlarmManager;
	@Autowired
	private VehAlarmLiveTraceManager vehAlarmLiveTraceManager;
	@Autowired
	private SysparaDao sysparaDao;
	
	ExecutorService exec = Executors.newFixedThreadPool(5);    
	
	public void receive(final TransObj message) {
		exec.submit((new Runnable() {
			public void run() {
				log.info("**********地市接收 出警反馈信息Topic : " + ((TransAlarmHandled)message.getObj()).getBkxh());
				try {
					Syspara sysparam = sysparaDao.getSyspara("xzqh", "1", "");
					String csz = sysparam.getCsz();	//本地行政区
					String[] bkfw = message.getJsdw().split(",");
					List<String> bkfwList = Arrays.asList(bkfw);
					if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)&&csz!="430000000000"){
						//保存出警反馈(联动)
						vehAlarmHandleManager.saveAlarmHandleLink((TransAlarmHandled)message.getObj());
						//保存指令记录集
						List<TransCmd> alarmCmdList = ((TransAlarmHandled)message.getObj()).getCmdList();
						for(TransCmd alarmCmd : alarmCmdList){
							vehAlarmManager.saveCmd(alarmCmd);
						}
						//保存出警跟踪记录集
						List<TransLivetrace> alarmTraceList = ((TransAlarmHandled)message.getObj()).getLivetraceList();
						for(TransLivetrace alarmTrace : alarmTraceList){
							vehAlarmLiveTraceManager.saveLiveTrace(alarmTrace);
						}
						//更新报警记录状态(是否下达指令、是否反馈、是否拦截)
						String bjxh = ((TransAlarmHandled)message.getObj()).getBjxh();
						VehAlarmrec alermRec = vehAlarmManager.getAlarmsign(bjxh);
						alermRec.setSffk("1");		//是否已出警反馈
						alermRec.setSfxdzl("1");	//是否已下达指令
						alermRec.setSflj(((TransAlarmHandled)message.getObj()).getSflj());  //是否拦截到
						vehAlarmManager.saveAlarmSign(alermRec);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}
	
	/*public void receiveAlarmHandled(final AlarmhandledMessage message) {
		exec.submit((new Runnable() {
			public void run() {
				System.out.println("**********地市接收 出警反馈信息Topic : " + message.getAlarmHandled().getBkxh());
				try {
					//保存出警反馈(联动)
					vehAlarmHandleManager.saveAlarmHandleLink(message.getAlarmHandled());
					//保存指令记录集
					List<VehAlarmCmd> alarmCmdList = message.getAlarmHandled().getCmdList();
					for(VehAlarmCmd alarmCmd : alarmCmdList){
						vehAlarmManager.saveCmd(alarmCmd);
					}
					//保存出警跟踪记录集
					List<VehAlarmLivetrace> alarmTraceList = message.getAlarmHandled().getLivetraceList();
					for(VehAlarmLivetrace alarmTrace : alarmTraceList){
						vehAlarmLiveTraceManager.saveLiveTrace(alarmTrace);
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}*/
	
}
