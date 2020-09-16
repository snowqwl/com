package com.sunshine.monitor.comm.vehicle;

import java.util.Map;

public interface IVehicle {
	
	<K,V> Object query(Map<K,V> map) throws Exception;
	
}
