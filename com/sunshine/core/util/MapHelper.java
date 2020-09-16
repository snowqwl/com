package com.sunshine.core.util;

import java.util.HashMap;

public class MapHelper {
	private MapHelper(){}
	
	public static HashMap newHashMap(Object... objects ){
		HashMap map = new HashMap();
		for(int i = 0; i+1<objects.length ; i+=2){
			map.put(objects[i], objects[i+1]);
		}
		return map;
	}
}
