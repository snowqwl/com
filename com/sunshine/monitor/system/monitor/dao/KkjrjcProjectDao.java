package com.sunshine.monitor.system.monitor.dao;

import com.sunshine.monitor.comm.dao.BaseDao;

public interface KkjrjcProjectDao extends BaseDao {
	
	
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
    
    public int getKkzxCount(String kssj,String jssj) throws Exception;
}
