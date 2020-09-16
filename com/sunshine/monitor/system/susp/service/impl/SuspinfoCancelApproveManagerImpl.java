

package com.sunshine.monitor.system.susp.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.maintain.sms.DefaultSmsTransmitterCallBack;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.activemq.bean.SuspinfoMessage;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.alarm.dao.VehAlarmDao;
import com.sunshine.monitor.system.communication.dao.CommunicationDao;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysparaDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspInfoAuditDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoCancelApproveDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;
import com.sunshine.monitor.system.susp.service.SuspinfoCancelApproveManager;

@Transactional
@Service("suspinfoCancelApproveManager")
public class SuspinfoCancelApproveManagerImpl implements SuspinfoCancelApproveManager {

	@Autowired
	@Qualifier("suspinfoCancelApproveDao")
	private SuspinfoCancelApproveDao suspinfoCancelApproveDao;
	
    @Autowired
    @Qualifier("vehAlarmDao")
	private VehAlarmDao vehAlarmDao;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("sysparaDao")
	private SysparaDao sysparaDao;
	

	@Autowired
	private UrlDao urlDao;
	
	@Autowired
	@Qualifier("suspinfoAuditDao")
	private SuspInfoAuditDao suspinfoAuditDao;
	
	@Autowired
	@Qualifier("suspinfoEditDao")
	private SuspinfoEditDao suspinfoEditDao;
	
	@Autowired
	@Qualifier("maintainDao")
	private MaintainDao maintainDaoImpl;
	
