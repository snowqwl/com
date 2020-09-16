package com.sunshine.monitor.system.monitor.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;

public interface IdentProjectDao extends BaseDao {

	public List<Map<String,Object>> getIdentProjectInfo(String kssj,String jssj,String kdbhs);
	
	public List<Map<String,Object>> getIndetcdProjectInfo(String kssj,String jssj,String kdbh,String fxbh);
}
