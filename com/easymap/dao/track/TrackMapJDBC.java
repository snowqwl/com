package com.easymap.dao.track;

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
import org.springframework.jdbc.core.JdbcTemplate;

import com.sunshine.monitor.comm.dao.util.SqlParser;

public abstract class TrackMapJDBC implements InitializingBean {
	
	@Autowired
	@Qualifier("dataSourceYdb")
	protected DataSource dataSourceYdb;
	
	public JdbcTemplate jdbcTemplate;
	
	protected Logger log = LoggerFactory.getLogger(TrackMapJDBC.class);
	
	public final SqlParser sqlParser = new SqlParser();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.jdbcTemplate = new JdbcTemplate(this.dataSourceYdb);
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
		List vl=new ArrayList<>();
	
		int start = (curPage - 1) * pageSize;
		vl.add(pageSize);
		vl.add(start);
		sb.append(sql).append(" limit ").append("?").append(" offset ").append("?");
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sb.toString(),vl.toArray());
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		return list;
	}
	/**
	 * 查询列表数据
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPageDatas(String sql,Object [] key, int curPage,
			int pageSize) throws Exception {
		StringBuffer sb = new StringBuffer();
		int start = (curPage - 1) * pageSize;
		key[key.length]=pageSize;
		key[key.length]=start;
		sb.append(sql).append(" limit ").append("?").append(" offset ").append("?");
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sb.toString(),key);
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
	 * 查询总数
	 * @param sql
	 * @return
	 * @throws Exception
	 */ 
	
	public int getTotal(String sql,Object[] key) throws Exception {
		int total = 0;
		try {			
			sql=sql.replace("COUNT(1)",  "COUNT(1) as count");
			long start = System.currentTimeMillis();
			Map m = this.jdbcTemplate.queryForMap(sql, key);
			long end = System.currentTimeMillis();
			total=Integer.parseInt(String.valueOf(m.get("count")));
			log.info("sqlCount-(" + (end-start) + "毫秒)" +sql);
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
	protected int getCount(String sql) {
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
}
