package com.sunshine.monitor.system.analysis.clickhouse.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.sunshine.monitor.comm.dao.util.SqlParser;

public abstract class ClickHouseJDBC implements InitializingBean {
	
	@Autowired
	@Qualifier("clickhouseSource")
	protected DataSource clickhouseSource;
	
	public JdbcTemplate jdbcTemplate;
	
	protected Logger log = LoggerFactory.getLogger(ClickHouseJDBC.class);
	
	public final SqlParser sqlParser = new SqlParser();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.jdbcTemplate = new JdbcTemplate(this.clickhouseSource);
	}
	
	/**
	 * 查询列表数据
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPageDatas(String sql, int curPage,
			int pageSize) throws Exception {
		StringBuffer sb = new StringBuffer();
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ").append(pageSize).append(" offset ").append(start);
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sb.toString());
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		return list;
	}
	
	/**
	 * 查询总数
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int getTotal(String sql) throws Exception {
		int total = 0;
		try {
			String csql = sqlParser.getSmartCountSql(sql);
			long start = System.currentTimeMillis();
			total = this.jdbcTemplate.queryForObject(csql, Integer.class);
			long end = System.currentTimeMillis();
			log.info("sqlCount-(" + (end-start) + "毫秒)" +csql);
		} catch (Exception e) {
			e.printStackTrace();
			total = 0;
		}
		return total;
	}
	
	/**
	 * 查询数据，并且返回总数
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> findPageForMap(String sql, int curPage,
			int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		int total = getCount(sql);
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ").append(pageSize).append(" offset ").append(start);
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sb
				.toString());
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		// 设置总共有多少条记录
		map.put("total", total);
		// 设置当前页的数据
		map.put("rows", list);

		return map;
	}
	
	/**
	 * 获取统计sql语句
	 * 
	 * @param sql
	 * @return
	 */
	private int getCount(String sql) {
		int total = 0;
		try {
			String csql = sqlParser.getSmartCountSql(sql);
			long start = System.currentTimeMillis();
			total = this.jdbcTemplate.queryForObject(csql, Integer.class);
			long end = System.currentTimeMillis();
			log.info("sqlCount-(" + (end-start) + "毫秒)" +csql);
		} catch (Exception e) {
			e.printStackTrace();
			total = 0;
		}
		return total;
	}
	
	/**
	 * 查询分页数据，按照指定类型返回
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
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
	
	/**
	 * 获取list数据
	 * @param sql
	 * @param classz
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryForList(String sql, Class<T> classz)
			throws Exception {
		return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(classz));
	}
	
	/**
	 * 相当于  List&lt;Entity&gt; <br />
	 * Map key 为小写字母
	 */
	public List<Map<String,Object>>  findListBySql(String sql){
		SqlRowSet dataSet = jdbcTemplate.queryForRowSet(sql);
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
				entity.put(column.toLowerCase(), dataSet.getObject(column));
			}
			entityList.add(entity);
		}
		return entityList ;
	}
}
