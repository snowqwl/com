package com.sunshine.monitor.comm.log;

/**
 * 日志新增参数bean 
 * @author liumeng
 * 2016-4-21 
 */
public class LogHandlerParam {
	 
	 /**
	  * 日志类型编号
	  */
	 private String type;
	 
	 /**
	  * 日志类型说明
	  */
	 private String description;
	 
	 /**
	  * 执行的方法名称
	  */
	 private String operationMethod;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOperationMethod() {
		return operationMethod;
	}

	public void setOperationMethod(String operationMethod) {
		this.operationMethod = operationMethod;
	}
	 
}
