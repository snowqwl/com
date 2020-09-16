package com.sunshine.monitor.system.susp.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.comm.util.AbstractLobCreatingPreparedStatementCallbackImpl;
import com.sunshine.monitor.comm.util.orm.helper.QueryCondition;
import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.bean.TransSuspmonitor;
import com.sunshine.monitor.system.susp.bean.GkSuspinfoApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;

public interface SuspinfoDao extends BaseDao {

	/**
	 * 按条件查询分页
	 * @param condition
	 * @return
	 */
	public Map<String, Object> querySuspinfoByPage(Map<String, Object> condition) throws Exception;
	
	public Map<String, Object> querySuspinfoByPage(Map<String, Object> condition,Class<?> classz) throws Exception;

	@Deprecated
	/**
	 * 建议使用saveSuspinfo2方法
	 */
	public Object saveSuspinfo(VehSuspinfo vs)throws Exception;
	
	/**
	 * 保存布控信息
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveSuspinfo2(VehSuspinfo bean) throws Exception;
	
	/**
	 * 获取布控序号
	 * @return
	 * @throws Exception
	 */
	public String getBkxh(String bkjg)throws Exception;
	
	public Object updateSuspinfo(VehSuspinfo vs)throws Exception;
	
	//public int saveSuspinfoLink(VehSuspinfo bean) throws Exception;
	public int saveSuspinfoLink(TransSusp bean) throws Exception;
	
	public int saveSuspinfoLinkST(TransSusp bean) throws Exception;
	
	public int saveSuspinfomonitorLink(TransSuspmonitor bean) throws Exception;
	
	public int saveCsuspinfoLink(TransSusp bean) throws Exception;
	
	public VehSuspinfo findSuspInfo(String hphm,String ysbh);
	
	public boolean saveSuspinfopictrue(VehSuspinfopic vspic)throws Exception;
	
	public boolean isSusp(VehSuspinfopic vspic)throws Exception;
	
	public boolean updateSuspPic(String sql,AbstractLobCreatingPreparedStatementCallbackImpl callBack)throws Exception;
	
	public boolean savePic(VehSuspinfopic vspic)throws Exception;
	
	public List checkSuspinfo(String hphm)throws Exception;
	/**
	 * 查询布控信息
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getSuspinfoDetail(String bkxh)throws Exception;
	
	/**
	 * 查询布控信息（各地市）
	 * @param bkxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getCitySuspDetail(String bkxh,String cityname) throws Exception;
	
	public TransSusp getTransSuspDetail(String bkxh)throws Exception;

	public Map<String,Object> getSuspinfoByHphm(String hphm,String hpzl,boolean flag) throws Exception;

	
	/**
	 * 查询布控信息
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getVehsuspinfo(String bkxh)throws Exception;

	//--------------------------------------撤控确认-------------------------------------------
	
	/**
	 * 查询须要撤控确认的信息（布控审批用户）
	 * @param conditions 条件(key-value)
	 * @return Map
	 * @throws Exception
	 * @author OUYANG 2013/6/28 
	 */
	public Map<String, Object> querySuspinfoCancelList(Map<String,Object> conditions) throws Exception;
	
	/**
	 * 保存撤控确认
	 * @param vehSuspinfo
	 * @return
	 */
	public int saveSuspinfoCancelsign(VehSuspinfo vehSuspinfo) throws Exception;
	
	/**
	 * 撤控操作
	 * @param vehSuspinfo
	 * @return
	 * @throws Exception
	 */
//	public int saveSuspinfoCancel(VehSuspinfo vehSuspinfo) throws Exception;
	
	/**
	 * 
	 * @param subSql
	 * @return
	 * @throws Exception
	 */
	public List<VehSuspinfo> querySuspinfo(String subSql) throws Exception;

	/**
	 * 查询须要撤控确认的信息(指挥中心用户)
	 * @param conditions 条件(key-value)
	 * @return Map
	 * @throws Exception
	 * @author OUYANG 2013/6/28 
	 */
	public Map<String, Object> querySuspinfoCancelListForZx(
			Map<String, Object> conditions);
	
	
	public VehSuspinfo save(VehSuspinfo info ) throws Exception ;
	
	public VehSuspinfo update(VehSuspinfo info) throws Exception ; 
	
	public List<VehSuspinfo>  getDxhmByHpdm(String hphm) throws Exception;
	
	/**
	 * 管控类核查
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryHCSuspinfo(Map<String, Object> page, String con) throws Exception;
	
	/**
	 * 添加管控核查记录
	 * @param gsf
	 * @throws Exception
	 */
	public void insertHCsuspinfo(GkSuspinfoApprove gsf) throws Exception;
	
	/**
	 * 查询当前登录用户 布控/撤控 未审批，未审核总数
	 * @param trafficFlow
	 * @param yhdh
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSuspinfoCountByBkr(String bkjg,String curDate) throws Exception;
	
	/**
	 * 获取当前登录用户所属部门 已申请，已审核，已审批的总数
	 * @param trafficFlow
	 * @param yhdh
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSuspinfoCountByBkjg(String roles,String yhdh,String bkjg) throws Exception;
	
	
}
