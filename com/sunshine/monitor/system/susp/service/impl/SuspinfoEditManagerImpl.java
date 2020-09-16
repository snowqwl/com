package com.sunshine.monitor.system.susp.service.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sunshine.monitor.comm.dao.PicDao;
import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.maintain.sms.DefaultSmsTransmitterCallBack;
import com.sunshine.monitor.comm.util.AbstractLobCreatingPreparedStatementCallbackImpl;
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.comm.util.PropertyUtil;
import com.sunshine.monitor.system.communication.dao.CommunicationDao;
import com.sunshine.monitor.system.manager.bean.Code;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.PrefectureManager;
import com.sunshine.monitor.system.manager.service.SystemManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;
import com.sunshine.monitor.system.susp.service.SuspinfoEditManager;

@Transactional
@Service("suspinfoEditManager")
public class SuspinfoEditManagerImpl implements SuspinfoEditManager {

	@Autowired
	@Qualifier("oracleLobHandler")
	public LobHandler lobHandler;

	@Autowired
	@Qualifier("suspinfoEditDao")
	private SuspinfoEditDao suspinfoEditDao;

	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;

	@Autowired
	@Qualifier("CommunicationDao")
	private CommunicationDao communicationDao;

	@Autowired
	@Qualifier("picDao")
	private PicDao picDao;

	@Autowired
	@Qualifier("maintainDao")
	private MaintainDao maintainDaoImpl;

	@Autowired
	@Qualifier("systemManager")
	private SystemManager systemManager;
	@Autowired
	private SuspinfoDao suspinfoDao;

	@Autowired
	@Qualifier("prefectureManager")
	private PrefectureManager prefectureManager;

	/**
	 * 本地布控变更查询
	 * 
	 * @param map
	 * @param info
	 * @return
	 */
	@OperationAnnotation(type = OperationType.SUSP_LD_QUERY, description = "联动布控申请查询")
	public Map findSuspinfoForMap(Map map, VehSuspinfo info) throws Exception {
		return suspinfoEditDao.findSuspinfoForMap(map, info);
	}

