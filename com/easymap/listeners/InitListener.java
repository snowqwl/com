package com.easymap.listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//import org.apache.log4j.PropertyConfigurator;

import com.easymap.pool.SqlProperties;


public class InitListener implements ServletContextListener {
	public static SqlProperties sqlProperties = null;
	public static Properties configProperties = null;
	public static MapServiceConfig mapServiceConfig = null;

	public void contextInitialized(ServletContextEvent paramServletContextEvent) {
		InputStream input = paramServletContextEvent.getServletContext().getResourceAsStream("/WEB-INF/GpsServerConfig.properties");
		try {
			//sqlProperties = new SqlProperties();
			//sqlProperties.loadConfig(paramServletContextEvent.getServletContext().getRealPath("/WEB-INF/config/sql.properties"));
			configProperties = new Properties();
			configProperties.load(input);
//			Properties localProperties = new Properties();
//			localProperties.load(paramServletContextEvent.getServletContext().getResourceAsStream("/WEB-INF/config/log4j.properties"));
//			PropertyConfigurator.configure(localProperties);
//			mapServiceConfig = new MapServiceConfig();
//			mapServiceConfig.loadConfig(paramServletContextEvent.getServletContext().getResourceAsStream("/WEB-INF/MapServiceConfig.xml"));
//			InitTools localInitTools = new InitTools();
//			localInitTools.init();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException localIOException4) {
				localIOException4.printStackTrace();
			}
		}
	}

	public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
	}
}

/*
 * Location:
 * C:\Users\Administrator\Desktop\gps产品\gpsServer.ear\gpsServer.war\WEB
 * -INF\lib\gpsServer.V2.0.5.201108301200\ Qualified Name:
 * com.easymap.listeners.InitListener JD-Core Version: 0.5.4
 */