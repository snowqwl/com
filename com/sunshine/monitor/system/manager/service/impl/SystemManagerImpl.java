package com.sunshine.monitor.system.manager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.StatSystem;
import com.sunshine.monitor.system.manager.bean.AdministrativeDivision;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Transactional
@Service("systemManager")
public class SystemManagerImpl implements SystemManager {

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	public List getCode(String code) throws Exception {
		return systemDao.getCode(code);
	}

	public void setSystemDao(SystemDao systemDao) {
		this.systemDao = systemDao;
	}

	public int loadUser() throws Exception {
		return this.systemDao.loadUser();
	}

	public int loadDepartment() throws Exception {
		return this.systemDao.loadDepartment();
	}

	public int loadCode() throws Exception {
		return this.systemDao.loadCode();
	}

	public int loadPrefecture() throws Exception {
		return this.systemDao.loadPrefecture();
	}

	public String getParameter(String gjz, HttpServletRequest request) {
		return this.systemDao.getParameter(gjz, request);
	}

	public String getParameter(String gjz, String glbm,
			HttpServletRequest request) {
		return this.systemDao.getParameter(gjz, glbm, request);
	}

	public SysUser getUser(String yhdh) throws Exception {
		return this.systemDao.getUser(yhdh);
	}

	public String getUserName(String yhdh) throws Exception {
		return this.systemDao.getUserName(yhdh);
	}

	public List getUsers(String glbm) throws Exception {
		return this.systemDao.getUsers(glbm);
	}

	public List getDepartments() throws Exception {
		return this.systemDao.getDepartments();
	}

	public Department getDepartment(String glbm) throws Exception {
		return this.systemDao.getDepartment(glbm);
	}

	public String getDepartmentName(String glbm) throws Exception {
		return this.systemDao.getDepartmentName(glbm);
	}

	public List getCodes(String dmlb) throws Exception {
		return this.systemDao.getCodes(dmlb);
	}

	public Code getCode(String dmlb, String dmz) throws Exception {
		return this.systemDao.getCode(dmlb, dmz);
	}

	public String getCodeValue(String dmlb, String dmz) throws Exception {
		return this.systemDao.getCodeValue(dmlb, dmz);
	}

	public List<Code> getCodesByDmsm(String dmlb, String dmsm, int position)
			throws Exception {
		return this.systemDao.getCodesByDmsm(dmlb, dmsm, position);
	}

	public String getCodeValue(String dmlb, String dmz, int position)throws Exception{
		
		return this.systemDao.getCodeValue(dmlb, dmz, position);
	}
	
	public String getSysDate(String day, boolean isTime) throws Exception {
		return this.systemDao.getSysDate(day, isTime);
	}

	public List<AdministrativeDivision> queryAdministrativeAdvisions()
			throws Exception {
		String subConditions = " where sfxs='1' and zt='1' order by wz asc";
		return systemDao.queryAdministrativeDivisions(subConditions);
	}

	public List<AdministrativeDivision> queryCityAdministrativeAdvisions(
			String cityname) throws Exception {
		return systemDao.queryCityAdministrativeAdvisions(cityname);

	}

	public List getCrossingByFilter(String xzqh, String dldm) throws Exception {
		return systemDao.getCrossingByFilter(xzqh, dldm);
	}

	public List getDistricts(String glbm) throws Exception {
		return systemDao.getDistricts(glbm);
	}

	public List getDistrictsByCity(String city) throws Exception {
		return systemDao.getDistrictsByCity(city);
	}

	/**
	 * 根据行政区划代码获取行政区划名称
	 * 
	 * @param xzqhdm
	 * @return
	 * @throws Exception
	 */
	public String getDistrictNameByXzqh(String xzqhdm) throws Exception {
		return systemDao.getDistrictNameByXzqh(xzqhdm);
	}

	public List getRoadsByFilter(String xzqh) throws Exception {
		return systemDao.getRoadsByFilter(xzqh);
	}

	public List getTrafficDepartment(String xzqh) throws Exception {
		return systemDao.getTrafficDepartment(xzqh);
	}

	public void updateStatSystem(StatSystem ss) {
		systemDao.updateStatSystem(ss);

	}

	public int loadDirect() throws Exception {

		return systemDao.loadDirect();
	}

	public List<Map<String, Object>> getDirects() throws Exception {
		return this.systemDao.getDirects();
	}
	
	public void loadGateTree() throws Exception {
		this.systemDao.loadGateTree();
	}
	
	public void loadGateTreeFilterDirect() throws Exception {
		this.systemDao.loadGateTreeFilterDirect();
	}

	public int updateVisitTotal() throws Exception {
       return this.systemDao.updateVisitTotal();		
	}

	public List<Map<String, Object>> getXzqhTree(String dwdm) throws Exception {
		List<Map<String, Object>> list = this.systemDao.getXzqhTree(dwdm);
		Map<String,Object> map = new HashMap<String,Object>();
		return list;
	}
	
	public List getCityList() throws Exception{
		return this.systemDao.getCityList();
	}
	
	public List getKd(String dwdm) throws Exception{
		//return this.systemDao.getCityList();
		return null;
	}
	
	public String getBaseConnectionState(String ds)throws Exception{
		 return this.systemDao.getBaseConnectionState(ds);
	}
	
	public List getCodeByDmlb(String dmlb,String subsql)throws Exception{
		return this.systemDao.getCodeByDmlb(dmlb,subsql);
	}
}
