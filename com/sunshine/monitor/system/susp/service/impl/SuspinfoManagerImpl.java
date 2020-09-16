package com.sunshine.monitor.system.susp.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.comm.log.OperationAnnotation;
import com.sunshine.monitor.comm.log.OperationType;
import com.sunshine.monitor.comm.maintain.SmsFacade;
import com.sunshine.monitor.comm.maintain.dao.MaintainDao;
import com.sunshine.monitor.comm.maintain.sms.DefaultSmsTransmitterCallBack;
import com.sunshine.monitor.comm.util.AbstractLobCreatingPreparedStatementCallbackImpl;
import com.sunshine.monitor.comm.util.BusinessType;
import com.sunshine.monitor.comm.util.DispatchedRangeType;
import com.sunshine.monitor.comm.util.SuspBussinessStatu;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.bean.TransSuspmonitor;
import com.sunshine.monitor.system.analysis.bean.TrafficFlow;
import com.sunshine.monitor.system.manager.bean.BusinessLog;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.bean.Syspara;
import com.sunshine.monitor.system.manager.dao.SysUserDao;
import com.sunshine.monitor.system.manager.dao.SysparaDao;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.susp.bean.AuditApprove;
import com.sunshine.monitor.system.susp.bean.GkSuspinfoApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.dao.SuspInfoAuditDao;
import com.sunshine.monitor.system.susp.dao.SuspInfoCancelDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoCancelApproveDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;
import com.sunshine.monitor.system.susp.service.SuspinfoManager;

@Service
@Transactional(readOnly = true)
public class SuspinfoManagerImpl implements SuspinfoManager {
	
	@Autowired
	@Qualifier("oracleLobHandler")
	public LobHandler lobHandler;
	
	@Autowired
	private SuspinfoDao suspinfoDao;
	
	@Autowired
	private SuspInfoCancelDao suspInfoCancelDao;
	
	@Autowired
	private SuspInfoAuditDao suspinfoAuditDao;
	
	@Autowired
	private SuspinfoCancelApproveDao suspinfoCancelApproveDao;

	@Autowired
	private SystemDao systemDao;
	
	@Autowired
	@Qualifier("sysUserDao")
	private SysUserDao sysUserDao;
	
	@Autowired
	@Qualifier("sysparaDao")
	private SysparaDao sysparaDao;

	@Autowired
	private UrlDao urlDao;

	@Autowired
	private LogManager logManager;
	
	@Resource(name = "redlistCache")
	private Cache cache;
	
	@Autowired
	@Qualifier("maintainDao")
	private MaintainDao maintainDaoImpl;
	

