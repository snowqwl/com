package com.sunshine.monitor.comm.quart.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.comm.quart.bean.JobEntity;
/**
 * 后台任务操作接口
 * 
 * @author OUYANG 2013/6/18
 * update 2013/6/25
 * 
 */
public interface ScheduleJobDao extends BaseDao {

	/**
	 * 查询后台任务列表 : 如果tableName==null,则查询初始化表[JM_TRANS_SCHEDULE]，否则查询tableName
	 * 
	 * @param condition
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryScheduleList(Map<String, Object> condition,
			String tableName) throws Exception;
	
	
	/**
	 * 添加定时调度任务
	 * @param jobEntity
	 * @return Rwbh
	 * @throws Exception
	 */
	public String addSchedule(JobEntity jobEntity) throws Exception;
	
	
	/**
	 * 更新任务信息
	 * @param jobEntity
	 * @return
	 * @throws Exception
	 */
	public int updateJobEntity(JobEntity jobEntity) throws Exception;

	/**
	 * 删除任务
	 * @param rwbh
	 * @throws Exception
	 */
	public int deleteById(String rwbh) throws Exception;

	/**
	 * 
	 * @param rwbh
	 * @return
	 * @throws Exception
	 */
	public JobEntity getJobEntityByRwbh(String rwbh) throws Exception;

	/**
	 * 获取所有任务
	 * False:加载所有正常的后台任务
	 * True:加载所有后台任务
	 * @param isLoadAll 是否加载所有任务
	 * @return
	 * @throws Exception
	 */
	public List<JobEntity> getAllJobEntitys(boolean isLoadAll) throws Exception;

	
	/**
	 * 更新任务状态
	 * @param rwbh
	 * @param rwzt
	 * @return
	 * @throws Exception
	 */
	public int updateJobStatu(String rwbh, String rwzt) throws Exception ;
	
	
	/**
	 * 更新任务错误内容描述
	 * @param rwbh
	 * @param cwnr
	 * @return
	 * @throws Exception
	 */
	public int updateJobErrordesc(String rwbh, String cwnr) throws Exception ;
	
	
	/**
	 * 获取系统时间
	 * @return
	 * @throws Exception
	 */
	public String getTime() throws Exception;
	
	
	/**
	 * 更新任务更新时间及状态正在运行中
	 * @param rwbh
	 * @return
	 * @throws Exception
	 */
	public int updateJobDate(String rwbh) throws Exception ;
	
	
	/**
	 * 保存后台任务执行日志
	 * @param rwbh
	 * @param zxsj
	 * @param czlx
	 * @param czjg
	 * @param cwnr
	 * @return
	 * @throws Exception
	 */
	public int saveLog(String rwbh, String zxsj, String czlx, String czjg,
			String cwnr) throws Exception ;
	
	
	/**
	 * 检查本机IP与系统参数表配置的IP是否相同
	 * 127.0.0.1除外
	 * @return
	 * @throws Exception
	 */
	public boolean checkTaskip() throws Exception ;
	
	
	/**
	 * True:任务正常与否，都进行初始化
	 * @param flag
	 * @throws Exception
	 */
	public void initJobData(boolean flag) throws Exception;
	
	public void updateJob(JobEntity jobEntity)throws Exception;
}
