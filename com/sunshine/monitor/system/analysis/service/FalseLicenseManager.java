package com.sunshine.monitor.system.analysis.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.analysis.bean.FalseLicense;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface FalseLicenseManager {

	
	public Map<String,Object> findPageForFL(Map<String,Object> map,FalseLicense fl)throws Exception;
	
	public void delFalseLicense(List<FalseLicense> list)throws Exception;
	
	public Map getAllHphm(Map<String,Object> filter)throws Exception;
	
	public List getTrajectoryByHphm(Map<String,Object> filter)throws Exception ;
	
	public int writeTrajectory(List list)throws Exception ;
	
	public int writeFalseTable(Map<String,Object> filter)throws Exception ;
	
	public boolean isExistHphm(Map<String,Object>filter)throws Exception;
	
	public Map getFalseCount(Map<String, Object> filter) throws Exception;
	
	public Map getSuspectedListBysign(Map<String ,Object > filter) throws Exception ;
	
	public Map getFalseListByHphm(Map<String,Object> filter) throws Exception ;
	
	public String sureFalse(String hphm,String flag,String hpzl) throws Exception ;
	
	public Map<String, Object> queryFalseList(Map filter,VehPassrec veh) throws Exception ;
	
	public List<Map<String, Object>> queryXyFalseListExt(Map filter, VehPassrec veh) throws Exception ;
	
	public Integer queryXyFalseListTotal(Map filter, VehPassrec veh) throws Exception ;
	
	public Map queryXyFalseList(Map filter) throws Exception ;
	
	public Map queryXyAllFalseList(Map filter) throws Exception ;
	
	public void updateXyFalse(String hphm,String hpzl) throws Exception;
	
	public void updateXyFalse(String newzt, String hphm,String hpzl,String hpys,SysUser user) throws Exception;
	
	/**查询分析出来的过车信息*/
	public List queryPass(Map filter) throws Exception ;
	
	public List<Map<String, Object>> queryXyFalseListExt(Map<String, Object> filter) throws Exception ;
	public Integer queryXyFalseListTotal(Map<String, Object> filter) throws Exception ;
}
