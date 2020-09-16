package com.sunshine.monitor.system.ws.outAccess.transform;

public class BKFWToNew {
	public static String transform(String oldStr){
		String bkfw = oldStr.trim();
		bkfw = bkfw.replaceAll("\\D", "000000,");
		bkfw = bkfw + "000000";
		return bkfw;
	}
}
