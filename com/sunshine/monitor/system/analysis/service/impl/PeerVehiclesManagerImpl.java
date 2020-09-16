package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.maintain.util.FileSystemResource;
import com.sunshine.monitor.comm.util.http.HttpXmlClient;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.PeerVehiclesDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.dao.SubjectDao;
import com.sunshine.monitor.system.analysis.service.PeerVehiclesManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Service("peerManager")
@Transactional
public class PeerVehiclesManagerImpl implements PeerVehiclesManager {
	
	protected Logger log = LoggerFactory.getLogger(PeerVehiclesManagerImpl.class);
	
	@Autowired
	@Qualifier("peerDao")
	private PeerVehiclesDao peerDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	private SubjectDao subjectDao;
	
	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	private ScsVehPassrecDao scsVehPassrecDao;
	
	private static String peer_server_ip;
	
	private static String peer_server_port;
	
	static {
		Properties property = FileSystemResource.getProperty("common.properties");
		peer_server_ip = property.getProperty("peer.server.ip", "10.142.54.242");
		peer_server_port = property.getProperty("peer.server.port", "8081");
	}
  
	public Map<String, Object> queryForPeers(ScsVehPassrec veh,Map<String,Object> filter) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
        //查询同行车信息
		result = this.peerDao.queryForPeers(veh,filter);
        //获取违法关联数
        List<Map<String,Object>> list =(List<Map<String, Object>>) result.get("rows");
       List<CarKey> cars = new ArrayList<CarKey>();
        for(Map<String,Object> m:list){
        	CarKey car = new CarKey();	
        	car.setCarNo(m.get("hphm").toString());
        	car.setCarType(m.get("hpzl").toString());
        	cars.add(car);
        }
        
