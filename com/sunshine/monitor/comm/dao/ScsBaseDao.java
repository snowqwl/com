package com.sunshine.monitor.comm.dao;


import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
/**
 * for Scsdb 
 * @author maijial
 *
 */
public interface ScsBaseDao {

	
	/**
	 * Query a list of object
	 * @param sql
	 * @param classz
	 * @return
	 */
	public abstract <T> List<T> queryForList(String sql,Class<T> classz) throws Exception;
		
	/**
	 * query for integer
	 * @param <T>
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public abstract <T> int queryForInt(String sql)throws Exception; 
	
	/**
	 * 分页
	 * @param sql
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> findPageForMap(String sql, int curPage, int pageSize) throws Exception;
	
	public <T> Map<String, Object> findPageForMap(String sql, int curPage, int pageSize,Class<T> clazz) throws Exception;
    
	public <T> int queryForInt(String preSql,Object[] values) throws Exception;
	
	public <T> T quertForObject(String preSql,Class<T> classz) throws Exception;
	
	public List<Map<String,Object>>  findList(String sql);
	
	/**
	 * 创建临时表
	 * @param sql
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public int createTemp(String sql,String tablename)throws Exception;
	
	/**
	 * 删除临时表
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public int dropTable(String tablename)throws Exception;
	
	public <T> List<T> query(String tableName,Class<T> clazz, Cnd cnd , Pager pager) throws Exception ;
	
	public void createTable(Class<?> clazz, String tableName);
	
	public  int update(String sql);
	
	/**
	 * 根据实体映射保存
	 * @param entity 实体
	 * @return
	 */
	<T> int save(T[] entity,String tableName) throws Exception ;
	
	public void createIndex(String tableName, String indexName, String... fields);
	
	//========================总记录与分页数据分开================================================
	public int getTotal(String sql) throws Exception;
	public int getTotal(String sql, Object...params) throws Exception;
	public List<Map<String, Object>> getPageDatas(String sql, int curPage, int pageSize) throws Exception;
	public List<Map<String, Object>> getPageDatas(String sql, int curPage, int pageSize, Object...params) throws Exception;
	public <T> List<T> getPageDatas(String sql, int curPage,int pageSize, Class<T> clazz) throws Exception;
	public <T> List<T> getPageDatas(String sql, int curPage,int pageSize, Class<T> clazz, Object...params) throws Exception;
}
