package com.sunshine.monitor.comm.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sunshine.monitor.comm.dao.exception.DaoAccessException;
import com.sunshine.monitor.comm.dao.page.PagingParameter;

/**
 * 分页扩展接口
 * @author Administrator
 *
 */
public interface ExpandBaseDao extends BaseDao {
	
	/**
	 * 查询总记录数
	 * @param sql                 查询SQL
	 * @param limit               查询限制,limit=0查询不受限制
	 * @return
	 * @throws SQLException
	 * @throws DaoAccessException
	 */
	public int getRecordCounts(String sql, int limit);
	
	
	/**
	 * 
	 * @param sql                 查询SQL
	 * @param jdt                 外部数据源
	 * @param limit               查询限制,limit=0查询不受限制
	 * @return
	 * @throws SQLException
	 * @throws DaoAccessException
	 */
	public int getRecordCounts(String sql,JdbcTemplate jdt, int limit);
	
	/**
	 * 
	 * @param sql                 查询SQL
	 * @param pagingParameter     分页对象,pagingParameter=null或无参数构分页对象,不做分页
	 * @param jdt                 外部数据源
	 * @return
	 * @throws SQLException
	 * @throws DaoAccessException
	 */
	public List getRecordData(String sql, PagingParameter pagingParameter,JdbcTemplate jdt);


	/**
	 * 
	 * @param sql                 查询SQL
	 * @param pagingParameter     分页对象,pagingParameter=null或无参数构分页对象,不做分页
	 * @return
	 * @throws SQLException
	 * @throws DaoAccessException
	 */
	public List getRecordData(String sql, PagingParameter pagingParameter);
	
	
	/**
	 * 
	 * @param <T>         
	 * @param sql                 查询SQL
	 * @param pagingParameter     分页对象,pagingParameter=null或无参数构分页对象,不做分页
	 * @param classz
	 * @return
	 * @throws SQLException
	 * @throws DaoAccessException
	 */
	public <T> List<T> getRecordData(String sql, PagingParameter pagingParameter, Class<T> classz);
	
	
	/**
	 * 
	 * @param <T>
	 * @param sql                 查询SQL
	 * @param pagingParameter     分页对象,pagingParameter=null或无参数构分页对象,不做分页
	 * @param classz
	 * @param args
	 * @return
	 * @throws SQLException
	 * @throws DaoAccessException
	 */
	public <T> List<T> getRecordData(String sql, PagingParameter pagingParameter, Class<T> classz, Object...args);

	/**
	 * 
	 * @param <T>
	 * @param sql                 查询SQL
	 * @param pagingParameter     分页对象,pagingParameter=null或无参数构分页对象,不做分页
	 * @param classz
	 * @param jdbcTemplate        外部数据
	 * @return
	 * @throws SQLException
	 * @throws DaoAccessException
	 */
	public <T> List<T> getRecordData(String sql, PagingParameter pagingParameter, Class<T> classz, JdbcTemplate jdbcTemplate);
}