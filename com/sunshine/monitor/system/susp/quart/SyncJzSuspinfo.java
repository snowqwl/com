package com.sunshine.monitor.system.susp.quart;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.quart.bean.JobEntity;
import com.sunshine.monitor.comm.quart.dao.ScheduleJobDao;
import com.sunshine.monitor.comm.util.BeanUtils;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.comm.util.orm.Constant;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.QueryCondition.Op;
import com.sunshine.monitor.system.jzSuspinfo.bean.JzVehSuspinfo;
import com.sunshine.monitor.system.jzSuspinfo.bean.SyncLog;
import com.sunshine.monitor.system.jzSuspinfo.dao.JzVehSuspinfoDao;
import com.sunshine.monitor.system.jzSuspinfo.dao.SyncLogDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;
import com.sunshine.monitor.system.susp.exception.SuspDaoAccessException;
import com.sunshine.monitor.system.susp.exception.SuspinfoValidateException;
import com.sunshine.monitor.system.susp.service.ExternalSuspinfoManager;

/**
 *  警综同步
 * @author lifenghu
 *
 */
public class SyncJzSuspinfo {
	
	private JzVehSuspinfoDao jzVehSuspinfoDao ;
	
	private ScheduleJobDao scheduleJobDao ;
	
	private ExternalSuspinfoManager externalSuspinfoManager;
	
	private SuspinfoDao suspinfoDao;
	
	private SyncLogDao syncLogDao;
	
	private Logger log = LoggerFactory.getLogger(SyncJzSuspinfo.class);
	
	private SimpleDateFormat format;
	
	public SyncJzSuspinfo(){
		try{
			format = new SimpleDateFormat(Constant.dateFormat);
			jzVehSuspinfoDao = SpringApplicationContext.getBean("jzVehSuspinfoDao", JzVehSuspinfoDao.class);
			scheduleJobDao = SpringApplicationContext.getBean("scheduleJobDao", ScheduleJobDao.class);
			externalSuspinfoManager = SpringApplicationContext.getBean("externalSuspinfoManager", ExternalSuspinfoManager.class);
			suspinfoDao = SpringApplicationContext.getBean("suspinfoDaoImpl", SuspinfoDao.class);
			syncLogDao = SpringApplicationContext.getBean("syncLogDao", SyncLogDao.class);
		}catch(Exception e){
			log.error("警综同步类构造出错！！！");
			throw new RuntimeException(e);
		}
		log.debug("警综同步类构造完成，准备执行");
	}

	public void execute() throws JobExecutionException {
		 SimpleDateFormat startTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		this.log.debug("警综同步类开始执行");
		try {
			JobEntity jobEntity = scheduleJobDao.getJobEntityByRwbh("A0110") ;
			Date startDate = startTimeFormat.parse(jobEntity.getBz()) ;
			Date nowDate = suspinfoDao.getDbNowDate() ;
			Date maxDate = startDate ;
			List<JzVehSuspinfo>  jzSuspList = jzVehSuspinfoDao.getJzVehSuspinfoList(startDate, nowDate);
			log.debug("警综同步类，获取警综同步数据"+jzSuspList.size()+"条");
			if(jzSuspList.size()>0){
				JSONObject jsonObj = JSONObject.fromObject(jzSuspList.get(0));
				log.debug("警综同步类,获取数据模版："+jsonObj.toString());
			}
			SyncLog log = new SyncLog();
			log.setRwbh("brigandage");
			log.setTbsj(nowDate);
			int sucessSusp = 0;
			int sucessCancleSusp = 0;
			for(JzVehSuspinfo susp : jzSuspList){
				log.setYwbh(susp.getYsbh());
				if(susp.getGxsj().compareTo(maxDate) == 1) maxDate = susp.getGxsj();
				try{
					validate(susp);
					VehSuspinfo dbSuspinfo = findSuspInfo(susp.getHphm(), susp.getYsbh());
					if("1".equals(susp.getJlzt())){
						if(dbSuspinfo!=null) throw ValidateException.newInstance("0" , "布控已经存在");
						suspInfo(susp);
						sucessSusp++;
					} else if("2".equals(susp.getJlzt())){
						if(dbSuspinfo == null) throw ValidateException.newInstance("0" , "布控不存在，不能撤控");
						susp.setBkxh(dbSuspinfo.getBkxh());
						suspInfoCancle(susp);
						sucessCancleSusp++;
					} else {
						//报警状态为空或者不存在的状态
						throw ValidateException.newInstance("-1");
					} 
					//日志
					log.setCzjg("1");
					log.setTbnr(susp.getBkxh());
				} catch(ValidateException e){
					this.log.error("警综同步，校验不通过"+e.getMessage()); 
					// 日志
					log.setCzjg("0");
					log.setTbnr(e.getMessage());
				} catch(SuspinfoValidateException e){
					this.log.error("警综同步，业务校验不通过 "+e.getMessage()); 
					// 日志
					log.setCzjg("0");
					log.setTbnr(e.getMessage());
				} catch(Exception e1){
					this.log.error("警综同步，发生未知执行异常 "+e1.getMessage()); 
					//日志
					log.setCzjg("0");
					log.setTbnr(e1.getMessage());
					//syncError 暂时不增加
				}
				//syncLogDao.save(log);
			}
			this.log.debug("警综同步类，布控"+sucessSusp+"条");
			this.log.debug("警综同步类，撤控"+sucessCancleSusp+"条");
			jobEntity.setBz(startTimeFormat.format(maxDate));
			scheduleJobDao.updateJz(jobEntity , "rwbh");
			this.log.debug("警综同步类，布控"+sucessSusp+"条");
		} catch (Exception e) {
			this.log.debug("警综同步类，执行中发生未知异常");
			e.printStackTrace();
		}
		
	}
	
