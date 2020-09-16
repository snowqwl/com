package com.sunshine.monitor.comm.util;

import java.util.Comparator;
/**
 * 
 * @author Administrator
 *
 */
public class KeyComparator implements Comparator<String>{

	/* 
     * int compare(Object o1, Object o2) 返回一个基本类型的整型， 
     * 返回负数表示：o1 小于o2， 
     * 返回0 表示：o1和o2相等， 
     * 返回正数表示：o1大于o2。 
     */
	public int compare(String o1, String o2) {
		
		return o1.compareTo(o2);
	}

}
