package com.sunshine.monitor.system.analysis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.analysis.dao.StudyDao;
import com.sunshine.monitor.system.analysis.service.StudyManager;

@Service("studyManager")
@Transactional
public class StudyManagerImpl implements StudyManager {

	@Autowired
	@Qualifier("studyDao")
	private StudyDao studyDao;
	public Map queryGateGroupList(Map filter, String gname) {
		return this.studyDao.queryGateGroupList(filter, gname);
	}
	
	public int insertGateGroup(String gname,String kdbhs) {
		return this.studyDao.insertGateGroup(gname, kdbhs);
	}
	
	public int updateGateGroup(String gid,String gname,String kdbhs) {
		return this.studyDao.updateGateGroup(gid, gname, kdbhs);
	}
	
	public void deleteGateGroup(String gid) {
		this.studyDao.deleteGateGroup(gid);
	}
	
	public List queryGateListByGid(String gid) {
		return this.studyDao.queryGateListByGid(gid);
	}
	
	public Map queryTimeGroupList(Map filter, String tname){
		return this.studyDao.queryTimeGroupList(filter, tname);
	}
	
	public int insertTimeGroup(Map params) {
		return this.studyDao.insertTimeGroup(params);
	}
	
	public int updateTimeGroup(Map params) {
		return this.studyDao.updateTimeGroup(params);
	}
	
	public void deleteTimeGroup(String tid) {
		this.studyDao.deleteTimeGroup(tid);
	}
	
	public List getTimeGroupByTid(String tid) {
		return this.studyDao.getTimeGroupByTid(tid);
	}
	
	public Map  queryRuleList(Map filter, Map param) {
		return this.studyDao.queryRuleList(filter, param);
	}
	
	public int updateRule(Map param) {
		return this.studyDao.updateRule(param);
	}
	
	public int deleteRule(String rid) {
		return this.studyDao.deleteRule(rid);
	}

}
