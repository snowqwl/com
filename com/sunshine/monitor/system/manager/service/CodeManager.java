package com.sunshine.monitor.system.manager.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Codetype;

public interface CodeManager {
	
	public List<Codetype> getCodetypes(Codetype paramCodetype) throws Exception;
	
	public Codetype getCodetype(String paramString) throws Exception;
	
	public Map<String,Object> getCodetypesByPageSize(Map filter,Codetype paramCodetype) throws Exception;
	
	public Code getCodeByTable(String paramString1, String paramString2) throws Exception;
	
	/**
	 * 初始化方向类型下拉菜单
	 * @param paramString   代码类别  对应FRM_CODE表
	 * @return
	 */
	public List<Code> getCodesByTable(String paramString) throws Exception;
	
	public Map<String, Object> getCodesByTable(Map filter,String paramString) throws Exception;
	
	public int saveCodetype(Codetype paramCodetype) throws Exception;
	
	public int removeCodetype(Codetype paramCodetype) throws Exception;
	
	public int saveCode(Code paramCode) throws Exception;
	
	public int removeCode(Code paramCode) throws Exception;
	
	public List<Code> getCodeDetail(String dmlb,String dmz)throws Exception;
	
	public int updateCode(Code code) throws Exception;
	
}