package com.sunshine.monitor.comm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sunshine.monitor.comm.util.orm.helper.Cnd;

public interface BaseDao {

	/**
	 * Query single object
	 * @param sql
	 * @param classz
	 * @return
	 */
	public abstract <T> T queryObject(String sql, Class<T> classz) throws Exception;

	/**
	 * Query a list of object
	 * @param sql
	 * @param classz
	 * @return
	 */
	public abstract <T> List<T> queryList(String sql,Class<T> classz) throws Exception;
	

	/**
	 * 
	 * @param <T>
	 * @param jTemplate
	 * @param sql
	 * @param classz
	 * @return
	 */
	public abstract <T> List<T> queryForList(JdbcTemplate jTemplate, String sql, Class<T> classz);

	
	/**
	 * 
	 * @param <T>
	 * @param tableName
	 * @param colName
	 * @param colValue
	 * @param classz
	 * @return
	 * @throws Exception
	 */
	public abstract <T> T queryDetail(String tableName, String colName, String colValue, Class<T> classz)  throws Exception;
	
	/**
	 * 
	 * @param <T>
	 * @param colValue
	 * @return
	 * @throws Exception
	 */
	public abstract <T> T queryDetail(String colValue)  throws Exception;
		
	
	/**
	 * 分页
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> findPageForMap(String sql, int curPage, int pageSize);

	public Map<String, Object> findPageForMap(String sql,Object[] array, int curPage, int pageSize);

	
	public Map<String, Object> findPageForMap(String sql, int curPage, int pageSize,Class<?> clazz);

	public Map<String, Object> findPageForMap(String sql,Object[] array, int curPage, int pageSize,
											  Class<?> clazz);
	
	public Map<String, Object> findPageForMapObj(String sql, int curPage, int pageSize);
	
	/**
	 * 分页，不限定查询数量(bean注入)
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @param clazz
	 * @return
	 */
	public Map<String, Object> findPageForMapNoLimit(String sql, int curPage,
			int pageSize, Class<?> clazz);

	
	
	/**
	 * 分页，不限定查询数量(非bean注入)
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> findPageForMapNoLimit(String sql, int curPage,
			int pageSize);
	public Map<String, Object> findPageForMapNoLimit(String sql, Object[] array,int curPage,
													 int pageSize);
	
	/**
	 * Query limit Map
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @param classz
	 * @param queryCount
	 * @return
	 */
	public Map<String, Object> queryPageForMap(String sql, int curPage, int pageSize, Class<?> classz, int queryCount, String tableName);
	
	/**
	 * Query limit Map by jdbcTemplate
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @param classz
	 * @param queryCount
	 * @param jd
	 * @return
	 */
	public Map<String, Object> queryPageForMapByJdbc(String sql, int curPage, int pageSize, Class<?> classz, int queryCount, JdbcTemplate jd);

	/**
	 * 
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @param classz
	 * @param queryCount
	 * @return
	 */
	public Map<String, Object> queryForMap(String sql, int curPage, int pageSize, Class<?> classz, int queryCount);

	
	/**
	 * Query limit Map
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @param queryCount
	 * @param jTemplate
	 * @return
	 */
	public Map<String, Object> queryPageLimitTotal(String sql, int curPage, int pageSize, int queryCount, JdbcTemplate jTemplate,String orderStr);
	public Map<String, Object> queryPageLimitTotal(String sql,Object[] array, int curPage,
												   int pageSize,
												   int queryCount, JdbcTemplate jTemplate,String orderStr);



	/**
	 * 获取数据库当前时间
	 * @return
	 */
	public Date getDbNowDate();
	
	public <T> T queryForBeanByCondition(Class<T> clazz,Cnd cnd) throws Exception;
	
	public <T> T update(T entity , String keyName) throws Exception;
	
	public <T> T updateJz(T entity, String keyName) throws Exception;
	
	/**
	 * 
	 * @param <T>
	 * @param map
	 * @param classz
	 * @return
	 */
	public <T> List<T> queryForListByMap(Map<String,Object> map,String tablename,Class<T> classz) throws Exception;
	
	/**
	 * 给出表指定字段值　
	 * @param tableName
	 * @param pkName
	 * @param viewName
	 * @param pk
	 * @return
	 */
	public Object getField(String tableName, String pkName, String viewName, Object pk);
	
		
	/**
	 * 根据 whereSql 物理删除表
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public int delete(String whereSql) throws Exception;
	
	public Map<String, Object> findPageForMapNoLimitByJdbc(String sql, int curPage,
			int pageSize, Class<?> clazz,JdbcTemplate jd);
	
	/**
	 * 使用数据源查询分页
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @param clazz
	 * @param jd
	 * @return
	 */
	public Map<String, Object> findPageForMapByJdbc(String sql, int curPage,
			int pageSize, Class<?> clazz,JdbcTemplate jd);

	public Map<String, Object> findPageForMapByJdbc(String sql, Object[] array,int curPage,
													int pageSize, Class<?> clazz,JdbcTemplate jd);
	
	/**
	 * 删除
	 * @param tabname
	 * @param colname
	 * @param colValue
	 * @return
	 * @throws Exception
	 */
	public int delete(String tabname,String colname,String colValue)throws Exception;
	
	
	/**
	 * 
	 * @param sql
	 * @param args
	 * @param classz
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryForList(String sql, Object args[],Class<T> classz) throws Exception;
	
	//========================总记录与分页数据分开================================================
	public int getTotal(String sql) throws Exception;
	public int getTotal(String sql, Object...params) throws Exception;
	public List<Map<String, Object>> getPageDatas(String sql, int curPage, int pageSize) throws Exception;
	public List<Map<String, Object>> getPageDatas(String sql, int curPage, int pageSize, Object...params) throws Exception;
	/**
	 * 不计总数的分页查询记录
	 * @param <T>
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getPageDatas(String sql, int curPage,int pageSize, Class<T> clazz) throws Exception;
	public <T> List<T> getPageDatas(String sql, int curPage,int pageSize, Class<T> clazz, Object...params) throws Exception;
	
}