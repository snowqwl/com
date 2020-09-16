package com.sunshine.monitor.system.analysis.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunshine.monitor.comm.bean.MapEntry;
import com.sunshine.monitor.comm.util.orm.helper.Cnd;
import com.sunshine.monitor.comm.util.orm.helper.ConditionGroup;
import com.sunshine.monitor.comm.util.orm.helper.Pager;
import com.sunshine.monitor.comm.util.orm.helper.QueryCondition.Op;
import com.sunshine.monitor.system.analysis.action.NightCarController.DateComparator;
import com.sunshine.monitor.system.analysis.action.NightCarController.StartEndDate;
import com.sunshine.monitor.system.analysis.bean.NightCarTemp;
import com.sunshine.monitor.system.analysis.bean.NightCarTotal;
import com.sunshine.monitor.system.analysis.service.impl.NightCarServiceImpl;

@Controller
@RequestMapping(value = "/nightCar.do", params = "method")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
public class NightCarController {
	//TODO 为了演示 这里暂时开放权限 private --> public
	public static String NIGHTCAR_ATTR = "NIGHTCAR_ATTR";
	@Autowired
	@Qualifier("nightCarManager")
	private  NightCarServiceImpl nightCarManager;
	
	@RequestMapping
	public String forwardMain(){
		return "analysis/NightCar";
	}
	
