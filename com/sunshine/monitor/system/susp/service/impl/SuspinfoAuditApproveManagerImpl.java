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
import com.sunshine.monitor.system.activemq.bean.TransAuditApprove;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.manager.bean.BusinessLog;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.DepartmentDao;
import com.sunshine.monitor.system.manager.dao.LogDao;
import com.sunshine.monitor.system.manager.dao.SysparaDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.dao.SuspInfoAuditDao;
import com.sunshine.monitor.system.susp.dao.SuspInfoCancelDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoApproveDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoCancelApproveDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;
import com.sunshine.monitor.system.susp.service.SuspinfoAuditApproveManager;

@Transactional
@Service("suspinfoAuditApproveManager")
public class SuspinfoAuditApproveManagerImpl implements
		SuspinfoAuditApproveManager {

	@Autowired
	@Qualifier("suspinfoApproveDao")
	private SuspinfoApproveDao suspinfoApproveDao;

	@Autowired
	@Qualifier("suspinfoCancelApproveDao")
	private SuspinfoCancelApproveDao suspinfoCancelApproveDao;

	@Autowired
	@Qualifier("suspInfoCancelDao")
	private SuspInfoCancelDao suspInfoCancelDao;

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	@Autowired
	@Qualifier("sysparaDao")
	private SysparaDao sysparaDao;

	@Autowired
	@Qualifier("suspinfoAuditDao")
	private SuspInfoAuditDao suspinfoAuditDao;

	@Autowired
	@Qualifier("suspinfoEditDao")
	private SuspinfoEditDao suspinfoEditDao;

	@Autowired
	@Qualifier("departmentDao")
	private DepartmentDao departmentDao;
	
	@Autowired
	@Qualifier("maintainDao")
	private MaintainDao maintainDaoImpl;

	@Autowired
	@Qualifier("logDao")
	private LogDao logDao;

	@Autowired
	private UrlDao urlDao;
	/**
	 * 
	 * 函数功能说明 : 查询需布控审批的记录集合
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getSuspinfoApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map = this.suspinfoApproveDao.getSusinfoApproves(
				filter, info, glbm);
		List queryList = (List) map.get("rows");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			vehSupsinfo.put("BKJGMC", this.systemDao.getDepartmentName(glbm));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		return map;
	}

	public SuspinfoApproveDao getSuspinfoApproveDao() {
		return suspinfoApproveDao;
	}

	public void setSuspinfoApproveDao(SuspinfoApproveDao suspinfoApproveDao) {
		this.suspinfoApproveDao = suspinfoApproveDao;
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
	 * 
	 * （根据参数）查询需布控审批的记录集合
	 * @param bkxh
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public Object getApproveDetailForBkxh(String bkxh, String glbm)
			throws Exception {
		VehSuspinfo vehInfo = (VehSuspinfo) this.suspinfoApproveDao
				.getApprpvesDetailForBkxh(bkxh, glbm);
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

		List<Code> sqsbList = this.systemDao.getCode("190001");
		for (Code code : sqsbList) {
			if (code.getDmz().equals(vehInfo.getSqsb())) {
				vehInfo.setSqsbmc(code.getDmsm1());
			}
		}

		if (vehInfo.getCsys() != null && vehInfo.getCsys().length() > 0) {
			vehInfo.setCsysmc(getCsys(vehInfo.getCsys()));
		}

		if (vehInfo.getBkfw() != null && vehInfo.getBkfw().length() > 0) {
			vehInfo.setBkfwmc(getBkfw(vehInfo.getBkfw()));
		}

		if (vehInfo.getBjfs() != null && vehInfo.getBjfs().length() > 0) {
			vehInfo.setBjfsmc(getBjfs(vehInfo.getBjfs()));
		}

		if (vehInfo.getJyaq() != null && vehInfo.getJyaq().length() > 0) {
			vehInfo.setJyaq(TextToHtml(vehInfo.getJyaq()));
		}
		return vehInfo;
	}
	/**
	 * 
	 * 得到车身颜色
	 * @param csys
	 * @throws Exception    
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
						+ this.systemDao.getCodeValue("030108", String
								.valueOf(t.charAt(i))) + ",";
			}
			if (ccsys.endsWith(","))
				ccsys = ccsys.substring(0, ccsys.length() - 1);
			return ccsys;
		} catch (Exception e) {
		}
		return csys;
	}
	/**
	 * 
	 * 得到布控范围
	 * @param bkfw
	 * @throws Exception    
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
	 * 
	 * 	查询code_url表的信息
	 * @param 
	 * @throws Exception    
	 * @return 
	 */
	public List getBkfwListTree() throws Exception {
		return this.suspinfoApproveDao.getBkfwListTree();
	}
	/**
	 * 
	 * 得到报警方式
	 * @param bjfs
	 * @throws Exception    
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
	 * 
	 * 转义特殊符号
	 * @param s
	 * @throws Exception    
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
	 * 
	 * 函数功能说明:查询已知布控序号布控记录的布控审核审批记录集合
	 * 修改日期 	2013-6-19
	 * @param bkxh
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	public List getSuspinfoAuditHistory(String bkxh) throws Exception {
		if (bkxh == null || bkxh.length() < 1) {
			return null;
		}
		AuditApprove aa = new AuditApprove();
		aa.setBkxh(bkxh);
		aa.setBzw("12");
		List list = this.suspinfoApproveDao.getAuditApproves(aa);
		for (Iterator it = list.iterator(); it.hasNext();) {
			AuditApprove t = (AuditApprove) it.next();
			t.setCzjg(this.systemDao.getCodeValue("190002", t.getCzjg()));
			if (t.getBzw().equals("1")) {
				t.setBzw("审核");
			} else if (t.getBzw().equals("2")) {
				t.setBzw("审批");
			}
		}
		return list;
	}
	/**
	 * 
	 * 函数功能说明 :布控审批调用方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Map saveApprove(AuditApprove info) throws Exception {
		Map result = null;
		VehSuspinfo vehInfo = (VehSuspinfo) this.suspinfoApproveDao
				.getApprpvesDetailForBkxh(info.getBkxh(), info.getCzrdw());
		if (vehInfo == null) {
			return Common.messageBox("参数传入异常,未找到相应的对象！", "0");
		}
		Department dp = this.systemDao.getDepartment(info.getCzrdw());
		if (dp == null) {
			return Common.messageBox("参数传入异常,未找到相应的对象！", "0");
		} else {
			if (dp.getSsjz() == null || dp.getSsjz().equals("")) {
				return Common.messageBox("本部门缺少警钟信息！", "0");
			}
		}
		try {
			String t_jlzt = "0";
			String t_ywzt = null;
			if (info.getCzjg().equals("0")) {//布控审批不通过
				t_ywzt = "99";
				//短信立刻发送---1
				sendSmsToBkrByApproveNoPass(info, vehInfo);
			} else if (info.getCzjg().equals("2")) {
				t_ywzt = "13";
				sendSmsToBkrByApproveNeedEdit(info, vehInfo);
			} else if (info.getCzjg().equals("1")) {
				t_ywzt = "14";
				t_jlzt = "1";
			}
			// 保存到审核审批表中
			info.setBzw("2");
			int count = suspinfoApproveDao.saveSuspinfoApprove(info);
			// 更新布控表信息
			this.suspinfoApproveDao.updateSuspinfoApprove(t_ywzt, t_jlzt, info
					.getBkxh());

			// 保存到传输表
			if (t_jlzt.equals("1") && vehInfo.getBkfwlx().equals("2")) {
				Syspara sysparam = this.sysparaDao.getSyspara("xzqh", "1", "");
				String csz = sysparam.getCsz();
				String[] bkfw = vehInfo.getBkfw().split(",");
				for (String jsdw : bkfw) {
					this.suspinfoApproveDao.saveTranSusp(csz, jsdw, info
							.getBkxh(), "11");
				}
			}

			// 保存日志表
			this.suspinfoApproveDao.saveSuspinfoApproveLog(info, "12", vehInfo,
					dp.getSsjz());
			result = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;
	}

	
	/**
	 * 
	 * 函数功能说明 : 布控审核调用方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map   
	 * @throws
	 */
	public int saveAuditApprove(AuditApprove info) throws Exception {
		return suspinfoApproveDao.saveSuspinfoApprove(info);
	}
	/**
	 * 
	 * 函数功能说明:保存审核审批表数据
	 * @param info(TransAuditApprove)
	 * @throws Exception    
	 * @return 
	 */
	public int saveAuditApprove(TransAuditApprove info) throws Exception {
		return suspinfoApproveDao.saveSuspinfoApprove(info);
	}
	/**
	 * 
	 * 函数功能说明 : 查询需要审核的布控数据
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map findSuspinfoAuditForMap(Map filter, VehSuspinfo info, String glbm)
			throws Exception {

		return suspinfoAuditDao.findSuspinfoAuditForMap(filter, info, glbm);
	}
	
	/**
	 * 
	 * 函数功能说明 : 查询需要审核的超时布控数据
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map findSuspinfoAuditOverTimeForMap(Map filter, VehSuspinfo info, String glbm)
			throws Exception {

		return suspinfoAuditDao.findSuspinfoAuditOverTimeForMap(filter, info, glbm);
	}

	@OperationAnnotation(type = OperationType.SUSP_CHECK_ADD, description = "布控领导审核新增")
	public Map<String,String> saveAudit(final AuditApprove info) throws Exception {

		Map<String,String> result = null;

		try {
			final VehSuspinfo suspInfo = suspinfoEditDao
					.getSuspinfoDetailForBkxh(info.getBkxh());
			if (suspInfo == null) {
				return Common.messageBox("参数传输异常,未找到相应的对象！", "0");

			}

			Department dp = departmentDao.getDepartment(info.getCzrdw());
			if (dp == null) {
				return Common.messageBox("参数传输异常,未找到相应的对象！", "0");

			} else {

				if (StringUtils.isBlank(dp.getSsjz()))
					return Common.messageBox("本部门缺少警种信息！", "0");

			}

			String T_JLZT = "0";
			String T_YWZT = null;
			if (info.getCzjg().equals("0")) { 
				T_YWZT = "99";
				//布控审核不通过发短信通知布控人
				//sendSmsToBkrByAuditNoPass(info, suspInfo);
				new Thread(){
					@Override
					public void run() {
						sendSmsToBkrByAuditNoPass(info, suspInfo);
					}
				}.start();
			} else if (info.getCzjg().equals("2")) {
				//sendSmsToBkrByAuditNeedEdit(info, suspInfo);
				new Thread(){
					@Override
					public void run() {
						sendSmsToBkrByAuditNeedEdit(info, suspInfo);
					}
				}.start();
				T_YWZT = "13";
			} else if (info.getCzjg().equals("1")) {
				if (suspInfo.getBkdl().equals("3")) {
					/**
					 * 更新时间2015/4/24
					 * 管控类增加审批
					 */
					//T_YWZT = "14";
					T_YWZT = "12";
					//T_JLZT = "1";
				} else {
					T_YWZT = "12";
					//审核通过发送短信提醒审批人进行审批
					//sendSmsToSuspApprovePeople(info, suspInfo);
					new Thread(){
						@Override
						public void run() {
							sendSmsToSuspApprovePeople(info, suspInfo);
						}
					}.start();
				}
			}

			// 写入审核审批记录表
			info.setBzw("1");// 布控审核
			this.insertAuditApprove(info);

			// 更新布控表jlzt字段
			this.updateSuspInfo(T_YWZT, T_JLZT, info.getBkxh());

			if (T_JLZT.equals("1") && "2".equals(suspInfo.getBkfwlx())) {

				Syspara syspara = sysparaDao.getSyspara("xzqh", "1", "");
				String xzqh = syspara.getCsz();

				// 未安装新系统地市按原传输机制传输
				String[] bkfws = suspInfo.getBkfw().split(",");
				for (String jsdw : bkfws) {
					CodeUrl url = urlDao.getUrl(jsdw);
					if (StringUtils.isNotBlank(url.getBz())
							&& "0".equals(url.getBz().substring(0, 1))) {
						this.insertTransSusp_test(xzqh, jsdw, info.getBkxh(),
								"11");
					}
				}

				// 联动管控类布控信息已布控成功，写入传输表
				this.insertTransSusp(xzqh, suspInfo.getBkfw(), info.getBkxh(),
						"11");

			}

			// 更新日志表
			this.insertBusinessLog(suspInfo, info, dp.getSsjz(), "11");

			// 发送提醒审批人审批布控短息(管控类不进入审批流程)
			//if(!suspInfo.getBkdl().equals("3")){
		//	List<Department> dpList = departmentDao.getHigherDepartmentList(dp
		//			.getGlbm());
			
			/*for (Department department : dpList) {
				if (dp.getJb().equals("2")) {
					//如果是2级的，只发给自己，不发给特殊部门。
					if (department.getJb().equals("2")
							&& StringUtils.isNotBlank(department.getLxdh1())) {
						smster.send(department.getLxdh1(), content);
					}
				} else {
					int jb = Integer.parseInt(department.getJb()) - 1;
					if (department.getJb().equals(String.valueOf(jb))
							&& StringUtils.isNotBlank(department.getLxdh1())) {
						smster.send(department.getLxdh1(), content);
					}
				}
			}*/
			//}
			result = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("程序异常！", "0");

		}

		return result;
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
	 * 发短信提醒布控审批人
	 * @param info
	 * @param suspInfo
	 */
	private void sendSmsToSuspApprovePeople(AuditApprove info,
			VehSuspinfo suspInfo) {
		try{
			//短息立刻发送 ---1
			//SmsFacade smsFacade = new SmsFacade();
			String content = "您有一条【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控小类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】的布控记录需要审批！";
			//if(!StringUtils.isBlank(info.getLxdh()))
			//	smsFacade.sendAndPostSms(info.getLxdh(), content, null);
			Map<String, String> dxcondition  = new HashMap<String,String>();
			dxcondition.put("xh", suspInfo.getBkxh());
			dxcondition.put("ywlx","12");
			dxcondition.put("dxjsr",info.getUser()==null?"":info.getUser() );
			dxcondition.put("dxfsr", suspInfo.getBkr()==null?"":suspInfo.getBkr());
			dxcondition.put("dxjshm", info.getLxdh()==null?"":info.getLxdh());
			dxcondition.put("dxnr", content);
			dxcondition.put("reason", "");
			dxcondition.put("fsfs", "1");
			this.sendSMS(info.getLxdh(), content, dxcondition);
			String value="id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,reason,fsfs";
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
				+dxcondition.get("reason")
				+"','"
				+dxcondition.get("fsfs")
				+"'"
				;
			
			this.maintainDaoImpl.saveDxsj(value,values);
			//短息定时发送 ---2
			String content2 = "您有一条【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控小类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】的布控记录超过2小时还没审批，请审批！";
			/*if(!StringUtils.isBlank(info.getLxdh()))
			smster.send(info.getLxdh(), content);*/
			
			Map<String, String> dxcondition2  = new HashMap<String,String>();
			dxcondition2.put("xh", suspInfo.getBkxh());
			dxcondition2.put("ywlx","12");
			dxcondition2.put("dxjsr",info.getUser()==null?"":info.getUser() );
			dxcondition2.put("dxfsr", suspInfo.getBkr()==null?"":suspInfo.getBkr());
			dxcondition2.put("dxjshm", info.getLxdh()==null?"":info.getLxdh());
			dxcondition2.put("dxnr", content2);
			dxcondition2.put("reason", "");
			dxcondition2.put("bz", "1");
			dxcondition2.put("fsfs", "2");
			if(!StringUtils.isBlank(info.getLxdh())){
				dxcondition2.put("zt", "0");
			}
			else{
				dxcondition2.put("zt", "2");
				dxcondition2.put("reason", "手机号码为空，短信自动作废");
			}
			String value2="id,xh,ywlx,dxjsr,dxjshm,dxnr,ywclsj,zt,bz,reason,fsfs";
			String values2=""
				+"SEQ_FRM_COMMUNICATION.nextval,'"
				+dxcondition2.get("xh")
				+"','"
				+dxcondition2.get("ywlx")
				+"','"
				+dxcondition2.get("dxjsr")
				+"','"
				+dxcondition2.get("dxjshm")
				+"','"
				+dxcondition2.get("dxnr")
				+"',"
				+"sysdate"
				+",'"
				+dxcondition2.get("zt")
				+"','"
				+dxcondition2.get("bz")
				+"','"
				+dxcondition2.get("reason")
				+"','"
				+dxcondition2.get("fsfs")
				+"'"
				;
			
			this.maintainDaoImpl.saveDxsj(value2,values2);
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}

	private void insertTransSusp_test(String xzqh, String jsdw, String bkxh,
			String type) throws Exception {
		this.suspinfoAuditDao.insertTransSusp_test(xzqh, jsdw, bkxh, type);
	}

	/**
	 * 
	 * 函数功能说明 : 撤控审核调用方法 修改日期 2013-6-19
	 * 
	 * @param info
	 * @return
	 * @throws Exception
	 * @return Map<String,String>
	 */
	@OperationAnnotation(type = OperationType.CSUSP_CHECK_ADD, description = "撤控领导审核新增")
	public Map<String, String> saveCAudit(final AuditApprove info) throws Exception {

		Map<String, String> result = null;

		try {
			final VehSuspinfo suspInfo = suspinfoEditDao
					.getSuspinfoDetailForBkxh(info.getBkxh());
			if (suspInfo == null) {
				result = Common.messageBox("参数传输异常,未找到相应的对象！", "0");

			}

			Department dp = departmentDao.getDepartment(info.getCzrdw());
			if (dp == null) {

				result = Common.messageBox("参数传输异常,未找到相应的对象！", "0");
			} else {

				if (StringUtils.isBlank(dp.getSsjz()))
					return Common.messageBox("本部门缺少警种信息！", "0");
			}

			String T_JLZT = "1";
			String T_YWZT = null;
			
			if (info.getCzjg().equals("0")) {
				T_YWZT = "14";
				//撤控审核不通过，短信通知布控人
				//sendSmsToBkrByCancelAuditNoPass(info, suspInfo);
				new Thread(){
					@Override
					public void run() {
						sendSmsToBkrByCancelAuditNoPass(info, suspInfo);
					}
				}.start();
			} 
			
			/*
			 * yaowang
			 * 2018-3-26
			 * 1 、 3行未注释该方法未完全注释
			 */
			//else if (info.getCzjg().equals("1")) {1行

				// 管控类审核通过后，流程结束
//				if (suspInfo.getBkdl().equals("3")) {
//
//					T_JLZT = "2";
//					T_YWZT = "99";
				//} 3行
			else {
					T_YWZT = "42";
					//撤控审核通过，短信通知撤控审批人
					//sendSmsToCancelSuspApprovePeople(info, suspInfo);
					new Thread(){
						@Override
						public void run() {
							sendSmsToCancelSuspApprovePeople(info, suspInfo);
						}
					}.start();
				}
//			}

			// 写入审核审批记录表
			info.setBzw("3");// 撤控审核
			this.insertAuditApprove(info);

			// 更新布控表jlzt字段
			this.updateSuspInfo(T_YWZT, T_JLZT, info.getBkxh());

			if (T_JLZT.equals("2") && "2".equals(suspInfo.getBkfwlx())) {

				Syspara syspara = sysparaDao.getSyspara("xzqh", "1", "");
				String xzqh = syspara.getCsz();

				// 未安装新系统地市按原传输机制传输
				String[] bkfws = suspInfo.getBkfw().split(",");
				for (String jsdw : bkfws) {
					CodeUrl url = urlDao.getUrl(jsdw);
					if (StringUtils.isNotBlank(url.getBz())
							&& "0".equals(url.getBz().substring(0, 1))) {
						this.insertTransSusp_test(xzqh, jsdw, info.getBkxh(),
								"13");
					}
				}

				// 联动管控类撤控审核通过(流程结束)，写入传输表
				this.insertTransSusp(xzqh, suspInfo.getBkfw(), info.getBkxh(),
						"13");

			}

			// 更新日志表
			this.insertBusinessLog(suspInfo, info, dp.getSsjz(), "21");

			// 发送提醒审批短息
			List<Department> dpList = departmentDao.getHigherDepartmentList(dp
					.getGlbm());
			
		//	if(!suspInfo.getBkdl().equals("3")){
				
			
		//	}
		/*	for (Department department : dpList) {
				if (dp.getJb().equals("2")) {

					if (department.getJb().equals("2")
							&& StringUtils.isNotBlank(department.getLxdh1())) {
						smster.send(department.getLxdh1(), content);
					}
				} else {

					int jb = Integer.parseInt(department.getJb()) - 1;
					if (department.getJb().equals(String.valueOf(jb))
							&& StringUtils.isNotBlank(department.getLxdh1())) {
						smster.send(department.getLxdh1(), content);
					}

				}
			}*/

			result = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("程序异常！", "1");
		}

		return result;
	}
	
	
	
	/**
	 * 撤控审核通过之后,短信提醒审批人
	 * @param info
	 * @param suspInfo
	 */
	private void sendSmsToCancelSuspApprovePeople(AuditApprove info,
			VehSuspinfo suspInfo) {
		try{
		//短信立刻发送 ---1
		String content = "您有一条【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控小类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】的撤控记录需要审批！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", suspInfo.getBkxh());
		dxcondition.put("ywlx","42");
		dxcondition.put("dxjsr",info.getUser()==null?"":info.getUser() );
		dxcondition.put("dxjshm", info.getLxdh()==null?"":info.getLxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("fsfs", "1");
		dxcondition.put("reason", "");
		this.sendSMS(info.getLxdh(), content, dxcondition);
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
		
		
		//短信定时发送 ----2
		String content2 = "您有一条【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控小类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】的撤控记录超过2小时还没审批，请审批！";
		
		
		Map<String, String> dxcondition2  = new HashMap<String,String>();
		dxcondition2.put("xh", suspInfo.getBkxh());
		dxcondition2.put("ywlx","42");
		dxcondition2.put("dxjsr",info.getUser()==null?"":info.getUser() );
		dxcondition2.put("dxjshm", info.getLxdh()==null?"":info.getLxdh());
		dxcondition2.put("dxnr", content2);
		dxcondition2.put("bz", "1");
		dxcondition2.put("fsfs", "2");
		dxcondition2.put("reason", "");
		if(!StringUtils.isBlank(info.getLxdh())){
			dxcondition2.put("zt", "0");
		}
		else
		{
			dxcondition2.put("zt", "2");
			dxcondition2.put("reason", "手机号码为空，短信自动作废");
		}
		String value2="id,xh,ywlx,dxjsr,dxjshm,dxnr,ywclsj,zt,bz,fsfs,reason";
		String values2=""
			+"SEQ_FRM_COMMUNICATION.nextval,'"
			+dxcondition2.get("xh")
			+"','"
			+dxcondition2.get("ywlx")
			+"','"
			+dxcondition2.get("dxjsr")
			+"','"
			+dxcondition2.get("dxjshm")
			+"','"
			+dxcondition2.get("dxnr")
			+"',"
			+"sysdate"
			+",'"
			+dxcondition2.get("zt")
			+"','"
			+dxcondition2.get("bz")
			+"','"
			+dxcondition2.get("fsfs")
			+"','"
			+dxcondition2.get("reason")
			+"'"
			;
		
		this.maintainDaoImpl.saveDxsj(value2,values2);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 
	 * 函数功能说明 : 查询撤控申请的列表方法
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @param modul
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map findSuspinfoCancelForMap(Map filter, VehSuspinfo info,
			String glbm, String modul) throws Exception {

		return suspInfoCancelDao.findSuspinfoCancelForMap(filter, info, glbm,
				modul);
	}
	/**
	 * 
	 * 函数功能说明 : 查询撤控申请的列表方法
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @param modul
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map findSuspinfoCancelOverTimeForMap(Map filter, VehSuspinfo info,
			String glbm, String modul) throws Exception {

		return suspInfoCancelDao.findSuspinfoCancelOverTimeForMap(filter, info, glbm,
				modul);
	}
	/**
	 * 
	 * 函数功能说明 : 撤控申请保存方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @param jz
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	@OperationAnnotation(type = OperationType.CSUSP_APPLY_ADD, description = "撤控申请新增")
	public boolean saveCancelSuspInfo(final VehSuspinfo info, String jz)
			throws Exception {

		boolean result = false;
		final VehSuspinfo suspInfo = suspinfoEditDao
		.getSuspinfoDetailForBkxh(info.getBkxh());
		try {
			/**
			 * 用户需求修改，从2016-11-22开始，撤控走一键撤控的流程
			 * 一键撤控：撤控不需要走撤控审核审批流程，直接将状态改为99，结束整个流程
			 * liumeng
			 */
			//布控信息立案人不为当前操作人，撤控不走审核审批流程，直接写入传输表中
//			if(!info.getCxsqr().equals(suspInfo.getLar())){
				updateLdSuspInfoForYjck(info,suspInfo);
//			}else{//立案人本人撤控
//				this.updateSuspInfoForCancel(info);
//			}
			
			BusinessLog log = new BusinessLog();
			log.setYwxh(info.getBkxh());
			log.setYwlb("20");
			log.setYwjb(info.getBkjb());
			log.setCzrdh(info.getCxsqr());
			log.setCzrjh(info.getCxsqrjh());
			log.setCzrdwdm(info.getCxsqdw());
			log.setCzrdwjz(jz);

			this.insertBusinessLog(log);
			result = true;
			
			if(info.getBkjb().equals("4")){
				//sendSmsToCancelSuspAuditPeople(info, suspInfo);
				new Thread(){
					@Override
					public void run() {
						sendSmsToCancelSuspAuditPeople(info, suspInfo);
					}
				}.start();
				}
				
		} catch (Exception e) {
			throw e;
		}

		return result;
	}
	
	
	

	

	public int updateSuspInfoForCancel(VehSuspinfo info) throws Exception {

		return suspInfoCancelDao.updateSuspInfoForCancel(info);
	}

	public int insertBusinessLog(BusinessLog info) throws Exception {
		return logDao.saveBusinessLog(info);
	}
	/**
	 * 
	 * 	查询veh_alarmrec表的信息
	 * @param 
	 * @throws Exception    
	 * @return 
	 */
	public List getAlarmList(String bkxh) throws Exception {
		List list = suspInfoCancelDao.getAlarmList(bkxh);
		for (Object o : list) {
			VehAlarmrec var = (VehAlarmrec) o;
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
	 * 
	 * 函数功能说明:获取审核审批表记录集合
	 * 修改日期 	2013-6-19
	 * @param aa
	 * @return
	 * @throws Exception    
	 * @return List
	 */
	public List getAuditApproves(AuditApprove aa) throws Exception {
		List list = suspInfoCancelDao.getAuditApproves(aa);

		for (Object o : list) {
			AuditApprove t = (AuditApprove) o;
			t.setCzjgmc(this.systemDao.getCodeValue("190002", t.getCzjg()));
			if (t.getBzw().equals("1"))
				t.setBzwmc("布控审核");
			else if (t.getBzw().equals("3"))
				t.setBzwmc("撤控审核");
			else if (t.getBzw().equals("2"))
				t.setBzwmc("布控审批");
			else if (t.getBzw().equals("4"))
				t.setBzwmc("撤控审批");
		}
		return list;
	}
	/**
	 * 
	 * 函数功能说明:查询需撤控审批的列表记录(包括涉案和交通违法)
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getSuspinfoCancelApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map = this.suspinfoCancelApproveDao
				.getSusinfoCancelApproves(filter, info, glbm);
		List queryList = (List) map.get("rows");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			vehSupsinfo.put("BKJGMC", this.systemDao.getDepartmentName(glbm));
			vehSupsinfo.put("BJZTMC", this.systemDao.getCodeValue("130009",
					vehSupsinfo.get("BJZT").toString()));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		return map;
	}

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
	 * 
	 * 函数功能说明:撤控审核审批历史记录(针对已知布控序号的布控记录)
	 * 修改日期 	2013-6-19
	 * @param bkxh
	 * @return
	 * @throws Exception    
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
				t.setBzwmc("审核");
			else if ((t.getBzw().equals("2")) || (t.getBzw().equals("4")))
				t.setBzwmc("审批");
		}
		return list;
	}
	/**
	 * 
	 * 函数功能说明 :保存撤控审批记录方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Map saveCApprove(final AuditApprove info) throws Exception {
		Map result = null;
		final VehSuspinfo vehInfo = (VehSuspinfo) this.suspinfoCancelApproveDao
				.getCancelApprpvesDetailForBkxh(info.getBkxh(), info.getCzrdw());
		if (vehInfo == null) {
			return Common.messageBox("参数传入异常,未找到相应的对象！", "0");
		}
		Department dp = this.systemDao.getDepartment(info.getCzrdw());
		if (dp == null) {
			return Common.messageBox("参数传入异常,未找到相应的对象！", "0");
		} else {
			if (dp.getSsjz() == null || dp.getSsjz().equals("")) {
				return Common.messageBox("本部门缺少警钟信息！", "0");
			}
		}
		try {
			String t_jlzt = "1";
			String t_ywzt = null;
			if (info.getCzjg().equals("0")) {
				//撤控审批不通过
				t_ywzt = "14";
				//sendSmsToBkrByCancelApproveNoPass(info, vehInfo);
				new Thread(){
					@Override
					public void run() {
						sendSmsToBkrByCancelApproveNoPass(info, vehInfo);
					}
				}.start();
			
			} else if (info.getCzjg().equals("1")) {
				t_ywzt = "99";
				t_jlzt = "2";
			}

			// 保存到审核审批表中
			info.setBzw("4");
			int count = suspinfoCancelApproveDao
					.saveSuspinfoCancelApprove(info);
			// 更新布控表信息
			this.suspinfoCancelApproveDao.updateSuspinfoCancelApprove(t_ywzt,
					t_jlzt, info.getBkxh());

			// 保存到传输表
			if (t_jlzt.equals("2") && vehInfo.getBkfwlx().equals("2")) {
				Syspara sysparam = this.sysparaDao.getSyspara("xzqh", "1", "");
				String csz = sysparam.getCsz();

				this.suspinfoCancelApproveDao.saveTranSusp(csz, vehInfo
						.getBkfw(), info.getBkxh(), "13");

			}

			// 保存日志表
			this.suspinfoCancelApproveDao.saveSuspinfoCancelApproveLog(info,
					"22", vehInfo, dp.getSsjz());
			result = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			result = Common.messageBox("出现异常！", "0");
		}
		return result;
	}
	
	
	
	/**
	 * 	
	 * 函数功能说明:查询需撤控审批的列表记录(涉案)
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getSuspinfoCancelClassApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map = this.suspinfoCancelApproveDao
				.getSuspinfoCancelClassApproves(filter, info, glbm);
		List queryList = (List) map.get("rows");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			vehSupsinfo.put("BKJGMC", this.systemDao.getDepartmentName(glbm));
			vehSupsinfo.put("BJZTMC", this.systemDao.getCodeValue("130009",
					vehSupsinfo.get("BJZT").toString()));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		return map;
	}
	/**
	 * 
	 * 函数功能说明:查询需撤控审批的列表记录(交通违法)
	 * 修改日期 	2013-6-19
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getSuspinfoCancleTrafficApproves(Map filter,
			VehSuspinfo info, String glbm) throws Exception {
		Map<String, Object> map = this.suspinfoCancelApproveDao
				.getSuspinfoCancelTrafficApproves(filter, info, glbm);
		List queryList = (List) map.get("rows");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator i = queryList.iterator(); i.hasNext();) {
			Map<String, Object> vehSupsinfo = (Map<String, Object>) i.next();
			vehSupsinfo.put("BKJGMC", this.systemDao.getDepartmentName(glbm));
			vehSupsinfo.put("BJZTMC", this.systemDao.getCodeValue("130009",
					vehSupsinfo.get("BJZT").toString()));
			list.add(vehSupsinfo);
		}
		map.put("rows", list);
		return map;
	}

	public int insertBusinessLog(VehSuspinfo suspInfo, AuditApprove info,
			String ssjz, String ywlb) throws Exception {

		return suspinfoAuditDao.insertBusinessLog(suspInfo, info, ssjz, "11");
	}

	public int insertAuditApprove(AuditApprove info) throws Exception {

		return suspinfoAuditDao.insertAuditApprove(info);
	}

	public int updateSuspInfo(String T_YWZT, String T_JLZT, String bkxh)
			throws Exception {

		return suspinfoAuditDao.updateSuspInfo(T_YWZT, T_JLZT, bkxh);
	}

	public int insertTransSusp(String xzqh, String jsdw, String bkxh,
			String type) throws Exception {

		return suspinfoAuditDao.insertTransSusp(xzqh, jsdw, bkxh, type);
	}
	/**
	 * 
	 * (根据参数)查询veh_suspinfo表的未审核数据数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoAuditCount(String begin, String end, String glbm)
			throws Exception {
		return this.suspinfoAuditDao.getSupinfoAuditCont(begin, end, glbm);
	}
	
	/**
	 * 
	 * (根据参数)查询veh_suspinfo表的超时审核数据数目（管控类）
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoAuditOverTimeCount(String begin, String end, String glbm)
			throws Exception {
		return this.suspinfoAuditDao.getSuspinfoAuditOverTimeCount(begin, end, glbm);
	}
	
	/**
	 * 
	 * (根据参数)得到布控未审批数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoApproveCount(String begin, String end, String glbm)
			throws Exception {
		return this.suspinfoApproveDao.getSusupinfoApproveCount(begin, end,
				glbm);
	}
	
	/**
	 * 
	 * (根据参数)得到布控超时审批数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoApproveOverTimeCount(String begin, String end, String glbm)
			throws Exception {
		return this.suspinfoApproveDao.getSusupinfoApproveOverTimeCount(begin, end,
				glbm);
	}
	/**
	 * 
	 * (根据参数)得到撤控审批数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoCancelApproveCount(String begin, String end,
			String glbm) throws Exception {
		return this.suspinfoCancelApproveDao.getSuspinfoCancelApproveCount(
				begin, end, glbm);
	}
	/**
	 * 
	 * (根据参数)得到撤控审批数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoCancelApproveOverTimeCount(String begin, String end,
			String glbm) throws Exception {
		return this.suspinfoCancelApproveDao.getSuspinfoCancelApproveOverTimeCount(
				begin, end, glbm);
	}
	/**
	 * 
	 * (根据参数)得到撤控审核数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoCancelAuditCount(String begin, String end, String glbm)
			throws Exception {
		return this.suspinfoAuditDao.getSuspinfoCancelAuditCount(begin, end,
				glbm);
	}
	/**
	 * 
	 * (根据参数)得到撤控审核数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoCancelAuditOverTimeCount(String begin, String end, String glbm)
			throws Exception {
		return this.suspinfoAuditDao.getSuspinfoCancelAuditOverTimeCount(begin, end,
				glbm);
	}
	/**
	 * 
	 * (根据参数)得到超期撤控审批数目
	 * @param begin
	 * @param end
	 * @param glbm
	 * @param yhmc
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getExpireCancelSuspinfoCount(String begin, String end,
			String glbm, String yhmc) throws Exception {
		return this.suspinfoApproveDao.getExpireCancelSuspinfoCount(begin, end,
				glbm, yhmc);
	}
	
	/**
	 * （根据参数）得到超时未撤控数目（已拦截确认）
	 * @param begin
	 * @param end
	 * @param glbm
	 * @param yhmc
	 * @return
	 * @throws Exception
	 */
	public int getSuspinfoOuttimeCountCount(String begin, String end, String glbm,String yhmc) throws Exception {
		return this.suspinfoApproveDao.getSuspinfoOuttimeCountCount(begin, end, glbm, yhmc);
	}
	/**
	 * 
	 * (根据参数)得到用户布控信息
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public List<Code> getUserSuspinfo(String begin, String end, String yhdh)
			throws Exception {
		return this.suspinfoApproveDao.getUserSuspinfo(begin, end, yhdh);
	}
	/**
	 * 
	 * (根据参数)得到联动布控数目
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoBkfwlxCount(String begin, String end, String yhdh)
			throws Exception {
		return this.suspinfoApproveDao.getSuspinfoBkfwlxCount(begin, end, yhdh);
	}
	/**
	 * 
	 * (根据参数)得到联动布控数目
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public int getSuspinfoNoBkfwlxCount(String begin, String end, String yhdh)
			throws Exception {
		return this.suspinfoApproveDao.getSuspinfoNoBkfwlxCount(begin, end,
				yhdh);
	}
	/**
	 * 
	 * 超时撤控审批列表
	 * @param filter
	 * @param info
	 * @param glbm
	 * @return
	 * @throws Exception    
	 * @return 
	 */
	public Map findSuspinfoCancelTimeoutForMap(Map filter, VehSuspinfo info,
			String glbm) throws Exception {
		return this.suspInfoCancelDao.findSuspinfoCancelTimeoutForMap(filter,
				info, glbm);
	}
	
	
	/**
	 * 发短信给布控人,审核不同过的时候
	 * @param info
	 * @param suspInfo
	 */
	private void sendSmsToBkrByAuditNoPass(AuditApprove info,
			VehSuspinfo suspInfo){
		try {
			//短信立刻发送 --1
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String content  = "您布控的【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】在"+sdf.format(new Date())+"，不通过，原因："+info.getMs()+" ！";
			Map<String, String> dxcondition  = new HashMap<String,String>();
			dxcondition.put("xh", info.getBkxh());
			dxcondition.put("ywlx","16");
			dxcondition.put("dxjsr",suspInfo.getBkr()==null?"":suspInfo.getBkr() );
			dxcondition.put("dxjshm", suspInfo.getBkjglxdh()==null?"":suspInfo.getBkjglxdh());
			dxcondition.put("dxnr", content);
			dxcondition.put("fsfs", "1");
			dxcondition.put("reason", "");
			this.sendSMS(suspInfo.getBkjglxdh(), content, dxcondition);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发短信给布控人,审批不通过的时候
	 * @param info
	 * @param vehInfo
	 */
	private void sendSmsToBkrByApproveNoPass(AuditApprove info,
			VehSuspinfo vehInfo) {
		try{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您布控的【车辆号牌号码：" + vehInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",vehInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",vehInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",vehInfo.getBklb())+"】在"+sdf.format(new Date())+"审批不通过，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","17");
		dxcondition.put("dxjsr",vehInfo.getBkr()==null?"":vehInfo.getLar() );
		dxcondition.put("dxjshm", vehInfo.getBkjglxdh()==null?"":vehInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("fsfs","1");
		dxcondition.put("sysdate","''");
		dxcondition.put("reason","");
		this.sendSMS(vehInfo.getBkjglxdh(), content, dxcondition);
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
	
	
	/**
	 * 发短信给布控人,审核需修改的时候
	 * @param info
	 * @param suspInfo
	 */
	private void sendSmsToBkrByAuditNeedEdit(AuditApprove info,
			VehSuspinfo suspInfo) {
		try{
		//短信立刻发送 --1
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您布控的【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】待修改，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","16");
		dxcondition.put("dxjsr",suspInfo.getBkr()==null?"":suspInfo.getBkr() );
		dxcondition.put("dxjshm", suspInfo.getBkjglxdh()==null?"":suspInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("fsfs", "1");
		dxcondition.put("reason", "");
		this.sendSMS(suspInfo.getBkjglxdh(), content, dxcondition);
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
	
	
	
	/**
	 * 发短信给布控人,审批需修改时候
	 * @param info
	 * @param vehInfo
	 */
	private void sendSmsToBkrByApproveNeedEdit(AuditApprove info,
			VehSuspinfo vehInfo) {
		try{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您布控的【车辆号牌号码：" + vehInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",vehInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",vehInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",vehInfo.getBklb())+"】在"+sdf.format(new Date())+"待修改，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","17");
		dxcondition.put("dxjsr",vehInfo.getBkr()==null?"":vehInfo.getLar() );
		dxcondition.put("dxjshm", vehInfo.getBkjglxdh()==null?"":vehInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("fsfs","1");
		dxcondition.put("sysdate","''");
		dxcondition.put("reason","");
		this.sendSMS(vehInfo.getBkjglxdh(), content, dxcondition);
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
	
	/**
	 * 发短信给布控人,撤控审核不通过的时候
	 * @param info
	 * @param suspInfo
	 */
	private void sendSmsToBkrByCancelAuditNoPass(AuditApprove info,
			VehSuspinfo suspInfo) {
		try{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您撤控的【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】在"+sdf.format(new Date())+"审核不通过，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","43");
		dxcondition.put("dxjsr",suspInfo.getLar()==null?"":suspInfo.getLar() );
		dxcondition.put("dxjshm", suspInfo.getBkjglxdh()==null?"":suspInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("reason", "");
		dxcondition.put("fsfs", "1");
		this.sendSMS(suspInfo.getLadwlxdh(), content, dxcondition);
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
	
	/**
	 * 发短信给布控人，撤控审批不通过的时候
	 * @param info
	 * @param vehInfo
	 */
	private void sendSmsToBkrByCancelApproveNoPass(AuditApprove info, VehSuspinfo vehInfo) {
		try{
		//短信立刻发送 ---1
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您撤控的【车辆号牌号码：" + vehInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",vehInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",vehInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",vehInfo.getBklb())+"】在"+sdf.format(new Date())+"审批不通过，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","44");
		dxcondition.put("dxjsr",vehInfo.getBkr()==null?"":vehInfo.getBkr() );
		dxcondition.put("dxjshm", vehInfo.getBkjglxdh()==null?"":vehInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("fsfs", "1");
		dxcondition.put("reason", "");
		this.sendSMS(vehInfo.getLadwlxdh(), content, dxcondition);
			String value="id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,reason";
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
			+dxcondition.get("reason")
			+"'"
			;
		
		this.maintainDaoImpl.saveDxsj(value,values);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 发短信给布控人,撤控审核需修改时候
	 * @param info
	 * @param suspInfo
	 */
	private void sendSmsToBkrByCancelAuditNeedEdit(AuditApprove info,
			VehSuspinfo suspInfo) {
		try{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您撤控的【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】待审核，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","43");
		dxcondition.put("dxjsr",suspInfo.getLar()==null?"":suspInfo.getLar() );
		dxcondition.put("dxjshm", suspInfo.getBkjglxdh()==null?"":suspInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("reason", "");
		dxcondition.put("fsfs", "1");
		this.sendSMS(suspInfo.getLadwlxdh(), content, dxcondition);
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
	
	
	/**
	 * 发短信给布控人，撤控审批需修改的时候
	 * @param info
	 * @param vehInfo
	 */
	private void sendSmsToBkrByCancelApproveNeedEdit(AuditApprove info, VehSuspinfo vehInfo) {
		try{
		//短信立刻发送 ---1
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String content = "您撤控的【车辆号牌号码：" + vehInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",vehInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",vehInfo.getBkdl())+"、布控子类："+this.systemDao.getCodeValue("120005",vehInfo.getBklb())+"】待审批，原因："+info.getMs()+" ！";
		Map<String, String> dxcondition  = new HashMap<String,String>();
		dxcondition.put("xh", info.getBkxh());
		dxcondition.put("ywlx","44");
		dxcondition.put("dxjsr",vehInfo.getBkr()==null?"":vehInfo.getBkr() );
		dxcondition.put("dxjshm", vehInfo.getBkjglxdh()==null?"":vehInfo.getBkjglxdh());
		dxcondition.put("dxnr", content);
		dxcondition.put("fsfs", "1");
		dxcondition.put("reason", "");
		this.sendSMS(vehInfo.getLadwlxdh(), content, dxcondition);
			String value="id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,reason";
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
			+dxcondition.get("reason")
			+"'"
			;
		
		this.maintainDaoImpl.saveDxsj(value,values);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 申请撤控的时候,发送短信给审核人
	 * @param info
	 * @param suspInfo
	 */
	private void sendSmsToCancelSuspAuditPeople(VehSuspinfo info, VehSuspinfo suspInfo) {
		try {
			// 短信立刻发送 ---1
			String content = "您有一条【车辆号牌号码：" + suspInfo.getHphm() + "、号牌种类："
					+ this.systemDao.getCodeValue("030107", suspInfo.getHpzl())
					+ "、布控大类："
					+ this.systemDao.getCodeValue("120019", suspInfo.getBkdl())
					+ "、布控小类："
					+ this.systemDao.getCodeValue("120005", suspInfo.getBklb())
					+ "】的撤控记录需要审核！";
			Map<String, String> dxcondition = new HashMap<String, String>();
			dxcondition.put("xh", info.getBkxh());
			dxcondition.put("ywlx", "41");
			dxcondition.put("dxjsr", info.getUser() == null ? "" : info
					.getUser());
			dxcondition.put("dxjshm", info.getLxdh() == null ? "" : info
					.getLxdh());
			dxcondition.put("dxnr", content);
			dxcondition.put("zt", "0");
			dxcondition.put("bz", "1");
			dxcondition.put("fsfs", "1");
			dxcondition.put("reason", "");
			this.sendSMS(info.getLxdh(), content, dxcondition);
			String value = "id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,reason";
			String values = "" + "SEQ_FRM_COMMUNICATION.nextval,'"
					+ dxcondition.get("xh") + "','" + dxcondition.get("ywlx")
					+ "','" + dxcondition.get("dxjsr") + "','"
					+ dxcondition.get("dxjshm") + "','"
					+ dxcondition.get("dxnr") + "',"
					+ dxcondition.get("sysdate") + ",'" + dxcondition.get("zt")
					+ "','" + dxcondition.get("fsfs") + "','"
					+ dxcondition.get("reason") + "'";

			this.maintainDaoImpl.saveDxsj(value, values);

			// 短信定时发送 ---2
			String content2 = "您有一条【号牌号码：" + suspInfo.getHphm() + "、号牌种类："
					+ this.systemDao.getCodeValue("030107", suspInfo.getHpzl())
					+ "、布控级别："
					+ this.systemDao.getCodeValue("120005", suspInfo.getBklb())
					+ "】的撤控记录超过2小时还没审核，请审核！";
			Map<String, String> dxcondition2 = new HashMap<String, String>();
			dxcondition2.put("xh", info.getBkxh());
			dxcondition2.put("ywlx", "41");
			dxcondition2.put("dxjsr", info.getUser() == null ? "" : info
					.getUser());
			dxcondition2.put("dxjshm", info.getLxdh() == null ? "" : info
					.getLxdh());
			dxcondition2.put("dxnr", content2);
			dxcondition2.put("bz", "1");
			dxcondition2.put("fsfs", "2");
			dxcondition2.put("reason", "");

			if (!StringUtils.isBlank(info.getLxdh())) {
				dxcondition2.put("zt", "0");
			} else {
				dxcondition2.put("zt", "2");
				dxcondition2.put("reason", "手机号码为空，短信自动作废");
			}

			String value2 = "id,xh,ywlx,dxjsr,dxjshm,dxnr,ywclsj,zt,bz,fsfs,reason";
			String values2 = "" + "SEQ_FRM_COMMUNICATION.nextval,'"
					+ dxcondition2.get("xh") + "','" + dxcondition2.get("ywlx")
					+ "','" + dxcondition2.get("dxjsr") + "','"
					+ dxcondition2.get("dxjshm") + "','"
					+ dxcondition2.get("dxnr") + "'," + "sysdate" + ",'"
					+ dxcondition2.get("zt") + "','" + dxcondition2.get("bz")
					+ "','" + dxcondition2.get("fsfs") + "','"
					+ dxcondition2.get("reason") + "'";

			this.maintainDaoImpl.saveDxsj(value2, values2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	private void updateLdSuspInfoForYjck(VehSuspinfo info,VehSuspinfo suspInfo) {
		//更新布控表信息
		this.suspinfoCancelApproveDao.updateSuspinfoCancelApprove("99","2",info.getBkxh());
		
		//添加传输表中
		Syspara sysparam;
		try {
			if(("3").equals(suspInfo.getBkfwlx())){
				VehSuspinfo vehSuspinfo = suspinfoEditDao.getSuspinfoDetailForBkxh(info.getBkxh());
				//一键撤控没有审核审批
//				AuditApprove auditApprove = suspinfoEditDao.getAuditApprove(info.getBkxh());
				
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
//				JSONArray json2 = JSONArray.fromObject(auditApprove); 
				//添加参数
				params.add(new BasicNameValuePair("vehSuspinfoBean", json1.toString()));
//				params.add(new BasicNameValuePair("auditApproveBean", json2.toString()));

				HttpResponse responseSend;
				httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				responseSend = new DefaultHttpClient().execute(httppost);
//				String sendPostResult = EntityUtils.toString(responseSend.getEntity());
//				Map<String, Object> map = (Map<String, Object>)JSON.parse(sendPostResult);
			}else{
				sysparam = this.sysparaDao.getSyspara("xzqh","1","");
				String csz = sysparam.getCsz();
				this.suspinfoCancelApproveDao.saveTranSusp(csz, suspInfo.getBkfw(),info.getBkxh(), "13");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<Map<String,Object>> getVehSuspinfoList() throws Exception {
		List<Map<String,Object>> list = suspinfoAuditDao.getVehSuspinfoList();
		return list;
	}
	/**
	 * yaowang
	 * 函数功能说明 : 撤控申请保存方法
	 * 修改日期 	2013-6-19
	 * @param info
	 * @param jz
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	@OperationAnnotation(type = OperationType.CSUSP_APPLY_ADD, description = "撤控申请新增")
	public boolean saveCancelSuspInfocksq(final VehSuspinfo info, String jz)
			throws Exception {

		boolean result = false;
		final VehSuspinfo suspInfo = suspinfoEditDao
		.getSuspinfoDetailForBkxh(info.getBkxh());
		try {
			suspInfoCancelDao.updateSuspInfoForCancelcksq(info);
			
			BusinessLog log = new BusinessLog();
			log.setYwxh(info.getBkxh());
			log.setYwlb("20");
			log.setYwjb(info.getBkjb());
			log.setCzrdh(info.getCxsqr());
			log.setCzrjh(info.getCxsqrjh());
			log.setCzrdwdm(info.getCxsqdw());
			log.setCzrdwjz(jz);

			this.insertBusinessLog(log);
			result = true;
			
			if(info.getBkjb().equals("4")){
				//sendSmsToCancelSuspAuditPeople(info, suspInfo);
				new Thread(){
					@Override
					public void run() {
						sendSmsToCancelSuspAuditPeople(info, suspInfo);
					}
				}.start();
				}
				
		} catch (Exception e) {
			throw e;
		}

		return result;
	}
}
