package com.sunshine.monitor.system.zdcars.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sunshine.monitor.system.zdcars.bean.CarS;

public interface ZdCarsService {
	public Map<String , Object> saveCars(CarS car) throws Exception ;
	public Map<String , Object> saveCarsInfo(CarS car) throws Exception;
	public Object selectCarsResult(String key,String value,HttpServletRequest request) throws Exception;
	public void downloadResult();
}
