package com.sunshine.monitor.comm.util.orm.helper;

public class StaticExpression implements SqlExpression {
	private String name;
	public StaticExpression(String name){
		this.name = name;
	}
	
	public String toSql() {
		return this.name;
	}
	
	public static StaticExpression AND(){
		return new StaticExpression("AND");
	}
	
	public static StaticExpression OR(){
		return new StaticExpression("OR");
	}
}
