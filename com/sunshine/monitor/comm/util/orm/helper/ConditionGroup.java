package com.sunshine.monitor.comm.util.orm.helper;

import java.util.ArrayList;
import java.util.List;

import com.sunshine.monitor.comm.util.orm.helper.QueryCondition.Op;


public class ConditionGroup implements Condition {
	private List<SqlExpression> conditions = new ArrayList<SqlExpression>();
	private List<Object> values = new ArrayList<Object>();
	private boolean isTop = true;

	
	public String toSql() {
		StringBuilder sb = new StringBuilder();
		
		for(SqlExpression sql : conditions){
			sb.append(sql.toSql()).append(" ");
		}
		if(!isTop){
			sb.insert(0, "(");
			sb.append(")");
		}
		return sb.toString();
	}

	public ConditionGroup and(String name, Op op, Object value) {
		and(new QueryCondition(name, op, value));
		return this;
	}
	
	public ConditionGroup and(ConditionGroup group) {
		group.setTop(false);
		if(conditions.size() != 0) conditions.add(StaticExpression.AND());
		values.addAll(group.getValues());
		conditions.add(group);
		return this;
	}
	
	public ConditionGroup and(QueryCondition condition) {
		if(conditions.size() != 0) conditions.add(StaticExpression.AND());
		conditions.add(condition);
		values.add(condition.getValue());
		return this;
	}
	
	public ConditionGroup or(String name, Op op, Object value) {
		or(new QueryCondition(name, op, value));
		return this;
	}
	
	public ConditionGroup or(QueryCondition condtion) {
		if(conditions.size() != 0) conditions.add(StaticExpression.OR());
		values.add(condtion.getValue());
		conditions.add(condtion);
		return this;
	}
	
	public ConditionGroup or(ConditionGroup group) {
		group.setTop(false);
		if(conditions.size() != 0) conditions.add(StaticExpression.OR());
		values.addAll(group.getValues());
		conditions.add(group);
		return this;
	}
	
	
	public Condition and(StaticExpression condition) {
		if(conditions.size() != 0) conditions.add(StaticExpression.AND());
		conditions.add(condition);
		return this;
	}

	
	public Condition or(StaticExpression condition) {
		if(conditions.size() != 0) conditions.add(StaticExpression.OR());
		conditions.add(condition);
		return null;
	}
	
	public boolean isTop() {
		return isTop;
	}

	public void setTop(boolean isTop) {
		this.isTop = isTop;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

}
