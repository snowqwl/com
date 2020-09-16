package com.sunshine.monitor.system.activemq.service.receiver;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.activemq.bean.TransAlarm;
import com.sunshine.monitor.system.activemq.bean.TransAlarmHandled;
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.activemq.bean.TransCmd;
import com.sunshine.monitor.system.activemq.bean.TransLivetrace;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.bean.TransSuspmonitor;
import com.sunshine.monitor.system.activemq.service.send.SuspinfomonitorQueueProducerManager;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.VehAlarmHandleManager;
import com.sunshine.monitor.system.alarm.service.VehAlarmLiveTraceManager;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;


/**
 * 使用多线程接受消息
 */
@Transactional
//public class SuspinfoTopicConsumer implements MessageListener  {
public class SuspinfoTopicConsumer  {
	
	private static final Log log = LogFactory.getLog(SuspinfoTopicConsumer.class);
	
	@Autowired
	private SuspinfoManager suspinfoManager;
	@Autowired
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;
	@Autowired
	private VehAlarmManager vehAlarmManager;
	@Autowired
	private VehAlarmHandleManager vehAlarmHandleManager;
	@Autowired
	private VehAlarmLiveTraceManager vehAlarmLiveTraceManager;
	@Autowired
	private SysparaDao sysparaDao;
	
	//ExecutorService exec = Executors.newFixedThreadPool(5);
	SimpleMessageConverter simpleMessageConverter;
    
