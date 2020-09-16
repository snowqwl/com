package com.sunshine.monitor.comm.quart.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.quart.bean.JobEntity;
/**
 * 
 * @author OUYANG 2013/6/25
 *
 */
public interface ScheduleJobManager {

	/**
	 * 查询后台任务列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryScheduleList(Map<String, Object> conditions) throws Exception;

	
	/**
	 * 添加定时调度任务
	 * @param jobEntity
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> addSchedule(JobEntity jobEntity) throws Exception;


	/**
	 * 由任务编号查询任务对象
	 * @param rwbh
	 * @return
	 * @throws Exception
	 */
	public JobEntity getJobEntityByRwbh(String rwbh) throws Exception;
	
	
	
	/**
	 * 更新任务
	 * @param jobEntity
	 * @throws Exception
	 */
	public int updateJobEntity(JobEntity jobEntity) throws Exception;
	
	
	
	/**
	 * 加载所有任务对象
	 * @param isLoadAll
	 * @return
	 * @throws Exception
	 */
	public List<JobEntity> getAllJobEntitys(boolean isLoadAll) throws Exception;
	
	/**
	 * 更新任务状态
	 * @param jobid
	 * @param statu
	 * @return
	 * @throws Exception
	 */
	public int updateJobStatu(String jobid, String statu) throws Exception;
	
	
	/**
	 * 更新任务错误内容描述
	 * @param jobid
	 * @param cwnr
	 * @return
	 * @throws Exception
	 */
	public int updateJobErrordesc(String jobid, String errorDesc) throws Exception ;
	
	
	/**
	 * 删除任务(事务)
	 * @param rwbh
	 * @throws Exception
	 */
	public Map<String,String> deleteScheduleJobByrwbh(String rwbh) throws Exception;
	
	/**
	 * 删除任务
	 * @param rwbh
	 * @throws Exception
	 */
	public int deleteScheduleJob(String rwbh) throws Exception;	
	
	/**
	 * 启动任务
	 * @param rwbh
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> startScheduleJob(String rwbh) throws Exception;
	
	
	/**
	 * 停止任务
	 * @param rwbh
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> stopScheduleJob(String rwbh) throws Exception;
	
	/**
	 * 保存后台任务执行日志
	 * @param rwbh 任务编号
	 * @param zxsj 最新时间
	 * @param czlx 操作类型(0-停止、1-启动、2-自动运行)
	 * @param czjg 操作结果(0-失败、1-成功)
	 * @param cwnr 错误内容
	 * @return
	 * @throws Exception
	 */
	public int saveLog(String rwbh, String zxsj, String czlx, String czjg,
			String cwnr) throws Exception ;
	
	/**
	 * 获取系统时间
	 * @return
	 * @throws Exception
	 */
	public String getTime() throws Exception;
	
	
	/**
	 * 更新任务
	 * 如果任务周变化了,则须重新加入调度器
	 * @param jobEntity
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> updateScheduleJob(JobEntity jobEntity) throws Exception ;
	
	
}
