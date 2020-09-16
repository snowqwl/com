package com.sunshine.monitor.comm.util;

import org.springframework.jdbc.core.CallableStatementCallback;

public abstract class CallableStatementCallbackImpl implements
		CallableStatementCallback {
	private Object parameterObject;

	public Object getParameterObject() {
		return this.parameterObject;
	}

	public void setParameterObject(Object parameterObject) {
		this.parameterObject = parameterObject;
	}
}
