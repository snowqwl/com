package com.sunshine.monitor.system.redlist.util;

/**
 * 
 * @author OYANG
 * @version V1.0.6
 * 
 */
public abstract class SqlUtil {

	/* SQL STATEMENT include key word 'ORDER' */
	public static final String ORDER_STRING = "ORDER";

	/* SQL STATEMENT include descending order, key word 'DESC' */
	public static final String DESC_STRING = "DESC";

	/* SQL STATEMENT include key word 'ASC' */
	public static final String ASC_STRING = "ASC";

	/**
	 * 过滤SQL中的排序
	 * 
	 * @param sql
	 * @return
	 */
	public static String filterOrderbyStrOfSql(String sql) {
		if (sql == null)
			throw new IllegalArgumentException("SQL参数为空！");
		String upSql = sql.toUpperCase();
		int order = upSql.indexOf(ORDER_STRING);
		int desc = upSql.indexOf(DESC_STRING);
		int asc = upSql.indexOf(ASC_STRING);
		int descOrasc = (desc != -1) ? desc : (asc != -1) ? asc : -1;
		int add = (desc != -1) ? DESC_STRING.length()
				: (asc != -1) ? ASC_STRING.length() : -1;
		if (order != -1 && descOrasc != -1) {
			StringBuffer result = new StringBuffer(sql);
			result.delete(order, descOrasc + add);
			return result.toString();
		}
		return sql;
	}

}
