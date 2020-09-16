package com.sunshine.monitor.system.query.service;

import java.util.List;

public interface StatSignQueryManager {

	/**
	 * 签收统计处置统计
	 * @param kssj
	 * @param jssj
	 * @param glbm
	 * @param bkdl
	 * @return
	 */
	public List<List<String>> getSignQuery(String kssj,String jssj,String glbm,String bkdl,String jb);
}
