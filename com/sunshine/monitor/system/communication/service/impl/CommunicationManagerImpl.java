package com.sunshine.monitor.system.communication.service.impl;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.system.communication.bean.Communication;
import com.sunshine.monitor.system.communication.dao.CommunicationDao;
import com.sunshine.monitor.system.communication.service.CommunicationManager;
import com.sunshine.monitor.system.gate.bean.CodeDirect;
import com.sunshine.monitor.system.gate.dao.GateDao;
import com.sunshine.monitor.system.gate.service.GateManager;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Service("CommunicationManager")
public class CommunicationManagerImpl implements CommunicationManager {

	@Autowired
	@Qualifier("CommunicationDao")
	private CommunicationDao communicationDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("gateManager")
	private GateManager gateManager;
	
	@Autowired
	@Qualifier("gateDao")
	private GateDao gateDao;
	
	@OperationAnnotation(type = OperationType.INTEGRATION_PASSRC_QUERY, description = "短信查询")
	public Map<String, Object> querySMSlist(Map<String, Object> conditions)
			throws Exception {
		return getSMSForList(conditions);
	}
	
	private Map<String, Object> getSMSForList(Map<String, Object> conditions)
			throws Exception {
		Map<String, Object> map = this.communicationDao.getSMSForList(conditions,
				null);
		List<Communication> queryList = (List<Communication>) map.get("rows");
		for (int i=0;i<queryList.size();i++) {
			Map  temp = (Map) queryList.get(i);
			temp.put("YWLX", this.systemDao.getCodeValue("300000",temp.get("YWLX").toString()));
			temp.put("DXFSSJ", String.valueOf(temp.get("DXFSSJ")));
			//gate.put("KDBH",this.systemDao.getCodeValue("150000",gate.get("KDBH").toString()));
			//gate.put("FXBH",this.systemDao.getCodeValue("150001",gate.get("FXBH").toString()));
			//gate.put("KKZT",this.systemDao.getCodeValue("156003",gate.get("KKZT").toString()));
		}
		map.put("rows", queryList);
		return map;
	}

	public Map<String, Object> queryGatelist(Map<String, Object> conditions)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = this.communicationDao.getGateForList(conditions,null);
		List<CodeDirect> queryList = (List<CodeDirect>) map.get("rows");
		for (int i=0;i<queryList.size();i++) {
			//Object temp =queryList.get(i);
			Map  gate = (Map) queryList.get(i);
			gate.put("KDMC", this.gateManager.getGateName(gate.get("KDBH").toString()));
			gate.put("FXMC", this.gateManager.getDirectName(gate.get("FXBH").toString()));
			gate.put("GXSJ", String.valueOf(gate.get("gxsj")));
		}
		map.put("rows", queryList);
		return map;
	}
	
	public Map<String, Object> queryUserlist(Map<String, Object> conditions)  throws Exception{
		// TODO Auto-generated method stub
		Map<String, Object> map =this.communicationDao.getUserForList(conditions,null);
		List<SysUser> queryList = (List<SysUser>) map.get("rows");
		for(int i=0;i<queryList.size();i++){
			Map user = (Map)queryList.get(i);
			user.put("GXSJ", String.valueOf(user.get("gxsj")));
		}
		map.put("rows",queryList);
		return map;
	}

	public int updateGate(Map gate) throws Exception {
		// TODO Auto-generated method stub
		String []  phoneNumber = gate.get("ywsjhm").toString().split(",");
		boolean flag = true;
		for(int i =0 ; i<phoneNumber.length ; i++){
			if(!isNumeric(phoneNumber[i])){
				flag = false;
				break;
			}
		}
		if(flag){
		return this.communicationDao.updateGate(gate);
		}
		else
			return 0;
	}
	
	
	public int updateUserPhone(Map user) throws Exception{
		// TODO Auto-generated method stub

		return this.communicationDao.updateUserPhone(user);
	}
	
	public int updateCommById(Map sms) throws Exception {

		SmsFacade smsFacade = new SmsFacade();
		String dxjshm = sms.get("dxjshm").toString();
		String dxnr = sms.get("dxnr").toString();
		String id = sms.get("id").toString();
		if (StringUtils.isNotBlank(dxjshm)) {
			/* 发送 */
			boolean flag = smsFacade.sendAndPostSms(dxjshm, dxnr, null);
			if (!flag) {
				this.communicationDao.updateCommuniceZt_2(id, "发送短信失败");
				return 2;
			} else
				return this.communicationDao.updateCommuniceZt_1(id);

		} else {
			this.communicationDao.updateCommuniceZt_2(id, "接收人手机号码为空，短信自动作废");
			return 3;
		}
	}
	
	public boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	
	/**
	 * 通过xh查询短信信息列表
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List getSMSDatesByXh(String xh) throws Exception{
		
		return this.communicationDao.getSMSDatesByXh(xh);
	}
}
