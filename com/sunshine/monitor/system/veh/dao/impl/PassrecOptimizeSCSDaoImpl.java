package com.sunshine.monitor.system.veh.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.dao.PassrecOptimizeSCSDao;

/**
 * 过车查询基于GreenPlum改造实现
 * @author licheng
 * @date 2016-8-15
 */
@Repository("passrecOptimizeSCSDao")
public class PassrecOptimizeSCSDaoImpl extends ScsBaseDaoImpl implements PassrecOptimizeSCSDao {

	public int queryPassCount(String sql, Object... params) throws Exception {
		long pstart = System.currentTimeMillis();
		int result = 0;
		try{
			Object r = this.jdbcScsTemplate.queryForObject(sql, params, Object.class);
			result = Integer.valueOf(r.toString());
			long pend = System.currentTimeMillis();
			log.info("JCBKSQL-COUNT:(" + (pend-pstart) + "毫秒)" + sql);
		}catch(EmptyResultDataAccessException e){
			//不做处理
		}
		return result;
	}
	
	public int queryPassCount(String sql) throws Exception {
		long pstart = System.currentTimeMillis();
		int result = 0;
		try{
			Object r = this.jdbcScsTemplate.queryForInt(sql);
			result = Integer.valueOf(r.toString());
			long pend = System.currentTimeMillis();
			log.info("JCBKSQL-COUNT:(" + (pend-pstart) + "毫秒)" + sql);
		}catch(EmptyResultDataAccessException e){
			//不做处理
		}
		return result;
	}

	public List queryPassPage(String sql, Object[] params, Class<?> clazz)
			throws Exception {
		long pstart = System.currentTimeMillis();
		List list = this.jdbcScsTemplate.query(sql, params, new BeanPropertyRowMapper(clazz));
		long pend = System.currentTimeMillis();
		log.info("JCBKSQL-PAGE:(" + (pend-pstart) + "毫秒)" + sql);
		return list;
	}
	

	public List queryPassPage(String sql, Class<?> clazz)
			throws Exception {
		long pstart = System.currentTimeMillis();
		List list = this.jdbcScsTemplate.query(sql,new BeanPropertyRowMapper(clazz));
		long pend = System.currentTimeMillis();
		log.info("JCBKSQL-PAGE:(" + (pend-pstart) + "毫秒)" + sql);
		return list;
	}
	
	/**
	 * 构建GP分页
	 */
	public String getPageSqlGp(String sql, int cpage, int psize,
			List<Object> params) throws Exception {
		// 起始行数
		int startIndex = (cpage - 1) * psize;
		// 构造GP数据库的分页语句
		StringBuffer psql = new StringBuffer(sql);
		psql.append(" limit ? offset ?");
		params.add(psize);
		params.add(startIndex);
		return psql.toString();
	}
	
	/**
	 * 构建GP分页sql
	 * @param sql
	 * @param cpage
	 * @param psize
	 * @return
	 * @throws Exception
	 */
	public String getPageSqlGp(String sql, int cpage, int psize) throws Exception {
		// 起始行数
		int startIndex = (cpage - 1) * psize;
		// 构造GP数据库的分页语句
		StringBuffer psql = new StringBuffer(sql);
		psql.append(" limit "+psize+" offset "+startIndex);
		return psql.toString();
	}
	
	@Override
	public List<VehPassrec> getPasslistsExt(String sql, Map<String, Object> conditions) throws Exception {
		List<VehPassrec> list = this.getPageDatas(sql,
				Integer.parseInt(conditions.get("page").toString()),
				Integer.parseInt(conditions.get("rows").toString()),VehPassrec.class);
		return list;
	}
	
	public int queryPassAndJjlist3Total(String sql) throws Exception{
		return super.getTotal(sql);
	}
}
