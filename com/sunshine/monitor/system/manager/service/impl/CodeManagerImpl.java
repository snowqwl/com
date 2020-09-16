package com.sunshine.monitor.system.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.manager.aspect.ManagerFilterAnnotation;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Codetype;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.CodeDao;
import com.sunshine.monitor.system.manager.dao.CodetypeDao;
import com.sunshine.monitor.system.manager.service.CodeManager;

@Service("codeManager")
@Transactional()
public class CodeManagerImpl implements CodeManager {

	@Autowired
	@Qualifier("codeDao")
	private CodeDao codeDao = null;
	
	@Autowired
	@Qualifier("codetypeDao")
	private CodetypeDao codetypeDao = null;
	
	public Code getCodeByTable(String dmlb, String dmz)
			throws Exception {
		return this.codeDao.getCodeByTable(dmlb, dmz);
	}

	public List<Code> getCodesByTable(String dmlb) throws Exception {
		return this.codeDao.getCodesByTable(dmlb);
	}

	public Map<String, Object> getCodesByTable(Map filter,String dmlb) throws Exception {
		return this.codeDao.getCodesbyTable(filter,dmlb);
	}
	public Codetype getCodetype(String dmlb) throws Exception {
		return this.codetypeDao.getCodetype(dmlb);
	}

	public List<Codetype> getCodetypes(Codetype codetype) throws Exception {
		return this.codetypeDao.getCodetypes(codetype);
	}

	public Map<String,Object> getCodetypesByPageSize(Map filter, Codetype paramCodetype)
			throws Exception {
		return this.codetypeDao.getCodetypesByPageSize(filter,paramCodetype);
	}

	public CodeDao getCodeDao() {
		return codeDao;
	}

	public void setCodeDao(CodeDao codeDao) {
		this.codeDao = codeDao;
	}

	public CodetypeDao getCodetypeDao() {
		return codetypeDao;
	}

	public void setCodetypeDao(CodetypeDao codetypeDao) {
		this.codetypeDao = codetypeDao;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int removeCode(Code code) throws Exception {
		return this.codeDao.removeCode(code);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int removeCodetype(Codetype codetype) throws Exception {
		return this.codetypeDao.removeCodetype(codetype);
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int saveCodetype(Codetype codetype) throws Exception {
		return this.codetypeDao.saveCodetype(codetype);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@ManagerFilterAnnotation(method = "addCode", clazz = Code.class)
	public int saveCode(Code code) throws Exception {
	     return this.codeDao.saveCode(code);
	}
	
	public List<Code> getCodeDetail(String dmlb,String dmz)throws Exception{
		return this.codeDao.getCodeDetail(dmlb,dmz);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateCode(Code code) throws Exception {
		return this.codeDao.updateCode(code);
	}
	
}
