package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;





import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.bean.CombineCondition;
import com.sunshine.monitor.system.analysis.clickhouse.bean.ClickHouseConstant;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
import com.sunshine.monitor.system.analysis.clickhouse.utils.ClickHouseSqlUtils;
import com.sunshine.monitor.system.analysis.dao.CombineAnalysisDao;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.dao.StudyDao;
import com.sunshine.monitor.system.analysis.dao.TimeSpaceCombineDao;
import com.sunshine.monitor.system.analysis.service.TimeSpaceCombineManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.query.dao.QueryListDao;

@Service("timeSpaceCombineManager")
@Transactional
public class TimeSpaceCombineManagerImpl implements TimeSpaceCombineManager {

	@Autowired
	private TimeSpaceCombineDao timeSpaceCombineDao;
	
	@Autowired
	private CombineAnalysisDao combineAnalysisDao;
	
	@Autowired
	private SystemDao systemDao;
	
	@Autowired
	private GateDao gateDao;
	
	@Autowired
	private DirectDao directDao;
	
	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	@Autowired
	@Qualifier("studyDao")
	private StudyDao studyDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos clickHouseRepos;
	
	/**
	 * 频繁出入/区域碰撞 -- 共用
	 */
	private static String tableName = "yp_passrec_area";
	
	
	public Map<String, Object> supposeAnalysis(Map filter, CombineCondition cc) throws Exception {
		String consql = "";
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += " and hphm = '"+cc.getHphm()+"' ";
		}
		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and hpys = '"+cc.getHpys()+"' ";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and hpzl = '"+cc.getHpzl()+"' ";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and csys = '"+cc.getCsys()+"' ";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {
			//consql += " and cllx = '"+cc.getCllx()+"' ";
			consql += " and hpzl = '"+cc.getCllx()+"' ";
		}
		JSONArray ja = JSONArray.fromObject(cc.getCondition());
		for(int i = 0; i < ja.size(); i++) {
			String kdbhs = (String) ja.getJSONObject(i).get("kdbh");
			String fxbhs = (String) ja.getJSONObject(i).get("fxbh");
			String kssj = (String) ja.getJSONObject(i).get("kssj");
			String jssj = (String) ja.getJSONObject(i).get("jssj");

			if(i == 0) {
				consql += " and ";
			}
			consql += " (1=1 ";
			if(kdbhs != null && !"".equals(kdbhs)) {				
				consql += "and kdbh in ("+kdbhs+") ";
			}
			
			if(fxbhs != null && !"".equals(fxbhs)) {
				//consql += " and fxbh in ("+fxbhs+")";
				String[] fxbhArray = kdbhs.split(",");
				for(int j= 0 ;j<fxbhArray.length; j++) {
					if(j == 0) {
						consql += "and fxbh in (";
					}
					consql += "'" + fxbhArray[j] +"'";
						if(j < fxbhArray.length-1) {
							consql += ",";
					}
					
				}
				consql +=")";
			}
			
			if(kssj != null && !"".equals(kssj)) {
				consql += " and gcsj >= '"+kssj+"' ";
			}
			
			if(jssj != null && !"".equals(jssj)) {
				consql += " and gcsj<= '"+jssj+"' ";
			}
			consql += ")";
			if(i<ja.size()-1) {
				consql += " or ";
			}
		}
		//轨迹统计
		if("1".equals(cc.getTjlx())) {
			Map<String, Object> countMap = this.timeSpaceCombineDao.trackCount(filter, consql);
			return countMap;
		}
		
		//轨迹信息
		Map<String,Object> map =  this.timeSpaceCombineDao.combineAnalysis(filter, consql);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("hpys").toString()));
			result.put("KDMC", this.gateDao.getGateName(result.get("kdbh").toString()));
			result.put("FXMC", this.gateDao.getDirectName(result.get("fxbh").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("kdbh").toString().substring(0, 6)));
			list.add(result);
		}
		map.put("rows", list);
		return map;
	}
	
	public Map<String, Object> supposeAnalysisByOracle(Map filter,
			CombineCondition cc) throws Exception {
		String consql = "";
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += " and hphm = '"+cc.getHphm()+"' ";
		}
		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and hpys = '"+cc.getHpys()+"' ";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and hpzl = '"+cc.getHpzl()+"' ";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and csys = '"+cc.getCsys()+"' ";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {
			//consql += " and cllx = '"+cc.getCllx()+"' ";
			consql += " and hpzl = '"+cc.getCllx()+"' ";
		}
		JSONArray ja = JSONArray.fromObject(cc.getCondition());
		
		String temp = consql;
		String[] conditionArray = new String[ja.size()];
		for(int i = 0; i < ja.size(); i++) {
			String kdbhs = (String) ja.getJSONObject(i).get("kdbh");
			String fxbhs = (String) ja.getJSONObject(i).get("fxbh");
			String kssj = (String) ja.getJSONObject(i).get("kssj");
			String jssj = (String) ja.getJSONObject(i).get("jssj");
			conditionArray[i] = " select distinct hphm from "+Constant.SCS_PASS_TABLE+" where 1=1 "+temp;
			if(i == 0) {
				consql += " and ";
			}
			consql += " (1=1 ";
			if(kdbhs != null && !"".equals(kdbhs)) {				
				consql += " and kdbh in("+kdbhs+") ";
				conditionArray[i] += " and kdbh in("+kdbhs+") ";
			}
			
			if(fxbhs != null && !"".equals(fxbhs)) {
				//consql += " and fxbh in ("+fxbhs+")";
				String[] fxbhArray = fxbhs.split(",");
				for(int j= 0 ;j<fxbhArray.length; j++) {
					if(j == 0) {
						consql += "and fxbh in(";
						conditionArray[i] += "and fxbh in(";
					}
					consql += "'" + fxbhArray[j] +"'";
					conditionArray[i] += "'" + fxbhArray[j] +"'";
						if(j < fxbhArray.length-1) {
							consql += ",";
							conditionArray[i] += ",";
					}
					
				}
				consql +=")";
				conditionArray[i] += ")";
			}
			
			
			if(kssj != null && !"".equals(kssj)) {
				consql += " and gcsj >= '"+kssj+"' ";
				conditionArray[i] += " and gcsj >= '"+kssj+"' ";
			}
			
			if(jssj != null && !"".equals(jssj)) {
				consql += " and gcsj<= '"+jssj+"' ";
				conditionArray[i] += " and gcsj <= '"+jssj+"' ";
			}
			consql += ")";
			if(i<ja.size()-1) {
				consql += " or ";
			}
		}
		
		//轨迹统计
		if("1".equals(cc.getTjlx())) {
			Map<String, Object> countMap = this.timeSpaceCombineDao.supposeAnalysisTrack(filter, consql, conditionArray);
			return countMap;
		}
		
		//轨迹信息
		Map<String,Object> map =  this.timeSpaceCombineDao.supposeAnalysis(filter, consql, conditionArray);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("hpys").toString()));
			result.put("KDMC", this.gateDao.getGateName(result.get("kdbh").toString()));
			result.put("FXMC", this.gateDao.getDirectName(result.get("fxbh").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("kdbh").toString().substring(0, 6)));
			list.add(result);
		}
		map.put("rows", list);
		return map;
	}


	public Map<String, Object> offenInOut(Map filter, CombineCondition cc)
			throws Exception {
		String consql = "";
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += " and hphm = '"+cc.getHphm()+"' ";
		}
		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and hpys = '"+cc.getHpys()+"' ";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and hpzl = '"+cc.getHpzl()+"' ";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and csys = '"+cc.getCsys()+"' ";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {
			//consql += " and cllx = '"+cc.getCllx()+"' ";
			consql += " and hpzl = '"+cc.getCllx()+"' ";
		}
		
		JSONObject jo = JSONObject.fromObject(cc.getCondition());
		String kssj = jo.getString("kssj");
		String jssj = jo.getString("jssj");
		String kdbhs = jo.getString("kdbh");
		String fxbhs = jo.getString("fxbh");
		
		if(kdbhs != null && !"".equals(kdbhs)) {
			String[] kdbhArray = kdbhs.split(",");
			for(int j= 0 ;j<kdbhArray.length; j++) {
				if(j == 0) {
					consql += " and kdbh in(";
				}
					consql += "'" + kdbhArray[j] +"'";
					if(j < kdbhArray.length-1) {
						consql += ",";
				}
				
			//consql += " and kdbh in ("+kdbhs+")";
			}
			consql +=")";
		}
		
		
		if(fxbhs != null && !"".equals(fxbhs)) {
			//consql += " and fxbh in ("+fxbhs+")";
			String[] fxbhArray = fxbhs.split(",");
			for(int j= 0 ;j<fxbhArray.length; j++) {
				if(j == 0) {
					consql += " and fxbh in(";
				}
					consql += "'" + fxbhArray[j] +"'";
					if(j < fxbhArray.length-1) {
						consql += ",";
				}
				
			}
			consql +=")";
		}
		
		if(kssj != null && !"".equals(kssj)) {
			consql += " and gcsj >= '"+kssj+"' ";
		}
		if(jssj != null && !"".equals(jssj)) {
			consql += " and gcsj <= '"+jssj+"' ";
		}
		
		
		//统计信息
		if ("1".equals(cc.getTjlx())) {
			consql += " )";
		if(cc.getPl() != null && !"".equals(cc.getPl())) {
			consql += " where cs >= "+cc.getPl();
		}
		
		return this.timeSpaceCombineDao.offenInOut(filter, consql);
		}
		
		//轨迹信息
		Map<String,Object> map =  this.timeSpaceCombineDao.combineAnalysis(filter, consql);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("hpys").toString()));
			result.put("KDMC", this.gateDao.getGateName(result.get("kdbh").toString()));
			result.put("FXMC", this.gateDao.getDirectName(result.get("fxbh").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("kdbh").toString().substring(0, 6)));
			list.add(result);
		}
		map.put("rows", list);
		return map;
		
	}

	public Map<String, Object> districtAnalysis(Map filter, CombineCondition cc)
			throws Exception {
		String consql = "";
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += " and hphm = '"+cc.getHphm()+"' ";
		}
		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and hpys = '"+cc.getHpys()+"' ";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and hpzl = '"+cc.getHpzl()+"' ";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and csys = '"+cc.getCsys()+"' ";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {
			//consql += " and cllx = '"+cc.getCllx()+"' ";
			consql += " and hpzl = '"+cc.getCllx()+"' ";
		}
		JSONArray ja = JSONArray.fromObject(cc.getCondition());
		for(int i = 0; i < ja.size(); i++) {
			String kdbhs = (String) ja.getJSONObject(i).get("kdbh");
			String kssj = (String) ja.getJSONObject(i).get("kssj");
			String jssj = (String) ja.getJSONObject(i).get("jssj");
			String fxbhs = (String) ja.getJSONObject(i).get("fxbh");
			if(i == 0) {
				consql += " and ";
			}
			consql += " (1=1 ";
			if(kdbhs != null && !"".equals(kdbhs)) {
				String[] kdbhArray = kdbhs.split(",");
				for(int j= 0 ;j<kdbhArray.length; j++) {
					if(j == 0) {
						consql += " and kdbh in(";
					}
					consql += "'" + kdbhArray[j] +"'";
					if(j < kdbhArray.length-1) {
						consql += ",";
					}					
				}
				consql +=")";
			}
			if(fxbhs != null && !"".equals(fxbhs)) {
				String[] fxbhArray = fxbhs.split(",");
				for(int j= 0 ;j<fxbhArray.length; j++) {
					if(j == 0) {
						consql += " and fxbh in(";
					}
						consql += "'" + fxbhArray[j] +"'";
						if(j < fxbhArray.length-1) {
							consql += ",";
					}
					
				}
				consql +=")";
			}
			
			if(kssj != null && !"".equals(kssj)) {
				consql += " and gcsj >= '"+kssj+"' ";
			}
			
			if(jssj != null && !"".equals(jssj)) {
				consql += " and gcsj<= '"+jssj+"' ";
			}
			consql += ")";
			if(i<ja.size()-1) {
				consql += " or ";
			}
		}
		//轨迹统计
		if("1".equals(cc.getTjlx())) {
			Map<String, Object> countMap = this.timeSpaceCombineDao.trackCount(filter, consql);
			return countMap;
		}
		//轨迹信息
		Map<String,Object> map =  this.timeSpaceCombineDao.combineAnalysis(filter, consql);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator it = queryList.iterator(); it.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) it.next();
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("hpys").toString()));
			result.put("KDMC", this.gateDao.getGateName(result.get("kdbh").toString()));
			result.put("FXMC", this.gateDao.getDirectName(result.get("fxbh").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("kdbh").toString().substring(0, 6)));
			list.add(result);
		}
		map.put("rows", list);
		return map;
	}
	
	
	/**
	 * 大数据
	 */
	public Map<String, Object> supposeAnalysisByScsDB(Map filter, CombineCondition cc) throws Exception {
		String consql = "";
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += "and (hphm= '"+cc.getHphm()+"' )";
		}
		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and (hpys = '"+cc.getHpys()+"' )";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and (hpzl = '"+cc.getHpzl()+"' )";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and (csys = '"+cc.getCsys()+"' )";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {
			//consql += " and (cllx = '"+cc.getCllx()+"' )";
			consql += " and (hpzl = '"+cc.getCllx()+"' )";
		}
		JSONArray ja = JSONArray.fromObject(cc.getCondition());
		System.out.println("jasize:"+ja.size());
		String temp = consql;
		String[] conditionArray = new String[ja.size()];
		for(int i = 0; i < ja.size(); i++) {
			String kdbhs = (String) ja.getJSONObject(i).get("kdbh");
			String fxbhs = (String) ja.getJSONObject(i).get("fxbh");
			String kssj = (String) ja.getJSONObject(i).get("kssj");
			String jssj = (String) ja.getJSONObject(i).get("jssj");
			conditionArray[i] = " select hphm,hpzl,hpys from "+Constant.SCS_PASS_TABLE+" where 1=1 "+temp;
			if(i == 0) {
				consql += " and (";
			}
			consql += " (1=1 ";
			if(kdbhs != null && !"".equals(kdbhs)) {				
				consql += " and kdbh in("+kdbhs+") ";
				conditionArray[i] += " and kdbh in ("+kdbhs+") ";
			}
			
			if(fxbhs != null && !"".equals(fxbhs)) {
				String[] fxbhArray = fxbhs.split(",");
				for(int j= 0 ;j<fxbhArray.length; j++) {
					if(j == 0) {
						consql += "and fxbh in (";
						conditionArray[i] += "and fxbh in (";
					}
					consql += "'" + fxbhArray[j] +"'";
					conditionArray[i] += "'" + fxbhArray[j] +"'";
						if(j < fxbhArray.length-1) {
							consql += ",";
							conditionArray[i] += ",";
					}
					
				}
				consql +=")";
				conditionArray[i] += ")";
			}
			
			
			if(kssj != null && !"".equals(kssj)) {
				consql += " and gcsj >= str_to_date('"+kssj+"','%Y-%m-%d %H:%i:%s') ";
				conditionArray[i] += " and gcsj >= str_to_date('"+kssj+"','%Y-%m-%d %H:%i:%s') ";
			}
			
			if(jssj != null && !"".equals(jssj)) {
				consql += " and gcsj<= str_to_date('"+jssj+"','%Y-%m-%d %H:%i:%s') ";
				conditionArray[i] += " and gcsj <= str_to_date('"+jssj+"','%Y-%m-%d %H:%i:%s') ";
			}
			conditionArray[i] += " group by hphm,hpzl,hpys ";
			consql += ")";
			if(i<ja.size()-1) {
				consql += " or ";
			}
		}
		consql += ") ";
		//轨迹统计
		if("1".equals(cc.getTjlx())) {
			Map map = this.combineAnalysisDao.combineAnalysisByScsDb(filter, consql, conditionArray);
			List queryList =  (List) map.get("rows");
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for (Iterator i = queryList.iterator(); i.hasNext();) {
				Map<String, Object> result = (Map<String, Object>) i.next();
				result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
				result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
				list.add(result);
			}
			map.put("rows", list);
			return map;
		}
		Map map = this.combineAnalysisDao.supposeGjList(filter, conditionArray);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
			result.put("KDMC", this.gateDao.getGateName(result.get("KDBH").toString()));
			result.put("FXMC", this.gateDao.getDirectName(result.get("FXBH").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
			list.add(result);
		}
		map.put("rows", list);
		return map;
	}
	public Map<String, Object> offenInOutByScsDB(Map filter, CombineCondition cc,String tablename)
			throws Exception {
		
		String consql = "";
		String cssql = "" ;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += "and t.hphm = '"+cc.getHphm()+"' ";
		}		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and t.hpys = '"+cc.getHpys()+"' ";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and t.hpzl = '"+cc.getHpzl()+"' ";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and t.csys = '"+cc.getCsys()+"' ";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {			
			consql += " and t.hpzl = '"+cc.getCllx()+"' ";
		}
		
		JSONObject jo = JSONObject.fromObject(cc.getCondition());
		String kssj = jo.getString("kssj");
		String jssj = jo.getString("jssj");
		String kssd1 = jo.getString("kssd1");
		String jssd1 = jo.getString("jssd1");
		String kssd2 = jo.getString("kssd2");
		String jssd2 = jo.getString("jssd2");
		String kssd3 = jo.getString("kssd3");
		String jssd3 = jo.getString("jssd3");
		String times = jo.getString("times");
		String kdbhs = jo.getString("kdbh");
		
		if(kdbhs != null && !"".equals(kdbhs)) {
			String[] kdbhArray = kdbhs.split(",");
			for(int j= 0 ;j<kdbhArray.length; j++) {
				if(j == 0) {
					consql += "and t.kkbh in (";
				}
					consql += "'" + kdbhArray[j] +"'";
					if(j < kdbhArray.length-1) {
						consql += ",";
				}
			}
			consql +=") ";
		}
		
		if(kssj != null && !"".equals(kssj)) {
			consql += " and gcsj >= '"+kssj.trim()+" 00:00:00' ";
		}
		if(jssj != null && !"".equals(jssj)) {
			consql += " and gcsj <= '"+jssj.trim()+" 23:59:59' ";
		}
		
		consql += " AND (";
		if(kssd1 != null && !"".equals(kssd1)&& !"undefined".equals(kssd1)) {
			consql += " (formatDateTime(gcsj,'%H:%M') >= '"+kssd1.trim()+"' ";
		}
		if(jssd1 != null && !"".equals(jssd1)&& !"undefined".equals(jssd1)) {
			consql += " and formatDateTime(gcsj,'%H:%M') <= '"+jssd1.trim()+"')";
		}
		if(kssd2 != null && !"".equals(kssd2)&& !"undefined".equals(kssd2)) {
			consql += " or (formatDateTime(gcsj,'%H:%M') >= '"+kssd2.trim()+"' ";
		}
		if(jssd2 != null && !"".equals(jssd2)&& !"undefined".equals(jssd2)) {
			consql += " and formatDateTime(gcsj,'%H:%M') <= '"+jssd2.trim()+"')";
		}
		if(kssd3 != null && !"".equals(kssd3)&& !"undefined".equals(kssd3)) {
			consql += " or (formatDateTime(gcsj,'%H:%M') >= '"+kssd3.trim()+"' ";
		}
		if(jssd3 != null && !"".equals(jssd3)&& !"undefined".equals(jssd3)) {
			consql += " and formatDateTime(gcsj,'%H:%M') <= '"+jssd3.trim()+"')";
		}
		consql += ")";
		if(times != null && !"".equals(times)&& !"undefined".equals(times)) {
			cssql = " and  b.cs >='"+times+"' ";
		}
		
		//轨迹统计
		if("1".equals(cc.getTjlx())) {
			//Map map =  this.combineAnalysisDao.offenInOutByScsDb(filter, consql,cc.getPl(),tablename);
			Map map =  this.combineAnalysisDao.offenInOutByScsDb(filter, consql,cc.getPcgz(),tablename,cssql);
			List<Map<String,Object>> queryList =  (List) map.get("rows");
			if(queryList.size() == 0) {
				return map;
			}	
			List<CarKey> cars = new ArrayList<CarKey>();
			for(Map<String,Object> m:queryList){
				CarKey car = new CarKey();	
	        	car.setCarNo(m.get("HPHM").toString());
	        	car.setCarType(m.get("HPZL").toString());
	        	cars.add(car);
	        }
			//Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
			Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for (Iterator i = queryList.iterator(); i.hasNext();) {
				Map<String, Object> result = (Map<String, Object>) i.next();
				result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
				result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
				result.put("WFXX", wfxx.get(new CarKey(result.get("HPHM").toString(), result.get("HPZL").toString())));
				list.add(result);
			}
			map.put("rows", list);
			//map.put("pagesign", "0");
			return map;
		}
		
		//Map map = this.combineAnalysisDao.queryGjList(filter, consql);
		Map map = this.clickHouseRepos.queryGjList(filter, consql);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("GCSJ", sdf.format(result.get("GCSJ")));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
			result.put("KDMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
			result.put("FXMC", this.directDao.getOldDirectName(result.get("FXBH").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
			list.add(result);
		}
		map.put("rows", list);
		return map;
		
	}

	public String getSql(Map<String, Object> filter, CombineCondition cc,String tablename)throws Exception {
		// 拼接sql与参数预编译
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer("SELECT hphm, hpzl, cs, kks, xzqs, ts FROM (");
		sb.append("SELECT hphm,hpzl,SUM (total) cs,COUNT (DISTINCT kkbh) kks,COUNT (DISTINCT xzqh) xzqs," +
				"COUNT (DISTINCT ldate) AS ts FROM ");
		sb.append(tablename);
		sb.append(" where 1 = 1 ");
		
		JSONObject jo = JSONObject.fromObject(cc.getCondition());
		String kssj = jo.getString("kssj");
		String jssj = jo.getString("jssj");
		String kssd1 = jo.getString("kssd1");
		String jssd1 = jo.getString("jssd1");
		String kssd2 = jo.getString("kssd2");
		String jssd2 = jo.getString("jssd2");
		String kssd3 = jo.getString("kssd3");
		String jssd3 = jo.getString("jssd3");
		String times = jo.getString("times");
		String kdbhs = jo.getString("kdbh");
//		String fxbhs = jo.getString("fxbh");
		if(kdbhs != null && !"".equals(kdbhs)) {
			String[] kdbhArray = kdbhs.split(",");
			String kkbhSql = "";
			for(int j= 0 ;j<kdbhArray.length; j++) {
				kkbhSql += "'"+kdbhArray[j]+"'"; 
				if(j < kdbhArray.length-1) {
					kkbhSql += ",";
				}
			}
			sb.append("and kkbh  in ("+ kkbhSql +") ");
		}

		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			sb.append("and hpzl = '"+cc.getHpzl()+"' ");
		}			
		if(kssj != null && !"".equals(kssj)) {
			sb.append("and gcsj >= '"+kssj+"' ");
		}
		if(jssj != null && !"".equals(jssj)) {
			sb.append("and gcsj <= '"+jssj+"' ");
		}
		sb.append(" AND ( 1=1 ");
		if(kssd1 != null && !"".equals(kssd1)&& !"undefined".equals(kssd1)) {
			sb.append(" or (ltime >= '"+kssd1+"' ");
		}
		if(jssd1 != null && !"".equals(jssd1)&& !"undefined".equals(jssd1)) {
			sb.append(" and ltime <= '"+jssd1+"')");
		}
		if(kssd2 != null && !"".equals(kssd2)&& !"undefined".equals(kssd2)) {
			sb.append(" or (ltime >= '"+kssd2+"' ");
		}
		if(jssd2 != null && !"".equals(jssd2)&& !"undefined".equals(jssd2)) {
			sb.append(" and ltime <= '"+jssd2+"')");
		}
		if(kssd3 != null && !"".equals(kssd3)&& !"undefined".equals(kssd3)) {
			sb.append(" or (ltime >= '"+kssd3+"' ");
		}
		if(jssd3 != null && !"".equals(jssd3)&& !"undefined".equals(jssd3)) {
			sb.append(" and ltime <= '"+jssd3+"')");
		}
		sb.append(")");
		sb.append(" GROUP BY hphm,hpzl ");
		sb.append(" ) a1 ");
		sb.append(" where 1=1 ");
		if(times!= null && !"".equals(times)) {
			sb.append(" and cs >= "+times+" ");
		}
		sb.append("ORDER BY	cs DESC");
		return sb.toString();
	}
	
	public List<Map<String, Object>> offenInOutByScsDBExt(Map<String, Object> filter, CombineCondition cc)
			throws Exception {
		//String sql = getSql(filter, cc, tableName);
		String sql = ClickHouseSqlUtils.getOffenInOutSql(filter, cc, ClickHouseConstant.PASS_TABLE_NAME);		
		//轨迹统计
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>() ;
		if("1".equals(cc.getTjlx())) {
			//list =  this.combineAnalysisDao.offenInOutByScsDbExt(filter, sql);
			list =  this.clickHouseRepos.offenInOutByScsDbExt(filter, sql);
			if(list.size() == 0) {
				return list;
			}	
			for(int i = 0; i < list.size(); i++){
				Map<String, Object> map = (Map<String, Object>) list.get(i);
				map.put("HPZLMC", this.systemDao.getCodeValue("030107",	map.get("HPZL") == null ? "" : map.get("HPZL").toString()));
	        }
			return list;
		}
		
		return list;
	}
	
	public int queryTotals(CombineCondition cc, Map<String, Object> filter)throws Exception {
		//String sql = getSql(filter, cc, tableName);
		//return combineAnalysisDao.getTotals(sql);

		String sql = ClickHouseSqlUtils.getOffenInOutSql(filter, cc, ClickHouseConstant.PASS_TABLE_NAME);
		return this.clickHouseRepos.getTotal(sql);
	}
	public Map<String, Object> districtAnalysisByScsDB(Map filter, CombineCondition cc, String tablename)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//分页标志位判断是否为分页
		String consql = "";
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += "and (hphm= '"+cc.getHphm()+"' )";
		}
		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and (hpys = '"+cc.getHpys()+"' )";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and (hpzl = '"+cc.getHpzl()+"' )";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and (csys = '"+cc.getCsys()+"' )";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {
			//consql += " and (cllx = '"+cc.getCllx()+"' )";
			consql += " and (hpzl = '"+cc.getCllx()+"' )";
		}
		JSONArray ja = JSONArray.fromObject(cc.getCondition());
		//System.out.println("jasize:"+ja.size());
		String temp = consql;
		String[] conditionArray = new String[ja.size()];
		for(int i = 0; i < ja.size(); i++) {
			String kdbhs = (String) ja.getJSONObject(i).get("kdbh");
			String fxbhs = (String) ja.getJSONObject(i).get("fxbh");
			String kssj = (String) ja.getJSONObject(i).get("kssj");
			String jssj = (String) ja.getJSONObject(i).get("jssj");
			conditionArray[i] = " select distinct concat(kkbh,'|',fxlx,'|',hphm,'|',hpzl,'|',toString(gcsj)) as gcxh,hphm,hpzl,hpys,kkbh as kdbh,gcsj,tp1,fxlx as fxbh from " + ClickHouseConstant.PASS_TABLE_NAME + " where 1=1 "+temp;
			if(i == 0) {
				consql += " and (";
			}
			consql += " (1=1 ";
			if(kdbhs != null && !"".equals(kdbhs)) {	
				kdbhs = "'" + kdbhs.replace(",", "','") + "'";
				consql += " and kkbh in("+kdbhs+") ";
				conditionArray[i] += " and kkbh in ("+kdbhs+") ";
			}
			
			if(fxbhs != null && !"".equals(fxbhs)) {
				String[] fxbhArray = fxbhs.split(",");
				for(int j= 0 ;j<fxbhArray.length; j++) {
					if(j == 0) {
						consql += "and concat(kkbh,fxlx) in (";
						conditionArray[i] += "and concat(kkbh,fxlx) in (";
					}
					consql += "'" + fxbhArray[j] +"'";
					conditionArray[i] += "'" + fxbhArray[j] +"'";
						if(j < fxbhArray.length-1) {
							consql += ",";
							conditionArray[i] += ",";
					}
					
				}
				consql +=")";
				conditionArray[i] += ")";
			}
			
			
			if(kssj != null && !"".equals(kssj)) {
				consql += " and gcsj >= '"+kssj+"' ";
				conditionArray[i] += " and gcsj >= '"+kssj+"' ";
			}
			
			if(jssj != null && !"".equals(jssj)) {
				consql += " and gcsj<= '"+jssj+"') ";
				conditionArray[i] += " and gcsj <= '"+jssj+"' ";
			}
			
			consql += ")";
			if(i<ja.size()-1) {
				consql += " or ";
			}
		}
		consql += ") ";
		//轨迹统计
		if("1".equals(cc.getTjlx())) {
			//Map map = this.combineAnalysisDao.combineAnalysisByScsDb(filter, consql, conditionArray);
			//Map map = this.combineAnalysisDao.districtbyScsDb(filter, consql, conditionArray, tablename);
			Map map = this.clickHouseRepos.districtbyScsDb(filter, consql, conditionArray, tablename);
			List<Map<String,Object>> queryList =  (List) map.get("rows");
			if(queryList.size() == 0) {
				return map;
			}
			List<CarKey> cars = new ArrayList<CarKey>();
			for(Map<String,Object> m:queryList){
				CarKey car = new CarKey();	
	        	car.setCarNo(m.get("HPHM").toString());
	        	car.setCarType(m.get("HPZL").toString());
	        	cars.add(car);
	        }
			
			//Map<CarKey,Integer> wfxx = new HashMap<>();
			Map<CarKey,Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for (Iterator i = queryList.iterator(); i.hasNext();) {
				Map<String, Object> result = (Map<String, Object>) i.next();
				result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
				result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
				result.put("WFXX", wfxx.get(new CarKey(result.get("HPHM").toString(), result.get("HPZL").toString())));
				list.add(result);
			}
			map.put("rows", list);
			map.put("pagesign", "0");
			map.put("allgates", this.systemDao.getAllGate());
			return map;
		}
		
		if("3".equals(cc.getTjlx())) {
			//Map map = this.combineAnalysisDao.combineAnalysisByScsDb(filter, consql, conditionArray);
			//Map map = this.combineAnalysisDao.supposeGjList(null, conditionArray);
			Map map = this.clickHouseRepos.supposeGjList(null, conditionArray);
			List<Map<String,Object>> queryList =  (List) map.get("rows");
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for (Iterator i = queryList.iterator(); i.hasNext();) {
				Map<String, Object> result = (Map<String, Object>) i.next();
				result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
				result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
				String kdbh = (String) result.get("KDBH");
				Map gateByKDBH = this.systemDao.getGateByKDBH(kdbh);
				if(gateByKDBH!=null)
				{
					result.put("KDMC", gateByKDBH.get("KDMC"));
					result.put("KD_X", gateByKDBH.get("X"));
					result.put("KD_Y", gateByKDBH.get("Y"));
				}
				//result.put("KDMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
				list.add(result);
			}
			map.put("rows", list);
			map.put("allgates", this.systemDao.getAllGate());
			return map;
		}
		
		//Map map = this.combineAnalysisDao.supposeGjList(filter, conditionArray);
		Map map = this.clickHouseRepos.supposeGjList(filter, conditionArray);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("GCSJ", sdf.format(result.get("GCSJ")));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
			result.put("KDMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
			result.put("FXMC", this.directDao.getOldDirectName(result.get("FXBH").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
			list.add(result);
		}
		map.put("rows", list);
		map.put("allgates", this.systemDao.getAllGate());
		return map;
	}
	
	/**
	 * <first written> licheng 2016-5-25 区域碰撞去掉原有区域数、卡口数、天数、次数，改为统计各个域出现次数
	 * @param filter
	 * @param cc
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> districtAnalysis2ByScsDB(Map filter, CombineCondition cc, String tablename)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String consql = "";
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += "and (hphm= '"+cc.getHphm()+"' )";
		}
		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and (hpys = '"+cc.getHpys()+"' )";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and (hpzl = '"+cc.getHpzl()+"' )";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and (csys = '"+cc.getCsys()+"' )";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {
			consql += " and (cllx = '"+cc.getCllx()+"' )";
		}
		JSONArray ja = JSONArray.fromObject(cc.getCondition());
		String temp = consql;
		String[] conditionArray = new String[ja.size()];
		// 定义区域字段
		String[] areas = new String[ja.size()];
		for(int j = 0;j < ja.size(); j++){
			areas[j] = " area"+(j+1)+"";
		}
		for(int i = 0; i < ja.size(); i++) {
			String kdbhs = (String) ja.getJSONObject(i).get("kdbh");
			String fxbhs = (String) ja.getJSONObject(i).get("fxbh");
			String kssj = (String) ja.getJSONObject(i).get("kssj");
			String jssj = (String) ja.getJSONObject(i).get("jssj");
			StringBuffer areaStr = new StringBuffer();
			// 拼接area字符串
			for(int k = 0; k < ja.size(); k++){
				if(k == i){
					areaStr.append(",count(1) as " + areas[k]);
				}else {
					areaStr.append(",0 as " + areas[k]);
				}
			}
			conditionArray[i] = " select hphm,hpzl,hpys"+areaStr.toString()+" from " + ClickHouseConstant.PASS_TABLE_NAME + " where 1=1 "+temp;
			if(i == 0) {
				consql += " and (";
			}
			consql += " (1=1 ";
			if(kdbhs != null && !"".equals(kdbhs)) {
				kdbhs = "'" + kdbhs.replace(",", "','") + "'";
				consql += " and kkbh in("+kdbhs+") ";
				conditionArray[i] += " and kkbh in ("+kdbhs+") ";
			}
			if(fxbhs != null && !"".equals(fxbhs)) {
				fxbhs = "'" + fxbhs.replace(",", "','") + "'";
				consql += " and concat(kkbh,fxlx) in("+fxbhs+") ";
				conditionArray[i] += " and concat(kkbh,fxlx) in ("+fxbhs+") ";
			}
			if(kssj != null && !"".equals(kssj)) {
				consql += " and gcsj >= '"+kssj+"' ";
				conditionArray[i] += " and gcsj >= '"+kssj+"' ";
			}
			
			if(jssj != null && !"".equals(jssj)) {
				consql += " and gcsj<= '"+jssj+"') ";
				conditionArray[i] += " and gcsj <= '"+jssj+"' ";
			}
			conditionArray[i] += " group by hphm,hpzl,hpys ";
			if(i<ja.size()-1) {
				consql += " or ";
			}
		}
		consql += ") ";
		//轨迹统计
		if("1".equals(cc.getTjlx())) {
			Map map = this.combineAnalysisDao.district2byScsDb(filter, consql, conditionArray, tablename);
			List<Map<String,Object>> queryList =  (List) map.get("rows");
			if(queryList.size() == 0) {
				return map;
			}
			List<CarKey> cars = new ArrayList<CarKey>();
			for(Map<String,Object> m:queryList){
				CarKey car = new CarKey();	
	        	car.setCarNo(m.get("HPHM")==null?"":m.get("HPHM").toString());
	        	car.setCarType(m.get("HPZL").toString());
	        	cars.add(car);
	        }
			//Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
			Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for (Iterator i = queryList.iterator(); i.hasNext();) {
				Map<String, Object> result = (Map<String, Object>) i.next();
				result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
				result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
				result.put("WFXX", wfxx.get(new CarKey(result.get("hphm").toString(), result.get("hpzl").toString())));
				list.add(result);
			}
			map.put("rows", list);
			map.put("pagesign", "0");
			map.put("allgates", this.systemDao.getAllGate());
			
			return map;
		}
		
		//Map map = this.combineAnalysisDao.queryGjList(filter, consql);
		Map map = this.clickHouseRepos.queryGjList(filter, consql);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("GCSJ", sdf.format(result.get("GCSJ")));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
			result.put("KDMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
			result.put("FXMC", this.directDao.getOldDirectName(result.get("FXBH").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
			list.add(result);
		}
		map.put("rows", list);
		map.put("allgates", this.systemDao.getAllGate());
		return map;
	}
	
	public List<Map<String, Object>> districtAnalysis2ByScsDBExt(Map filter, CombineCondition cc, String tablename)
	throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 拼接sql与参数预编译
		List<String> params = new ArrayList<String>();
		String consql = "";
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += "and (hphm= '"+cc.getHphm()+"' )";
		}
		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and (hpys = '"+cc.getHpys()+"' )";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and (hpzl = '"+cc.getHpzl()+"' )";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and (csys = '"+cc.getCsys()+"' )";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {
			consql += " and (cllx = '"+cc.getCllx()+"' )";
		}
		JSONArray ja = JSONArray.fromObject(cc.getCondition());
		String temp = consql;
		String[] conditionArray = new String[ja.size()];
		// 定义区域字段
		String[] areas = new String[ja.size()];
		String[] qys = new String[ja.size()];
		for(int j = 0;j < ja.size(); j++){
			areas[j] = " area"+(j+1)+"";
			qys[j] = " qy"+(j+1)+"";
			
		}
		for(int i = 0; i < ja.size(); i++) {
			String kdbhs = (String) ja.getJSONObject(i).get("kdbh");
			String fxbhs = (String) ja.getJSONObject(i).get("fxbh");
			String kssj = (String) ja.getJSONObject(i).get("kssj");
			String jssj = (String) ja.getJSONObject(i).get("jssj");
			StringBuffer areaStr = new StringBuffer();
			// 拼接area字符串
			for(int k = 0; k < ja.size(); k++){
				if(k == i){
					areaStr.append(",count(1) AS " + areas[k]);
					areaStr.append(",1 AS " + qys[k]);
				}else {
					areaStr.append(",0 as " + areas[k]);
					areaStr.append(",0 AS " + qys[k]);
				}
			}
			conditionArray[i] = " select hphm,hpzl "+areaStr.toString()+" from " + ClickHouseConstant.PASS_TABLE_NAME + " where 1=1 "+temp;
			if(i == 0) {
				consql += " and (";
			}
			consql += " (1=1 ";
			if(kdbhs != null && !"".equals(kdbhs)) {
				String[] kdbhss = kdbhs.split(",");
				String kkbhSql = "";
				for(int d = 0; d < kdbhss.length; d++){
					kkbhSql += "'"+kdbhss[d]+"'";
					if(d < kdbhss.length-1){
						kkbhSql += ",";
					}
				}
				conditionArray[i] += " and kkbh in ("+ kkbhSql +") ";
			}
			if(kssj != null && !"".equals(kssj)) {
				consql += " and gcsj >= '"+kssj+"' ";
				conditionArray[i] += " and gcsj >= '"+kssj+"' ";
			}
			
			if(jssj != null && !"".equals(jssj)) {
				consql += " and gcsj<= '"+jssj+"' ";
				conditionArray[i] += " and gcsj <= '"+jssj+"' ";
			}
			conditionArray[i] += " group by hphm,hpzl ";
			if(i<ja.size()-1) {
				consql += " or ";
			}
		}
		consql += ") ";
		//轨迹统计
		if("1".equals(cc.getTjlx())) {
			//List list = this.combineAnalysisDao.district2byScsDbExt(filter, consql, conditionArray, tablename,params.toArray());
			List list = this.clickHouseRepos.district2byScsDbExt(filter, consql, conditionArray, tablename,params.toArray());
			
			if(list.size() == 0) {
				return list;
			}
			for(int i = 0; i < list.size(); i++){
				Map map = (Map) list.get(i);
				map.put("HPZLMC", this.systemDao.getCodeValue("030107",
						map.get("HPZL") == null ? "" : map.get("HPZL").toString()));
			}
			
			return list;
	}
	
	List list = this.combineAnalysisDao.queryGjListExt(filter, consql);
	
	for (int i = 0; i < list.size(); i++) {
		Map<String, Object> result = (Map<String, Object>) list.get(i);
		result.put("GCSJ", sdf.format(result.get("GCSJ")));
		result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
		result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
		result.put("KDMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
		result.put("FXMC", this.directDao.getOldDirectName(result.get("FXBH").toString()));
		result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
		list.add(result);
	}
	//map.put("rows", list);
	//map.put("allgates", this.systemDao.getAllGate());
	return list;
	}
	
	public int getTotal2(Map filter, CombineCondition cc, String tablename)throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String consql = "";
		if(cc.getHphm() !=null && !"".equals(cc.getHphm())) {
			consql += "and (hphm= '"+cc.getHphm()+"' )";
		}
		
		if(cc.getHpys()!= null && !"".equals(cc.getHpys())) {
			consql += " and (hpys = '"+cc.getHpys()+"' )";
		}
		if(cc.getHpzl()!= null && !"".equals(cc.getHpzl())) {
			consql += " and (hpzl = '"+cc.getHpzl()+"' )";
		}
		if(cc.getCsys()!= null && !"".equals(cc.getCsys())) {
			consql += " and (csys = '"+cc.getCsys()+"' )";
		}
		if(cc.getCllx() != null && !"".equals(cc.getCllx())) {
			consql += " and (cllx = '"+cc.getCllx()+"' )";
		}
		JSONArray ja = JSONArray.fromObject(cc.getCondition());
		String temp = consql;
		String[] conditionArray = new String[ja.size()];
		// 定义区域字段
		String[] areas = new String[ja.size()];
		String[] qys = new String[ja.size()];
		for(int j = 0;j < ja.size(); j++){
			areas[j] = " area"+(j+1)+"";
			qys[j] = " qy"+(j+1)+"";
			
		}
		for(int i = 0; i < ja.size(); i++) {
			String kdbhs = (String) ja.getJSONObject(i).get("kdbh");
			String fxbhs = (String) ja.getJSONObject(i).get("fxbh");
			String kssj = (String) ja.getJSONObject(i).get("kssj");
			String jssj = (String) ja.getJSONObject(i).get("jssj");
			StringBuffer areaStr = new StringBuffer();
			// 拼接area字符串
			for(int k = 0; k < ja.size(); k++){
				if(k == i){
					areaStr.append(",count(1) AS " + areas[k]);
					areaStr.append(",1 AS " + qys[k]);
				}else {
					areaStr.append(",0 as " + areas[k]);
					areaStr.append(",0 AS " + qys[k]);
				}
			}
			conditionArray[i] = " select hphm,hpzl "+areaStr.toString()+" from " + ClickHouseConstant.PASS_TABLE_NAME + " where 1=1 "+temp;
			if(i == 0) {
				consql += " and (";
			}
			consql += " (1=1 ";
			if(kdbhs != null && !"".equals(kdbhs)) {
				String[] kdbhss = kdbhs.split(",");
				String kkbhSql = "";
				for(int d = 0; d < kdbhss.length; d++){
					kkbhSql += "'"+kdbhss[d]+"'";
					if(d < kdbhss.length-1){
						kkbhSql += ",";
					}
				}
				conditionArray[i] += " and kkbh in ("+ kkbhSql +") ";
			}
			if(kssj != null && !"".equals(kssj)) {
				consql += " and gcsj >= '"+kssj+"' ";
				conditionArray[i] += " and gcsj >= '"+kssj+"' ";
			}
			
			if(jssj != null && !"".equals(jssj)) {
				consql += " and gcsj<= '"+jssj+"' ";
				conditionArray[i] += " and gcsj <= '"+jssj+"' ";
			}
			conditionArray[i] += " group by hphm,hpzl ";
			if(i<ja.size()-1) {
				consql += " or ";
			}
		}
		consql += ") ";
		try{
			if("1".equals(cc.getTjlx())) {
				//return combineAnalysisDao.getTotal2(filter, consql, conditionArray, tablename);
				return clickHouseRepos.getDistrictTotal2(filter, consql, conditionArray, tablename);
			}
			return combineAnalysisDao.getTotal3(filter, consql);
		} catch(Exception e){
			return 0;
		}
	}
	public int alarmRule(Map param) {
		StringBuffer sb = new StringBuffer();
		if(param.get("hphm")!=null && !"".equals(param.get("hphm"))) {
			sb.append(" and hphm = '").append(param.get("hphm")).append("' ");
		}
		if(param.get("hpzl")!=null && !"".equals(param.get("hpzl"))) {
			sb.append(" and hpzl = '").append(param.get("hpzl")).append("' ");
		}
		if(param.get("hpys")!=null && !"".equals(param.get("hpys"))) {
			sb.append(" and hpys = '").append(param.get("hpys")).append("' ");
		}
		
		if(param.get("condition")!=null && !"".equals(param.get("condition"))) {
			JSONObject jo = JSONObject.fromObject(param.get("condition"));
			String kssj = jo.getString("kssj");
			String jssj = jo.getString("jssj");
			String kssd = jo.getString("kssd");
			String jssd = jo.getString("jssd");
			String kdbhs = jo.getString("kdbh");
			String fxbhs = jo.getString("fxbh");
			if(kdbhs != null && !"".equals(kdbhs)) {
				String[] kdbhArray = kdbhs.split(",");
				for(int j= 0 ;j<kdbhArray.length; j++) {
					if(j == 0) {
						sb.append(" and kdbh in (");
					}
					sb.append("'" + kdbhArray[j] +"'");
						if(j < kdbhArray.length-1) {
							sb.append(",");
					}
					
				}
				sb.append(") ");
			}
			if(fxbhs != null && !"".equals(fxbhs)) {
				String[] fxbhArray = fxbhs.split(",");
				for(int j= 0 ;j<fxbhArray.length; j++) {
					if(j == 0) {
						sb.append(" and fxbh in (");
					}
					sb.append("'" + fxbhArray[j] +"'");
						if(j < fxbhArray.length-1) {
							sb.append(",");
					}
					
				}
				sb.append(") ");
			}
			if(kssj != null && !"".equals(kssj)) {				
				sb.append(" and gcsj >= '"+kssj+"' ");
			}
			if(jssj != null && !"".equals(jssj)) {
				sb.append(" and gcsj <= '"+jssj+"' ");
			}
			if(kssd != null && !"".equals(kssd)) {
				sb.append(" and to_char(gcsj,'hh24:mi') >= '"+kssd+"' ");
			}
			if(jssd != null && !"".equals(jssd)) {
				sb.append(" and to_char(gcsj,'hh24:mi') <= '"+jssd+"' ");
			}
		}
		
		if(param.get("mgkk")!=null && !"".equals(param.get("mgkk"))) {
			List<Map<String,Object>> gatelist = this.studyDao.queryGateListByGid(param.get("mgkk").toString());
			if(gatelist != null) {
				String kdbhs = "";
				for(Map m :gatelist) {
					kdbhs += "'"+m.get("KDBH")+"',";
				}
				sb.append(" and kdbh in (").append(kdbhs.substring(0, kdbhs.length()-1)).append(") "); 
			}
			
		}
		
		if(param.get("mgsd")!=null && !"".equals(param.get("mgsd"))) {
			List<Map<String,Object>> timelist = this.studyDao.getTimeGroupByTid(param.get("mgsd").toString());
			if(timelist != null) {
				JSONArray ja  =  JSONArray.fromObject(timelist.get(0).get("TIMEGROUP").toString());
				sb.append(" and ( ");
				for(int i = 0; i < ja.size(); i++) {
					sb.append("( to_char(gcsj,'hh24:mi') >= '").append(ja.getJSONObject(i).get("kssd")).append("' ");
					sb.append("and to_char(gcsj,'hh24:mi') <= '").append(ja.getJSONObject(i).get("jssd")).append("' )");
					if(i<ja.size()-1) {
						sb.append(" or ");
					}
				}
				sb.append(" )");
				
			}
		}
		
		return this.combineAnalysisDao.alarmRule(sb.toString());
	}

}
