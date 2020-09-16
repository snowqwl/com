package com.sunshine.monitor.comm.vehicle.service;

import java.util.Map;

import com.dragonsoft.adapter.service.AdapterSend;
import com.sunshine.monitor.comm.vehicle.IVehicle;
import com.sunshine.monitor.comm.vehicle.KeyConfig;

public class VehicleRbspImpl implements IVehicle, KeyConfig {
	/**
	 * 适配器
	 */
	private AdapterSend rbspSend;

	public VehicleRbspImpl() {
		rbspSend = new AdapterSend();
	}

	@Override
	public <K, V> Object query(Map<K, V> map) throws Exception {
		String strCondition = "HPHM='" + map.get(KEY_HPHM).toString()
				+ "' AND HPZL='" + map.get(KEY_HPZL).toString() + "'";
		return rbspSend.sendQuery(map.get(KEY_QUERY_GROUP_NAME).toString(), strCondition,
				map.get(KEY_CARD_ID).toString(), map.get(KEY_JH).toString(),
				map.get(KEY_DEPTNO).toString());
	}
}
