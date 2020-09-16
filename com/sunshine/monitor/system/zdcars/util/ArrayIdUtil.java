package com.sunshine.monitor.system.zdcars.util;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import com.ibm.icu.text.SimpleDateFormat;

public class ArrayIdUtil {
	/**
	 * 唯一id标识生成
	 * @return
	 */
	public static long getId(){
		SimpleDateFormat  sdf=new SimpleDateFormat("yymmddhhmmssSSS");
		String i=sdf.format(new Date());
		Random r=new Random();
		
		return Long.parseLong(i)+Math.abs(r.nextLong());
	}
	/**
	 * 批量导入的 批次号自动生成
	 * @return
	 */
	public static String getArrayId(){
		
		return String.valueOf(UUID.randomUUID());
	}
	
	/**
	 * 生成时间标识
	 */
	public static long getTimeId(){
		SimpleDateFormat  sdf=new SimpleDateFormat("yymmddhhmmssSSS");
		String i=sdf.format(new Date());
		return Long.parseLong(i);
	}
}