	/**
	 * 查询veh_suspinfo表信息
	 * 
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getSuspinfoDetail(String bkxh) throws Exception {
		VehSuspinfo info = suspinfoEditDao.getSuspinfoDetailForBkxh(bkxh);
		List hpzlList = systemDao.getCodes("030107");
		for (Object c : hpzlList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getHpzl())) {
				info.setHpzlmc(code.getDmsm1());
			}
		}

		if (StringUtils.isNotBlank(info.getBkfw())) {
			if ("3".equals(info.getBkfwlx())) {
				info.setBkfwmc(getksBkfw(info.getBkfw()));
			} else {
				info.setBkfwmc(getBkfw(info.getBkfw()));
			}

		}

		List bkxzList = systemDao.getCodes("120021");
		for (Object c : bkxzList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBkxz())) {
				info.setBkxzmc(code.getDmsm1());
			}
		}

		Map map = communicationDao.getcommunicateByXh(bkxh);
		if (map.get("DXNR") != null)
			info.setXjdx(map.get("DXNR").toString());
		else
			info.setXjdx("");

		return info;
	}

	/**
	 * 查询veh_suspinfo表信息
	 * 
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getSuspinfoDetailForBkxh(String bkxh) throws Exception {
		VehSuspinfo info = suspinfoEditDao.getSuspinfoDetailForBkxh(bkxh);
		setSuspInfoItemValue(info);

		try {
			Map map = communicationDao.getcommunicateByXh(bkxh);
			if (map.get("DXNR") != null)
				info.setXjdx(map.get("DXNR").toString());
			else
				info.setXjdx("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public VehSuspinfo getCitySuspinfoDetailForBkxh(String bkxh, String cityname)
			throws Exception {
		VehSuspinfo info = suspinfoEditDao.getCitySuspinfoDetailForBkxh(bkxh,
				cityname);
		setSuspInfoItemValue(info);

		return info;
	}

	public void setSuspInfoItemValue(VehSuspinfo info) throws Exception {
		if (info == null)
			throw new IllegalArgumentException("布控信息不存在！");
		List xxlyList = systemDao.getCodes("120012");
		for (Object c : xxlyList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getXxly())) {
				info.setXxlymc(code.getDmsm1());
			}
		}

		List cllxList = systemDao.getCodes("030104");
		for (Object c : cllxList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getCllx())) {
				info.setCllxmc(code.getDmsm1());
			}
		}

		List hpysList = systemDao.getCodes("031001");
		for (Object c : hpysList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getHpys())) {
				info.setHpysmc(code.getDmsm1());
			}
		}

		List bkdlList = systemDao.getCodes("120019");
		for (Object c : bkdlList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBkdl())) {
				info.setBkdlmc(code.getDmsm1());
			}
		}

		List hpzlList = systemDao.getCodes("030107");
		for (Object c : hpzlList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getHpzl())) {
				info.setHpzlmc(code.getDmsm1());
			}
		}

		List bjfsList = systemDao.getCodes("130000");
		for (Object c : bjfsList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBjfs())) {
				info.setBjfsmc(code.getDmsm1());
			}
		}

		List bklbList = systemDao.getCodes("120005");
		for (Object c : bklbList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBklb())) {
				info.setBklbmc(code.getDmsm1());
			}
		}

		List ywztList = systemDao.getCodes("120008");
		for (Object c : ywztList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getYwzt())) {
				info.setYwztmc(code.getDmsm1());
			}
		}

		List bkjbList = systemDao.getCodes("120020");
		for (Object c : bkjbList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBkjb())) {
				info.setBkjbmc(code.getDmsm1());
			}
		}

		if (StringUtils.isNotBlank(info.getBkfw())) {
			if ("3".equals(info.getBkfwlx())) {
				info.setBkfwmc(getksBkfw(info.getBkfw()));
			} else {
				// System.out.println("111");
				info.setBkfwmc(getBkfw(info.getBkfw()));
			}

		}

		List bkfwlxList = systemDao.getCodes("120004");
		String fwlxmc = null;
		for (Object c : bkfwlxList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBkfwlx())) {
				fwlxmc = code.getDmsm1();
			}
		}

		if (fwlxmc != null)
			info.setBkfwlx(fwlxmc);

		List bkxzList = systemDao.getCodes("120021");
		for (Object c : bkxzList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBkxz())) {
				info.setBkxzmc(code.getDmsm1());
			}
		}

		List sqsbList = systemDao.getCodes("190001");
		for (Object c : sqsbList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getSqsb())) {
				info.setSqsbmc(code.getDmsm1());
			}
		}

		List ckyyList = systemDao.getCodes("120007");
		for (Object c : ckyyList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getCkyydm()))
				info.setCkyydmmc(code.getDmsm1());
		}

		List bjztList = systemDao.getCodes("130009");
		for (Object c : bjztList) {
			Code code = (Code) c;
			if (code.getDmz().equals(info.getBjzt()))
				info.setBjztmc(code.getDmsm1());
		}

		if (StringUtils.isNotBlank(info.getBjfs())) {
			info.setBjfsmc(getBjfs(info.getBjfs()));
		}

		if (StringUtils.isNotBlank(info.getCsys())) {
			info.setCsysmc(getCsys(info.getCsys()));
		}

		if (StringUtils.isNotBlank(info.getJyaq())) {
			String s = info.getJyaq();
			info.setJyaq(TextToHtml(s));
		}

		if (StringUtils.isNotBlank(info.getCkyyms())) {
			String s = info.getCkyyms();
			info.setCkyyms(TextToHtml(s));
		}
	}

	/**
	 * 本地布控变更保存
	 * 
	 * @param suspInfo
	 * @return
	 * @throws Exception
	 */
	// @OperationAnnotation(type = OperationType.SUSP_LOCAL_CHANGE_SAVE,
	// description = "本地布控变更保存")
	public Map updateSuspinfo(final VehSuspinfo suspInfo, VehSuspinfopic vspic)
			throws Exception {
		Map r = null;
		try {
			// 更新布控表
			suspinfoEditDao.updateSuspinfo(suspInfo);
			// 更新susp_picrec(图片表)
			if (vspic != null) {
				vspic.setBkxh(suspInfo.getBkxh());
				// 保存图片
				// this.suspinfoDao.saveSuspinfopictrue(vspic);
				this.saveSuspinfopictrue(vspic);

			}
			r = Common.messageBox("保存成功！", "1");
		} catch (Exception e) {
			e.printStackTrace();
			r = Common.messageBox("程序异常！", "1");
		}
		/*
		 * yaowang 2018-4-16 新增判断ywzt不等于13 当业务状态为13时 布控未提交状态
		 */
		new Thread() {
			@Override
			public void run() {
				sendSmsToBkrByEdit(suspInfo);
			}
		}.start();
		return r;
	}

	/**
	 * 删除未审批布控记录需更新布控表
	 * 
	 * @param suspInfo
	 * @return
	 * @throws Exception
	 */
	public int delSuspInfo(VehSuspinfo suspInfo) throws Exception {

		return suspinfoEditDao.updateSuspInfoForDel(suspInfo);

	}

