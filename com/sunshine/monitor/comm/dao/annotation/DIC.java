package com.sunshine.monitor.comm.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DIC {
	/**
	 * 字典的code_type id
	 * @return
	 */
	String dicType() ;
	/**
	 * 需要映射的字段
	 * @return
	 */
	String dicFieldName();
}
