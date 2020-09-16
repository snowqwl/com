package com.sunshine.monitor.system.test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.monitor.system.test.bean.User;
import com.sunshine.monitor.system.test.dao.UserDao;
import com.sunshine.monitor.system.test.service.UserManager;

@Transactional
@Service
public class UserManagerImpl implements UserManager {

	@Autowired
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User queryUser(String userName, String password) throws Exception {
		return this.userDao.queryUser(userName, password);
	}
	
	@Cacheable(value="jcbkCache", key="#root.methodName", condition="") 
	public List<User> findUserList() throws Exception {
		return this.userDao.findUserList();
	}
}
