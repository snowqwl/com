package com.sunshine.monitor.comm.maintain.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.MaintainHandle;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.manager.bean.Department;

public interface MaintainDao {

	public Map findMaintainForMap(Map filter, String bzlx)throws Exception;

	public void setby1ForGate(String kdbh)throws Exception;

	public Map findMaintainHandleForMap(Map filter, MaintainHandle handle,Department department)throws Exception;

	public MaintainHandle getMaintainHandleForId(String id)throws Exception;

	public String saveMaintainHandle(MaintainHandle handle)throws Exception;
	
	public void setCodeGateState(String kdbh,String fxbh,String kkzt,String sbzt) throws Exception;
	
	public List<CodeGateExtend> getGzCodeGateInfo() throws Exception;
	
	//短信发送记录
	public int saveDxsj(String value,String values)
	throws Exception;

}
