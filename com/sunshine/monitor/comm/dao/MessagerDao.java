package com.sunshine.monitor.comm.dao;

import java.util.List;

import com.sunshine.monitor.comm.bean.Messager;

public interface MessagerDao extends BaseDao {
	
	
	/**
	 * 获取Kettle所需不符合规则的信息
	 * @return
	 */
	public List<Messager> getMessagerInfo() throws Exception;

	public List getStatHbInfo()throws Exception;

}
