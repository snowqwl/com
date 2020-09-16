package com.easymap.pool;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easymap.listeners.InitListener;

public class DBConnection {
	private static Logger logger = LoggerFactory.getLogger("Connection");

	public static Connection getJDBCConnection() throws Exception {
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.43.129.158:1521:ORCL", "jcz","jcz");
		return conn;
		
	}

	public static Connection getGPSConnection() throws Exception {
		InitialContext ic = new InitialContext();
		if (ic == null)
			throw new Exception("Context is null.");
		String jd = InitListener.configProperties.getProperty("datasource");
		DataSource ds = (DataSource) ic.lookup(jd);
		if (ds == null)
			throw new Exception("DataSource is null.");
		Connection conn = ds.getConnection();
		if (conn.isClosed())
			throw new Exception("Jndi Connection is closed.");
		if (logger.isInfoEnabled())
			logger.info("Jndi Connection Succeed!");
		return conn;
	}
}