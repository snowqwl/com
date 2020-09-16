package com.sunshine.monitor.system.manager.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagerFilterAnnotation {
    /**
     * 操作方法
     * @return
     */
	String  method();
	
	/**
	 * 操作对象
	 * @return
	 */
	Class clazz();
	
}