	@ResponseBody
	@RequestMapping
	public Map<String,Object> specificDateCompute (
			HttpServletRequest request , HttpServletResponse response,
			String kdbhs , String timeStr , String dayStart, 
			String dayEnd,
			Integer rows,
			Integer page,
			Integer thresholds)  throws Exception{
		String[] timeStrs =  timeStr.split(",");
		String[] gateIds = kdbhs.split(",");
		String groupId = nightCarManager.computeGroupId(gateIds,dayStart,dayEnd);
		List<Date> searchDates = new ArrayList<Date>();
		for(String timestr : timeStrs){ 
			String startTimeStr = timestr;
			String endTimeStr = timestr;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date startTime = format.parse(startTimeStr.trim());
			searchDates.add(startTime);
			Date endTime = format.parse(endTimeStr.trim());
			String id="";
			Map<String,DateCache> obj = (Map<String,DateCache>) request.getSession()
					.getServletContext().getAttribute(NIGHTCAR_ATTR);
			String sessionId = request.getSession().getId();
			DateCache dateCache = null;
			if(obj!=null){
				dateCache = obj.get(groupId);
			} else {
				synchronized (request.getSession()) {
					//为了演示临时修改 从 session 提升到 servletContext 是不安全的
					obj = (Map<String, DateCache>) request.getSession().getServletContext()
							.getAttribute(NIGHTCAR_ATTR);
					if (obj == null) {
						obj = new HashMap<String, DateCache>();
						request.getSession().getServletContext().setAttribute(NIGHTCAR_ATTR, obj);
						try {
							// 第一次进入 创建表
							nightCarManager.createTempTable(sessionId);
						} catch (Throwable t) {
							request.getSession().getServletContext().setAttribute(NIGHTCAR_ATTR, null);
							throw new RuntimeException(t);
						}
					}
				}
			}
			if(dateCache == null){
				dateCache = new DateCacheInMen(Calendar.DATE);
				obj.put(groupId, dateCache);
			}
			//临时修改 非session 
			List<StartEndDate> searchDateList = dateCache.noCacheDate(groupId, startTime, endTime);
			
			if(searchDateList == null ){
				id = String.format("{groupId:'%1$s'}",groupId);
			}else{
			  nightCarManager.computeNightCar(searchDateList,dayStart,dayEnd
					, gateIds ,groupId,sessionId);
			  dateCache.complate();
			    id = String.format("{groupId:'%1$s'}",groupId);
			}
			if(page == null) page=1;
			if(thresholds == null) thresholds = 1;
		}
		
		try {
			Pager pager = new Pager(page,rows);
			 List<NightCarTotal> data = nightCarManager.nightCarTempTotalList(
					request.getSession().getId(), 
					groupId, searchDates.toArray(new Date[searchDates.size()]), pager,
					1); 
			 Map<String,Object> map = new HashMap<String,Object>(); 
			 map.put("rows", data);
			 map.put("total", pager.getPageCount());
			 map.put("groupId",groupId);
			 return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	//更换数据库，更换天运星的写法，去除创建临时表
	@ResponseBody
	@RequestMapping
	public Map<String,Object> compute (
			HttpServletRequest request , HttpServletResponse response,
			String kdbhs , String startTimeStr , 
			String endTimeStr , String dayStart, 
			String dayEnd,
			Integer rows,
			Integer page,
			Integer thresholds)  throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = format.parse(startTimeStr.trim());
		Date endTime = format.parse(endTimeStr.trim());
		String[] gateIds = kdbhs.split(",");
		String groupId = nightCarManager.computeGroupId(gateIds,dayStart,dayEnd);
		String id="";
		Map<String,DateCache> obj = (Map<String,DateCache>) request.getSession()
				.getServletContext().getAttribute(NIGHTCAR_ATTR);
		String sessionId = request.getSession().getId();
		DateCache dateCache = null;
		if (obj != null) {
			dateCache = obj.get(groupId);
		} else {
			obj = new HashMap<>();
		}
		
		if(dateCache == null){
			dateCache = new DateCacheInMen(Calendar.DATE);
			obj.put(groupId, dateCache);
		}
		//临时修改 非session 
		List<StartEndDate> searchDateList = dateCache.noCacheDate(groupId, startTime, endTime);
		
		/*if(searchDateList == null ){
			id = String.format("{groupId:'%1$s'}",groupId);
		}else{
		  nightCarManager.computeNightCar(searchDateList,dayStart,dayEnd
				, gateIds ,groupId,sessionId);
		  dateCache.complate();
		    id = String.format("{groupId:'%1$s'}",groupId);
		}*/
		
		if(page == null) page=1;
		if(thresholds == null) thresholds = 1;
		try {
			 Pager pager = new Pager(page,rows);
			 List<NightCarTotal> data = nightCarManager.nightCarTempTotalList(
					request.getSession().getId(), 
					groupId, startTimeStr, endTimeStr, pager,
					thresholds, kdbhs);
			 Map<String,Object> map = new HashMap<String,Object>();
			 map.put("rows", data);
			 map.put("total", pager.getPageCount());
			 map.put("groupId",groupId);
			 return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@ResponseBody
	@RequestMapping
	public Map<String,Object> nightCarTempList(HttpServletRequest request, 
			String groupId, Integer rows, Integer page,String startTime , 
			String endTime , String carNo , String licenseType, String licenseColor )
					throws ParseException{
		Cnd cnd = Cnd.where().and("carDate", Op.EGT, startTime.trim()).
				and("carDate", Op.ELT, endTime.trim()).
				and("licenseType",Op.EQ,licenseType).
				and("licenseColor",Op.EQ, licenseColor).
				and("carNo", Op.EQ, carNo);
		try {
			Pager pager = new Pager(page,rows);
			List<NightCarTemp> data = nightCarManager.nightCarTempList(request.getSession().getId(), 
					groupId, cnd, pager);
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("rows", data);
			result.put("total", pager.getPageCount());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@ResponseBody
	@RequestMapping
	public Map<String,Object> nightCarTempListBySpecificDate(HttpServletRequest request, 
			String groupId, Integer rows, Integer page,String dateStrs, String carNo , String licenseType, String licenseColor )
					throws ParseException{
		String[] dateAgrs = dateStrs.split(",");
		ConditionGroup group = new ConditionGroup();
		for(String dateStr : dateAgrs){
			group.or("carDate", Op.EQ, dateStr);
		}
		Cnd cnd = Cnd.where().and(group).
				and("licenseType",Op.EQ,licenseType).
				and("licenseColor",Op.EQ, licenseColor).
				and("carNo", Op.EQ, carNo);
		try {
			Pager pager = new Pager(page,rows);
			List<NightCarTemp> data = nightCarManager.nightCarTempList(request.getSession().getId(), 
					groupId, cnd, pager);
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("rows", data);
			result.put("total", pager.getPageCount());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 内存模式的时间缓存类
	 * 多线程 使用 同个id 请求同一个对象的noCacheDate方法是不安全的
	 * noCacheDate调用以后使用complete方法才提交到DateCache对象中
	 * 同个线程使用多个noCacheDate后complete，只最后一次调用的结果
	 * 推入DateCache
	 * @author lifenghu
	 * TODO 为了演示临时开放权限 private --> public
	 */
	public static class DateCacheInMen implements DateCache{
		/** 例如 ： Calendar.DATE  */
		private int dateLevel;
		/** 例如 ： Calendar.DATE  */
		//TODO 为了演示临时开放权限 private --> public
		public DateCacheInMen(int calendarField){
			this.dateLevel = calendarField;
		}
		private Map<String,TreeSet<StartEndDate>> dateContainer = new HashMap<String,TreeSet<StartEndDate>>();
		private ThreadLocal<MapEntry<String,TreeSet<StartEndDate>>> tempCache = new ThreadLocal<MapEntry<String,TreeSet<StartEndDate>>>();
		public  List<StartEndDate> noCacheDate(String id , Date startTime, Date endTime ){
			//groupId-sessionId 
			TreeSet<StartEndDate> dateList = dateContainer.get(id);
			
			TreeSet<StartEndDate> tempDateList = new TreeSet<StartEndDate>(new DateComparator());
			synchronized (dateContainer) {
				dateList = dateContainer.get(id);
				if(dateList == null){
					dateList = new TreeSet<StartEndDate>(new DateComparator());
					dateContainer.put(id, dateList);
				}
			}
			synchronized (dateList) {
				for(StartEndDate date : dateList){
					StartEndDate tempDate = new StartEndDate(new Date(date.getStartTime().getTime()),
							new Date(date.getEndTime().getTime()));
					tempDateList.add(tempDate);
				}
				MapEntry<String,TreeSet<StartEndDate>> tempCacheObj = new MapEntry<String,TreeSet<StartEndDate>>(id, tempDateList);
				tempCache.set(tempCacheObj);
				return noCacheDate(tempDateList, startTime, endTime);
			}
		}
		
		public void complate(){
			synchronized (dateContainer) {
				MapEntry<String,TreeSet<StartEndDate>> tempCacheObj = tempCache.get();
				dateContainer.put(tempCacheObj.getKey(), tempCacheObj.getValue());
			}
		}
		
		private List<StartEndDate> noCacheDate(TreeSet<StartEndDate> dateList , Date startTime, Date endTime ){
			startTime = DateUtils.truncate(startTime, Calendar.DATE);
			endTime = DateUtils.truncate(endTime, Calendar.DATE);
			StartEndDate source = new StartEndDate(startTime, endTime);
			List<StartEndDate> noCacheList = new ArrayList<StartEndDate>();
			if(dateList.contains(source)){
				return null;
			}
			while(source!=null){
				StartEndDate frontDate = dateList.lower(source);
				StartEndDate[] frontComplement = null;
				StartEndDate[] behindComplement = null;
				if(frontDate == null){
					frontComplement = new StartEndDate[2];
					frontComplement[0] = source;
				} else {
					frontComplement = complement(frontDate, source);
				}
				if(frontComplement == null){
					return null;
				} 
				StartEndDate behindDate = dateList.higher(source);
				if(behindDate == null){
					noCacheList.add(frontComplement[0]);
					if (frontDate!=null && isJoin(frontDate, frontComplement[0])) 
						frontDate.setEndTime(frontComplement[0].getEndTime());
					else  dateList.add(frontComplement[0]);
					source = null;
				} else {
					behindComplement = complement(behindDate, frontComplement[0]);
					if(behindComplement == null){
						return null;
					} else {
						if(behindComplement[0] != null){
							noCacheList.add(behindComplement[0]);
							if(behindComplement[1]!=null) source = behindComplement[1];
							else source = null;
						}
					}
					if(frontDate!=null && isJoin(frontDate, behindComplement[0]) ) {
						frontDate.setEndTime(behindComplement[0].getEndTime());
						if( isJoin( behindComplement[0] , behindDate) ){
							frontDate.setEndTime(behindDate.getEndTime());
							dateList.remove(behindDate);
						}
					} else {
						if( isJoin( behindComplement[0] , behindDate) ){
							behindDate.setStartTime(behindComplement[0].getStartTime());
						}else dateList.add(behindComplement[0]);
					}
				}
			}
			return noCacheList;
		}
		
		boolean isJoin(StartEndDate frontDate , StartEndDate behindDate){
			int compare = frontDate.getStartTime().compareTo(behindDate.getStartTime());
			if(compare>0) throw new RuntimeException("前时间的开始时间不能大于后时间的开始时间");
			Calendar c = Calendar.getInstance();
			c.setTime(frontDate.getEndTime());
			c.add(dateLevel, 1);
			compare = c.getTime().compareTo(behindDate.getStartTime());
			if(compare<0){
				return false;
			} else return true;
		}
		
		StartEndDate[] complement(StartEndDate target, StartEndDate source){
			int compare = target.getStartTime().compareTo(source.getStartTime());
			StartEndDate[] result = new StartEndDate[2];
			if(compare <= 0){
				int compare1 = target.getEndTime().compareTo(source.getStartTime());
				if(compare1 < 0){
					result[0] = new StartEndDate((Date)source.getStartTime().clone(), (Date)source.getEndTime().clone());
					return result;
				} else if(compare1 == 0) {
					Calendar c = Calendar.getInstance();
					c.setTime(source.getStartTime());
					c.add(this.dateLevel, 1);
					Date finalStartTime = c.getTime();
					if(finalStartTime.compareTo(source.getEndTime())>0){
						return null;
					} else {
						result[0] = new StartEndDate(finalStartTime, (Date)source.getEndTime().clone());
					}
					return result;
				} else {
					Calendar c = Calendar.getInstance();
					c.setTime(target.getEndTime());
					c.add(this.dateLevel, 1);
					Date finalStartTime = c.getTime();
					if(finalStartTime.compareTo(source.getEndTime())>0){
						return null;
					} else {
						result[0] = new StartEndDate(finalStartTime, (Date)source.getEndTime().clone());
					}
					return result;
				}
			} else {
				int compare1 = target.getStartTime().compareTo(source.getEndTime());
				if(compare1>0){
					result[0] = new StartEndDate((Date)source.getStartTime().clone(), (Date)source.getEndTime().clone());
				} else {
					int compare2 = target.getEndTime().compareTo(source.getEndTime());
					if(compare2>=0){
						Calendar c = Calendar.getInstance();
						c.setTime(target.getStartTime());
						c.add(this.dateLevel, -1);
						result[0] = new StartEndDate((Date)source.getStartTime().clone(), c.getTime());
					} else {
						Calendar c = Calendar.getInstance();
						c.setTime(target.getStartTime());
						c.add(this.dateLevel, -1);
						result[0] = new StartEndDate((Date)source.getStartTime().clone(), c.getTime());
						c.setTime(target.getEndTime());
						c.add(this.dateLevel, 1);
						result[1] = new StartEndDate(c.getTime(), (Date)source.getEndTime().clone());
					}
				} 
				return result;
			}
		}
	}
	
	static class DateComparator implements Comparator<StartEndDate>{
		/*
		 *  开始时间一样的排在date1后面
		 *  开始时间和结束时间一致才算相等
		 */
		public int compare(StartEndDate date1, StartEndDate date2) {
			int compare = date1.getStartTime().compareTo(date2.getStartTime());
			if(compare!=0) return compare;
			else {
				compare = date1.getEndTime().compareTo(date2.getEndTime());
				if(compare == 0) return 0;
				else return 1;
			}
		}
		
	}
	
	public static class StartEndDate {
		private Date startTime;
		private Date endTime;
		
		private StartEndDate(Date startTime, Date endTime) {
			if(endTime.compareTo(startTime)<0) throw new RuntimeException("开始时间不能大于结束时间");
			this.startTime = startTime;
			this.endTime = endTime;
		}
		public Date getStartTime() {
			return startTime;
		}
		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}
		public Date getEndTime() {
			return endTime;
		}
		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}
		
	}
	
	public interface DateCache{
		List<StartEndDate> noCacheDate(String id , Date startTime, Date endTime ) ;
		
		void complate();
	}
}
