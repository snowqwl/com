package com.sunshine.monitor.system.alarm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sunshine.monitor.comm.bean.Page;
import com.sunshine.monitor.comm.bean.SqlConditionFactory;
import com.sunshine.monitor.comm.bean.SqlConditionFactory.SqlCondition;
import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.util.AlarmSignStau;
import com.sunshine.monitor.comm.util.BusinessType;
import com.sunshine.monitor.comm.util.InformationSource;
import com.sunshine.monitor.system.activemq.bean.TransAlarm;
import com.sunshine.monitor.system.activemq.bean.TransCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmCmd;
import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmLivetrace;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.AlarmDao;
import com.sunshine.monitor.system.alarm.service.VehAlarmHandleManager;
import com.sunshine.monitor.system.alarm.service.VehAlarmManager;
import com.sunshine.monitor.system.gate.bean.CodeGate;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.BusinessLog;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.manager.service.SysparaManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

@Transactional
@Service("vehAlarmManager")
public class VehAlarmManagerImpl implements VehAlarmManager {
	
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private AlarmDao alarmDao;

	@Autowired
	private SystemDao systemDao;

	@Autowired
	private SuspinfoManager suspinfoManager;

	@Autowired
	private LogManager logManager;

	@Autowired
	private VehAlarmHandleManager vehAlarmHandleManager;
	
	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;

	@Autowired
	@Qualifier("sysparaManager")
	private SysparaManager sysparaManager;

	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;

	@Autowired
	private UrlDao urlDao;
	
	public Map<String, Object> getAlarmList(String kdbh,Page page) throws Exception {
		//获取预警信息列表
		Map<String,Object> map = this.alarmDao.getAlarmList(kdbh,page);
		//翻译字段
		List<Map<String,Object>> list =(List<Map<String,Object>>)map.get("rows");
		for(Map<String,Object> m:list){
			if(m.get("HPZL")!=null&&!StringUtils.isEmpty(m.get("HPZL").toString())){
				m.put("HPZLMC",this.systemDao.getCodeValue("030107",m.get("HPZL").toString()));
			}else{
				m.put("HPZLMC","未知");
			}
			if(m.get("SQSB")!=null&&!StringUtils.isEmpty(m.get("SQSB").toString())){
				m.put("SQSBMC",this.systemDao.getCodeValue("190001",m.get("SQSB").toString()));
			}else{
				m.put("SQSBMC","未知");
			}
			String bkjbmc = "";
			int s =Integer.parseInt(m.get("BKJB").toString());
			switch(s){
				case 1:bkjbmc = "一级";break;
				case 2:bkjbmc = "二级";break;
				case 3:bkjbmc = "三级";break;
				case 4:bkjbmc = "四级";break;
			}
			m.put("BKJBMC",bkjbmc);
			m.put("CLTZ",m.get("CLTZ")==null?"无":m.get("CLTZ"));
			m.put("CLPP",m.get("CLPP")==null?"无":m.get("CLPP"));
			m.put("CLSD",m.get("Clsd")==null?"无":m.get("CLSD"));
		}
		map.put("rows",list);
		return map;
	}
	
	@OperationAnnotation(type = OperationType.ALARM_SIGN_QUERY, description = "预警签收查询")
	public Map<String, Object> getAlarmsignList(Map<String, Object> conditions)
			throws Exception {
		Map<String,Object> map =  this.alarmDao.getAlarmList(conditions, null, null);
		List<Map<String,Object>> list =(List<Map<String,Object>>)map.get("rows");
		if(list == null || list.size() < 1) {
			return map;
		}
        for(int i = 0 ;i<list.size();i++){
        	Map<String,Object> rec = list.get(i);
        	CodeGate gate = this.gateManager.getGate(rec.get("KDBH").toString());
        	if(gate!=null){
            	rec.put("KKZT",gate.getKkzt());
        	}else{
        		rec.put("KKZT","");
        	}
        }
        map.put("rows",list);
		return map;
	}

