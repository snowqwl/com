package com.sunshine.monitor.comm.quart;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sunshine.monitor.comm.quart.bean.JobEntity;
import com.sunshine.monitor.comm.quart.dao.ScheduleJobDao;

/**
 * 
 * @author OUYANG 2013/6/21
 * 
 */
public class JobData implements Job {

	private Log log = LogFactory.getLog(JobData.class);

	public static Map<String, String> kit = new ConcurrentHashMap<String, String>();

	/**
	 * If you was will put a key of "attribute" into JobDataMap, then the
	 * appropriate job method of setAttribute is called by auto
	 */
	public static final String JOBDAO = "jobDao";

	public static final String SCHUDULERDAO = "schedulerDao";

	private ScheduleJobDao jobDao;

	private AbstractScheduler schedulerDao;

	public String getKit(String rwbh) {
		if (kit.containsKey(rwbh)) {
			return kit.get(rwbh).toString();
		}
		return "";
	}

	public String removeKit(String rwbh) {
		if (kit.containsKey(rwbh)) {
			return kit.remove(rwbh);
		}
		return "";
	}

	public void setKit(String rwbh, String key) {
		kit.put(rwbh, key);
	}

	public void setJobDao(ScheduleJobDao jobDao) {
		this.jobDao = jobDao;
	}

	public void setSchedulerDao(AbstractScheduler schedulerDao) {
		this.schedulerDao = schedulerDao;
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String zxsj = "";
		JobEntity job = null;
		// 是否存在返回值
		boolean isExistReturn = false;
		// 返回值
		String returnValue = "";
		String sxlStr = null;
		//String ccgcStr = null;
		String sxlff = null;
		String rwbh = context.getJobDetail().getKey().getName().substring(4);
		if (jobDao == null) {
			log.info("[Scheduler]Trask Dao Object(JobDao) pass failure!");
			return;
		}
		try {
			zxsj = jobDao.getTime();
		} catch (Exception e) {
			removeKit(rwbh);
			log.error("[Scheduler]ZXSJ(New Time) get failure:" + e.getMessage()
					+ "\".");
			return;
		}
		JobEntity jobEntity = null;
		try {
			if (String.valueOf(kit.get(rwbh)).equals("2")) {
				log.info("[Scheduler]Task of \"" + rwbh + "\" was running.");
				jobDao.saveLog(rwbh, zxsj, "2", "0", "任务正在运行");
				return;
			}
			if (!kit.containsKey(rwbh))
				setKit(rwbh, "1");
			job = jobDao.getJobEntityByRwbh(rwbh);
			if (job == null) {
				log.info("[Scheduler]Job Object(JobEntiry) of " + rwbh
						+ " is null!");
				return;
			}
			sxlStr = job.getSxl();
			//ccgcStr = job.getCcgc();
			sxlff = job.getSxlff();
			if ((sxlStr != null) && (sxlStr.length() > 1)) {
				kit.put(rwbh, JobStatus.RUNING_STATU.getCode());
				jobDao.updateJobDate(rwbh);
				Class<?> clazz = Class.forName(sxlStr); // ClassNotFoundException
				Object obj = clazz.newInstance();
				if ((sxlff != null) && (sxlff.length() > 1)) {
					if (sxlff.indexOf(" return") != -1) {
						job.setSxlff(sxlff.substring(0, sxlff.indexOf(" ")));
						isExistReturn = true;
					}
					Method meth = clazz.getMethod(job.getSxlff()); // NoSuchMethodException
					try {
						if (isExistReturn) {
							returnValue = (String) meth.invoke(obj);
						} else {
							meth.invoke(obj);
						}
						/** 调用目标方法抛出异常处理 */
					}catch(InvocationTargetException e4){
						removeKit(rwbh);
						e4.printStackTrace();
					}catch(IllegalArgumentException e5){
						removeKit(rwbh);
						e5.printStackTrace();
					} catch (Exception e6) {
						removeKit(rwbh);
						e6.printStackTrace();
					} catch (Throwable e7){
						removeKit(rwbh);
						e7.printStackTrace();
					} finally{
						removeKit(rwbh);
					}
				}
				jobEntity = new JobEntity();
				jobEntity.setCwnr("");
				jobEntity.setRwbh(rwbh);
				jobEntity.setRwzt(JobStatus.NORMAL_STATU.getCode());
				jobDao.updateJob(jobEntity);
				kit.put(rwbh, JobStatus.NORMAL_STATU.getCode());
				// 保存成功日志
				jobDao.saveLog(rwbh, zxsj, "2", "1", returnValue);
			} else {
				log.info("[Scheduler]Job of " + rwbh + " is not do nothing");
			}
		} catch (ClassNotFoundException e1) {
			log.info("[Scheduler]任务：" + rwbh + "异常:找不到执行类!" + e1.getMessage());
			removeKit(rwbh);
			try {
				saveLog(rwbh,zxsj,"2", "0","异常:找不到执行类!"+e1.getMessage());
				// 删除任务
				schedulerDao.deleteJobFromSchedule(job);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e2) {
			log.info("[Scheduler]任务：" + rwbh + "异常:找不到执行类方法!" + e2.getMessage());
			removeKit(rwbh);
			try {
				saveLog(rwbh,zxsj,"2", "0","异常:找不到执行类方法!"+e2.getMessage());
				// 删除任务
				schedulerDao.deleteJobFromSchedule(job);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e3) {
			log.info("[Scheduler]任务：" + rwbh + "其它异常原因!" + e3.getMessage());
			removeKit(rwbh);
			try {
				saveLog(rwbh,zxsj,"2", "0","其它异常原因!"+e3.getMessage());
				// 删除任务
				//this.schedulerDao.deleteJobFromSchedule(job);
			} catch (Exception e) {
				removeKit(rwbh);
				e.printStackTrace();
			}
		} catch(Throwable e4){
			log.info("[Scheduler]任务：" + rwbh + "错误原因!" + e4.getMessage());
			removeKit(rwbh);
			try {
				saveLog(rwbh,zxsj,"2", "0","错误原因!"+e4.getMessage());
				// 删除任务
				//this.schedulerDao.deleteJobFromSchedule(job);
			} catch (Exception e) {
				removeKit(rwbh);
				e.printStackTrace();
			}
		} finally{
			removeKit(rwbh);
		}
	}
	
	public void saveLog(String rwbh, String zxsj, String czlx, String czjg,
			String emsg) throws Exception {
		JobEntity jobEntity = new JobEntity();
		jobEntity.setCwnr(emsg);
		jobEntity.setRwbh(rwbh);
		jobEntity.setRwzt(JobStatus.STOP_STATU.getCode());
		jobDao.updateJob(jobEntity);
		// 保存失败日志
		jobDao.saveLog(rwbh, zxsj, czlx, czjg, emsg);
	}
}
