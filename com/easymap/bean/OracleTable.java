package com.easymap.bean;

import java.sql.Timestamp;

public class OracleTable {

	private String tableName;
	private Timestamp paramFromTime;
	private Timestamp paramToTime;

	public Timestamp getParamFromTime() {
		return this.paramFromTime;
	}

	public void setParamFromTime(Timestamp paramTimestamp) {
		this.paramFromTime = paramTimestamp;
	}

	public Timestamp getParamToTime() {
		return this.paramToTime;
	}

	public void setParamToTime(Timestamp paramTimestamp) {
		this.paramToTime = paramTimestamp;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String paramString) {
		this.tableName = paramString;
	}
}