        //Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
        Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
        //翻译字段
        for(int i=0;i<list.size();i++){
            Map<String,Object> map = list.get(i);
            map.put("hpysmc",this.systemDao.getCodeValue("031001",map.get("hpys").toString()));
            map.put("hpzlmc",this.systemDao.getCodeValue("030107",map.get("hpzl").toString()));
			map.put("wfxx", wfxx.get(
				new CarKey(map.get("hphm").toString(), map.get("hpzl").toString())
		    ));
        }
        result.put("rows", list);
        return result;
	}
	
	public List<Map<String, Object>> queryForPeersExt(ScsVehPassrec veh,Map<String,Object> filter) throws Exception {
        //查询同行车信息
	   List<Map<String,Object>> list = this.peerDao.queryForPeersExt(veh,filter);
        //获取违法关联数
       List<CarKey> cars = new ArrayList<CarKey>();
        for(Map<String,Object> m:list){
        	CarKey car = new CarKey();	
        	car.setCarNo(m.get("hphm").toString());
        	car.setCarType(m.get("hpzl").toString());
        	cars.add(car);
        }
        
        //Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
        Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
        //翻译字段
        for(int i=0;i<list.size();i++){
            Map<String,Object> map = list.get(i);
            map.put("hpysmc",this.systemDao.getCodeValue("031001",map.get("hpys").toString()));
            map.put("hpzlmc",this.systemDao.getCodeValue("030107",map.get("hpzl").toString()));
			map.put("wfxx", wfxx.get(
				new CarKey(map.get("hphm").toString(), map.get("hpzl").toString())
		    ));
        }
        return list;
	}
	
	public Integer queryForPeersTotal(ScsVehPassrec veh,Map<String,Object> map) throws Exception {
		Integer count = 0;
		count = this.peerDao.queryForPeersTotal(veh,map);
		return count;
	}
	
	public List<Map<String, Object>> queryForContrailExt(ScsVehPassrec veh,Map<String,Object> map) throws Exception {
		List<Map<String, Object>> list = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		list = this.peerDao.queryForContrailExt(veh, map);
		//违法关联
		List<CarKey> cars=new ArrayList<CarKey>();
		for(Map<String,Object> m : list){
			CarKey car=new CarKey();
			car.setCarNo(m.get("HPHM").toString());
			car.setCarType(m.get("HPZL").toString());
			cars.add(car);
		}
		//Map<CarKey,Integer> wfxx = this.scsVehPassrecDao.getViolationCount(cars);
		Map<CarKey,Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
		for(int i=0;i<list.size();i++){           
			Map<String,Object> scs = list.get(i);
			scs.put("gcsjmc",sdf.format(scs.get("gcsj")));
			scs.put("hpysmc",this.systemDao.getCodeValue("031001",scs.get("hpys").toString()));
			scs.put("hpzlmc",this.systemDao.getCodeValue("030107",scs.get("hpzl").toString()));
			scs.put("kdmc",this.systemDao.getField("code_Gate","kdbh","kdmc",scs.get("kdbh").toString()));
			scs.put("xzqhmc",this.systemDao.getDistrictNameByXzqh(scs.get("kdbh").toString().substring(0,6)));
			scs.put("fxmc", this.systemDao.getField("code_gate_extend","fxbh","fxmc",scs.get("fxbh").toString()));
			scs.put("WFXX", wfxx.get(new CarKey(scs.get("HPHM").toString(),scs.get("HPZL").toString())));
		}		
		return list;
	}
	
	public Integer queryForContrailTotal(ScsVehPassrec veh,Map<String,Object> map) throws Exception {
		Integer total = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		total = this.peerDao.queryForContrailTotal(veh, map);
		return total;
	}
	
	
	public Map<String, Object> queryForContrail(ScsVehPassrec veh,
			Map<String, Object> map) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = this.peerDao.queryForContrail(veh, map);
        //翻译字段
		/*List<ScsVehPassrec> rows = (List<ScsVehPassrec>) result.get("rows");
		for(int i=0;i<rows.size();i++){
			ScsVehPassrec scs = rows.get(i);
			scs.setXzqhmc(this.systemDao.getDistrictNameByXzqh(scs.getKdbh().substring(0,6)));
			Map<String,Object> gateInfo= this.systemDao.getGateByKDBH(scs.getKdbh());
			if(gateInfo!=null){
			  scs.setKkjd(gateInfo.get("X").toString());
			  scs.setKkwd(gateInfo.get("Y").toString());
			}
		}
        result.put("rows", rows);*/
        
        //翻译字段
		List<Map<String,Object>> rows = (List<Map<String,Object>>) result.get("rows");
		//违法关联
		List<CarKey> cars=new ArrayList<CarKey>();
		for(Map<String,Object> m : rows){
			CarKey car=new CarKey();
			car.setCarNo(m.get("HPHM").toString());
			car.setCarType(m.get("HPZL").toString());
			cars.add(car);
		}
		//Map<CarKey,Integer> wfxx = this.scsVehPassrecDao.getViolationCount(cars);
		Map<CarKey,Integer> wfxx = this.illegalZYKDao.getViolationCount(cars);
		for(int i=0;i<rows.size();i++){           
			Map<String,Object> scs = rows.get(i);
			scs.put("gcsjmc",sdf.format(scs.get("gcsj")));
			scs.put("hpysmc",this.systemDao.getCodeValue("031001",scs.get("hpys").toString()));
			scs.put("hpzlmc",this.systemDao.getCodeValue("030107",scs.get("hpzl").toString()));
			scs.put("kdmc",this.systemDao.getField("code_Gate","kdbh","kdmc",scs.get("kdbh").toString()));
			scs.put("xzqhmc",this.systemDao.getDistrictNameByXzqh(scs.get("kdbh").toString().substring(0,6)));
			scs.put("fxmc", this.systemDao.getField("code_gate_extend","fxbh","fxmc",scs.get("fxbh").toString()));
			scs.put("WFXX", wfxx.get(new CarKey(scs.get("HPHM").toString(),scs.get("HPZL").toString())));
		}		
		result.put("rows", rows);
		return result;
	}

	public Map<String, Object> queryPeerDetail(ScsVehPassrec veh,
			Map<String, Object> map) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String,String> mapParams = new HashMap<String,String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map condition = JSON.parseArray(veh.getCondition()).getJSONObject(0);
		mapParams.put("license", (String) condition.get("hphm"));
		mapParams.put("licenseType", (String) condition.get("hpzl"));
		mapParams.put("peerLicense", veh.getHphm());
		mapParams.put("peerLicenseType", veh.getHpzl());
		mapParams.put("beginDate", veh.getKssj());
		mapParams.put("endDate", veh.getJssj());
		mapParams.put("pageIndex", (String) map.get("page"));
		mapParams.put("pageSize", (String) map.get("rows"));
		if(veh.getGaptime() !=null && !"".equals(veh.getGaptime())){
			mapParams.put("minutes", veh.getGaptime());
		}
		String serviceUrl = "http://"+peer_server_ip+":"+peer_server_port+"/api/hn/analysis/peerVehicle/getDetail";
		log.info("输入参数:" +  mapParams.toString());
		long t1 = System.currentTimeMillis();
		// 发送请求
		String respText = HttpXmlClient.post(serviceUrl, mapParams);
		if (respText != null){
			log.info("输出结果:" + respText);
			result = JSON.parseObject(respText);
			
			//翻译字段
			List<Map<String, Object>> rows = (List<Map<String ,Object>>) result.get("rows");
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> scs = rows.get(i);
				//int sjc = Math.round(Math.abs(sdf.parse(scs.get("gcsj").toString()).getTime()-sdf.parse(scs.get("zcgcsj").toString()).getTime())/1000);
				//scs.put("sjc", sjc);
				scs.put("gcsj", scs.get("gcsj"));
				scs.put("kdmc", this.systemDao.getField("code_Gate", "kdbh",
						"kdmc", scs.get("kdbh").toString()));
				scs.put("fxmc", this.systemDao.getField("code_gate_extend", "fxbh",
						"fxmc", scs.get("fxbh").toString()));
				scs.put("xzqhmc", this.systemDao.getDistrictNameByXzqh(scs.get(
						"kdbh").toString().substring(0, 6)));
				scs.put("hpysmc", (scs.get("hpys") != null) ? this.systemDao
						.getCodeValue("031001", scs.get("hpys").toString()) : "");
				scs.put("xzqhmc", this.systemDao.getDistrictNameByXzqh(scs.get(
						"kdbh").toString().substring(0, 6)));
				scs.put("num", (Integer.parseInt(map.get("page").toString())-1)*Integer.parseInt(map.get("rows").toString())+(i+1));
			}
	        result.put("rows", rows);
		}
        
		return result;
	}

