package com.sunshine.monitor.system.monitor.service;

public interface StatIdentManager {

	public String getIdentListXML(String kssj, String jssj, String jkdss) throws Exception;

	public String getHourIdentListXML(String kssj, String jssj, String kdbh,String fxbh, String cdbh) throws Exception;
}
