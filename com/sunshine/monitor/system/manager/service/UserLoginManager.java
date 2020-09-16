package com.sunshine.monitor.system.manager.service;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.system.manager.bean.UserLoginXls;

public interface UserLoginManager {
	
	public Map<String,Object> queryList(Map<String, Object> conditions )throws Exception;
	
	public Map<String,Object> queryForDetail(Map<String, Object> conditions )throws Exception;
	
	public List<UserLoginXls> getUserLoginList(UserLoginXls info)throws Exception;
}