	public Map<String, Object> getAlarmsignListInfo(
			Map<String, Object> conditions) throws Exception {
		return this.alarmDao.getAlarmListInfo(conditions, null, null);
	}

	public VehAlarmrec getAlarmsign(String bjxh) throws Exception {

		return this.alarmDao.getVehAlarmrec(bjxh);
	}

	public VehAlarmrec getAlarmsignDetail(String bjxh) throws Exception {
		VehAlarmrec bean = this.alarmDao.getVehAlarmrec(bjxh);
		if (bean != null) {
			Code code = null;
			code = this.systemDao.getCode("030107", bean.getHpzl());
			if (code != null) {
				bean.setHpzlmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120019", bean.getBjdl());
			if (code != null) {
				bean.setBjdlmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120005", bean.getBjlx());
			if (code != null) {
				bean.setBjlxmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120014", bean.getQrzt());
			if (code != null) {
				bean.setQrztmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("130004", bean.getJyljtj());
			if (code != null) {
				bean.setLjtjmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120012", bean.getXxly());
			if (code != null) {
				bean.setXxly(code.getDmsm1());
			}
			code = this.systemDao.getCode("031001", bean.getHpys());
			if (code != null) {
				bean.setHpys(code.getDmsm1());
			}
			code = this.systemDao.getCode("030108", bean.getCsys());
			if (code != null) {
				bean.setCsysmc(code.getDmsm1());
			}
			bean.setTp1(bean.getTp1().replaceAll("\\\\", "/"));
			bean.setTp2(bean.getTp2().replaceAll("\\\\", "/"));
			bean.setTp3(bean.getTp3().replaceAll("\\\\", "/"));
		}
		return bean;
	}

	public VehAlarmrec getCityAlarmsignDetail(String bjxh, String cityname)
			throws Exception {
		VehAlarmrec bean = this.alarmDao.getCityVehAlarmrec(bjxh, cityname);
		if (bean != null) {
			Code code = null;
			code = this.systemDao.getCode("030107", bean.getHpzl());
			if (code != null) {
				bean.setHpzlmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120019", bean.getBjdl());
			if (code != null) {
				bean.setBjdlmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120005", bean.getBjlx());
			if (code != null) {
				bean.setBjlxmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120014", bean.getQrzt());
			if (code != null) {
				bean.setQrztmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("130004", bean.getJyljtj());
			if (code != null) {
				bean.setLjtjmc(code.getDmsm1());
			}
			code = this.systemDao.getCode("120012", bean.getXxly());
			if (code != null) {
				bean.setXxly(code.getDmsm1());
			}
			code = this.systemDao.getCode("031001", bean.getHpys());
			if (code != null) {
				bean.setHpys(code.getDmsm1());
			}
			code = this.systemDao.getCode("030108", bean.getCsys());
			if (code != null) {
				bean.setCsysmc(code.getDmsm1());
			}
			bean.setTp1(bean.getTp1().replaceAll("\\\\", "/"));
			bean.setTp2(bean.getTp2().replaceAll("\\\\", "/"));
			bean.setTp3(bean.getTp3().replaceAll("\\\\", "/"));
		}
		return bean;
	}

	@OperationAnnotation(type = OperationType.ALARM_SIGN, description = "预警签收")
	public Map<String, Object> doTransactionalSaveAlarmSign(
			VehAlarmrec vehAlarmrec) throws Exception {
		VehSuspinfo vehSuspinfo = this.suspinfoManager.getSuspInfo(vehAlarmrec
				.getBkxh());
		Map<String, Object> result = new HashMap<String, Object>();
		Assert.notNull(vehSuspinfo, "未找到对应的布控信息");
		/** 保存预警签收信息 */
		int flag = saveAlarmSign(vehAlarmrec);
		if (flag > 0) {
			/** 写日志 */
			BusinessLog log = new BusinessLog();
			log.setYwxh(vehAlarmrec.getBjxh());
			log.setYwlb(BusinessType.ALARM_CONFIRM.getCode());
			log.setYwjb(vehSuspinfo.getBkjb());
			log.setBzsj(vehAlarmrec.getBjsj());
			log.setCzrdh(vehAlarmrec.getQrr());
			log.setCzrjh(vehAlarmrec.getQrrjh());
			log.setCzrdwdm(vehAlarmrec.getQrdwdm());
			log.setCzrdwjz(vehAlarmrec.getQrrdwjz());
			this.logManager.saveBusinessLog(log);

			/** 写同步传输表 */
			Syspara syspara = this.sysparaManager.getSyspara("xzqh", "1", "");
			String xzqh = syspara.getCsz();
			this.saveTransAlarm(vehAlarmrec, vehSuspinfo, xzqh);

			/** 边界预警 写入传输表,联动布控做边界预警信息推送 */
			if (AlarmSignStau.SIGN_VALID.getCode()
					.equals(vehAlarmrec.getQrzt()) && InformationSource.LINKAGE_WRITER.getCode().equals(
							vehSuspinfo.getXxly())) {
				/** 有效预警 */
				VehAlarmrec v = getAlarmsign(vehAlarmrec.getBjxh());
				CodeGate codeGate = this.gateManager.getGate(v.getKdbh());
				if (codeGate != null) {
					String bjcs = codeGate.getBjcs();
					/** 卡口某方向配置边界城市 */
					if (bjcs != null && !"".equals(bjcs)) {
							// 联动边界城市
							StringTokenizer st = new StringTokenizer(bjcs,",");
							String[] bkfws = vehSuspinfo.getBkfw().split(",");
							List<String> bjfwList = Arrays.asList(bkfws);
							boolean is = false;
							while(st.hasMoreTokens()){
								String bj = st.nextToken();
								if(bjfwList.indexOf(bj) != -1){
									is = true;
									break;
								}
							}
							if (xzqh.equals(vehSuspinfo.getBkpt()) && is) {
								this.saveBoundaryTransAlarm(codeGate.getBjcs(),
										vehAlarmrec.getBjxh(), xzqh, "28");
							}
					}
				} else {
					/** 卡口备案信息与报警记录中的卡口和方向信息不一致 */
					Assert.notNull(codeGate, "未维护卡口方向信息,写入传输表失败!");
				}
			}
			/** 保存到临时表(签收修改) */
			VehAlarmrec temp = this.alarmDao.getVehAlarmrec(vehAlarmrec
					.getBjxh());
			this.saveAlarmandSigntoTemptable(temp);
			result.put("code", "1");
			result.put("msg", "预警签收成功"+vehSuspinfo.getBkjb());
		}
		return result;
	}

	public boolean saveBoundaryTransAlarm(String bkfw, String bjxh,
			String xzqh, String type) throws Exception {

		return this.alarmDao.saveBoundaryTransAlarm(bkfw, bjxh, xzqh, type);
	}

	public int saveAlarmSign(VehAlarmrec vehAlarmrec) throws Exception {

		return this.alarmDao.saveAlarmSign(vehAlarmrec);
	}

	/**
	 * 保存告警信息(联动) 支持事务
	 */
	public int saveAlarmLink(TransAlarm bean) throws Exception {
		VehSuspinfo suspinfo = this.suspinfoManager.getSuspInfo(bean.getBkxh());
		if (suspinfo != null) {
			// 更新布控表的报警状态为1
			suspinfo.setBjzt("1");
			// 更新布控信息
			this.suspinfoManager.updateSuspinfo(suspinfo);
			// 保存预警信息
			bean.setXxly("2"); // 改为联动
			this.alarmDao.saveAlarmLink(bean);

		}
		return 0;
	}

	/**
	 * 保存边界预警
	 */
	public int saveBoundaryAlarm(TransAlarm transAlarm) throws Exception {
		transAlarm.setQrdwdm(null);
		transAlarm.setQrdwdmmc(null);
		transAlarm.setQrdwlxdh(null);
		transAlarm.setQrjg(null);
		transAlarm.setQrrjh(null);
		transAlarm.setQrrmc(null);
		transAlarm.setQrzt("0");
		transAlarm.setQrsj(null);
		this.alarmDao.saveAlarmLink(transAlarm);
		return 1;
	}

	/**
	 * 保存告警签收信息(联动)
	 */
	public int saveAlarmsureLink(TransAlarm bean) throws Exception {
		// 保存预警签收信息
		bean.setXxly("2"); // 改为联动
		return this.alarmDao.saveAlarmLink(bean);
	}

	public TransAlarm getTransAlarm(String ywxh) throws Exception {
		return this.alarmDao.getTransAlarmDetail(ywxh);
	}

	public boolean saveTransAlarm(VehAlarmrec vehAlarmrec,
			VehSuspinfo vehSuspinfo, String xzqh) throws Exception {

		return this.alarmDao.saveTransAlarm(vehAlarmrec, vehSuspinfo, xzqh);
	}
	
	@OperationAnnotation(type = OperationType.ALARM_CMD_QUERY, description = "下达指令查询")
	public Map<String, Object> getAlarmListForCmd(Map<String, Object> conditions)
			throws Exception {
		Map<String, Object> map = this.alarmDao.getAlarmList(conditions,null,null);
		List list = (List) map.get("rows");
		for (int i = 0; i < list.size(); i++) {
			Map maplist = (Map) list.get(i);
			String bjxh = maplist.get("BJXH").toString();
			List<VehAlarmCmd> listcmd = alarmDao.getCmdlist(bjxh);
			if (listcmd.size() > 0)
				maplist.put("ZLSJ", listcmd.get(0).getZlsj());
        	CodeGate gate = this.gateManager.getGate(maplist.get("KDBH").toString());
        	if(gate!=null){
        		maplist.put("KKZT",gate.getKkzt());
        	}else{
        		maplist.put("KKZT","");
        	}
		}
        map.put("rows",list);

		return map;
	}

	public List<VehAlarmCmd> getCmdlist(String bjxh, boolean isQueryCmdScope)
			throws Exception {
		List<VehAlarmCmd> list = this.alarmDao.getCmdlist(bjxh);
		for (Iterator<VehAlarmCmd> it = list.iterator(); it.hasNext();) {
			VehAlarmCmd bean = (VehAlarmCmd) it.next();
			if (isQueryCmdScope) {
				List<VehAlarmCmd> zlfwList = getCmdScopelist(bean.getZlxh());
				bean.setZlfwList(zlfwList);
			}
		}
		return list;
	}

	public List<VehAlarmCmd> getCityCmdlist(String bjxh, String cityname)
			throws Exception {
		return this.alarmDao.getCityCmdlist(bjxh, cityname);
	}

	public List<VehAlarmCmd> getCmdScopelist(String zlxh) throws Exception {
		List<VehAlarmCmd> zlfwList = this.alarmDao.getCmdScopelist(zlxh);
		for (Iterator<VehAlarmCmd> it = zlfwList.iterator(); it.hasNext();) {
			VehAlarmCmd cmd = (VehAlarmCmd) it.next();
			cmd.setZljsdwmc(this.systemDao.getDepartmentName(cmd.getZljsdw()));
			cmd.setSffkmc(this.systemDao.getCodeValue("130006", cmd.getSffk()));
		}
		return zlfwList;
	}

	public String saveCmd(VehAlarmCmd vehAlarmCmd) throws Exception {

		return this.alarmDao.saveCmd(vehAlarmCmd);
	}

	public String saveCmd(TransCmd vehAlarmCmd) throws Exception {
		vehAlarmCmd.setXxly("2");
		return this.alarmDao.saveCmd(vehAlarmCmd);
	}

	public boolean saveCmdScope(VehAlarmCmd vehAlarmCmd) throws Exception {

		return this.alarmDao.saveCmdScope(vehAlarmCmd);
	}

	public boolean updateAlarmIsxdzl(String bjxh) throws Exception {

		return this.alarmDao.updateAlarmIsxdzl(bjxh);
	}

	@OperationAnnotation(type = OperationType.ALARM_CMD, description = "下达指令")
	public boolean doTransactionalSaveCmd(VehAlarmCmd vehAlarmCmd)
			throws Exception {
		String zlxh = this.saveCmd(vehAlarmCmd);
		if (zlxh != null) {
			vehAlarmCmd.setZlxh(zlxh);
			this.saveCmdScope(vehAlarmCmd);
			this.updateAlarmIsxdzl(vehAlarmCmd.getBjxh());
		}
		return true;
	}
	
	@OperationAnnotation(type = OperationType.ALARM_SIGN_QUERY, description = "预警签收查询")
	public Map<String, Object> getAlarmsigntempList(Map<String, Object> conditions) throws Exception {
		Map<String,Object> map =  this.alarmDao.getAlarmList(conditions,"JM_VEH_ALARMREC", null);
		List<Map<String,Object>> list =(List<Map<String,Object>>)map.get("rows");
        for(int i = 0 ;i<list.size();i++){
        	Map<String,Object> rec = list.get(i);
        	CodeGate gate = this.gateManager.getGate(rec.get("KDBH").toString());
        	if(gate!=null){
            	rec.put("KKZT",gate.getKkzt());
        	}else{
        		rec.put("KKZT","");
        	}
        }
        map.put("rows",list);
		return map;
	}

	public boolean saveAlarmandSigntoTemptable(VehAlarmrec vehAlarmrec)
			throws Exception {

		return this.alarmDao.saveAlarmandSigntoTemptable(vehAlarmrec);
	}

	public Map<String, Object> updateAlarmSign(VehAlarmrec vehAlarmrec)
			throws Exception {

		return this.alarmDao.updateAlarmSign(vehAlarmrec);
	}

	/**
	 * 加入事务处理 因考核问题:不插入bussiness_log表
	 */
	public Map<String, Object> doTransactionalUpdateAlarmSign(
			VehAlarmrec vehAlarmrec) throws Exception {
		// 已下达指令无法修改
		VehAlarmrec temp = this.alarmDao.getVehAlarmrec(vehAlarmrec.getBjxh());
		if ("1".equals(temp.getSfxdzl())) {
			Map<String, Object> tempmap = new HashMap<String, Object>();
			tempmap.put("code", "0");
			tempmap.put("msg", "预警已下达指令无法修改!");
			return tempmap;
		}
		// 更新预警签收信息
		Map<String, Object> map = updateAlarmSign(vehAlarmrec);
		String code = (String) map.get("code");
		if ("1".equals(code)) {
			VehSuspinfo vehSuspinfo = this.suspinfoManager
					.getSuspDetail(vehAlarmrec.getBkxh());
			// 写同步传输表
			Syspara syspara = this.sysparaManager.getSyspara("xzqh", "1", "");
			this.saveTransAlarm(vehAlarmrec, vehSuspinfo, syspara.getCsz());
			// 删除报警副本
			this.alarmDao.deleteTimeoutTempAlarmsign(vehAlarmrec.getBjxh());
		}
		return map;
	}
	
	@OperationAnnotation(type=OperationType.HANDLE_SIGN_QUERY,description="反馈签收查询")
	public Map<String, Object> queryAlarmhandleConfirmlist(
			Map<String, Object> conditions) throws Exception {

		Map<String, Object> result = this.alarmDao
				.queryAlarmhandleConfirmlist(conditions);
		List<Map<String, Object>> list = (List<Map<String, Object>>) result
				.get("rows");
		for (Map<String, Object> map : list) {
			Object by1 = map.get("BY1");
			map.put("SFYX", by1);
        	CodeGate gate = this.gateManager.getGate(map.get("KDBH").toString());
        	if(gate!=null){
            	map.put("KKZT",gate.getKkzt());
        	}else{
        		map.put("KKZT","");
        	}
		}
		//result.put("rows", value)
		return result;
	}

	public List<VehAlarmHandled> queryAlarmHandleList(String bjxh)
			throws Exception {
		List<VehAlarmHandled> list = this.alarmDao.queryAlarmHandleList(bjxh);
		for (Iterator<VehAlarmHandled> it = list.iterator(); it.hasNext();) {
			VehAlarmHandled bean = (VehAlarmHandled) it.next();
			bean.setSfljmc(this.systemDao
					.getCodeValue("130002", bean.getSflj()));
		}
		return list;
	}

	public List<VehAlarmHandled> queryCityAlarmHandleList(String bjxh,
			String cityname) throws Exception {
		List<VehAlarmHandled> list = this.alarmDao.queryCityAlarmHandleList(
				bjxh, cityname);
		for (Iterator<VehAlarmHandled> it = list.iterator(); it.hasNext();) {
			VehAlarmHandled bean = (VehAlarmHandled) it.next();
			bean.setSfljmc(this.systemDao
					.getCodeValue("130002", bean.getSflj()));
		}
		return list;
	}

	public List<VehAlarmLivetrace> getLivetraceList(String bjxh)
			throws Exception {

		return this.alarmDao.getLivetraceList(bjxh);
	}

	public List<VehAlarmLivetrace> getCityLivetraceList(String bjxh,
			String cityname) throws Exception {
		return this.alarmDao.getCityLivetraceList(bjxh, cityname);
	}
	
	@OperationAnnotation(type=OperationType.HANDLE_SIGN,description="反馈签收")
	public int saveHandledConfirm(VehAlarmHandled vehAlarmHandled)
			throws Exception {
		int result = -1;
		result = this.alarmDao.saveHandledConfirm(vehAlarmHandled);
		// 写同步传输表
		VehSuspinfo vehSuspinfo = this.suspinfoManager
				.getSuspDetail(vehAlarmHandled.getBkxh());
		// 属于联动并且为拦截成功的布控信息
		if ("2".equals(vehSuspinfo.getXxly())
				&& "1".equals(vehAlarmHandled.getSflj())) {
			Syspara syspara = sysparaManager.getSyspara("xzqh", "1", "");
			String csdw = syspara.getCsz();
			result = this.vehAlarmHandleManager.insertTransTabForHandle(csdw,
					vehSuspinfo.getBkfw(), vehAlarmHandled.getFkbh(), "26",
					"jm_trans_alarm");
		}
		return result;
	}

	public VehAlarmHandled queryAlarmHandled(String fkbh) throws Exception {
		VehAlarmHandled vehAlarmHandled = this.alarmDao.queryAlarmHandled(fkbh);
		String fkdwdm = vehAlarmHandled.getBy3();
		String fkdwdmmc = this.systemDao.getDepartmentName(fkdwdm);
		vehAlarmHandled.setBy3(fkdwdmmc);
		return vehAlarmHandled;
	}

	@OperationAnnotation(type = OperationType.INTEGRATION_ALARM_QUERY, description = "报警信息查询")
	public Map<String, Object> getAlarmList(Map<String, Object> conditions,
			UserSession userSession) throws Exception {
		String user = userSession.getSysuser().getYhdh();
		String glbm = userSession.getDepartment().getGlbm();
		Map<String, Object> map = null;
		/*if (conditions.get("city") == null
		|| "".equals(conditions.get("city").toString())
		|| "430000".equals(conditions.get("city").toString())) {
	
} else {*/
	//String city = conditions.get("city").toString();
	//String cityname = this.systemDao.getCodeValue("105000", city);
		putValue(conditions);
		conditions.put("glbm", glbm);
		if (StringUtils.isNotBlank((String)conditions.get("cityname"))) {
		map = this.alarmDao.getAlarmList(conditions, "veh_alarmrec@"
				+ conditions.get("cityname").toString(), user);
		}else{
			conditions.remove("cityname");
			map = this.alarmDao.getAlarmList(conditions, null, user);
			List<Map<String,Object>> list =(List<Map<String,Object>>)map.get("rows");
	        for(int i = 0 ;i<list.size();i++){
	        	Map<String,Object> rec = list.get(i);
	        	CodeGate gate = this.gateManager.getGate(rec.get("KDBH").toString());
	        	if(gate!=null){
	            	rec.put("KKZT",gate.getKkzt());
	        	}else{
	        		rec.put("KKZT","");
	        	}
	        }				
		}
	//}
		return map;
	}
	

	private void putValue(Map<String, Object> conditions) throws Exception{
		String cityDwdm = (String)conditions.get("city");
		String _kdbh = (String)conditions.get("kdbh");
		if (StringUtils.isNotBlank(cityDwdm)) {
			Code code = this.systemManager.getCode("105000", cityDwdm);
			/**DBLINK名称*/
			String cityname = code.getDmsm1();
			/**DMSM3本地查询标志*/
			String flag = code.getDmsm3();
			/**转换标记,需要转换单位代码(高支队单位430000870000转成431500)*/
			String zhdwdm = code.getDmsm4();
			if(StringUtils.isNotBlank(cityname)){
				/**如果FLAG为1则按本地库查询*/
				if("1".equals(flag)){
					/**转换级别高于>卡口条件，则不在模糊查询*/
					if(!"".equals(zhdwdm) && _kdbh == null){
						conditions.put("citylike", zhdwdm);
					} else if(StringUtils.isBlank(_kdbh)){
						conditions.put("citylike", cityDwdm.substring(0,4));
					} 
					cityname = "";
				}
			}
			conditions.put("cityname", cityname);
			
		}
	}

	@OperationAnnotation(type = OperationType.INTEGRATION_ALARM_QUERY, description = "报警信息查询")
	public Map<String, Object> getConfirmOutList(
			Map<String, Object> conditions, UserSession userSession)
			throws Exception {
		String user = userSession.getSysuser().getYhdh();
		return this.alarmDao.getConfirmOutList(conditions);
	}

	@OperationAnnotation(type = OperationType.INTEGRATION_ALARM_QUERY, description = "报警信息查询")
	public Map<String, Object> getAlarmListByHphm(
			Map<String, Object> conditions, UserSession userSession)
			throws Exception {
		String user = userSession.getSysuser().getYhdh();
		Map<String, Object> map = this.alarmDao.getAlarmListByHphm(conditions);
		return map;
	}

	public List<VehAlarmHandled> queryAlarmhandleConfirmed(String bjxh)
			throws Exception {
		List<VehAlarmHandled> list = this.alarmDao
				.queryAlarmhandleConfirmed(bjxh);
		for (VehAlarmHandled vh : list) {
			vh.setBy3(this.systemDao.getDepartmentName(vh.getBy3()));
		}
		return list;
	}

	public List<VehAlarmHandled> queryCityAlarmhandleConfirmed(String bjxh,
			String cityname) throws Exception {
		List<VehAlarmHandled> list = this.alarmDao.queryCityAlarmhandleConfirmed(bjxh, cityname);
		for (VehAlarmHandled vh : list) {
			vh.setBy3(this.systemDao.getDepartmentName(vh.getBy3()));
		}
		return list;
	}

	public List<VehAlarmrec> getAlarmList(String bkxh) throws Exception {
		List<VehAlarmrec> list = this.alarmDao.getAlarmList(bkxh);
		for (Iterator<VehAlarmrec> it = list.iterator(); it.hasNext();) {
			VehAlarmrec bean = (VehAlarmrec) it.next();
			if ((bean.getHpzl() != null) && (!"".equals(bean.getHpzl()))) {
				String tmp = this.systemDao.getCodeValue("030107", bean
						.getHpzl());
				if ((tmp != null) && (!"".equals(tmp))) {
					bean.setHpzl(tmp);
				}
			}
			if ((bean.getBjdl() != null) && (!"".equals(bean.getBjdl()))) {
				String tmp = this.systemDao.getCodeValue("120019", bean
						.getBjdl());
				if ((tmp != null) && (!"".equals(tmp))) {
					bean.setBjdl(tmp);
				}
			}
			if ((bean.getBjlx() != null) && (!"".equals(bean.getBjlx()))) {
				String tmp = this.systemDao.getCodeValue("120005", bean
						.getBjlx());
				if ((tmp != null) && (!"".equals(tmp))) {
					bean.setBjlx(tmp);
				}
			}
			if ((bean.getQrzt() != null) && (!"".equals(bean.getQrzt()))) {
				String tmp = this.systemDao.getCodeValue("120014", bean
						.getQrzt());
				if ((tmp != null) && (!"".equals(tmp))) {
					bean.setQrztmc(tmp);
				}
			}

		}
		return list;
	}

	public Date getDbNowTime() {
		return alarmDao.getDbNowDate();
	}

	public int getOverTimeAlarmCount() throws Exception {
		List<SqlCondition> conditions = new ArrayList<SqlCondition>();
		Date now = getDbNowTime();
		Date overDate = new Date(now.getTime() - VehAlarmManager.overTime);
		conditions.add(SqlConditionFactory.createSqlCondition("bjsj",
				SqlConditionFactory.Operation.LT, overDate));
		return alarmDao.getAlarmCount(conditions);
	}

	public int getOverTimeAlarmCountInThisMonth(String glbm) throws Exception {
		/*
		 * List<SqlCondition> conditions = new ArrayList<SqlCondition>(); Date
		 * now = getDbNowTime(); Date overDate = new
		 * Date(now.getTime()-VehAlarmManager.overTime); Calendar start =
		 * Calendar.getInstance(TimeZone.getDefault(),Locale.CHINA);
		 * start.setTime(new Date()); start.set(start.get(Calendar.YEAR),
		 * start.get(Calendar.MONTH), 1, 0,0);
		 * conditions.add(SqlConditionFactory.createSqlCondition("bjsj",
		 * SqlConditionFactory.Operation.GT, start.getTime()));
		 * conditions.add(SqlConditionFactory.createSqlCondition("bjsj",
		 * SqlConditionFactory.Operation.LT, overDate)); return
		 * alarmDao.getAlarmCount(conditions);
		 */
		return alarmDao.getAlarmSignOutCount(glbm);
	}

	public int getConfirmOut() throws Exception {
		return alarmDao.getConfirmOut();
	}
	/**
	 * 保存指挥中心反馈签收后发送短信数据
	 * @param dxcondition
	 * @return
	 * @throws Exception
	 */
	
	public int saveDxsj(Map<String, String> dxcondition) throws Exception{
		return alarmDao.saveDxsj(dxcondition);
		}

	public List getNoLJ() throws Exception {
		return alarmDao.getNoLJ();
	}

	public void deleteVehalarec(String bjxh) throws Exception {
		
		this.alarmDao.deleteTimeoutTempAlarmsign(bjxh);
	}
	
	public List<VehAlarmrec> selectToHnKSAlarm(String xzqh) throws Exception {
		
		return this.alarmDao.selectToHnKSAlarm(xzqh);
	}
	
	public List<VehAlarmrec> selectToGdKSAlarm(String xzqh) throws Exception {
		
		return this.alarmDao.selectToGdKSAlarm(xzqh);
	}
}
