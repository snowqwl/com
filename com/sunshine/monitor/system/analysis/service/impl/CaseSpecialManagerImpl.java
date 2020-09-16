package com.sunshine.monitor.system.analysis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;
import com.sunshine.monitor.system.analysis.bean.CaseInfo;
import com.sunshine.monitor.system.analysis.bean.CaseSpecial;
import com.sunshine.monitor.system.analysis.bean.PoliceInfor;
import com.sunshine.monitor.system.analysis.dao.CaseSpecialDao;
import com.sunshine.monitor.system.analysis.service.CaseSpecialManager;

@Service("caseSpecialManager")
@Transactional
public class CaseSpecialManagerImpl implements CaseSpecialManager {

	@Autowired
	@Qualifier("caseSpecialDao")
	private CaseSpecialDao caseSpecialDao;
	
	
	public Map<String, Object> findCaseByPage(Map<String, Object> filter)
			throws Exception {
		return this.caseSpecialDao.findCaseByPage(filter);
	}
	
	
	public Map<String, Object> findPoliceByPage(Map<String, Object> filter)
	throws Exception {
       return this.caseSpecialDao.findPoliceByPage(filter);
   }
	
	
	public Map<String, Object> findCaseInfoByPage(Map<String, Object> filter)
	throws Exception {
	    return this.caseSpecialDao.findCaseInfoByPage(filter);
	 }
	
	public Map<String, Object> findExemplaryCaseByPage(Map<String, Object> filter)
	throws Exception {
	    return this.caseSpecialDao.findExemplaryCaseByPage(filter);
	 }
	
	

	
	public CaseSpecialDao getCaseSpecialDao() {
		return caseSpecialDao;
	}
	
	
	public int saveCaseDoubtfulExceplary(String[] zazyxh, String[] ajzyxh,
			String[] jqh, String[] kyzyxh) throws Exception {
		return this.caseSpecialDao.saveCaseDoubtfulExceplary(zazyxh, ajzyxh, jqh, kyzyxh);
	}
	
	public int saveCaseInfo(CaseSpecial caseSpecial) throws Exception {
	 return this.caseSpecialDao.saveCaseInfo(caseSpecial);
	}
	
	public int deleteCaseInfo(String[] kyzyxhArr) throws Exception {
		return this.caseSpecialDao.deleteCaseInfo(kyzyxhArr);
	}
	


	public void setCaseSpecialDao(CaseSpecialDao caseSpecialDao) {
		this.caseSpecialDao = caseSpecialDao;
	}


	public List<CaseInfo> getCaseInfoList() throws Exception {
		return this.caseSpecialDao.getCaseInfoList();
	}


	public List<CaseGroupEntity> getExemplaryCaseList() throws Exception {
		return this.caseSpecialDao.getExemplaryCaseList();
	}


	public List<PoliceInfor> getPoliceList() throws Exception {
		return this.caseSpecialDao.getPoliceList();
	}


	public int saveCasePic(Map<String, Object> map) throws Exception {
		return this.caseSpecialDao.saveExemplaryCaseInfo(map);
	}


	@Override
	public Map<String, Object> quicksearchList(Map<String, Object> filter) throws Exception {	
	Map result=new HashMap<>();
	      result=this.caseSpecialDao.quicksearchListByPage(filter);
		return result;
	}
	

}
