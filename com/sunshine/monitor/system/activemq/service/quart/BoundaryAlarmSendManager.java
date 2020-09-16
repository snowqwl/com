package com.sunshine.monitor.system.activemq.service.quart;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.activemq.bean.BoundaryAlarmrecMessage;
import com.sunshine.monitor.system.activemq.bean.TransAlarm;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.dao.TransDao;
import com.sunshine.monitor.system.activemq.service.send.BoundaryAlarmQueueProducerManager;

/**
 * 边界预警信息推送定时执行任务
 * 发送条件:联动布控且边界地市为本次联动的范围
 * 发送时间:预警签收后将数据写入传输表,由后台任务发送到MQ中心
 * 发送内容:只发送预警信息、不发送布控信息及审核审批信息
 * 
 * @author OUYANG
 * 
 */
public class BoundaryAlarmSendManager extends BaseDaoImpl {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void doTask() {
		try {
			TransDao transDao = this.getTransDao();
			/** 获取传输任务 */
			List list = transDao.getTransList("JM_TRANS_ALARM", "28", "50");
			if (list.isEmpty())
				return;
			String csdw = ((TransObj) list.get(0)).getCsdw();
			logger.info("[jcbk][transMQ]地市-" + csdw + "-本次边界预警传输获取到的数量为:"
					+ list.size());
			BoundaryAlarmQueueProducerManager boundaryAlarmQueueManager = this
					.getBoundaryAlarmQueueManager();
			BoundaryAlarmrecMessage boundaryAlarmrecMessage = null;
			for (Object obj : list) {
				TransObj transObj = (TransObj) obj;
				/** 报警序号 */
				String bjxh = transObj.getYwxh();
				TransAlarm transAlarm = transDao.getTransAlarmDetail(bjxh);
				if (transAlarm == null) {
					logger.info("边界预警传输:未找到报警序号为:" + bjxh + "的报警记录,传输失败!");
					boundaryAlarmQueueManager.updateTransStatus(
							"JM_TRANS_ALARM", transObj.getCsxh());
					logger.info("边界预警传输:传输序号为:" + transObj.getCsxh()
							+ "的任务,停止调度!");
					continue;
				}
				/**
				// 布控序号
				String bkxh = transAlarm.getBkxh();
				// 布控、布控审核审批信息 
				TransSusp transSusp = transDao.getTransSuspDetail(bkxh, 11);
				if (transSusp == null) {
					logger.info("边界预警传输:未找到布控序号为:" + bjxh + "的布控记录,传输失败!");
					continue;
				}
				*/
				boundaryAlarmrecMessage = new BoundaryAlarmrecMessage();
				boundaryAlarmrecMessage.setTransAlarm(transAlarm);
				//boundaryAlarmrecMessage.setTransSusp(transSusp);
				transObj.setObj(boundaryAlarmrecMessage);
				/** 推送边界信息 */
				boundaryAlarmQueueManager.sendBoundaryAlarm(transObj);
				/** 更新调度状态 */
				boundaryAlarmQueueManager.updateTransStatus("JM_TRANS_ALARM",
						transObj.getCsxh());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 信息发送对象
	 * 
	 * @return
	 */
	private BoundaryAlarmQueueProducerManager getBoundaryAlarmQueueManager() {
		BoundaryAlarmQueueProducerManager boundaryAlarmQueueManager = (BoundaryAlarmQueueProducerManager) SpringApplicationContext
				.getStaticApplicationContext().getBean(
						"boundaryAlarmQueueProducerManager");
		return boundaryAlarmQueueManager;
	}

	/**
	 * 传输操作对象
	 * 
	 * @return
	 */
	private TransDao getTransDao() {
		return (TransDao) SpringApplicationContext
				.getStaticApplicationContext().getBean("transDao");
	}
}
