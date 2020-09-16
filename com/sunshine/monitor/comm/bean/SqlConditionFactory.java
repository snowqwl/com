package com.sunshine.monitor.comm.bean;

public class SqlConditionFactory {
	private SqlConditionFactory(){}
	
	public static SqlCondition createSqlCondition(String fieldName,Operation op,Object value){
		SqlCondition c = new SqlCondition();
		c.setFieldName(fieldName);
		c.setOp(op);
		c.setValue(value);
		return c;
	}
	
	public static class SqlCondition{
		private String fieldName;
		private Operation op;
		private Object value;
		
		private SqlCondition(){}
		
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public Operation getOp() {
			return op;
		}
		public void setOp(Operation op) {
			this.op = op;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		
	}
	
	public static enum Operation {
		EQ("="),GT(">"),LT("<"),EGT(">="),ELT("<=");
		private String opStr;
		
		Operation(String opStr){
			this.opStr = opStr;
		}

		public String getOpStr() {
			return opStr;
		}

		public void setOpStr(String opStr) {
			this.opStr = opStr;
		}
		
	}
}
