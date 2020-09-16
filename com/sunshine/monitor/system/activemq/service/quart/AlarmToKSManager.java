package com.sunshine.monitor.system.activemq.service.quart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.cglib.beans.BeanMap;

import com.sunshine.monitor.comm.maintain.StkkSMSTransmitter;
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.util.PropertyUtil;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;

public class AlarmToKSManager {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	/*private VehAlarmManager vehAlarmManager = 
			(VehAlarmManager)SpringApplicationContext.getStaticApplicationContext().getBean("vehAlarmManager");
	
	private SysparaDao sysparaDao = 
			(SysparaDao)SpringApplicationContext.getStaticApplicationContext().getBean("sysparaDao");
	
	private SuspinfoEditDao suspinfoEditDao = 
			(SuspinfoEditDao)SpringApplicationContext.getStaticApplicationContext().getBean("suspinfoEditDao");
	
	private MaintainDao maintainDaoImpl = 
			(MaintainDao)SpringApplicationContext.getStaticApplicationContext().getBean("maintainDaoImpl");
	
	private SystemDao systemDao = 
			(SystemDao)SpringApplicationContext.getStaticApplicationContext().getBean("systemDao");*/
	
	
	//定时任务调度广东接口（传输预警记录）
	public void SendAlarmToGD() {
		System.out.println();
		
		try {
			logger.debug("进入定时任务调度广东接口（传输预警记录）");
			VehAlarmManager vehAlarmManager = 
					(VehAlarmManager)SpringApplicationContext.getStaticApplicationContext().getBean("vehAlarmManager");
			
			SysparaDao sysparaDao = 
					(SysparaDao)SpringApplicationContext.getStaticApplicationContext().getBean("sysparaDao");
			
			
			Syspara syspara = sysparaDao.getSyspara("xzqh", "1", "");
			String xzqh = syspara.getCsz();
			
			/*List<Map<String,Object>> list = vehAlarmManager.selectToGdKSAlarm(xzqh);
			
			for (final Map<String,Object> map:list){
				
				Map<String,Object> maps = keyToLowerCase(map);
				VehAlarmrec vehAlarmrecBean = new VehAlarmrec();
				
				final VehAlarmrec vehAlarmrec =(VehAlarmrec)mapToBean(maps,vehAlarmrecBean); 
				
				if(vehAlarmrec!=null){
					//启动多线程调用广东接收预警记录接口
					new Thread(){
						@Override
						public void run() {
							runToGdServie(vehAlarmrec);
						}
					}.start();
				}*/
		
			/*List<VehAlarmrec> list = vehAlarmManager.selectToGdKSAlarm(xzqh);
			logger.info("查询出来的记录条数为:"+list.size());
			for ( VehAlarmrec vehAlarmrec:list){
				runToGdServie(vehAlarmrec);
			}*/
			
			
			List<VehAlarmrec> list = vehAlarmManager.selectToGdKSAlarm(xzqh);
			logger.debug("轮询预警短信条数为："+list.size());
			for (final VehAlarmrec vehAlarmrec:list){
				
				if(vehAlarmrec!=null){
					//启动多线程调用广东接收预警记录接口
					new Thread(){
						@Override
						public void run() {
							runToGdServie(vehAlarmrec);
						}
					}.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void runToGdServie(VehAlarmrec alamrec){
		try {
			logger.info("进入调用广东接口平台");
			PropertyUtil.load("ipport");
			String ipGd=PropertyUtil.get("ipgd").trim();
			String portGd=PropertyUtil.get("portgd").trim();
			
			//POST的URL
			String url = "http://" + ipGd + ":" + portGd + "/api/gdservice/vehicle/suspService/saveAlarmrec";
			logger.info("调用湖南接口平台地址为："+url);
			//建立HttpPost对象
			HttpPost httppost = new HttpPost(url);
			//建立一个NameValuePair数组，用于存储欲传送的参数
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			
			JSONArray json1 = JSONArray.fromObject(alamrec); 
			//添加参数
			params.add(new BasicNameValuePair("vehAlamrecBean", json1.toString()));

			HttpResponse responseSend;
			httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			responseSend = new DefaultHttpClient().execute(httppost);
			String sendPostResult = EntityUtils.toString(responseSend.getEntity());
			logger.debug("调用广东接口平台接口返回结果：" + sendPostResult);
//			Map<String, Object> map = (Map<String, Object>)JSON.parse(sendPostResult);
		} catch (Exception e) {
			logger.error("调用广东接口平台接口失败：", e);
		}
		
	}
	
	//注释原因：把短信发送写进接口
	/*//定时任务发送短信（跨省预警记录）
	public void SendSmsKSAlarm() {
		try {
			VehAlarmManager vehAlarmManager = 
					(VehAlarmManager)SpringApplicationContext.getStaticApplicationContext().getBean("vehAlarmManager");
			
			SysparaDao sysparaDao = 
					(SysparaDao)SpringApplicationContext.getStaticApplicationContext().getBean("sysparaDao");
			
			Syspara syspara = sysparaDao.getSyspara("xzqh", "1", "");
			String xzqh = syspara.getCsz();
			
			List<Map<String,Object>> list = vehAlarmManager.selectToHnKSAlarm(xzqh);
			for (final Map<String,Object> map:list){
				
				Map<String,Object> maps = keyToLowerCase(map);
				VehAlarmrec vehAlarmrecBean = new VehAlarmrec();
				
				final VehAlarmrec vehAlarmrec =(VehAlarmrec)mapToBean(maps,vehAlarmrecBean); 
				//启动多线程发送短信提醒
				new Thread(){
					@Override
					public void run() {
						sendSmsByKsAlamrec(vehAlarmrec);
					}
				}.start();
			}
			
			List<VehAlarmrec> list = vehAlarmManager.selectToHnKSAlarm(xzqh);
			logger.info("查询传过来的预警数为："+list.size());
			for (final VehAlarmrec vehAlarmrec:list){
				if(vehAlarmrec!=null){
					//启动多线程发送短信提醒
					new Thread(){
						@Override
						public void run() {
							sendSmsByKsAlamrec(vehAlarmrec);
						}
					}.start();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询传过来的预警接口失败：", e);
		}
	}
	
	public void sendSmsByKsAlamrec(VehAlarmrec alamrec){
		logger.info("进入发短信方法");
		try{
			VehAlarmManager vehAlarmManager = 
					(VehAlarmManager)SpringApplicationContext.getStaticApplicationContext().getBean("vehAlarmManager");
			
			SysparaDao sysparaDao = 
					(SysparaDao)SpringApplicationContext.getStaticApplicationContext().getBean("sysparaDao");
			
			SuspinfoEditDao suspinfoEditDao = 
					(SuspinfoEditDao)SpringApplicationContext.getStaticApplicationContext().getBean("suspinfoEditDao");
			
			MaintainDao maintainDaoImpl = 
					(MaintainDao)SpringApplicationContext.getStaticApplicationContext().getBean("maintainDaoImpl");
			
			SystemDao systemDao = 
					(SystemDao)SpringApplicationContext.getStaticApplicationContext().getBean("systemDao");
			
			VehSuspinfo vehSuspinfo = suspinfoEditDao.getSuspinfoDetailForBkxh(alamrec.getBkxh());
			
			StkkSMSTransmitter smster = new StkkSMSTransmitter();
			//短信立刻发送 ---1
			String content = "【跨省预警信息】车辆号牌号码：" + vehSuspinfo.getHphm()+"、号牌种类："+systemDao.getCodeValue("030107",vehSuspinfo.getHpzl())+"、布控大类："+systemDao.getCodeValue("120019",vehSuspinfo.getBkdl())+"、布控小类："+systemDao.getCodeValue("120005",vehSuspinfo.getBklb())+",在"+alamrec.getKdmc()+"、"+alamrec.getFxmc()+"过车时间为："+alamrec.getGcsj();
			Map<String, String> dxcondition  = new HashMap<String,String>();
			dxcondition.put("xh", vehSuspinfo.getBkxh());
			dxcondition.put("ywlx","15");
			dxcondition.put("dxjsr",vehSuspinfo.getUser()==null?"":vehSuspinfo.getUser() );
			dxcondition.put("dxjshm",vehSuspinfo.getLxdh()==null?"":vehSuspinfo.getLxdh());
			dxcondition.put("dxnr", content);
			dxcondition.put("fsfs", "1");
			dxcondition.put("reason", "");
			logger.info("接收短信人的手机号码为："+vehSuspinfo.getDxjshm());
			if(!StringUtils.isBlank(vehSuspinfo.getLxdh())){
				dxcondition.put("zt", "1");
				dxcondition.put("sysdate", "sysdate");
				smster.send(vehSuspinfo.getDxjshm(), content);
			}
			else
			{
				dxcondition.put("zt", "2");
				dxcondition.put("sysdate", "''");
				dxcondition.put("reason", "手机号码为空，短信自动作废");
			}
			String value="id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,reason";
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
				+"',"
				+dxcondition.get("sysdate")
				+",'"
				+dxcondition.get("zt")
				+"','"
				+dxcondition.get("fsfs")
				+"','"
				+dxcondition.get("reason")
				+"'"
				;
			
				maintainDaoImpl.saveDxsj(value,values);
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("发短信方法调用失败：", e);
		}
	}
	
	public static <T> T mapToBean(Map<String, Object> map, Class<T> obj) throws Exception {  
	    if (map == null) {  
	        return null;  
	    }  
	    Set<Entry<String, Object>> sets = map.entrySet();  
	    T t = obj.newInstance();  
	    Method[] methods = obj.getDeclaredMethods();  
	    for (Entry<String, Object> entry : sets) {  
	        String str = entry.getKey();  
	        String setMethod = "set" + str.substring(0, 1).toUpperCase() + str.substring(1);  
	        for (Method method : methods) {  
	            if (method.getName().equals(setMethod)) {  
	                method.invoke(t, entry.getValue());  
	            }  
	        }  
	    }  
	    return t;  
	}
	
	public static <T> T mapToBean(Map<String, Object> map,T bean) {  
	    BeanMap beanMap = BeanMap.create(bean);  
	    beanMap.putAll(map);  
	    return bean;  
	}
	
	public Map<String,Object> keyToLowerCase(Map<String,Object> map) throws Exception {  
	    if (map == null) {  
	        return null;  
	    } 
	    Map<String,Object> lowerCaseMap = new HashMap<String,Object>();
	    Set<Entry<String, Object>> sets = map.entrySet();  
	    for (Entry<String, Object> entry : sets) {  
	        String str = entry.getKey();
	        lowerCaseMap.put(str.toLowerCase(), entry.getValue());
	    }  
	    return lowerCaseMap;  
	}*/
}
