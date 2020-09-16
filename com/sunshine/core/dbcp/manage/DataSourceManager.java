package com.sunshine.core.dbcp.manage;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * Manager DataBase Connection Pool
 * @author YANGYANG
 *
 */
public class DataSourceManager {
	
	private BasicDataSource dataSource ;

	public BasicDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int getMaxActive() {
		return dataSource.getMaxActive();
	}

	public int getMaxIdle() {
		return dataSource.getMaxIdle();
	}

	public long getMaxWait() {
		return dataSource.getMaxWait();
	}

	public int getMinIdle() {
		return dataSource.getMinIdle();
	}

	public int getNumActive() {
		return dataSource.getNumActive();
	}

	public int getNumIdle() {
		return dataSource.getNumIdle();
	}

	public void setMaxActive(int maxActive) {
		dataSource.setMaxActive(maxActive);
	}

	public void setMaxIdle(int maxIdle) {
		dataSource.setMaxIdle(maxIdle);
	}

	public void setMaxWait(long maxWait) {
		dataSource.setMaxWait(maxWait);
	}

	public void setMinIdle(int minIdle) {
		dataSource.setMinIdle(minIdle);
	}
}
