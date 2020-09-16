package com.sunshine.monitor.system.monitor.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.system.monitor.bean.GateProject;
import com.sunshine.monitor.system.monitor.dao.GateProjectDao;
import com.sunshine.monitor.system.monitor.service.GateProjectManager;



@Service("gateProjectManager")
public class GateProjectManagerImpl implements GateProjectManager {

    @Autowired
    @Qualifier("gateProjectDao")
	private GateProjectDao gateProjectDao;

	public Map<String, Object> findGateProjectForMap(Map filter,
			GateProject project) {
		return this.gateProjectDao.findGateProjectForMap(filter, project);
	}

	public GateProject getGateProject(String rq, String dwdm) {
		return this.gateProjectDao.getGateProject(rq, dwdm);
	}

	public List<GateProject> getGateProjects() {
		return this.gateProjectDao.getGateProjects();
	}
	
	public Map getGateConnectCount() throws Exception{
		return this.gateProjectDao.getGateConnectCount();
	}
	

	public List getCode_Url() throws Exception{
		return this.gateProjectDao.getCode_Url();
	}

	public List getGateInfo() throws Exception{
		return this.gateProjectDao.getGateInfo();
	}
	
	public List getGateInfoByCity(String glbm) throws Exception{
		return this.gateProjectDao.getGateInfoByCity(glbm);
	}

	public List<GateProject> getGateProjectsByrq(String rq) {
		return this.gateProjectDao.getGateProjectsByrq(rq);
	}

	public boolean saveGateProject(GateProject project) {
		return this.gateProjectDao.saveGateProject(project);
	}

	public boolean batchDeleteProject(String[] rqs) {
		return this.gateProjectDao.batchDeleteProject(rqs);
	}
	
}
