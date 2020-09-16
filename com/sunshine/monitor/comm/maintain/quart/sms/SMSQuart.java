package com.sunshine.monitor.comm.maintain.quart.sms;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sunshine.monitor.comm.bean.MaintainHandle;
import com.sunshine.monitor.comm.bean.Messager;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.maintain.quart.sms.service.SMSLogManager;
import com.sunshine.monitor.comm.maintain.service.MaintainManager;
import com.sunshine.monitor.comm.service.MessagerManager;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.gate.service.GateManager;

/**
 * 
 * @author OUYANG
 *
 */
public class SMSQuart {
	
	/*发送号码*/
	private static final String FSHM ="106573405110750";
	
	/*发送状态*/
	private static final String FSZT = "1";
	
	/*问题描述*/
	private static final String WTMS = "卡口超过24小时未上传数据" ;
	
	/**
	 * 做任务
	 */
	public void doTask() throws Exception{
		MessagerManager messagerManager = (MessagerManager)SpringApplicationContext.getStaticApplicationContext().getBean("messagerManager");
		GateManager gateManager = (GateManager)SpringApplicationContext.getStaticApplicationContext().getBean("gateManager");
		SMSLogManager smsLogManager = (SMSLogManager)SpringApplicationContext.getStaticApplicationContext().getBean("smsLogManager");
		MaintainManager maintainManager = (MaintainManager)SpringApplicationContext.getStaticApplicationContext().getBean("maintainManager");
		MaintainDao   maintainDao=(MaintainDao)SpringApplicationContext.getStaticApplicationContext().getBean("maintainDao");
		/***卡口超过24小时未上传数据**/
		List<Messager> msglist = messagerManager.getMessagerInfo();
		SmsFacade smsFacade = new SmsFacade();
		for(Messager msg : msglist){
			String kdbh = msg.getKdbh();
			String fxbh = msg.getFxbh();
			/*卡点编号不能为空*/
			if("".equals(kdbh) || kdbh == null){
				continue;
			}
			CodeGate cgate = gateManager.getGate(kdbh);
			if(cgate == null){
				continue;
			}
			String kdmc = cgate.getKdmc();
			CodeGateExtend exend = gateManager.getDirect(fxbh);
			if(exend != null){
				//写运维表
				MaintainHandle handle = new MaintainHandle();
				handle.setGzlx("3");
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(msg.getGcsj() != null)
					handle.setGzsj(df.format(msg.getGcsj()));
				handle.setGzqk("【"+kdmc+"--"+exend.getFxmc()+"方向】"+WTMS);
				handle.setSbr("系统");
				handle.setSbrmc("系统");
				String jshm = exend.getYwsjhm();
				handle.setYwdh(jshm);
				handle.setKdbh(kdbh);
				handle.setFxbh(fxbh);
				String seqXh=maintainManager.saveMaintainHandle(handle);
				
				
				Map<String, String> dxcondition = new HashMap<String, String>();
				dxcondition.put("xh", seqXh);
				dxcondition.put("ywlx", "51");//卡口超过24小时未上传数据
				dxcondition.put("dxjsr", exend.getYwzrr()==null?"''":exend.getYwzrr());
				dxcondition.put("dxjshm",jshm);
				dxcondition.put("dxnr", "【"+kdmc+"--"+exend.getFxmc()+"方向】"+WTMS);
				dxcondition.put("fsfs", "1");
				dxcondition.put("reason", "''");
				/*手机号码为空不发送*/
				if(StringUtils.isNotBlank(jshm)){
					dxcondition.put("zt", "1");
					dxcondition.put("sysdate", "sysdate");
					/*发送*/
					boolean flag = smsFacade.sendAndPostSms(jshm, "【"+kdmc+"--"+exend.getFxmc()+"方向】"+WTMS, null);
					if(!flag){
						dxcondition.put("zt", "2");
						dxcondition.put("sysdate", "''");
						dxcondition.put("reason", "发送短信失败");
					}
				}else{
					dxcondition.put("zt", "2");
					dxcondition.put("sysdate", "''");
					dxcondition.put("reason", "接收人手机号码为空，短信自动作废");
				}
				
				//在卡口信息维护表中，设置卡口状态
				//maintainManager.setCodeGatestate(kdbh, fxbh,"4","1");
				
				String value = "id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,reason";
				String values = "" + "SEQ_FRM_COMMUNICATION.nextval,'"
						+ dxcondition.get("xh") + "','" + dxcondition.get("ywlx")
						+ "','" + dxcondition.get("dxjsr") + "','"
						+ dxcondition.get("dxjshm") + "','"
						+ dxcondition.get("dxnr") + "',"
						+ dxcondition.get("sysdate") + ",'" 
						+ dxcondition.get("zt")
						+ "','" + dxcondition.get("fsfs") + "','"
						+ dxcondition.get("reason") + "'";
				maintainManager.saveDxsj(value, values);
				
			}
			else
				System.out.println("卡口扩展表没有对应的记录");
			
		}
		 //需要更新有数据上传上传的故障卡口
		/**
		List<CodeGateExtend> list = maintainManager.getGzCodeGateInfo();
		if (list.size() > 0) {
			for (CodeGateExtend codeGate : maintainManager.getGzCodeGateInfo()) {
				String kdbh = codeGate.getKdbh();
				String fxbh = codeGate.getFxbh();
				//卡点编号不能为空
				if ("".equals(kdbh) || kdbh == null) {
					continue;
				}
				maintainManager.setCodeGatestate(codeGate.getKdbh(),codeGate.getFxbh(),"1","0");
			}
		}
      */
		
		
		/***号牌识别率环比超30%**/
		List  hblist = messagerManager.getStatHbInfo();
		for(int i=0; i<hblist.size();i++){
			Map map = (Map)hblist.get(i);
			
			String kdbh = map.get("KDBH")==null?"":map.get("KDBH").toString();
			String gzms = map.get("GZMS")==null?"":map.get("GZMS").toString();
			String tjrq = map.get("TJRQ")==null?"":map.get("TJRQ").toString();
			String fxbh = map.get("FXBH")==null?"":map.get("FXBH").toString();
			if(StringUtils.isBlank(gzms)){
				continue;
			}
			/*卡点编号不能为空*/
			if("".equals(kdbh) || kdbh == null){
				continue;
			}
			
			CodeGate cgate = gateManager.getGate(kdbh);
			if(cgate == null){
				continue;
			}
			String kdmc = cgate.getKdmc();
			CodeGateExtend exend = gateManager.getDirect(fxbh);
			
			if(exend != null){
				//写运维表
				MaintainHandle handle = new MaintainHandle();
				handle.setGzlx("4");
				handle.setGzsj(tjrq+" 00:00:00");
				handle.setGzqk("【"+kdmc+"--"+exend.getFxmc()+"方向】"+gzms);
				handle.setSbr("系统");
				handle.setSbrmc("系统");
				String seqXh=maintainManager.saveMaintainHandle(handle);
				
				String jshm = exend.getYwsjhm();
				Map<String, String> dxcondition = new HashMap<String, String>();
				dxcondition.put("xh", seqXh);
				dxcondition.put("ywlx", "52");//号牌识别率环比超30%
				dxcondition.put("dxjsr", exend.getYwzrr()==null?"''":exend.getYwzrr());
				dxcondition.put("dxjshm",jshm);
				dxcondition.put("dxnr", "【"+kdmc+"--"+exend.getFxmc()+"方向】"+gzms);
				dxcondition.put("fsfs", "1");
				dxcondition.put("reason", "''");
				/*手机号码为空不发送*/
				if(StringUtils.isNotBlank(jshm)){
					dxcondition.put("zt", "1");
					dxcondition.put("sysdate", "sysdate");
					/*发送*/
					boolean flag=smsFacade.sendAndPostSms(jshm, "【"+kdmc+"--"+exend.getFxmc()+"方向】"+gzms, null);
					if(!flag){
						dxcondition.put("zt", "2");
						dxcondition.put("sysdate", "''");
						dxcondition.put("reason", "发送短信失败");
					}
					}
				else{
					dxcondition.put("zt", "2");
					dxcondition.put("sysdate", "''");
					dxcondition.put("reason", "接收人手机号码为空，短信自动作废");
					}
				
				String value = "id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,reason";
				String values = "" + "SEQ_FRM_COMMUNICATION.nextval,'"
						+ dxcondition.get("xh") + "','" + dxcondition.get("ywlx")
						+ "','" + dxcondition.get("dxjsr") + "','"
						+ dxcondition.get("dxjshm") + "','"
						+ dxcondition.get("dxnr") + "',"
						+ dxcondition.get("sysdate") + ",'" 
						+ dxcondition.get("zt")
						+ "','" + dxcondition.get("fsfs") + "','"
						+ dxcondition.get("reason") + "'";
				maintainManager.saveDxsj(value, values);
			}
			else
				System.out.println("卡口扩展表没对应的记录");
			
			
		}
		
	}
}
