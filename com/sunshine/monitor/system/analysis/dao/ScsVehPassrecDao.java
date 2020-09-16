package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.analysis.bean.VehicleEntity;
import com.sunshine.monitor.system.analysis.bean.VehpassCount;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface ScsVehPassrecDao extends ScsBaseDao {

	/**
	 * 统计过车查询相关信息
	 * @return
	 */
	public List<VehpassCount> getVehpasssCountList(String dateString) throws Exception;
	
	/**
	 * 根据号牌号码，号牌种类查询最新一条过车记录
	 */
	public List<VehPassrec> getLatestVehPassrec(String hphm, String hpzl) throws Exception;
	
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
	 * 查询机动车信息明细
	 * @param hphm
	 * @param hpzl
	 * @return
	 * @throws Exception
	 */
	public VehicleEntity getVehicleInfo(VehicleEntity veh)throws Exception;
	
	/**
	 * 批量获取机动车信息-greenplum
	 * @param cars
	 * @return
	 * @throws Exception 
	 */
	public Map<CarKey, VehicleEntity> getVehicleList(List<CarKey> cars) throws Exception;
	
	/**
	 * 根据sql和参数查询过车列表数据
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryGjPassrecListExt(String sql, Object[] params) throws Exception;
	
	/**
	 * 根据sql和参数查询过车列表总数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer queryGjPassrecListTotal(String sql, Object[] params) throws Exception;
	
	public List<Map<String, Object>> queryHideDayHotView(String sql) throws Exception;
	
	/**
	 * 根据过车序号查询过车记录
	 * @param colmuns 需要查询的字段
	 * @param gcxh 车车序号
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVehPassrecByGcxh(String colmuns, String gcxh) throws Exception; 
	
}
