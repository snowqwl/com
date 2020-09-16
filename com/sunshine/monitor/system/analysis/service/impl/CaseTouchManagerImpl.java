package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.analysis.bean.CaseTouch;
import com.sunshine.monitor.system.analysis.bean.CaseTouchAnalysis;

import com.sunshine.monitor.system.analysis.dao.CaseTouchDao;
import com.sunshine.monitor.system.analysis.dao.CaseTouchSCSDao;
import com.sunshine.monitor.system.analysis.dao.CaseTouchZYKDao;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.service.CaseTouchManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
@Transactional
@Service("CaseTouchManager")
public class CaseTouchManagerImpl implements CaseTouchManager {
	
	@Autowired
	@Qualifier("CaseTouchDao")
	private CaseTouchDao caseTouchDao;
	
	@Autowired
	@Qualifier("caseTouchSCSDao")
	private CaseTouchSCSDao caseTouchSCSDao;
	
	@Autowired     
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos ClickHouseRepos;
	
	@Autowired
	@Qualifier("caseTouchZYKDao")
	private CaseTouchZYKDao caseTouchZYKDao;
	

	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	private DirectDao directDao;
	
	protected Logger debugLogger = LoggerFactory.getLogger(LikeVehiclesManagerImpl.class);
	
	public String saveCaseTouchGz(CaseTouchAnalysis command)throws Exception{
		return this.caseTouchDao.saveCaseTouchGz(command);
	}
	
	public Map<String,Object> queryCaseTouchRuleList(Map<String,Object> conditions)throws Exception{
		Map<String, Object> map =  this.caseTouchDao.queryCaseTouchRuleList(conditions);		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List queryList = (List) map.get("rows");
		for (int i=0;i<queryList.size();i++) {
			Map  carmap = (Map) queryList.get(i);
			carmap.put("GXSJ", sdf.format(carmap.get("gxsj")));
		}
		map.put("rows", queryList);
		return map;
	}
	
	public CaseTouchAnalysis getEditInformation(String gzbh)throws Exception{
		return this.caseTouchDao.getEditInformation(gzbh);
	}
	
	public int updateCaseTouchGz(CaseTouchAnalysis command)throws Exception{
		return this.caseTouchDao.updateCaseTouchGz(command);
	}
	
	public List getCaseTouchRule() throws Exception{
		return this.caseTouchDao.getCaseTouchRule();
	}
	
	public List getCaseDm(String ywxtmc,String dmlb,String dmmc) throws Exception{
		return this.caseTouchZYKDao.getCaseDm(ywxtmc,dmlb,dmmc);
	}
	
	public Map<String,Object> getCaseForList(Map<String,Object> conditions)throws Exception{		
		Map<String, Object> map =  this.caseTouchDao.getCaseForListNew(conditions);		
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,Object>> queryList = (List<Map<String,Object>>) map.get("rows");
		for (int i=0;i<queryList.size();i++) {
			Map<String,Object>  carmap = (Map<String,Object> ) queryList.get(i);
			String ajbh = carmap.get("AJBH")==null?"":carmap.get("AJBH").toString();
			String ajmc = carmap.get("AJMC")==null?"":carmap.get("AJMC").toString();
			String ajly = carmap.get("AJLY")==null?"":carmap.get("AJLY").toString();
			String zyaq = carmap.get("ZYAQ")==null?"":carmap.get("ZYAQ").toString();
			String fasjcz = carmap.get("fasjcz").toString();
			String fasjcz_date = fasjcz.substring(0, 4)+"-"+fasjcz.substring(4,6)+"-"+fasjcz.substring(6,8)+" "+fasjcz.substring(8,10)+":"
							   + fasjcz.substring(10,12)+":"+fasjcz.substring(12,14);
			String fasjzz = carmap.get("fasjzz").toString();
			String fasjzz_date = fasjzz.substring(0, 4)+"-"+fasjzz.substring(4,6)+"-"+fasjzz.substring(6,8)+" "+fasjzz.substring(8,10)+":"
							   + fasjzz.substring(10,12)+":"+fasjzz.substring(12,14);
			carmap.put("ajbh", ajbh);
			carmap.put("ajmc", ajmc);
			carmap.put("ajly", ajly);
			carmap.put("zyaq", zyaq);
			carmap.put("FASJCZ", fasjcz_date);
			carmap.put("FASJZZ", fasjzz_date);
			carmap.put("FADD_QXMC",this.caseTouchDao.getXzqhName(carmap.get("fadd_qx").toString()));			
		}
		map.put("rows", queryList);
		return map;
	}
	
	
	public Map<String,Object> getCaseTouchAnaiysisDatagrid(Map<String,Object> filter,List<CaseTouch> list)throws Exception{
		Map result =  this.ClickHouseRepos.getCaseTouchAnaiysisDatagrid(filter,list);
		List resultList=(List) result.get("rows");
		List<CarKey> cars = new ArrayList<CarKey>();
		for(int i=0;i<resultList.size();i++){
			Map map=(Map)resultList.get(i);
			CarKey car = new CarKey();	
			car.setCarNo(map.get("hphm").toString());
        	car.setCarType(map.get("hpzl").toString());
        	cars.add(car);
        	map.put("hpzlmc",this.systemDao.getCodeValue("030107",map.get("HPZL").toString()));
		}
		 //获取违法关联数      
		 try {
			//Map<CarKey,Integer> wfxx=this.queryListDao.getViolationCount(cars);
			Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
			    //翻译字段
			    for(int i=0;i<resultList.size();i++){
			        Map<String,Object> map = (Map<String, Object>) resultList.get(i);
					map.put("wfxx", wfxx.get(
							 new CarKey(map.get("hphm").toString(), map.get("hpzl").toString())
				    ));
			    }
		} catch (Exception e) {
			debugLogger.warn("违法库查询",e);
		}
       		
		
		return result;
	}
	
	//根据号牌号码和部分条件获取过车的轨迹列表
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCaseTouchVehList(VehPassrec veh,Map<String, Object> filter) throws Exception {
		Map map =this.caseTouchSCSDao.getCaseTouchVehList(veh, filter);
		List<Map<String,Object>> list =(List<Map<String, Object>>) map.get("rows");
		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
			Map<String,Object> result=i.next();
			result.put("HPYS", systemManager.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("XZQH", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
			result.put("KDBH", this.gateDao.getOldGateName(result.get("KDBH").toString()));
			result.put("FXBH", this.directDao.getOldDirectName(result.get("FXBH").toString()));
		}
		return map;
	}
	
}