	@Autowired
	@Qualifier("CommunicationDao")
	private CommunicationDao communicationDao;
	/**
	 * 撤控指挥中心审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	@OperationAnnotation(type=OperationType.CSUSP_APPROVAL_QUERY,description="撤控指挥中心审批查询")
	public Map<String, Object> getSuspinfoCancelApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoCancelApproveDao.getSusinfoCancelApproves(filter, info, glbm);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			//vehSupsinfo.put("BKJGMC",this.systemDao.getDepartmentName(glbm));
			vehSupsinfo.put("BJZTMC",this.systemDao.getCodeValue("130009", vehSupsinfo.get("BJZT").toString()));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		return map;
	}
	/**
	 * 撤控指挥中心超时审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	@OperationAnnotation(type=OperationType.CSUSP_APPROVAL_QUERY,description="撤控指挥中心超时审批查询")
	public Map<String, Object> getSuspinfoCancelApprovesOverTime(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoCancelApproveDao.getSusinfoCancelApprovesOverTime(filter, info, glbm);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			//vehSupsinfo.put("BKJGMC",this.systemDao.getDepartmentName(glbm));
			vehSupsinfo.put("BJZTMC",this.systemDao.getCodeValue("130009", vehSupsinfo.get("BJZT").toString()));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		return map;
	}
	
	public VehAlarmDao getVehAlarmDao() {
		return vehAlarmDao;
	}

	public void setVehAlarmDao(VehAlarmDao vehAlarmDao) {
		this.vehAlarmDao = vehAlarmDao;
	}

	public SuspinfoCancelApproveDao getSuspinfoCancelApproveDao() {
		return suspinfoCancelApproveDao;
	}
	
	public void setSuspinfoCancelApproveDao(
			SuspinfoCancelApproveDao suspinfoCancelApproveDao) {
		this.suspinfoCancelApproveDao = suspinfoCancelApproveDao;
	}

	public SystemDao getSystemDao() {
		return systemDao;
	}

	public void setSystemDao(SystemDao systemDao) {
		this.systemDao = systemDao;
	}
	
	public SysparaDao getSysparaDao() {
		return sysparaDao;
	}

	public void setSysparaDao(SysparaDao sysparaDao) {
		this.sysparaDao = sysparaDao;
	}
	
	/**
	 * (根据参数)得到审批详细信息
	 * @param bkxh
	 * @param glbm
	 * @return
	 */
	public Object getApproveDetailForBkxh(String bkxh, String glbm)
			throws Exception {
		VehSuspinfo vehInfo = (VehSuspinfo) this.suspinfoCancelApproveDao.getCancelApprpvesDetailForBkxh(bkxh, glbm);
		
		List<Code> hpzlList = this.systemDao.getCode("030107");
		for (Code code : hpzlList) {
			if (code.getDmz().equals(vehInfo.getHpzl())) {
				vehInfo.setHpzlmc(code.getDmsm1());
			}
		}
		
		List<Code> hpysList = this.systemDao.getCode("031001");
		for (Code code : hpysList) {
			if (code.getDmz().equals(vehInfo.getHpys())) {
				vehInfo.setHpysmc(code.getDmsm1());
			}
		}
		
		List<Code> bkdlList = this.systemDao.getCode("120019");
		for (Code code : bkdlList) {
			if (code.getDmz().equals(vehInfo.getBkdl())) {
				vehInfo.setBkdlmc(code.getDmsm1());
			}
		}
		
        List<Code> cllxList = this.systemDao.getCode("030104");
        for (Code code : cllxList) {
        	if (code.getDmz().equals(vehInfo.getCllx())) {
        		vehInfo.setCllx(code.getDmsm1());
        	}
        }
        
        List<Code> bklbList = this.systemDao.getCode("120005");
        for (Code code : bklbList) {
        	if (code.getDmz().equals(vehInfo.getBklb())) {
        		vehInfo.setBklbmc(code.getDmsm1());
        	}
        }
        
        List<Code> ywztList = this.systemDao.getCode("120008");
        for (Code code : ywztList) {
        	if (code.getDmz().equals(vehInfo.getYwzt())) {
        		vehInfo.setYwztmc(code.getDmsm1());
        	}
        }
        
        List<Code> bkjbList = this.systemDao.getCode("120020");
        for (Code code : bkjbList) {
        	if (code.getDmz().equals(vehInfo.getBkjb())) {
        		vehInfo.setBkjbmc(code.getDmsm1());
        	}
        }
        
        List<Code> bkfwlxList = this.systemDao.getCode("120004");
        for (Code code : bkfwlxList) {
        	if (code.getDmz().equals(vehInfo.getBkfwlx())) {
        		vehInfo.setBkfwlx(code.getDmsm1());
        	}
        }
        
        List<Code> bkxzList = this.systemDao.getCode("120021");
        for (Code code : bkxzList) {
        	if (code.getDmz().equals(vehInfo.getBkxz())) {
        		vehInfo.setBkxzmc(code.getDmsm1());
        	}
        }
        
        List<Code> bjztList = this.systemDao.getCode("130009");
        for (Code code : bjztList) {
        	if (code.getDmz().equals(vehInfo.getBjzt())) {
        		vehInfo.setBjztmc(code.getDmsm1());
        	}
        }
        
        List<Code> ckyydmList = this.systemDao.getCode("120007");
        for (Code code : ckyydmList) {
        	if (code.getDmz().equals(ckyydmList)) {
        		vehInfo.setCkyydmmc(code.getDmsm1());
        	}
        }
        
        List<Code> sqsbList = this.systemDao.getCode("190001");
        for (Code code : sqsbList) {
        	if (code.getDmz().equals(vehInfo.getSqsb())) {
        		vehInfo.setSqsbmc(code.getDmsm1());
        	}
        }
        
        
        if (vehInfo.getCsys() != null && vehInfo.getCsys().length() > 0) {
        	vehInfo.setCsysmc(getCsys(vehInfo.getCsys()));
        }
        
        if (vehInfo.getBkfw() != null && vehInfo.getBkfw().length() > 0)  {
        	vehInfo.setBkfwmc(getBkfw(vehInfo.getBkfw()));
        }
        
        if (vehInfo.getBjfs() != null && vehInfo.getBjfs().length() > 0) {
        	vehInfo.setBjfsmc(getBjfs(vehInfo.getBjfs()));
        }
        
        if (vehInfo.getJyaq() != null && vehInfo.getJyaq().length() > 0) {
        	vehInfo.setJyaq(TextToHtml(vehInfo.getJyaq()));
        }
        
        Map map=communicationDao.getcommunicateByXh(bkxh);
		if(map.get("DXNR")!=null)
			vehInfo.setXjdx(map.get("DXNR").toString());
		else
			vehInfo.setXjdx("");
        
        return vehInfo;
	}
	

	/**
	 *得到车身颜色
	 * @param csys
	 * @return
	 */
	public String getCsys(String csys) throws Exception {
		try {
			if ((csys == null) || (csys.length() < 1))
				return csys;
			String t = csys;
			if (csys.indexOf(",") != -1)
				t = csys.replaceAll(",", "");
			String ccsys = "";
			for (int i = 0; i < t.length(); i++) {
				ccsys = ccsys
						+ this.systemDao.getCodeValue("030108", String.valueOf(t.charAt(i)))
						+ ",";
			}
			if (ccsys.endsWith(","))
				ccsys = ccsys.substring(0, ccsys.length() - 1);
			return ccsys;
		} catch (Exception e) {
		}
		return csys;
	}
	
