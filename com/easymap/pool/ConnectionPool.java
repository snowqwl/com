package com.easymap.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ConnectionPool {
	
	
	private int initSize = 5;
	private int maxConnections = -1;
	private int capacityIncrement = 5;
	private int activeNum = 0;
	private int freeNum = 0;
	private String user;
	private String passWord;
	private String url;
	private List<Connection> pools;

	public ConnectionPool(String user, String passWord,String url) {
		this.user = user;
		this.passWord = passWord;
		this.url = url;
	}

	
	public boolean initialize() {
		this.pools = new ArrayList<Connection>();
		for (int i = 0; i < this.initSize; ++i) {
			Connection conn = createConnection(this.user,this.passWord, this.url);
			if (conn == null)
				continue;
			this.pools.add(conn);
			this.activeNum += 1;
			this.freeNum += 1;
		}
		return true;
	}

	public synchronized Connection getConnection() throws SQLException {
		Connection conn = null;
		if ((this.freeNum == 0) && (this.maxConnections != -1)&& (this.activeNum == this.maxConnections))
			throw new SQLException("连接池中没有空闲连接,且当前链接已经达到最大限制!");
		if ((this.freeNum == 0)&& (((this.maxConnections == -1) || (this.activeNum < this.maxConnections)))) {
			for (int i = 0; i < this.capacityIncrement; ++i) {
				Connection conn2 = createConnection(this.user,this.passWord, this.url);
				if (conn2 != null) {
					this.pools.add(conn2);
					this.activeNum += 1;
					this.freeNum += 1;
				}
				if (this.activeNum == this.maxConnections)
					break;
			}
			conn = this.pools.get(0);
			this.pools.remove(0);
			this.freeNum -= 1;
			try {
				if (conn.isClosed()) {
					System.out.println("删除一个无效的链接");
					this.activeNum -= 1;
					getConnection();
				}
			} catch (SQLException e) {
				System.out.println("删除一个无效的链接");
				this.activeNum -= 1;
				getConnection();
			}
			return conn;
		}
		if ((this.freeNum > 0)&& (((this.maxConnections == -1) || (this.activeNum- this.freeNum < this.maxConnections)))) {
			conn = this.pools.get(0);
			this.pools.remove(0);
			this.freeNum -= 1;
			try {
				if (conn.isClosed()) {
					System.out.println("删除一个无效的链接");
					this.activeNum -= 1;
					getConnection();
				}
			} catch (SQLException e) {
				System.out.println("删除一个无效的链接");
				this.activeNum -= 1;
				getConnection();
			}
			return conn;
		}
		if ((this.freeNum > 0)&& (this.activeNum - this.freeNum >= this.maxConnections))
			throw new SQLException("当前正在使用的链接已经达到最大限制!");
		return null;
	}

	public synchronized void freeConnection(Connection conn) {
		try {
			if ((!conn.isClosed()) && (this.freeNum < this.initSize)) {
				this.pools.add(conn);
				System.out.println("释放连接，当前连接池中连接数:" + this.activeNum+ "当前空闲连接数:" + ++this.freeNum);
			} else {
				conn.close();
				this.activeNum -= 1;
			}
			super.notifyAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Connection createConnection(String username,String password, String url) {
		Connection conn = null;
		try {
			if ((username == null) || (password == null))
				conn = DriverManager.getConnection(url);
			else
				conn = DriverManager.getConnection(url,username, password);
			System.out.println("连接池创建一个新的连接");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("无法创建这个URL的连接" + url);
		}
		return conn;
	}
	
	
	public void destroy() {
		System.out.println("关闭所有连接...");
		for(Connection conn:pools){
			try {
				conn.close();
				this.activeNum -= 1;
				this.freeNum -= 1;
			} catch (SQLException localSQLException) {
				System.out.println("无法关闭连接池中的连接");
			}
		}
		this.pools = null;
	}
	

	public int getMaxConnections() {
		return this.maxConnections;
	}

	public void setMaxConnections(int paramInt) {
		this.maxConnections = paramInt;
	}

	public int getInitSize() {
		return this.initSize;
	}

	public void setInitSize(int paramInt) {
		this.initSize = paramInt;
	}

	public int getCapacityIncrement() {
		return this.capacityIncrement;
	}

	public void setCapacityIncrement(int paramInt) {
		this.capacityIncrement = paramInt;
	}

	public String getPassWord() {
		return this.passWord;
	}

	public void setPassWord(String paramString) {
		this.passWord = paramString;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String paramString) {
		this.url = paramString;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String paramString) {
		this.user = paramString;
	}

	public int getActiveNum() {
		return this.activeNum;
	}

	public int getFreeNum() {
		return this.freeNum;
	}
}