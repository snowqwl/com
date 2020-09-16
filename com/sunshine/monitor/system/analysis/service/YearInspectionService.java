package com.sunshine.monitor.system.analysis.service;

import com.sunshine.monitor.system.analysis.bean.JmYearInspection;

public interface YearInspectionService {
	
	public void downloadResult();
	
	/**
	 * 保存到报废未年检黑名单库
	 * @param JmYearInspection 
	 * @return
	 */
	public int saveYearInspection(JmYearInspection bean) throws Exception;

}
