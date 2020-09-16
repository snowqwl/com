package com.sunshine.monitor.system.activemq.service.quart;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dc.trans.send.CSuspSend;
import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.activemq.service.send.CsuspinfoQueueProducerManager;
import com.sunshine.monitor.system.activemq.service.send.SuspinfoTopicProducerManager;

public class CsuspSendManager extends BaseDaoImpl{
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public void doTask() {
		//logger.info("自动撤控传输-启动.");
		//获取自动布控传输信息
		try {
			TransDao transDao = (TransDao)SpringApplicationContext.getStaticApplicationContext().getBean("transDao");
			//1、获取布控传输待发送记录
			List list = transDao.getTransList("jm_trans_susp", "13", "50");
			if ((list != null) && (list.size() > 0)) {
				int size = list.size();
				String csdw = ((TransObj)list.get(0)).getCsdw();  //撤控传输机构(地市or省厅)
				if(csdw.equals("430000000000")) {
					//改为统一由Destination suspinfoTopicDestination 来发送
					SuspinfoTopicProducerManager suspinfoTopicProducerManager = 
						(SuspinfoTopicProducerManager)SpringApplicationContext.getStaticApplicationContext().getBean("suspinfoTopicProducerManager");
					logger.info("[jcbk][transMQ]省厅撤控传输（发送），本次撤控传输取到的数量为" + size);
					for (Iterator it = list.iterator(); it.hasNext();) {
						try {
							TransObj transobj = (TransObj) it.next();
							TransSusp susp = transDao.getTransSuspDetail(transobj.getYwxh(), 13);
							transobj.setObj(susp);
							//2、发送消息
							//csuspinfoTopicProducerManager.sendTopicMessage(transobj);
							suspinfoTopicProducerManager.sendTopicMessage(transobj);
							//3、更新调度状态
							//csuspinfoTopicProducerManager.updateTransStatus("jm_trans_susp", transobj.getCsxh());
							suspinfoTopicProducerManager.updateTransStatus("jm_trans_susp", transobj.getCsxh());
							//transDao.updateTransStatus("jm_trans_susp", transobj.getCsxh());
						}catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}else {
					CsuspinfoQueueProducerManager csuspinfoQueueProducerManager = 
						(CsuspinfoQueueProducerManager)SpringApplicationContext.getStaticApplicationContext().getBean("csuspinfoQueueProducerManager");
					logger.info("[jcbk][transMQ]地市"+csdw+"撤控传输（发送），本次撤控传输取到的数量为" + size);
					for (Iterator it = list.iterator(); it.hasNext();) {
						try {
							TransObj transobj = (TransObj) it.next();
							TransSusp susp = transDao.getTransSuspDetail(transobj.getYwxh(), 13);
							transobj.setObj(susp);
							//2、发送消息
							csuspinfoQueueProducerManager.send(transobj);
							/**
							 * 撤控信息传输到dc
							 */
							try{
								new CSuspSend().doSend();
							}catch(Exception e){
								e.printStackTrace();
							}
							//3、更新调度状态
							csuspinfoQueueProducerManager.updateTransStatus("jm_trans_susp", transobj.getCsxh());
							//transDao.updateTransStatus("jm_trans_susp", transobj.getCsxh());
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else if (list == null) {
				//logger.info("撤控传输（发送），本次撤控传输取到的List为Null");
			} else {
				//logger.info("撤控传输（发送），本次撤控传输取到的数量为0");
			}
			//logger.info("自动撤控传输-结束.");
		}catch (Exception e) {
				e.printStackTrace();
		}
		
	}
	

}
