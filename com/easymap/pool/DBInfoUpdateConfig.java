package com.easymap.pool;

import com.easymap.listeners.InitListener;

public class DBInfoUpdateConfig {
	
	private static final DBInfoUpdateConfig cfg = new DBInfoUpdateConfig();

	public static DBInfoUpdateConfig getInstance() {
		return cfg;
	}

	public String getValue(String paramString) {
		return InitListener.configProperties.getProperty(paramString).trim();
	}
}
