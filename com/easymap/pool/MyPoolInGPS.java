package com.easymap.pool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyPoolInGPS {

	private static MyPoolInGPS instance = null;
	private int maxConnect = 100;
	private int normalConnect = 10;
	private String url = DBInfoConfig.getInstance().getValue("DB.URL");
	private String user = DBInfoConfig.getInstance().getValue("DB.USER");
	private String password = DBInfoConfig.getInstance().getValue("DB.PASSWORD");
	private String driverName = DBInfoConfig.getInstance().getValue("DB.DRIVER");
	Driver driver = null;
	DBConnectionPool pool = null;

	public static synchronized MyPoolInGPS getInstance() {
		if (instance == null)
			instance = new MyPoolInGPS();
		return instance;
	}

	private MyPoolInGPS() {
		loadDrivers(this.driverName);
		createPool();
	}

	private void loadDrivers(String paramString) {
		String str = paramString;
		try {
			this.driver = ((Driver) Class.forName(str).newInstance());
			DriverManager.registerDriver(this.driver);
			System.out.println("成功注册JDBC驱动程序" + str);
		} catch (Exception localException) {
			System.out.println("无法注册JDBC驱动程序:" + str + ",错误:" + localException);
		}
	}

	private void createPool() {
		this.pool = new DBConnectionPool(this.password, this.url, this.user,this.normalConnect, this.maxConnect);
		if (this.pool != null)
			System.out.println("创建连接池成功");
		else
			System.out.println("创建连接池失败");
	}

	public Connection getConnection() {
		if (this.pool != null)
			return this.pool.getConnection();
		return null;
	}

	public Connection getConnection(long paramLong) {
		if (this.pool != null)
			return this.pool.getConnection(paramLong);
		return null;
	}

	public void freeConnection(Connection paramConnection) {
		if (this.pool == null)
			return;
		this.pool.freeConnection(paramConnection);
	}

	public int getnum() {
		return this.pool.getnum();
	}

	public int getnumActive() {
		return this.pool.getnumActive();
	}

	public synchronized void release() {
		this.pool.release();
		try {
			DriverManager.deregisterDriver(this.driver);
			System.out.println("撤销JDBC驱动程序 " + this.driver.getClass().getName());
		} catch (SQLException localSQLException) {
			System.out.println("无法撤销JDBC驱动程序的注册:"+ this.driver.getClass().getName());
		}
	}
}