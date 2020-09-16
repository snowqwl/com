package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.clickhouse.bean.ClickHouseConstant;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.dao.XYScrapNaiDao;
import com.sunshine.monitor.system.analysis.service.XYScrapNaiManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.SystemManager;

@Service("xyScrapNaiManager")
public class XYScrapNaiManagerImpl implements XYScrapNaiManager {
	@Autowired
	@Qualifier("xyScrapNaiDao")
	private XYScrapNaiDao xyScrapNaiDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	@Qualifier("scsVehPassrecDao")
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos clickHouseRepos;
	
	@Autowired
	private SystemManager systemManager;
	@Autowired
	private DirectDao directDao;
	
	protected Logger debugLogger = LoggerFactory.getLogger(XYScrapNaiManagerImpl.class);
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> findXyForPage(ScsVehPassrec veh, Map<String, Object> filter) throws Exception {
		//veh.setKdbh("'"+veh.getKdbh().replace(",","','")+"'");
		Map<String,Object> result=this.xyScrapNaiDao.findXyForPage(veh, filter);
		List<Map<String,Object>> list=(List<Map<String,Object>>) result.get("rows");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<CarKey> cars = new ArrayList<CarKey>();
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=(Map<String,Object>)list.get(i);
			CarKey car = new CarKey();	
			car.setCarNo(map.get("HPHM").toString());
        	car.setCarType(map.get("HPZL").toString());
        	cars.add(car);
        	map.put("RKSJ", sdf.format(map.get("RKSJ")));
			map.put("HPZLMC",this.systemDao.getCodeValue("030107",map.get("HPZL")==null?"":map.get("HPZL").toString()));
			map.put("HPYSMC",this.systemDao.getCodeValue("031001",map.get("HPYS")==null?"":map.get("HPYS").toString()));
			map.put("LBMC", systemManager.getCodeValue("120005", map.get("LB").toString()));//嫌疑类型（布控类别）
			map.put("CSYSMC",(map.get("CSYS")!=null)?this.systemDao.getCodeValue("030108",map.get("CSYS").toString()):"");
			map.put("CLLXMC",(map.get("CLLX")!=null)?this.systemDao.getCodeValue("030104",map.get("CLLX").toString()):"");
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> findXyCsForPage(ScsVehPassrec veh, Map<String, Object> filter) throws Exception {
		//veh.setKdbh("'"+veh.getKdbh().replace(",","','")+"'");
		Map<String,Object> result=this.xyScrapNaiDao.findXyCsForPage(veh, filter);
		List<Map<String,Object>> list=(List<Map<String,Object>>) result.get("rows");
		for(int i=0;i<list.size();i++){
			Map<String,Object> map=(Map<String,Object>)list.get(i);
			map.put("HPZLMC",this.systemDao.getCodeValue("030107",map.get("HPZL")==null?"":map.get("HPZL").toString()));
			map.put("HPYSMC",this.systemDao.getCodeValue("031001",map.get("HPYS")==null?"":map.get("HPYS").toString()));
		}
		return result;
	}

//	@SuppressWarnings("unchecked")
//	public Map<String, Object> queryForXYList(VehPassrec veh,Map<String, Object> filter) throws Exception {
//		// TODO Auto-generated method stub
//		veh.setKdbh("'"+veh.getKdbh().replace(",", "','")+"'");
//		Map<String, Object> map =this.xyScrapNaiDao.queryForXYList(veh, filter);
//		List<Map<String,Object>> list =(List<Map<String, Object>>) map.get("rows");
//		for(Iterator<Map<String,Object>> i=list.iterator();i.hasNext();){
//			Map<String,Object> result=i.next();
//			result.put("HPYS", systemManager.getCodeValue("031001", result.get("HPYS").toString()));
//			result.put("LBMC", systemManager.getCodeValue("120005", result.get("LB").toString()));//嫌疑类型（布控类别）
//			result.put("XZQH", this.systemDao.getDistrictNameByXzqh(result.get("KKBH").toString().substring(0, 6)));
//			result.put("KDBH", this.gateDao.getOldGateName(result.get("KKBH").toString()));
//			result.put("FXBH", this.directDao.getOldDirectName(result.get("FXBH").toString()));
//		}
//		return map;
//	}

	public Map<String, Object> findXYCarInfo(String id,String hphm,String hpzl) throws Exception {
		//报废未年检的车辆信息
		Map<String,Object> veh = xyScrapNaiDao.getXYCarInfo(id);
		//车辆最后的过车记录信息
		Map<String,Object> map = this.getVehDetail(hphm, hpzl);
		
		if(veh != null){
		  veh.put("HPYSMC",(veh.get("HPYS")!=null)?this.systemDao.getCodeValue("031001",veh.get("HPYS").toString()):"");
		  veh.put("HPZLMC",(veh.get("HPZL")!=null)?this.systemDao.getCodeValue("030107",veh.get("HPZL").toString()):"");
		  veh.put("CLLXMC",(veh.get("CLLX")!=null)?this.systemDao.getCodeValue("030104",veh.get("CLLX").toString()):"");
		  veh.put("CSYSMC",(veh.get("CSYS")!=null)?this.systemDao.getCodeValue("030108",veh.get("CSYS").toString()):"");
		 
		  //veh.put("FXBH", this.directDao.getOldDirectName(map.get("FXBH").toString()));
		  if(map != null){
			  veh.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(map.get("KDBH").toString().substring(0, 6)));
			  veh.put("KDMC",  map.get("KDMC"));
			  veh.put("FXMC",  map.get("FXMC"));
			  veh.put("GCSJ", map.get("GCSJ"));
			  veh.put("KDBH",  map.get("KDBH"));
			  veh.put("GCXH", map.get("GCXH"));
			  veh.put("CDBH", map.get("CDBH")); 
		  }
		}
		return veh;
	}
	
