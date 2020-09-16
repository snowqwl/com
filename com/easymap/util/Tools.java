package com.easymap.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class Tools {
	
	public static String gTrim(String str)
	{
		return str.trim();
	}
	
	public static String getSafeStr(String str)
	{
		if(Tools.isEmpty(str))
			return "";
		return str.trim();
	}
	
	public static String getSafeObj(Object obj)
	{
		if(Tools.isEmpty(obj))
			return "";
		return obj.toString().trim();
	}
	
	public static boolean isNotEmpty(List<? extends Object> list) {
		return !isEmpty(list);
	}

	public static boolean isEmpty(List<? extends Object> list) {
		return list == null || list.isEmpty();
	}

	public static boolean isEmpty(Collection<?> c) {
		return c == null || c.isEmpty();
	}

	public static boolean isEmpty(String input) {
		return input == null || input.trim().length() < 1;
	}
	
	public static boolean isEmptyStr(String input) {
		return "null".equalsIgnoreCase(input);
	}

	public static boolean isNotEmpty(String input) {
		return !isEmpty(input);
	}

	public static boolean isEmpty(Object input) {
		if (input instanceof String) {
			return isEmpty((String) input);
		}
		if (input instanceof List<?>) {
			return input == null || ((List<?>) input).isEmpty();
		}
		if (input instanceof Collection<?>) {
			return input == null || ((Collection<?>) input).isEmpty();
		}
		return input == null;
	}

	public static boolean isNotEmpty(Object input){
		return !isEmpty(input);
	}
	
	public static boolean isEmpty(Map map){
		if (null == map || map.size()==0){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNotEmpty(Map map){
		return !isEmpty(map);
	}
	
	public static boolean isEmpty(Object[] objects){
		if (null == objects || objects.length==0){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNotEmpty(Object[] objects){
		return !isEmpty(objects);
	}
	
	// 只能判断非小数.
	public static boolean isNumeric(String s) {
		if (s == null) {
			return false;
		}
		int i = s.length();
		if (i <= 0) {
			return false;
		}
		for (int j = 0; j < i; j++) {
			if (!Character.isDigit(s.charAt(j))) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isDouble(String str) {  
		try {
			double dd = Double.valueOf(str);
			return true;
		} catch (Exception e) {
			System.out.println(str + "  试图转换为Double,异常");
			return false;
		}
	}
	
	/**
	 * 如果为空或者null,返回 -1.
	 * @param val
	 * @return
	 */
	public static Integer getInteger(String val)
	{
		if(isEmpty(val))
			return -1;
		if(!isNumeric(val))
			return -1;
		return Integer.valueOf(val);
	}
	
	/**
	 * 如果为空或者null,返回 0.
	 * @param val
	 * @return
	 */
	public static Integer getIntegerz(String val)
	{
		if(isEmpty(val))
			return 0;
		if(!isNumeric(val))
			return 0;
		return Integer.valueOf(val);
	}
	
	public static double getDouble(String val)
	{
		if(isEmpty(val))
			return -1;
		if(!isDouble(val))
			return -1;
		return Double.valueOf(val);
	}
	
	public static double getDoublez(String val)
	{
		if(isEmpty(val))
			return 0;
		if(!isDouble(val))
			return 0;
		return Double.valueOf(val);
	}
	
	public static Integer getDouble2Integer(String val)
	{
		if(isEmpty(val))
			return 0;
		if(!isDouble(val))
			return 0;
		return (int)(double)Double.valueOf(val);
	}
	
	public static String getSafeSqlStr(String str)
	{
		return str.replace("%", "").replace("-", "").replace("/", "");
	}
	
	/**
	 * 检查是否有中文
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str)
	{
		if(isEmpty(str))
			return false;
		int count = 0; // 多少个中文
		 String regEx = "[\\u4e00-\\u9fa5]";
		 Pattern p = Pattern.compile(regEx);
		 Matcher m = p.matcher(str);    
		while (m.find()) {    
			for (int i = 0; i <= m.groupCount(); i++) {    
				 count = count + 1;    
			 }    
		 }    
		 return count > 0;
	}
	
	// 生成UUID，可做主键。
	public static String getOneUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "").toUpperCase();
	}
	
	public static String genUniqueStrId() {
		return System.currentTimeMillis() + "" + (int) (Math.random() * 10);
	}

	public static Long genUniqueLongId() {
		return parseLong(System.currentTimeMillis() + "" + (int) (Math.random() * 10));
	}
	
	public static Long parseLong(String input) {
		try {
			return Long.parseLong(input);
		} catch (Exception e) {
			return new Long(0);
		}
	}
	
	public static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat formatd = new SimpleDateFormat("yyyyMMdd");
	public static final DateFormat formatw = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat formatw2 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	public static final DateFormat formatws = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static final DateFormat formatws2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static final DateFormat formatm = new SimpleDateFormat("MM-dd HH:mm");
	public static final DateFormat formats = new SimpleDateFormat("MM-dd");
	public static final DateFormat matter_cn = new SimpleDateFormat("现在时间:yyyy年MM月dd日E HH时mm分ss秒");
	public static final DateFormat formatrqxq = new SimpleDateFormat("yyyy-MM-dd E");
	public static final DateFormat formatsj = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat formatsjfz = new SimpleDateFormat("HH:mm");
	
	/**
	 * "yyyy年MM月dd日 E"
	 * @return
	 */
	public static String getNowDateWeek()
	{
		Date date = new Date();
		return formatrqxq.format(date);
	}
	
	/**
	 * "HH:mm:ss"
	 * @return
	 */
	public static String getNowTime()
	{
		Date date = new Date();
		return formatsj.format(date);
	}
	
	/**
	 * HH:mm
	 * @return
	 */
	public static String getNowTimeFz()
	{
		Date date = new Date();
		return formatsjfz.format(date);
	}
	
	/**
	 * "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getNowTimeString()
	{
		Date date = new Date();
		return formatw.format(date);
	}
	
	/**
	 * yyyy-MM-dd_HH:mm:ss
	 * @return
	 */
	public static String getNowTimeHString() {
		Date date = new Date();
		return formatw2.format(date);
	}
	
	/**
	 * "yyyy-MM-dd"
	 * @return
	 */
	public static String getNowDateHString()
	{
		Date date = new Date();
		return format.format(date);
	}
	
	/**
	 * "yyyyMMdd"
	 * @return
	 */
	public static String getNowDateString()
	{
		Date date = new Date();
		return formatd.format(date);
	}
	
	/**
	 * "yyyyMMddHHmmssSSS"
	 * @return
	 */
	public static String getDefaultBh()
	{
		Date date = new Date();
		return formatws2.format(date);
	}
	
	public static String getDateString(DateFormat dateFormat,Date date)
	{
		return dateFormat.format(date);
	}
	
	public static Date getNowDate()
	{
		return new Date();
	}
	
	public static String getSerialNumber(int bh,int len)
	{
//		String newString = String.format("%0"+Common.BH_LEN+"d", bh);
		String newString = String.format("%0"+len+"d", bh);
		return getNowDateString() + newString;
	}
	
	public static String getSerialNumberb(int bh,int len)
	{
//		String newString = String.format("%0"+Common.BH_LEN+"d", bh);
		return String.format("%0"+len+"d", bh);
	}

	public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        T[] copy = ((Object)newType == (Object)Object[].class)
            ? (T[]) new Object[newLength]
            : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
	
	// 输出 执行用时 
	public static void printUseTime(double dataNum,long startTime,long endTime,String explain)
	{
		long useMillis = endTime - startTime;
		double useSecond = useMillis * 1.0 / 1000;
		//System.out.println(explain+ " " + dataNum + " 条数据用时:  " + useMillis + " 毫秒 ≈ " + useSecond + " 秒");
		System.err.println(String.format("%s %s 条数据执行用时:  %s  毫秒 ≈ %s 秒\r\n", explain,dataNum,useMillis,useSecond));
	}
	
	public static void printUseTime(long startTime,long endTime,String explain)
	{
		long useMillis = endTime - startTime;
		double useSecond = useMillis * 1.0 / 1000;
		//System.out.println(explain+ " " + dataNum + " 条数据用时:  " + useMillis + " 毫秒 ≈ " + useSecond + " 秒");
		System.err.println(String.format("%s 执行用时:  %s  毫秒 ≈ %s 秒\r\n", explain,useMillis,useSecond));
	}
	
	public static void dy(String msg)
	{
		dy(null,msg);
	}
	
	public static void dy(Class class1,String msg)
	{
		String classString = isEmpty(class1)?"":class1.getName();
		System.err.println(classString + " \t " + msg);
	}

	public static void printArrVal(Object...colval)
	{
		if (isNotEmpty(colval)) {
			StringBuilder sb = new StringBuilder();
			for (Object object : colval) {
				sb.append("\t").append(object).append(";");
			}
			Tools.dy(null,sb.toString());
		}
	}
	
	 //   当前日期加减n天后的日期，返回String   (yyyy-mm-dd)  
    public static String   nDaysAftertoday(int   n)   {  
        SimpleDateFormat   df   =   new   SimpleDateFormat("yyyy-MM-dd");  
        Calendar   rightNow   =   Calendar.getInstance();  
        //rightNow.add(Calendar.DAY_OF_MONTH,-1);  
        rightNow.add(Calendar.DAY_OF_MONTH,+n);  
        return   df.format(rightNow.getTime());  
    }  
 
//    //   当前日期加减n天后的日期，返回String   (yyyy-mm-dd)  
//    public   Date   nDaysAfterNowDate(int   n)   {  
//        Calendar   rightNow   =   Calendar.getInstance();  
//        //rightNow.add(Calendar.DAY_OF_MONTH,-1);  
//        rightNow.add(Calendar.DAY_OF_MONTH,+n);  
//        return   rightNow.getTime();  
//    }  
    /**
     * 是否含有sql注入，返回true表示含有
     * @param obj
     * @return
     */
    public static boolean containsSqlInjection(Object obj){
        Pattern pattern= Pattern.compile(" (and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%)");
        Matcher matcher=pattern.matcher(obj.toString());
        return matcher.find();
    }
public String regReplaceImage(String content){
	   Pattern p= Pattern.compile(" (and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%)");
    StringBuffer operatorStr=new StringBuffer(content);
    Matcher m = p.matcher(content);
    while(m.find()) {
        //使用分组进行替换
        operatorStr.replace(m.start(2),m.end(2),"");
        m = p.matcher(operatorStr);
    }
    return operatorStr.toString();
}
}
