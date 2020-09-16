package com.sunshine.monitor.comm.quart;

import com.sunshine.monitor.comm.quart.bean.JobEntity;

/**
 * Quart Scheduler
 * @author OUYANG 2013/6/21
 *
 */
public interface AbstractScheduler {
	
	
	/**
	 * 初始化调度器，并启动任务
	 */
	public void initScheduler(boolean flag) throws Exception;
	
	
	/**
	 * 添加一个任务
	 * @param scheduler
	 * @param jobEntity
	 */
	public boolean addJobtoSchedule(JobEntity jobEntity) throws Exception;
	

	/**
	 * 删除一个任务
	 * @param jobEntity
	 * @return
	 */
	public boolean deleteJobFromSchedule(JobEntity jobEntity) throws Exception;
	
	
	/**
	 * 更新任务
	 * @param jobEntity
	 * @return
	 */
	public boolean updateJobFromSchedule(JobEntity jobEntity) throws Exception;
	
	
	/**
	 * 关闭调度器
	 * @return
	 */
	public void shutdownSchedule() throws Exception;
	
}
