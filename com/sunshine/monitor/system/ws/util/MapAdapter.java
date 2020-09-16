package com.sunshine.monitor.system.ws.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter extends XmlAdapter<MapConvertor, Map<String, String>> {

	@Override
	public MapConvertor marshal(Map<String, String> map) throws Exception {
		MapConvertor convertor = new MapConvertor();
		for(Map.Entry<String, String> entry:map.entrySet()){
			
			MapConvertor.MapEntry e = new MapConvertor.MapEntry(entry);
			convertor.addEntry(e);
		}
		return convertor;
	}

	@Override
	public Map<String, String> unmarshal(MapConvertor map) throws Exception {
		Map<String, String> result = new HashMap<String,String>();
		for(MapConvertor.MapEntry e :map.getEntries()){
			result.put(e.getKey(), e.getValue());
		}
		return result;
	}

}