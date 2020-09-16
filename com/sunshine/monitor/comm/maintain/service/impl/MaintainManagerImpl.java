package com.sunshine.monitor.comm.maintain.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.bean.MaintainHandle;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.maintain.service.MaintainManager;
import com.sunshine.monitor.comm.maintain.sms.DefaultSmsTransmitterCallBack;
import com.sunshine.monitor.system.gate.bean.CodeGateExtend;
import com.sunshine.monitor.system.manager.bean.Department;

@Service("maintainManager")
@Transactional
public class MaintainManagerImpl implements MaintainManager {

	@Autowired
	@Qualifier("maintainDao")
	private MaintainDao maintainDao;

	public Map findMaintainForMap(Map filter, String bzlx) throws Exception {

		return maintainDao.findMaintainForMap(filter, bzlx);
	}

	public void setby1ForGate(String kdbh) throws Exception {

		maintainDao.setby1ForGate(kdbh);

	}

	public Map findMaintainHandleForMap(Map filter, MaintainHandle handle,
			Department department) throws Exception {

		return maintainDao.findMaintainHandleForMap(filter, handle, department);
	}

	public MaintainHandle getMaintainHandleForId(String id) throws Exception {

		return maintainDao.getMaintainHandleForId(id);
	}

	private void sendSMS(String phone, String content,
			Map<String, String> dxcondition) {
		SmsFacade smsFacade = new SmsFacade();
		if (phone == null || "".equals(phone)) {
			dxcondition.put("zt", "2");
			dxcondition.put("sysdate", "''");
			dxcondition.put("reason", "手机号码为空，短信自动作废");
		} else {
			try {
				DefaultSmsTransmitterCallBack callBack = new DefaultSmsTransmitterCallBack();
				boolean flag = smsFacade.sendAndPostSms(phone, content,
						callBack);
				if (flag) {
					dxcondition.put("zt", "1");
					dxcondition.put("sysdate", "sysdate");
				} else {
					dxcondition.put("zt", "2");
					dxcondition.put("sysdate", "''");
					dxcondition.put("reason", callBack.getErrorMsg());
				}
			} catch (Exception e) {
				dxcondition.put("zt", "2");
				dxcondition.put("sysdate", "''");
				dxcondition.put("reason", "短信发送失败");
				e.printStackTrace();
			}
		}
	}

	public String saveMaintainHandle(MaintainHandle handle) throws Exception {
		if (StringUtils.isNotBlank(handle.getSfcl())) {
			// 处理状态
			// System.out.println(handle.getSfcl());
			if ("1".equals(handle.getSfcl())) {
				if (StringUtils.isNotBlank(handle.getXh())) {
					MaintainHandle handl = this.maintainDao
							.getMaintainHandleForId(handle.getXh());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");

					// 写入短信管理

					Map<String, String> dxcondition = new HashMap<String, String>();
					dxcondition.put("xh", handl.getXh());
					dxcondition.put("ywlx", "55");// 故障已签收
					dxcondition.put("dxjsr", handl.getSbr());
					dxcondition.put("dxjshm", handl.getYwdh());
					dxcondition.put("dxnr", "您有一条," + handl.getGzqk()
							+ "的故障记录在" + sdf.format(new Date()) + "已被"
							+ handle.getSbrmc() + "签收");
					dxcondition.put("fsfs", "1");
					dxcondition.put("reason", "''");
					String content = "您有一条," + handl.getGzqk() + "的故障记录在"+ sdf.format(new Date()) + "已被"+ handle.getSbrmc() + "签收";
					sendSMS(handl.getYwdh(), content, dxcondition);
					String value = "id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,reason";
					String values = "" + "SEQ_FRM_COMMUNICATION.nextval,'"
							+ dxcondition.get("xh") + "','"
							+ dxcondition.get("ywlx") + "','"
							+ dxcondition.get("dxjsr") + "','"
							+ dxcondition.get("dxjshm") + "','"
							+ dxcondition.get("dxnr") + "',"
							+ dxcondition.get("sysdate") + ",'"
							+ dxcondition.get("zt") + "','"
							+ dxcondition.get("fsfs") + "','"
							+ dxcondition.get("reason") + "'";
					maintainDao.saveDxsj(value, values);

				}
			}
		}
		return maintainDao.saveMaintainHandle(handle);
	}

	public int saveDxsj(String value, String values) throws Exception {

		return this.maintainDao.saveDxsj(value, values);
	}

	public void setCodeGatestate(String kdbh, String fxbh, String kkzt,
			String sbzt) throws Exception {
		this.maintainDao.setCodeGateState(kdbh, fxbh, kkzt, sbzt);
	}

	public List<CodeGateExtend> getGzCodeGateInfo() throws Exception {
		return this.maintainDao.getGzCodeGateInfo();
	}

}
