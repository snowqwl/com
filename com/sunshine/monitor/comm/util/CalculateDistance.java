package com.sunshine.monitor.comm.util;

import java.text.DecimalFormat;

/**
 * 根据经纬度计算两点之间的距离
 * @author liumeng
 *
 */
public class CalculateDistance {
	private static double EARTH_RADIUS = 6378.137;//地球半径
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}

	public static String getDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);

	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   //s = Math.round(s * 10000) / 10000;
	   DecimalFormat df1 = new DecimalFormat("#.####");
	   String temp  = df1.format(s);
	   return temp;
	}
	
	public static void main(String[] args) {
		
		
		System.out.println(getDistance(39.058438,111.714797, 27.758248, 111.986277));
		 DecimalFormat df1 = new DecimalFormat("#.#");
		   String time  = df1.format(0.12345);
		   System.out.println(time);
	}
	
}
