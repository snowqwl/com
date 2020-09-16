package com.sunshine.monitor.system.monitor.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.monitor.bean.GateProject;


public interface GateProjectManager {
	/**
	 * 查询JM_GATE_PROJECT/code_url表信息
	 * @param filter
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findGateProjectForMap(Map filter, GateProject project);
	/**
	 *(根据参数)查询JM_GATE_PROJECT表信息
	 * @param rq
	 * @param dwdm
	 * @return
	 * @throws Exception
	 */
	public GateProject getGateProject(String rq, String dwdm);
	/**
	 *查询JM_GATE_PROJECT表全部信息
	 * @return
	 * @throws Exception
	 */
	public List<GateProject> getGateProjects();
	/**
	 *(根据参数rq)查询JM_GATE_PROJECT表全部信息
	 * @param rq
	 * @return
	 * @throws Exception
	 */
	public List<GateProject> getGateProjectsByrq(String rq);
	/**
	 *保存JM_GATE_PROJECT表全部信息
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public boolean saveGateProject(GateProject project);
	
	/**
	 * 删除卡口登记
	 * @Description:
	 * @param: 
	 * @return: 
	 * @version:
	 */
	public boolean batchDeleteProject(String[] rqs);
	
	/**
	 * 统计卡口接入情况
	 * @return
	 */
	public Map getGateConnectCount() throws Exception;
	
	

	public List getCode_Url() throws Exception;
	
	
	/**
	 * 全省卡口接入情况
	 * @return
	 * @throws Exception
	 */
	public List getGateInfo() throws Exception;
	/**
	 * 地市版卡口接入情况
	 * @param glbm
	 * @return
	 * @throws Exception
	 */
	public List getGateInfoByCity(String glbm) throws Exception;
	
	
}
