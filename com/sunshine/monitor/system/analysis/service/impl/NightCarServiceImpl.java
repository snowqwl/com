package com.sunshine.monitor.system.analysis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sunshine.monitor.comm.bean.CarKey;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.comm.util.orm.helper.QueryCondition.Op;
import com.sunshine.monitor.system.analysis.Constant;
import com.sunshine.monitor.system.analysis.action.NightCarController.StartEndDate;
import com.sunshine.monitor.system.analysis.bean.NightCarTemp;
import com.sunshine.monitor.system.analysis.bean.NightCarTotal;
import com.sunshine.monitor.system.analysis.clickhouse.repository.ClickHouseRepos;
import com.sunshine.monitor.system.analysis.dao.IllegalZYKDao;
import com.sunshine.monitor.system.analysis.dao.NightCarTempDao;
import com.sunshine.monitor.system.analysis.service.NightCarService;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.query.dao.QueryListDao;

@Service("nightCarManager")
public class NightCarServiceImpl implements NightCarService { 
	//TODO 为了演示临时修改 应该为 SCS_TEMP_PREFIX
	public static final String tempTablePrefix = Constant.SCS_CACHE_PREFIX+"_nightCar";
	@Autowired
	@Qualifier("nightCarTempDao")
	private NightCarTempDao nightCarTempDao;
	
	@Autowired
	@Qualifier("queryListDao")
	private QueryListDao queryListDao;
	
	@Autowired
	@Qualifier("illegalZYKDao")
	private IllegalZYKDao illegalZYKDao;
	
	@Autowired
	@Qualifier("clickHouseRepos")
	private ClickHouseRepos clickHouseRepos;
	
	@Autowired
	@Qualifier("systemDao")
	private SystemDao systemDao;
	
	public Boolean computeNightCar(
			List<StartEndDate> searchDateList,String dayStart,String dayEnd,
			final String[] gateIds,final String groupId , final String sessionId)
			throws Exception {
		//整理数据
		Date now = new Date();
		Date nowDate = DateUtils.truncate(now, Calendar.DATE);
		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		List<Future<Boolean>> result = new ArrayList<Future<Boolean>>();
		for(StartEndDate seDate : searchDateList){
			computer(seDate, nowDate, dayStart,dayEnd, gateIds, groupId, 
					sessionId,threadPool,result);
		}
		for(Future<Boolean> callBack : result){
			callBack.get();
		}
		threadPool.shutdown();
		return true;
	}
	
