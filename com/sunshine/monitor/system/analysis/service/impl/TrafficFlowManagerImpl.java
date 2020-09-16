package com.sunshine.monitor.system.analysis.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunshine.core.util.DateUtil;
import com.sunshine.monitor.comm.util.SimpleSqlJoin;
import com.sunshine.monitor.system.analysis.bean.TrafficFlow;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
import com.sunshine.monitor.system.analysis.dao.TrafficFlowDao;
import com.sunshine.monitor.system.analysis.service.TrafficFlowManager;

@Transactional
@Service("trafficFlowManager")
public class TrafficFlowManagerImpl implements TrafficFlowManager {

	@Autowired
	@Qualifier("TrafficFlowJdbcDao")
	private TrafficFlowDao trafficFlowJdbcDao;
	
	@Autowired
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos clickHouseRepos;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private String tableName = "jm_veh_passrec_flow";

	/**
	 * 抽取每个月数据建立临时表，再基于临时做流量分析
	 */
	public <T> List<List<? extends Object>> queryTotalFlowPeerDay(
			TrafficFlow trafficFlow) throws Exception {
		List<List<? extends Object>> list = null;
		ExecutorService service = null;
		try {
			List<? extends Object> curentList = null;
			List<? extends Object> relativeList = null;
			service = Executors.newFixedThreadPool(2);
			
			SimpleSqlJoin sql3 = getSql(trafficFlow, trafficFlow.getKssj(), 'M');
			Object[] args3 = sql3.getArgs();
			Future f1 = service.submit(new ResultCall<List>(tableName, sql3.toString(), args3));
			
			TrafficFlow cTrafficFlow = (TrafficFlow)trafficFlow.clone();
			cTrafficFlow.setKssj(DateUtil.countBeforeMonthDay(trafficFlow.getKssj()));
			cTrafficFlow.setJssj(DateUtil.countBeforeMonthDay(trafficFlow.getJssj()));
			SimpleSqlJoin sql4 = getSql(cTrafficFlow, cTrafficFlow.getJssj(), 'M');
			Object[] args4 = sql4.getArgs();
			Future f2 = service.submit(new ResultCall<List>(tableName, sql4.toString(), args4));
			
			long start3 = System.currentTimeMillis();
			curentList = (List)f1.get();
			long end3 = System.currentTimeMillis();
			log.info("YP-LLFX-M(" + (end3 - start3) + ")-" + sql3.toString() );

			long start4 = System.currentTimeMillis();
			relativeList = (List)f2.get();
			long end4 = System.currentTimeMillis();
			log.info("YP-LLFX-M(" + (end4 - start4) + ")-" + sql4.toString() );

			// 保存容器
			list = new ArrayList<List<? extends Object>>();
			list.add(curentList);
			list.add(relativeList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage() + "-cause:" + e.getCause());
		} finally{
			service.shutdown();
		}
		return list;
	}
	
	/**
	 * 线程查询流量天数据
	 * @author oy 2016/08/03
	 * @param <T>
	 */
	class ResultCall<T> implements Callable<T>{
		private String s1 ;
		private String s2 ;
		private Object[] args ;
		public ResultCall(String s1, String s2, Object...args){
			this.s1 = s1 ;
			this.s2 = s2 ;
			this.args = args;
		}
		@Override
		public T call() throws Exception {
			//return (T)trafficFlowJdbcDao.queryTotalFlowPeerDay(s1, s2, args);
			return (T)clickHouseRepos.queryTotalFlowPeerDay(s1, s2, args);
		}
	}	
	
	public void createTableIndex(String tableName,String ...fields){
		for(String field : fields){
			StringBuffer buffer = new StringBuffer(tableName);
			buffer.append("_");
			buffer.append(field);
			buffer.append("_Idx");
			this.trafficFlowJdbcDao.createTableIdx(tableName, buffer.toString(), field);
		}
	}
	
	public void deleteTable(String tableName) {

		trafficFlowJdbcDao.deleteTempTable(tableName);
	}

	
	public boolean isExstisTable(String name) throws Exception {
		
		return trafficFlowJdbcDao.isExstisTable(name);
	}

	public int copyDatasTotempTable(String condition, String tableName,
			Object[] args) {
		
		return this.trafficFlowJdbcDao.copyDatasTotempTable(condition, tableName, args);
	}
	
