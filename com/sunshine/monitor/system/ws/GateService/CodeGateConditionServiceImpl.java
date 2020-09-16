package com.sunshine.monitor.system.ws.GateService;

import java.util.List;

import javax.jws.WebService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.ws.GateService.bean.CodeGateEntity;
import com.sunshine.monitor.system.ws.GateService.bean.CodeGateExtendEntity;


@WebService(endpointInterface = "com.sunshine.monitor.system.ws.GateService.CodeGateConditionService", serviceName = "CodeGateConditionService")
@Component("codeGateConditionServiceImpl")
public class CodeGateConditionServiceImpl implements CodeGateConditionService{
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	public List<CodeGateEntity> getGates() {
		String sql = "SELECT * FROM CODE_GATE WHERE KKZT=1 ORDER BY KDBH DESC";
		List<CodeGateEntity> list = null;
		try {
			list = this.systemDao.queryList(sql, CodeGateEntity.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CodeGateExtendEntity> getDirectName(String kdbh) {
		String sql = "SELECT * FROM CODE_GATE_EXTEND WHERE KDBH='" + kdbh +"'";
		List<CodeGateExtendEntity> list = null;
		try {
			list = this.systemDao.queryList(sql,CodeGateExtendEntity.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
