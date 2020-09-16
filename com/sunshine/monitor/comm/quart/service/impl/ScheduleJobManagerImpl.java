package com.sunshine.monitor.comm.quart.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.quart.AbstractScheduler;
import com.sunshine.monitor.comm.quart.bean.JobEntity;
import com.sunshine.monitor.comm.quart.dao.ScheduleJobDao;
import com.sunshine.monitor.comm.quart.service.ScheduleJobManager;

@Service
@Transactional
public class ScheduleJobManagerImpl implements ScheduleJobManager {

	@Autowired
	private ScheduleJobDao scheduleJobDao;

	@Autowired
	@Qualifier("schedulerDaoImpl")
	private AbstractScheduler schedulerDao;

	public Map<String, Object> queryScheduleList(Map<String, Object> conditions)
			throws Exception {

		return this.scheduleJobDao.queryScheduleList(conditions, null);
	}

	public Map<String, String> addSchedule(JobEntity jobEntity)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String rwbh = this.scheduleJobDao.addSchedule(jobEntity);
		if (rwbh != null) {
			jobEntity.setRwbh(rwbh);
			boolean flag = this.schedulerDao.addJobtoSchedule(jobEntity);
			if (flag) {
				map.put("code", "1");
				map.put("msg", "任务加入调度器成功!");
			} else {
				map.put("code", "0");
				map.put("msg", "任务加入调度器失败!");
			}
		}
		return map;
	}

	public JobEntity getJobEntityByRwbh(String rwbh) throws Exception {

		return scheduleJobDao.getJobEntityByRwbh(rwbh);
	}

	public int updateJobEntity(JobEntity jobEntity) throws Exception {

		return this.scheduleJobDao.updateJobEntity(jobEntity);
	}

	public List<JobEntity> getAllJobEntitys(boolean isLoadAll) throws Exception {

		return this.scheduleJobDao.getAllJobEntitys(isLoadAll);
	}

	public int updateJobStatu(String jobid, String statu) throws Exception {

		return this.scheduleJobDao.updateJobStatu(jobid, statu);
	}

	public int updateJobErrordesc(String jobid, String errorDesc)
			throws Exception {

		return this.scheduleJobDao.updateJobErrordesc(jobid, errorDesc);
	}

	public int deleteScheduleJob(String rwbh) throws Exception {

		return this.scheduleJobDao.deleteById(rwbh);
	}

	public int saveLog(String rwbh, String zxsj, String czlx, String czjg,
			String cwnr) throws Exception {

		return this.scheduleJobDao.saveLog(rwbh, zxsj, czlx, czjg, cwnr);
	}

	public String getTime() throws Exception {

		return this.scheduleJobDao.getTime();
	}

	public Map<String, String> deleteScheduleJobByrwbh(String rwbh)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		JobEntity jobEntity = this.getJobEntityByRwbh(rwbh);
		// 从调度器中删除任务
		boolean flag = this.schedulerDao.deleteJobFromSchedule(jobEntity);
		if (flag) {
			int result = this.deleteScheduleJob(rwbh);
			if (result == 1) {
				this.saveLog(rwbh, getTime(), "1", "1", "任务删除成功!");
				map.put("code", "1");
				map.put("msg", "任务删除成功");
			}
		}
		return map;
	}

	public Map<String, String> startScheduleJob(String rwbh) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		JobEntity jobEntity = this.getJobEntityByRwbh(rwbh);
		boolean flag = this.schedulerDao.addJobtoSchedule(jobEntity);
		if (flag) {
			int result = this.updateJobStatu(rwbh, "1");
			if (result == 1) {
				this.saveLog(rwbh, getTime(), "1", "1", "任务手工启动成功!");
				map.put("code", "1");
				map.put("msg", "任务启动成功");
			}
		}
		return map;
	}

	public Map<String, String> stopScheduleJob(String rwbh) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		JobEntity jobEntity = this.getJobEntityByRwbh(rwbh);
		boolean flag = this.schedulerDao.deleteJobFromSchedule(jobEntity);
		if (flag) {
			int result = this.updateJobStatu(rwbh, "0");
			if (result == 1) {
				this.saveLog(rwbh, getTime(), "1", "1", "任务手工停止成功!");
				map.put("code", "1");
				map.put("msg", "任务停止成功");
			}
		}
		return map;
	}

	public Map<String, String> updateScheduleJob(JobEntity jobEntity)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		JobEntity d_jobEntity = this.getJobEntityByRwbh(jobEntity.getRwbh()
				.trim());
		String temp = d_jobEntity.getRwzq().trim();
		if (temp.equals(jobEntity.getRwbh())) {
			int result = this.updateJobEntity(jobEntity);
			if (result == 1) {
				map.put("code", "1");
				map.put("msg", "任务更新成功");
			}
		} else {
			boolean flag = this.schedulerDao.updateJobFromSchedule(jobEntity);
			if (flag) {
				int result = this.updateJobEntity(jobEntity);
				if(result == 1){
					map.put("code", "1");
					map.put("msg", "任务更新成功");			
				}
			}
		}
		return map;
	}
}