	public void receiveSuspinfo(final TransObj message) {
		//exec.submit((new Runnable() {
			//public void run() {
				//TransBean transBean = (TransBean)message.getObj();
				String info = "";
				switch(Integer.valueOf(message.getType())) {
				case 11: 
					info = "布控";
					break;
				case 13:
					info = "撤控";
					break;
				case 21:
					info = "预警";
					break;
				case 22:
					info = "预警签收";
					break;
				case 25:
					info = "出警反馈";
					break;
				case 26:
					info = "指挥中心签收反馈";
					break;
				default:
					info = "[无定义]type="+message.getType();
				}
				//log.info(">>>>>>>>地市接收"+info+"订阅信息[Topic] :业务序号为 " + message.getYwxh());
				try {
					Syspara sysparam = sysparaDao.getSyspara("xzqh", "1", "");
					String csz = sysparam.getCsz();	//本地行政区
					log.info("[jcbk][transMQ]>>>>>>>>地市("+csz+")接收"+info+"订阅信息[Topic] :业务序号为 " + message.getYwxh());
					
					String[] bkfw = message.getJsdw().split(",");
					List<String> bkfwList = Arrays.asList(bkfw);
					
					if(info.equals("布控")){
						SuspinfomonitorQueueProducerManager suspinfomonitorQueueProducerManager = 
							(SuspinfomonitorQueueProducerManager)SpringApplicationContext.getStaticApplicationContext().getBean("suspinfomonitorQueueProducerManager");
						
						if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)&&csz!="430000000000"){
							//保存布控信息
							suspinfoManager.saveSuspinfoLink((TransSusp)message.getObj());
							//保存布控图片
							TransSusp susp = (TransSusp)message.getObj();
							VehSuspinfopic vspic = new VehSuspinfopic();
							vspic.setBkxh(susp.getBkxh());
							vspic.setZjlx(susp.getPicname());
							vspic.setZjwj(susp.getPic());
							//2.1 保存布控附件图片
							saveSuspFj(susp,vspic);
							
							suspinfoManager.saveSuspinfopictrue(vspic);
							List<TransAuditApprove> auditList = ((TransSusp)message.getObj()).getAuditList();
							if(auditList.size()>0){
								//保存审核、审批信息
								for(TransAuditApprove auditApprove :auditList){
									suspinfoAuditApproveManager.saveAuditApprove(auditApprove);
									
									//布控审批回执->送回省中心
									if("2".equals(auditApprove.getBzw())){
										TransSuspmonitor transobj = new TransSuspmonitor();
										transobj.setBkxh(((TransSusp)message.getObj()).getBkxh());
										transobj.setDwdm(csz);
										transobj.setBkjg("已布控");
										
										suspinfomonitorQueueProducerManager.send(transobj);
									}
								}
							}
						} else {
							//本地市 布控审批回执反馈给省厅
							TransSuspmonitor transobj = new TransSuspmonitor();
							transobj.setBkxh(((TransSusp)message.getObj()).getBkxh());
							transobj.setDwdm(csz);
							transobj.setBkjg("已布控");
							
							suspinfomonitorQueueProducerManager.send(transobj);
						}
					}
					if(info.equals("撤控")){
						SuspinfomonitorQueueProducerManager suspinfomonitorQueueProducerManager = 
							(SuspinfomonitorQueueProducerManager)SpringApplicationContext.getStaticApplicationContext().getBean("suspinfomonitorQueueProducerManager");
						
						if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)){
							int returnVal = suspinfoManager.saveCsuspinfoLink((TransSusp)message.getObj());
							if(returnVal>0){
								List<TransAuditApprove> auditList = ((TransSusp)message.getObj()).getAuditList();
								if(auditList.size()>0){
									//保存审核、审批信息
									for(TransAuditApprove auditApprove :auditList){
										suspinfoAuditApproveManager.saveAuditApprove(auditApprove);
										
										//撤控审批回执->送回省中心
										if("2".equals(auditApprove.getBzw())){
											TransSuspmonitor transobj = new TransSuspmonitor();
											transobj.setBkxh(((TransSusp)message.getObj()).getBkxh());
											transobj.setDwdm(csz);
											transobj.setBkjg("已撤控");
											
											suspinfomonitorQueueProducerManager.send(transobj);
										}
									}
								}
							}
						} else {
							//本地市 撤控审批回执反馈给省厅
							TransSuspmonitor transobj = new TransSuspmonitor();
							transobj.setBkxh(((TransSusp)message.getObj()).getBkxh());
							transobj.setDwdm(csz);
							transobj.setBkjg("已撤控");
							
							suspinfomonitorQueueProducerManager.send(transobj);
						}
					}
					if(info.equals("预警签收")){
						
						if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)){
							vehAlarmManager.saveAlarmsureLink((TransAlarm)message.getObj());
							
							VehSuspinfo veh = new VehSuspinfo();
							veh.setBkxh(((TransAlarm)message.getObj()).getBkxh());
							veh.setBjzt("1");
							veh.setBjsj(((TransAlarm)message.getObj()).getBjsj());
							suspinfoManager.updateSuspinfo(veh);
							
						}
					}
					if(info.equals("出警反馈")){
						
						if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)){
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
					}
					if(info.equals("指挥中心签收反馈")){
						
						if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)){
							//保存出警反馈(联动)
							vehAlarmHandleManager.saveAlarmHandleLink((TransAlarmHandled)message.getObj());
							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		//}));
	
	

	

	private void saveSuspFj(TransSusp susp,VehSuspinfopic vspic) {
		//车辆布控申请表
		if(susp.getClbksqblj() != null && !susp.getClbksqblj().equals("")){
			vspic.setBkxh(susp.getBkxh());
			vspic.setClbksqblj(susp.getClbksqblj());
			vspic.setClbksqb(susp.getClbksqb());
		}
		//立案决定书
		if(susp.getLajdslj() != null && !susp.getLajdslj().equals("")){
			vspic.setBkxh(susp.getBkxh());
			vspic.setLajdslj(susp.getLajdslj());
			vspic.setLajds(susp.getLajds());
		}
		//移交承诺书
		if(susp.getYjcnslj() != null && !susp.getYjcnslj().equals("")){
			vspic.setBkxh(susp.getBkxh());
			vspic.setYjcnslj(susp.getYjcnslj());
			vspic.setYjcns(susp.getYjcns());
		}
	}
}
	/*
	public void receiveSuspinfo(final SuspinfoMessage message) {
	//public void receiveSuspinfo(final TransObj message) {
		//System.out.println(Thread.currentThread().getName());
		exec.submit((new Runnable() {
			public void run() {
				//System.out.println(Thread.currentThread().getName());
				System.out.println("**********地市接收 订阅信息Topic : " + message.getSuspinfo().getBkxh());
				try {
					suspinManager.saveSuspinfoLink(message.getSuspinfo());
					List<AuditApprove> auditList = message.getSuspinfo().getAuditList();
					if(auditList.size()>0){
						//保存审核、审批信息
						for(AuditApprove auditApprove :auditList){
							suspinfoAuditApproveManager.saveAuditApprove(auditApprove);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}
	private void saveSuspFj(TransSusp susp,VehSuspinfopic vspic) {
		//车辆布控申请表
		if(susp.getClbksqblj() != null && !susp.getClbksqblj().equals("")){
			vspic.setBkxh(susp.getBkxh());
			vspic.setClbksqblj(susp.getClbksqblj());
			vspic.setClbksqb(susp.getClbksqb());
		}
		//立案决定书
		if(susp.getLajdslj() != null && !susp.getLajdslj().equals("")){
			vspic.setBkxh(susp.getBkxh());
			vspic.setLajdslj(susp.getLajdslj());
			vspic.setLajds(susp.getLajds());
		}
		//移交承诺书
		if(susp.getYjcnslj() != null && !susp.getYjcnslj().equals("")){
			vspic.setBkxh(susp.getBkxh());
			vspic.setYjcnslj(susp.getYjcnslj());
			vspic.setYjcns(susp.getYjcns());
		}
		
	}

*/
	//异步
	/*public void onMessage(Message msg) {
	 
	 	msg.getStringProperty("");
		try {
			msg.acknowledge();
			
			HashMap<String, byte[]> map = (HashMap<String, byte[]>) ((ObjectMessage) msg).getObjectProperty("Map");
			try {
				// POJO must implements Seralizable
				ByteArrayInputStream bis = new ByteArrayInputStream(map.get("POJO"));
				ObjectInputStream ois = new ObjectInputStream(bis);
				SuspinfoMessage returnObject = (SuspinfoMessage)ois.readObject();
				System.out.println(returnObject.getObj().getBkxh());
			}  catch (IOException e) {
				log.error("fromMessage(Message)", e);
			} catch (ClassNotFoundException e) {
				log.error("fromMessage(Message)", e);
			}
			
			//System.out.println("**********地市接收 订阅信息Topic : " + message.getObj().getBkxh());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}*/

//}
