package com.sunshine.monitor.system.susp.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.activemq.bean.TransSusp;
import com.sunshine.monitor.system.activemq.bean.TransSuspmonitor;
import com.sunshine.monitor.system.analysis.bean.TrafficFlow;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.susp.bean.GkSuspinfoApprove;
import com.sunshine.monitor.system.susp.bean.VehSuspinfo;
import com.sunshine.monitor.system.susp.bean.VehSuspinfopic;

public interface SuspinfoManager {

	/**
	 * 布控列表查询
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> querySuspinfoByPage(Map<String, Object> condition)
			throws Exception;
	
	
	/**
	 * 查询本地布控信息
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryLocalSuspinfoByPage(Map<String, Object> condition)
			throws Exception;
	
	
	
	/**
	 * 查询本地模糊布控信息
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryLocalMhSuspinfoByPage(Map<String, Object> condition)
			throws Exception;
	
	
	
	/**
	 * 查询联动布控信息
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryLinkSuspinfoByPage(Map<String, Object> condition)
			throws Exception;
	

	/**
	 * 根据号牌号码、号牌种类查询布控列表
	 * 
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getSuspinfoByHphm(String hphm, String hpzl,boolean flag)
			throws Exception;

	/**
	 * 保存布控图片
	 * 
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public boolean saveSuspinfopictrue(VehSuspinfopic vspic) throws Exception;

	/**
	 * 新增布控
	 * 
	 * @param bean
	 * @param vpic
	 * @return
	 * @throws Exception
	 */
	public Object saveSuspinfo(VehSuspinfo bean) throws Exception;
	/**
	 * 联动布控状态监测
	 * 
	 * @param bean(TransSuspmonitor)
	 * @return
	 * @throws Exception
	 */
	public int saveSuspinfomonitorLink(TransSuspmonitor bean) throws Exception;
	
	/**
	 * 修改布控
	 * 
	 * @param bean
	 * @param vpic
	 * @return
	 * @throws Exception
	 */
	public Object updateSuspinfo(VehSuspinfo bean) throws Exception;
	
	//public int saveSuspinfoLink(VehSuspinfo bean) throws Exception;
	/**
	 * 新增联动布控
	 */
	public int saveSuspinfoLink(TransSusp bean) throws Exception;
	
	/**
	 * 新增联动布控——省厅
	 */
	public int saveSuspinfoLinkST(TransSusp bean) throws Exception;
	
	/**
	 * 保存联动撤控
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public int saveCsuspinfoLink(TransSusp bean) throws Exception;
	
	/**
	 * [新增布控]事务
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public Object saveVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception;
	
	/**
	 * [新增布控--过滤红名单]事务
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public Object saveVehSuspinfoFilterRedList(VehSuspinfo bean, VehSuspinfopic vspic)
	throws Exception ;
	
	
	/**
	 * [新增本市布控]
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public Object saveLocalVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception;
	
	
	/**
	 * [新增一键布控]
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public Object saveAutoVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic) throws Exception;
			
	
	/**
	 * [新增本市模糊布控]
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public Object saveLocalMhVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception; 
	
	
	/**
	 * [新增联动布控]
	 * 
	 * @param bean
	 * @param vspic
	 * @return
	 * @throws Exception
	 */
	public Object saveLinkVehSuspinfo(VehSuspinfo bean, VehSuspinfopic vspic)
			throws Exception; 
	

	/**
	 * 布控范围节点列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CodeUrl> getBkfwListTree() throws Exception;
	

	/**
	 * 查询布控信息(非关联转换)
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getSuspInfo(String bkxh) throws Exception;
	
	/**
	 * 查询布控信息(已关联转换)
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getSuspDetail(String bkxh) throws Exception;
	
	/**
	 * 查询布控信息（各地市）
	 * @param bkxh
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public VehSuspinfo getCitySuspDetail(String bkxh,String cityname) throws Exception;
	
	/**
	 * 查询传输布控信息
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public TransSusp getTransSusp(String bkxh) throws Exception;

	//--------------------------------------撤控确认-------------------------------------------
	/**
	 * 查询须要撤控确认的信息
	 * @param conditions 条件(key-value)
	 * @return Map
	 * @throws Exception
	 * @author OUYANG 2013/6/28 
	 * @param tip 
	 */
	public Map<String, Object> querySuspinfoCancelList(Map<String, Object> conditions, String tip) throws Exception ;

	/**
	 * 保存撤控确认
	 * @param vehSuspinfo
	 * @return
	 */
	public int saveSuspinfoCancelsign(VehSuspinfo vehSuspinfo) throws Exception;
	
	/**
	 * 系统自动撤控
	 * @param vehSuspinfo
	 * @return
	 * @throws Exception
	 */
	public int  saveAutomaticCancel(VehSuspinfo vehSuspinfo) throws Exception;
	
	/**
	 * 
	 * @param vehSuspinfo
	 * @param isforce 是否执行检查
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryValidGKSuspinfo(VehSuspinfo vehSuspinfo, boolean isforce) throws Exception;
	
	/**
	 * 验证布控记录是否有更高级别
	 * @param hphm
	 * @param bkr
	 * @param bkdl
	 * @return
	 * @throws Exception
	 */
	public String checkSuspinfo(String hphm,String bkdl) throws Exception;
	
	/**
	 * 通过号牌号码取布控中的bkjglxdh
	 * @param vehSuspinfo
	 * @return
	 */
	public List<VehSuspinfo>  getDxhmByHpdm(String hphm) throws Exception;
	
	public void saveLjNotCancalSj(VehSuspinfo vehSuspinfo,List<VehSuspinfo> fsdxhmlist) throws Exception;
	
	/**
	 * 管控核查列表
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryHCSuspinfo(Map<String, Object> con) throws Exception;

	/**
	 * 添加管控核查记录
	 * @param gsf
	 * @throws Exception
	 */
	public void insertHCsuspinfo(GkSuspinfoApprove gsf) throws Exception;
	
	/**
	 * 
	 * @param bjxh 预警序号
	 * @return
	 * @throws Exception
	 */
	public GkSuspinfoApprove queryGkSuspinfoApprove(String glbm) throws Exception;
	
	/**
	 * 获得当前登录用户 布控/撤控 未审批，未审核总数
	 * @param yhdh
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> querySuspinfoCountByBkr(String bkjg) throws Exception;
	
	/**
	 * 获取当前登录用户所属部门 已申请，已审核，已审批的总数
	 * @param yhdh
	 * @return
	 * @throws Exception
	 */
	public List<Object> querySuspinfoCountByBkjg(String roles,String yhdh,String glbm) throws Exception;
}
