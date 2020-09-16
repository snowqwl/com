package com.sunshine.monitor.system.analysis.dao;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.bean.JmYearInspection;

public interface YearInspectionDao extends BaseDao {
	
	/**
	 * 保存到报废未年检黑名单库
	 * @param JmYearInspection 
	 * @return
	 */
	public int saveYearInspection(JmYearInspection bean) throws Exception;

}
