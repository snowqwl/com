package com.sunshine.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public final class SysUtil {
	public static final DecimalFormat intFormatter = new DecimalFormat("#");
    public static final DecimalFormat floatFormatter = new DecimalFormat("#.00");	
    public static final String GET = "get" ;
    public static final String SET = "set" ;
    
    
	private SysUtil() {
	}

	public static String getChinese(String ss) {
		String strpa = null;
		try {
			if (ss != null)
				strpa = new String(ss.getBytes("ISO-8859-1"), "GB2312");
		} catch (Exception e) {
			strpa = ss;
		}
		return strpa;
	}

	public static float formatFloat(double val1, double val2) {
		return val2 == 0 ? 0 : Float.parseFloat(floatFormatter.format(val1 / val2));
	}

	public static double formatDouble(double a, double b) {
		if (b == 0) return 0;
		return Double.parseDouble(floatFormatter.format(a / b));
	}
	
	public static long formatLong(double a, double b) {
	 	if (b == 0) return 0;
	 	return Long.parseLong(intFormatter.format(a / b));
	}

	/**
	 * 两个数据之间的差是否大于10%
	 * 
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static boolean isGap(Number val1, Number val2) {
		if (val1.doubleValue() * val2.doubleValue() < 0)
			return true;

		Number big = val1;
		Number small = val2;
		if (val2.doubleValue() > val1.doubleValue()) {
			Number _temp = val1;
			val1 = val2;
			val2 = _temp;
		}
		if ((big.doubleValue() - small.doubleValue()) / big.doubleValue() >= 0.1D)
			return true;
		return false;
	}
	
	
	public static boolean isNull(String target) {
		if (target == null || "".equals(target) || target.length() == 0)
			return true;
		else
			return false;
	}
	
	public static boolean isNull(Object target) {
		if (target == null)
			return true;
		else
			return false;
	}
	
	public static String ifNull(String str) {
		if (str == null || str.equalsIgnoreCase("null"))
			return "";
		else
			return str;
	}	
	
	public static synchronized int getRandomId(){
		while(true){
		    int id = (int)(Math.random() * 10000);
		    if( id > 1000)
		    	return id;
		}
	}
	
	/**
	 * 字符串替换(针对转义字符)
	 * @param strSource
	 * @param strFrom
	 * @param strTo
	 * @return
	 */
	public static String replaceStr(String strSource, String strFrom, String strTo) {
		if (strSource == null)
			return null;

		int i = 0;
		if ((i = strSource.indexOf(strFrom, i)) >= 0) {
			char[] cSrc = strSource.toCharArray();
			char[] cTo = strTo.toCharArray();
			int len = strFrom.length();
			StringBuffer buf = new StringBuffer(cSrc.length);
			buf.append(cSrc, 0, i).append(cTo);
			i += len;
			int j = i;
			while ((i = strSource.indexOf(strFrom, i)) > 0) {
				buf.append(cSrc, j, i - j).append(cTo);
				i += len;
				j = i;
			}
			buf.append(cSrc, j, cSrc.length - j);
			return buf.toString();
		}
		return strSource;
	}
	
	/**
	 * 如果文件夹不存在，则创建
	 * @param path
	 */
	public static void createFileIfNotExists(String path){
		if(path==null||"".equals(path))
			return;
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
	}
	
	/**
	 * 删除文件夹或文件
	 * @param folderPath
	 */
	public static void delFolder(String folderPath) {
		String filePath = null;
		java.io.File myFilePath = null;
		try {
			delAllFile(folderPath); //删除完里面所有内容
			filePath = folderPath;
			filePath = filePath.toString();
			myFilePath = new java.io.File(filePath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally{
			filePath = null;
			myFilePath = null;
		}
	}
	
	/**
	 * 删除文件夹中的所有内容
	 * @param path
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()|| file.list()==null ) 
			return flag;

		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + File.separator + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + File.separator + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}	
	
	public static void exeCmd(String cmd) {
		Process p = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			p = Runtime.getRuntime().exec(cmd);
			isr = new InputStreamReader(p.getErrorStream());
			br = new BufferedReader(isr);
			while (br.readLine() != null) 
				p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				br.close();
				isr.close();
			}catch(Exception e){				
			}
			br = null;
			isr = null;
			p = null;
		}
	}
	
	public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyMMddHHmm");
	public static final Random random = new Random();
	
	/**
	 * 获取时间戳和随机数组成的ID值
	 * @param prefix 前缀
	 * @param randomLength 随机数长度
	 * @return 返回结果格式为：前缀+时间戳+随机数<br/>
	 *         时间戳长度为:10,Format表达式为:yyMMddHHmm
	 */
	public static synchronized String getTimeRandomID(String prefix, int randomLength) {
		String id = prefix == null ? "" : prefix;
		id += datetimeFormat.format(new Date());
		if (randomLength > 0)
			id += getRandomNumStr(randomLength);
		return id;
	}
	
	/**
	 * 获取数字组成的随机字符串
	 * @param length 长度
	 * @return
	 */
	public static synchronized String getRandomNumStr(int length) {
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length; i++)
			sb.append(random.nextInt(10));
		return sb.toString();
	}
	
	/**
	 * 裁剪字符串为指定长度<br/>
	 * 如果源字符串长度不超过指定长度,则返回源字符串<br/>
	 * 如果源字符串长度大于指定长度,则返回源字符串的前len个字符+...
	 * @param src	源字符串
	 * @param len	指定长度
	 * @return
	 */
	public static String trimLength(String src,int len){
		if(src==null)
			return null;
		int slen = src.length();
		if(slen>len)
			return src.substring(0, len)+"...";
		return src;
	}
	
	/**
	 * 首字符大写
	 * @param fieldName
	 * @return
	 */
	public static String upperFirstChar(String flag, String fieldName) {
		if (fieldName == null)
			return null;
		StringBuffer methodName = new StringBuffer(12) ;
		methodName.append(flag) ;
		methodName.append(fieldName.substring(0, 1).toUpperCase()) ;
		methodName.append(fieldName.substring(1).toLowerCase()) ;
		return methodName.toString() ;
	}
}