package com.sunshine.monitor.system.veh.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.core.util.DateUtil;
import com.sunshine.monitor.comm.util.redis.JedisService;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.veh.bean.PropertyBean;
import com.sunshine.monitor.system.veh.bean.VehRealpass;
import com.sunshine.monitor.system.veh.dao.RealDao;
import com.sunshine.monitor.system.veh.service.RealManager;

@Transactional
@Service("realManager")
public class RealMangerImpl implements RealManager {
	
	@Autowired
	@Qualifier("realDao")
	private RealDao realDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	public VehRealpass getVehRealPassM(String kdbh,String fxbh, String cdbh) {
		
		return this.realDao.getVehRealPassM(kdbh, fxbh, cdbh);
	}
	
	/**
	 * 查询实时过车信息
	 * @param kds
	 * @return
	 */
	@Override
	public List<VehRealpass> getRealPassListRedis(String[] kds) {
		List<VehRealpass> list = new ArrayList<VehRealpass>();
		JedisService jedis = new JedisService();
		for(int i=0; i<kds.length; i++){
			VehRealpass vehrp = null;
			String key = kds[i];
			Map<String,Object> value = jedis.getVehRealPassRedis(key);
			if(value!=null && value.get("isOnline")!=null && "是".equals(value.get("isOnline"))){
				vehrp = buildVehRrealPass(value);
			}
			list.add(vehrp);
		}
		return list;
	}
	
	@Override
	public VehRealpass getVehRealPassRedis(String kdbh,String fxbh, String cdbh) {
		VehRealpass vehrp = null;
		Map<String,Object> value = null;
		JedisService jedis = new JedisService();
		if(cdbh!=null && !"".equals(cdbh)){
			String fxlx = fxbh.substring(12,13);
			value = jedis.getVehRealPassRedis(kdbh, fxlx, cdbh);
		}
		if((cdbh==null||"".equals(cdbh)) && (fxbh!=null && !"".equals(fxbh))){
			String fxlx = fxbh.substring(12,13);
			value = jedis.getVehRealPassRedis(kdbh, fxlx);
		}else{
			value = jedis.getVehRealPassRedis(kdbh);
		}
		if(value!=null && value.get("isOnline")!=null && !"否".equals(value.get("isOnline"))){
			vehrp = buildVehRrealPass(value);
		}
		return vehrp;
	}