	@Autowired
	@Qualifier("suspinfoEditDao")
	private SuspinfoEditDao suspinfoEditDao;
	/**
	 * 布控列表查询
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> querySuspinfoByPage(Map<String, Object> condition)
			throws Exception {
		Map<String, Object> result = this.suspinfoDao
				.querySuspinfoByPage(condition);
		return result;
	}
	/**
	 * 查询联动布控信息
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.SUSP_LD_QUERY, description = "查询联动布控信息")
	public Map<String, Object> queryLinkSuspinfoByPage(
			Map<String, Object> condition) throws Exception {

		return this.querySuspinfoByPage(condition);
	}
	/**
	 * 查询本地模糊布控信息
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.SUSP_MH_QUERY, description = "查询模糊布控信息")
	public Map<String, Object> queryLocalMhSuspinfoByPage(
			Map<String, Object> condition) throws Exception {

		return this.querySuspinfoByPage(condition);
	}
	/**
	 * 查询本地布控信息
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.SUSP_LOCAL_QUERY, description = "查询本市布控信息")
	public Map<String, Object> queryLocalSuspinfoByPage(
			Map<String, Object> condition) throws Exception {
		return this.querySuspinfoByPage(condition);
	}
	/**
	 * [新增布控]事务
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public Object saveVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception {
		// 保存布控信息
		Map<String, Object> map = (Map<String, Object>) this.saveSuspinfo(bean);
		String code = (String) map.get("code");
		VehSuspinfo vehSuspinfo = (VehSuspinfo) map.get("suspinfo");
		
		VehSuspinfo suspInfo = suspinfoEditDao
		.getSuspinfoDetailForBkxh(vehSuspinfo.getBkxh());
		if ("1".equals(code)) {
			if (vspic != null) {
				vspic.setBkxh(vehSuspinfo.getBkxh());
				// 保存图片
				this.saveSuspinfopictrue(vspic);
			}
			BusinessLog log = new BusinessLog();
			log.setYwxh(vehSuspinfo.getBkxh());
			log.setYwlb(BusinessType.DISPATCHED_APPLY.getCode());// 业务类别(布控申请)
			log.setYwjb(vehSuspinfo.getBkjb());
			log.setCzrdh(vehSuspinfo.getBkr());
			log.setCzrjh(vehSuspinfo.getBkrjh());
			log.setBzsj(null);
			log.setCzrdwdm(vehSuspinfo.getBkjg());
			log.setCzrdwjz(vehSuspinfo.getBkrdwjz());
			// 保存日志(考核)
			this.logManager.saveBusinessLog(log);
		}
		if(code.equals("1")){
		if(bean.getBkdl().equals("3")){
			sendSmsToSuspAuditPeople(bean, vehSuspinfo, suspInfo);
		}
		}
		return map;
	}
	
	/**
	 * [新增布控--过滤红名单]事务
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public Object saveVehSuspinfoFilterRedList(VehSuspinfo bean,
			VehSuspinfopic vspic) throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		//查询所属警种
		try {
			Object ssjz = this.suspinfoDao.getField("frm_department","glbm", "ssjz",bean.getBkjg());
			if(ssjz==null){
				map.put("code", "-999");
				map.put("msg", "您操作的用户所在的部门缺少警种");
				return map;
			}
		} catch (Exception e) {
			throw new Exception("您操作的用户所在的部门缺少警种");
		}
		
		//判断是否模糊布控，模糊布控则不能联动其他地市
		if ("1".equals(bean.getMhbkbj()) && "2".equals(bean.getBkfwlx())) {
			map.put("code", "0");
			map.put("msg", "模糊布控不能联动其它地市,布控范围只能在本地");
			return map;
		}
		
		int status = 0;
		// 红名单不能布控
		if (bean.getHphm() != null) {
			if (checkRedList(bean.getHphm())) {
				//查询序列号
				String bkxh= this.suspinfoDao.getBkxh(bean.getBkjg());
				bean.setBkxh(bkxh);
				//查询布控平台
				String bkpt = this.suspinfoDao.getField("frm_syspara", "gjz", "csz", "xzqh").toString();
				bean.setBkpt(bkpt);
				
				// 保存布控信息
				status =  this.suspinfoDao.saveSuspinfo2(bean);
				map.put("bkxh", bkxh);
			} else {
				map.put("code", "-5");
				map.put("msg", "布控失败！ 该车牌不允许布控");
				return map;
			}
		}

		//String code = (String) map.get("code");
		//VehSuspinfo vehSuspinfo = (VehSuspinfo) map.get("suspinfo");

		VehSuspinfo suspInfo = suspinfoEditDao
				.getSuspinfoDetailForBkxh(bean.getBkxh());
		if (status==1) {
			if (vspic != null) {
				vspic.setBkxh(bean.getBkxh());
				// 保存图片
				this.saveSuspinfopictrue(vspic);
			}
			BusinessLog log = new BusinessLog();
			log.setYwxh(bean.getBkxh());
			log.setYwlb(BusinessType.DISPATCHED_APPLY.getCode());// 业务类别(布控申请)
			log.setYwjb(bean.getBkjb());
			log.setCzrdh(bean.getBkr());
			log.setCzrjh(bean.getBkrjh());
			log.setBzsj(null);
			log.setCzrdwdm(bean.getBkjg());
			log.setCzrdwjz(bean.getBkrdwjz());
			// 保存日志(考核)
			this.logManager.saveBusinessLog(log);
			
			/*
			 * yaowang
			 * 2018-4-16
			 * 新增判断ywzt不等于13  当业务状态为13时 布控未提交状态
			 */
			if (bean.getBkdl().equals("3") && bean.getYwzt()!="13" && !bean.getYwzt().equals("13")) {
				sendSmsToSuspAuditPeople(bean,bean,suspInfo);
			}
		}
		map.put("code", status);
		map.put("msg", "新增布控成功!");
		return map;
	}
	
	
	/**
	 * [新增本市布控]
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.SUSP_LOCAL_ADD, description = "新增本市布控申请")
	public Object saveLocalVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception {
	
		return this.saveVehSuspinfoFilterRedList(bean, vspic);
	}
	/**
	 * [新增一键布控]
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.SUSP_LOCAL_ADD, description = "新增一键布控申请")
	public Object saveAutoVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception {

		return this.saveVehSuspinfoFilterRedList(bean, vspic);
	}
	/**
	 * [新增联动布控]
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.SUSP_LD_ADD, description = "新增跨市联动布控")
	public Object saveLinkVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception {

		return this.saveVehSuspinfoFilterRedList(bean, vspic);
	}
	/**
	 * [新增本市模糊布控]
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.SUSP_MH_ADD, description = "新增本市模糊布控申请")
	public Object saveLocalMhVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception {

		return this.saveVehSuspinfoFilterRedList(bean, vspic);
	}
	/**
	 * 新增布控
	 * 
	 * @param bean
	 * @param vpic
	 * @return
	 * @throws Exception
	 */
	public Object saveSuspinfo(VehSuspinfo bean) throws Exception {
		Map<String, Object> map = (Map<String, Object>) this.suspinfoDao
				.saveSuspinfo(bean);
		return map;
	}
	
	/**
	 * 修改布控
	 * 
	 * @param bean
	 * @param vpic
	 * @return
	 * @throws Exception
	 */
	public Object updateSuspinfo(VehSuspinfo bean) throws Exception {
		Map<String, Object> map = (Map<String, Object>) this.suspinfoDao
				.updateSuspinfo(bean);
		return map;
	}
	
	/**
	 * 布控范围节点列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CodeUrl> getBkfwListTree() throws Exception {
		return this.urlDao.getCodeUrls();
	}
	/**
	 * 查询布控信息(已关联转换)
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getSuspDetail(String bkxh) throws Exception {

		String _return = "";

		VehSuspinfo vs = this.suspinfoDao.getSuspinfoDetail(bkxh);

		if (vs == null)
			throw new IllegalArgumentException("布控信息不存在！");

		// 联动布控
		if (vs.getBkfwlx().equals(
				DispatchedRangeType.LINKAGE_DISPATCHED.getCode())) {
			String xzqhd = vs.getBkfw();
			if (!xzqhd.equals("")) {
				String[] xzqhAy = xzqhd.split(",");
				for (int i = 0; i < xzqhAy.length; i++) {
					String xzqhmc = this.urlDao.getUrlName(xzqhAy[i]);
					_return = _return + xzqhmc + ",";
				}
			}
			// 本地布控
		} else {
			_return = this.urlDao.getUrlName(vs.getBkfw());
		}
		vs.setHpzlmc(this.systemDao.getCodeValue("030107", vs.getHpzl()));
		vs.setJyaq(vs.getJyaq().replaceAll("\r\n", ""));
		vs.setBklb(this.systemDao.getCodeValue("120005", vs.getBklb()));
		vs.setBkjbmc(this.systemDao.getCodeValue("120020", vs.getBkjb()));
		if (_return.length() > 0) {
			if (vs.getBkfwlx().equals(
					DispatchedRangeType.LINKAGE_DISPATCHED.getCode()))
				vs.setBkfwmc(_return.substring(0, _return.length() - 1));
			else {
				vs.setBkfwmc(_return);
			}
		}
		vs.setBjyamc(this.systemDao.getCodeValue("130022", vs.getBjya()));

		vs.setBkjbmc(this.systemDao.getCodeValue("120020", vs.getBkjb()));

		vs.setBkfwlx(this.systemDao.getCodeValue("120004", vs.getBkfwlx()));

		vs.setXxlymc(this.systemDao.getCodeValue("120012", vs.getXxly()));

		vs.setBkdlmc(this.systemDao.getCodeValue("120019", vs.getBkdl()));

		if ((vs.getHpys() != null) && (!"".equals(vs.getHpys()))) {
			vs.setHpys(this.systemDao.getCodeValue("031001", vs.getHpys()));
		}

		vs.setYwzt(this.systemDao.getCodeValue("120008", vs.getYwzt()));

		if ((vs.getCllx() != null) && (!"".equals(vs.getCllx()))) {
			vs.setCllx(this.systemDao.getCodeValue("030104", vs.getCllx()));
		}

		if ((vs.getSqsb() != null) && (!"".equals(vs.getSqsb()))) {
			vs.setSqsb(this.systemDao.getCodeValue("190001", vs.getSqsb()));
		}

		if ((vs.getBkxz() != null) && (!"".equals(vs.getBkxz()))) {
			vs.setBkxz(this.systemDao.getCodeValue("120021", vs.getBkxz()));
		}

		if ((vs.getCkyydm() != null) && (!"".equals(vs.getCkyydm()))) {
			vs.setCkyydm(this.systemDao.getCodeValue("120007", vs.getCkyydm()));
		}

		String _result = "";
		if ((vs.getCsys() != null) && (!"".equals(vs.getCsys()))) {
			String[] csys = vs.getCsys().split(",");
			for (int i = 0; i < csys.length; i++) {
				_result = _result
						+ this.systemDao.getCodeValue("030108", csys[i]) + ",";
			}
			if (_result.length() > 0) {
				vs.setCsys(_result.substring(0, _result.length() - 1));
			}
		}
		String bjfs = vs.getBjfs();
		if ((bjfs != null) && (!"".equals(bjfs))) {
			_result = "";
			int count = bjfs.length();
			for (int j = 0; j < bjfs.length(); j++) {
				if (bjfs.charAt(j) == '1') {
					_result = _result
							+ this.systemDao.getCodeValue("130000", String
									.valueOf(count)) + ",";
				}
				count--;
			}
			if (_result.length() > 0) {
				vs.setBjfs(_result.substring(0, _result.length() - 1));
			}
		}
		return vs;
	}
	
	public VehSuspinfo getCitySuspDetail(String bkxh,String cityname) throws Exception {


		String _return = "";

		VehSuspinfo vs = this.suspinfoDao.getCitySuspDetail(bkxh,cityname);

		if (vs == null)
			return vs;

		// 联动布控
		if (vs.getBkfwlx().equals(
				DispatchedRangeType.LINKAGE_DISPATCHED.getCode())) {
			String xzqhd = vs.getBkfw();
			if (!xzqhd.equals("")) {
				String[] xzqhAy = xzqhd.split(",");
				for (int i = 0; i < xzqhAy.length; i++) {
					String xzqhmc = this.urlDao.getUrlName(xzqhAy[i]);
					_return = _return + xzqhmc + ",";
				}
			}
			// 本地布控
		} else {
			_return = this.urlDao.getUrlName(vs.getBkfw());
		}
		vs.setHpzlmc(this.systemDao.getCodeValue("030107", vs.getHpzl()));
		vs.setJyaq(vs.getJyaq().replaceAll("\r\n", ""));
		vs.setBklb(this.systemDao.getCodeValue("120005", vs.getBklb()));
		vs.setBkjbmc(this.systemDao.getCodeValue("120020", vs.getBkjb()));
		if (_return.length() > 0) {
			if (vs.getBkfwlx().equals(
					DispatchedRangeType.LINKAGE_DISPATCHED.getCode()))
				vs.setBkfwmc(_return.substring(0, _return.length() - 1));
			else {
				vs.setBkfwmc(_return);
			}
		}
		vs.setBjyamc(this.systemDao.getCodeValue("130022", vs.getBjya()));

		vs.setBkjbmc(this.systemDao.getCodeValue("120020", vs.getBkjb()));

		vs.setBkfwlx(this.systemDao.getCodeValue("120004", vs.getBkfwlx()));

		vs.setXxlymc(this.systemDao.getCodeValue("120012", vs.getXxly()));

		vs.setBkdlmc(this.systemDao.getCodeValue("120019", vs.getBkdl()));

		if ((vs.getHpys() != null) && (!"".equals(vs.getHpys()))) {
			vs.setHpys(this.systemDao.getCodeValue("031001", vs.getHpys()));
		}

		vs.setYwzt(this.systemDao.getCodeValue("120008", vs.getYwzt()));

		if ((vs.getCllx() != null) && (!"".equals(vs.getCllx()))) {
			vs.setCllx(this.systemDao.getCodeValue("030104", vs.getCllx()));
		}

		if ((vs.getSqsb() != null) && (!"".equals(vs.getSqsb()))) {
			vs.setSqsb(this.systemDao.getCodeValue("190001", vs.getSqsb()));
		}

		if ((vs.getBkxz() != null) && (!"".equals(vs.getBkxz()))) {
			vs.setBkxz(this.systemDao.getCodeValue("120021", vs.getBkxz()));
		}

		if ((vs.getCkyydm() != null) && (!"".equals(vs.getCkyydm()))) {
			vs.setCkyydm(this.systemDao.getCodeValue("120007", vs.getCkyydm()));
		}

		String _result = "";
		if ((vs.getCsys() != null) && (!"".equals(vs.getCsys()))) {
			String[] csys = vs.getCsys().split(",");
			for (int i = 0; i < csys.length; i++) {
				_result = _result
						+ this.systemDao.getCodeValue("030108", csys[i]) + ",";
			}
			if (_result.length() > 0) {
				vs.setCsys(_result.substring(0, _result.length() - 1));
			}
		}
		String bjfs = vs.getBjfs();
		if ((bjfs != null) && (!"".equals(bjfs))) {
			_result = "";
			int count = bjfs.length();
			for (int j = 0; j < bjfs.length(); j++) {
				if (bjfs.charAt(j) == '1') {
					_result = _result
							+ this.systemDao.getCodeValue("130000", String
									.valueOf(count)) + ",";
				}
				count--;
			}
			if (_result.length() > 0) {
				vs.setBjfs(_result.substring(0, _result.length() - 1));
			}
		}
		return vs;
	
		
	}
	/**
	 * 根据号牌号码、号牌种类查询布控列表
	 * 
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	@OperationAnnotation(type = OperationType.SUSP_LOCAL_QUERY, description = "查询本市布控信息")
	public Map<String,Object> getSuspinfoByHphm(String hphm, String hpzl, boolean flag)
			throws Exception {
		Map<String,Object> map=this.suspinfoDao.getSuspinfoByHphm(hphm, hpzl, flag);
		
		return  map;
	}
	/**
	 * 验证布控记录是否有更高级别
	 * @param hphm
	 * @param bkr
	 * @param bkdl
	 * @return
	 * @throws Exception
	 */
	public String  checkSuspinfo(String hphm,String bkdl) throws Exception{
		String result="";
		List list=this.suspinfoDao.checkSuspinfo(hphm);
		if(list.size()>0){
			result=hphm+" 号码布控失败：已经存在更高级别的布控记录";
		}
		return result;		
	}
	
	
	/**
	 * 获取布控信息(VehSuspinfo),非关联
	 */
	public VehSuspinfo getSuspInfo(String bkxh) throws Exception {

		return this.suspinfoDao.getSuspinfoDetail(bkxh);
	}
	/**
	 * 查询传输布控信息
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public TransSusp getTransSusp(String bkxh) throws Exception {

		return this.suspinfoDao.getTransSuspDetail(bkxh);
	}

	public int saveSuspinfoLink(TransSusp bean) throws Exception {
		/**
		 * 新增联动布控
		 */
		return this.suspinfoDao.saveSuspinfoLink(bean);
	}
	/**
	 * 新增联动布控——省厅
	 */
	public int saveSuspinfoLinkST(TransSusp bean) throws Exception {

		return this.suspinfoDao.saveSuspinfoLinkST(bean);
	}

	public int saveSuspinfomonitorLink(TransSuspmonitor bean) throws Exception {
		return this.suspinfoDao.saveSuspinfomonitorLink(bean);
	}
	/**
	 * 保存联动撤控
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveCsuspinfoLink(TransSusp bean) throws Exception {
		return this.suspinfoDao.saveCsuspinfoLink(bean);
	}
	/**
	 * 查询须要撤控确认的信息
	 * @param conditions 条件(key-value)
	 * @return Map
	 * @throws Exception
	 * @author OUYANG 2013/6/28 
	 * @param tip 
	 */
	@OperationAnnotation(type=OperationType.CSUSP_APPLY_SURE_QUERY,description="拦截确认查询")
	public Map<String, Object> querySuspinfoCancelList(
			Map<String, Object> conditions,String tip) throws Exception {
		//统一按布控机关  进行撤控拦截确认
		/*if("1".equals(tip))//指挥中心登录
			return this.suspinfoDao.querySuspinfoCancelListForZx(conditions);
		else*/
			return this.suspinfoDao.querySuspinfoCancelList(conditions);
	}
	/**
	 * 保存撤控确认
	 * @param vehSuspinfo
	 * @return
	 */
	@OperationAnnotation(type=OperationType.CSUSP_APPLY_SURE_SAVE,description="拦截确认保存")
	public int saveSuspinfoCancelsign(VehSuspinfo vehSuspinfo) throws Exception {

		return this.suspinfoDao.saveSuspinfoCancelsign(vehSuspinfo);
	}
	/**
	 * 系统自动撤控(撤控人信息，撤控原因-已处理，撤控说明-系统自动撤控)
	 * 1.更新布（撤）控表:记录状态、业务状态、更新时间以及撤控人相关信息
	 * 2.添加撤控审核审批记录
	 * @param vehSuspinfo
	 * @return
	 * @throws Exception
	 */
	public int  saveAutomaticCancel(VehSuspinfo vehSuspinfo) throws Exception {
		int success = 0;
		try {
			vehSuspinfo = this.buildVehSuspinfo(vehSuspinfo);
			
			// 更新布(撤)控表信息
			this.suspInfoCancelDao.updateSuspInfo(vehSuspinfo); 
			// 添加审核审批表记录
			//this.suspInfoCancelDao.insertAuditApprove(this.buildAuditApprove(vehSuspinfo)); 
			
			//保存到传输表
			if (vehSuspinfo.getJlzt().equals("2") && vehSuspinfo.getBkfwlx().equals("2")) {
				Syspara sysparam = this.sysparaDao.getSyspara("xzqh", "1", "");
				String csz = sysparam.getCsz();
				String bkfw = vehSuspinfo.getBkfw();
				
				//未安装新系统地市按原传输机制传输
				String[]bkfws = vehSuspinfo.getBkfw().split(",");
				for(String jsdw : bkfws){
					CodeUrl url = urlDao.getUrl(jsdw);
					if(StringUtils.isNotBlank(url.getBz() ) && "0".equals(url.getBz().substring(0, 1))){
						suspinfoAuditDao.insertTransSusp_test(csz, jsdw, vehSuspinfo.getBkxh(),"13");
					}
				}
				suspinfoCancelApproveDao.saveTranSusp(csz, bkfw, vehSuspinfo.getBkxh(), "13");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	public VehSuspinfo buildVehSuspinfo(VehSuspinfo vehSuspinfo) {
		String t_jlzt = "2"; // 记录状态：撤控
		String t_ywzt = "99"; // 业务状态：布控已撤销
		String t_ckyydm = "02"; // 撤控原因代码: 已处理,
		String t_ckyyms = "系统自动撤控"; // 撤控原因描述
		String t_bkr = vehSuspinfo.getBkr();
		String t_bkjg = vehSuspinfo.getBkjg();
		String t_bkjgmc = vehSuspinfo.getBkjgmc();
		String t_bkrmc = vehSuspinfo.getBkrmc();
		
		vehSuspinfo.setJlzt(t_jlzt);
		vehSuspinfo.setYwzt(t_ywzt);
		vehSuspinfo.setCxsqr(t_bkr);
		vehSuspinfo.setCxsqdw(t_bkjg);
		vehSuspinfo.setCxsqdwmc(t_bkjgmc);
		vehSuspinfo.setCxsqrmc(t_bkrmc);
		vehSuspinfo.setCkyydm(t_ckyydm);
		vehSuspinfo.setCkyyms(t_ckyyms);
		return vehSuspinfo;
	}
	
	public AuditApprove buildAuditApprove(VehSuspinfo vehSuspinfo) {
		String t_czjg = "1"; // 操作结果：同意
		String t_bzw = "4"; // 标志位：撤控审批
		String t_ms = "系统自动撤控"; // 描述
		String t_bkxh = vehSuspinfo.getBkxh();
		String t_bkr = vehSuspinfo.getBkr();
		String t_bkjg = vehSuspinfo.getBkjg();
		String t_bkjgmc = vehSuspinfo.getBkjgmc();
		String t_bkrmc = vehSuspinfo.getBkrmc();
		
		String t_czrjh = "";
		try {
			t_czrjh = sysUserDao.getSysUserByYHDH(vehSuspinfo.getBkr()).getJh();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AuditApprove auditApprove = new AuditApprove();
		auditApprove.setBkxh(t_bkxh);
		auditApprove.setCzr(t_bkr);
		auditApprove.setCzrjh(t_czrjh);
		auditApprove.setCzrmc(t_bkrmc);
		auditApprove.setCzrdw(t_bkjg);
		auditApprove.setCzrdwmc(t_bkjgmc);
		auditApprove.setCzjg(t_czjg);
		auditApprove.setBzw(t_bzw);
		auditApprove.setMs(t_ms);
		return auditApprove;
	}
	/**
	 * 
	 * @param vehSuspinfo
	 * @param isforce 是否执行检查
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryValidGKSuspinfo(VehSuspinfo vehSuspinfo,
			boolean isforce) throws Exception {
		Map<String, Object> result = null;
		List<VehSuspinfo> gk_list = new ArrayList<VehSuspinfo>();
		List<VehSuspinfo> normal_list = new ArrayList<VehSuspinfo>();
		// 有效布控信息列表
		String _sql = " and jlzt in('0','1') and hphm ='"
				+ vehSuspinfo.getHphm() + "'";
		List<VehSuspinfo> list = this.suspinfoDao.querySuspinfo(_sql);
		for (VehSuspinfo vs : list) {
			String bkdl = vs.getBkdl();
			if ("3".equals(bkdl)) {
				gk_list.add(vs);
			} else {
				normal_list.add(vs);
			}
		}
		result = new HashMap<String, Object>();
		if (isforce) {
			// 布控大类参数(提交)
			String bkdl = vehSuspinfo.getBkdl();
			// 管控前:不管当前布控类别是什么，
			// 如果数据库存在有效管控类布控信息，则不允许布控
			if (gk_list.size() > 0) {
				result.put("code", "1");
				return result;
				// 管控后:数据库存在一般布控信息，现在布控一条管控类布控信息
			} else if (normal_list.size() > 0) {
				result.put("code", "2");
				return result;
			}
		}
		return null;
	}
	
	public List<VehSuspinfo>  getDxhmByHpdm(String hphm) throws Exception{
		
		return this.suspinfoDao.getDxhmByHpdm(hphm);		
	}
	
	public void  saveLjNotCancalSj(VehSuspinfo vehSuspinfo,List<VehSuspinfo> fsdxhmlist) throws Exception{
		String fsdxhm="";
		Map<String, String> dxcondition  = new HashMap<String,String>();							
		VehSuspinfo info = this.getSuspDetail(vehSuspinfo.getBkxh());
		String msg = "布控类别为【"+info.getBkdlmc()+"】,车辆类型为【"+info.getHpzlmc()+"】,号牌号码【"+info.getHphm()+"】的车辆已拦截成功，系统已撤控！";
		//String msg1 = "布控类别为【"+info.getBkdlmc()+"】,车辆类型为【"+info.getHpzlmc()+"】,号牌号码【"+info.getHphm()+"】的车辆已拦截成功，请对相应的布控信息24小时内及时撤控！";
		dxcondition.put("xh", vehSuspinfo.getBkxh());
		dxcondition.put("ywlx","14");				
		dxcondition.put("fsfs1","1");
		dxcondition.put("fsfs2","2");
		dxcondition.put("zt2", "0");
		dxcondition.put("bz","3");
		dxcondition.put("dxnr", msg);	
		//dxcondition.put("dxnr1", msg1);
		if(fsdxhmlist.size()>0){
		for(int i=0;i<fsdxhmlist.size();i++){
			VehSuspinfo fsdxsj = (VehSuspinfo)fsdxhmlist.get(i);
			if(fsdxsj.getBkjglxdh().length()>0){
			fsdxhm+=fsdxsj.getBkjglxdh()+",";					
				}	
		
			dxcondition.put("dxjsr",fsdxsj.getBkr()==null?"":fsdxsj.getBkr() );
			dxcondition.put("dxjshm", fsdxsj.getBkjglxdh()==null?"":fsdxsj.getBkjglxdh());	
			if(!fsdxsj.getBkjglxdh().equals("")){
				dxcondition.put("zt", "1");
				dxcondition.put("sysdate","sysdate");						
			}
			else{
				dxcondition.put("zt", "2");
				dxcondition.put("sysdate","''");
			}
		
			String value="id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs";
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
			+dxcondition.get("fsfs1")
			+"'"		
			;
			String value1="id,xh,ywlx,dxjsr,dxjshm,dxnr,DXFSSJ,zt,fsfs,bz,ywclsj";
			String values1=""
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
			+dxcondition.get("zt2")
			+"','"
			+dxcondition.get("fsfs2")
			+"','"
			+dxcondition.get("bz")
			+"',"				
			+"sysdate"
			;
		
		this.maintainDaoImpl.saveDxsj(value,values);
		this.maintainDaoImpl.saveDxsj(value1,values1);	
			}
		}
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
	 * 申请布控之后 短信通知审核人
	 * @param bean
	 * @param vehSuspinfo
	 * @param suspInfo
	 */
	private void sendSmsToSuspAuditPeople(VehSuspinfo bean,
			VehSuspinfo vehSuspinfo, VehSuspinfo suspInfo) {
		try{
		//短信立刻发送---1
		String content = "您有一条【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控小类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】的布控记录需要审核！";
			Map<String, String> dxcondition  = new HashMap<String,String>();
			dxcondition.put("xh", vehSuspinfo.getBkxh());
			dxcondition.put("ywlx","11");
			dxcondition.put("dxjsr",bean.getUser()==null?"":bean.getUser() );
			dxcondition.put("dxjshm", bean.getLxdh());
			dxcondition.put("dxnr", content);
			dxcondition.put("reason", "");
			dxcondition.put("fsfs", "1");
			sendSMS(bean.getLxdh(), content, dxcondition);
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
		
		//短信定时提醒----2
		String content2 = "您有一条【车牌号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控小类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】的布控记录超过2小时还没审核，请审核！";
		Map<String, String> dxcondition2  = new HashMap<String,String>();
		dxcondition2.put("xh", vehSuspinfo.getBkxh());
		dxcondition2.put("ywlx","11");
		dxcondition2.put("dxjsr",bean.getUser()==null?"":bean.getUser() );
		dxcondition2.put("dxjshm", bean.getLxdh());
		dxcondition2.put("reason", "");
		dxcondition2.put("dxnr", content2);
		dxcondition2.put("bz", "1");
		dxcondition2.put("fsfs", "2");
		if(!StringUtils.isBlank(bean.getLxdh())){
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
	
	public boolean checkRedList(String hphm){
		try{
			Element element = this.cache.get("redlist");
			Set<String> set = (Set<String>)element.getObjectValue();
			Iterator it = set.iterator();
			while(it.hasNext()){
				String redHphm=(String) it.next();
				if(hphm.equals(redHphm)){
					return false;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return true;
		}
		return true;
	}
	
	@OperationAnnotation(type=OperationType.HCSUSP_APPROVAL_LD_QUERY,description="联动布控管控核查查询")
	public Map<String, Object> queryHCSuspinfo(Map<String, Object> con)
			throws Exception {
		StringBuffer tsql = new StringBuffer();
		if (StringUtils.isNotBlank((String)con.get("hphm")))
			tsql.append(" AND S.HPHM='")
				.append((String)con.get("hphm"))
				.append("'");
		if (StringUtils.isNotBlank((String)con.get("hpzl")))
			tsql.append(" AND S.HPZL='")
				.append((String)con.get("hpzl"))
				.append("'");
		if (StringUtils.isNotBlank((String)con.get("kssj")))
			tsql.append(" AND S.BKSJ>=")
				.append("to_date('"+(String)con.get("kssj")+"','yyyy-mm-dd hh24:mi:ss')");
		if (StringUtils.isNotBlank((String)con.get("jssj")))
			tsql.append(" AND S.BKSJ<=")
				.append("to_date('"+(String)con.get("jssj")+"','yyyy-mm-dd hh24:mi:ss')");
		if (StringUtils.isNotBlank((String)con.get("bkdl")))
			tsql.append(" AND S.BKDL='")
				.append((String)con.get("bkdl"))
				.append("'");
		if (StringUtils.isNotBlank((String)con.get("bklb")))
			tsql.append(" AND S.BKLB='")
				.append((String)con.get("bklb"))
				.append("'");
		if (StringUtils.isNotBlank((String)con.get("hcjg"))){
			tsql.append(" AND G.HCJG='")
				.append((String)con.get("hcjg"))
				.append("'");
		} else {
			tsql.append(" AND G.HCJG is null");
		}
		if (StringUtils.isNotBlank((String)con.get("bkdw")))
			tsql.append(" And (S.BKJG='"+(String)con.get("bkdw")+"' OR EXISTS(SELECT P.XJJG FROM FRM_PREFECTURE P WHERE S.BKJG=P.XJJG AND P.DWDM = '"+(String)con.get("bkdw")+"'))");
		Map<String,Object> map = this.suspinfoDao.queryHCSuspinfo(con, tsql.toString());
		List<Map<String,Object>> list =(List<Map<String,Object>>)map.get("rows");
		if(list == null || list.size() < 1) 
			return map;
		for(int i = 0; i<list.size(); i++){
        	Map<String,Object> rec = list.get(i);
        	String bkjgmc = systemDao.getDepartmentName((String)rec.get("BKJG"));
        	if(bkjgmc != null){
            	rec.put("BKJGMC",bkjgmc);
        	}else{
        		rec.put("BKJGMC",(String)rec.get("BKJG"));
        	}
        }
        map.put("rows",list);
		return map;
	}
	
	@OperationAnnotation(type=OperationType.HCSUSP_APPROVAL_LD_SAVE,description="联动布控管控核查确认")
	public void insertHCsuspinfo(GkSuspinfoApprove gsf) throws Exception {
		
		this.suspinfoDao.insertHCsuspinfo(gsf);
	}
	
	public GkSuspinfoApprove queryGkSuspinfoApprove(String bjxh)
			throws Exception {
		GkSuspinfoApprove gkSuspinfoApprove = null;
		String sql = "SELECT BJXH, BKXH, HCR, HCSJ, HCJG, HCNR, HCDW, BY1, BY2 FROM VEH_GK_SUSPINFO_APPROVE WHERE BJXH=?";
		List<GkSuspinfoApprove> list = this.suspinfoDao.queryForList(sql, new Object[]{bjxh}, GkSuspinfoApprove.class);
		if(!list.isEmpty()){
			gkSuspinfoApprove = list.get(0);
			gkSuspinfoApprove.setHcdwmc(this.systemDao.getDepartmentName(gkSuspinfoApprove.getHcdw()));
			gkSuspinfoApprove.setHcrmc(this.systemDao.getUserName(gkSuspinfoApprove.getHcr()));
			return gkSuspinfoApprove;
		}
		return gkSuspinfoApprove;
	}
	@Override
	public List<Map<String, Object>> querySuspinfoCountByBkr(String glbm) throws Exception {
		String curDate = getYearMonth();
		List<Map<String, Object>> list = this.suspinfoDao.getSuspinfoCountByBkr(glbm,curDate);
		if(list.size()>0){
			return list;
		}
		return null;
	}
	
	/**
	 * 初始化X轴数据，如果不存在此类型的数据，新增并默认为0
	 * @param data
	 */
	private void initPie(List<Map<String, Object>> data){
		List<Map<String, Object>> resultlList = new ArrayList<Map<String,Object>>();
		
		for(SuspBussinessStatu ywzt : SuspBussinessStatu.values()){
			String temp = ywzt.getDmz();
			if(temp.equals("13") || temp.equals("15")){//过滤
				continue;
			}			
			
			boolean check = false;//默认false,不存在
			for(Map<String, Object> map:data){
				String _ywzt = (String)map.get("YWZT");
				if(_ywzt.equals(temp)){
					check=true;
					break;
				}
			}
			
			if(!check){//不存在
				Map<String, Object> map = addMap(temp);
				data.add(map);
			}
		}
	}
	
	private Map<String, Object> addMap(String ywzt){
		Map<String, Object> map = new HashMap<String, Object>();
		int ywztInt = Integer.parseInt(ywzt);
		String jlzt = "0";//默认布控
		if(ywztInt>12){
			jlzt="1";//状态大于12就是撤控
		}
		if(ywztInt==99){
			jlzt="2";
		}
		map.put("JLZT", jlzt);
		map.put("YWZT", ywzt);
		map.put("TOTAL", "0");
		return map;
	}
	
	/**
	 * 获得当前年月
	 * @return
	 */
	private String getYearMonth(){
		/**
         * 声明一个int变量year
         */
        int year;
        /**
         * 声明一个int变量month
         */
        int month;
        /**
         * 声明一个字符串变量date
         */
        String date;
        /**
         * 实例化一个对象calendar
         */
        Calendar calendar = Calendar.getInstance();
        /**
         * 获取年份
         */
        year = calendar.get(Calendar.YEAR);
        /**
         * 获取月份
         */
        month = calendar.get(Calendar.MONTH) + 1;
        /**
         * 拼接年份和月份
         */
        date = year + "-" + ( month<10 ? "0" + month : month);
        /**
         * 返回当前年月
         */
        return date;
	}
	
	@Override
	public List<Object> querySuspinfoCountByBkjg(String roles,String yhdh,String bkjg)
			throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<String> categoryNameMap = new ArrayList<String>();//Y轴
		List<Map<String, List<String>>> dataList = new ArrayList<Map<String,List<String>>>();//X轴
		
		List<Map<String, Object>> datas = this.suspinfoDao.getSuspinfoCountByBkjg(roles,yhdh,bkjg);
		
		
		//if(datas.size()>0){
			initPie(datas);//初始化数组			
			//categoryNameMap.add("已申请");
			categoryNameMap.add("待审核");
			categoryNameMap.add("待审批");
			
			Map<String, List<String>> tempB = new HashMap<String, List<String>>();
			Map<String, List<String>> tempC = new HashMap<String, List<String>>();
			List<String> list1 = new ArrayList<String>();
			List<String> list2 = new ArrayList<String>();
			
			//Iterator<String> it = categoryNameList.iterator();  
			for(int k = 1;k<=categoryNameMap.size();k++){
				
				for(int i = 0;i<datas.size();i++){ //3                            					
					Map<String, Object> map = datas.get(i);				
					String jlzt = (String) map.get("JLZT");
					String ywzt = (String) map.get("YWZT");
					String value = map.get("TOTAL").toString();
					int isStatus = getCategoryXName(ywzt);//当前状态
					int isType = checkSuspinfoType(jlzt, ywzt);//布控 or 撤控
					if(isStatus==k){
						if(isType==1){
							list1.add(value);
							continue;
						}						
						if(isType==2){
							list2.add(value);
							continue;
						}
					}
				}
			}
			tempB.put("布控", list1);
			tempC.put("撤控", list2);
			dataList.add(tempB);
			dataList.add(tempC);
			result.add(categoryNameMap);
			result.add(dataList);
		//}		
		
		return result;//
	} 
	
	/**
	 * 判断Y轴类型
	 * @param jlzt
	 * @param ywzt
	 * @return
	 */
	private int checkSuspinfoType(String jlzt,String ywzt){
		int lable = 0;
		if(jlzt.equals("0") && ywzt.equals("11")){
			lable = 1;
		}else if(jlzt.equals("0") && ywzt.equals("12")){
			lable = 1;
		}else if((jlzt.equals("1") || jlzt.equals("2")) && ywzt.equals("14")){
			lable = 1;			
		}else if(jlzt.equals("1") && ywzt.equals("99")){
			lable = 1;
		}else if((jlzt.equals("1") || jlzt.equals("2")) && ywzt.equals("41")){
			lable = 2;
		}else if(jlzt.equals("1") && ywzt.equals("42")){
			lable = 2;
		}else if(jlzt.equals("2") && ywzt.equals("99")){
			lable = 2;
		}
		return lable;
	}
	
	/**
	 * 判断X轴类型
	 * @param ywzt
	 * @return
	 */
	private int getCategoryXName(String ywzt){
		int lable = 0;
		if(ywzt.equals("11") || ywzt.equals("41")){
			lable = 1;//已申请--待审核
		}else if(ywzt.equals("12") || ywzt.equals("42")){
			lable = 2;//已审核--待审批
		}
//		else if(ywzt.equals("14") || ywzt.equals("99")){
//			lable = 3;//已审批--已完成
//		}
		return lable;
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
		int count =0;
		boolean flag = this.suspinfoDao.isSusp(vspic);
		if(flag){//修改
			String picName = vspic.getZjlx();
			String clbksqbName = vspic.getClbksqblj();
			String lajdsName = vspic.getLajdslj();
			String yjcnsName = vspic.getYjcnslj();
			String sql = "";
			if(picName != null && !"".equals(picName)){
				sql = "UPDATE susp_picrec set zjlx = ?, zjwj = ? where bkxh = ?";
				AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
						lobHandler) {
					public void setValues(PreparedStatement pstmt,LobCreator lobCreator) throws SQLException,DataAccessException {
						final VehSuspinfopic picture = (VehSuspinfopic) this.getParameterObject();
						lobCreator.setBlobAsBytes(pstmt, 2, picture.getZjwj());
						pstmt.setString(1, picture.getZjlx());
						pstmt.setString(3, picture.getBkxh());
					}
				};
				callBack.setParameterObject(vspic);
				count += this.suspinfoDao.updateSuspPic(sql,callBack)?0:1;
			}
			if(clbksqbName != null && !"".equals(clbksqbName)){
				sql = "UPDATE susp_picrec set clbksqblj = ?, clbksqb = ? where bkxh = ?";
				AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
						lobHandler) {
					public void setValues(PreparedStatement pstmt,LobCreator lobCreator) throws SQLException,DataAccessException {
						final VehSuspinfopic picture = (VehSuspinfopic) this.getParameterObject();
						lobCreator.setBlobAsBytes(pstmt, 2, picture.getClbksqb());
						pstmt.setString(1, picture.getClbksqblj());
						pstmt.setString(3, picture.getBkxh());
					}
				};
				callBack.setParameterObject(vspic);
				count += this.suspinfoDao.updateSuspPic(sql,callBack)?0:1;
			}
			if(lajdsName != null && !"".equals(lajdsName)){
				sql = "UPDATE susp_picrec set lajdslj = ?, lajds = ? where bkxh = ?";
				AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
						lobHandler) {
					public void setValues(PreparedStatement pstmt,LobCreator lobCreator) throws SQLException,DataAccessException {
						final VehSuspinfopic picture = (VehSuspinfopic) this.getParameterObject();
						lobCreator.setBlobAsBytes(pstmt, 2, picture.getLajds());
						pstmt.setString(1, picture.getLajdslj());
						pstmt.setString(3, picture.getBkxh());
					}
				};
				callBack.setParameterObject(vspic);
				count += this.suspinfoDao.updateSuspPic(sql,callBack)?0:1;
			}
			if(yjcnsName != null && !"".equals(yjcnsName)){
				sql = "UPDATE susp_picrec set yjcnslj = ?, yjcns = ? where bkxh = ?";
				AbstractLobCreatingPreparedStatementCallbackImpl callBack = new AbstractLobCreatingPreparedStatementCallbackImpl(
						lobHandler) {
					public void setValues(PreparedStatement pstmt,LobCreator lobCreator) throws SQLException,DataAccessException {
						final VehSuspinfopic picture = (VehSuspinfopic) this.getParameterObject();
						lobCreator.setBlobAsBytes(pstmt, 2, picture.getYjcns());
						pstmt.setString(1, picture.getYjcnslj());
						pstmt.setString(3, picture.getBkxh());
					}
				};
				callBack.setParameterObject(vspic);
				count += this.suspinfoDao.updateSuspPic(sql,callBack)?0:1;
			}
		}else{//新增
			count +=  this.suspinfoDao.savePic(vspic)?0:1;
		}
		return (count<1)?false:true;
	}
}
