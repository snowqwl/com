package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.analysis.bean.DayNightAnalysis;
import com.sunshine.monitor.system.analysis.bean.DayNigntNewAnaiysis;
import com.sunshine.monitor.system.analysis.dao.DaysNightAnalysisDao;
import com.sunshine.monitor.system.analysis.dao.DaysNightAnalysisScsDao;
import com.sunshine.monitor.system.analysis.service.DaysNightAnalysisManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.notice.bean.SysInformation;
@Transactional
@Service
public class DaysNightAnalysisManagerImpl implements DaysNightAnalysisManager {
	@Autowired
	private DaysNightAnalysisDao daysNightAnalysisDao;
	
	@Autowired
	private DaysNightAnalysisScsDao daysNightAnalysisScsDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	private DirectDao directDao;
	
	public int saveDayNightGz(DayNightAnalysis info)throws Exception{
		
		return this.daysNightAnalysisDao.saveDayNightGz(info);
	}
	public List<DayNightAnalysis>  getDqValid() throws Exception {
		
		return this.daysNightAnalysisDao.getDqValid();
	}
	
	public Map<String, Object> queryGZList(Map<String, Object> conditions) throws Exception{
		Map info= this.daysNightAnalysisDao.queryGZList(conditions);
		List queryList = (List) info.get("rows");
		for (int i=0;i<queryList.size();i++) {
			Map  temp = (Map) queryList.get(i);		
			if(temp.get("daykssj").toString()!=""&&temp.get("dayjssj").toString()!=""){			
				String zsj = temp.get("daykssj").toString()+"-"+temp.get("dayjssj").toString();
				temp.put("Z", zsj);								
				}
			if(temp.get("prenightkssj").toString()!=""&&temp.get("prenightjssj").toString()!=""){
			
				String yssj = "上一天: "+temp.get("prenightkssj").toString()+"-"+temp.get("prenightjssj").toString();
				temp.put("YS", yssj);								
			}
			if(temp.get("nextnightkssj").toString()!=""&&temp.get("nextnightjssj").toString()!=""){
			
				String yxsj = "下一天: "+temp.get("nextnightkssj").toString()+"-"+temp.get("nextnightjssj").toString();
				temp.put("YX", yxsj);								
			}
			}
		return info;
	}
	
		public int delDayNightAna(String gzxh) throws Exception {
				
				return this.daysNightAnalysisDao.delDayNightAna(gzxh);
			}
		public DayNightAnalysis getEditDaynightInfo(String gzxh) throws Exception {
			
			return this.daysNightAnalysisDao.getEditDaynightInfo(gzxh);
		}
		public int editDayNightGz(DayNightAnalysis info) throws Exception {
			
			return this.daysNightAnalysisDao.editDayNightGz(info);
		}
		public int editZt(DayNightAnalysis info,String zt) throws Exception {
			
			return this.daysNightAnalysisDao.editZt(info,zt);
		}
		public int editOtherZt() throws Exception {
			
			return this.daysNightAnalysisDao.editOtherZt();
		}
		public DayNightAnalysis getZyxx() throws Exception {
			
			return this.daysNightAnalysisDao.getZyxx();
		}
		public Map<String,Object> queryList(DayNigntNewAnaiysis dn,Map<String, Object> filter)throws Exception{
			Map<String,Object>  map = this.daysNightAnalysisScsDao.queryList(dn,filter);
			List list=(List) map.get("rows");		
			for(int i=0;i<list.size();i++){
				Map map1=(Map)list.get(i);				
				map1.put("hpzlmc",this.systemDao.getCodeValue("030107",map1.get("HPZL")==null?"":map1.get("HPZL").toString()));
				map1.put("hpysmc",this.systemDao.getCodeValue("031001",map1.get("HPYS")==null?"":map1.get("HPYS").toString()));
			}
			return map;
		}
		
		public Map<String,Object> queryDayNightPageList(DayNigntNewAnaiysis dn,Map<String, Object> filter)throws Exception{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String,Object>  map = this.daysNightAnalysisScsDao.queryDayNightPageList(dn,filter);
			List list=(List) map.get("rows");		
			for(int i=0;i<list.size();i++){
				Map result=(Map)list.get(i);				
				result.put("HPYS", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
				result.put("XZQH", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
				result.put("KDBH", this.gateDao.getOldGateName(result.get("KDBH").toString()));
				result.put("FXBH", this.directDao.getOldDirectName(result.get("FXBH").toString()));
				result.put("GCSJ", sdf.format(result.get("GCSJ")));
			}
			return map;
		}
		@Override
		public int getForDayNightTotal(DayNigntNewAnaiysis dn,
				Map<String, Object> filter) throws Exception {
			try{
				dn.setKdbh("'" + dn.getKdbh().replace(",", "','") + "'");
				return daysNightAnalysisScsDao.getForDayNightTotal(dn, filter);
			} catch(Exception e){
				return 0;
			}
		}
		@Override
		public List<Map<String, Object>> queryForDayNightListExt(
				DayNigntNewAnaiysis dn, Map<String, Object> filter)
				throws Exception {
			List<Map<String,Object>>  list = this.daysNightAnalysisScsDao.queryForDayNightListExt(dn,filter);
			for(int i=0;i<list.size();i++){
				Map map1=(Map)list.get(i);				
				map1.put("hpzlmc",this.systemDao.getCodeValue("030107",map1.get("HPZL")==null?"":map1.get("HPZL").toString()));
				map1.put("hpysmc",this.systemDao.getCodeValue("031001",map1.get("HPYS")==null?"":map1.get("HPYS").toString()));
			}
			return list;
		}
}
