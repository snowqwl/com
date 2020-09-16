package com.sunshine.monitor.comm.util.orm.helper;

import com.sunshine.monitor.comm.util.orm.helper.QueryCondition.Op;


public class Cnd implements Condition,OrderBy {
	private final ConditionGroup conditions = new ConditionGroup();
	private final OrderByGroup orders = new OrderByGroup();
	
	private Cnd(){
	}
	
	public static Cnd where(){
		return new Cnd();
	}
	
	public ConditionGroup getConditions() {
		return conditions;
	}

	public OrderByGroup getOrders() {
		return orders;
	}

	
	public String toSql() {
		return conditions.toSql() + orders.toSql();
	}

	
	public Cnd and(String name, Op op, Object value) {
		conditions.and(name, op, value);
		return this;
	}

	
	public Cnd and(ConditionGroup group) {
		conditions.and(group);
		return this;
	}

	
	public Cnd and(QueryCondition condition) {
		conditions.and(condition);
		return this;
	}

	
	public Cnd or(String name, Op op, Object value) {
		conditions.or(name, op, value);
		return this;
	}

	
	public Cnd or(QueryCondition condtion) {
		conditions.or(condtion);
		return this;
	}

	
	public Cnd or(ConditionGroup group) {
		conditions.or(group);
		return this;
	}

	
	public OrderByGroup ASC(String name) {
		orders.ASC(name);
		return orders;
	}

	
	public OrderByGroup DESC(String name) {
		orders.DESC(name);
		return orders;
	}

	
	public Cnd and(StaticExpression condition) {
		conditions.and(condition);
		return this;
	}

	
	public Cnd or(StaticExpression condition) {
		conditions.or(condition);
		return this;
	}

}
