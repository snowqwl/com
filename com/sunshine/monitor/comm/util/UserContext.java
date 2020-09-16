package com.sunshine.monitor.comm.util;

import com.sunshine.monitor.system.manager.bean.SysUser;

public class UserContext {

	private static final ThreadLocal<SysUser> context = new ThreadLocal<SysUser>();

	public static void setUser(SysUser sysuser) {
		
		context.set(sysuser);
	}

	public static SysUser getUser() {
		
		return context.get();
	}
	
	public static void clear(){
		
		context.remove();
	}
}
