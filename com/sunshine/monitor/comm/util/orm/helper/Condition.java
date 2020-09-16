package com.sunshine.monitor.comm.util.orm.helper;

import com.sunshine.monitor.comm.util.orm.helper.QueryCondition.Op;


public interface Condition extends SqlExpression {
	public Condition and(String name, Op op, Object value);
	
	public Condition and(ConditionGroup group) ;
	
	public Condition and(QueryCondition condition);
	
	public Condition and(StaticExpression condition);
	
	public Condition or(String name, Op op, Object value);
	
	public Condition or(QueryCondition condtion);
	
	public Condition or(ConditionGroup group);
	
	public Condition or(StaticExpression condition);
	
}
