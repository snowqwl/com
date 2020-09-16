package com.easymap.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBConnectionPool {
	
	private int checkedOut;
	private List<Connection> freeConnections = new ArrayList<Connection>();
	private int maxConn;
	private int normalConn;
	private String password;
	private String url;
	private String user;
	private static int num = 0;
	private static int numActive = 0;

	public DBConnectionPool() {
		
	}

	public DBConnectionPool(String password, String url,String user, int normalConn, int maxConn) {
		this.password = password;
		this.url = url;
		this.user = user;
		this.maxConn = maxConn;
		this.normalConn = normalConn;
		for (int i = 0; i < normalConn; ++i) {
			Connection conn = newConnection();
			if (conn == null)
				continue;
			this.freeConnections.add(conn);
			num += 1;
		}
	}

	public synchronized void freeConnection(Connection paramConnection) {
		if (this.freeConnections.size() >= this.normalConn) {
			try {
				System.out.println("连接池中已经有5个连接，关闭当前的链接，当前连接池连接数"+ this.freeConnections.size());
				paramConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			this.freeConnections.add(paramConnection);
			num += 1;
			System.out.println("释放空闲连接，当前连接池连接数" + this.freeConnections.size());
		}
		this.checkedOut -= 1;
		numActive -= 1;
		super.notifyAll();
		System.out.println("当前连接数:" + numActive);
	}

	public synchronized Connection getConnection() {
		Connection conn = null;
		if (this.freeConnections.size() > 0) {
			num -= 1;
			conn = (Connection) this.freeConnections.get(0);
			this.freeConnections.remove(0);
			try {
				if (conn.isClosed()) {
					System.out.println("从连接池删除一个无效连接");
					conn = getConnection();
				}
			} catch (SQLException e) {
				System.out.println("从连接池删除一个无效连接");
				conn = getConnection();
			}
		} else if ((this.maxConn == 0) || (this.checkedOut < this.maxConn)) {
			conn = newConnection();
		}
		if (conn != null)
			System.out.println("当前连接数:" + ++this.checkedOut);
		numActive += 1;
		return conn;
	}

	public synchronized Connection getConnection(long paramLong) {
		long l = new Date().getTime();
		Connection conn;
		while ((conn = getConnection()) == null) {
			try {
				super.wait(paramLong);
			} catch (InterruptedException localInterruptedException) {
			}
			if (new Date().getTime() - l >= paramLong)
				return null;
		}
		return conn;
	}

	public synchronized void release() {
		System.out.println("关闭所有连接...");
		for(Connection conn:freeConnections){
			try {
				conn.close();
				freeConnections.remove(conn);
				num -= 1;
			} catch (SQLException e) {
				System.out.println("无法关闭连接池中的连接");
			}
		}
		numActive = 0;
	}

	private Connection newConnection() {
		Connection conn = null;
		try {
			if (this.user == null)
				conn = DriverManager.getConnection(this.url);
			else
				conn = DriverManager.getConnection(this.url,this.user, this.password);
			System.out.println("连接池创建一个新的连接");
		} catch (SQLException localSQLException) {
			System.out.println("无法创建这个URL的连接" + this.url);
			return null;
		}
		return conn;
	}

	public int getnum() {
		return num;
	}

	public int getnumActive() {
		return numActive;
	}

	public Connection getnewConnection() throws SQLException {
		Connection conn = null;
		try {
			DBInfoConfig fig=DBInfoConfig.getInstance();
			Class.forName(fig.getValue("DB.DRIVER"));
			conn = DriverManager.getConnection(fig.getValue("DB.URL"), fig.getValue("DB.USER"), fig.getValue("DB.PASSWORD"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}