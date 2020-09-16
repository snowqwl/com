package com.sunshine.monitor.system.test.dao.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl;
import com.sunshine.monitor.system.test.bean.User;
import com.sunshine.monitor.system.test.dao.UserDao;

@Repository
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

	private Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	public User queryUser(String userName, String password) throws Exception {
		String sql = "select * from user_temp where userName = ? and password = ?";
		return this.queryObject(sql, User.class,userName,password);
	}

	public List<User> findUserList() throws Exception {
		logger.info(".....执行数据库查询......");
		String sql = "select * from user_temp";
		return this.queryList(sql, User.class);
	}
}
