package com.sunshine.monitor.system.query.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.ScsVehPassrecDao;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.UrlManager;
import com.sunshine.monitor.system.query.bean.Surveil;
import com.sunshine.monitor.system.query.bean.VehAlarmrecIntegrated;
import com.sunshine.monitor.system.query.bean.VehPassrecIntegrated;
import com.sunshine.monitor.system.query.dao.IllegalQuerySCSDao;
import com.sunshine.monitor.system.query.dao.QueryListDao;
import com.sunshine.monitor.system.query.service.IntegrateQueryManager;
import com.sunshine.monitor.system.veh.bean.VehPassrec;


@Service
@Transactional
public class IntegrateQueryManagerImpl implements IntegrateQueryManager {
	
	@Autowired
	private QueryListDao queryListDao;

	@Autowired
	private SystemDao systemDao;
	
	@Autowired
	private UrlManager urlManager;
	
	@Autowired
	private ScsVehPassrecDao scsVehPassrecDao;
	
	@Autowired
	private IllegalQuerySCSDao illegalQuerySCSDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	/**
	 * 
	 * 函数功能说明:集中库查询过车数据
	 * @param map
	 * @param info
	 * @param citys
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	@OperationAnnotation(type=OperationType.INTEGRATION_PASSRC_QUERY,description="省集中库过车信息查询")
	public Map getMapForIntegratePass(Map filter, VehPassrec info, String citys)
			throws Exception {
		// TODO Auto-generated method stub
		return queryListDao.getMapForIntegrate(filter, info, citys);
	}


	/**
	 * 
	 * 函数功能说明：查询交通违法记录
	 * @param filter
	 * @param info
	 * @param citys
	 * @param wflxTab 
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	@OperationAnnotation(type=OperationType.TRAFFIC_QUERY,description="交通违法查询")
	public Map<String,Object> getMapForIntegrateTraffic(Map<String,Object> filter, VehPassrec info,
			String citys,String wflxTab) throws Exception {
		//return queryListDao.getMapForIntegrateTraffic(filter, info, citys,wflxTab);
		//return illegalQuerySCSDao.getMapForIntegrateTraffic(filter, info, citys, wflxTab);
		return illegalZYKDao.getMapForIntegrateTraffic(filter, info, citys, wflxTab);
	}
	
	/**
	 * 集中库过车记录查询明细
	 * @param hphm
	 * @param kdbh
	 * @param gcsj
	 * @param cxlx 
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public VehPassrecIntegrated getPassIntegrateDetail(String hphm, String kdbh, String gcsj,String cxlx)throws Exception{
		
		VehPassrecIntegrated info = queryListDao.getIntegratedPassDetail(hphm, kdbh, gcsj, cxlx);
		VehPassrecIntegrated alarm=queryListDao.getAlarmrec(info.getGcxh());
		if (alarm != null) {
			if (alarm.getBjxh() != null) {
				info.setBjxh(alarm.getBjxh());
			}
			if (alarm.getBjdl() != null) {
				List<Code> bjdlList=this.systemDao.getCode("130033");
				for(Object c : bjdlList){
					Code code = (Code)c;
					if(code.getDmz().equals(info.getBjdl())){
						info.setBjdlmc(code.getDmsm1());
					}
				}	
			}
			if (alarm.getBjlx() != null) {
				List<Code> bjlxList=this.systemDao.getCode("130034");
				for(Object c : bjlxList){
					Code code = (Code)c;
					if(code.getDmz().equals(info.getBjlx())){
						info.setBjlxmc(code.getDmsm1());
					}
				}	
			}
			if (alarm.getBjsj() != null) {
				info.setBjsj(alarm.getBjsj());
			}
			if (alarm.getBjdwmc() != null) {
				info.setBjdwmc(alarm.getBjdwmc());
			}
		}
		List cllxList = systemDao.getCodes("030104");
		for(Object c : cllxList){
			Code code = (Code)c;
			if(code.getDmz().equals(info.getCllx())){
				info.setCllxmc(code.getDmsm1());
			}
		}
		
		List<CodeUrl> cityList = this.urlManager.getCodeUrls();
		for(CodeUrl url : cityList){
			if(url.getDwdm().equals(info.getGcxh().substring(0,12))){
				info.setCity(url.getJdmc());
			}
		}
		
		List<Code> hpzlList = this.systemDao.getCode("030107");
		for (Code code : hpzlList) {
			if (code.getDmz().equals(info.getHpzl())) {
				info.setHpzlmc(code.getDmsm1());
			}
		}
		
		List<Code> hpysList = this.systemDao.getCode("031001");
		for (Code code : hpysList) {
			if (code.getDmz().equals(info.getHpys())) {
				info.setHpysmc(code.getDmsm1());
			}
			if (code.getDmz().equals(info.getCwhpys())) {
				info.setCwhpysmc(code.getDmsm1());
			}
		}
		
		info.setCsysmc(systemDao.getCsys(info.getCsys()));
		
		if(info.getCllxmc() == null){
			info.setCllxmc("");
		}
		
		if(info.getCsysmc() == null){
			info.setCsysmc("");
		}
		
		if(info.getRksj() == null){
			info.setRksj("");
		}
		
		if(info.getCdbh() == null){
			info.setCdbh("");
		}
		return info;	
	}
	/**
	 * 查询T_AP_VIO_SURVEIL表信息
	 * @param xh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public Surveil getSuvreitForXh(String xh) {
		
		Surveil surveil = queryListDao.getSuvreitForXh(xh);
		try {
			surveil.setPic(queryListDao.getPicPath(surveil.getXh()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return surveil;
	}
	/**
	 * 
	 * 函数功能说明：查询v_veh_alarmrec表信息
	 * @param filter
	 * @param info
	 * @param citys
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map getMapForIntegrateAlarm(Map filter, VehAlarmrecIntegrated info,
			String citys) throws Exception {
		return this.queryListDao.getMapForIntegrateAlarm(filter, info, citys);
	}
	/**
	 * 查询v_veh_alarmrec表信息
	 * @param bjxh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public VehAlarmrecIntegrated getAlarmIntegrateDetail(String bjxh)
			throws Exception {
		VehAlarmrecIntegrated info = this.queryListDao.getIntegratedAlarmDetial(bjxh);
		List<Code> bjdlList = this.systemDao.getCode("130033");
		for (Object c : bjdlList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBjdl())) {
				info.setBjdlmc(code.getDmsm1());
			}
		}

		List<Code> bjlxList = this.systemDao.getCode("130034");
		for (Object c : bjlxList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBjlx())) {
				info.setBjlxmc(code.getDmsm1());
			}
		}
		
		List<Code> hpzlList = this.systemDao.getCode("030107");
		for (Code code : hpzlList) {
			if (code.getDmz().equals(info.getHpzl())) {
				info.setHpzlmc(code.getDmsm1());
			}
		}
		
		List<Code> hpyzList = this.systemDao.getCodes("031003");
		for(Code code : hpyzList){
			if(code.getDmz().equals(info.getHpyz()))  {
				info.setHpyzmc(code.getDmsm1());
			}
		}
		
		List<Code> hpysList = this.systemDao.getCode("031001");
		for (Code code : hpysList) {
			if (code.getDmz().equals(info.getHpys())) {
				info.setHpysmc(code.getDmsm1());
			}
			if (code.getDmz().equals(info.getCwhpys())) {
				info.setCwhpysmc(code.getDmsm1());
			}
		}
		
		List<CodeUrl> cityList = this.urlManager.getCodeUrls();
		for(CodeUrl url : cityList){
			if(url.getDwdm().equals(info.getGcxh().substring(0,12))){
				info.setCity(url.getJdmc());
			}
		}
		
		return info;
	}
	/**
	 * 查询T_AP_VIO_VIOLATION表信息
	 * @param wfbh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public List<Map<String, Object>> getViolationForWfbh(String dzwzId)throws Exception{
		
		//List<Map<String,Object>> list = this.illegalQuerySCSDao.getViolationForWfbh(dzwzId);
		List<Map<String,Object>> list = this.illegalZYKDao.getViolationForWfbh(dzwzId);
        for(int i = 0;i<list.size();i++){
        	Map<String,Object> map = list.get(i);
        	String wfsj = map.get("wfsj").toString();
			String wfsj_format = wfsj.substring(0, 4)+"-"+wfsj.substring(4, 6)+"-"+
								 wfsj.substring(6, 8)+" "+wfsj.substring(8, 10)+":"+wfsj.substring(10,12)+":"+wfsj.substring(12);
			map.put("wfsj",wfsj_format);
			
			String lrsj = map.get("lrsj").toString();
			String lrsj_format = lrsj.substring(0, 4)+"-"+lrsj.substring(4, 6)+"-"+
								 lrsj.substring(6, 8)+" "+lrsj.substring(8, 10)+":"+lrsj.substring(10,12)+":"+lrsj.substring(12);
			map.put("lrsj",lrsj_format);
			
			String clsj = map.get("clsj")!=null?map.get("clsj").toString():"";
			if(clsj!=null && !"".equals(clsj)){
				String clsj_format = clsj.substring(0, 4)+"-"+clsj.substring(4, 6)+"-"+
									 clsj.substring(6, 8)+" "+clsj.substring(8, 10)+":"+clsj.substring(10,12)+":"+clsj.substring(12);
				map.put("clsj",clsj_format);
			}
        	map.put("HPZLMC",this.systemDao.getCodeValue("030107",map.get("HPZL").toString()));
        	map.put("dzwzid", map.get("DZWZID"));
        	map.put("cjjg",  map.get("CJJG"));
        	map.put("cjjgmc",  map.get("CJJGMC"));
        	map.put("clfl",  map.get("CLFL"));
        	map.put("hphm",  map.get("HPHM"));
        	map.put("fzjg",  map.get("FZJG"));
        	map.put("wfdd",  map.get("WFDD"));
        	map.put("wfdz",  map.get("WFDZ"));
        	map.put("wfxw",  map.get("WFXW"));
        	map.put("fkje",  map.get("FKJE"));
        	map.put("cljgmc",  map.get("CLJGMC"));
        	map.put("cljg",  map.get("CLJG"));
        	map.put("wfbh",  map.get("WFBH"));
        	map.put("dsr",  map.get("DSR"));
        	map.put("zsxxdz",  map.get("ZSXXDZ"));
        	map.put("jdsbh",  map.get("JDSBH"));
        	map.put("jdcsyr",  map.get("JDCSYR"));
        	map.put("lrr",  map.get("LRR"));
        	map.put("zqmj",  map.get("ZQMJ"));
        	
        }		
		return list;
	}
	/**
	 * 查询T_AP_VIO_FORCE表信息
	 * @param xh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public List<Map<String, Object>> getForceForXh(String xh) {
		return  this.queryListDao.getForceForXh(xh);
	}
	
	public Map<String, Object> getTrafficDetail(VehPassrec veh,
			Map<String, Object> filter) throws Exception {
		Map<String,Object> map = this.queryListDao.getTrafficDetail(veh.getHphm(),filter);
		List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("rows");
		for(int i=0;i<list.size();i++){
			Map<String,Object> vio = list.get(i);
			vio.put("HPZLMC",this.systemDao.getCodeValue("030107", vio.get("HPZL").toString()));
		}
		map.put("rows", list);
		return map;
	}
	
    public Map<String,Object> getViolationDetail(CarKey car,Map<String,Object> filter)throws Exception{
        //Map<String,Object> map = this.illegalQuerySCSDao.getViolationDetail(car, filter);
        Map<String,Object> map = this.illegalZYKDao.getViolationDetail2(car, filter);
       
		List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("rows");
		for(int i=0;i<list.size();i++){
			Map<String,Object> vio = list.get(i);
			String wfsj = vio.get("wfsj").toString();
			String wfsj_format = wfsj.substring(0, 4)+"-"+wfsj.substring(4, 6)+"-"+
								 wfsj.substring(6, 8)+" "+wfsj.substring(8, 10)+":"+wfsj.substring(10,12)+":"+wfsj.substring(12);
			vio.put("WFSJ1",wfsj_format);
			vio.put("HPZLMC",this.systemDao.getCodeValue("030107", vio.get("HPZL").toString()));
			vio.put("hphm", vio.get("HPHM"));
			vio.put("wfdz", vio.get("WFDZ"));
			vio.put("cljgmc", vio.get("CLJGMC"));
			vio.put("dzwzid", vio.get("DZWZID"));
		}
		map.put("rows", list);
    	return map;
    }
}
