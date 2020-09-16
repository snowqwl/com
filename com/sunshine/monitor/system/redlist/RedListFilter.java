package com.sunshine.monitor.system.redlist;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.stereotype.Component;

import com.sunshine.monitor.system.redlist.util.SqlUtil;

/**
 * 红名单过滤
 * 
 * @author OUYANG 2013/7/25
 * @version V1.0.6
 */
@Component
@Aspect
public class RedListFilter {

	private Logger logger = LoggerFactory.getLogger(RedListFilter.class);

	@Autowired
	@Qualifier("jdbcTemplate")
	public JdbcTemplate jdbcTemplate;

	private static final String HPHM_STRING = "HPHM";

	private static final String extPrefix = " select * from ( ";

	private static final String extSuffix = " ) where ";

	/**
	 * 顺序
	 * 
	 * @return
	 */
	public int getOrder() {
		return 1;
	}

	/**
	 * 分页过滤,含有"Page"的方法
	 * @see com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl
	 */
	@Pointcut("execution(* com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl.*Page*(..))")
	public void redlistSqlpCut() {

	}

	/**
	 * 总数过滤,过滤方法getRecordCounts
	 * @see com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl
	 */
	@Pointcut("execution(* com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl.getRecordCounts(..))")
	public void redlistCountSqlCut() {
		
	}
	
	/**
	 * 单分页查询过滤,过滤方法getRecordData
	 * @see com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl
	 */
	@Pointcut("execution(* com.sunshine.monitor.comm.dao.jdbc.BaseDaoImpl.getRecordData(..))")
	public void redlistPagingSqlCut() {
		
	}
	
	
	/**
	 * 被拦截方法的第一个参数为SQL语句 系统不再自动验证查询的语句是否含有号牌号码,需要过滤红名单,则在调用的方法前加this.getSelf()
	 * 形式如下: this.getSelf().queryPageForMap(Args)
	 * @param jp  切点对象
	 * @return
	 * @throws Throwable
	 */
	@Around("redlistSqlpCut()")
	public Object doFilterPagingSql(ProceedingJoinPoint jp) throws Throwable {
		
		return getChangeSql(jp);
	}

	/**
	 * 总记录数
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	@Around("redlistCountSqlCut()")
	public Object doFilterCountSql(ProceedingJoinPoint jp) throws Throwable {
		
		return getChangeSql(jp);
	}
	
	/**
	 * 单分页数据
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	@Around("redlistPagingSqlCut()")
	public Object doFilterSinglePageSql(ProceedingJoinPoint jp) throws Throwable {
		
		return getChangeSql(jp);
	}
	
	/**
	 * 加入红名单过滤
	 * @param args
	 * @return
	 */
	private Object getChangeSql(JoinPoint jp) throws Throwable{
		Object[] args =  jp.getArgs();
		Object retObj = null;
		String prSql = "";
		if (args != null && args.length > 0) {
			/* 系统不再自动验证 */
			// boolean flag = checkExitsHphm(args); 
			// 存在号牌号码字段 
			// if (flag)
			     // 第一个参数为SQL
				 String sql = (String) args[0];
				 prSql = new StringBuffer(extPrefix)
					.append(sql)
					.append(
							" ) ywb where not exists(select rdn.hphm from jm_red_namelist rdn where ywb.hphm=rdn.hphm and rdn.status='1' and rdn.isvalid='1') ")
					.toString();
		}
		if (!StringUtils.isBlank(prSql)) {
			logger.debug(jp+"--"+prSql);
			args[0] = prSql;
			retObj = ((ProceedingJoinPoint)jp).proceed(args);
		} else {
			retObj = ((ProceedingJoinPoint)jp).proceed();
		}
		return retObj;
	}
	
	/**
	 * 如果目标方法含有JdbcTemplate对象，则使用方法参数的JdbcTemplate对象,否则使用默认(注入)
	 * 
	 * @param args
	 * @return
	 */
	private JdbcTemplate getJdbcTemplate(Object[] args) {
		JdbcTemplate temp = this.jdbcTemplate;
		for (Object o : args) {
			if (o instanceof JdbcTemplate) {
				temp = (JdbcTemplate) o;
			}
		}
		return temp;
	}

	/**
	 * 检查查询中是否存在号牌号码字段
	 * 
	 * @param args
	 * @return
	 */
	private boolean checkExitsHphm(Object... args) {
		String sql = (String) args[0];
		/* 过滤排序 */
		String count_sql = SqlUtil.filterOrderbyStrOfSql(sql);
		String exSql = new StringBuffer(extPrefix).append(count_sql.toString())
				.append(extSuffix).append(" rownum = 1").toString();
		// 是否存在号牌号码字段
		JdbcTemplate jTemplate = getJdbcTemplate(args);
		RowCountCallbackHandler rcch = new RowCountCallbackHandler();
		jTemplate.query(exSql, rcch);
		String coloumn[] = rcch.getColumnNames();
		// 查询有数据
		if (coloumn != null && coloumn.length > 0) {
			List<String> list = Arrays.asList(coloumn);
			if (list.contains(HPHM_STRING)) {
				return true;
			}
		}
		return false;
	}
}
