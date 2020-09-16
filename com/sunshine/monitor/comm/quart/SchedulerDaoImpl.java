package com.sunshine.monitor.comm.quart;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.quart.bean.JobEntity;
import com.sunshine.monitor.comm.quart.dao.ScheduleJobDao;

/**
 * 
 * @author OUYANG 2013/6/21
 * 
 */
@Transactional
@Component("schedulerDaoImpl")
public class SchedulerDaoImpl implements AbstractScheduler {

	private Logger log = Logger.getLogger(SchedulerDaoImpl.class);

	/**
	 * Job Prefix distinguish from trigger name
	 */
	private static String JOB_PREFIX = "JOB_";

	/**
	 * Trigger Prefix distinguish from Job name
	 */
	private static String TRIGGER_PREFIX = "TRI_";

	/**
	 * Job and Trigger group name is "DEFAULT" by default You could configure as
	 * not same group of job and trigger
	 */
	private static String DEFAULT_GROUP_NAME = "DEFAULT";

	@Autowired
	@Qualifier("quarzScheduler")
	private Scheduler quarzScheduler;

	@Autowired
	@Qualifier("scheduleJobDao")
	private ScheduleJobDao jobDao;

	public void setQuarzScheduler(Scheduler quarzScheduler) throws Exception {

		this.quarzScheduler = quarzScheduler;
	}

	public Scheduler getQuarzScheduler() {
		return quarzScheduler;
	}

	public ScheduleJobDao getJobDao() {
		return jobDao;
	}

	public void initScheduler(boolean flag) throws Exception {
		// 更新任务状态
		jobDao.initJobData(flag);
		List<JobEntity> list = jobDao.getAllJobEntitys(flag);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (addJobtoSchedule(list.get(i))) {
					continue;
				}
			}
		}
	}

	public void shutdownSchedule() throws Exception {
		quarzScheduler.shutdown(true);
		log.debug("调度器已关闭!");
	}

	public boolean checkJobIsExist(JobKey jobKey) throws Exception {

		return quarzScheduler.checkExists(jobKey);
	}

	public boolean deleteJobFromSchedule(JobEntity jobEntity) throws Exception {
		JobKey jobKey = new JobKey(JOB_PREFIX + jobEntity.getRwbh(),
				DEFAULT_GROUP_NAME);
		boolean d_flag = true;
		if (checkJobIsExist(jobKey)) {
			quarzScheduler.pauseJob(jobKey);
			d_flag = quarzScheduler.deleteJob(jobKey);
			log.debug("任务:" + jobEntity.getRwbh() + " 从调度器中删除!");
		}
		return d_flag;
	}

	public boolean updateJobFromSchedule(JobEntity jobEntity) throws Exception {
		deleteJobFromSchedule(jobEntity);
		String[] strs = getJobAndTriggerStr(jobEntity.getRwbh());
		JobDetail job = newJob(JobData.class).withIdentity(strs[0],
				DEFAULT_GROUP_NAME).build();
		CronTrigger trigger = newTrigger().withIdentity(strs[1],
				DEFAULT_GROUP_NAME).withSchedule(
				cronSchedule(jobEntity.getRwzq())).build();
		putData(job);
		quarzScheduler.scheduleJob(job, trigger);
		log.debug("任务:" + jobEntity.getRwbh() + " 更新成功!");
		return true;
	}

	public boolean addJobtoSchedule(JobEntity jobEntity) throws Exception {
		String[] strs = getJobAndTriggerStr(jobEntity.getRwbh());
		JobDetail job = newJob(JobData.class).withIdentity(strs[0],
				DEFAULT_GROUP_NAME).build();
		CronTrigger trigger = newTrigger().withIdentity(strs[1],
				DEFAULT_GROUP_NAME).withSchedule(
				cronSchedule(jobEntity.getRwzq())).build();
		putData(job);
		quarzScheduler.scheduleJob(job, trigger);
		log.debug("任务:" + jobEntity.getRwbh() + " 加入调度器!");
		return true;
	}

	private void putData(JobDetail job) {
		job.getJobDataMap().put(JobData.JOBDAO, getJobDao());
		job.getJobDataMap().put(JobData.SCHUDULERDAO, this);
	}

	private String[] getJobAndTriggerStr(String rwbh) {
		String[] strs = { new StringBuffer(JOB_PREFIX).append(rwbh).toString(),
				new StringBuffer(TRIGGER_PREFIX).append(rwbh).toString() };
		return strs;
	}
}
