package com.sunshine.monitor.comm.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志操作注解,应用于方法
 * @author OUYANG 2013/7/18
 *
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) 
public @interface OperationAnnotation {
	
	/**
	 * 操作类型
	 * @return
	 */
	public OperationType type();   
    
	/**
	 * 功能操作描述
	 * @return
	 */
	public String description() default "";
	
	
	/**
	 * 用户名
	 * @return
	 */
	public String user() default "";
	
	/**
	 * 部门
	 * @return
	 */
	public String department() default "";
}
