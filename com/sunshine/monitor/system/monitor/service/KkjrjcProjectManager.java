package com.sunshine.monitor.system.monitor.service;

public interface KkjrjcProjectManager {
	
	/**
	 * 统计卡口接入情况,在Monitor用户抽取数据
	 * @return
	 * @throws Exception
	 */
	public int getKkjrjcQueryCount() throws Exception;
	
	/**
	 * 统计卡口在线情况,在Monitor用户抽取数据
	 * @return
	 * @throws Exception
	 */
	public int getKkzxQueryCount() throws Exception;

}
