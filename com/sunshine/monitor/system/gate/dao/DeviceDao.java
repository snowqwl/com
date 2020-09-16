package com.sunshine.monitor.system.gate.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.gate.bean.CodeDevice;

public interface DeviceDao extends BaseDao {
    
	public Map<String, Object> getDevices(Map filter,CodeDevice paramCodeDevice,
			String paramString) throws Exception;

	public CodeDevice getDevice(String paramString) throws Exception;
	
	public String getDeviceName(String sbbh) throws Exception;
	
	/**
	 * 旧版设备编号
	 * @param sbbh
	 * @return
	 * @throws Exception
	 */
	public String getOldDeviceName(String sbbh) throws Exception;
	
    public List getAllDevices() throws Exception;
    
    public List getDevices() throws Exception;
    
    public List<CodeDevice> getDevicesByKdbh(String kdbh) throws Exception;
    
    public Map<String, Object> getDevices(Map filter,CodeDevice paramCodeDevice) throws Exception;
    
    public int saveDevice(CodeDevice codeDvice) throws Exception;
    
    public int removeDevice(CodeDevice codeDevice) throws Exception;
}
