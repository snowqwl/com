package com.sunshine.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * time and date utility
 * @author afunms
 * @version Sourceview4.0 2009.12.20
 */
public final class DateUtil {
	public static final SimpleDateFormat datetimeFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssSSS");
	public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
	public static final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	public static final SimpleDateFormat yyyymmFormat = new SimpleDateFormat("yyyyMM");
	public static final SimpleDateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH");
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	private DateUtil(){		
	}

	public static String getCurrentDateTime2(){
	    return datetimeFormat2.format(Calendar.getInstance().getTime());
	}

	public static String getCurrentTime(){
	    return timeFormat.format(Calendar.getInstance().getTime());
	}

	public static synchronized String getCurrentDateTime(){
	    return datetimeFormat.format(Calendar.getInstance().getTime());
	}

	public static String getCurrentDate(){
	    return dateFormat.format(Calendar.getInstance().getTime());
	}

	public static String getCurrentMonth(){
	    return monthFormat.format(Calendar.getInstance().getTime());
	}

	public static String getCurrentYear(){
	    return yearFormat.format(Calendar.getInstance().getTime());
	}

	public static String getCurrentHour(){
	    return hourFormat.format(Calendar.getInstance().getTime());
	}

	public static String getLastHour(){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, -1);

	    return hourFormat.format(cal.getTime());
	}

	public static String getYYYYMM(){
	    return yyyymmFormat.format(Calendar.getInstance().getTime());	    
	}

	public static long getDiffTwoTimeToLong(String time1, String time2) {
		if (time1 == null || time2 == null)
			return 0;
		return  dateTimeToLong(time1) - dateTimeToLong(time2);
	}

	public static String getDiffTwoTime(String time1, String time2) {
		if (time1 == null || time2 == null)
			return "";
		long diffTime = dateTimeToLong(time2) - dateTimeToLong(time1);
		return secondToTimeString(diffTime);
	}

	public static int getDiffDays(String time1, String time2) {
		try {
			long diffTime = datetimeFormat.parse(time2).getTime() - datetimeFormat.parse(time1).getTime();
			return (int)(diffTime / 1000 / 86400);
		} catch (ParseException e) {
			return 0;
		}
	}

	public static synchronized int getDiffHours(String time1, String time2) {
		try {
			long diffTime = datetimeFormat.parse(time2).getTime() - datetimeFormat.parse(time1).getTime();
			return (int)(diffTime / 3600000);	//1000*60*60
		} catch (ParseException e) {
			return 0;
		}
	}
	
	public static synchronized int getDiffMinutes(String time1, String time2) {
		try {
			long diffTime = datetimeFormat.parse(time2).getTime() - datetimeFormat.parse(time1).getTime();
			return (int)(diffTime / 1000 / 60);
		} catch (ParseException e) {
			return 0;
		}
	}
	
	/**
	 * 用于多处轮询计算,请误修改
	 */
	public static synchronized long getDiffSeconds(String time1, String time2) {
		try {
			long diffTime = Math.abs(datetimeFormat.parse(time2).getTime() - datetimeFormat.parse(time1).getTime());
			return diffTime / 1000;
		} catch (ParseException e) {
			return 0;
		}
	}
	
	public static long dateTimeToLong(String dateTime) {
		try {
			Date date = datetimeFormat.parse(dateTime);
			return date.getTime();
		} catch (ParseException e) {
			return 0;
		}
	}
	
    public static String longToTime(long timeLong){
    	if(timeLong == 0) return "";
    	
        Date date = new Date(timeLong);
        return datetimeFormat.format(date);
    }

	public static String secondToTimeString(long second){
		second = second / 1000;
		
		StringBuffer timeStr = new StringBuffer(10);
		long hh24 = second / 3600;
		long surplus = second % 3600;

		long mi = surplus / 60;
		long ss = surplus % 60;

		if(hh24 > 0){
			timeStr.append(hh24);
			timeStr.append("小时");
		}		
		if(mi > 0){
			timeStr.append(mi);
			timeStr.append("分钟");
		}		
		if(timeStr.length() == 0 || ss > 0){
			timeStr.append(ss);
			timeStr.append("秒");
		}
		return timeStr.toString();
	}
	
	public static long getCurrentLongDateTime() {
		return new Date().getTime();
	}
	
    public static String getCurrentTimeAsID(){
        return String.valueOf(System.nanoTime());
    }
    
    public static String getWeekDay(int day){
		if(day==1)
			return "星期一";
		else if(day==2)
			return "星期二";
		else if(day==3)
			return "星期三";
		else if(day==4)
			return "星期四";
		else if(day==5)
			return "星期五";
		else if(day==6)
			return "星期六";
		else
			return "星期日";
	}
    
    /**
	 * 判断是否在今天之前
	 * @param c
	 * @return
	 */
	public static boolean beforeToday(Calendar c){
		Calendar today = Calendar.getInstance();
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		today.set(Calendar.AM_PM, 0);
		today.set(Calendar.HOUR, 0);
		return c.getTimeInMillis()<today.getTimeInMillis();
	}
	
	/**
	 * 判断是否在今天之后
	 */
	public static boolean afterToday(Calendar c){
		Calendar today = Calendar.getInstance();
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		today.set(Calendar.AM_PM, 1);
		today.set(Calendar.HOUR, 12);
		today.add(Calendar.SECOND, -1);
		return c.getTimeInMillis()>today.getTimeInMillis();
	}
	
	/**
	 * 清空时间
	 * @param c
	 * @return
	 */
	public static Calendar clearTime(Calendar c){
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.set(Calendar.AM_PM, 0);
		c.set(Calendar.HOUR, 0);
		return c;
	}
	
	/**
	 * 设置时间为23:59:59
	 * @param c
	 * @return
	 */
	public static Calendar fillTime(Calendar c){
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.set(Calendar.AM_PM, 1);
		c.set(Calendar.HOUR, 12);
		c.add(Calendar.SECOND, -1);
		return c;
	}
	
	/**
	 * 设置日期为本周一
	 * @param c
	 * @return
	 */
	public static Calendar toFirstDateOfWeek(Calendar c){
		int day = c.get(Calendar.DAY_OF_WEEK);	//1:星期天、2:星期一...
		if(day==1)
			day = 8;
		day=day-2;
		c.add(Calendar.DATE, -day);
		return c;
	}
	
	/**
	 * 设置日期为本周日
	 * @param c
	 * @return
	 */
	public static Calendar toLastDateOfWeek(Calendar c){
		int day = c.get(Calendar.DAY_OF_WEEK);	//1:星期天、2:星期一...
		if(day==1)
			day = 8;
		day=8-day;
		c.add(Calendar.DATE, day);
		return c;
	}
	
	/**
	 * 设置日期为本月第一天
	 * @param c
	 * @return
	 */
	public static Calendar toFirstDateOfMonth(Calendar c){
		c.set(Calendar.DATE, 1);
		return c;
	}
	
	/**
	 * 设置日期为本月第一天
	 * @param c
	 * @return
	 */
	public static String toFirstDateOfMonth(String dateStr){
		Calendar c = null;
		try {
			Date date = dateFormat.parse(dateStr);
			c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DATE, 1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormat.format(c.getTime());
	}
	
	/**
	 * 设置日期为本月最后一天
	 * @param c
	 * @return
	 */
	public static Calendar toLastDateOfMonth(Calendar c){
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DATE, 1);
		c.add(Calendar.DATE, -1);
		return c;
	}

	/**
	 * 设置日期为本月最后一天
	 * @param c
	 * @return
	 */
	public static String toLastDateOfMonth(String dateStr){
		Calendar c = null;
		try {
			Date date = dateFormat.parse(dateStr);
			c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MONTH, 1);
			c.set(Calendar.DATE, 1);
			c.add(Calendar.DATE, -1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormat.format(c.getTime());
	}	
	
	/**
	 * 设置为本年第一天
	 * @param c
	 * @return
	 */
	public static Calendar toFirstDateOfYear(Calendar c){
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DATE, 1);
		return c;
	}
	
	/**
	 * 设置为本年最后一天
	 * @param c
	 * @return
	 */
	public static Calendar toLastDateOfYear(Calendar c){
		c.add(Calendar.YEAR, 1);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DATE, 1);
		c.add(Calendar.DATE, -1);
		return c;
	}

	/**
	 * 得到本月以前N个月
	 */
	public static List<String> getBeforeMonths(int n) {
		List<String> months = new ArrayList<String>();
		
		Calendar cal = Calendar.getInstance();
		for(int i=0;i<n;i++){
			cal.add(Calendar.MONTH, 0 - i);
			months.add(DateUtil.yyyymmFormat.format(cal.getTime()));
		}
		return months;
	}
	
	/**
	 * 取日期字符串中的日,日期字符串格式为yyyy-MM-dd
	 * @param fullDate
	 * @return
	 */
	public static int extractDateNum(String fullDate){
		int d = 0;
		try{
			d = Integer.parseInt(fullDate.split("-")[2]);
		}catch(Exception e){
		}
		return d;
	}
	
	/**
	 * 讲数字转成时间需要的格式,10以下的在数字前加0
	 * @param num
	 * @return
	 */
	public static String formatDateNum(int num){
		return num<10?"0"+num:""+num;
	}
	
	/**
	 * 取得两个日期间的年份和月份
	 * @param beforeDate
	 * @param afterDate
	 * @return
	 */
	public static List<String> getMiddleMonths(String startTime,String endTime){
		List<String> list = new ArrayList<String>();
		try {
			Calendar beforeCalendar = Calendar.getInstance();
			Calendar afterCalendar = Calendar.getInstance();
			beforeCalendar.setTime(DateUtil.datetimeFormat.parse(startTime));
			afterCalendar.setTime(DateUtil.datetimeFormat.parse(endTime));
			DateUtil.toLastDateOfMonth(afterCalendar);
			while(beforeCalendar.compareTo(afterCalendar) <= 0){
				list.add(DateUtil.yyyymmFormat.format(beforeCalendar.getTime()));
				beforeCalendar.add(Calendar.MONTH, 1);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	
	public static List<Integer> getLastSixHours(){
		List<Integer> hours = new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 1);
		for(int i=0;i<6;i++){
			cal.add(Calendar.HOUR, -1);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			hours.add(hour);
		}	
		Collections.sort(hours);		
		return hours;
	}
	
	/**
	 * 跳过工作日延迟
	 * @param c 参照时间
	 * @param add 要延迟的天数(5天以内)
	 * @return
	 */
	public static String addWorkDay(Calendar c,int add){
		int d = c.get(Calendar.DAY_OF_WEEK);
		if(d==Calendar.SUNDAY)
			add+=1;
		else if(d==Calendar.SATURDAY)
			add+=2;
		else if(d==Calendar.FRIDAY)
			add+=3;
		c.add(Calendar.DATE, add);
		return DateUtil.datetimeFormat.format(c.getTime());
	}
	
	/**
	 * 计算月最大天数
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getMaxDayOfMonth(String dateStr) throws Exception {
		Date date = null;
		Calendar c = Calendar.getInstance();
		if (!StringUtils.isBlank(dateStr)) {
			date = monthFormat.parse(dateStr);
		}
		date = (date == null) ? new Date() : date;
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 格式化到天
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String formatByDay(String dateStr)throws Exception {
		Date date = dateFormat.parse(dateStr);
		if(date == null) {
			date = new Date();
		}
		return dateFormat.format(date);
	}
	
	/**
	 * 格式化到月
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String formatByMonth(String dateStr)throws Exception {
		Date date = monthFormat.parse(dateStr);
		if(date == null) {
			date = new Date();
		}
		return monthFormat.format(date);
	}
	
	
	/**
	 * 上一天
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String countBeforeDay(String dateStr)throws Exception {
		Date date = dateFormat.parse(dateStr);
		date = DateUtils.addDays(date, -1);
		return dateFormat.format(date);
	}
	
	/**
	 * 上一月(含天)
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String countBeforeMonthDay(String dateStr)throws Exception { 
		Date date = dateFormat.parse(dateStr);
		date = DateUtils.addMonths(date, -1);
		return dateFormat.format(date);
	}
	
	
	/**
	 * 上一月(不含天)
	 * @return
	 * @throws Exception
	 */
	public static String countBeforeMonth(String dateStr) throws Exception {
		Date date = monthFormat.parse(dateStr);
		date = DateUtils.addMonths(date, -1);
		return monthFormat.format(date);
	}
	
	/**
	 * 获取年月
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String getYYYYMM(String dateStr)throws Exception{
		Date date = dateFormat.parse(dateStr);
		return yyyymmFormat.format(date);
	}
	
	/**
	 * 一天中最大小时数
	 * @return
	 */
	public static int getMaxHour(){
		Calendar c = Calendar.getInstance();
		return c.getMaximum(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 计算在某时间之前几小时的时间
	 * @param time1
	 * @param hours
	 * @return
	 */
	public static String getBeforeHours(String time1, int hours) {
		try {
			long time2 = datetimeFormat.parse(time1).getTime() - hours * 3600000;
			return DateUtil.longToTime(time2);
		} catch (ParseException e) {
			return time1;
		}
	}
}