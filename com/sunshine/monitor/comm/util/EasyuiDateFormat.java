package com.sunshine.monitor.comm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In order to format EASYUI date
 * @author OUYANG
 *
 */
public class EasyuiDateFormat {
	
	public static String getStandardDate(String datetime){
		if(datetime == null || "".equals(datetime))
			return null ;
		String temp[] = datetime.split(" ");
		if(temp.length <= 1)
			return null ;
		String t = temp[0];
		Pattern p = Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{1,4}$");
		Matcher m = p.matcher(t);
		boolean flag = m.matches();
		StringBuffer sb = new StringBuffer();
		String result = null;
		if(flag){
			String[] xarray = temp[0].split("/");
			if(xarray.length == 3){
				sb.append(xarray[2]);
				sb.append("-");
				sb.append(xarray[0]);
				sb.append("-");
				sb.append(xarray[1]);
			}
			return sb.toString() + " " +temp[1];
		}
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(EasyuiDateFormat.getStandardDate("06/5/2013 20:39:14"));
	}

}
