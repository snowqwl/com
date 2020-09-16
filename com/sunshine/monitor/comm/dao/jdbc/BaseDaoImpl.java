package com.sunshine.monitor.comm.dao.jdbc;

import java.lang.reflect.Field;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.ExpandBaseDao;
import com.sunshine.monitor.comm.dao.page.DataStore;
import com.sunshine.monitor.comm.dao.page.PagingParameter;
import com.sunshine.monitor.comm.dao.page.PagingSqlBuilder;
import com.sunshine.monitor.comm.dao.util.SqlParser;
import com.sunshine.monitor.comm.util.orm.SqlUtils;
import com.sunshine.monitor.comm.util.orm.bean.PreSqlEntry;
import com.sunshine.monitor.comm.util.orm.exception.ORMException;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.system.redlist.BeanSelfAware;

@Repository
public abstract class BaseDaoImpl implements ExpandBaseDao, BeanSelfAware,
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
	
	public final SqlParser sqlParser = new SqlParser();

	private ExpandBaseDao self;

	@Autowired
	@Qualifier("jdbcTemplate")
	public JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("oracleLobHandler")
	public LobHandler lobHandler;

	protected ApplicationContext applicationContext;

	protected Logger log = LoggerFactory.getLogger(getClass());

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setSelf(Object proxyBean) {
		this.self = (ExpandBaseDao) proxyBean;
	}

	public ExpandBaseDao getSelf() {
		return this.self;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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
	
	@Override
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

	@Override
	public int getTotal(String sql, Object... params) throws Exception {
		int total = 0;
		try {
			String csql = sqlParser.getSmartCountSql(sql);
			long start = System.currentTimeMillis();
			total = this.jdbcTemplate.queryForObject(csql, params, Integer.class);
			long end = System.currentTimeMillis();
			log.info("sqlCount-(" + (end-start) + "毫秒)" +csql);
		} catch (Exception e) {
			e.printStackTrace();
			total = 0;
		}
		return total;
	}

	/**
	 * 不计总数的分页查询记录
	 */
	@Override
	public List<Map<String, Object>> getPageDatas(String sql, int curPage,
			int pageSize) throws Exception {
		// 组装分页参数
		PagingParameter paging = new PagingParameter(curPage, pageSize);
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.getRecordDataWithoutTotal(sql,paging);
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sql);
		return list;
	}

	@Override
	public List<Map<String, Object>> getPageDatas(String sql, int curPage,
			int pageSize, Object... params) throws Exception {
		StringBuffer sb = new StringBuffer();
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ").append(pageSize).append(" offset ").append(start);
		long pstart = System.currentTimeMillis();
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sb.toString(), params);
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		return list;
	}

	@Override
	public <T> List<T> getPageDatas(String sql, int curPage, int pageSize,
			Class<T> clazz) throws Exception {
		
		return null;
	}

	@Override
	public <T> List<T> getPageDatas(String sql, int curPage, int pageSize,
			Class<T> clazz, Object... params) throws Exception {
		StringBuffer sb = new StringBuffer();
		int start = (curPage - 1) * pageSize;
		sb.append(sql).append(" limit ").append(pageSize).append(" offset ").append(start);
		long pstart = System.currentTimeMillis();
		List<T> list = this.jdbcTemplate.queryForList(sb.toString(), params, clazz);
		long pend = System.currentTimeMillis();
		log.info("sqlPage-(" + (pend-pstart) + "毫秒)" +sb.toString());
		return list;
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
		List<T> list = null;
		StringBuffer sqls = new StringBuffer(50);
		sqls.append("SELECT * FROM ");
		sqls.append(tableName);
		sqls.append(" where ");
		sqls.append(colName).append("='").append(colValue).append("'");
		list = this.queryForList(sqls.toString(),classz);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public <T> T queryDetail(String colValue) throws Exception {
		StringBuffer sqls = new StringBuffer(50);
		sqls.append("SELECT * FROM ");
		sqls.append(this.tableName);
		sqls.append(" where ");
		sqls.append(this.pkName).append("='").append(colValue).append("'");
		return queryObject(sqls.toString(), (Class<T>) Class
				.forName(this.beanClassName));
	}

	/**
	 * 根据SQL语句查询返回一条记录
	 */
	public <T> T queryObject(String sql, Class<T> classz) throws Exception {
		long _1 = System.currentTimeMillis();
		T t = this.jdbcTemplate.queryForObject(sql, new JcbkRowMapper<T>(
				classz));
		long _2 = System.currentTimeMillis();
		log.debug("Query Object("+(_2-_1)+"ms)-->"+ sql);
		return t;
	}

	public <T> T queryObject(String sql, Class<T> classz,Object... params) throws Exception {
		long _1 = System.currentTimeMillis();
		T t = this.jdbcTemplate.queryForObject(sql, new JcbkRowMapper<T>(
				classz),params);
		long _2 = System.currentTimeMillis();
		log.debug("Query Object("+(_2-_1)+"ms)-->"+ sql);
		return t;
	}

	/**
	 * 根据SQL语句查询返回列表
	 */
	public <T> List<T> queryList(String sql, Class<T> classz) throws Exception {
		
		return queryForList(jdbcTemplate, sql, classz);
	}
	public <T> List<T> queryList(String sql, Object[] args,Class<T> classz) throws Exception {

		return queryForList(jdbcTemplate, args,sql, classz);
	}

	public <T> List<T> queryList(String sql, Class<T> classz,Object ... params) throws Exception {

		return queryForList(jdbcTemplate, sql, classz,params);
	}

	/**
	 * 根据SQL语句查询返回一条记录
	 */
	public <T> T queryForObject(String sql, Class<T> classz) {
		T t = null;
		try {
			long _1 = System.currentTimeMillis();
			t = this.jdbcTemplate.queryForObject(sql, new JcbkRowMapper<T>(
					classz));
			long _2 = System.currentTimeMillis();
			log.debug("Query Object("+(_2-_1)+"ms)-->"+ sql);
		} catch (EmptyResultDataAccessException e) {			
			//e.printStackTrace();
		}
		return t;
	}
	public <T> T queryForObject(String sql, Object[] array,Class<T> classz) {
		T t = null;
		try {
			long _1 = System.currentTimeMillis();
			t = this.jdbcTemplate.queryForObject(sql, array,new JcbkRowMapper<T>(
					classz));
			long _2 = System.currentTimeMillis();
			log.debug("Query Object("+(_2-_1)+"ms)-->"+ sql);
		} catch (EmptyResultDataAccessException e) {
			//e.printStackTrace();
		}
		return t;
	}

	/**
	 * 根据SQL语句查询返回列表
	 */
	public <T> List<T> queryForList(String sql, Class<T> classz) {
		
		return queryForList(jdbcTemplate, sql, classz);
	}
	public <T> List<T> queryForList(String sql, Class<T> classz,Object[] array) {
		return queryForList(jdbcTemplate, sql, classz,array);
	}
	
	/**
	 * 根据SQL语句查询返回列表
	 */
	public <T> List<T> queryForList(String sql, Object args[],Class<T> classz) {
		long _1 = System.currentTimeMillis();
		List<T> list = this.jdbcTemplate.query(sql, args ,new JcbkRowMapper<T>(classz));
		long _2 = System.currentTimeMillis();
		log.debug("Query List("+(_2-_1)+"ms)-->"+ sql);
		return list;
	}
	
	/**
	 * 根据SQL语句查询返回列表
	 */
	public <T> List<T> queryForList(String sql, Object args[],Class<T> classz,JdbcTemplate jd) {
		long _1 = System.currentTimeMillis();
		List<T> list = jd.query(sql, args ,new JcbkRowMapper<T>(classz));
		long _2 = System.currentTimeMillis();
		log.debug("Query List("+(_2-_1)+"ms)-->"+ sql);
		return list;
	}

	/**
	 * 根据SQL语句查询返回列表
	 */
	public List queryForListWithText(String sql,JdbcTemplate jd,String search_text) {
		long _1 = System.currentTimeMillis();
		List list = jd.queryForList(sql,search_text);
		long _2 = System.currentTimeMillis();
		log.debug("Query List("+(_2-_1)+"ms)-->"+ sql);
		return list;
	}





	/**
	 * 根据SQL语句查询返回列表
	 */
	public <T> List<T> queryForList(JdbcTemplate jTemplate, String sql,
			Class<T> classz) {
		long _1 = System.currentTimeMillis();
		List<T> list = jTemplate.query(sql, new JcbkRowMapper<T>(classz));
		long _2 = System.currentTimeMillis();
		log.debug("Query List("+(_2-_1)+"ms)-->"+ sql);
		return list;
	}
	public <T> List<T> queryForList(JdbcTemplate jTemplate, Object[] args,String sql,
									Class<T> classz) {
		long _1 = System.currentTimeMillis();
		List<T> list = jTemplate.query(sql, args,new JcbkRowMapper<T>(classz));
		long _2 = System.currentTimeMillis();
		log.debug("Query List("+(_2-_1)+"ms)-->"+ sql);
		return list;
	}
	public <T> List<T> queryForList(JdbcTemplate jTemplate, String sql,
									Class<T> classz,Object ... params) {
		long _1 = System.currentTimeMillis();
		List<T> list = jTemplate.query(sql, new JcbkRowMapper<T>(classz),params);
		long _2 = System.currentTimeMillis();
		log.debug("Query List("+(_2-_1)+"ms)-->"+ sql);
		return list;
	}

	
	/**
	 * 查询总记录数
	 * @param sql    查询SQL
	 * @param limit  0=不限制查询数量
	 * @return
	 */
	public int getRecordCounts(String sql, int limit){
		
		return getRecordCounts(sql, jdbcTemplate, limit);
	}

	public int getRecordCounts(String sql, Object[] array,int limit){

		return getRecordCounts(sql, array,jdbcTemplate, limit);
	}

	public int getRecordCounts(String sql, int limit,Object ... params){

		return getRecordCounts(sql, jdbcTemplate, limit,params);
	}
	
	/**
	 * 查询总记录数
	 * @param jdt    外部数据源
	 * @param sql    查询SQL
	 * @param limit  0=不限制查询数量
	 * @return
	 */
	public int getRecordCounts(String sql,JdbcTemplate jdt, int limit){
		String countSql = PagingSqlBuilder.getCountSql(sql, limit);
		long _1 = System.currentTimeMillis();
		int records = jdt.queryForInt(countSql);
		long _2 = System.currentTimeMillis();
		log.debug("COUNT("+(_2-_1)+"ms)-->"+ countSql);
		return records;
	}
	public int getRecordCounts(String sql,Object array,JdbcTemplate jdt, int limit){
		String countSql = PagingSqlBuilder.getCountSql(sql,limit);
		long _1 = System.currentTimeMillis();
		int records = jdt.queryForInt(countSql,array);
		long _2 = System.currentTimeMillis();
		log.debug("COUNT("+(_2-_1)+"ms)-->"+ countSql);
		return records;
	}
	public int getRecordCounts(String sql,JdbcTemplate jdt, int limit,Object ... params){
		String countSql = PagingSqlBuilder.getCountSql(sql, limit);
		long _1 = System.currentTimeMillis();
		int records = jdt.queryForInt(countSql,params);
		long _2 = System.currentTimeMillis();
		log.debug("COUNT("+(_2-_1)+"ms)-->"+ countSql);
		return records;
	}
	/**
	 * 查询总记录数
	 * @param jdt    外部数据源
	 * @param sql    查询SQL
	 * @param limit  0=不限制查询数量
	 * @return
	 */
	
	/**
	 * 获取分页数据
	 * @param sql              查询SQL
	 * @param pagingParameter  pagingParameter=null则不分页 
	 * @return
	 */
	public List getRecordData(String sql, PagingParameter pagingParameter,JdbcTemplate jdt){
		String paginationSQL = PagingSqlBuilder.getPagingSql(sql, pagingParameter);
		long _1 = System.currentTimeMillis();
		List list = jdt.queryForList(paginationSQL);
		long _2 = System.currentTimeMillis();
		log.debug("PAGE("+(_2-_1)+"ms)-->"+ paginationSQL);
		return list;
	}
	public List getRecordData(String sql,Object[] array, PagingParameter pagingParameter,
							  JdbcTemplate jdt){
		String paginationSQL = PagingSqlBuilder.getPagingSql(sql, pagingParameter);
		long _1 = System.currentTimeMillis();
		List list = jdt.queryForList(paginationSQL,array);
		long _2 = System.currentTimeMillis();
		log.debug("PAGE("+(_2-_1)+"ms)-->"+ paginationSQL);
		return list;
	}
	
	/**
	 * 获取分页数据 不计总数
	 * @param sql              查询SQL
	 * @param pagingParameter  pagingParameter=null则不分页 
	 * @return
	 */
	public List getRecordDataWithoutTotal(String sql, PagingParameter pagingParameter,JdbcTemplate jdt){
		String paginationSQL = PagingSqlBuilder.getPagingSqlWithoutTotal(sql, pagingParameter);
		long _1 = System.currentTimeMillis();
		List list = jdt.queryForList(paginationSQL);
		long _2 = System.currentTimeMillis();
		log.debug("PAGE("+(_2-_1)+"ms)-->"+ paginationSQL);
		return list;
	}
	
	/**
	 * 获取分页数据
	 * @param sql              查询SQL
	 * @param pagingParameter  pagingParameter=null则不分页 
	 * @return
	 */
	public List getRecordData(String sql, PagingParameter pagingParameter){
		
		return getRecordData(sql, pagingParameter, jdbcTemplate);
	}
	public List getRecordData(String sql, Object[] array,PagingParameter pagingParameter){

		return getRecordData(sql, array,pagingParameter, jdbcTemplate);
	}
	
	/**
	 * 获取分页数据不计总数
	 * @param sql              查询SQL
	 * @param pagingParameter  pagingParameter=null则不分页 
	 * @return
	 */
	public List getRecordDataWithoutTotal(String sql, PagingParameter pagingParameter){
		
		return getRecordDataWithoutTotal(sql, pagingParameter, jdbcTemplate);
	}
	
	/**
	 * 获取分页数据
	 * @param <T>
	 * @param sql              查询SQL
	 * @param pagingParameter  pagingParameter=null则不分页 
	 * @param classz           CLASS
	 * @return
	 */
	public <T> List<T> getRecordData(String sql, PagingParameter pagingParameter, Class<T> classz){
		
		return getRecordData(sql, pagingParameter, classz, jdbcTemplate);
	}
	
	/**
	 * 获取分页数据
	 * @param <T>
	 * @param sql             查询SQL
	 * @param pagingParameter pagingParameter=null则不分页 
	 * @param classz          CLASS
	 * @param args            参数数组
	 * @return
	 */
	public <T> List<T> getRecordData(String sql, PagingParameter pagingParameter, Class<T> classz, Object...args){
		String paginationSQL = PagingSqlBuilder.getPagingSql(sql, pagingParameter);
		long _1 = System.currentTimeMillis();
		List<T> list =  queryForList(paginationSQL,args,classz);
		long _2 = System.currentTimeMillis();
		log.debug("PAGE("+(_2-_1)+"ms)-->"+ paginationSQL);
		return list;
	}
	
	/**
	 * 获取分页数据
	 * @param <T>
	 * @param sql
	 * @param pagingParameter
	 * @param classz
	 * @param jdbcTemplate
	 * @return
	 */
	public <T> List<T> getRecordData(String sql, PagingParameter pagingParameter, Class<T> classz, JdbcTemplate jdbcTemplate){
		String paginationSQL = PagingSqlBuilder.getPagingSql(sql, pagingParameter);
		long _1 = System.currentTimeMillis();
		List<T> list =  queryForList(jdbcTemplate,paginationSQL,classz);
		long _2 = System.currentTimeMillis();
		log.debug("PAGE("+(_2-_1)+"ms)-->"+ paginationSQL);
		return list;
	}
	public <T> List<T> getRecordData(String sql,Object array, PagingParameter pagingParameter,
									 Class<T> classz, JdbcTemplate jdbcTemplate){
		String paginationSQL = PagingSqlBuilder.getPagingSql(sql, pagingParameter);
		long _1 = System.currentTimeMillis();
		List<T> list =  queryForList(jdbcTemplate,paginationSQL,classz,array);
		long _2 = System.currentTimeMillis();
		log.debug("PAGE("+(_2-_1)+"ms)-->"+ paginationSQL);
		return list;
	}

	/**
	 * 分页,查询总记录限制为10000
	 * 原型返回
	 * @param sql            查询SQL
	 * @param curPage        当前页
	 * @param pageSize       页大小
	 */
	public Map<String, Object> findPageForMap(String sql, int curPage,
			int pageSize) {
		
		return getDatas(sql, curPage, pageSize, 10000, jdbcTemplate);
	}
	public Map<String, Object> findPageForMap(String sql, Object[] array,int curPage,
											  int pageSize) {

		return getDatas(sql,array, curPage, pageSize, 10000, jdbcTemplate);
	}

	/**
	 * 分页,查询总记录限制为2000
	 * 对象封装
	 * @param sql            查询SQL
	 * @param curPage        当前页
	 * @param pageSize       页大小
	 * @param classz         CLASS
	 */
	public Map<String, Object> findPageForMap(String sql, int curPage,
			int pageSize, Class<?> classz) {
		
		return getDatas(sql, curPage, pageSize, classz, 2000, jdbcTemplate);
	}
	public Map<String, Object> findPageForMap(String sql, Object[] array,int curPage,
											  int pageSize, Class<?> classz) {

		return getDatas(sql, array, curPage, pageSize, classz, 2000, jdbcTemplate);
	}

	/**
	 * 分页,查询总记录限制为2000
	 * 对象封装
	 * @param sql 查询SQL
	 * @param search_text 模糊查询字段
	 * @param curPage 当前页
	 * @param pageSize 页大小
	 * @return
	 */

	/**
	 *
	 * @param sql
	 * @param search_text
	 * @param curPage
	 * @param pageSize
	 * @param queryCount
	 * @param jd
	 * @return
	 */


	/**
	 * 获取分页数据
	 * @param sql
	 * @param pagingParameter
	 * @param jdbcTemplate
	 * @return
	 */

	
	/**
	 * 分页,查询总记录数限制为10000
	 * 对象封装
	 * @param sql            查询SQL
	 * @param curPage        当前页
	 * @param pageSize       页大小
	 * @param clazz          CLASS
	 */
	public Map<String, Object> findPageForMapNoLimit(String sql, int curPage,
			int pageSize, Class<?> clazz) {
		
		return getDatas(sql, curPage, pageSize, clazz, 10000, jdbcTemplate);
	}
	
	/**
	 * 分页,查询总记录不受限制
	 * @param sql            查询SQL
	 * @param curPage        当前页
	 * @param pageSize       页大小
	 */
	public Map<String, Object> findPageForMapNoLimit(String sql, int curPage,
			int pageSize) {
		
		return getDatas(sql, curPage, pageSize, 0, jdbcTemplate);
	}
	public Map<String, Object> findPageForMapNoLimit(String sql, Object[] array,int curPage,
													 int pageSize) {

		return getDatas(sql, curPage, pageSize, 0, jdbcTemplate);
	}

	/**
	 * 分页显示 对象封装
	 * @param sql            查询SQL
	 * @param curPage        当前页
	 * @param pageSize       页大小
	 * @param queryCount     查询记录数
	 * @param tableName      查询表名 (暂无用)
	 * @return
	 */
	public Map<String, Object> queryPageForMap(String sql, int curPage,
			int pageSize, Class<?> classz, int queryCount, String tableName) {
		// 查询总记录由方法传递进来
		int totalRows = queryCount;
		PagingParameter pagingParameter = new PagingParameter(curPage,pageSize,totalRows);
		List list = getRecordData(sql, pagingParameter, classz);
		return new DataStore(totalRows,list).getEntity();
	}
	
	/**
	 * 分页显示 对象封装
	 * @param sql            查询SQL
	 * @param curPage        当前页
	 * @param pageSize       页大小
	 * @param queryCount     查询记录数
	 * @param jd             数据源
	 * @param classz         CLASS
	 * @return
	 */
	public Map<String, Object> queryPageForMapByJdbc(String sql, int curPage, int pageSize, Class<?> classz, int queryCount, JdbcTemplate jd){
		int totalRows = queryCount;
		PagingParameter pagingParameter = new PagingParameter(curPage,pageSize,totalRows);
		List<?> list = getRecordData(sql, pagingParameter, classz, jd);
		return new DataStore(totalRows,list).getEntity();
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
		    int totalRows = 10000;
			PagingParameter pagingParameter = new PagingParameter(curPage,pageSize,totalRows);
			List<?> list = getRecordData(sql, pagingParameter, clazz, jd);
	return new DataStore(totalRows,list).getEntity();}

	public Map<String, Object> findPageForMapByJdbc(String sql, Object[] array,int curPage,
													int pageSize, Class<?> clazz,JdbcTemplate jd) {
		int totalRows = 10000;
		PagingParameter pagingParameter = new PagingParameter(curPage,pageSize,totalRows);
		List<?> list = getRecordData(sql, array,pagingParameter, clazz, jd);
		return new DataStore(totalRows,list).getEntity();}

	
	public Map<String, Object> queryForMap(String sql, int curPage,
			int pageSize, Class<?> classz, int queryCount) {
		
		return getDatas(sql, curPage, pageSize, classz, queryCount, jdbcTemplate);
	}
	public Map<String, Object> queryForMap(String sql,Object[] array, int curPage,
										   int pageSize, Class<?> classz, int queryCount) {

		return getDatas(sql, array,curPage, pageSize, classz, queryCount, jdbcTemplate);
	}

	public Map<String, Object> queryPageLimitTotal(String sql, int curPage,
			int pageSize, int queryCount, JdbcTemplate jTemplate,
			String orderStr) {
		
		return getDatas(sql, curPage, pageSize, queryCount, jdbcTemplate);
	}
	public Map<String, Object> queryPageLimitTotal(String sql, Object[] array,int curPage,
												   int pageSize, int queryCount, JdbcTemplate jTemplate,
												   String orderStr) {

		return getDatas(sql, curPage, pageSize, queryCount, jdbcTemplate);
	}



	
	public Date getDbNowDate() {
		String sql = "select sysdate from dual";
		return jdbcTemplate.queryForObject(sql, Date.class);
	}

	public <T> T queryForBeanByCondition(Class<T> clazz,Cnd cnd) throws Exception {
		PreSqlEntry entry = SqlUtils.getPreSelectSql(getTableName(), null,cnd);
		List<T> result = this.jdbcTemplate.query(entry.getSql(), entry
				.getValues().toArray(), new JcbkRowMapper<T>(clazz));
		if (result == null || result.size() == 0)
			return null;
		else
			return result.get(0);
	}

	public <T> T update(T entity, String keyName) throws ORMException {
		PreSqlEntry entry = SqlUtils.getPreUpdateSqlByObject(getTableName(),
				entity, new HashMap<String, String>(), keyName);
		this.jdbcTemplate.update(entry.getSql(), entry.getValues().toArray());
		return entity;
	}
	
	public <T> T updateJz(T entity, String keyName) throws Exception {
		PreSqlEntry entry = SqlUtils.getPreUpdateSqlByObject(getTableName(),
				entity, new HashMap<String, String>(), keyName);
		Object[] objArray = entry.getValues().toArray();
		this.jdbcTemplate.update(entry.getSql(), entry.getValues().toArray());
		return entity;
	}

	public Map<String, Object> findPageForMapObj(String sql, int curPage,
			int pageSize) {
		
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
		List list = this.jdbcTemplate.queryForList(String.format(sql, viewName,tableName,pkName),new Object[]{pk},Object.class);
		if (list != null && list.size() > 0) {
			 result = list.get(0);
		}else{
			result = pk;
		}
		return result;
	}
	
	public int delete(String whereSql) throws Exception {
		return this.jdbcTemplate.update("delete "+getTableName()+" where "+ whereSql);
	}
	
	public int delete(String tabname,String colname,String colValue)throws Exception{
		StringBuffer sb = new StringBuffer(" delete from ");
		sb.append(tabname)
		  .append(" where ").append(colname).append(" = ").append(colValue);
		return this.jdbcTemplate.update(sb.toString());
	}
	
	public Map<String, Object> findPageForMapNoLimitByJdbc(String sql, int curPage,
			int pageSize, Class<?> classz,JdbcTemplate jd) {
		
		return getDatas(sql, curPage, pageSize, classz, 0, jdbcTemplate);
	}
	public Map<String, Object> findPageForMapNoLimitByJdbc(String sql,Object[] array, int curPage,
														   int pageSize, Class<?> classz,JdbcTemplate jd) {

		return getDatas(sql, curPage, pageSize, classz, 0, jdbcTemplate);
	}
	
	private Map<String, Object> getDatas(String sql, int curPage, int pageSize, Class<?> classz, int queryCount, JdbcTemplate jd){
		int totalRows = getRecordCounts(sql, jd, queryCount);
		PagingParameter pagingParameter = new PagingParameter(curPage,pageSize,totalRows);
		List<?> list = getRecordData(sql, pagingParameter, classz, jd);
		return new DataStore(totalRows,list).getEntity();
	}
	private Map<String, Object> getDatas(String sql, Object array,int curPage, int pageSize,
										 Class<?> classz, int queryCount, JdbcTemplate jd){
		int totalRows = getRecordCounts(sql, jd, queryCount);
		PagingParameter pagingParameter = new PagingParameter(curPage,pageSize,totalRows);
		List<?> list = getRecordData(sql, array,pagingParameter, classz, jd);
		return new DataStore(totalRows,list).getEntity();
	}
	
	
	private Map<String, Object> getDatas(String sql, int curPage, int pageSize, int queryCount, JdbcTemplate jd){
		int totalRows = getRecordCounts(sql, jd, queryCount);
		PagingParameter pagingParameter = new PagingParameter(curPage,pageSize,totalRows);
		List list = getRecordData(sql, pagingParameter, jd);
		return new DataStore(totalRows,list).getEntity();
	}
	private Map<String, Object> getDatas(String sql, Object[] array,int curPage, int pageSize,
										 int queryCount,JdbcTemplate jd){
		int totalRows = getRecordCounts(sql, jd, queryCount,array);
		PagingParameter pagingParameter = new PagingParameter(curPage,pageSize,totalRows);
		List list = getRecordData(sql,array, pagingParameter, jd);
		return new DataStore(totalRows,list).getEntity();
	}
	
	public static class JcbkRowMapper<T> implements RowMapper<T> {

		private Class<T> requiredType;

		private Class<?>[] LongType = { Long.class };

		private Class<?>[] StringType = { String.class };

		private Class<?>[] DoubleType = { Double.class };
		
		private Class<?>[] ShortType = { Short.class };
		
		private Class<?>[] FloatType = { Float.class };
		
		private Class<?>[] BlobType = { Blob.class };

		private Class<?>[] IntType = { int.class };
		
		private static Logger log = LoggerFactory.getLogger(JcbkRowMapper.class);

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
						//TODO 写法无法适应驼峰命名，整理bean以后修改
						Field f = classObject.getDeclaredField(colName.toLowerCase());
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
								if("Cdbh".equals(colName)){
									//System.out.println("111");
								}
								colValue = rs.getObject(i);
								if(Integer.class.getName().equals(f.getType().getName())){
									colValue = ((Number)colValue).intValue();
									type = IntType;
								} else if (Long.class.getName().equals(f.getType().getName())){
									colValue = ((Number)colValue).longValue();
									type = LongType;
								}else if (Double.class.getName().equals(f.getType().getName())){
									colValue = ((Number)colValue).doubleValue();
									type = DoubleType;
								}else if (Short.class.getName().equals(f.getType().getName())){
									colValue = ((Number)colValue).shortValue();
									type = ShortType;
								}else if (Double.class.getName().equals(f.getType().getName())){
									colValue = ((Number)colValue).floatValue();
									type = FloatType;
								}else if(String.class.getName().equals(f.getType().getName())){
									if(((Number)colValue) == null)colValue=(String)null;
									else colValue = ((Number)colValue).toString();
									type = StringType;
								}else{
									log.error("无法判断number类型对应的java类型:"+f.getType().getName());
									throw new RuntimeException("无法判断number类型对应的java类型");
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
}