	/**
	 * 线程查询流量小时数据
	 * @author oy 2016/08/03
	 * @param <T>
	 */
	class HResultCall<T> implements Callable<T>{
		private String s1 ;
		private String s2 ;
		private Object[] args ;
		public HResultCall(String s1, String s2, Object...args){
			this.s1 = s1 ;
			this.s2 = s2 ;
			this.args = args;
		}
		@Override
		public T call() throws Exception {
			//return (T) trafficFlowJdbcDao.queryTotalFlowPeerHour(s1, s2, args);
			return (T) clickHouseRepos.queryTotalFlowPeerHour(s1, s2, args);
		}
	}	
	
	public <T> List<? extends Object> queryFlowPeerHour(TrafficFlow trafficFlow)
			throws Exception {
		List list = null;
		ExecutorService service = null;
		try {
			list = new ArrayList();
			TrafficFlow cTrafficFlow = (TrafficFlow)trafficFlow.clone();
			cTrafficFlow.setKssj(DateUtil.countBeforeDay(trafficFlow.getKssj()));
			
			SimpleSqlJoin sql1 = getSql(cTrafficFlow, cTrafficFlow.getKssj(), 'H');
			service = Executors.newFixedThreadPool(2);
			long start1 = System.currentTimeMillis();
			Future f1 = service.submit(new HResultCall<List>(tableName, sql1.toString(), sql1.getArgs()));
			List curentList  = (List)f1.get();
			long end1 = System.currentTimeMillis();
			log.info("YP-LLFX-H(" + (end1 - start1) + ")-" + sql1.toString() );
			
			SimpleSqlJoin sql2 = getSql(cTrafficFlow, cTrafficFlow.getJssj(), 'H');
			Future f2 = service.submit(new HResultCall<List>(tableName, sql2.toString(), sql2.getArgs()));
			List relativeList  = (List)f2.get();
			long end2 = System.currentTimeMillis();
			log.info("YP-LLFX-H(" + (end2 - end1) + ")-" + sql2.toString() );
			
			Collections.addAll(list, curentList.toArray());
			Collections.addAll(list, relativeList.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage() + "-cause:" + e.getCause());
		} finally{
			service.shutdown();
		}
		return list;
	}
	
	
	/**
	 * 条件SQL
	 * @param trafficFlow
	 * @param flag
	 * @return
	 */
	private SimpleSqlJoin getSql(TrafficFlow trafficFlow, String tjsj, char fs) throws Exception {
		SimpleSqlJoin sql = new SimpleSqlJoin();
		if(tjsj == null || "".equals(tjsj))
			throw new IllegalArgumentException("非法参数!");
		String[] ymd = tjsj.split("-");
		sql.Add("toYear(gcsj) = ?", "", Integer.valueOf(ymd[0]));
		sql.Add("toMonth(gcsj) = ?", "and", Integer.valueOf(ymd[1]));
		if(fs=='H'){
			sql.Add("toDayOfMonth(gcsj) = ?", "and", Integer.valueOf(ymd[2]));
		} 
		if (!trafficFlow.displayType()) {
			sql.Add("kkbh = ?", "and", trafficFlow.getDisplayType());
		} else {
			if (!StringUtils.isBlank(trafficFlow.getKdbh())) {
				String[] kdbhs = trafficFlow.getKdbh().split(",");
				if (kdbhs.length > 0) {
					StringBuffer subSql = new StringBuffer();
					for (int i = 0; i < kdbhs.length; i++) {
						subSql.append("'").append(kdbhs[i]).append("',");
					}
					int last = subSql.lastIndexOf(",");
					subSql.deleteCharAt(last);
					sql.AddMoreAndRep(" kkbh in(%s)", subSql.toString(), "and",
							null);
				}
			}
		}
		if (!StringUtils.isBlank(trafficFlow.getHpys()))
			sql.Add("hpys = ?", "and", trafficFlow.getHpys());
		if (!StringUtils.isBlank(trafficFlow.getCllx()))
			sql.Add("cllx = ?", "and", trafficFlow.getCllx());
		if (!StringUtils.isBlank(trafficFlow.getCsys()))
			sql.Add("csys = ?", "and", trafficFlow.getCsys());
		return sql;
	}
}
