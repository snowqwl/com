package com.sunshine.monitor.comm.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.exception.DaoAccessException;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.Pager;

public interface DBaseDao<E> {
	
	/**
	 * 获取操作表的表名
	 * @return
	 */
	String getTableName();
	
	/**
	 * 保存实体，userDefSql 定义 insert sql 语句的自定义字段 如： gxsj = sysdate
	 * @param entity
	 * @param userDefSql
	 * @throws Exception
	 */
	public  E save(E entity,Map<String,String> userDefSql) throws DaoAccessException;
	
	/**
	 * 更新实体，userDefSql 定义 insert sql 语句的自定义字段 如： gxsj = sysdate
	 * @param entity
	 * @param userDefSql
	 * @param keyName
	 * @throws Exception
	 */
	public  E upate(E entity, Map<String,String> userDefSql,String keyName)  throws DaoAccessException;
	
	/**
	 * 根据实体映射保存
	 * @param entity 实体
	 * @return
	 */
	E save(E entity) throws Exception ;
	
	/**
	 * 根据实体映射更新
	 * @param entity 实体
	 * @return
	 */
	E update(E entity, String keyName) throws DaoAccessException ;
	
	public List<E> queryForList(Class<E> clazz , Cnd cnd)  throws DaoAccessException ;
	
	public List<E> queryForCondition(Class<E> clazz ,Pager pager, 
			Cnd cnd) throws Exception;
	
	public E queryForBean(Class<E> clazz , Cnd cnd) throws DaoAccessException ;
	
	public int queryForInt(String columnName , Cnd cnd) throws DaoAccessException;
	
	public int getTotal(Cnd cnd) throws DaoAccessException;
}
