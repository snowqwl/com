package com.sunshine.monitor.comm.dao.page;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分页SQL语句构建工具类
 * 
 */
public class PagingSqlBuilder {
	
	private static final Logger log = LoggerFactory.getLogger(PagingSqlBuilder.class);
	
	/* SQL STATEMENT include key word 'ORDER' */
	public static final String ORDER_STRING = "ORDER";

	/* SQL STATEMENT include descending order, key word 'DESC' */
	public static final String DESC_STRING = "DESC";

	/* SQL STATEMENT include key word 'ASC' */
	public static final String ASC_STRING = "ASC";
	
	private PagingSqlBuilder() {}

	/**
	 * 获得计算总记录数的SQL语句
	 * @param rawSql
	 * @param limit 0=不限制
	 * @return
	 */
	public static String getCountSql(String rawSql, int limit) {
		StringBuffer totalSQL = new StringBuffer("SELECT COUNT(1) FROM (");
		totalSQL.append(filterOrderbyStrOfSql(rawSql));
		if(limit>0){
			totalSQL.append(") where rownum <=");
			totalSQL.append(limit);
		} else {
			totalSQL.append(")");
		}
		log.debug(totalSQL.toString());
		return totalSQL.toString();
	}
	
	/**
	 * 获得计算分组总记录数的SQL语句
	 */
	public String getGroupCountSql(String rawSql, int limit) {
		String groupCountSql =  "SELECT SUM(RECORDS) FROM (" + getCountSql(rawSql,limit) + ") AS T";
		log.debug(groupCountSql);
		return groupCountSql;
	}
	
	/**
	 * 过滤ORDER BY
	 * @param sql
	 * @return
	 */
	public static String filterOrderbyStrOfSql(String sql){
		String upSql = sql.toUpperCase();
		int order = upSql.lastIndexOf(ORDER_STRING);
		int desc = upSql.lastIndexOf(DESC_STRING);
		int asc = upSql.lastIndexOf(ASC_STRING);
		int descOrasc = (desc != -1) ? desc:(asc!=-1) ? asc : -1;
		int add = (desc != -1) ? DESC_STRING.length():(asc!=-1) ? ASC_STRING.length() : -1;
		if(order != -1 && descOrasc != -1){
			StringBuffer result = new StringBuffer(sql);
			result.delete(order, descOrasc+add);
			return result.toString();
		}
		return sql;
	}
	
	/**
	 * 获得分页SQL语句
	 */
	public static String getPagingSql(String rawSql, PagingParameter paging) {
		if(paging == null || paging.isInvalid()) {
			log.debug(rawSql);
			return rawSql;
		}
		Map<String,Integer> map = paging.getStartAndEndRow();
		int start = map.get("start");
		int end = map.get("end");
		String pagingSql = "SELECT T.*, ROWNUM AS ROW_NUM FROM (" + rawSql + ") T WHERE ROWNUM <= " + end;
		if(start == 0) {
			log.debug(pagingSql);
			return pagingSql;
		}
		pagingSql = "SELECT * FROM (" + pagingSql + ") T_O WHERE ROW_NUM > " + start;
		log.debug(pagingSql);
		return pagingSql;
	}
	
	/**
	 * 获得分页SQL语句 不计总数
	 */
	public static String getPagingSqlWithoutTotal(String rawSql, PagingParameter paging) {
		if(paging == null || paging.isInvalid()) {
			log.debug(rawSql);
			return rawSql;
		}
		Map<String,Integer> map = paging.getStartAndEndRowWithoutTotal();
		int start = map.get("start");
		int end = map.get("end");
		String pagingSql = "SELECT T.*, ROWNUM AS ROW_NUM FROM (" + rawSql + ") T WHERE ROWNUM <= " + end;
		if(start == 0) {
			log.debug(pagingSql);
			return pagingSql;
		}
		pagingSql = "SELECT * FROM (" + pagingSql + ") T_O WHERE ROW_NUM > " + start;
		log.debug(pagingSql);
		return pagingSql;
	}
}