	private void suspInfoCancle(JzVehSuspinfo susp)
			throws IllegalAccessException, InvocationTargetException,
			SuspinfoValidateException, SuspDaoAccessException {
		VehSuspinfo suspinfo = transformSuspinfoCancle(susp);
		AuditApprove auditApprove = transformAuditinfo(susp);
		externalSuspinfoManager.suspinfoCancle(suspinfo, auditApprove);
	}
	
	private void suspInfo(JzVehSuspinfo susp) throws IllegalAccessException,
			InvocationTargetException, SuspinfoValidateException,
			SuspDaoAccessException {
		VehSuspinfo suspinfo = transformSuspinfo(susp);
		AuditApprove auditApprove = transformAuditinfo(susp);
		externalSuspinfoManager.suspinfo(suspinfo,auditApprove);
	}

	private VehSuspinfo findSuspInfo(String hphm, String ysbh) throws Exception {
		Cnd cnd = Cnd.where().and("hphm", Op.EQ, hphm).and("ysbh", Op.EQ , ysbh );
	    return suspinfoDao.findSuspInfo(hphm,ysbh);
	}

	/**
	 * 转换为审批信息对象
	 * @param susp
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private AuditApprove transformAuditinfo(JzVehSuspinfo susp)
			throws IllegalAccessException, InvocationTargetException {
		AuditApprove aa  = new AuditApprove();
		BeanUtils.copyProperties(aa , susp);
		if (aa.getCzrdw() == null || aa.getCzrdw().equals("")) {
			aa.setCzrdw(susp.getCxsqdw());
		}
		aa.setBzw("2");
		aa.setCzjg("1");
		return aa;
	}
	
	/**
	 * 转换为布控信息对象
	 * @param susp
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private VehSuspinfo transformSuspinfo(JzVehSuspinfo susp) 
			throws IllegalAccessException, InvocationTargetException {
		VehSuspinfo suspinfo = new VehSuspinfo();
		BeanUtils.copyProperties(suspinfo , susp);
		suspinfo.setJyaq(StringUtils.substring(susp.getJyaq(), 0, 509)+"...") ;
		suspinfo.setClsbdh(StringUtils.substring(susp.getClsbdh(), 0, 28)+"...");
		suspinfo.setCltz(StringUtils.substring(susp.getCltz(), 0, 509)+"...");
		if (suspinfo.getLadwlxdh() == null || suspinfo.getLadwlxdh().equals("")) { suspinfo.setLadwlxdh("-");}
		suspinfo.setBkdl("1") ;
		suspinfo.setBklb("06");
		suspinfo.setBkfwlx("1");
		suspinfo.setBkjb("2");
		suspinfo.setBkxz("1");
		suspinfo.setSqsb("0");
		suspinfo.setBjya("1");
		suspinfo.setBjfs("0111");
		suspinfo.setDxjshm("");
		suspinfo.setXxly("6");
		suspinfo.setYwzt("14");
		suspinfo.setBjzt("0");
		suspinfo.setMhbkbj("0");
		return suspinfo;
	}
	private VehSuspinfo transformSuspinfoCancle(JzVehSuspinfo susp) {
		VehSuspinfo suspinfo = new VehSuspinfo();
		BeanUtils.copyProperties(suspinfo , susp);
		suspinfo.setCxsqr(susp.getCxsqr());
		suspinfo.setCxsqrjh(susp.getCxsqrjh());
		suspinfo.setCxsqrmc(susp.getCxsqrmc());
		suspinfo.setCxsqdw(susp.getCxsqdw());
		suspinfo.setCxsqdwmc(susp.getCxsqdwmc());
		if(susp.getCxsqsj() != null)
			suspinfo.setCxsqsj(format.format(susp.getCxsqsj()));
		suspinfo.setCkyydm("99");
		suspinfo.setYwzt("99");
		suspinfo.setJlzt("2");
		return suspinfo;
	}
	
	/**
	 * 校验同步数据
	 * @param susp
	 * @throws ValidateException
	 */
	private void validate(JzVehSuspinfo susp) throws ValidateException {
		//校验号牌种类长度,不能为空且必须为2
		if(susp.getHpzl()==null || susp.getHpzl().length() != 2){
			throw ValidateException.newInstance("-1","校验号牌种类长度,不能为空且必须为2("+susp.getHpzl()+")");
		}
		//校验号牌号码，不能为空且必须为6-10长度
		if(susp.getHphm() == null 
				|| susp.getHphm().length() < 6 
				|| susp.getHphm().length() > 10){
			throw ValidateException.newInstance("-1","校验号牌号码，不能为空且必须为6-10长度("+susp.getHphm()+")");
		}
		
	}
	
	private static class ValidateException extends Exception{
		private static final long serialVersionUID = -1085514877986959303L;
		
		private String errorCode;
		
		public static ValidateException newInstance(String code){
			return new ValidateException(code);
		}
		
		public static ValidateException newInstance(String code,String msg){
			return new ValidateException(code,msg);
		}
		
		public ValidateException(String code){
			super();
			this.errorCode = code;
		}
		public ValidateException(String code,String msg){
			super(msg);
			this.errorCode = code;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		
	}
	public static void main(String[] args) {
	}
}
