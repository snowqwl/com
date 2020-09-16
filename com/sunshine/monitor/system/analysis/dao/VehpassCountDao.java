package com.sunshine.monitor.system.analysis.dao;

import java.util.List;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.bean.VehpassCount;

public interface VehpassCountDao extends BaseDao {
	
	/**
	 * 获取过车统计表最后一次统计时间
	 * @return
	 */
	public String getVehpassMaxDate() throws Exception;
	
	
	/**
	 * 保存过车数据统计信息
	 * @return
	 */
	public int saveVehpassCountInfo(List<VehpassCount> vhepassList) throws Exception;

}
