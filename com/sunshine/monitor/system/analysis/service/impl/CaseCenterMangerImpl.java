package com.sunshine.monitor.system.analysis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.analysis.bean.CaseEntity;
import com.sunshine.monitor.system.analysis.bean.CaseGroupEntity;
import com.sunshine.monitor.system.analysis.dao.CaseCenterDao;
import com.sunshine.monitor.system.analysis.dao.CaseCenterSCSDao;
import com.sunshine.monitor.system.analysis.dao.CaseCenterZYKDao;
import com.sunshine.monitor.system.analysis.service.CaseCenterManager;
import com.sunshine.monitor.system.manager.service.SystemManager;



@Service("caseCenterManager")
public class CaseCenterMangerImpl implements CaseCenterManager{
	@Autowired
	@Qualifier("caseCenterDao")
	private CaseCenterDao caseCenterDao;
	
	@Autowired
	@Qualifier("caseCenterSCSDao")
	private CaseCenterSCSDao caseCenterSCSDao;
	
	@Autowired
	@Qualifier("caseCenterZYKDao")
	private CaseCenterZYKDao caseCenterZYKDao;
	
	@Autowired
	private SystemManager systemManager;
	public List<Map<String, Object>> queryAnalysisProjectList(
			CaseGroupEntity caseEntity) throws Exception {
		List list=this.caseCenterDao.queryAnalysisProjectList(caseEntity);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			map.put("FXLXMC", systemManager.getCodeValue("157000", map.get("FXLX").toString()));
		}
		return list;
	}

	public Map<String, Object> findCaseByPage(CaseEntity entity,
			Map<String, Object> filter) throws Exception {
		Map<String,Object> map = this.caseCenterDao.findCaseByPage(entity, filter);
		/**
		 * TODO 翻译字段
		 */
		return map;
	}

	public CaseEntity getCaseDetail(Map<String,Object> entity) throws Exception {
		Map<String, Object> result = this.caseCenterSCSDao.getCaseDetail(entity);
		CaseEntity caseEntity = new CaseEntity();
		caseEntity.setAjbh(result.get("ajbh")==null?"":result.get("ajbh").toString());
		caseEntity.setAfsj(result.get("afsj")==null?"":result.get("afsj").toString());
		caseEntity.setAfdz(result.get("afdz")==null?"":result.get("afdz").toString());
		return caseEntity;
	}

	public Map<String, Object> findCaseGroupByPage(CaseGroupEntity entity,
			Map<String, Object> filter) throws Exception {
		Map<String,Object> result = this.caseCenterDao.findCaseGroupByPage(entity, filter);
		// 用于保存分析时，则不查询关联案件
		if(filter.get("saveanalysis")==null || "".equals(filter.get("saveanalysis").toString()) || "null".equals(filter.get("saveanalysis").toString())){
			List<CaseGroupEntity> list =(List<CaseGroupEntity>)result.get("rows");
			for(CaseGroupEntity item:list){
				JSONArray array = new JSONArray();
				// 从oracle专案关联信息表中查询出关联的案件编号
				List<CaseEntity> ajs = this.caseCenterDao.getCaseList(item.getZabh());  
				// 从greenplum中查询出案件信息
				for(CaseEntity aj:ajs){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("ajbh", aj.getAjbh());
					Map<String, Object> ajxx = this.caseCenterSCSDao.getCaseDetail(params);
					JSONObject json = new JSONObject();
					json.put("ajbh",ajxx.get("ajbh"));
					json.put("ajmc",ajxx.get("ajmc"));
					array.add(json);
				}
				item.setGlaj(array.toString());
			}
			result.put("rows",list);
		}
		return result;
	}

	@Transactional
	public void saveCaseGroup(CaseGroupEntity entity) throws Exception {
		String zabh = this.caseCenterDao.saveCaseGroup(entity);
		String[] glaj = entity.getGlaj().split(",");
		for(String s:glaj){
			this.caseCenterDao.saveAssociateInfo(zabh,s);
		}
	}
}