	/**
	 * 计算一个开始结束时间
	 * @param result 
	 * @param threadPool 
	 */
	private void computer(StartEndDate seDate, Date nowDate,String dayStart, String dayEnd,
			final String[] gateIds,final String groupId , final String sessionId, 
			ExecutorService threadPool, List<Future<Boolean>> result
			){
		Date startTime = seDate.getStartTime();
		Date endTime = seDate.getEndTime();
		final Date startDate = DateUtils.truncate(startTime, Calendar.DATE);
		Date endDate = DateUtils.truncate(endTime, Calendar.DATE);
		Date maxDate = DateUtils.addDays(nowDate, -1);
		endDate = endDate.before(maxDate) ? endDate : maxDate;

		if (startDate.after(endTime) || startTime == endTime)
			throw new RuntimeException("结束时间必须大于开始时间");

		if (!startDate.before(nowDate))
			throw new RuntimeException("开始时间至少是当前时间的前一天");
		int dayNum =(int)( (endDate.getTime() - startDate.getTime() )/(24*60*60*1000)) + 1;
		final Calendar c = Calendar.getInstance(); 
		String[]dayStarts = dayStart.split(":");
		String[] dayEnds = dayEnd.split(":");
		for(int i = 0 ; i<dayNum ; i++){
			//计算结束时间
			c .setTime(startDate);
			c.add(Calendar.DATE, i);
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dayEnds[0]));
			c.set(Calendar.MINUTE, Integer.parseInt(dayEnds[1]));
			c.set(Calendar.SECOND, Integer.parseInt(dayEnds[2]));
			final Date dayEndDate = c.getTime();
			//计算开始时间
			c.add(Calendar.DATE, -1);
			c.add(Calendar.SECOND, 1);
			final Date dayStartDate = c.getTime();
			//计算白昼界限时间
			c.setTime(startDate);
			c.add(Calendar.DATE, i);
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dayStarts[0]));
			c.set(Calendar.MINUTE, Integer.parseInt(dayStarts[1]));
			c.set(Calendar.SECOND, Integer.parseInt(dayEnds[2]));
			final Date limitDate= c.getTime();
			Callable<Boolean> taskCarNoNotNull = new Callable<Boolean>() {
				public Boolean call() throws Exception {
					computeNightCarInOneDayCarNoNotNull(dayStartDate, dayEndDate , 
							limitDate, gateIds,groupId,sessionId);
					return true;
				}
			};
			Callable<Boolean> taskCarNoIsNull = new Callable<Boolean>() {
				public Boolean call() {
					try{
						computeNightCarInOneDayCarNoIsNull(dayStartDate, dayEndDate , 
								limitDate, gateIds,groupId,sessionId);
					}catch(Exception e){
						e.printStackTrace();
						return false;
					}
					return true;
				}
			};
			result.add(threadPool.submit(taskCarNoNotNull));
			result.add(threadPool.submit(taskCarNoIsNull));
		}
	}
	
	public String computeGroupId(String[] gateId, String dayStart, String dayEnd
			){
		Arrays.sort(gateId);
		StringBuilder sb = new StringBuilder(StringUtils.join(gateId));
		sb.append(dayStart).append("-")
		.append(dayEnd);
		return sb.toString();
	}
	
	private int computeNightCarInOneDayCarNoNotNull(
			Date startDate, Date endDate , 
			Date limitDate, String[] gateIds, 
			String groupId, String sessionId
			){
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formateDate = new SimpleDateFormat("yyyy-MM-dd");
		//TODO  insert into  %7$s_%6$s --> %7$s 临时修改， 并发操作不安全的
		String sql = "insert into  %7$s(groupId,dateStr,carDate,carNo,licenseColor,licenseType,passCount) " +
				"select '%5$s' as groupId,'%1$s|%4$s' as dateStr, '%8$s' as carDate," +
					"t.hphm as carNo ,t.hpys as licenseColor,t.hpzl as licenseType , count(t.gcxh) as passCount " +
				"from "+Constant.SCS_PASS_TABLE+" t where t.gcsj >= '%1$s' and t.gcsj <='%2$s' and (%3$s) " +
				"and t.hphm not in (" +Constant.SCS_NO_PLATE+") "+
				"group by t.hphm,t.hpzl,t.hpys having max(t.gcsj) < '%4$s'";
//		String sql = "create table if not exists %7$s_%6$s " +
//		"select '%5$s' as groupId,'%1$s|%2$s' as dateStr, '%8$s' as carDate," +
//			"t.hphm as carNo ,t.hpys as licenseColor,t.hpzl as licenseType , count(t.hphm) as passCount " +
//		"from "+Constant.SCS_PASS_TABLE+" t where t.gcsj >= '%1$s' and t.gcsj <='%2$s' and (%3$s) " +
//		"group by t.hphm,t.hpzl,t.hpys having max(t.gcsj) < '%4$s'";
//		String gates = "'"+StringUtils.join(gateIds, "','")+"'";
		StringBuilder sb = new StringBuilder();
		for(String gateId : gateIds){
			sb.append(" t.kdbh='").append(gateId).append("'").append(" or ");
		}
		sb.delete(sb.length()-4, sb.length());
		sql = String.format(sql, 
				formate.format(startDate), //1
				formate.format(endDate),//2
				sb.toString(),//3
//				gates,//3
				formate.format(limitDate) ,//4
				groupId,//5
				sessionId,//6
				tempTablePrefix,//7
				formateDate.format(endDate));//8
		return this.nightCarTempDao.update(sql);
	}
	
	private void computeNightCarInOneDayCarNoIsNull(
			Date startDate, Date endDate , 
			Date limitDate, String[] gateIds, 
			String groupId, String sessionId
			) throws Exception{
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formateDate = new SimpleDateFormat("yyyy-MM-dd");
		String selectSql = "select t.hphm as carNo ,t.hpys as licenseColor,t.hpzl as licenseType , count(t.gcxh) as passCount " +
		"from t("+Constant.SCS_PASS_TABLE+"::t.gcsj >= '%1$s' and t.gcsj <='%2$s' and (%3$s) " +
		"and t.hphm in (" +Constant.SCS_NO_PLATE+") ) "+
		"group t.hphm,t.hpzl,t.hpys having max(t.gcsj) < '%4$s' ";
		
		StringBuilder sb = new StringBuilder();
		for(String gateId : gateIds){
			sb.append(" t.kdbh='").append(gateId).append("'").append(" or ");
		}
		sb.delete(sb.length()-4, sb.length());
		selectSql = String.format(selectSql, 
				formate.format(startDate), //1
				formate.format(endDate),//2
				sb.toString(),//3
				formate.format(limitDate) );//4
		List<NightCarTemp> nullCarNos = this.nightCarTempDao.queryForList(selectSql, NightCarTemp.class);
		//TODO 并发不安全的修改 %1$s_%2$s --> %1$s
		String insertFormat = "insert into %1$s " +
		" (groupId,dateStr,carDate,carNo,licenseColor,licenseType,passCount) " +
		" values ('%3$s','%4$s|%11$s','%6$s','%7$s','%8$s','%9$s','%10$s')";
		if(nullCarNos!=null){
			for(NightCarTemp temp : nullCarNos){
				temp.setCarDate(formateDate.format(endDate));
				temp.setDateStr(formate.format(startDate)+"|"+formate.format(endDate));
				temp.setGroupId(groupId);
				this.nightCarTempDao.update(String.format(insertFormat, 
						tempTablePrefix,//1
						sessionId,//2
						groupId,//3
						formate.format(startDate), //4
						formate.format(endDate),//5
						formateDate.format(endDate),//6
						temp.getCarNo(),//7
						temp.getLicenseColor(),//8
						temp.getLicenseType(),//9
						temp.getPassCount(),//10
						formate.format(limitDate)//11
					));
			}
		}
	}

	public List<NightCarTemp> nightCarTempList(String sessionId,String groupId, Cnd cnd,
			Pager pager) throws Exception {
		if(cnd == null) cnd = Cnd.where();
		cnd.and("groupId",Op.EQ, groupId);
		//TODO 为了演示临时修改  此修改不同session会冲突
//		return nightCarTempDao.query(tempTablePrefix+"_"+sessionId, NightCarTemp.class, cnd, pager);
		return nightCarTempDao.query(tempTablePrefix, NightCarTemp.class, cnd, pager);
	}



	public List<NightCarTotal> nightCarTempTotalList(String sessionId, 
			String groupId, String startTime, String endTime, Pager pager, Integer thresholds, String kdbhs) throws Exception {
		//List<NightCarTotal> nightCarTotalList = nightCarTempDao.nightCarTempTotalList(sessionId, groupId, pager,startTime,endTime,thresholds);
		List<NightCarTotal> nightCarTotalList = clickHouseRepos.nightCarTempTotalList(sessionId, groupId, pager,startTime,endTime,thresholds,kdbhs);
		List<CarKey> carKeyList = new ArrayList<CarKey>();
		for(NightCarTotal nightCarTotal : nightCarTotalList){
			CarKey carKey = new CarKey(nightCarTotal.getCarNo(),nightCarTotal.getLicenseType());
			carKeyList.add(carKey);
		}
		Map<CarKey, Integer> trafficCountMap = new HashMap<>();
		try {
			//trafficCountMap = illegalZYKDao.getViolationCount(carKeyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(NightCarTotal nightCarTotal : nightCarTotalList){
			CarKey carKey = new CarKey(nightCarTotal.getCarNo(),nightCarTotal.getLicenseType());
			Integer count = trafficCountMap.get(carKey);
			nightCarTotal.setViolationCount(count==null?"0": count.toString());
			nightCarTotal.setLicenseColorMc(this.systemDao.getCodeValue("031001",nightCarTotal.getLicenseColor()));
			nightCarTotal.setLicenseTypeMc(this.systemDao.getCodeValue("030107",nightCarTotal.getLicenseType()));
		}
		
		return nightCarTotalList;
	}
	
	//TODO 临时的方法  重复的代码
	public List<NightCarTotal> nightCarTempTotalList(String sessionId, 
			String groupId, Date[] times, Pager pager, Integer thresholds) {
		List<NightCarTotal> nightCarTotalList = nightCarTempDao
				.nightCarTempTotalList(sessionId, groupId, pager,
						times,thresholds);
		List<CarKey> carKeyList = new ArrayList<CarKey>();
		for(NightCarTotal nightCarTotal : nightCarTotalList){
			CarKey carKey = new CarKey(nightCarTotal.getCarNo(),nightCarTotal.getLicenseType());
			carKeyList.add(carKey);
		}
		Map<CarKey, Integer> trafficCountMap = null;
		try {
			//trafficCountMap = queryListDao.getViolationCount(carKeyList);
			trafficCountMap = illegalZYKDao.getViolationCount(carKeyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(trafficCountMap != null && trafficCountMap.size() > 0)
			for(NightCarTotal nightCarTotal : nightCarTotalList){
				CarKey carKey = new CarKey(nightCarTotal.getCarNo(),nightCarTotal.getLicenseType());
				Integer count = trafficCountMap.get(carKey);
				nightCarTotal.setViolationCount(count==null?"0": count.toString());
			}
		return nightCarTotalList;
	}

	public void createTempTable(String sessionId) {
		//TODO 为了演示临时修改 此修改不同session会冲突
//		nightCarTempDao.createTable(NightCarTemp.class, tempTablePrefix+"_"+sessionId);
		nightCarTempDao.createTable(NightCarTemp.class, tempTablePrefix);
	}

	public Map<String, List<Date>> getAllGroupIdCarDate() throws Exception {
		List<NightCarTemp> nightCarTempList = nightCarTempDao.nightCarTempListShowByFieldDistinct(new String[]{"groupId","carDate"});
		Map<String, List<Date>> map = new HashMap<String, List<Date>> ();
		SimpleDateFormat formateDate = new SimpleDateFormat("yyyy-MM-dd");
		for(NightCarTemp temp : nightCarTempList){
			List<Date> computeDates = map.get(temp.getGroupId());
			if(computeDates == null) {
				computeDates = new ArrayList<Date>();
				map.put(temp.getGroupId(), computeDates);
			}
			computeDates.add(formateDate.parse(temp.getCarDate()));
		}
		
		return map;
	}

	
}
