package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.Syspara;


public interface SysparaDao extends BaseDao {
	
	public abstract List getSysparas(String paramString) throws Exception;
	
	public Map<String, Object> getSysparas(Map filter) throws Exception;
	
    public Syspara getSyspara(String paramString1, 
    		String paramString2, String paramString3) throws Exception;
}
