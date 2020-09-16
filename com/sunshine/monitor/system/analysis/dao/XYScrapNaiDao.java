package com.sunshine.monitor.system.analysis.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.analysis.bean.ScsVehPassrec;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

public interface XYScrapNaiDao extends BaseDao {
	/**
	 * 嫌疑报废、逾期未年检车辆查询
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findXyForPage(ScsVehPassrec veh,Map<String, Object> filter) throws Exception;
	
	/**
	 * <2016-11-23>licheng-嫌疑报废、逾期未年检车辆查询--显示过车次数
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findXyCsForPage(ScsVehPassrec veh,Map<String, Object> filter) throws Exception;
	
	/**
	 * 根据车牌号码、车牌类型等参数查询所有在嫌疑库中的过车数据 
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
//	public Map<String, Object> queryForXYList(VehPassrec veh,Map<String, Object> filter) throws Exception;
	
	/**
	 * 根据车牌号码、车牌类型查询所有在过车记录库中的车辆信息
	 * @param veh
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getXYCarInfo(String id) throws Exception; 
	
	/**
	 * 根据嫌疑库主键ID，修改嫌疑记录状态
	 * @param status
	 * @param xyids
	 * @return
	 * @throws Exception
	 */
	public int updateXYStatus(String status, String xyids) throws Exception;
	
	/**
	 * 新增记录到JM_ZYK_SCRAP_NAI (报废/逾期未年检库)
	 * @param xyid 嫌疑库主键ID
	 * @return
	 * @throws Exception
	 */
//	public int insertScrapNaiInfo(String xyId) throws Exception;
}
