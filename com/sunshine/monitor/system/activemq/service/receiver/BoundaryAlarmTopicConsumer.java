package com.sunshine.monitor.system.activemq.service.receiver;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.activemq.bean.BoundaryAlarmrecMessage;
import com.sunshine.monitor.system.activemq.bean.TransAlarm;
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.activemq.bean.TransObj;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

/**
 * 接收边界预警信息
 * 
 * @author OUYANG
 * 
 */
@Transactional
public class BoundaryAlarmTopicConsumer {

	private static final Log log = LogFactory
			.getLog(BoundaryAlarmTopicConsumer.class);

	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysparaManager;

	@Autowired
	private GateManager gateManager;
	
	@Autowired
	@Qualifier("suspinfoManagerImpl")
	private SuspinfoManager suspinfoManager;
	
	@Autowired
	private VehAlarmManager vehAlarmManager;
	
	@Autowired
	private SuspinfoAuditApproveManager suspinfoAuditApproveManager;

	private ExecutorService exec = Executors.newFixedThreadPool(2);

	public void receiveBoundaryAlarm(final TransObj message) {
		exec.submit(new Runnable() {
			public void run() {
				try {
					BoundaryAlarmrecMessage boundaryAlarmrecMessage = null;
					String csz = getLocalXzqh();
					/** Boundary range */
					String[] bjfw = message.getJsdw().split(",");
					List<String> bjfwList = Arrays.asList(bjfw);
					if (bjfwList.contains(csz)
							&& !message.getCsdw().equals(csz)) {
						log.info("[jcbk][transMQ]地市-"+csz+"-接收"+message.getType()+"订阅信息 :业务序号为: " + message.getYwxh());
						boundaryAlarmrecMessage = (BoundaryAlarmrecMessage) message
								.getObj();
						TransAlarm transAlarm = boundaryAlarmrecMessage
								.getTransAlarm();

						/** 边界预警签收,默认为市公安局 */
						String kdbh = transAlarm.getKdbh();
						String fxbh = transAlarm.getFxbh();
						CodeGateExtend codeGateExtend = gateManager.getDirect(fxbh);
						Assert.notNull(codeGateExtend, message.getCsdw()
								+ "的边界卡口:" + kdbh + ",未推送到 " + csz);

						/**
						 * 不传输布控及布控审核审批记录
						 * 是否修改布控记录中"是否报警"?
						 * 
						TransSusp transSup = boundaryAlarmrecMessage
								.getTransSusp();
						// 边界转入的布控信息当做联动布控 
						//transSup.setBkfw(csz);
						//transSup.setBkfwlx("2");
						transSup.setBjzt("1");
						transSup.setBjsj(transAlarm.getBjsj());
						// 保存布控时，布控时间被设为当前系统时间
						suspinfoManager.saveSuspinfoLink(transSup);
						
						// 保存布控图片
						VehSuspinfopic vspic = new VehSuspinfopic();
						vspic.setBkxh(transSup.getBkxh());
						vspic.setZjlx(transSup.getPicname());
						vspic.setZjwj(transSup.getPic());
						suspinfoManager.saveSuspinfopictrue(vspic);
						
						// 保存审核审批信息
						List<TransAuditApprove> auditList = transSup.getAuditList();
						if(auditList!=null&&auditList.size() > 0){
							for(TransAuditApprove auditApprove :auditList){
								suspinfoAuditApproveManager.saveAuditApprove(auditApprove);
							}
						}*/
						
						/**配置签收单位*/
						String jsonZrdw = codeGateExtend.getZrdw();
						String zrdw = Common.getAlarmDwdm(jsonZrdw, transAlarm.getBjdl());
						transAlarm.setBjdwdm(zrdw);
						/**保存预警信息*/
						vehAlarmManager.saveBoundaryAlarm(transAlarm);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Query xzqh
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getLocalXzqh() throws Exception {
		Syspara sysparam = sysparaManager.getSyspara("xzqh", "1", "");
		return sysparam.getCsz();
	}
}
