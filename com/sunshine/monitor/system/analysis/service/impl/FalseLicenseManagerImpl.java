package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.action.AJAXCtrl;
import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.util.picws.PicWSInvokeTool;
import com.sunshine.monitor.system.analysis.bean.FalseLicense;
import com.sunshine.monitor.system.analysis.dao.FalseLicenseDao;
import com.sunshine.monitor.system.analysis.dao.FalseLicenseSCSDao;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.analysis.dao.SuitLicenseDao;
import com.sunshine.monitor.system.analysis.factory.FalseLicenseFactory;
import com.sunshine.monitor.system.analysis.service.FalseLicenseManager;
import com.sunshine.monitor.system.gate.dao.DirectDao;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.dao.PassrecDao;

@Service("falseManager")
@Transactional
public class FalseLicenseManagerImpl implements FalseLicenseManager {

	@Autowired
	@Qualifier("falseDao")
	private FalseLicenseDao falseDao;
	
	@Autowired
	@Qualifier("falseSCSDao")
	private FalseLicenseSCSDao falseSCSDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;

	@Autowired
	@Qualifier("passrecDao")
	private PassrecDao passrecDao;

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("falseLicenseFactory")
	private FalseLicenseFactory falseLicenseFactory;
	
	@Autowired
	private SuitLicenseDao suitLicenseDao;
	
	@Autowired
	private GateDao gateDao;
	
	@Autowired
	private DirectDao directDao;

	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	@Autowired
	@Qualifier("scsVehPassrecDao")
	private ScsVehPassrecDao scsVehPassrecDao;
	
	private Logger log = LoggerFactory.getLogger(FalseLicenseManagerImpl.class);

	public Map<String, Object> findPageForFL(Map<String, Object> map,
			FalseLicense fl) throws Exception {
		Map<String,Object> result = new HashMap<String,Object>();
		result = this.falseDao.findPageForFL(map, fl);
		List<FalseLicense> list=(List<FalseLicense>)result.get("rows");
		for(FalseLicense car:list){
			car.setHpzlmc(this.systemDao.getCodeValue("030107",car.getHpzl()));
			car.setCllxmc(this.systemDao.getCodeValue("030104",car.getCllx()));
			car.setHpysmc(this.systemDao.getCodeValue("031001",car.getHpys()));
			car.setCsysmc(this.systemDao.getCodeValue("030108",car.getCsys()));
		}
		return result;
	}

	public void delFalseLicense(List<FalseLicense> list) throws Exception {
		this.falseDao.delFalseLicense(list);
	}

	public Map getAllHphm(Map<String,Object> filter) throws Exception {
		
		return this.falseSCSDao.getAllHphm(filter);
	}

	public List getTrajectoryByHphm(Map<String, Object> filter) throws Exception {
		return this.falseDao.getTrajectoryByHphm(filter);
	}

	public int writeTrajectory(List list) throws Exception {
		return this.falseDao.writeTrajectory(list);
	}

	public boolean isExistHphm(Map<String, Object> filter) throws Exception {
		
		return this.falseDao.isExistHphm(filter);
	}

	public int writeFalseTable(Map<String, Object> filter) throws Exception {
		
		return this.falseDao.writeFalseTable(filter);
	}

	public Map getFalseCount(Map<String, Object> filter) throws Exception {
		return this.falseLicenseFactory.UseModel(falseLicenseFactory.SOURCENAME).getFalseCount(filter);
	}

	public Map getSuspectedListBysign(Map<String, Object> filter)
			throws Exception {
		return this.falseLicenseFactory.UseModel(falseLicenseFactory.SOURCENAME).getSuspectedListBysign(filter);
	}

	public Map getFalseListByHphm(Map<String, Object> filter) throws Exception {
		Map resultMap=new HashMap();
		resultMap = this.falseLicenseFactory.UseModel(falseLicenseFactory.SOURCENAME).getFalseListByHphm(filter);
		
		return resultMap;
	}

