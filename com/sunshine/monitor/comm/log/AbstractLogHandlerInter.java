package com.sunshine.monitor.comm.log;

import javax.servlet.http.HttpServletRequest;

/**
 * 适配器
 * @author liumeng 2016-4-21
 *
 */
public abstract class AbstractLogHandlerInter implements LogHandlerInter {

	@Override
	public void doPreHandle(Object obj, LogHandlerParam bean,
			HttpServletRequest request) throws Exception {

	}

	@Override
	public void doAfterHandle(Object obj, LogHandlerParam bean,
			HttpServletRequest request) throws Exception {

	}
}
