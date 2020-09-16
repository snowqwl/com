package com.sunshine.monitor.system.query.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.bean.ModelDetail;
import com.sunshine.monitor.system.analysis.bean.WhilteName;
import com.sunshine.monitor.system.query.bean.Surveil;
import com.sunshine.monitor.system.query.bean.SuspMonitor;
import com.sunshine.monitor.system.query.bean.VehAlarmrecIntegrated;
import com.sunshine.monitor.system.query.bean.VehPassrecIntegrated;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface QueryListDao extends BaseDao{

	/**
	 * 
	 * 函数功能说明：单表分页查询
	 * 修改日期 	2013-6-28
	 * @param map：页面分页参数(页码和行数)
	 * @param tableName :表名
	 * @param condition :查询条件
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map getMapForFilter(Map map,String tableName,StringBuffer condition)throws Exception;
	
	/**
	 * 函数功能说明：查询布控表
	 * @param map
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map getMapForSuspinfoFilter(Map map,StringBuffer condition)throws Exception;
	
	/**
	 * 函数功能说明：查询各地市布控信息
	 * @param map
	 * @param condition
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public Map getMapForCitySuspinfoFilter(Map map,StringBuffer condition,String cityname)throws Exception;
	
	public Map getSuspinfoMapForFilerByHphm(Map map, String hphm,String bkjg)throws Exception;
	/**
	 * 
	 * 函数功能说明:集中库查询过车数据
	 * 修改日期 	2013-7-19
	 * @param map
	 * @param info
	 * @param citys
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map getMapForIntegrate(Map map,VehPassrec info,String citys)throws Exception;

	/**
	 * 
	 * 函数功能说明：查询交通违法记录
	 * 修改日期 	2013-7-20
	 * @param filter
	 * @param info
	 * @param citys
	 * @param wflxTab 
	 * @return
	 * @throws Exception    
	 * @return Map
	 */
	public Map getMapForIntegrateTraffic(Map filter, VehPassrec info,
			String citys, String wflxTab)throws Exception;
	
	/**
	 * 根据号牌号码集合查询车辆违法次数
	 * 缺少号牌种类
	 * 请使用getViolationCount方法
	 * @param hphms
	 * @return
	 * @throws Exception
	 */
	@Deprecated
    public Map<String,Object> getTrafficCount(List<String> hphms)throws Exception;
   
    /**
     * 根据号牌号码查询车辆违法历史记录
     * 缺少号牌种类
     * 请使用getViolationDetail方法
     * @param hphms
     * @return
     * @throws Exception
     */
    @Deprecated
    public Map<String,Object> getTrafficDetail(String hphms,Map<String,Object> filter)throws Exception;
    
    /**
     * 根据号牌号码，号牌种类查询车辆违法次数
     * @param cars
     * @return
     * @throws Exception
     */
	public Map<CarKey,Integer> getViolationCount(List<CarKey> cars)throws Exception;
	
	/**
	 * 根据号牌号码，号牌种类查询查询车辆违法历史记录
	 * @param car
	 * @param filter
	 * @return
	 */
	public Map<String,Object> getViolationDetail(CarKey car,Map<String,Object> filter)throws Exception;
	
 	/**
	 * 函数功能说明：集中库查询报警信息
	 * @param filter
	 * @param info
	 * @param citys
	 * @return
	 * @throws Exception
	 */
    public Map getMapForIntegrateAlarm(Map filter,VehAlarmrecIntegrated info,String citys)throws Exception;
	    
	public VehPassrecIntegrated getIntegratedPassDetail(String hphm,String kdbh,String gcsj,String cxlx)throws Exception;
	
	public List<SuspMonitor> getSuspMonitorListForBkxh(String bkxh)throws Exception;

	public Surveil getSuvreitForXh(String jdsbh);
	
	public List<Map<String, Object>> getViolationForWfbh(String wfbh);
	
	public String getPicPath(String xh)throws Exception;
	
	public  VehPassrecIntegrated getAlarmrec(String gcxh)throws Exception;
	
	public VehAlarmrecIntegrated getIntegratedAlarmDetial(String bjxh)throws Exception;
	
	/**
	 * 函数功能说明：查询四率两数
	 * @param glbm
	 * @param kssj
	 * @param jssj
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getstat4Rate2NumList(String glbm,String kssj,String jssj)throws Exception;

	public List<Map<String, Object>> getForceForXh(String xh);
	
	/**
	 * 查询省厅KK库中是否存在当前地市的dblink
	 * @param cityname
	 * @return
	 * @throws Exception
	 */
	public int getAllDbLink(String cityname)throws Exception;
	/**
	 * 布撤控信息查询中增加布控审批人
	 * @param bkxh
	 * @return
	 * @throws Exception
	 */
	public String getBksprByBkxh(String bkxh)throws Exception;
	
	/**
	 * @author huanghaip
	 * @date 2017-3-23
	 * 获取联动布控反馈列表
	 * @param bkxh 布控序号
	 * @param bkjg 布控结果：已布控，已撤控
	 * @return
	 * @throws Exception
	 */
	public List<SuspMonitor> getSuspMonitorList(String bkxh, String bkjg)throws Exception;
	
	/**
	 * 查询模型列表
	 * @param Id
	 * @return
	 * @throws Exception
	 */
	public ModelDetail getById(String Id) throws Exception;

}