/*	public Map<String, Object> queryPeerCsDetail(ScsVehPassrec veh,
			Map<String, Object> map) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 主车辆sql
		StringBuffer zcSql = new StringBuffer(
				"SELECT hphm,gcsj,kdbh FROM veh_passrec WHERE gcsj > '"
						+ veh.getKssj() + "'AND gcsj < '" + veh.getJssj() + "'");
		// 同行车辆sql
		StringBuffer txcSql = new StringBuffer("SELECT gcxh,hphm,hpzl,hpys,tp1,gcsj,kdbh,fxbh FROM veh_passrec WHERE hphm = '"
						+ veh.getHphm() + "' AND hpzl = '" + veh.getHpzl()
						+ "'"); 
		String condition = veh.getCondition();
		JSONObject jsonobj = JSONObject.fromObject(condition.substring(1,condition.length()-1));
		// 同行车辆hphm,hpzl
		if(jsonobj.containsKey("hphm")){
			zcSql.append(" and hphm = '" + jsonobj.get("hphm") + "'");
		}
		if(jsonobj.containsKey("hpzl")){
			zcSql.append(" and hpzl = '" + jsonobj.get("hpzl") + "'");
		}
		if (jsonobj.containsKey("detail")) {
			// 拼接同行车辆的gcsj,kdbh条件
			String txctj = "";
			// 拼接主车辆的kdbh条件
			String kdbhlist = "";
			String detail = jsonobj.get("detail").toString();
			String[] detailArray = detail.split("#");
			for (String s : detailArray) {
				String kdbh = s.substring(s.indexOf(",")+1);
				String gcsj = s.substring(0, s.indexOf(","));
				if(kdbhlist.indexOf(kdbh) == -1){
					kdbhlist += "'"+kdbh+"',";
				}
				txctj += "(gcsj = '"+gcsj+"' AND kdbh = '"+kdbh+"') OR ";
			}
			
			zcSql.append(" AND kdbh IN (" + kdbhlist.substring(0, kdbhlist.length()-1) + ")");
			txcSql.append(" AND ("+txctj.substring(0, txctj.lastIndexOf("OR"))+")");
		}
		// 同行间隔时间
		String gaptime = String.valueOf((Integer.parseInt(veh.getGaptime())*60));
		// 终极大sql
		String sql = "SELECT * FROM (SELECT a.gcxh,a.hphm,a.hpzl,a.hpys,a.tp1,a.gcsj,a.kdbh,a.fxbh,b.hphm as zcphm,b.gcsj AS zcgcsj " +
				"from ("+txcSql+")a,("+zcSql+")b WHERE a.kdbh = b.kdbh AND a.gcsj >= b.gcsj - INTERVAL '"+gaptime+"' SECOND AND a.gcsj <= b.gcsj + INTERVAL '"+gaptime+"' SECOND)c";
		long start = System.currentTimeMillis();
		result = this.peerDao.queryPeerCsDetail(sql, map);
		long end = System.currentTimeMillis();
		log.info("同行轨迹点集合查询耗时："+(end - start)+"MS");
		// 翻译字段
		List<Map<String, Object>> rows = (List<Map<String, Object>>) result
				.get("rows");
		for (int i = 0; i < rows.size(); i++) {
			Map<String, Object> scs = rows.get(i);
			int sjc = Math.round(Math.abs(sdf.parse(scs.get("gcsj").toString()).getTime()-sdf.parse(scs.get("zcgcsj").toString()).getTime())/1000);
			scs.put("sjc", sjc);
			scs.put("kdmc", this.systemDao.getField("code_Gate", "kdbh",
					"kdmc", scs.get("kdbh").toString()));
			scs.put("fxmc", this.systemDao.getField("code_gate_extend", "fxbh",
					"fxmc", scs.get("fxbh").toString()));
			scs.put("xzqhmc", this.systemDao.getDistrictNameByXzqh(scs.get(
					"kdbh").toString().substring(0, 6)));
			scs.put("hpysmc", (scs.get("hpys") != null) ? this.systemDao
					.getCodeValue("031001", scs.get("hpys").toString()) : "");
			scs.put("xzqhmc", this.systemDao.getDistrictNameByXzqh(scs.get(
					"kdbh").toString().substring(0, 6)));
		}
		result.put("rows", rows);
		return result;
	}*/
	
	public Map<String, Object> queryComparePic(ScsVehPassrec veh,
			Map<String, Object> filter) throws Exception {
		List<ScsVehPassrec[]> list = new ArrayList<ScsVehPassrec[]>();
		Map<String,Object> result = this.peerDao.queryPeerDetail(veh, filter);
        //翻译字段
		List<Map<String,Object>> rows = (List<Map<String,Object>>) result.get("rows");
		for(int i=0;i<rows.size();i++){
			ScsVehPassrec[] array = new ScsVehPassrec[2];
			ScsVehPassrec veh1 = new ScsVehPassrec();
			ScsVehPassrec veh2 = new ScsVehPassrec();
            
			Map<String,Object> scs = rows.get(i);
			scs.put("kdmc",this.systemDao.getField("code_Gate","kdbh","kdmc",scs.get("kdbh").toString()));
			scs.put("xzqhmc",this.systemDao.getDistrictNameByXzqh(scs.get("kdbh").toString().substring(0,6)));
			scs.put("fxmc", this.systemDao.getField("code_gate_extend","fxbh","fxmc",scs.get("fxbh").toString()));
			//组装成List<ScsVehPssrec[]>
			veh1.setHphm(scs.get("hphm").toString());
			veh1.setGcsj(scs.get("gcsj").toString());
			veh1.setKdmc(scs.get("kdmc").toString());
			veh1.setFxmc(scs.get("fxmc").toString());
			veh1.setXzqhmc(scs.get("xzqhmc").toString());
			veh1.setTp1(scs.get("tp1").toString());
			
			veh2.setHphm(scs.get("mbhp").toString());
			veh2.setGcsj(scs.get("mbsj").toString());
			veh2.setKdmc(scs.get("kdmc").toString());
			veh2.setFxmc(scs.get("fxmc").toString());
			veh2.setXzqhmc(scs.get("xzqhmc").toString());
			veh2.setTp1(scs.get("mbtp").toString());
            veh1.setSjc(scs.get("sjc").toString());
            
			array[0]=veh1;
			array[1]=veh2;
			list.add(array);
		}
		
		result.put("rows", list);
		return result;
	}

	public int updateCheck(ScsVehPassrec veh, String sessionId)
			throws Exception {
		return this.peerDao.updateCheck(veh, sessionId);
	}
