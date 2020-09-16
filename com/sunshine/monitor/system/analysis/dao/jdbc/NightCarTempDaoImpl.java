package com.sunshine.monitor.system.analysis.dao.jdbc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sunshine.monitor.comm.dao.jdbc.ScsBaseDaoImpl;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.system.analysis.bean.NightCarTemp;
import com.sunshine.monitor.system.analysis.bean.NightCarTotal;
import com.sunshine.monitor.system.analysis.dao.NightCarTempDao;
import com.sunshine.monitor.system.analysis.service.impl.NightCarServiceImpl;

@Repository("nightCarTempDao")
public class NightCarTempDaoImpl extends ScsBaseDaoImpl implements NightCarTempDao {
	private static Map<String,Map<String,Integer>> cache = new HashMap<String,Map<String,Integer>>();
	public List<NightCarTotal> nightCarTempTotalList(String sessionId,String groupId,
			Pager pager, String startTime, String endTime,  Integer thresholds) {
		//TODO 为了演示临时修改
//		String tableName = NightCarServiceImpl.tempTablePrefix+"_"+sessionId;
		String tableName = NightCarServiceImpl.tempTablePrefix;
		String selectData = "select t.carNo,t.licenseColor, t.licenseType,count(t.carNo) as totalDay " +
				"from t("+tableName+"::t.groupId=? and t.carDate >= ? and t.carDate <=? ) " +
				"group t.carNo,t.licenseColor,t.licenseType  " +
				"having count(t.carNo) >= "+thresholds +
				" order count(t.carNo) :desc " +
				" limit "+(pager.getStartRow()-1)+","+pager.getPageSize();
		String insertTotal = "create table %1$s select count(t.carNo) as total from "+tableName+
			" t where t.groupId=? and t.carDate >= ? and t.carDate <=?  " +
			" group by t.carNo,t.licenseColor,t.licenseType having count(t.carNo)>="+thresholds;
		Map<String, Integer> totalCache = cache.get(sessionId);
		Integer total = null;
		if(totalCache == null) {
			totalCache =  new HashMap<String,Integer>();
			cache.put(sessionId,totalCache);
		} else {
			total = totalCache.get(insertTotal);
		}
//		total = 1000;
		if(total == null){
			final String tempTotalTable = NightCarServiceImpl.tempTablePrefix+"_"+UUID.randomUUID().toString().replace("-", "_");
			this.jdbcScsTemplate.update(String.format(insertTotal, tempTotalTable), new String[]{groupId,startTime,endTime});
			String selectTotal = "select count(t.*) from t("+tempTotalTable+")";
			String totalStr = this.jdbcScsTemplate.queryForObject(selectTotal, String.class);
			total = Integer.parseInt(totalStr);
			synchronized (totalCache) {
				totalCache.clear();
				totalCache.put(insertTotal, total);
			}
			final NightCarTempDao dao = this;
			new Thread(){
				public void run() {
					try {
						dao.dropTable(tempTotalTable);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
		pager.setPageCount(total);
		
		return this.jdbcScsTemplate.query(selectData, 
				new String[]{groupId,startTime,endTime}, new JcbkScsRowMapper<NightCarTotal>(NightCarTotal.class));
	}
	//TODO 临时修改方法，重复代码。。。
	public List<NightCarTotal> nightCarTempTotalList(String sessionId,String groupId,
			Pager pager, Date[] dates,  Integer thresholds) {
		//TODO 为了演示临时修改
//		String tableName = NightCarServiceImpl.tempTablePrefix+"_"+sessionId;
		String tableName = NightCarServiceImpl.tempTablePrefix;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder whereDateStr = new StringBuilder("(");
		for(Date cDate : dates){
			whereDateStr.append("t.carDate='"+dateFormat.format(cDate)+"' or ");
		}
		whereDateStr.delete(whereDateStr.length()-3, whereDateStr.length());
		whereDateStr.append(")");
		String selectData = "select t.carNo,t.licenseColor, t.licenseType,count(t.carNo) as totalDay " +
				"from t("+tableName+"::t.groupId=? and "+whereDateStr+" ) " +
				"group t.carNo,t.licenseColor,t.licenseType  " +
				"having count(t.carNo) >= "+thresholds +
				" order count(t.carNo) :desc " +
				" limit "+(pager.getStartRow()-1)+","+pager.getPageSize();
		String insertTotal = "create table %1$s select count(t.carNo) as total from "+tableName+
			" t where t.groupId=? and "+whereDateStr+"  " +
			" group by t.carNo,t.licenseColor,t.licenseType having count(t.carNo)>="+thresholds;
		Map<String, Integer> totalCache = cache.get(sessionId);
		Integer total = null;
		if(totalCache == null) {
			totalCache =  new HashMap<String,Integer>();
			cache.put(sessionId,totalCache);
		} else {
			total = totalCache.get(insertTotal);
		}
//		total = 1000;
		if(total == null){
			final String tempTotalTable = NightCarServiceImpl.tempTablePrefix+"_"+UUID.randomUUID().toString().replace("-", "_");
			this.jdbcScsTemplate.update(String.format(insertTotal, tempTotalTable), new String[]{groupId});
			String selectTotal = "select count(t.*) from t("+tempTotalTable+")";
			String totalStr = this.jdbcScsTemplate.queryForObject(selectTotal, String.class);
			total = Integer.parseInt(totalStr);
			synchronized (totalCache) {
				totalCache.clear();
				totalCache.put(insertTotal, total);
			}
			final NightCarTempDao dao = this;
			new Thread(){
				public void run() {
					try {
						dao.dropTable(tempTotalTable);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
		pager.setPageCount(total);
		
		return this.jdbcScsTemplate.query(selectData, 
				new String[]{groupId}, new JcbkScsRowMapper<NightCarTotal>(NightCarTotal.class));
	}
	
	public List<NightCarTemp> nightCarTempListShowByFieldDistinct(
			String[] fields) {
		String selectStr = "t."+StringUtils.join(fields, ",t.");
		
		String groupByStr = " group " +selectStr;
		
		String sql = "select " + selectStr + " from t(ygntcache_nightCar) "+groupByStr;
		
		return this.jdbcScsTemplate.query(sql, new JcbkScsRowMapper<NightCarTemp>(NightCarTemp.class));
	}
}
