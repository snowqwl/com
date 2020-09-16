package com.sunshine.monitor.system.manager.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.manager.bean.UserLoginXls;

public interface UserLoginDao extends BaseDao{
	public Map<String, Object> queryList(Map<String, Object> conditions)throws Exception;
	
	public Map<String, Object> queryForDetail(Map<String, Object> conditions)throws Exception;
	
	public List<UserLoginXls> getUserLoginList(StringBuffer condition)throws Exception;
}
