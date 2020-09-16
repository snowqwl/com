package com.sunshine.monitor.system.veh.dao;

import java.util.List;
import java.util.Map;

import com.sunshine.monitor.comm.dao.ScsBaseDao;
import com.sunshine.monitor.system.veh.bean.VehPassrec;

/**
 * 过车查询基于GreenPlum改造接口
 * @author licheng
 * @date 2016-8-15
 */
public interface PassrecOptimizeSCSDao extends ScsBaseDao{
	
	/**
	 * 查询过车总数
	 * @param sql
	 * @param params
	 * @return
	 */
	public int queryPassCount(String sql, Object... params) throws Exception;
	
	/**
	 * 查询过车总数
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int queryPassCount(String sql) throws Exception;
	
	/**
	 * 查询过车分页数据
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List queryPassPage(String sql, Object[] params, Class<?> clazz) throws Exception;
	
	/**
	 * 查询过车分页数据
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public List queryPassPage(String sql, Class<?> clazz) throws Exception;
	
	/**
	 * <2016-8-17> 车查询基于GreenPlum改造
	 * 
	 * 构造GreenPlum数据库的分页语句,不含总数
	 * @param sql
	 * @param cpage
	 * @param psize
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getPageSqlGp(String sql, int cpage, int psize, List<Object> params) throws Exception;
	
	/**
	 * 构建GP分页sql
	 * @param sql
	 * @param cpage
	 * @param psize
	 * @return
	 * @throws Exception
	 */
	public String getPageSqlGp(String sql, int cpage, int psize) throws Exception;
	
	/**
	 * <2016-12-19> licheng 精确号牌分页查询,不计总数
	 * @param conditions
	 * @return
	 * @throws Exception
	 */
	public List<VehPassrec> getPasslistsExt(String sql, Map<String, Object> conditions) throws Exception;
	
	/**
	 * <2016-12-19> licheng 精确号牌分页查询,总计总数
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int queryPassAndJjlist3Total(String sql) throws Exception;
}
