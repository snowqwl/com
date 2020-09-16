package com.sunshine.monitor.comm.util.redis;

import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 查询redis数据的对系统接口
 * @author yingshaobu
 *
 */
public class JedisService {
	
	private String driverWayId = "kkMetrics_driverWayId";
	private String directionType = "kkMetrics_directionType";
	private String gate = "kkMetrics_gate";
	/**
	 * 单卡口实时过车查询,查询条件包括卡口编号,方向类型,车道号
	 * @param kdbh
	 * @param fxlx
	 * @param cdbh
	 * @return
	 */
	public Map<String,Object> getVehRealPassRedis(String kdbh,String fxlx, String cdbh) {
		String key = kdbh+"|"+fxlx+"|"+cdbh;
		Object value = ExRedisUtils.hget(driverWayId, key);
		Map<String,Object> vehpass = JSON.parseObject((String) value);
		return vehpass;
	}
	
	/**
	 * 单卡口实时过车查询,查询条件包括卡口编号
	 * @param kdbh
	 * @return
	 */
	public Map<String,Object> getVehRealPassRedis(String kdbh) {
		Object value = ExRedisUtils.hget(gate, kdbh);
		Map<String,Object> vehpass = JSON.parseObject((String) value);
		return vehpass;
	}
	
	/**
	 * 单卡口实时过车查询,查询条件包括卡口,方向类型
	 * @param kdbh
	 * @param fxlx
	 * @return
	 */
	public Map<String,Object> getVehRealPassRedis(String kdbh, String fxlx) {
		String key = kdbh+"|"+fxlx;
		Object value = ExRedisUtils.hget(directionType, key);
		Map<String,Object> vehpass = JSON.parseObject((String) value);
		return vehpass;
	}
}
