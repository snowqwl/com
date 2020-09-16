package com.sunshine.monitor.system.veh.util;

public class TestDES {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 	String str = "01234ABCDabcd!@#$";  
	        String t = "";  
	        System.out.println("加密后：" + (t = EncryUtil.encrypt(str)));  
	        System.out.println("解密后：" + EncryUtil.decrypt(t));  
	}

}
