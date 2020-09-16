package com.sunshine.monitor.system.monitor.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.monitor.bean.GateProject;

public interface GateProjectDao extends BaseDao{

	public Map<String,Object> findGateProjectForMap(Map filter, GateProject project) throws DataAccessException;

	public GateProject getGateProject(String rq, String dwdm) ;
	
	public List<GateProject> getGateProjects() throws DataAccessException;
	
	public List<GateProject> getGateProjectsByrq(String rqs) throws DataAccessException;
	
	public boolean saveGateProject(GateProject project) throws DataAccessException;
	
	public boolean batchDeleteProject(String[] rqhs) throws DataAccessException;

	public Map getGateConnectCount() throws Exception ;
	
	public List getCode_Url() throws Exception;
	
	/**
	 * 全省卡口备案情况
	 * @return
	 * @throws Exception
	 */
	public List getGateInfo() throws Exception ;
	
	/**
	 * 地市版卡口接入情况
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public List getGateInfoByCity(String glbm) throws Exception;
}
