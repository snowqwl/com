package com.sunshine.monitor.system.activemq.service.quart;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.activemq.service.send.SuspinfoTopicProducerManager;

public class SuspSendManager extends BaseDaoImpl{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass()); 
	
	public void doTask() {
		//获取自动布控传输信息
		try {
			TransDao transDao = (TransDao)SpringApplicationContext.getStaticApplicationContext().getBean("transDao");
			SuspinfoTopicProducerManager suspinfoTopicProducerManager = 
					(SuspinfoTopicProducerManager)SpringApplicationContext.getStaticApplicationContext().getBean("suspinfoTopicProducerManager");
			//1、获取布控传输待发送记录
			List list = transDao.getTransList("jm_trans_susp", "11", "50");
			if ((list != null) && (list.size() > 0)) {
				int size = list.size();
				logger.info("[jcbk]省厅布控传输（发送），本次布控传输取到的数量为" + size);
				for (Iterator it = list.iterator(); it.hasNext();) {
					try {
						TransObj transobj = (TransObj) it.next();
						TransSusp susp = transDao.getTransSuspDetail(transobj.getYwxh(), 11);
						transobj.setObj(susp);
						//2、发送消息,通知各地市
						suspinfoTopicProducerManager.sendTopicMessage(transobj);
						//3、更新调度状态
						suspinfoTopicProducerManager.updateTransStatus("jm_trans_susp", transobj.getCsxh());
						//transDao.updateTransStatus("jm_trans_susp", transobj.getCsxh());
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			} 
			/** 处理布控反馈给市州审批未通过或需要修改*/
			suspApproval(transDao, suspinfoTopicProducerManager);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 审批不同意、需修改列表（30）
	 * @param transDao
	 * @throws Exception
	 */
	private void suspApproval(TransDao transDao, SuspinfoTopicProducerManager suspinfoTopicProducer) throws Exception{
		List list = transDao.getTransList("jm_trans_susp", "30", "50");
		if ((list != null) && (list.size() > 0)) {
			for(Iterator it = list.iterator(); it.hasNext();){
				TransObj transobj = (TransObj) it.next();
				// 只取布控审批结果信息
				TransAuditApprove  taa = transDao.getTransSuspApproval(transobj.getYwxh(), "2");
				if(taa == null)
					continue;
				transobj.setObj(taa);
				suspinfoTopicProducer.sendTopicMessage(transobj);
				//3、更新调度状态
				suspinfoTopicProducer.updateTransStatus("jm_trans_susp", transobj.getCsxh());
			}
		}
	}
}