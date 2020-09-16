package com.sunshine.monitor.system.analysis.bean;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
/**
 * 分析类型常量表
 *
 */
public enum NightCarAnalysisType {
	COM_REG("1", "常规昼伏夜出"), 
	CON_REG("2", "连续性昼伏夜出"), 
	DAY_REG("3", "指定日期昼伏夜出");
	private String code;
	private String desc;

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	private NightCarAnalysisType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static Map<String,String> getAnalysisType() {
		NightCarAnalysisType[] arrys = NightCarAnalysisType.values();
		Map<String, String> resut = new TreeMap<String, String>();
		for (NightCarAnalysisType type : arrys) {
			resut.put(type.code, type.desc);
		}
		return resut;
	}
	
	public static void main(String[] args) {
		Map<String,String> map = NightCarAnalysisType.getAnalysisType();
		Set<Entry<String,String>> set = map.entrySet();
		Iterator<Entry<String,String>> it = set.iterator();
		while(it.hasNext()){
			Entry<String,String> entry = it.next();
			System.out.println("code:" + entry.getKey() + ",desc:" + entry.getValue());
		}
	}
}
