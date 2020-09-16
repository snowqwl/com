package com.sunshine.monitor.comm.util.orm.helper;

import org.apache.commons.lang.StringUtils;

public class QueryCondition implements SqlExpression {
	private String key ;
	private Op op ;
	private Object value;
	
	public QueryCondition(){}
	
	public QueryCondition(String key, Op op , Object value){
		this.key = key;
		this.op = op ;
		this.value = value ; 
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Op getOp() {
		return op;
	}

	public void setOp(Op op) {
		this.op = op;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public static enum Op {
		EQ("=") , LT("<") , GT(">") , ELT("<=") , EGT(">="), NEQ("<>"), NOTNULL("IS NOT NULL"),
		IN("in"), LIKE(" like ");
		private String sqlOp ;
		
		Op(String sqlOp){
			this.sqlOp = sqlOp;
		}
		
		public String getSqlOp(){
			return sqlOp;
		}
	}

	
	public String toSql() {
		switch (op) {
		case IN:
			Object value = getValue();
			String valueStr = "('";
			if( value instanceof String[] ){
				valueStr += StringUtils.join((String[])value, "','");
			}
			return key + op.getSqlOp() +valueStr+"')";
		default:
			return key + op.getSqlOp() +"?";
		}
		
	}
	
}
