package com.sunshine.monitor.comm.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtils {
	private static CacheMethod cache = new CacheMethod();
	
	public static Object getValue(Object obj, String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		return getGetMethod(obj.getClass(),fieldName).invoke(obj);
	}
	
	public static Method getGetMethod(Class<?> clazz, String fieldName) throws NoSuchMethodException, SecurityException{
		return cache.get(clazz, "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
	}
	
	public static Method getSetMethod(Class<?> clazz, String fieldName, Class<?> paramType) throws NoSuchMethodException, SecurityException{
		return cache.get(clazz, "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),paramType);
	}
	
	private static class CacheMethod{
		private Map<String, Method> map = new HashMap<String, Method>();
		
		private Method get(Class<?> clazz,String methodName,Class<?>...parameterTypes) throws NoSuchMethodException, SecurityException{
			String key = getKey(clazz, methodName, parameterTypes);
			Method m = map.get(key);
			if(m==null){
				synchronized (map) {
					m = map.get(key);
					if(m==null){
						m = clazz.getMethod(methodName, parameterTypes);
					}
				}
			}
			return m;
		}
		private String getKey(Class<?> clazz,String methodName,Class<?>...parameterTypes){
			StringBuilder sb = new StringBuilder(clazz.getName());
			sb.append("_").append(methodName).append("_");
			for(Class<?> c : parameterTypes) sb.append(c.getName()).append("_");
			sb.delete(sb.length()-1, sb.length());
			return sb.toString();
			
		}
	}
}
