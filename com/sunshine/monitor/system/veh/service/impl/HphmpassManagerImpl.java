package com.sunshine.monitor.system.veh.service.impl;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.dao.HphmpassDao;
import com.sunshine.monitor.system.veh.dao.PassrecDao;
import com.sunshine.monitor.system.veh.dao.PassrecOptimizeDao;
import com.sunshine.monitor.system.veh.service.HphmpassManager;


@Service("hphmpassManager")
public class HphmpassManagerImpl implements HphmpassManager {

	@Autowired
	@Qualifier("passrecOptimizeDao")
	private PassrecOptimizeDao passrecOptimizeDao;
	
	@Autowired
	@Qualifier("hphmpassDao")
	private HphmpassDao hphmpassDao;

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;

	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@Autowired
	@Qualifier("urlDao")
	private UrlDao urlDao;
	

	
	
	
	/**
	 * 用户查询提示
	 * @param condition
	 * @return
	 */
	@OperationAnnotation(type = OperationType.HPHM_PASS_QUERY, description = "多号牌过车查询")
	public int queryTips(Map condition) {
		return this.hphmpassDao.queryTips(condition);
	}
	
	/**
	 * 查询过车信息列表
	 * @param conditions
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.INTEGRATION_PASSRC_QUERY, description = "过车信息查询")
	public Map<String, Object> getPassrecList(Map<String, Object> conditions)
			throws Exception {
		return getPassrecForList(conditions);
	}
	
	
	
	
	private Map<String, Object> getPassrecForList(Map<String, Object> conditions)throws Exception {
		Map<String, Object> map = this.hphmpassDao.getPassrecList(conditions,
				null);
		List<VehPassrec> list = (List<VehPassrec>) map.get("rows");
		if (list != null) {
			for (Iterator<VehPassrec> it = list.iterator(); it.hasNext();) {
				VehPassrec v = (VehPassrec) it.next();
				v.setHpzlmc(this.systemDao.getCodeValue("030107", v.getHpzl()));
				v.setHpysmc(this.systemDao.getCodeValue("031001", v.getHpys()));
				v.setXsztmc(this.systemDao.getCodeValue("110005", v.getXszt()));
				v.setCwhpysmc(this.systemDao.getCodeValue("031001", v
						.getCwhpys()));
				v.setHpyzmc(this.systemDao.getCodeValue("031003", v.getHpyz()));
				v.setCllxmc(this.systemDao.getCodeValue("030104", v.getCllx()));
				v.setCsysmc(this.systemDao.getCsys(v.getCsys()));
				//v.setKdmc(this.gateDao.getGateName(v.getKdbh()));
				//v.setKdmc(this.gateManager.getGateName(v.getKdbh()));
				if (conditions.get("cityname") == null
							|| conditions.get("cityname").toString().length() < 1) {
				v.setKdmc(this.gateManager.getGateAndJjName(v.getKdbh(),"t_jj_code_gate"));
					}else{
				v.setKdmc(this.gateManager.getGateAndJjName(v.getKdbh(),"code_gate"));	
				}
				v.setSbmc("");
				v.setFxmc(this.gateManager.getDirectName(v.getFxbh()));
				v.setTp1(v.getTp1().replaceAll("\\\\", "/"));
				v.setTp2(v.getTp2().replaceAll("\\\\", "/"));
				v.setTp3(v.getTp3().replaceAll("\\\\", "/"));
			}
		}
		return map;
		}
}