	/**
	 *得到布控范围
	 * @param bkfw
	 * @return
	 */
	public String getBkfw(String bkfw) throws Exception {
		try {
			if ((bkfw == null) || (bkfw.length() < 1))
				return bkfw;
			String[] temp = bkfw.split(",");
			String bkfws = "";
			List list = this.getBkfwListTree();
			for (int i = 0; i < temp.length; i++) {
				for (Iterator it = list.iterator(); it.hasNext();) {
					CodeUrl cu = (CodeUrl) it.next();
					if (cu.getDwdm().equals(temp[i])) {
						bkfws = bkfws + cu.getJdmc() + ",";
					}
				}
			}
			if (bkfws.endsWith(","))
				bkfws = bkfws.substring(0, bkfws.length() - 1);
			return bkfws;
		} catch (Exception e) {
		}
		return bkfw;
	}
	/**
	 *查询code_url表数据
	 * @param 
	 * @return
	 */
	public List getBkfwListTree() throws Exception {
		return this.suspinfoCancelApproveDao.getBkfwListTree();
	}
	/**
	 *得到报警方式
	 * @param bjfs
	 * @return
	 */
	public String getBjfs(String bjfs) throws Exception {
		try {
			if ((bjfs == null) || (bjfs.length() < 1))
				return bjfs;
			String bjfss = "";
			if (bjfs.charAt(0) == '1')
				bjfss = bjfss + this.systemDao.getCodeValue("130000", "4")
						+ ",";
			if (bjfs.charAt(1) == '1')
				bjfss = bjfss + this.systemDao.getCodeValue("130000", "3")
						+ ",";
			if (bjfs.charAt(2) == '1')
				bjfss = bjfss + this.systemDao.getCodeValue("130000", "2")
						+ ",";
			if (bjfs.charAt(3) == '1')
				bjfss = bjfss + this.systemDao.getCodeValue("130000", "1")
						+ ",";
			if (bjfss.endsWith(","))
				bjfss = bjfss.substring(0, bjfss.length() - 1);
			return bjfss;
		} catch (Exception e) {
		}
		return bjfs;
	}
	/**
	 *特殊字符转义
	 * @param s
	 * @return
	 */
	public String TextToHtml(String s) {
		char[] c = s.toCharArray();

		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (int size = c.length; i < size; i++) {
			char ch = c[i];
			if (ch == '"')
				buf.append("&quot;");
			else if (ch == '&')
				buf.append("&amp;");
			else if (ch == '<')
				buf.append("&lt;");
			else if (ch == '>')
				buf.append("&gt;");
			else if (ch == '\r')
				buf.append("");
			else if (ch == '\n')
				buf.append("<br>");
			else if (ch == ' ')
				buf.append("&nbsp;");
			else {
				buf.append(ch);
			}
		}
		c = (char[]) null;
		return buf.toString();
	}
	/**
	 *得到veh_alarmrec表的信息
	 * @param info
	 * @return Map
	 */
	public List getSuspinfoAlarm(String bkxh) throws Exception {
		if ((bkxh == null) || (bkxh.length() < 1)) {
			return null;
		}
		List list = this.suspinfoCancelApproveDao.getCAlarmHistory(bkxh);
		for (Iterator it = list.iterator(); it.hasNext();) {
			VehAlarmrec var = (VehAlarmrec) it.next();
			var.setQrztmc(this.systemDao.getCodeValue("120014", var.getQrzt()));
			var.setSfxdzlmc(this.systemDao.getCodeValue("130030", var
					.getSfxdzl()));
			var.setSffkmc(this.systemDao.getCodeValue("130031", var.getSffk()));
			var.setSfljmc(this.systemDao.getCodeValue("130032", var.getSflj()));
			var.setXxlymc(this.systemDao.getCodeValue("120012", var.getXxly()));
		}
		return list;
	}
	/**
	 *得到撤控审核/审批信息
	 * @param bkxh
	 * @return List
	 */
	public List getSuspinfoCAuditHistory(String bkxh) throws Exception {
		if ((bkxh == null) || (bkxh.length() < 1)) {
			return null;
		}
		AuditApprove aa = new AuditApprove();
		aa.setBkxh(bkxh);
		aa.setBzw("34");
		List list = this.suspinfoCancelApproveDao.getAuditApproves(aa);
		for (Iterator it = list.iterator(); it.hasNext();) {
			AuditApprove t = (AuditApprove) it.next();
			t.setCzjgmc(this.systemDao.getCodeValue("190002", t.getCzjg()));
			if ((t.getBzw().equals("1")) || (t.getBzw().equals("3")))
				t.setBzw("审核");
			else if ((t.getBzw().equals("2")) || (t.getBzw().equals("4")))
				t.setBzw("审批");
		}
		return list;
	}
	
