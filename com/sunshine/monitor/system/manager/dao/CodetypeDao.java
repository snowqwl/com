package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.Codetype;

public interface CodetypeDao extends BaseDao {
	
	public List<Codetype> getCodetypes(Codetype paramCodetype) throws Exception;
	
	
	public Map<String,Object> getCodetypesByPageSize(Map filter,Codetype paramCodetype)
	          throws Exception;
	
	
	public Codetype getCodetype(String paramString) throws Exception;
	
	
	public int saveCodetype(Codetype paramCodetype) throws Exception;
	
	
	public int removeCodetype(Codetype paramCodetype) throws Exception;
}
