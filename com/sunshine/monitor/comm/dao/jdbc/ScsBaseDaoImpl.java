package com.sunshine.monitor.comm.dao.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.comm.dao.annotation.Column;
import com.sunshine.monitor.comm.dao.annotation.DIC;
import com.sunshine.monitor.comm.dao.annotation.GetName;
import com.sunshine.monitor.comm.dao.exception.DaoAccessException;
import com.sunshine.monitor.comm.dao.util.SqlParser;
import com.sunshine.monitor.comm.util.ReflectUtils;
import com.sunshine.monitor.comm.util.orm.SqlUtils;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.OrderByItem;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.system.manager.dao.SystemDao;

@Component("scsBaseDao")
public class ScsBaseDaoImpl implements ScsBaseDao{

	@Autowired
	@Qualifier("jdbcScsTemplate")
	protected JdbcTemplate jdbcScsTemplate;
	
	@Autowired
	@Qualifier("systemDao")
	protected SystemDao systemDao;
	
	private static String t_pass_s = "veh_passrec_s"; // 过车视图，基于列式存储
	
	private static String t_pass = "veh_passrec";  // 过车视图，基于行式存储
	
	protected Logger log = LoggerFactory.getLogger(ScsBaseDaoImpl.class);
	
	public final SqlParser sqlParser = new SqlParser();
	
	@Override
	public int getTotal(String sql) throws Exception {
		int total = 0;
		try {
			String csql = sqlParser.getSmartCountSql(sql);
			long start = System.currentTimeMillis();
			total = this.jdbcScsTemplate.queryForObject(csql, Integer.class);
			long end = System.currentTimeMillis();
			log.info("sqlCount-(" + (end-start) + "毫秒)" +csql);
		} catch (Exception e) {
			e.printStackTrace();
			total = 0;
		}
		return total;
	}

	@Override
	public int getTotal(String sql, Object... params) throws Exception {
		int total = 0;
		try {
			String csql = sqlParser.getSmartCountSql(sql);
			long start = System.currentTimeMillis();
			total = this.jdbcScsTemplate.queryForObject(csql, params, Integer.class);
			long end = System.currentTimeMillis();
			log.info("sqlCount-(" + (end-start) + "毫秒)" +csql);
		} catch (Exception e) {
			e.printStackTrace();
			total = 0;
		}
		return total;
	}

	@Override
	public List<Map<String, Object>> getPageDatas(String sql, int curPage,
			int pageSize) throws Exception {
		StringBuffer sb = new StringBuffer();
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ? offset ?");
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.jdbcScsTemplate.queryForList(sb.toString(),
				new Object[]{pageSize,start});
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		return list;
	}
	
