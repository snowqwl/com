package com.sunshine.monitor.system.gate.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.gate.bean.CodeDirect;
import com.sunshine.monitor.system.gate.bean.CodeGateCd;

public interface DirectDao extends BaseDao {
	public Map<String,Object> findDirectForMap(Map filter, CodeDirect direct) throws  Exception;

	public List getDirects() throws  Exception;
	
	public List<CodeDirect> getDirectsByfxbh(String fxbh) throws  Exception;

	public CodeDirect getDirect(String paramString) throws  Exception;
	
	public boolean saveDirect(CodeDirect direct) throws  Exception;
	
	public boolean saveDirect(CodeDirect direct, String fxbh) throws  Exception;

	public List getDirectBySbbh(String paramString) throws  Exception;

	public List getDirectByKdbh(String paramString) throws  Exception;
	
	/**
	 * 旧版卡口方向
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	public List getOldDirectByKdbh(String paramString) throws  Exception;

	public String getDirectName(String paramString) throws  Exception;
	
	/**
	 * 旧版卡口方向名称
	 * @param paramString
	 * @return
	 * @throws Exception
	 */
	public String getOldDirectName(String paramString) throws  Exception;
	
	public List<CodeGateCd> getGateCdListByFxbh(String fxbh)throws  Exception;
	

}