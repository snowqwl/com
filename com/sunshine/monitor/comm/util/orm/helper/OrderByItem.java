package com.sunshine.monitor.comm.util.orm.helper;

public class OrderByItem implements SqlExpression  {
	private String name;

	private String by;

	public OrderByItem(String name, String by) {
		this.name = name;
		this.by = by;
	}

	public String toSql() {
		return name+" "+by;
	}

	public String getName() {
		return name;
	}

	public String getBy() {
		return by;
	}
}