	private VehRealpass buildVehRrealPass(Map vehpass){
		VehRealpass vehrp = new VehRealpass();
		vehrp.setGcxh(vehpass.get("gcxh")==null?"":vehpass.get("gcxh").toString());
		vehrp.setDailyCount(vehpass.get("dailyCount")==null?"":vehpass.get("dailyCount").toString());
		vehrp.setKdbh(vehpass.get("gateId")==null?"":vehpass.get("gateId").toString());
		vehrp.setFxlx(vehpass.get("directionType")==null?"":vehpass.get("directionType").toString());
		vehrp.setFxbh(vehpass.get("gateId").toString()+vehpass.get("directionType").toString());
		vehrp.setCdbh(vehpass.get("driverWayId")==null?"":vehpass.get("driverWayId").toString());
		//vehrp.setCdlx(vehpass.get("driverWayType")==null?"":vehpass.get("driverWayType").toString());
		vehrp.setHpzl(vehpass.get("licenseType")==null?"":vehpass.get("licenseType").toString());
		vehrp.setGcsj(vehpass.get("passTime")==null?"":DateUtil.longToTime(Long.parseLong(vehpass.get("passTime").toString())));
		vehrp.setClsd(Long.parseLong(vehpass.get("speed")==null?"":vehpass.get("speed").toString()));
		vehrp.setHpys(vehpass.get("licenseColor")==null?"":vehpass.get("licenseColor").toString());
		vehrp.setCllx(vehpass.get("carType")==null?"":vehpass.get("carType").toString());
		vehrp.setHphm(vehpass.get("license")==null?"":vehpass.get("license").toString());
		vehrp.setCwhphm(vehpass.get("backLicense")==null?"":vehpass.get("backLicense").toString());
		vehrp.setCwhpys(vehpass.get("backLicenseColor")==null?"":vehpass.get("backLicenseColor").toString());
		vehrp.setHpyz(vehpass.get("identical")==null?"":vehpass.get("identical").toString());
		vehrp.setCsys(vehpass.get("carColor")==null?"":vehpass.get("carColor").toString());
		//vehrp.setClxs(Long.parseLong(vehpass.get("limitSpeed")==null?"":vehpass.get("limitSpeed").toString()));
		vehrp.setClpp(vehpass.get("carBrand")==null?"":vehpass.get("carBrand").toString());
		vehrp.setClwx(vehpass.get("carShape")==null?"":vehpass.get("carShape").toString());
		vehrp.setXszt(vehpass.get("travelStatus")==null?"":vehpass.get("travelStatus").toString());
		vehrp.setWfbj(vehpass.get("violationFlag")==null?"":vehpass.get("violationFlag").toString());
		// 关于过车图片的拼接
		String tplj = vehpass.get("picPath")==null?"":vehpass.get("picPath").toString();
		if("".equals(tplj)){
			tplj = "/";
			vehrp.setTp1((vehpass.get("picPath1")==null?tplj:vehpass.get("picPath1").toString()));
			vehrp.setTp2((vehpass.get("picPath2")==null?tplj:vehpass.get("picPath2").toString()));
			vehrp.setTp3((vehpass.get("picPath3")==null?tplj:vehpass.get("picPath3").toString()));
		} else {
			vehrp.setTp1(tplj + (vehpass.get("picPath1")==null?"":vehpass.get("picPath1").toString()));
			vehrp.setTp2(tplj + (vehpass.get("picPath2")==null?"":vehpass.get("picPath2").toString()));
			vehrp.setTp3(tplj + (vehpass.get("picPath3")==null?"":vehpass.get("picPath3").toString()));
		}
		
		vehrp.setXszt(vehpass.get("travelStatus")==null?"":vehpass.get("travelStatus").toString());
		//vehrp.setTztp(vehpass.get("featurePic")==null?"":vehpass.get("featurePic").toString());
		//vehrp.setDrtp1(vehpass.get("driverPic")==null?"":vehpass.get("driverPic").toString());
		//vehrp.setDrtp2(vehpass.get("copilotPic")==null?"":vehpass.get("copilotPic").toString());
		//vehrp.setFsbz(vehpass.get("sendFlag")==null?"":vehpass.get("dailyCount").toString());
		//vehrp.setFzhpzl(vehpass.get("assistantLicenseType")==null?"":vehpass.get("assistantLicenseType").toString());
		//vehrp.setFzhphm(vehpass.get("assistantLicense")==null?"":vehpass.get("assistantLicense").toString());
		//vehrp.setFzhpys(vehpass.get("assistantLicenseColor")==null?"":vehpass.get("assistantLicenseColor").toString());
		//vehrp.setCwkc(vehpass.get("outlineLength")==null?"":vehpass.get("outlineLength").toString());
		return vehrp;
	}
	public Map findAlarmForMap(Map map, PropertyBean bean) throws Exception {
		
		StringBuffer condition = new StringBuffer("where   hphm in(");
		String ss = "";
		if(StringUtils.isNotBlank(bean.getVar1())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar1() + "'";
		}
		
		if(StringUtils.isNotBlank(bean.getVar2())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar2() + "'";
		}
		
		if(StringUtils.isNotBlank(bean.getVar3())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar3() + "'";
		}
		
		if(StringUtils.isNotBlank(bean.getVar4())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar4() + "'";
		}
		
		if(StringUtils.isNotBlank(bean.getVar5())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar5() + "'";
		}
		
		if(StringUtils.isNotBlank(bean.getVar6())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar6() + "'";
		}
		
		if(StringUtils.isNotBlank(bean.getVar7())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar7() + "'";
		}
		
		if(StringUtils.isNotBlank(bean.getVar8())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar8() + "'";
		}
		
		if(StringUtils.isNotBlank(bean.getVar9())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar9() + "'";
		}
		
		if(StringUtils.isNotBlank(bean.getVar10())){
			if(ss.length() > 0) ss += ",";
			ss += "'" + bean.getVar10() + "'";
		}
		
		if(ss.length() == 0) ss = "'null'";
		
		condition.append(ss);
		condition.append(")  ");
		
		if(StringUtils.isNotBlank(bean.getGxsj())){
			condition.append("  and  bjsj >= to_date('").append(bean.getGxsj()).append("','yyyy-mm-dd hh24:mi:ss')  ");
		}else{
			condition.append(" and bjsj > sysdate - 1  ");
		}
		
		return realDao.findAlarmForMap(map,condition);
	}
	/**
	 * 查询实时过车信息
	 * @param kds
	 * @return
	 */
	public List getRealPassList(String[] kds) {
		
		return realDao.getRealPassList(kds);
	}
	/**
	 * (根据参数)查询Veh_passrec表信息
	 * @param gcxh
	 * @return
	 */
	public VehRealpass getVehRealDetail(String gcxh) {
		VehRealpass vr = this.realDao.getVehRealDetail(gcxh);
		try {
			if (vr != null) {
				vr.setCllx(this.systemDao.getCodeValue("030104", vr.getCllx()));

				vr.setFxbh(this.gateManager.getDirectName(vr.getFxbh()));

				vr.setKdbh(this.gateManager.getGateName(vr.getKdbh()));

				vr.setCsys(this.systemDao.getCodeValue("030108", vr.getCsys()));

				vr.setHpys(this.systemDao.getCodeValue("031001", vr.getHpys()));

				vr.setCwhpys(this.systemDao.getCodeValue("031001", vr
						.getCwhpys()));

				vr.setXszt(this.systemDao.getCodeValue("110005", vr.getXszt()));

				vr.setHpzlmc(this.systemDao
						.getCodeValue("030107", vr.getHpzl()));

				vr.setSbmc("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vr;
	}
	
	/**
	 * (根据参数)查询Veh_passrec表信息
	 * @param gcxh
	 * @return
	 */
	public VehRealpass getVehRealDetailByParams(String gcxh) {
		VehRealpass vr = this.realDao.getVehRealDetailByParams(gcxh);
		try {
			if (vr != null) {
				vr.setCllx(this.systemDao.getCodeValue("030104", vr.getCllx()));

				vr.setFxbh(this.gateManager.getDirectName(vr.getFxbh()));

				vr.setKdbh(this.gateManager.getGateName(vr.getKdbh()));

				vr.setCsys(this.systemDao.getCodeValue("030108", vr.getCsys()));

				vr.setHpys(this.systemDao.getCodeValue("031001", vr.getHpys()));

				vr.setCwhpys(this.systemDao.getCodeValue("031001", vr
						.getCwhpys()));

				vr.setXszt(this.systemDao.getCodeValue("110005", vr.getXszt()));

				vr.setHpzlmc(this.systemDao
						.getCodeValue("030107", vr.getHpzl()));

				vr.setSbmc("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vr;
	}
	/**
	 * 查询某卡口今日过车总数
	 * @oy
	 * @param kdbh
	 * @param cdbh 
	 * @param fxbh 
	 * @return
	 */
	public long getVehpassByKdbh(String kdbh, String fxbh, String cdbh){
		return this.realDao.getVehpassByKdbh(kdbh,fxbh,cdbh);
	}
	/**
	 * 查询实时监控信息
	 * @param kdbh
	 * @param fxbh
	 * @return
	 */
	public VehRealpass getJianKongInfo(String kdbh,String fxbh) {
		return this.realDao.getJianKongInfo( kdbh, fxbh);
	}

	public long getDiffTime(String time) throws Exception {
		return this.realDao.getDiffTime(time);
	}
}
