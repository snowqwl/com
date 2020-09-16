package com.sunshine.monitor.system.query.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
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
import com.sunshine.monitor.comm.util.Common;
import com.sunshine.monitor.system.manager.bean.BusinessLog;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.service.LogManager;
import com.sunshine.monitor.system.query.dao.ReQueryListDao;
import com.sunshine.monitor.system.query.service.ReSuspinfoQueryManager;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;
import com.sunshine.monitor.system.susp.dao.SuspinfoDao;
import com.sunshine.monitor.system.susp.dao.SuspinfoEditDao;

@Service
@Transactional
public class ReSuspinfoQueryManagerImpl implements ReSuspinfoQueryManager {
	
	/**
	 * 布控表索引列表
	 */
	private static final String IDX_BKR  = "IDX_PK_VEH_SUSPINFO_BKR";
    private static final String IDX_BKJG = "IDX_VEH_SUSPINFO_BKJG";
    private static final String IDX_BKSJ = "IDX_VEH_SUSPINFO_BKSJ";
    private static final String IDX_HPHM = "IDX_VEH_SUSPINFO_HPHM";
    private static final String IDX_BKXH = "IDX_VEH_SUSPINFO_BKXH";
	
    @Autowired
	private ReQueryListDao reQueryListDao;
    
    @Autowired
	private SuspinfoDao suspinfoDao;
    
    @Resource(name = "redlistCache")
	private Cache cache;
    
    @Autowired
	@Qualifier("suspinfoEditDao")
	private SuspinfoEditDao suspinfoEditDao;
    
    @Autowired
	private LogManager logManager;
    
    @Autowired
	private SystemDao systemDao;
    
    @Autowired
	@Qualifier("maintainDao")
	private MaintainDao maintainDaoImpl;
    
    @Autowired
	@Qualifier("oracleLobHandler")
	public LobHandler lobHandler;

	@Override
	public int updates(List<Map<String, Object>> listrows) {
		return reQueryListDao.updates(listrows);
	}
    
    @Override
	public int insert(List<Map<String, Object>> listrows) {
		return reQueryListDao.insert(listrows);
	}

	@Override
	public List<Map<String, Object>> query(String bkxh) {
		return reQueryListDao.query(bkxh);
	}
    
    
	public Map getSuspinfoMapForFilter(Map filter, VehSuspinfo info,
			String conSql) throws Exception {
		StringBuffer sb = new StringBuffer("  1=1 AND ywzt in('14','15','41','42')  AND jlzt<>'0' AND bkjzsj>sysdate ");
		StringBuffer idxSql = new StringBuffer();
		Boolean flag = true;
		if(StringUtils.isNotBlank(conSql)){
			sb.append(conSql);
		}
		
		if(StringUtils.isNotBlank(info.getBkxh())){
			sb.append(" and bkxh = '").append(info.getBkxh()).append("' ");
			if(flag){
				idxSql.append(IDX_BKXH+" ");
				flag=false;
			}
		}
		
		if(StringUtils.isNotBlank(info.getHphm())){
			sb.append("  and  hphm  like '%").append(Common.changeHphm(info.getHphm())).append("%'");
			if(flag){
				idxSql.append(IDX_HPHM+" ");
				flag=false;
			}
		}
		
		if(info.getBkr()!= null && info.getBkr().length() > 0){
			sb.append("  and bkr ='").append(info.getBkr()).append("' ");
			if(flag){
				idxSql.append(IDX_BKR+" ");
				flag=false;
			}
		}
		
		if(StringUtils.isNotBlank(info.getBkjg())){
			sb.append(" and bkjg = '").append(info.getBkjg()).append("' ");
			if(flag){
				idxSql.append(IDX_BKJG+" ");
				flag=false;
			}
		}
		
		if(StringUtils.isNotBlank(info.getKssj()))
			sb.append(" and bksj >=to_date('").append(info.getKssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		
		if(StringUtils.isNotBlank(info.getJssj()))
			sb.append(" and bksj <=to_date('").append(info.getJssj()).append("', 'yyyy-mm-dd hh24:mi:ss') ");
		if(flag){
			idxSql.append(IDX_BKSJ+" ");
			flag=false;
		}
		
		if(StringUtils.isNotBlank(info.getBkdl())){
			sb.append(" and bkdl = '").append(info.getBkdl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBklb())){
			sb.append("  and bklb = '").append(info.getBklb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getXxly())){
			sb.append(" and xxly = '").append(info.getXxly()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getHpzl())){
			sb.append(" and hpzl = '").append(info.getHpzl()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBkjb())){
			sb.append(" and bkjb = '").append(info.getBkjb()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getJlzt())){
			sb.append(" and jlzt = '").append(info.getJlzt()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBjya())){
			sb.append(" and bjya = '").append(info.getBjya()).append("' ");
		}
		
		if(StringUtils.isNotBlank(info.getBjzt())){
			sb.append(" and bjzt = '").append(info.getBjzt()).append("' ");
		}
		
		if(filter.get("city")!=null){
		if(StringUtils.isNotBlank(filter.get("city").toString())){
			sb.append(" and bkjg like '").append(filter.get("city")).append("%' ");
		}
		}
		sb.append("  order by bksj desc ");
		
		filter.put("idx_sql","/*+index(veh_suspinfo "+idxSql.toString()+")+*/");
		return reQueryListDao.getMapForSuspinfoFilter(filter,  sb);
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
			
			if (bean.getBkdl().equals("3")) {
				sendSmsToSuspAuditPeople(bean,bean,suspInfo);
			}
		}
		map.put("code", status);
		map.put("msg", "新增布控成功!");
		return map;
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
			//String picName = vspic.getZjlx();
			String clbksqbName = vspic.getClbksqblj();
			String lajdsName = vspic.getLajdslj();
			String yjcnsName = vspic.getYjcnslj();
			String sql = "";
			/*if(picName != null && !"".equals(picName)){
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
			}*/
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
		String content = "您有一条【车辆号牌号码：" + suspInfo.getHphm()+"、号牌种类："+this.systemDao.getCodeValue("030107",suspInfo.getHpzl())+"、布控大类："+this.systemDao.getCodeValue("120019",suspInfo.getBkdl())+"、布控小类："+this.systemDao.getCodeValue("120005",suspInfo.getBklb())+"】的续控记录需要审核！";
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


}
