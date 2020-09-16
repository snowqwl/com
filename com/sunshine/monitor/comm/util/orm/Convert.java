package com.sunshine.monitor.comm.util.orm;

public interface Convert {
	public boolean isCanConvert(int dbType,Object obj);
	public Object exec(Object obj);
}
