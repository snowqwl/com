package com.sunshine.monitor.system.notice.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.notice.bean.SysInformation;
import com.sunshine.monitor.system.notice.bean.SysInformationreceive;

public interface InformationDao {
	
	public Map<String, Object> getSysInformations(Map<String, Object> condition)throws Exception ;

	public List<SysInformationreceive> getReceivesById(String xh) throws Exception;
	
	public List<Department> getDepartmentByUser() throws Exception;
	
	public List<Department> getDepartmentByUser(String glbm) throws Exception;
	
	public String saveInformation(SysInformation information) throws Exception;
	
	public int updateInformation(SysInformation information) throws Exception;
	
	public int removeInformation(SysInformation information) throws Exception;
	
	public int removeReceive(String xh) throws Exception;
	
	public int saveReceive(SysInformationreceive sysInformationreceive) throws Exception;
	
	public List getReceivesForSublist(String paramString) throws Exception;
	
	public SysInformationreceive getReceives(String paramString1,String paramString2) throws Exception;
	
	public Map<String, Object> getSysInfos(Map<String, Object> condition)throws Exception ;
	public List<SysInformation> getZxggInfos(Map<String, Object> condition)throws Exception ;
	public SysInformation getInformation(String xh, String username) throws Exception ;
	public SysInformation getEditInformation(String xh) throws Exception ;
	public List getYhdh(String xh) throws Exception ;
}
