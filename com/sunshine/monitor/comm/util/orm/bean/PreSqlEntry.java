package com.sunshine.monitor.comm.util.orm.bean;

import java.util.List;


public class PreSqlEntry {
	private String sql ;
	private List<Object> values;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<Object> getValues() {
		return values;
	}
	public void setValues(List<Object> values) {
		this.values = values;
	}
}
