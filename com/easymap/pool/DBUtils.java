package com.easymap.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import sun.tools.tree.ThisExpression;

public class DBUtils {
	
	private static Connection conn = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	
	public static Connection getConnection(){
			try {
				if(conn==null||conn.isClosed()){
					conn = DBConnection.getJDBCConnection();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return conn; 
	}
	
	public static ResultSet findResultData(String sql){
		try {

			System.out.println("SQL:"+sql);
			 rs = findPrepareStatement(sql).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return rs;
	}
	public static ResultSet findResultData(String sql,List li){
		try {

			System.out.println("SQL:"+sql);
			PreparedStatement ps=findPrepareStatement(sql);
			setDataT(li, ps);
			 rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return rs;
	}
	public static void setDataT(List li,PreparedStatement pre){
		int i=0;
		for (Object object : li) {
			i++;
			String v= String.valueOf(object);
			try {
				pre.setString(i,v);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static PreparedStatement findPrepareStatement(String sql){
		try {
			ps = getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return ps;
	}
	
	public static void close(){
		try {
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e5) {
			e5.printStackTrace();
		}
	}

}
