package com.sunshine.monitor.system.analysis.zyk.service.imp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.system.analysis.zyk.bean.CaseInfo;
import com.sunshine.monitor.system.analysis.zyk.dao.CaseInfoDao;
import com.sunshine.monitor.system.analysis.zyk.service.CaseInfoManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;


@Service("caseInfoManager")
public class CaseInfoManagerImpl implements CaseInfoManager{
	
	@Autowired
	private CaseInfoDao caseInfoDao;
	@Autowired
	private SystemDao systemDao;
	
	private Logger log = LoggerFactory.getLogger(CaseInfoManagerImpl.class);
	private String table = "JM_ZTK_CASE_INFO";
	@Override
	public Map<String, Object> queryList(CaseInfo ci,
			Map<String, Object> filter) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sql = new StringBuffer("select id,ajbh,gabh,ajly,ajmc,hphm,hpzl,description,tplj,lrr,lrsj,xxly,xzqh from "+table+" where 1 = 1");
		if(ci.getHphm()!=null && !"".equals(ci.getHphm())){
			sql.append(" and hphm like '%"+ci.getHphm()+"%'");
		}
		if(ci.getHpzl()!=null && !"".equals(ci.getHpzl())){
			sql.append(" and hpzl = '"+ci.getHpzl()+"'");
		}
		if(ci.getKssj()!=null && !"".equals(ci.getKssj())){
			sql.append(" and lrsj >= to_date('"+ci.getKssj()+"','yyyy-mm-dd hh24:mi:ss')");
		}
		if(ci.getJssj()!=null && !"".equals(ci.getJssj())){
			sql.append(" and lrsj <= to_date('"+ci.getJssj()+"','yyyy-mm-dd hh24:mi:ss')");
		}
		if(ci.getLrr()!=null && !"".equals(ci.getLrr())){
			sql.append(" and lrr like '%"+ci.getLrr()+"%'");
		}
		if(ci.getAjbh()!=null && !"".equals(ci.getAjbh())){
			sql.append(" and ajbh like '%"+ci.getAjbh()+"%'");
		}
		if(ci.getAjly()!=null && !"".equals(ci.getAjly())){
			sql.append(" and ajly = '"+ci.getAjly()+"'");
		}
		if(ci.getAjmc()!=null && !"".equals(ci.getAjmc())){
			sql.append(" and ajmc like '%"+ci.getAjmc()+"%'");
		}
		
	    try {
	    	long start = System.currentTimeMillis();
			map = this.caseInfoDao.findPageForMap(sql.toString(), Integer
					.parseInt(filter.get("page").toString()), Integer
					.parseInt(filter.get("rows").toString()));
			long end = System.currentTimeMillis();
			log.info("查询案件信息库耗时："+(end - start)+"MS");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		list =  (List<Map<String, Object>>) map.get("rows");
		if(list!=null && list.size()>1){
			for (Iterator i = list.iterator(); i.hasNext();) {
				Map<String,Object> temp = (Map<String, Object>) i.next();
				String hpzl = "";
				try {
					hpzl = this.systemDao.getCodeValue("030107", temp.get("hpzl")==null?"":temp.get("hpzl").toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				temp.put("LRSJ", sdf.format(temp.get("lrsj")));
				temp.put("HPZL", hpzl);
			}
		}
		map.put("rows", list);
		return map;
	
	}
	
	@Override
	public void saveCaseInfo(CaseInfo ci) {
		try {
			this.caseInfoDao.saveCaseInfo(ci);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public CaseInfo getCaseInfoById(String id) {
		CaseInfo ci = new CaseInfo();
		try {
			ci = this.caseInfoDao.getCaseInfoById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ci;
	}
	
	@Override
	public void updateCaseInfo(CaseInfo ci){
		try {
			this.caseInfoDao.updateCaseInfo(ci);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteCaseInfo(String id){
		try {
			this.caseInfoDao.deleteCaseInfo(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<CaseInfo> getAllCaseInfo(){
		List<CaseInfo> caseInfoList = new ArrayList<CaseInfo>();
		try {
			 caseInfoList = this.caseInfoDao.getAllCaseInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return caseInfoList;
	}
}
