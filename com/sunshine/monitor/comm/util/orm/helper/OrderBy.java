package com.sunshine.monitor.comm.util.orm.helper;

public interface OrderBy  extends SqlExpression {
	public OrderByGroup ASC(String name);
	
	public OrderByGroup DESC(String name);
}
