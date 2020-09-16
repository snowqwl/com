package com.sunshine.monitor.comm.dao.jdbc;

import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.DBaseDao;
import com.sunshine.monitor.comm.dao.exception.DaoAccessException;
import com.sunshine.monitor.comm.util.orm.SqlUtils;
import com.sunshine.monitor.comm.util.orm.bean.PreSqlEntry;
import com.sunshine.monitor.comm.util.orm.exception.FormatSqlException;
import com.sunshine.monitor.comm.util.orm.exception.InitTableInfoException;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.Pager;

@Repository
public abstract class DBaseDaoImpl<E> implements DBaseDao<E> {
	@Autowired
	protected JdbcTemplate jdbcTemplate ;
	
	
	public E save(E entity) throws Exception {
		this.save(entity, new HashMap<String, String>());
		return entity;
	}
	
	
	public E save(E entity, Map<String, String> userDefSql) throws DaoAccessException  {
		PreSqlEntry sqlEntry;
		try {
			sqlEntry = SqlUtils.getPreInsertSqlByObject ( getTableName(), entity, userDefSql);
			this.jdbcTemplate.update(sqlEntry.getSql(),sqlEntry.getValues().toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoAccessException(e);
		}
		return entity;
	}
	
	
	public E update(E entity,String keyName) throws DaoAccessException {
		try {
			return this.upate(entity, new HashMap<String,String>(), keyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoAccessException(e);
		}
	};

	
	public E upate(E entity, Map<String, String> userDefSql,
			String keyName) throws DaoAccessException {
		try {
			PreSqlEntry sqlEntry = SqlUtils.getPreUpdateSqlByObject ( getTableName(), entity, userDefSql,keyName);
			this.jdbcTemplate.update(sqlEntry.getSql(),sqlEntry.getValues().toArray());
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoAccessException(e);
		}
	}
	
	
	public List<E> queryForList(Class<E> clazz , Cnd cnd) 
			throws DaoAccessException  {
		try {
			PreSqlEntry  entry = SqlUtils.getPreSelectSql(getTableName(),null, cnd);
			return this.jdbcTemplate.query(entry.getSql(), entry.getValues().toArray(), new JdbcAutoRowMapper<E>(clazz));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoAccessException(e);
		}
	}
	
	
	public E queryForBean(Class<E> clazz , Cnd cnd) throws DaoAccessException {
		try {
			List<E> result = queryForList(clazz , cnd);
			if(result!=null && result.size() > 0) return result.get(0);
			else return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoAccessException(e);
		}
	}
	
	
	public int queryForInt(String columnName , Cnd cnd) throws DaoAccessException {
		try {
			PreSqlEntry  entry = SqlUtils.getPreSelectSql(getTableName(), columnName , cnd);
			return this.jdbcTemplate.queryForInt(entry.getSql(), entry.getValues().toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoAccessException(e);
		}
	}
	
	
	public int getTotal(Cnd cnd) throws DaoAccessException {
		try{
			PreSqlEntry  entry = SqlUtils.getPreSelectSql(getTableName(), "count(*)" , cnd);
			return this.jdbcTemplate.queryForInt(entry.getSql(), entry.getValues().toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoAccessException(e);
		}
	}
	
	
	public List<E> queryForCondition(Class<E> clazz ,Pager pager, Cnd cnd) throws InitTableInfoException, FormatSqlException {
		PreSqlEntry  entry = SqlUtils.getPreSelectSql(getTableName(),null,cnd);
		SqlUtils.getPreSelectSqlByPage(entry, pager);
		return this.jdbcTemplate.query(entry.getSql(), entry.getValues().toArray(), new JdbcAutoRowMapper<E>(clazz));
	}
	
	public abstract String getTableName() ;
	
	
	/**
	 * 按照JDBC返回类型反射到实体类中，如果映射失败忽略错误继续映射下一个字段
	 * @author lifenghu
	 *
	 * @param <T> 映射实体
	 */
	public static class JdbcAutoRowMapper<T> implements RowMapper<T> {

		private Class<T> requiredType;

		private Class<?>[] LongType = { Long.class };

		private Class<?>[] StringType = { String.class };

		private Class<?>[] DoubleType = { Double.class };

		private Class<?>[] BlobType = { Blob.class };

		private Class<?>[] IntType = { int.class };

		public JdbcAutoRowMapper(Class<T> requiredType) {
			this.requiredType = requiredType;
		}

		/**
		 * 根据数据库表列名拼接SET、GET方法,找到并调用实体类方法
		 */
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			T objIns = null;
			try {
				Class<?> classObject = null;
				objIns = this.requiredType.newInstance();
				classObject = objIns.getClass();
				ResultSetMetaData rsmd = rs.getMetaData();

				Object colValue = null;
				Class<?>[] type = (Class[]) null;
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					try{
					String colName = FormatColName(rsmd.getColumnName(i));
					String colType = rsmd.getColumnTypeName(i);
					if (!colName.equals("Num")) {
						if ((colType.equals("CHAR"))
								|| (colType.equals("VARCHAR"))
								|| (colType.equals("VARCHAR2"))) {
							colValue = rs.getString(colName);
							type = StringType;
							if (colValue == null)
								colValue = "";
						} else if (colType.equals("DATE")) {
							Timestamp timestamp = rs.getTimestamp(colName);
							if (timestamp == null) continue ; 
							colValue = new Date(timestamp.getTime());
							type = new Class[]{Date.class};
						} else if (colType.equals("NUMBER")) {
							if (rsmd.getScale(i) <= 0) {
								if (rsmd.getPrecision(i) <= 0) {
									if (rs.getString(colName) != null)
										colValue = new Double(rs
												.getDouble(colName));
									else {
										colValue = null;
									}
									type = DoubleType;
								} else {
									if (rs.getString(colName) != null)
										colValue = new Long(rs.getLong(colName));
									else {
										colValue = null;
									}
									type = LongType;
								}
							} else {
								if (rs.getString(colName) != null)
									colValue = new Double(rs.getDouble(colName));
								else {
									colValue = null;
								}
								type = DoubleType;
							}
						} else if (colType.equals("BLOB")) {
							colValue = rs.getBlob(colName);
							type = BlobType;
						} else if (colType.equals("INTEGER")) {
							colValue = rs.getInt(colName);
							type = IntType;
						}
						Object[] values = { colValue };
						Method meth = classObject.getMethod("set" + colName,
								type);
						meth.invoke(objIns, values);
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return objIns;
		}

		private String FormatColName(String colName) {
			return colName.substring(0, 1).toUpperCase()
					+ colName.substring(1).toLowerCase();
		}

	}

}
