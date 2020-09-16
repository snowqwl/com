package com.ht.dc.trans.manager.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ht.dc.trans.bean.TransAlarm;
import com.ht.dc.trans.bean.TransAlarmHandled;
import com.ht.dc.trans.bean.TransAuditApprove;
import com.ht.dc.trans.bean.TransCmd;
import com.ht.dc.trans.bean.TransLivetrace;
import com.ht.dc.trans.bean.TransObj;
import com.ht.dc.trans.bean.TransSusp;
import com.ht.dc.trans.dao.TransDcDao;
import com.ht.dc.trans.manager.TransDcManager;
import com.sunshine.monitor.comm.bean.Result;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.AlarmDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;

@Service("transDcManager")
public class TransDcManagerImpl implements TransDcManager {
	
	@Autowired
	@Qualifier("transDcDao")
	private TransDcDao transDcDao;
	
	@Autowired
	private SuspinfoDao suspinfoDao;
	
	@Autowired
	private AlarmDao alarmDao;
	
	private Logger logger = LoggerFactory.getLogger(TransDcManagerImpl.class);
     
	public Result doReceive(TransObj obj) throws Exception {
		   Result result = new Result();
		   int info = 0;
		   if("11".equals(obj.getType())){
			   // 布控传输
			   TransSusp susp = (TransSusp) obj.getObj();
               info = this.saveSuspinfo(susp);
		   }else if("13".equals(obj.getType())){
			   // 撤控传输
			   info = this.updateSuspInfoForCancel((TransSusp)obj.getObj());
		   }else if("21".equals(obj.getType())){
			   // 预警传输
			   info = this.saveAlarmLink((TransAlarm)obj.getObj());
		   }else if("22".equals(obj.getType())){
			   // 预警签收确认传输
			   info = this.saveAlarmsureLink((TransAlarm)obj.getObj());
		   }else if("25".equals(obj.getType())){
			   // 处警反馈传输
			   TransAlarmHandled transAlarmHandled = (TransAlarmHandled) obj.getObj();
			   info = this.saveAlarmHandled(transAlarmHandled);
		   }
		   
		   if(info>=0){
			   result.setJg(1);
			   result.setInfo("插入成功");
		   }else{
			   result.setJg(0);
			   result.setInfo("插入失败");
		   }
		   return result;
	}
	
	/**
	 * 保存布控信息
	 * @param susp
	 * @return
	 * @throws Exception
	 */
	public int saveSuspinfo(TransSusp susp) throws Exception{
		int result = this.transDcDao.saveSuspInfo(susp);
		List<TransAuditApprove> auditList = susp.getAuditList();
		if(auditList.size()>0){
			//保存审核、审批信息
			for(TransAuditApprove auditApprove :auditList){
				this.transDcDao.saveSuspinfoApprove(auditApprove);
			}
		}
		return result;
	}

	
	/**
	 * 保存撤控信息
	 * @param susp
	 * @return
	 * @throws Exception
	 */
	public int updateSuspInfoForCancel(TransSusp susp) throws Exception{
		int result = 0;
		try {
			result = this.transDcDao.updateSuspInfoForCancel(susp);
			
			List<TransAuditApprove> auditList = susp.getAuditList();
			if(auditList.size()>0){
				//保存审核、审批信息
				for(TransAuditApprove auditApprove :auditList){
					this.transDcDao.insertAuditApprove(auditApprove);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 保存告警信息(联动) 支持事务
	 */
	public int saveAlarmLink(TransAlarm bean) throws Exception {
		VehSuspinfo suspinfo = this.suspinfoDao.getSuspinfoDetail(bean.getBkxh());
		int result = 0;
		if (suspinfo != null) {
			// 更新布控表的报警状态为1
			suspinfo.setBjzt("1");
			// 更新布控信息
			this.suspinfoDao.updateSuspinfo(suspinfo);
			// 保存预警信息
			bean.setXxly("2"); // 改为联动
			result = this.transDcDao.saveAlarmLink(bean);
		}
		return result;
	}
   
	/**
	 * 预警签收确认
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveAlarmsureLink(TransAlarm bean) throws Exception{
		int result = 0;
		bean.setXxly("2");
		result = transDcDao.saveAlarmLink(bean);
		
		VehSuspinfo veh = new VehSuspinfo();
		veh.setBkxh(bean.getBkxh());
		veh.setBjzt("1");
		veh.setBjsj(bean.getBjsj());
		suspinfoDao.updateSuspinfo(veh);
		return result;
	}
	
	public int saveAlarmHandled(TransAlarmHandled bean) throws Exception{
		//保存出警反馈(联动)
		bean.setXxly("2");
        transDcDao.saveAlarmHandleLink(bean);
     	//保存指令记录集
     	List<TransCmd> alarmCmdList = bean.getCmdList();
     	for(TransCmd alarmCmd : alarmCmdList){
     		alarmCmd.setXxly("2");
     		transDcDao.saveCmd(alarmCmd);
     	}
    	//保存出警跟踪记录集
    	List<TransLivetrace> alarmTraceList = bean.getLivetraceList();
    	for(TransLivetrace alarmTrace : alarmTraceList){
    		alarmTrace.setXxly("2");
    		transDcDao.saveLiveTrace(alarmTrace);
    	}
    	//更新报警记录状态(是否下达指令、是否反馈、是否拦截)
    	String bjxh = bean.getBjxh();
    	VehAlarmrec alermRec = alarmDao.getVehAlarmrec(bjxh);
    	try{
    		alermRec.setSffk("1");		//是否已出警反馈
    		alermRec.setSfxdzl("1");	//是否已下达指令
    		alermRec.setSflj(bean.getSflj());  //是否拦截到
    		return alarmDao.saveAlarmSign(alermRec);
    	}catch(NullPointerException ex){
    		logger.info("无报警信息，保存出警反馈失败！");
    		ex.printStackTrace();
    	}
		return -1;
	}
}