	/**
	 * 删除未审批布控写入日志表
	 * 
	 * @param suspInfo
	 * @param ssjz
	 * @return
	 * @throws Exception
	 */
	public int insertBusinessLogForDel(VehSuspinfo suspInfo, String ssjz)
			throws Exception {

		return suspinfoEditDao.insertBusinessLogForDel(suspInfo, ssjz);
	}

	/**
	 * 本地布控变更删除
	 * 
	 * @param suspInfo
	 * @param ssjz
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.SUSP_LD_CHANGE_DELETE, description = "联动布控变更删除")
	public Map delSuspinfo(VehSuspinfo suspInfo, String ssjz) throws Exception {
		Map r = null;

		try {
			delSuspInfo(suspInfo);
			insertBusinessLogForDel(suspInfo, ssjz);

			r = Common.messageBox("删除成功！", "1");

		} catch (Exception e) {

			r = Common.messageBox("程序异常！", "0");
			throw e;
		}

		return r;
	}

	public List getBkfwListTree() throws Exception {

		return suspinfoEditDao.getBkfwListTree();
	}

	/**
	 * 得到报警方式
	 * 
	 * @param bjfs
	 * @return
	 * @throws Exception
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
	 * 得到布控范围
	 * 
	 * @param bkfw
	 * @return
	 * @throws Exception
	 */
	public String getBkfw(String bkfw) throws Exception {
		try {
			if ((bkfw == null) || (bkfw.length() < 1))
				return bkfw;
			String[] temp = bkfw.split(",");
			String bkfws = "";
			List list = getBkfwListTree();
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
	 * by huyaj 2017/7/4s 得到跨省的布控范围
	 * 
	 * @param ksbkfw
	 * @return
	 * @throws Exception
	 */
	public String getksBkfw(String bkfw) throws Exception {
		try {
			if ((bkfw == null) || (bkfw.length() < 1))
				return bkfw;
			String[] temp = bkfw.split(",");
			String bkfws = "";
			try {
				List<Map<String, Object>> list = prefectureManager
						.getGdCityTree();
				for (int i = 0; i < temp.length; i++) {
					for (Iterator it = list.iterator(); it.hasNext();) {
						Map m = (Map) it.next();
						if (m.get("DWDM").toString().equals(temp[i])) {
							bkfws = bkfws+ m.get("JDMC").toString().replaceAll(" ", "") + ",";
						}
					}

				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (bkfws.endsWith(","))
				bkfws = bkfws.substring(0, bkfws.length() - 1);
			return bkfws;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bkfw;
	}

	/**
	 * 得到车身颜色
	 * 
	 * @param csys
	 * @return
	 * @throws Exception
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
						+ this.systemDao.getCodeValue("030108",
								String.valueOf(t.charAt(i))) + ",";
			}
			if (ccsys.endsWith(","))
				ccsys = ccsys.substring(0, ccsys.length() - 1);
			return ccsys;
		} catch (Exception e) {
		}
		return csys;
	}

	/**
	 * 特殊字符转义
	 * 
	 * @param s
	 * @return
	 * @throws Exception
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
	 * (根据参数)查询布控表
	 * 
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public List getSuspListForHphm(String hphm, String hpzl) throws Exception {
		List list = this.suspinfoEditDao.getSuspListForHphm(hphm, hpzl);
		for (Object o : list) {
			VehSuspinfo info = (VehSuspinfo) o;

			setSuspInfoItemValue(info);
		}

		return list;
	}

	/**
	 * (根据参数)查询布控表信息数目
	 * 
	 * @param begin
	 * @param end
	 * @param yhdh
	 * @param bkfwlx
	 * @return
	 * @throws Exception
	 */
	public int getSuspinfoEditCount(String begin, String end, String yhdh,
			String bkfwlx) throws Exception {
		return this.suspinfoEditDao.getSuspinfoEditCount(begin, end, yhdh,
				bkfwlx);
	}

	public AuditApprove getAuditApprove(String bkxh) throws Exception {
		return this.suspinfoEditDao.getAuditApprove(bkxh);
	}

	public Boolean existPic(String bkxh) {
		return picDao.existPic(bkxh);
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

	/**
	 * 
	 * jiu布控变更发短信 yaowang 2018-4-18 布控提交后不允许修改
	 * 
	 * @param suspInfo
	 */
	private void sendSmsToBkrByEdit(VehSuspinfo suspInfo) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String content = "您有一条【车辆号牌号码："
					+ suspInfo.getHphm()
					+ "、号牌种类："
					+ this.systemManager.getCodeValue("030107",
							suspInfo.getHpzl())
					+ "、布控大类："
					+ this.systemDao.getCodeValue("120019", suspInfo.getBkdl())
					+ "、布控级别："
					+ this.systemManager.getCodeValue("120005",
							suspInfo.getBklb()) + "】的布控记录在"
					+ sdf.format(new Date()) + "已变更！";
			Map<String, String> dxcondition = new HashMap<String, String>();
			dxcondition.put("xh", suspInfo.getBkxh());
			dxcondition.put("ywlx", "11");
			dxcondition.put("dxjsr",
					suspInfo.getBkr() == null ? "" : suspInfo.getBkr());
			dxcondition.put("dxjshm", suspInfo.getBkjglxdh() == null ? ""
					: suspInfo.getBkjglxdh());
			dxcondition.put("dxnr", content);
			dxcondition.put("fsfs", "1");
			dxcondition.put("reason", "");
			sendSMS(suspInfo.getBkjglxdh(), content, dxcondition);
			String value = "id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,reason";
			String values = "" + "SEQ_FRM_COMMUNICATION.nextval,'"
					+ dxcondition.get("xh") + "','" + dxcondition.get("ywlx")
					+ "','" + dxcondition.get("dxjsr") + "','"
					+ dxcondition.get("dxjshm") + "','"
					+ dxcondition.get("dxnr") + "',"
					+ dxcondition.get("sysdate") + ",'" + dxcondition.get("zt")
					+ "','" + dxcondition.get("fsfs") + "','"
					+ dxcondition.get("reason") + "'";
			long _3 = System.currentTimeMillis();
			this.maintainDaoImpl.saveDxsj(value, values);
			long _4 = System.currentTimeMillis();
			System.out.println("=====保存短信内容到表【frm_communication】===="
					+ (_4 - _3) + "ms");

			// 短信定时提醒----2
			String content2 = "您有一条【车牌号牌号码：" + suspInfo.getHphm() + "、号牌种类："
					+ this.systemDao.getCodeValue("030107", suspInfo.getHpzl())
					+ "、布控大类："
					+ this.systemDao.getCodeValue("120019", suspInfo.getBkdl())
					+ "、布控小类："
					+ this.systemDao.getCodeValue("120005", suspInfo.getBklb())
					+ "】的布控记录超过2小时还没审核，请审核！";
			Map<String, String> dxcondition2 = new HashMap<String, String>();
			dxcondition2.put("xh", suspInfo.getBkxh());
			dxcondition2.put("ywlx", "11");
			dxcondition2.put("dxjsr",
					suspInfo.getBkr() == null ? "" : suspInfo.getBkr());
			dxcondition2.put("dxjshm", suspInfo.getBkjglxdh() == null ? ""
					: suspInfo.getBkjglxdh());
			dxcondition2.put("reason", "");
			dxcondition2.put("dxnr", content2);
			dxcondition2.put("bz", "1");
			dxcondition2.put("fsfs", "2");
			if (!StringUtils.isBlank(suspInfo.getBkjglxdh() == null ? ""
					: suspInfo.getBkjglxdh())) {
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

	/**
	 * 保存布控图片
	 * 
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public boolean saveSuspinfopictrue(VehSuspinfopic vspic) throws Exception {
		boolean saveflag = false;
		int count = 0;
		boolean flag = this.suspinfoDao.isSusp(vspic);
		if (flag) {// 修改
			String picName = vspic.getZjlx();
			String clbksqbName = vspic.getClbksqblj();
			String lajdsName = vspic.getLajdslj();
			String yjcnsName = vspic.getYjcnslj();
			String sql = "";
			if (picName != null && !"".equals(picName)) {
				sql = "UPDATE susp_picrec set zjlx = ?, zjwj = ? where bkxh = ?";
				AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
						lobHandler) {
					public void setValues(PreparedStatement pstmt,
							LobCreator lobCreator) throws SQLException,
							DataAccessException {
						final VehSuspinfopic picture = (VehSuspinfopic) this
								.getParameterObject();
						lobCreator.setBlobAsBytes(pstmt, 2, picture.getZjwj());
						pstmt.setString(1, picture.getZjlx());
						pstmt.setString(3, picture.getBkxh());
					}
				};
				callBack.setParameterObject(vspic);
				count += this.suspinfoDao.updateSuspPic(sql, callBack) ? 0 : 1;
			}
			if (clbksqbName != null && !"".equals(clbksqbName)) {
				sql = "UPDATE susp_picrec set clbksqblj = ?, clbksqb = ? where bkxh = ?";
				AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
						lobHandler) {
					public void setValues(PreparedStatement pstmt,
							LobCreator lobCreator) throws SQLException,
							DataAccessException {
						final VehSuspinfopic picture = (VehSuspinfopic) this
								.getParameterObject();
						lobCreator.setBlobAsBytes(pstmt, 2,
								picture.getClbksqb());
						pstmt.setString(1, picture.getClbksqblj());
						pstmt.setString(3, picture.getBkxh());
					}
				};
				callBack.setParameterObject(vspic);
				count += this.suspinfoDao.updateSuspPic(sql, callBack) ? 0 : 1;
			}
			if (lajdsName != null && !"".equals(lajdsName)) {
				sql = "UPDATE susp_picrec set lajdslj = ?, lajds = ? where bkxh = ?";
				AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
						lobHandler) {
					public void setValues(PreparedStatement pstmt,
							LobCreator lobCreator) throws SQLException,
							DataAccessException {
						final VehSuspinfopic picture = (VehSuspinfopic) this
								.getParameterObject();
						lobCreator.setBlobAsBytes(pstmt, 2, picture.getLajds());
						pstmt.setString(1, picture.getLajdslj());
						pstmt.setString(3, picture.getBkxh());
					}
				};
				callBack.setParameterObject(vspic);
				count += this.suspinfoDao.updateSuspPic(sql, callBack) ? 0 : 1;
			}
			if (yjcnsName != null && !"".equals(yjcnsName)) {
				sql = "UPDATE susp_picrec set yjcnslj = ?, yjcns = ? where bkxh = ?";
				AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
						lobHandler) {
					public void setValues(PreparedStatement pstmt,
							LobCreator lobCreator) throws SQLException,
							DataAccessException {
						final VehSuspinfopic picture = (VehSuspinfopic) this
								.getParameterObject();
						lobCreator.setBlobAsBytes(pstmt, 2, picture.getYjcns());
						pstmt.setString(1, picture.getYjcnslj());
						pstmt.setString(3, picture.getBkxh());
					}
				};
				callBack.setParameterObject(vspic);
				count += this.suspinfoDao.updateSuspPic(sql, callBack) ? 0 : 1;
			}
		} else {// 新增
			count += this.suspinfoDao.savePic(vspic) ? 0 : 1;
		}
		return (count < 1) ? false : true;
	}

	/**
	 * yaowang 申请布控之后 短信通知审核人
	 * 
	 * @param bean
	 * @param vehSuspinfo
	 * @param suspInfo
	 */
	private void sendSmsToSuspAuditPeople(VehSuspinfo bean,
			VehSuspinfo vehSuspinfo, VehSuspinfo suspInfo) {
		try {
			// 短信立刻发送---1
			String content = "您有一条【车辆号牌号码：" + suspInfo.getHphm() + "、号牌种类："
					+ this.systemDao.getCodeValue("030107", suspInfo.getHpzl())
					+ "、布控大类："
					+ this.systemDao.getCodeValue("120019", suspInfo.getBkdl())
					+ "、布控小类："
					+ this.systemDao.getCodeValue("120005", suspInfo.getBklb())
					+ "】的布控记录需要审核！";
			Map<String, String> dxcondition = new HashMap<String, String>();
			dxcondition.put("xh", vehSuspinfo.getBkxh());
			dxcondition.put("ywlx", "11");
			dxcondition.put("dxjsr",
					bean.getUser() == null ? "" : bean.getUser());
			dxcondition.put("dxjshm", bean.getLxdh());
			dxcondition.put("dxnr", content);
			dxcondition.put("reason", "");
			dxcondition.put("fsfs", "1");
			sendSMS(bean.getLxdh(), content, dxcondition);
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

			// 短信定时提醒----2
			String content2 = "您有一条【车牌号牌号码：" + suspInfo.getHphm() + "、号牌种类："
					+ this.systemDao.getCodeValue("030107", suspInfo.getHpzl())
					+ "、布控大类："
					+ this.systemDao.getCodeValue("120019", suspInfo.getBkdl())
					+ "、布控小类："
					+ this.systemDao.getCodeValue("120005", suspInfo.getBklb())
					+ "】的布控记录超过2小时还没审核，请审核！";
			Map<String, String> dxcondition2 = new HashMap<String, String>();
			dxcondition2.put("xh", vehSuspinfo.getBkxh());
			dxcondition2.put("ywlx", "11");
			dxcondition2.put("dxjsr",
					bean.getUser() == null ? "" : bean.getUser());
			dxcondition2.put("dxjshm", bean.getLxdh());
			dxcondition2.put("reason", "");
			dxcondition2.put("dxnr", content2);
			dxcondition2.put("bz", "1");
			dxcondition2.put("fsfs", "2");
			if (!StringUtils.isBlank(bean.getLxdh())) {
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
}
