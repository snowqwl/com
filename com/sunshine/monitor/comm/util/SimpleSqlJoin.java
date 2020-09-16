package com.sunshine.monitor.comm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL拼接代码
 * @author OUYANG
 */
public class SimpleSqlJoin {

	public String strSql = "%s";

	public Object fsql = "";

	public String type = "";

	public Object[] v = null;

	public List<SimpleSqlJoin> list = null;

	public SimpleSqlJoin() {

	}

	public SimpleSqlJoin(Object fsql, String type, Object[] v) {
		this.fsql = fsql;
		this.type = type;
		this.v = v;
	}

	public SimpleSqlJoin(String strSql, Object fsql, String type, Object[] v) {
		this.strSql = strSql;
		this.fsql = fsql;
		this.type = type;
		this.v = v;
	}

	/**
	 * 获取值列表
	 * 
	 * @return
	 */
	public Object[] getArgs() {
		List<Object> valslist = new ArrayList<Object>();
		setVals(valslist);
		return valslist.toArray();
	}

	/**
	 * 
	 * @param valslist
	 */
	public void setVals(List<Object> valslist) {
		if (v != null) {
			for (int j = 0; j < v.length; j++) {
				if (v[j] != null) {
					valslist.add(v[j]);
				}
			}
		}
		if (fsql != null && fsql instanceof SimpleSqlJoin)
			((SimpleSqlJoin) fsql).setVals(valslist);
		if (list != null) {
			for (int j = 0; j < list.size(); j++) {
				SimpleSqlJoin sqlHandle = (SimpleSqlJoin) list.get(j);
				sqlHandle.setVals(valslist);
			}
		}
	}

	private static String addWhereSql(String strSql, Object sqlOne, String type) {
		return " " + type + " " + String.format(strSql, sqlOne) + " ";
	}

	public SimpleSqlJoin AddMore(Object fsql, String type, Object[] v) {
		if (list == null)
			list = new ArrayList<SimpleSqlJoin>();
		SimpleSqlJoin sql = new SimpleSqlJoin(fsql, type, v);
		list.add(sql);
		return this;
	}

	public SimpleSqlJoin Add(Object fsql, String type, Object v) {
		AddMore(fsql, type, new Object[] { v });
		return this;
	}

	public SimpleSqlJoin AddMoreAndRep(String strSql, Object fsql, String type,
			Object[] v) {
		if (list == null)
			list = new ArrayList<SimpleSqlJoin>();
		SimpleSqlJoin sql = new SimpleSqlJoin(strSql, fsql, type, v);
		list.add(sql);
		return this;
	}

	public SimpleSqlJoin AddAndRep(String strSql, Object fsql, String type, Object v) {
		AddMoreAndRep(strSql, fsql, type, new Object[] { v });
		return this;
	}

	public String toString() {
		String sql = "";
		for (int i = 0; i < list.size(); i++) {
			SimpleSqlJoin sqlHandle = (SimpleSqlJoin) list.get(i);
			sql += addWhereSql(sqlHandle.strSql, sqlHandle.fsql, sqlHandle.type);
		}
		return sql;
	}
}