	public Map<String, Object> findXYCarInfo(String id,String hphm,String hpzl,String gcxh) throws Exception {
		//报废未年检的车辆信息
		Map<String,Object> veh = xyScrapNaiDao.getXYCarInfo(id);
		//车辆最后的过车记录信息
		Map<String,Object> map = this.getVehDetail(hphm,hpzl);
		
		if(veh != null){
		  veh.put("HPYSMC",(veh.get("HPYS")!=null)?this.systemDao.getCodeValue("031001",veh.get("HPYS").toString()):"");
		  veh.put("HPZLMC",(veh.get("HPZL")!=null)?this.systemDao.getCodeValue("030107",veh.get("HPZL").toString()):"");
		  veh.put("CLLXMC",(veh.get("CLLX")!=null)?this.systemDao.getCodeValue("030104",veh.get("CLLX").toString()):"");
		  veh.put("CSYSMC",(veh.get("CSYS")!=null)?this.systemDao.getCodeValue("030108",veh.get("CSYS").toString()):"");
		 
		  //veh.put("FXBH", this.directDao.getOldDirectName(map.get("FXBH").toString()));
		  if(map != null){
			  veh.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(map.get("KDBH").toString().substring(0, 6)));
			  veh.put("KDMC",  map.get("KDMC"));
			  veh.put("FXMC",  map.get("FXMC"));
			  veh.put("GCSJ", map.get("GCSJ"));
			  veh.put("KDBH",  map.get("KDBH"));
			  veh.put("GCXH", map.get("GCXH"));
			  veh.put("CDBH", map.get("CDBH")); 
		  }
		}
		return veh;
	}

	public Map<String,Object> getVehDetail(String hphm,String hpzl)throws Exception{
		Map<String,Object> veh = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		/*List<Map<String,Object>> list= scsVehPassrecDao.findList
				("select gcxh,kdbh,fxbh,cdbh,hpzl,gcsj,clsd,hpys,cllx,hphm,cwhphm,cwhpys,hpyz,csys,clxs,clpp,xszt,tp1,rksj  "
						+ "from veh_passrec t where hphm='"+hphm+"' and hpzl ='"+hpzl+"' order by gcsj desc");*/
		List<Map<String,Object>> list= clickHouseRepos.jdbcTemplate.queryForList
				("select concat(kkbh,'|',fxlx,'|',hphm,'|',hpzl,'|',toString(gcsj)) as GCXH,kkbh as KDBH,fxlx as FXBH,cdh as CDBH,hpzl as HPZL,toString(gcsj) as GCSJ,clsd as CLSD,hpys as HPYS,cllx as CLLX,hphm as HPHM,hphm as CWHPHM,hpys as CWHPYS,'' as HPYZ,csys as CSYS,'' as CLXS,clpp as CLPP,'' as XSZT,tp1 as TP1,tp2 as TP2,tp3 as TP3,toString(gcsj) as RKSJ "
						+ "from " + ClickHouseConstant.PASS_TABLE_NAME + " t where hphm='"+hphm+"' and hpzl ='"+hpzl+"' order by gcsj desc limit 1");
		if(list.size()>0){
		  veh=list.get(0);
		  veh.put("HPYSMC",(veh.get("HPYS")!=null)?this.systemDao.getCodeValue("031001",veh.get("HPYS").toString()):"");
		  veh.put("HPZLMC",(veh.get("HPZL")!=null)?this.systemDao.getCodeValue("030107",veh.get("HPZL").toString()):"");
		  veh.put("CLLXMC",(veh.get("CLLX")!=null)?this.systemDao.getCodeValue("030104",veh.get("CLLX").toString()):"");
		  veh.put("CSYSMC",(veh.get("CSYS")!=null)?this.systemDao.getCodeValue("030108",veh.get("CSYS").toString()):"");
		  veh.put("XSZTMC",(veh.get("XSZT")!=null)?this.systemDao.getCodeValue("110005",veh.get("XSZT").toString()):"");
		  veh.put("KDMC",  (veh.get("KDBH")!=null)?this.systemDao.getField("code_Gate","kdbh","kdmc",veh.get("KDBH").toString()):"");
		  veh.put("FXMC",  (veh.get("FXBH")!=null)?this.systemDao.getField("code_gate_extend","fxbh","fxmc",veh.get("FXBH").toString()):"");	
		}
		return veh;
	}
	
	public Map<String,Object> getVehDetail(String gcxh)throws Exception{
		Map<String,Object> veh = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String colmuns = "gcxh,kdbh,fxbh,cdbh,hpzl,gcsj,clsd,hpys,cllx,hphm,cwhphm,cwhpys,hpyz,csys,clxs,clpp,xszt,tp1,rksj"; // 需要查询的字段
		List<Map<String,Object>> list= scsVehPassrecDao.getVehPassrecByGcxh(colmuns,gcxh);
		if(list.size()>0){
		  veh=list.get(0);
		  veh.put("GCSJ", sdf.format(veh.get("GCSJ")));
		  veh.put("RKSJ", sdf.format(veh.get("RKSJ")));
		  veh.put("HPYSMC",(veh.get("HPYS")!=null)?this.systemDao.getCodeValue("031001",veh.get("HPYS").toString()):"");
		  veh.put("HPZLMC",(veh.get("HPZL")!=null)?this.systemDao.getCodeValue("030107",veh.get("HPZL").toString()):"");
		  veh.put("CLLXMC",(veh.get("CLLX")!=null)?this.systemDao.getCodeValue("030104",veh.get("CLLX").toString()):"");
		  veh.put("CSYSMC",(veh.get("CSYS")!=null)?this.systemDao.getCodeValue("030108",veh.get("CSYS").toString()):"");
		  veh.put("XSZTMC",(veh.get("XSZT")!=null)?this.systemDao.getCodeValue("110005",veh.get("XSZT").toString()):"");
		  veh.put("KDMC",  (veh.get("KDBH")!=null)?this.systemDao.getField("code_Gate","kdbh","kdmc",veh.get("KDBH").toString()):"");
		  veh.put("FXMC",  (veh.get("FXBH")!=null)?this.systemDao.getField("code_gate_extend","fxbh","fxmc",veh.get("FXBH").toString()):"");	
		}
		return veh;
	}
	
	@Override
	public int editXYStatus(String status, String xyids) throws Exception {
		// TODO Auto-generated method stub
		return xyScrapNaiDao.updateXYStatus(status, xyids);
	}
	


//	@Override
//	public int editScrapNaiInfo(String xyId) throws Exception {
//		// TODO Auto-generated method stub
//		return 0;
//	}
}