/*
	public Map getContrailList(String sessionId) throws Exception {
		Map map = new HashMap();
		List<ScsVehPassrec> list =  this.peerDao.getContrailList(sessionId);
		for(int i=0;i<list.size();i++){
			ScsVehPassrec scs = (ScsVehPassrec) list.get(i);
			//scs.setXzqhmc(this.systemDao.getDistrictNameByXzqh(scs.getKdbh().substring(0,6)));
			Map<String,Object> gateInfo= this.systemDao.getGateByKDBH(scs.getKdbh());
			if(gateInfo!=null){
			  scs.setKkjd(gateInfo.get("X").toString());
			  scs.setKkwd(gateInfo.get("Y").toString());
			}
		}
		map.put("rows", list);
		map.put("allgates", this.systemDao.getAllGate());
		return map;
	}*/
	public Map getContrailList(String sessionId) throws Exception {
		/*Map map = new HashMap();
		List<ScsVehPassrec> list =  this.peerDao.getContrailList(sessionId);
		Map<String, List<ScsVehPassrec>> hphmTemp = new HashMap<String, List<ScsVehPassrec>>();
		for(int i=0;i<list.size();i++){
			ScsVehPassrec scs = (ScsVehPassrec) list.get(i);
			//scs.setXzqhmc(this.systemDao.getDistrictNameByXzqh(scs.getKdbh().substring(0,6)));
			Map<String,Object> gateInfo= this.systemDao.getGateByKDBH(scs.getKdbh());
			if(gateInfo!=null){
			  scs.setKkjd(gateInfo.get("X").toString());
			  scs.setKkwd(gateInfo.get("Y").toString());
			}
			
			if(hphmTemp.get(scs.getHphm())==null){
				List<ScsVehPassrec> listtemp= new ArrayList<ScsVehPassrec>();
				listtemp.add(scs);
				hphmTemp.put(scs.getHphm(), listtemp);
			}
			else
				hphmTemp.get(scs.getHphm()).add(scs);
		}
		map.put("rows", hphmTemp);
		map.put("allgates", this.systemDao.getAllGate());*/
		return null;
	}
	
	
	public Map getContrailListBySave(String ztbh,Map<String,Object> filter) throws Exception {
		Map map = new HashMap();
		List<ScsVehPassrec> list =  (List<ScsVehPassrec>) this.subjectDao.loadContrail(ztbh, filter).get("rows");
		for(int i=0;i<list.size();i++){
			ScsVehPassrec scs = (ScsVehPassrec) list.get(i);
			scs.setHpysmc(this.systemDao.getCodeValue("031001",
					scs.getHpys()));
			scs.setHpzlmc(this.systemDao.getCodeValue("030107",
					scs.getHpzl()));
			scs.setKdmc(this.systemDao.getField("dc_code_Gate","kdbh","kdmc",scs.getKdbh()).toString());
			scs.setFxmc(this.systemDao.getField("dc_code_direct","fxbh","fxmc",scs.getFxbh()).toString());
			scs.setXzqhmc(this.systemDao.getDistrictNameByXzqh(scs.getKdbh().substring(0,6)));
			//scs.setXzqhmc(this.systemDao.getDistrictNameByXzqh(scs.getKdbh().substring(0,6)));
			Map<String,Object> gateInfo= this.systemDao.getGateByKDBH(scs.getKdbh());
			if(gateInfo!=null){
			  scs.setKkjd(gateInfo.get("X").toString());
			  scs.setKkwd(gateInfo.get("Y").toString());
			}
		}
		map.put("rows", list);
		map.put("allgates", this.systemDao.getAllGate());
		return map;
	}

	public List<Map<String, Object>> getPeerList(String sessionId)
			throws Exception {
		return this.peerDao.getPeerList(sessionId);
	}
	

	public Map<String, Object> loadPeer(String page, String rows, VehPassrec veh,
			String sign, String cs, String symbol, String gaptime)
			throws Exception {
		Map<String,String> mapParams = new HashMap<String,String>();
		List<Object> resultList = new ArrayList<Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		mapParams.put("license", veh.getHphm());
		mapParams.put("licenseType", veh.getHpzl());
		mapParams.put("beginDate", veh.getKssj());
		mapParams.put("endDate", veh.getJssj());
		mapParams.put("pageIndex", page);
		mapParams.put("pageSize", rows);
		if(cs!=null&&!"".equals(cs)){
			mapParams.put("symbol", symbol);
			mapParams.put("timesFilter", cs);
		}
		if(gaptime!=null&&!"".equals("gaptime")){
			mapParams.put("minutes", gaptime);
		}
		//String serviceUrl = "http://10.142.54.242:8081/api/hn/analysis/peerVehicle/findPeerVehicle";
		String serviceUrl = "http://"+peer_server_ip+":"+peer_server_port+"/api/hn/analysis/peerVehicle/findPeerVehicle";
		log.info("输入参数:" +  mapParams.toString());
		long t1 = System.currentTimeMillis();
		// 发送请求
		String respText = HttpXmlClient.post(serviceUrl, mapParams);
		if (respText != null){
			log.info("输出结果:" + respText);
			resultMap = JSON.parseObject(respText);
			resultList = (List<Object>) resultMap.get("rows");
			if(resultList!=null && resultList.size()>0){
				List<CarKey> cars = new ArrayList<CarKey>();
				for(Iterator i = resultList.iterator();i.hasNext();){
					Map<String,Object> peer = (Map<String,Object>)i.next();
					CarKey car = new CarKey();	
		        	car.setCarNo(peer.get("hphm").toString());
		        	car.setCarType(peer.get("hpzl").toString());
		        	cars.add(car);
				}
				Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
				//Map<CarKey,Integer> wfxx=new HashMap<CarKey, Integer>();
				for(Iterator i = resultList.iterator();i.hasNext();){
					Map<String,Object> peer = (Map<String,Object>)i.next();
					peer.put("hpzlmc", this.systemDao.getCodeValue("030107",peer.get("hpzl")==null?"":peer.get("hpzl").toString()));
					peer.put("hpysmc", this.systemDao.getCodeValue("031001", peer.get("hpys")==null?"":peer.get("hpys").toString()));
					peer.put("wfxx", wfxx.get(new CarKey(peer.get("hphm").toString(), peer.get("hpzl").toString())));
				}
			}
		}else{
			log.error("unknow error");
		}
		log.info("查询耗时:" + (System.currentTimeMillis() -t1));
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("rows", resultList);
		return resultMap;
	}

}
