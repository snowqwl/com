package com.sunshine.monitor.comm.log;

import javax.servlet.http.HttpServletRequest;

/**
 * 新增日志的处理 接口
 * @author liumeng
 * 2016-4-21 
 */
public interface LogHandlerInter {
		
		/**
		 * 方法执行前
		 * @param obj
		 * @param bean
		 * @throws Exception
		 */
		public void doPreHandle(Object obj,LogHandlerParam bean,HttpServletRequest request) throws Exception;
		
		/**
		 * 方法执行后
		 * @param obj
		 * @param bean
		 * @throws Exception
		 */
		public void doAfterHandle(Object obj,LogHandlerParam bean,HttpServletRequest request) throws Exception;
		
		
}
