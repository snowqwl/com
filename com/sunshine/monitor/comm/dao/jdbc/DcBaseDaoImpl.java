package com.sunshine.monitor.comm.dao.jdbc;

import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.BaseDao;
import com.sunshine.monitor.comm.util.orm.SqlUtils;
import com.sunshine.monitor.comm.util.orm.bean.PreSqlEntry;
import com.sunshine.monitor.comm.util.orm.exception.ORMException;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.system.redlist.BeanSelfAware;

@Repository
public abstract class DcBaseDaoImpl implements BaseDao, BeanSelfAware,
		ApplicationContextAware {
	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 主键名
	 */
	private String pkName;

	/**
	 * 实体类名(全包名)
	 */
	private String beanClassName;

	private BaseDao self;

	//@Autowired
	//@Qualifier("jdbcDcTemplate")
	public JdbcTemplate jdbcDcTemplate;

	//@Autowired
	//@Qualifier("oracleLobHandler")
	public LobHandler lobHandler;

	protected ApplicationContext applicationContext;

	protected Logger log = LoggerFactory.getLogger(DcBaseDaoImpl.class);

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setSelf(Object proxyBean) {
		this.self = (BaseDao) proxyBean;
	}

	public BaseDao getSelf() {
		return this.self;
	}


	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	/**
	 * 根据主键查询返回一条记录
	 * 
	 * @param <T>
	 * @param tableName
	 * @param colName
	 * @param colValue
	 * @param classz
	 * @return
	 */
	public <T> T queryDetail(String tableName, String colName, String colValue,
			Class<T> classz) throws Exception {
		StringBuffer sqls = new StringBuffer(50);
		sqls.append("SELECT * FROM ");
		sqls.append(tableName);
		sqls.append(" where ");
		sqls.append(colName).append("=").append(colValue);
		return queryObject(sqls.toString(), classz);
	}

	public <T> T queryDetail(String colValue) throws Exception {
		StringBuffer sqls = new StringBuffer(50);
		sqls.append("SELECT * FROM ");
		sqls.append(this.tableName);
		sqls.append(" where ");
		sqls.append(this.pkName).append("=").append(colValue);
		return queryObject(sqls.toString(), (Class<T>) Class
				.forName(this.beanClassName));
	}

	/**
	 * 根据SQL语句查询返回一条记录
	 */
	public <T> T queryObject(String sql, Class<T> classz) throws Exception {
		return this.jdbcDcTemplate.queryForObject(sql, new JcbkRowMapper<T>(
				classz));
	}

	/**
	 * 根据SQL语句查询返回列表
	 */
	public <T> List<T> queryList(String sql, Class<T> classz) throws Exception {

		return this.jdbcDcTemplate.query(sql, new JcbkRowMapper<T>(classz));
	}

	/**
	 * 根据SQL语句查询返回一条记录
	 */
	public <T> T queryForObject(String sql, Class<T> classz) {
		return this.jdbcDcTemplate.queryForObject(sql, new JcbkRowMapper<T>(
				classz));
	}

	/**
	 * 根据SQL语句查询返回列表
	 */
	public <T> List<T> queryForList(String sql, Class<T> classz) {

		return this.jdbcDcTemplate.query(sql, new JcbkRowMapper<T>(classz));
	}

	/**
	 * 根据SQL语句查询返回列表
	 */
	public <T> List<T> queryForList(JdbcTemplate jTemplate, String sql,
			Class<T> classz) {

		return jTemplate.query(sql, new JcbkRowMapper<T>(classz));
	}

	/**
	 * 分页显示
	 * 
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @return
	 */

	public Map<String, Object> findPageForMap(String sql, int curPage,
			int pageSize) {
		StringBuffer totalSQL = new StringBuffer(
				" SELECT count(1) FROM ( select *  from (");
		StringBuffer filterSql = new StringBuffer(20);
		int pos = sql.toUpperCase().indexOf("ORDER");
		if (pos != -1) {
			String start = sql.substring(0, pos);
			String t = sql.substring(pos, sql.length());
			int p = t.indexOf(")");
			if (p != -1) {
				filterSql.append(start);
				filterSql.append(t.substring(0, t.length()));
			} else {
				filterSql.append(start);
			}
			totalSQL.append(filterSql.toString());
		} else {
			totalSQL.append(sql);
		}
		totalSQL.append(") where rownum <= 500 ) totalTable ");
		// 总记录数
		int totalRows = jdbcDcTemplate.queryForInt(totalSQL.toString());
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		List list = jdbcDcTemplate.queryForList(paginationSQL.toString());

		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}
	public Map<String, Object> findPageForMap(String sql, int curPage,
			int pageSize,Object vl ) {
		StringBuffer totalSQL = new StringBuffer(
				" SELECT count(1) FROM ( select *  from (");
		StringBuffer filterSql = new StringBuffer(20);
		int pos = sql.toUpperCase().indexOf("ORDER");
		if (pos != -1) {
			String start = sql.substring(0, pos);
			String t = sql.substring(pos, sql.length());
			int p = t.indexOf(")");
			if (p != -1) {
				filterSql.append(start);
				filterSql.append(t.substring(0, t.length()));
			} else {
				filterSql.append(start);
			}
			totalSQL.append(filterSql.toString());
		} else {
			totalSQL.append(sql);
		}
		totalSQL.append(") where rownum <= 500 ) totalTable ");
		// 总记录数
		int totalRows = jdbcDcTemplate.queryForInt(totalSQL.toString(),vl);
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		List list = jdbcDcTemplate.queryForList(paginationSQL.toString(),vl );

		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}
	public Map<String, Object> findPageForMap(String sql, int curPage,
			int pageSize, Class<?> clazz) {
		StringBuffer totalSQL = new StringBuffer(
				" SELECT count(1) FROM ( select *  from (");
		StringBuffer filterSql = new StringBuffer(20);
		int pos = sql.toUpperCase().indexOf("ORDER");
		if (pos != -1) {
			String start = sql.substring(0, pos);
			String t = sql.substring(pos, sql.length());
			int p = t.indexOf(")");
			if (p != -1) {
				filterSql.append(start);
				filterSql.append(t.substring(0, t.length()));
			} else {
				filterSql.append(start);
			}
			totalSQL.append(filterSql.toString());
		} else {
			totalSQL.append(sql);
		}
		totalSQL.append(") where rownum <= 2000 ) totalTable ");
		// 总记录数
		int totalRows = jdbcDcTemplate.queryForInt(totalSQL.toString());
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		List list = this.queryForList(paginationSQL.toString(), clazz);

		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}
	
	public Map<String, Object> findPageForMapNoLimit(String sql, int curPage,
			int pageSize, Class<?> clazz) {
		StringBuffer totalSQL = new StringBuffer(
				" SELECT count(1) FROM ( select *  from (");
		StringBuffer filterSql = new StringBuffer(20);
		int pos = sql.toUpperCase().indexOf("ORDER");
		if (pos != -1) {
			String start = sql.substring(0, pos);
			String t = sql.substring(pos, sql.length());
			int p = t.indexOf(")");
			if (p != -1) {
				filterSql.append(start);
				filterSql.append(t.substring(0, t.length()));
			} else {
				filterSql.append(start);
			}
			totalSQL.append(filterSql.toString());
		} else {
			totalSQL.append(sql);
		}
		totalSQL.append(") )");
		// 总记录数
		int totalRows = jdbcDcTemplate.queryForInt(totalSQL.toString());
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		List list = this.queryForList(paginationSQL.toString(), clazz);

		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}
	
	/**
	 * 分页，使用数据源
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @param clazz
	 * @param jd
	 * @return
	 */
	public Map<String, Object> findPageForMapByJdbc(String sql, int curPage,
			int pageSize, Class<?> clazz,JdbcTemplate jd) {
		StringBuffer totalSQL = new StringBuffer(
				" SELECT count(1) FROM ( select *  from (");
		StringBuffer filterSql = new StringBuffer(20);
		int pos = sql.toUpperCase().indexOf("ORDER");
		if (pos != -1) {
			String start = sql.substring(0, pos);
			String t = sql.substring(pos, sql.length());
			int p = t.indexOf(")");
			if (p != -1) {
				filterSql.append(start);
				filterSql.append(t.substring(0, t.length()));
			} else {
				filterSql.append(start);
			}
			totalSQL.append(filterSql.toString());
		} else {
			totalSQL.append(sql);
		}
		totalSQL.append(") where rownum <= 10000 ) totalTable ");
		// 总记录数
		int totalRows = jd.queryForInt(totalSQL.toString());
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		//List list = this.queryForList(paginationSQL.toString(), clazz);
		List list = this.queryForList(jd,paginationSQL.toString(), clazz);

		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}
	
	public Map<String, Object> findPageForMapNoLimitByJdbc(String sql, int curPage,
			int pageSize, Class<?> clazz,JdbcTemplate jd) {
		StringBuffer totalSQL = new StringBuffer(
				" SELECT count(1) FROM ( select *  from (");
		StringBuffer filterSql = new StringBuffer(20);
		int pos = sql.toUpperCase().indexOf("ORDER");
		if (pos != -1) {
			String start = sql.substring(0, pos);
			String t = sql.substring(pos, sql.length());
			int p = t.indexOf(")");
			if (p != -1) {
				filterSql.append(start);
				filterSql.append(t.substring(0, t.length()));
			} else {
				filterSql.append(start);
			}
			totalSQL.append(filterSql.toString());
		} else {
			totalSQL.append(sql);
		}
		totalSQL.append(") ) totalTable ");
		// 总记录数
		int totalRows = jd.queryForInt(totalSQL.toString());
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		//List list = this.queryForList(paginationSQL.toString(), clazz);
		List list = this.queryForList(jd,paginationSQL.toString(), clazz);

		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}

	/**
	 * 分页显示 对象封装
	 * 
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> queryPageForMap(String sql, int curPage,
			int pageSize, Class<?> classz, int queryCount, String tableName) {

		int totalRows = 500;
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");

		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		// System.out.println("分页:" + paginationSQL);

		// long start = System.currentTimeMillis();
		List<?> list = this.queryForList(paginationSQL.toString(), classz);
		// long end = System.currentTimeMillis() ;
		// System.out.println("分页(t):" + (end-start) + " ms");

		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}
	
public Map<String, Object> queryPageForMapByJdbc(String sql, int curPage, int pageSize, Class<?> classz, int queryCount, JdbcTemplate jd){
		
		int totalRows = queryCount;
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");

		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		List<?> list = this.queryForList(jd, paginationSQL.toString(), classz);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", totalRows);
		map.put("rows", list);

		return map;
	}

	public Map<String, Object> queryForMap(String sql, int curPage,
			int pageSize, Class<?> classz, int queryCount) {
		int totalRows = 500;
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+FIRST_ROWS(").append(pageSize).append(")*/ ");
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		List<?> list = queryForList(paginationSQL.toString(), classz);

		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);
		return map;
	}

	public Map<String, Object> queryPageLimitTotal(String sql, int curPage,
			int pageSize, int queryCount, JdbcTemplate jTemplate,
			String orderStr) {
		StringBuffer totalSQL = new StringBuffer(
				" SELECT count(1) FROM (select * from ( ");
		StringBuffer filterSql = new StringBuffer(20);
		int pos = sql.toUpperCase().indexOf("ORDER");
		if (pos != -1) {
			String start = sql.substring(0, pos);
			String t = sql.substring(pos, sql.length());
			int p = t.indexOf(")");
			if (p != -1) {
				filterSql.append(start);
				filterSql.append(t.substring(0, t.length()));
			} else {
				filterSql.append(start);
			}
			totalSQL.append(filterSql.toString());
		} else {
			totalSQL.append(sql);
		}
		totalSQL.append(") ").append(" t   ");

		totalSQL.append(")  totalTable ");
		totalSQL.append(" where ROWNUM < ").append(queryCount);

		// 总记录数
		int totalRows = jTemplate.queryForInt(totalSQL.toString());
		// 总页数
		int totalPages;
		if (pageSize != -1) {
			if (totalRows % pageSize == 0) {
				totalPages = totalRows / pageSize;
			} else {
				totalPages = (totalRows / pageSize) + 1;
			}
		} else {
			totalPages = 1;
		}
		// 起始行数
		int startIndex = (curPage - 1) * pageSize;
		// 结束行数
		int lastIndex = 0;
		if (totalRows < pageSize) {
			lastIndex = totalRows;
		} else if ((totalRows % pageSize == 0)
				|| (totalRows % pageSize != 0 && curPage < totalPages)) {
			lastIndex = curPage * pageSize;
		} else if (totalRows % pageSize != 0 && curPage == totalPages) {// 最后一页
			lastIndex = totalRows;
		}

		// 构造oracle数据库的分页语句
		StringBuffer cbo_sql = new StringBuffer(20);
		cbo_sql.append(" /*+RULE && +first_rows(").append(pageSize).append(
				")*/ ");

		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT ");
		paginationSQL.append(cbo_sql.toString());
		paginationSQL.append("temp.* ,ROWNUM num FROM ( ");
		paginationSQL.append(sql);
		if (pageSize != -1) {
			paginationSQL.append("" + ") temp where ROWNUM <= " + lastIndex);
		} else {
			paginationSQL.append("" + ") temp ");
		}
		paginationSQL.append(" ) WHERE" + " num > " + startIndex);

		if (StringUtils.isNotBlank(orderStr)) {
			paginationSQL.append("  order by ").append(orderStr).append("  ");
		}
		List list = jTemplate.queryForList(paginationSQL.toString());

		Map<String, Object> map = new HashMap<String, Object>();
		// 设置总共有多少条记录
		map.put("total", totalRows);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}

	public Date getDbNowDate() {
		String sql = "select sysdate from dual";
		return jdbcDcTemplate.queryForObject(sql, Date.class);
	}

	public static class JcbkRowMapper<T> implements RowMapper<T> {

		private Class<T> requiredType;

		private Class<?>[] LongType = { Long.class };

		private Class<?>[] StringType = { String.class };

		private Class<?>[] DoubleType = { Double.class };

		private Class<?>[] BlobType = { Blob.class };

		private Class<?>[] IntType = { int.class };

		public JcbkRowMapper(Class<T> requiredType) {
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
					try {
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
								colValue = cDateStrWithSecond(rs
										.getTimestamp(colName));
								type = StringType;
								if (colValue == null)
									colValue = "";
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
											colValue = new Long(rs
													.getLong(colName));
										else {
											colValue = null;
										}
										type = LongType;
									}
								} else {
									if (rs.getString(colName) != null)
										colValue = new Double(rs
												.getDouble(colName));
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
							Method meth = classObject.getMethod(
									"set" + colName, type);
							meth.invoke(objIns, values);
						}
					} catch (Exception e) {
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

		private String cDateStrWithSecond(Date nDate) {
			if (nDate == null) {
				return "";
			}
			SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return d.format(nDate);
		}
	}

	public <T> T queryForBeanByCondition(Class<T> clazz,Cnd cnd) throws Exception {
		PreSqlEntry entry = SqlUtils.getPreSelectSql(getTableName(), null,cnd);
		List<T> result = this.jdbcDcTemplate.query(entry.getSql(), entry
				.getValues().toArray(), new JcbkRowMapper<T>(clazz));
		if (result == null || result.size() == 0)
			return null;
		else
			return result.get(0);
	}

	public <T> T update(T entity, String keyName) throws ORMException {
		PreSqlEntry entry = SqlUtils.getPreUpdateSqlByObject(getTableName(),
				entity, new HashMap<String, String>(), keyName);
		this.jdbcDcTemplate.update(entry.getSql(), entry.getValues().toArray());
		return entity;
	}
	
	public <T> T updateJz(T entity, String keyName) throws ORMException {
		PreSqlEntry entry = SqlUtils.getPreUpdateSqlByObject(getTableName(),
				entity, new HashMap<String, String>(), keyName);
		Object[] objArray = entry.getValues().toArray();
		SimpleDateFormat startTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		System.out.println(startTimeFormat.format(objArray[0]));
		StringBuffer strSql = new StringBuffer("update JM_TRANS_SCHEDULE set zxsj=to_date(" + startTimeFormat.format(objArray[0]) + ",'yyyy-MM-dd HH24:mi:ss'),");
		strSql.append("cwnr='" + objArray[1] + "',bz='" + objArray[2] + "',rwzq='" + objArray[3] + "',rwzt='" + objArray[4] + "',");
		strSql.append("rwmc='" + objArray[5]+ "',sxl='" + objArray[6] + "',ccgc='" + objArray[7] + "',sxlff='" + objArray[8] + "'");
		strSql.append(" Where rwbh = '" + objArray[9] + "'");
		this.jdbcDcTemplate.update(strSql.toString());
		return entity;
	}

	public Map<String, Object> findPageForMapObj(String sql, int curPage,
			int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据Map条件拼接sql查询返回列表
	 */
	public <T> List<T> queryForListByMap(Map<String, Object> map,
			String tablename, Class<T> classz) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (map != null) {
			Set<String> set = map.keySet();
			for (Object o : set) {
				String val = o.toString();
				if (val.equalsIgnoreCase("dwdm")) {
					String[] deps = map.get(val).toString().split(",");
					for (int i = 0; i < deps.length; i++) {
						if (i == 0) {
							sb.append(" and dwdm = '" + deps[i] + "'");
						} else {
							sb.append(" or dwdm = '" + deps[i] + "'");
						}
					}
				} else if (!(val.equalsIgnoreCase("sortKey") || val.equalsIgnoreCase("sort"))) {
					sb.append(" and " + val + "='" + map.get(val) + "'");
				}
			}
			if (map.get("sortKey") != null) {
				sb.append(" order  by ").append(map.get("sortKey"));
				if (map.get("sort") != null) {
					sb.append(" " + map.get("sort"));
				}
			}
		}
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(tablename).append(" where 1=1 ").append(sb);

		return this.queryForList(sql.toString(), classz);
	}
	
	public Object getField(String tableName, String pkName, String viewName, Object pk){
		Object result = null;
		String sql = ("select t.%1$s from %2$s t where t.%3$s = ? and ROWNUM = 1");
		List list = this.jdbcDcTemplate.queryForList(String.format(sql, viewName,tableName,pkName),new Object[]{pk},Object.class);
		if (list != null && list.size() > 0) {
			 result = list.get(0);
		}else{
			result = pk;
		}
		return result;
	}

	public JdbcTemplate getJdbcDcTemplate() {
		return jdbcDcTemplate;
	}

	public void setJdbcDcTemplate(JdbcTemplate jdbcDcTemplate) {
		this.jdbcDcTemplate = jdbcDcTemplate;
	}
	
	public int delete(String whereSql) throws Exception {
		return this.jdbcDcTemplate.update("delete "+getTableName()+" where "+ whereSql);
	}

	public Map<String, Object> findPageForMapNoLimit(String sql, int curPage,
			int pageSize) {
		return null;
	}

	


}
