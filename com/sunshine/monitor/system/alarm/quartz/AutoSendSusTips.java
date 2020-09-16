package com.sunshine.monitor.system.alarm.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.AlarmDao;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.communication.dao.CommunicationDao;
import com.sunshine.monitor.system.sign.dao.BKSignDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

public class AutoSendSusTips {
	public void sendSusTips(){
		System.out.println("定时检测反馈签收完毕，但还没有拦截,对当前值班人短信提示");
		CommunicationDao communicationdao=(CommunicationDao)SpringApplicationContext.getStaticApplicationContext().getBean("CommunicationDao");
		MaintainDao   maintainDao=(MaintainDao)SpringApplicationContext.getStaticApplicationContext().getBean("maintainDao");
		AlarmDao    alarmDao=(AlarmDao)SpringApplicationContext.getStaticApplicationContext().getBean(AlarmDao.class);
		VehAlarmManager    alarmManager=(VehAlarmManager)SpringApplicationContext.getStaticApplicationContext().getBean(VehAlarmManager.class);
		SuspinfoManager    suspinfomanager=(SuspinfoManager)SpringApplicationContext.getStaticApplicationContext().getBean(SuspinfoManager.class);
		BKSignDao bkSignDao=(BKSignDao)SpringApplicationContext.getStaticApplicationContext().getBean("BKSignDao");
		try {
			//查询已经反馈签收但还没拦截的记录
			List<VehAlarmrec> noLjList=alarmDao.getNoLJ();
			//获取值班人信息
			List dutyList = bkSignDao.dutyInfo();
			
			for(int i=0;i<noLjList.size();i++){
			
				VehAlarmrec vehalarmrec=noLjList.get(i);
				//判断该反馈信息是否已经存在
				List flag=communicationdao.getInfoByXh(vehalarmrec.getFxbh()); 
				VehAlarmrec alarm = alarmManager.getAlarmsignDetail(vehalarmrec.getBjxh());
				VehSuspinfo susp = suspinfomanager.getSuspDetail(vehalarmrec.getBkxh());
				String msg = "在【"+alarm.getBjsj()+"】时间发生的预警类型为【"+alarm.getBjlxmc()+"】，号牌号码为【"+alarm.getHphm()+"】和车辆类型为【"+susp.getHpzlmc()+"】，经过【"+alarm.getKdmc()+"】卡口的车辆已反馈签收，请8小时内务必进行拦截";
				
				
				if(flag.size()==0){

					String zbrdh="";
					String zbrmc="";
					Map<String, String> dxcondition  = new HashMap<String,String>();
					
					
					 if(dutyList.size()>0){//判断有否有值班人,如果没值班人就 不发短信
						 Map map=(Map)dutyList.get(0);
							Map dutyMap=(Map)dutyList.get(0);
							zbrmc=dutyMap.get("ZBRMC").toString();
							dxcondition.put("reason","");
						 if(map.get("ZBRDH")!=null){ //判断短信是否有效 （如果号码为空或者不是11位 短信就自动作废 ） zt 0待发送 1已发送 2作废待删除
							 if(!map.get("ZBRDH").toString().equals("")&&map.get("ZBRDH").toString().length()==11){
								 zbrdh=dutyMap.get("ZBRDH").toString();
									dxcondition.put("zt", "0");
							 }
							 else{
								 dxcondition.put("zt", "2");
								 dxcondition.put("reason", "手机号码为空，短信自动作废");
							 }
						 }
						 else
							 dxcondition.put("zt", "2");
						 	 dxcondition.put("reason", "手机号码为空，短信自动作废");
						 	
							dxcondition.put("xh", vehalarmrec.getFkbh()==null?"":vehalarmrec.getFkbh());
							dxcondition.put("ywlx","15");
							dxcondition.put("dxjsr", zbrmc );
							dxcondition.put("dxjshm", zbrdh);
							dxcondition.put("dxnr", msg);
							dxcondition.put("bz", "2");
							dxcondition.put("fsfs", "2");
							String value="id,xh,ywlx,dxjsr,dxjshm,dxnr,zt,bz,fsfs,reason";
							String values=""
								+"SEQ_FRM_COMMUNICATION.nextval,'"
								+dxcondition.get("xh")
								+"','"
								+dxcondition.get("ywlx")
								+"','"
								+dxcondition.get("dxjsr")
								+"','"
								+dxcondition.get("dxjshm")
								+"','"
								+dxcondition.get("dxnr")
								+"','"
								+dxcondition.get("zt")
								+"','"
								+dxcondition.get("bz")
								+"','"
								+dxcondition.get("fsfs")
								+"','"
								+dxcondition.get("reason")
								+"'"
								;
							maintainDao.saveDxsj(value, values);
					 }
				}
			}
			System.out.println("成功发送短信"+communicationdao.sendNoLjTips()+"条");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
}
