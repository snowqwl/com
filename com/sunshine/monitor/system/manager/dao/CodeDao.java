package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.Code;

public interface CodeDao extends BaseDao {
	
	public Code getCodeByTable(String paramString1, String paramString2) 
	         throws Exception;

	public List<Code> getCodesByTable(String paramString) throws Exception;
	
	public Map<String, Object> getCodesbyTable(Map filter,String paramString) throws Exception;
	
	public int saveCode(Code paramCode) throws Exception;
	
	public int removeCode(Code paramCode) throws Exception;
	
	public List<Code> getCodeDetail(String dmlb,String dmz)throws Exception;
	
	public int updateCode(Code paramCode) throws Exception;
}
