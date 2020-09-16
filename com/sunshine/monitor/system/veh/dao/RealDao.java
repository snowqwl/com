package com.sunshine.monitor.system.veh.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.veh.bean.PropertyBean;
import com.sunshine.monitor.system.veh.bean.VehRealpass;

public interface RealDao extends BaseDao{

	public abstract VehRealpass getVehRealPassM(String kdbh,
			String fxbh, String cdbh);

	public abstract Map findAlarmForMap(Map map,StringBuffer condition);

	public abstract List getRealPassList(String[] kds);

	public abstract VehRealpass getVehRealDetail(String gcxh);
	
	//public abstract VehRealpass getVehRealDetailByParams(String kdbh,String fxbh,String cd);
	public abstract VehRealpass getVehRealDetailByParams(String gcxh);
	
	/**
	 * 查询某卡口今日过车总数
	 * @oy
	 * @param kdbh
	 * @param cdbh 
	 * @param fxbh 
	 * @return
	 */
	public long getVehpassByKdbh(String kdbh, String fxbh, String cdbh);

	public abstract VehRealpass getJianKongInfo(String kdbh,String fxbh);
	
	public Map<String,Object> findPageRealForMap(Map<String,Object> map,Class<?> clazz) throws Exception;
	
	//判断超时
	public long getDiffTime(String time) throws Exception ;
}
