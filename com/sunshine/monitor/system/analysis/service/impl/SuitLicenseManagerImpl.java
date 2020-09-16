package com.sunshine.monitor.system.analysis.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.analysis.bean.SuitLicense;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.bean.WhilteName;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.dao.SuitLicenseDao;
import com.sunshine.monitor.system.analysis.dao.SuitLicenseScSDao;
import com.sunshine.monitor.system.analysis.dao.XySuitLicenseDao;
import com.sunshine.monitor.system.analysis.dao.ZykVehPassrecDao;
import com.sunshine.monitor.system.analysis.service.SuitLicenseManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

@Service("suitLicenseManager")
public class SuitLicenseManagerImpl implements SuitLicenseManager {
	@Autowired
	private SuitLicenseScSDao suitLicenseScsDao;
	
	@Autowired
	private XySuitLicenseDao xySuitLicenseDao;
	
	@Autowired
	private SuitLicenseDao suitLicenseDao;
	
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
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	@Qualifier("zykVehPassrecDao")
	private ZykVehPassrecDao zykVehPassrecDao;
	
	@Autowired
	private ScsVehPassrecDao scsVehPassrecDao;
	

	@Override
	public int insertWhite(WhilteName wn) {
		// TODO Auto-generated method stub
		int x=xySuitLicenseDao.isHaveWhite(wn.getHphm(),wn.getHpzl());
		if(x>0){
			return -1;
		}
		return xySuitLicenseDao.insertWhite(wn);
	}


	@Override
	@Transactional
	public Map<String,Object> deleteWhite(JSONArray list) {
		Map<String,Object> resut=new HashMap<String,Object>();
		int i=0;
		for (int j = 0; j < list.size(); j++) {
			JSONObject json=JSONObject.parseObject((String)list.get(i));
			String hphm=json.getString("hphm");
			String hpzl=json.getString("hpzl");
			int z=xySuitLicenseDao.deleteWhite(hphm, hpzl);
			i=i+z;
		}
		if(i==list.size()){
			resut.put("msg", "删除成功。");
			return resut;
		}else{
			resut.put("msg", "删除失败");
			return resut;
		}
	}
	
