package com.sunshine.monitor.system.activemq.service.receiver;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.bean.TransSuspmonitor;
import com.sunshine.monitor.system.activemq.service.send.SuspinfomonitorQueueProducerManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;


/**
 * 使用多线程接受消息
 */
@Transactional
public class CsuspinfoTopicConsumer  {
	
	private static final Log log = LogFactory.getLog(CsuspinfoTopicConsumer.class);
	
	@Autowired
	private SuspinfoManager suspinManager;
	@Autowired
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;
	@Autowired
	private SysparaDao sysparaDao;
	
	ExecutorService exec = Executors.newFixedThreadPool(5);    
	
	public void receiveCsuspinfo(final TransObj message) {
		exec.submit((new Runnable() {
			public void run() {
				log.info("**********地市接收撤控信息 : " + ((TransSusp)message.getObj()).getBkxh());
				try {
					SuspinfomonitorQueueProducerManager suspinfomonitorQueueProducerManager = 
						(SuspinfomonitorQueueProducerManager)SpringApplicationContext.getStaticApplicationContext().getBean("suspinfomonitorQueueProducerManager");
					Syspara sysparam = sysparaDao.getSyspara("xzqh", "1", "");
					String csz = sysparam.getCsz();	//本地行政区
					String[] bkfw = message.getJsdw().split(",");
					List<String> bkfwList = Arrays.asList(bkfw);
					if(bkfwList.contains(csz) && !message.getCsdw().equals(csz)&&csz!="430000000000"){
						int returnVal = suspinManager.saveCsuspinfoLink((TransSusp)message.getObj());
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}));
	}
	
	/*public void receiveCsuspinfo(final SuspinfoMessage message) {
		exec.submit((new Runnable() {
			public void run() {
				//System.out.println("**********地市接收撤控信息 : " + message.getSuspinfo().getBkxh());
				try {
					suspinManager.saveCsuspinfoLink(message.getSuspinfo());
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
	}*/

	//异步
	/*public void onMessage(Message msg) {
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

}
