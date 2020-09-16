package com.sunshine.monitor.system.test.dao;

import java.util.List;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.system.test.bean.User;

public interface UserDao extends BaseDao {
	
	public User queryUser(String userName, String password) throws Exception;
	
	public List<User> findUserList() throws Exception ;
	
}