	@Override
	public List<Map> queryhpzl() throws Exception {
		// TODO Auto-generated method stub
		return xySuitLicenseDao.queryhpzl();
	}

	
	@Override
	public Map whiteList(Map conditions) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map=xySuitLicenseDao.whitelist(conditions);
		List<Map> list=(List<Map>) map.get("rows");
		for (Map m :list) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			m.put("CTIME", m.get("CTIME").toString());
			m.put("HPZLY",m.get("HPZL"));
			m.put("HPZL",m.get("HPZL")!=null?this.systemDao.getCodeValue("030107", m.get("HPZL").toString()):"");
		}
		map.put("rows", list);
		return map;
	}
	
	public Map querySuitLicense(Map filter, String kssj, String jssj) {
		Map<String,Object> map =  this.suitLicenseScsDao.getCount(filter, kssj,jssj);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("KSSJ", result.get("sj").toString().substring(0, 10)+" 00:00:00");
			result.put("JSSJ", result.get("sj").toString().substring(0, 10)+" 23:59:59");
			list.add(result);
		}
		map.put("rows", list);
		return map;
	}
	
	public Map queryXySuitLicense(Map filter, String kssj, String jssj,
			String zt, String hphm, String hpzl, String hpys) throws Exception{
		Map<String, Object> map = this.xySuitLicenseDao.getXySuit(filter, kssj,
				jssj,zt,hphm,hpzl,hpys);
		List<Map<String,Object>> queryList = (List) map.get("rows");
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
		return map;
	}

	public Map getXySuit(Map filter, String kssj, String jssj, String zt, String hpzl) {
		return this.suitLicenseScsDao.getXySuit(filter, kssj, jssj, zt,hpzl);
	}

	public static void main(String[] args) {
	}
	
	public boolean isaccord (String re, String str){
		Pattern pat = Pattern.compile(re);
		Matcher mat = pat.matcher(str);
		if(mat.matches()){
			return true;
		}else{
			return false;
		}
	}
	
	public static String getDatePoor(String s1 , String  s2) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = sdf.parse(s1);
			d2 = sdf.parse(s2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    // long ns = 1000;
	    // 获得两个时间的毫秒时间差异
	    long diff = d1.getTime() - d2.getTime();
	    // 计算差多少天
	    long day = diff / nd;
	    // 计算差多少小时
	    long hour = diff % nd / nh;
	    // 计算差多少分钟
	    long min = diff % nd % nh / nm;
	    // 计算差多少秒//输出结果
	    // long sec = diff % nd % nh % nm / ns;
	    long result=Math.abs(day*24+hour);
	    return result+"";
	}
	
	public Map getSuitList(Map filter, String hphm,String hpzl, String kssj, String jssj, String zt) throws Exception {
		Map<String,Object> map = this.xySuitLicenseDao.getSuitList(filter,hphm,hpzl, kssj, jssj, zt);
		Map<String, Object> ypyjMap = this.xySuitLicenseDao.getYpyj(filter);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> m = (Map<String, Object>) i.next();
			m.put("HPYSMC", this.systemDao.getCodeValue("031001", m.get("HPYS").toString()));
			m.put("KDMC1",this.gateDao.getOldGateName(m.get("KDBH1")==null?"":m.get("KDBH1").toString()));
			m.put("KDMC2",this.gateDao.getOldGateName(m.get("KDBH2")==null?"":m.get("KDBH2").toString()));
			m.put("FXMC1", this.directDao.getDirectName(m.get("FXBH1")==null?"":m.get("FXBH1").toString()));
			m.put("FXMC2", this.directDao.getDirectName(m.get("FXBH2")==null?"":m.get("FXBH2").toString()));
		    String gcsj1 = m.get("GCSJ1").toString();
		    String gcsj2 = m.get("GCSJ2").toString();
		    m.put("gcsj1", gcsj1);
		    m.put("gcsj2", gcsj2);
		    String  sss=getDatePoor(gcsj1  , gcsj2);
		    m.put("sss", sss);
		    if(m.containsKey("MINTIME")&&m.get("MINTIME")!=null){
		    	long st=Long.valueOf(m.get("MINTIME").toString());
		    	m.put("MINTIME", st/3600000);
		    }
		    list.add(m);
		}
		map.put("rows", list);
		map.put("ypyjMap", ypyjMap);
		return map;
	}

	public Map getSuitCount(Map filter, String kssj, String jssj,String hpzl) {
		
		Map map =  this.suitLicenseScsDao.getSuitCount(filter, kssj, jssj, hpzl);
		List<Map<String,Object>> queryList =  (List) map.get("rows");
		List<CarKey> cars = new ArrayList<CarKey>();
		for(Map<String,Object> m:queryList){
			CarKey car = new CarKey();	
        	car.setCarNo(m.get("HPHM").toString());
        	car.setCarType(m.get("HPZL").toString());
        	cars.add(car);
        }     
		try {
			//Map<CarKey,Integer> wfxx=this.queryListDao.getViolationCount(cars);
			Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	        for (Iterator i = queryList.iterator(); i.hasNext();) {
				Map<String, Object> result = (Map<String, Object>) i.next();
				result.put("WFXX", wfxx.get(new CarKey(result.get("HPHM").toString(), result.get("HPZL").toString())));
				list.add(result);
			}
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return map;
	}

	public Map getSuitVehList(Map filter, SuitLicense suitLicense) throws Exception {
		Map map = this.suitLicenseScsDao.getSuitVehList(filter, suitLicense);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("KDMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
			result.put("FXMC", this.directDao.getOldDirectName(result.get("FXBH").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("KDBH").toString().substring(0, 6)));
			list.add(result);
		}
		map.put("rows", list);
		return map;
	}

	public void updateSuitLicense(SuitLicense suitLicense, String newzt) {
		this.suitLicenseScsDao.updateSuitLicense(suitLicense, newzt);
		
	}
	
	public void updateSuitLicenseOracle(Map suitLicense, String newzt) {
		this.xySuitLicenseDao.updateSuitLicense(suitLicense, newzt);
		if(newzt=="1"||"1".equals(newzt)){
			// 判断套牌库中是否已经存入此号牌
			boolean suitFlag = suitLicenseDao.exitSuitLicense(suitLicense);
			if(!suitFlag){
				VehicleEntity veh = new VehicleEntity();
				veh.setFzjg(suitLicense.get("hphm").toString().substring(0, 2));
				veh.setHphm(suitLicense.get("hphm").toString().substring(1));
				veh.setHpzl(suitLicense.get("hpzl").toString());
				// 查询机动信息
				try {
					VehicleEntity vehTemp = this.zykVehPassrecDao.getVehicleInfo(veh);
					if(vehTemp!=null){
						suitLicense.put("cllx", vehTemp.getCllx());
						suitLicense.put("csys", vehTemp.getCsys());
						suitLicense.put("clpp", vehTemp.getClpp1());
						suitLicense.put("clwx", "");
					}
					suitLicense.put("xxly", "1");
					suitLicense.put("xzqh", suitLicense.get("kdbh").toString().substring(0, 4));
					// 插入到套牌库
					suitLicenseDao.insertSuitLicense2(suitLicense);
				} catch (Exception e) {
//					suitLicense.put("cllx", "1");
//					suitLicense.put("csys", "1");
//					suitLicense.put("clpp", "1");
//					suitLicense.put("xxly", "1");
//					suitLicense.put("xzqh", suitLicense.get("kdbh").toString().substring(0, 4));
//					suitLicense.put("clwx", "");
//					suitLicenseDao.insertSuitLicense2(suitLicense);
					e.printStackTrace();
				}
			}
		}
	}
	
	public Map querySuitList(Map filter,VehPassrec veh) throws Exception {
		Map map=this.suitLicenseScsDao.querySuitList(filter, veh);
		List queryList = (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPZLMC",this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("KDMC1", this.gateDao.getOldGateName(result.get("KDBH1").toString()));
			result.put("FXMC1", this.directDao.getOldDirectName(result.get("FXBH1").toString()));
			result.put("XZQHMC1", this.systemDao.getDistrictNameByXzqh(result.get("KDBH1").toString().substring(0, 6)));
			result.put("KDMC2", this.gateDao.getOldGateName(result.get("KDBH2").toString()));
			result.put("FXMC2", this.directDao.getOldDirectName(result.get("FXBH2").toString()));
			result.put("XZQHMC2", this.systemDao.getDistrictNameByXzqh(result.get("KDBH2").toString().substring(0, 6)));
			/*if(result.get("ZT")!=null){
				if(result.get("ZT").toString().equals("1")){
					result.put("SURESUIT", "1");
				}
			}*/
			list.add(result);
		}
		
		
		return map;
	}

	@Override
	public int getXySuitTotal(Map<String, Object> filter, String kssj,
			String jssj, String zt, String hphm, String hpzl, String hpys)
			throws Exception {
		try{
			return xySuitLicenseDao.getXySuitTotal(filter, kssj, jssj, zt, hphm, hpzl, hpys);
		} catch(Exception e){
			return 0;
		}
	}

	public static String changeTime(long l){
		String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(l);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        res=res.substring(0,4)+res.substring(5,7)+res.substring(8,11);
        return res;
	}
	
	public static String changeTime1(String s){
		String res;
        res=s.substring(0,4)+s.substring(5,7)+s.substring(8,11);
        return res;
	}
	
	
	@Override
	public List<Map<String, Object>> queryXySuitLicenseExt(
			Map<String, Object> filter, String kssj, String jssj, String zt,
			String hphm, String hpzl, String hpys) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,Object>> queryList = this.xySuitLicenseDao.getXySuitExt(filter, kssj,
				jssj,zt,hphm,hpzl,hpys);
		PropertiesConfiguration config;config = new PropertiesConfiguration("ipport.properties");
		String deckwhitere = config.getString("deckwhitere");
		for (Map<String,Object> map: queryList) {
			Date date = sdf.parse(String.valueOf(map.get("RKSJ")));
			map.put("RKSJ", sdf.format(date));
			if(deckwhitere!=null && !"off".equals(deckwhitere)){
				if(isaccord(deckwhitere,map.get("hphm").toString() ) ){
					queryList.remove(map);
				}
			}
		}
		if(queryList.size() == 0) {
			return queryList;
		}
		
		List<CarKey> cars = new ArrayList<CarKey>();
		for(Map<String,Object> m:queryList){
			CarKey car = new CarKey();	
        	car.setCarNo(m.get("HPHM").toString());
        	car.setCarType(m.get("HPZL").toString());
        	cars.add(car);
        }
//		Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			result.put("HPZLMC", this.systemDao.getCodeValue("030107", result.get("HPZL").toString()));
//			result.put("WFXX", wfxx.get(new CarKey(result.get("HPHM").toString(), result.get("HPZL").toString())));
			String xzqh1=(String) result.get("xzqh1");
			String xzqh2=(String) result.get("xzqh2");
			String xzqh1mc="";
			String xzqh2mc="";
			if(xzqh1!=null && !"".equals(xzqh1)) {
				Map<String, Object> m = this.xySuitLicenseDao.getXzqhmc(xzqh1);
				xzqh1mc=(String) m.get("xzqhmc");
			}
			if(xzqh2!=null && !"".equals(xzqh2)) {
				Map<String, Object> m = this.xySuitLicenseDao.getXzqhmc(xzqh2);
				xzqh2mc=(String) m.get("xzqhmc");
			}
			result.put("XZQH1MC",xzqh1mc);
			result.put("XZQH2MC",xzqh2mc);
			list.add(result);
		}
		return Common.orderListByWfcs(queryList,"WFXX");
	}

	@Override
	public int getSuitTotal(Map<String, Object> filter, VehPassrec veh)
			throws Exception {
		try{
			return suitLicenseDao.getSuitTotal(filter, veh);
		} catch(Exception e){
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> querySuitListExt(
			Map<String, Object> filter, VehPassrec veh) throws Exception {
		List<Map<String, Object>> queryList = this.suitLicenseDao.querySuitListExt(filter, veh);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPZLMC",this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
//			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("XZQH").toString()));
			list.add(result);
		}		
		return queryList;
	}


}
