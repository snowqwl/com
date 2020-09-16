package com.sunshine.core.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
/**
 * Defining Spring Cache key
 * @author YANGYANG
 *
 */
public class SpringCacheKeyGenerator implements KeyGenerator {

	public Object generate(Object target, Method method, Object... params) {
		
		return null;
	}
}