	@Override
	public <T> List<T> getPageDatas(String sql, int curPage, int pageSize,
			Class<T> clazz) throws Exception {
		StringBuffer sb = new StringBuffer();
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ? offset ?");
		long pstart = System.currentTimeMillis();
		//List<Map<String, Object>> list = this.jdbcScsTemplate.queryForList(sb.toString());
		List<T> list = this.jdbcScsTemplate.query(sb.toString(),
				new BeanPropertyRowMapper<T>(clazz),new Object[]{pageSize,start});
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getPageDatas(String sql, int curPage,
			int pageSize, Object... params) throws Exception {
		StringBuffer sb = new StringBuffer();
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ").append(pageSize).append(" offset ").append(start);
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.jdbcScsTemplate.queryForList(sb.toString(), params);
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		return list;
	}

	@Override
	public <T> List<T> getPageDatas(String sql, int curPage, int pageSize,
			Class<T> clazz, Object... params) throws Exception {
		StringBuffer sb = new StringBuffer();
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ").append(pageSize).append(" offset ").append(start);
		long pstart = System.currentTimeMillis();
		List<T> list = this.jdbcScsTemplate.queryForList(sb.toString(), params, clazz);
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		return list;
	}

	public Map<String, Object> findPageForMap(String sql, int curPage,
			int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		int total = getCount(sql);
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ").append(pageSize).append(" offset ").append(start);
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.jdbcScsTemplate.queryForList(sb
				.toString());
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		// 设置总共有多少条记录
		map.put("total", total);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}
	public Map<String, Object> findPageForMap(String sql, Object[] array,int curPage,
											  int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		int total = getCount(sql);
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ").append(pageSize).append(" offset ").append(start);
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.jdbcScsTemplate.queryForList(sb
				.toString(),array);
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		// 设置总共有多少条记录
		map.put("total", total);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}

	public <T> Map<String, Object> findPageForMap(String sql, int curPage,
			int pageSize, Class<T> clazz) throws Exception {
		int total = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		total =  getCount(sql);
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ").append(pageSize).append(" offset ").append(start);
		long pstart = System.currentTimeMillis();
		List<T> list = this.queryForList(sb.toString(), clazz);
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		// 设置总共有多少条记录
		map.put("total", total);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}

	public <T> List<T> queryForList(String sql, Class<T> classz)
			throws Exception {
		//return this.jdbcScsTemplate.query(sql, new JcbkScsRowMapper<T>(classz));/封装的方法有问题 已修改 oyll
		return this.jdbcScsTemplate.query(sql, new BeanPropertyRowMapper<T>(classz));
	}

	public <T> int queryForInt(String sql) throws Exception {
		int result = 0;
		Object o = this.jdbcScsTemplate.queryForObject(sql, Object.class);
		result = Integer.parseInt(o.toString());
		return result;
	}

	/**
	 * 获取统计sql语句
	 * 
	 * @param sql
	 * @return
	 */
	private int getCount(String sql) {
		/*StringBuffer sb = new StringBuffer();
		int opos = sql.toUpperCase().indexOf("ORDER");
		int rpos = sql.toUpperCase().indexOf("OVER(PARTITION)");
		if (opos != -1) {
			sql = sql.substring(0, opos);
		}
		int pos = sql.toUpperCase().indexOf("FROM");

		if (pos != -1) {
			String from = sql.substring(pos, sql.length());
			sb.append("select  count(1) from (").append(sql).append(") t ");
			String str = sb.toString();
			if (str.toUpperCase().indexOf("GROUP") == -1) {
				total = Integer.parseInt(this.jdbcScsTemplate.queryForObject(
						str.toString(), Object.class).toString());
			} else {
				//List<Map<String,Object>> list = this.jdbcScsTemplate.queryForList(sql);
				try {
					total = this.jdbcScsTemplate.queryForInt(str);
				} catch (Exception e) {
					total = 0;
					e.printStackTrace();
				}
			}
		}*/
		/**
		 * 优化Count查询,去掉Order by
		 * @author oy 2016/08/01
		 */
		int total = 0;
		try {
			String csql = sqlParser.getSmartCountSql(sql);
			long start = System.currentTimeMillis();
			total = this.jdbcScsTemplate.queryForObject(csql, Integer.class);
			long end = System.currentTimeMillis();
			log.info("sqlCount-(" + (end-start) + "毫秒)" +csql);
		} catch (Exception e) {
			e.printStackTrace();
			total = 0;
		}
		return total;
	}
	
	public <T> int queryForInt(String preSql, Object[] values) throws Exception {
		int result = 0;
		Object o = this.jdbcScsTemplate.queryForObject(preSql,values,Object.class);
		result = Integer.parseInt(o.toString());
		return result;
	}
	
	public <T> T quertForObject(String preSql,Class<T> classz) throws Exception {
		return this.jdbcScsTemplate.queryForObject(preSql,classz);
	}
	
	/**
	 * 相当于  List&lt;Entity&gt; <br />
	 * Map key 为大写字母
	 */
	public List<Map<String,Object>>  findList(String sql){
		SqlRowSet dataSet = jdbcScsTemplate.queryForRowSet(sql);
		List<Map<String,Object>> entityList = new ArrayList<Map<String,Object>>();
		SqlRowSetMetaData metaData = dataSet.getMetaData();
		int count = metaData.getColumnCount();
		List<String> columns = new ArrayList<String>();
		for(int i=1 ; i<=count ; i++){
			columns.add(metaData.getColumnLabel(i));
		}
		while(dataSet.next()){
			Map<String,Object> entity = new HashMap<String,Object>();
			for(String column : columns){
				entity.put(column.toUpperCase(), dataSet.getObject(column));
			}
			entityList.add(entity);
		}
		return entityList ;
	}
	
	public int createTemp(String sql,String tablename) throws Exception {
		this.jdbcScsTemplate.update("DROP TABLE IF EXISTS ?",tablename);

		StringBuffer sb = new StringBuffer(" create table ");
		sb.append(tablename).append("").append(sql);
		return this.jdbcScsTemplate.update(sb.toString());
	}

	public int dropTable(String tablename) throws Exception {
		return this.jdbcScsTemplate.update("DROP TABLE IF EXISTS ?",tablename);
	}
	
	public <T> List<T> query(String tableName,Class<T> clazz, Cnd cnd, Pager pager)
			throws Exception {
		String selectData = "select t.*";
		String selectTotal = "select count(t.*) as total ";
		String sql = " from t(%1$s::%2$s)";
		String selectSql = String.format(sql, tableName, cnd.getConditions().toSql());
		String totalStr = this.jdbcScsTemplate.queryForObject(selectTotal+selectSql,String.class, cnd.getConditions().getValues().toArray());
		Integer total = Integer.parseInt(totalStr);
		pager.setPageCount(total);
		StringBuilder sqlBuilder = new StringBuilder(selectSql);
		if(cnd.getOrders().getObis().size()>0){
			sqlBuilder.append(" order ");
			for(OrderByItem item : cnd.getOrders().getObis()){
				sqlBuilder.append(item.toSql()).append(",");
			}
			sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
		}
		if(pager!=null){
			sqlBuilder.append(" limit ").
					append(pager.getStartRow()-1).append(",").append(pager.getPageSize());
		}
		String finalSelectSql = selectData+sqlBuilder.toString();
		return this.jdbcScsTemplate.query(finalSelectSql, 
				cnd.getConditions().getValues().toArray(), new JcbkScsRowMapper<T>(clazz));
	}
	/**
	 * 类中字段有{@link com.sunshine.monitor.comm.dao.annotation.Column Column}
	 * 的字段才创建
	 */
	public void createTable(Class<?> clazz, String tableName) {
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sqlBuilder = new StringBuilder( "create table IF NOT EXISTS "+tableName+"(");
		for(Field field : fields){
			Column c = field.getAnnotation(Column.class);
			if(c == null) continue;
			sqlBuilder.append(field.getName()).append(" ");
			if(String.class.isAssignableFrom(field.getType())){
				int size = 255;
				if(c!=null) size = c.size();
				sqlBuilder.append("varchar("+size+")");
			} else if(Integer.class.isAssignableFrom(field.getType())) {
				sqlBuilder.append("int");
			} else if(Float.class.isAssignableFrom(field.getType())) {
				sqlBuilder.append("float");
			}else if(Date.class.isAssignableFrom(field.getType())) {
				sqlBuilder.append("date");
			}else{
				throw new RuntimeException("no support dataType !!"+field.getType());
			}
			sqlBuilder.append(",");
		}
		sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
		sqlBuilder.append(" ) ");
		this.jdbcScsTemplate.update(sqlBuilder.toString());
	}

	public int update(String sql) {
		return this.jdbcScsTemplate.update(sql);
	}
	
	/**
	 * 按照JDBC返回类型反射到实体类中，如果映射失败忽略错误继续映射下一个字段
	 * 
	 * @author maijial
	 * 
	 * @param <T>
	 *            映射实体
	 */
	public  class JcbkScsRowMapper<T> implements RowMapper<T> {

		private Class<T> requiredType;

		private Class<?>[] LongType = { Long.class };

		private Class<?>[] StringType = { String.class };

		private Class<?>[] DoubleType = { Double.class };

		private Class<?>[] BlobType = { Blob.class };

		private Class<?>[] IntType = { int.class };

		public JcbkScsRowMapper(Class<T> requiredType) {
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
				Field[] fields= objIns.getClass().getDeclaredFields();
				Map<String,Field> map = new HashMap<String,Field>();
				for(Field f:fields){
					map.put(f.getName().toLowerCase(),f);
				}
				ResultSetMetaData rsmd = rs.getMetaData();

				Object colValue = null;
				Class<?>[] type = (Class[]) null;
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					try {
						String colName = rsmd.getColumnName(i);
						String colType = rsmd.getColumnTypeName(i);
						
						if (!colName.equalsIgnoreCase("SCSHASHVAl")) {
							Field field = map.get(colName.toLowerCase());
							if(field == null) continue;
							if ((colType.equals("String"))) {
								try {
									colValue = rs.getString(colName);
								} catch (NullPointerException ex) {
									colValue = "";
								}
								type = StringType;
								if (colValue == null)
									colValue = "";
							}
							Object[] values = { colValue };
							Method meth = classObject.getMethod("set"
									+ StringUtils.capitalize(field.getName()), type);
							meth.invoke(objIns, values);
							
							DIC dic = field.getAnnotation(DIC.class);
							//翻译
							if(dic != null){
								String codeValue = systemDao.getCodeValue(dic.dicType(), colValue.toString());
								Method dicFieldSet = classObject.getMethod("set"
										+ StringUtils.capitalize(dic.dicFieldName()), String.class);
								dicFieldSet.invoke(objIns, codeValue);
							}
							
							GetName getName = field.getAnnotation(GetName.class);
							//翻译名称　
							if(getName != null){
								String nameValue = null;
								try{
									nameValue = systemDao.getField(getName.tableName(), getName.pk(), getName.name(), colValue).toString();
								}catch(Exception e){
									e.printStackTrace();
									//忽略异常,允许找不到等情况
								}
								if(nameValue == null) nameValue = colValue.toString();
								Method dicFieldSet = classObject.getMethod("set"
										+ StringUtils.capitalize(getName.nameFieldName()), String.class);
								dicFieldSet.invoke(objIns, nameValue);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return objIns;
		}

	}

	public <T> int save(final T[] entity,String tableName) throws DaoAccessException  {
		try {
			Object[] objs = SqlUtils.getPreInsertSql(tableName, entity.getClass(), null);
			String sql = (String)objs[0];
			final String[] fields = (String[])objs[1];
			BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter(){
				public int getBatchSize() {
					return fields.length;
				}
				public void setValues(
						PreparedStatement paramPreparedStatement,int i)
								throws SQLException {
						Object value = null;
						try {
							value = ReflectUtils.getValue(entity[i], fields[i]);
						} catch (Exception e) {
							log.error("批量插入反射获取数据出错",e);
							throw new RuntimeException(e);
						}
						paramPreparedStatement.setObject(i, value);
				}
				
			};
			this.jdbcScsTemplate.batchUpdate(sql, setter);
		} catch (Exception e) {
			log.error("批量插入数据出错", e);
			throw new DaoAccessException(e);
		}
	return entity.length;
}

	public void createIndex(String tableName, String indexName,
			String... fields) {
		String sql = "create index "+indexName+" on "+tableName+" ("+StringUtils.join(fields, ",")+")";
		jdbcScsTemplate.execute(sql);
	}

}
