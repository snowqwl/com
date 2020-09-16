package com.sunshine.monitor.system.veh.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.veh.bean.VehModelBean;

public interface VehModelDao extends BaseDao {
	
	/**
	 * 查询车辆品牌信息列表
	 * @param conditions
	 * @return map
	 * @throws Exception
	 */
	public List<VehModelBean> getVehModelList(Map<String, Object> conditions) throws Exception;
	
	
	public int insert(Map<String, Object> map) throws Exception;
	
	public int updateClxh(VehModelBean bean) throws Exception;
}