	public String sureFalse(String hphm, String flag,String hpzl) throws Exception {
		return 		this.falseLicenseFactory.UseModel(falseLicenseFactory.SOURCENAME).sureFalse(hphm, flag,hpzl);
	}
	
	public Map queryFalseList(Map filter,VehPassrec veh) throws Exception {
		Map map=this.falseDao.queryFalseList(filter, veh);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> result = (Map<String, Object>) i.next();
			result.put("HPZLMC",this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPYS").toString()));
			//result.put("KDMC", this.gateDao.getOldGateName(result.get("KDBH").toString()));
			//result.put("FXMC", this.directDao.getOldDirectName(result.get("FXBH").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("xzqh").toString()));
			result.put("CSYSMC",this.systemDao.getCodeValue("030108", result.get("csys")==null?"":result.get("csys").toString()));
			result.put("RKSJ", result.get("rksj")==null?"":sdf.format(result.get("rksj")));
			result.put("CZR", result.get("czr"));
			result.put("CZSJ", result.get("czsj")==null?"":sdf.format(result.get("czsj")));
			result.put("xxly", result.get("xxly")==null?"":result.get("xxly").toString());
			/*if(result.get("ZT")!=null){
				if(result.get("ZT").toString().equals("1")){
					result.put("SURESUIT", "1");
				}
			}*/
			list.add(result);
		}
		return map;
	}
	
