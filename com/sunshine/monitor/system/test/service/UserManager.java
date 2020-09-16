package com.sunshine.monitor.system.test.service;

import java.util.List;

import com.sunshine.monitor.system.test.bean.User;

public interface UserManager {
	
	public User queryUser(String userName, String password) throws Exception;
	
	public List<User> findUserList() throws Exception;
}
