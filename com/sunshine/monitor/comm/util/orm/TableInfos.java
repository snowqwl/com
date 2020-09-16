package com.sunshine.monitor.comm.util.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.comm.util.orm.exception.InitTableInfoException;

public class TableInfos {
	private static final Map<String,Map<String,Integer>> tableInfo = new HashMap<String,Map<String,Integer>>();
	private static JdbcTemplate jdbcTemplate;
	public static final TableInfos INSTANS = new TableInfos();
	
	private TableInfos(){}
	
	
	public Map<String, Integer> get(String tableName) throws InitTableInfoException {
		Map<String, Integer> columsInfo = tableInfo.get(tableName.toLowerCase());
		if(columsInfo == null){
			try {
				columsInfo = findForDB(tableName);
			} catch (SQLException e) {
				throw new InitTableInfoException(e);
			}
		}
		if(columsInfo == null) throw new InitTableInfoException("找不到表名为："+tableName+"的信息");
		return columsInfo;
	}

	private Map<String, Integer> findForDB(String tableNamePattern) throws SQLException {
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		tableNamePattern = tableNamePattern.toUpperCase();
		Statement statm = conn.createStatement();
		ResultSet rst = statm.executeQuery("select * from "+tableNamePattern+" where 1=2");
		ResultSetMetaData rsm = rst.getMetaData();
		int count = rsm.getColumnCount();
		Map<String,Integer> columnTypeMap = new HashMap<String,Integer>();
		for(int i=1;i<count+1;i++){
			String columnName = rsm.getColumnLabel(i).toLowerCase();
			Integer dataType = rsm.getColumnType(i);
			columnTypeMap.put(columnName, dataType);
		}
		
		tableInfo.put(tableNamePattern.toLowerCase(), columnTypeMap);
		rst.close();
		conn.close();
		return columnTypeMap;
	}

	private JdbcTemplate getJdbcTemplate() {
		if(this.jdbcTemplate == null){
			this.jdbcTemplate = SpringApplicationContext.getStaticApplicationContext().getBean("jdbcTemplate",JdbcTemplate.class);
		}
		return this.jdbcTemplate;
	}
}