	/**
	 * 假牌分析
	 */
	public Map queryXyFalseList(Map filter) throws Exception{
		//Map map = this.falseLicenseFactory.UseModel(falseLicenseFactory.SOURCENAME).queryXyFalseList(filter);
		Map map = this.falseDao.queryXyFalseList(filter);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> queryList = (List)map.get("rows");
		if(queryList.size() == 0){
			return map;
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<CarKey> cars = new ArrayList<CarKey>();
		for(Map<String, Object> m : queryList){
			CarKey car = new CarKey();	
        	car.setCarNo(m.get("HPHM").toString());
        	car.setCarType(m.get("HPZL").toString());
        	cars.add(car);
		}
		//Map<CarKey,Integer> wfxx=this.scsVehPassrecDao.getViolationCount(cars);
		Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		for(Iterator i = queryList.iterator();i.hasNext();){
			Map<String,Object> result = (Map<String,Object>)i.next();
			//result.put("HPZL", result.get("hpzl"));
			result.put("HPHM", result.get("hphm"));
			//result.put("TOTAL", result.get("total"));
			result.put("KKMC", this.gateDao.getOldGateName(result.get("KKBH").toString()));
			result.put("HPZLMC", this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPZL")==null?"":result.get("HPYS").toString()));
			result.put("CSYSMC",this.systemDao.getCodeValue("030108", result.get("csys")==null?"":result.get("csys").toString()));
			result.put("WFXX", wfxx.get(new CarKey(result.get("HPHM").toString(), result.get("HPZL").toString())));
			result.put("ZT", result.get("zt"));
			result.put("RKSJ", result.get("rksj")==null?"":sdf.format(result.get("rksj")));
			result.put("CZR", result.get("czr"));
			result.put("CZSJ", result.get("czsj")==null?"":sdf.format(result.get("czsj")));
			result.put("xxly", result.get("xxly")==null?"":result.get("xxly").toString());
			list.add(result);
		}
		map.put("rows",list);
		return map;
	}
	
	/**
	 * 假牌分析--只查询记录
	 */
	public List<Map<String, Object>> queryXyFalseListExt(Map filter, VehPassrec veh) throws Exception{
		//Map map = this.falseLicenseFactory.UseModel(falseLicenseFactory.SOURCENAME).queryXyFalseList(filter);
		List<Map<String, Object>> queryList = this.falseDao.queryXyFalseListExt(filter, veh);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(queryList.size() == 0){
			return queryList;
		}
//		List<CarKey> cars = new ArrayList<CarKey>();
//		for(Map<String, Object> m : queryList){
//			CarKey car = new CarKey();	
//        	car.setCarNo(m.get("HPHM").toString());
//        	car.setCarType(m.get("HPZL").toString());
//        	cars.add(car);
//		}
//		Map<CarKey,Integer> wfxx=this.illegalZYKDao.getViolationCount(cars);
		for(Iterator i = queryList.iterator();i.hasNext();){
			Map<String,Object> result = (Map<String,Object>)i.next();
			result.put("HPZL", result.get("hpzl"));
			result.put("HPHM", result.get("hphm"));
			result.put("TOTAL", result.get("total"));
			result.put("HPZLMC", this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPZL")==null?"":result.get("HPYS").toString()));
//			result.put("KKMC", this.gateDao.getOldGateName(result.get("KKBH").toString()));//			
//			result.put("CSYSMC",this.systemDao.getCodeValue("030108", result.get("csys")==null?"":result.get("csys").toString()));
//			result.put("WFXX", wfxx.get(new CarKey(result.get("HPHM").toString(), result.get("HPZL").toString())));
//			result.put("ZT", result.get("zt"));
//			result.put("RKSJ", result.get("rksj")==null?"":sdf.format(result.get("rksj")));
//			result.put("CZR", result.get("czr"));
//			result.put("CZSJ", result.get("czsj")==null?"":sdf.format(result.get("czsj")));
//			result.put("xxly", result.get("xxly")==null?"":result.get("xxly").toString());
		}
		return queryList;
	}
	
	/**
	 * 假牌分析--只查询总记录条数
	 */
	public Integer queryXyFalseListTotal(Map filter, VehPassrec veh) throws Exception{
		Integer total = 0;
		total = this.falseDao.queryXyFalseListTotal(filter, veh);
		return total;
	}
	
	public Map queryXyAllFalseList(Map filter) throws Exception{
		//Map map = this.falseLicenseFactory.UseModel(falseLicenseFactory.SOURCENAME).queryXyFalseList(filter);
		Map map = this.falseDao.queryXyAllFalseList(filter);
		List<Map<String, Object>> queryList = (List)map.get("rows");
		if(queryList.size() == 0){
			return map;
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Iterator i = queryList.iterator();i.hasNext();){
			Map<String,Object> result = (Map<String,Object>)i.next();
			result.put("HPHM", result.get("hphm"));
			result.put("HPZLMC", this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPZL")==null?"":result.get("HPYS").toString()));
			result.put("ZT", result.get("zt"));
			result.put("kdmc1", gateDao.getGateName(result.get("kkbh").toString()));
			result.put("GCTP1",result.get("tp1"));
			result.put("GCXH1", result.get("gcxh"));
			result.put("gcsj1", result.get("gcsj").toString());
			result.put("kdmc1", result.get("kkmc"));
			list.add(result);
		}
		map.put("rows",list);
		return map;
	}

	
	public void updateXyFalse(String hphm, String hpzl) throws Exception{
		// 判断假牌库中是否已经存入此号牌
		boolean falseFlag = falseDao.isFalsed(hphm,hpzl);
		if(!falseFlag){
			try {
				// 查询出假牌信息，取最新一条过车数据
				List<VehPassrec> vehPassrecs = scsVehPassrecDao.getLatestVehPassrec(hphm, hpzl);
				VehPassrec vehPassrec = vehPassrecs.get(0);
				// 插入到假牌库
				falseDao.insertFalse(vehPassrec);
				System.out.println("分析结果插入假牌成功："+hphm+"zl:"+hpzl);
			} catch (Exception e) {
				System.out.println("分析结果插入假牌失败："+hphm+"zl:"+hpzl);
				e.printStackTrace();
			}
		}
	}
	
	public void updateXyFalse(String newzt, String hphm, String hpzl,String hpys,SysUser user) throws Exception{
		this.falseDao.updateXyFalse(newzt, hphm, hpzl,hpys,user);
		if(newzt =="1" || "1".equals(newzt)){
			// 判断假牌库中是否已经存入此号牌
			boolean falseFlag = falseDao.isFalsed(hphm,hpzl);
			String msg="";
			if(!falseFlag){
				try {
					// 插入到假牌库
					falseDao.insertFalse(newzt, hphm, hpzl,user);
					log.info("分析结果插入假牌成功："+hphm+",hpzl:"+hpzl+",hpys:"+hpys);
					msg = "更新成功";
				} catch (Exception e) {
					log.info("分析结果插入假牌失败："+hphm+",hpzl:"+hpzl+",hpys:"+hpys);
					e.printStackTrace();
					msg = "更新失败";
				}
			}else{
				msg = "假牌库中已存在该号牌！";
			}
		}
	}
	
	public List queryPass(Map filter) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>>  list = this.falseSCSDao.queryPass(filter);
		
		//map.putAll(wfmap);
		//List<Map<String, Object>> queryList = (List)map.get("rows");
		//List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Iterator iterator = list.iterator();  
		while(iterator.hasNext())
		{
			Map<String, Object> result = (Map<String, Object>) iterator.next();
			result.put("SJC", filter.get("sjc")==null?"":filter.get("sjc").toString());
			result.put("HPZLMC",this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("hpys")==null?"":result.get("hpys").toString()));
			result.put("CLLXMC",this.systemDao.getCodeValue("030104",result.get("cllx")==null?"":result.get("cllx").toString()));
			result.put("CSYSMC",this.systemDao.getCodeValue("030108",result.get("csys")==null?"":result.get("csys").toString()));
			result.put("KDMC", this.gateDao.getOldGateName(result.get("kdbh").toString()));
			result.put("FXMC", this.directDao.getOldDirectName(result.get("fxbh").toString()));
			result.put("XZQHMC", this.systemDao.getDistrictNameByXzqh(result.get("kdbh").toString().substring(0, 6)));
			//result.put("FXMC",this.systemDao.getField("code_gate_extend","fxbh","fxmc",result.get("fxbh").toString())==null?"":result.get("fxbh").toString());
			result.put("GCSJ",sdf.format(result.get("gcsj")));

			filter.put("kdbh", result.get("kdbh").toString());
			List<Map<String,Object>> queryList3 =this.falseDao.queryKksbl(filter);
			if(queryList3.size() > 0){
//				for(Iterator i3 = queryList3.iterator();i3.hasNext();){
				for(Map<String,Object> result3 : queryList3){
					String sd = result.get("gcsd").toString();
					int hours = Integer.parseInt(sd);
//					Map<String, Object> result3 = (Map<String, Object>) i3.next();
					if(hours<6||hours>18){//夜晚
						String sbsl = result3.get("YWSBSL")==null?"":result3.get("YWSBSL").toString();
						String sbzs = result3.get("YWSBZS")==null?"":result3.get("YWSBZS").toString();
						if(sbzs!=null&&!sbzs.equals("")&&!sbzs.equals("0") && !"".equals(sbzs)){
							double zs = Double.parseDouble(sbzs);
							double sb = Double.parseDouble(sbsl);
							double sbl = (double)(Math.round((sb/zs)*100));
							result.put("SBL", sbl+"%(夜晚识别率)");
						}else{
							result.put("SBL", "无数据");
						}
					}else{
						String sbsl = result3.get("BTSBSL")==null?"":result3.get("BTSBSL").toString();
						String sbzs = result3.get("BTSBZS")==null?"":result3.get("BTSBZS").toString();
						if(sbzs!=null&&!sbzs.equals("")&&!sbzs.equals("0") && !"".equals(sbzs)){
							double zs = Double.parseDouble(sbzs);
							double sb = Double.parseDouble(sbsl);
							double sbl = (double)(Math.round((sb/zs)*100));
							result.put("SBL", sbl+"%(白天识别率)");
						}else{
							result.put("SBL", "无数据");
						}
					}
				}
			}
		}
		//map.put("rows",list);
		return list;
	}
	
	private String getSql(Map<String,Object> filter,VehPassrec veh){
		StringBuffer sb = new StringBuffer("SELECT gcxh,kkbh,hphm,hpys,hpzl,zt,csys,rksj,czr,czsj,jdcsyr,xzqh,xxly,by1 FROM jm_zyk_xy_false");//trunc((rksj-gcsj),2) as sjc,
		sb.append(" WHERE 1=1 ");
		if (veh.getHpzl() != null && !"".equals(veh.getHpzl())) {
			if (!veh.getHpzl().equals(""))
				sb.append(" and hpzl='" + veh.getHpzl() + "'");
		}
		if (veh.getHphm() != null && !"".equals(veh.getHphm())) {
			if (!veh.getHphm().equals(""))
				sb.append(" and hphm='" + veh.getHphm() + "'");
		}
		if (veh.getHpys() != null && !"".equals(veh.getHpys())) {
			if (!veh.getHpys().equals(""))
				sb.append(" and hpys='" + veh.getHpys() + "'");
		}
		if (veh.getKssj() != null && !"".equals(veh.getKssj())) {
			if (!veh.getKssj().equals(""))
				sb.append("  AND rksj >= to_date('" + veh.getKssj() + "','yyyy-mm-dd hh24:mi:ss')");
		}
		if (veh.getJssj() != null && !"".equals(veh.getJssj())) {
			if (!veh.getJssj().equals(""))
				sb.append(" AND rksj < to_date('" + veh.getJssj() + "','yyyy-mm-dd hh24:mi:ss')");
		}		
		sb.append(" order by rksj desc");
		return sb.toString();
	}

	@Override
	public List<Map<String, Object>> queryXyFalseListExt(Map<String, Object> filter)throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		VehPassrec veh = new VehPassrec();
		veh.setKssj(filter.get("kssj").toString());
		veh.setJssj(filter.get("jssj").toString());
		veh.setHphm(filter.get("hphm").toString());
		veh.setHpzl(filter.get("hpzl").toString());
		String sql = getSql(filter, veh);
		List<Map<String, Object>> queryList = this.falseDao.queryXyFalseListExt(filter, sql);
		if(queryList.size() == 0){
			return queryList;
		}
		for(Iterator i = queryList.iterator();i.hasNext();){
			Map<String,Object> result = (Map<String,Object>)i.next();
			result.put("HPZL", result.get("hpzl"));
			result.put("HPHM", result.get("hphm"));
			result.put("HPZLMC", this.systemDao.getCodeValue("030107",result.get("HPZL")==null?"":result.get("HPZL").toString()));
			result.put("KKMC", this.gateDao.getOldGateName(result.get("KKBH").toString()));
			result.put("HPYSMC", this.systemDao.getCodeValue("031001", result.get("HPZL")==null?"":result.get("HPYS").toString()));
			result.put("CSYSMC",this.systemDao.getCodeValue("030108", result.get("csys")==null?"":result.get("csys").toString()));
			result.put("ZT", result.get("zt"));
			result.put("RKSJ", result.get("rksj")==null?"":sdf.format(result.get("rksj")));
			result.put("CZR", result.get("czr"));
			result.put("CZSJ", result.get("czsj")==null?"":sdf.format(result.get("czsj")));
			result.put("xxly", result.get("xxly")==null?"":result.get("xxly").toString());
		}
		return queryList;
	}

	@Override
	public Integer queryXyFalseListTotal(Map<String, Object> filter)
			throws Exception {
		VehPassrec veh = new VehPassrec();
		veh.setKssj(filter.get("kssj").toString());
		veh.setJssj(filter.get("jssj").toString());
		veh.setHphm(filter.get("hphm").toString());
		veh.setHpzl(filter.get("hpzl").toString());
		String sql = getSql(filter, veh);
		return this.falseDao.queryXyFalseListTotal(filter, sql);
	}
	
	
}
