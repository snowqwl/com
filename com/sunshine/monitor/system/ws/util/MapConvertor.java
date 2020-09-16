package com.sunshine.monitor.system.ws.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MapConvertor")
@XmlAccessorType(XmlAccessType.FIELD)
public class MapConvertor {
	
	private List<MapEntry> entries = new ArrayList<MapEntry>();
		public void addEntry(MapEntry entry){
		entries.add(entry);
	}
	
	public static class MapEntry{
		public MapEntry() {
			super();

		}
		public MapEntry(Map.Entry<String,String> entry) {
			super();
			this.key = entry.getKey();
			this.value = entry.getValue();
		}
		public MapEntry(String key,String value) {
			super();
			this.key = key;
			this.value = value;
		}
		private String key;
		private String value;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	public List<MapEntry> getEntries() {
		return entries;
	}
}