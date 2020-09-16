package com.sunshine.monitor.system.query.service;

public interface StatQueryManager {

	/**
	 * 四两统计/二数统计
	 * @param dwdm
	 * @param kssj
	 * @param jssj
	 * @return
	 * @throws Exception
	 */
	public String[]  getShowStat2Rate4XML(String dwdm,String kssj,String jssj)  throws Exception ;

}