	/**
	 *撤控指挥中心审批新增
	 * @param info
	 * @return Map
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@OperationAnnotation(type=OperationType.CSUSP_APPROVAL_ADD,description="撤控指挥中心审批新增")
	public Map saveApprove(final AuditApprove info) throws Exception {
		Map result = null;
		final VehSuspinfo vehInfo = (VehSuspinfo) this.suspinfoCancelApproveDao.getCancelApprpvesDetailForBkxh(info.getBkxh(), info.getCzrdw());
		if (vehInfo == null) {
			return Common.messageBox("参数传入异常,未找到相应的对象！", "0");
		}
		Department dp = this.systemDao.getDepartment(info.getCzrdw());
		if (dp == null) {
			return Common.messageBox("参数传入异常,未找到相应的对象！", "0");
		} else {
			if (dp.getSsjz() == null || dp.getSsjz().equals("")) {
				return Common.messageBox("本部门缺少警种信息！", "0");
			}
		}
		try {
			String t_jlzt = "1";
			String t_ywzt = null;
			if (info.getCzjg().equals("0")) {
				t_ywzt = "14";
				
				//短信立刻发送 ---1
				//sendSmsToBkrByCancelApproveNoPass(info, vehInfo);
				new Thread(){
					@Override
					public void run() {
						sendSmsToBkrByCancelApproveNoPass(info, vehInfo);
					}
				}.start();
				
			} else if (info.getCzjg().equals("1")) {
				t_ywzt = "99";
				t_jlzt= "2";
			} 
			
			//保存到审核审批表中
			info.setBzw("4");
			int count  = insertAuditApprove(info);
			//更新布控表信息
			this.updateSuspInfo(t_ywzt, t_jlzt, info.getBkxh());
			
			//保存到传输表
			if (t_jlzt.equals("2") && vehInfo.getBkfwlx().equals("2")) {
				Syspara sysparam = this.sysparaDao.getSyspara("xzqh", "1", "");
				String csz = sysparam.getCsz();
				String bkfw = vehInfo.getBkfw();
				

				//未安装新系统地市按原传输机制传输
				String[]bkfws = vehInfo.getBkfw().split(",");
				for(String jsdw : bkfws){
					CodeUrl url = urlDao.getUrl(jsdw);
					if(StringUtils.isNotBlank(url.getBz() ) && "0".equals(url.getBz().substring(0, 1))){
						this.insertTransSusp_test(csz, jsdw, info.getBkxh(),"13");
					}
				}
				
				this.insertTransSusp(csz, bkfw, info.getBkxh(), "13");
			}
			
			
			
			VehSuspinfo vehSuspinfo = suspinfoEditDao.getSuspinfoDetailForBkxh(info.getBkxh());
			AuditApprove auditApprove = suspinfoEditDao.getAuditApprove(info.getBkxh());
			
			if(t_jlzt.equals("2") && vehInfo.getBkfwlx().equals("3") && "99".equals(vehSuspinfo.getYwzt())){
				
				PropertiesConfiguration config;
				config = new PropertiesConfiguration("ipport.properties");
				String ipGd = config.getString("ipgd");
				String portGd = config.getString("portgd");
				
				//POST的URL
				String url = "http://" + ipGd + ":" + portGd + "/api/gdservice/vehicle/suspService/cancelSuspInfo";
				//建立HttpPost对象
				HttpPost httppost = new HttpPost(url);
				//建立一个NameValuePair数组，用于存储欲传送的参数
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				
				JSONArray json1 = JSONArray.fromObject(vehSuspinfo); 
				JSONArray json2 = JSONArray.fromObject(auditApprove); 
				//添加参数
				params.add(new BasicNameValuePair("vehSuspinfoBean", json1.toString()));
				params.add(new BasicNameValuePair("auditApproveBean", json2.toString()));

				HttpResponse responseSend;
				httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				responseSend = new DefaultHttpClient().execute(httppost);
//				String sendPostResult = EntityUtils.toString(responseSend.getEntity());
//				Map<String, Object> map = (Map<String, Object>)JSON.parse(sendPostResult);
			}
			
			//保存日志表
			insertBusinessLog(vehInfo,info,dp.getSsjz(),"22");
			
			result = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;
	}
	
	
	


	public int insertTransSusp_test(String xzqh,String jsdw,String bkxh,String type)throws Exception{
		
		return suspinfoAuditDao.insertTransSusp_test(xzqh, jsdw, bkxh,type);
	}
	public int insertAuditApprove(AuditApprove info) throws Exception {
		return this.suspinfoCancelApproveDao.saveSuspinfoCancelApprove(info);
	}

	public int insertBusinessLog(VehSuspinfo suspInfo, AuditApprove info,
			String ssjz, String ywlb) throws Exception {
		return this.suspinfoCancelApproveDao.saveSuspinfoCancelApproveLog(info, ywlb, suspInfo, ssjz);
	}

	public int insertTransSusp(String xzqh, String jsdw, String bkxh,
			String type) throws Exception {
		return this.suspinfoCancelApproveDao.saveTranSusp(xzqh, jsdw, bkxh, type);
	}

	public void sendMsg(SuspinfoMessage message) throws Exception {
		
	}

	public int updateSuspInfo(String T_YWZT, String T_JLZT, String bkxh)
			throws Exception {
		return this.suspinfoCancelApproveDao.updateSuspinfoCancelApprove(T_YWZT, T_JLZT, bkxh);
	}
	/**
	 * 撤控指挥中心涉案类审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	@OperationAnnotation(type=OperationType.CSUSP_APPROVAL_SA_QUERY,description="撤控指挥中心涉案类审批查询")
	public Map<String, Object> getSuspinfoCancelClassApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoCancelApproveDao.getSuspinfoCancelClassApproves(filter, info, glbm);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			//vehSupsinfo.put("BKJGMC",this.systemDao.getDepartmentName(glbm));
			vehSupsinfo.put("BJZTMC",this.systemDao.getCodeValue("130009", vehSupsinfo.get("BJZT").toString()));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		return map;
	}
	/**
	 * 撤控指挥中心交通类审批查询
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 */
	@OperationAnnotation(type=OperationType.CSUSP_APPROVAL_JT_QUERY,description="撤控指挥中心交通类审批查询")
	public Map<String, Object> getSuspinfoCancleTrafficApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map =  this.suspinfoCancelApproveDao.getSuspinfoCancelTrafficApproves(filter, info, glbm);
		List queryList =  (List) map.get("rows");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			//vehSupsinfo.put("BKJGMC",this.systemDao.getDepartmentName(glbm));
			vehSupsinfo.put("BJZTMC",this.systemDao.getCodeValue("130009", vehSupsinfo.get("BJZT").toString()));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		return map;
	}
	
	private void sendSMS(String phone, String content, Map<String, String> dxcondition){
		SmsFacade smsFacade = new SmsFacade();
		if(phone == null || "".equals(phone)){
			dxcondition.put("zt", "2");
			dxcondition.put("sysdate", "''");
			dxcondition.put("reason", "手机号码为空，短信自动作废");
		} else {
			try {
				DefaultSmsTransmitterCallBack callBack = new DefaultSmsTransmitterCallBack();
				boolean flag=smsFacade.sendAndPostSms(phone, content, callBack);
				if(flag){
					dxcondition.put("zt", "1");
					dxcondition.put("sysdate", "sysdate");
				} else {
					dxcondition.put("zt", "2");
					dxcondition.put("sysdate","''");
					dxcondition.put("reason", callBack.getErrorMsg());
				}
			} catch (Exception e) {
				dxcondition.put("zt", "2");
				dxcondition.put("sysdate","''");
				dxcondition.put("reason","短信发送失败" );
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 发短信给布控人,撤控审批人不通过的时候
	 * @param info
	 * @param vehInfo
	 */
	private void sendSmsToBkrByCancelApproveNoPass(AuditApprove info,
			VehSuspinfo vehInfo) {
		try{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您撤控的【车辆号牌号码：" + vehInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",vehInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",vehInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",vehInfo.getBklb())+"】在"+sdf.format(new Date())+"审批不通过，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","43");
		dxcondition.put("dxjsr",vehInfo.getLar()==null?"":vehInfo.getLar() );
		dxcondition.put("dxjshm", vehInfo.getBkjglxdh()==null?"":vehInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("reason", "");
		dxcondition.put("fsfs", "1");
		sendSMS(vehInfo.getBkjglxdh(),content, dxcondition);
			String value="id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,reason";
		String values=""
			+"SEQ_FRM_COMMUNICATION.nextval,'"
			+dxcondition.get("xh")
			+"','"
			+dxcondition.get("ywlx")
			+"','"
			+dxcondition.get("dxjsr")
			+"','"
			+dxcondition.get("dxjshm")
			+"','"
			+dxcondition.get("dxnr")
			+"',"
			+dxcondition.get("sysdate")
			+",'"
			+dxcondition.get("zt")
			+"','"
			+dxcondition.get("fsfs")
			+"','"
			+dxcondition.get("reason")
			+"'"
			;
		this.maintainDaoImpl.saveDxsj(value,values);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
