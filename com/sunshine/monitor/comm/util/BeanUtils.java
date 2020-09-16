package com.sunshine.monitor.comm.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sunshine.monitor.comm.util.orm.Constant;

public class BeanUtils {
	/**
	 * 复制 对象数据 针对系统中存在的问题， 一切 Date 类数据 都是字符串形式接收
	 * 复制源目标 有 getXXX 方法的数据  ， 接收方用setXXX 注入 （XXX代表的部分必须相同）
	 * 要求 ： 源方有getXXX方法 ， 接收方有getXXX, setXXX 方法
	 * @param target 目标，接收方
	 * @param resource 源数据，提供方
	 */
	public static void copyProperties(Object target, Object resource){
		Method[] methods = resource.getClass().getMethods();
		for(Method method : methods){
			String methodName = method.getName();
			String fileName = "";
			if(methodName.startsWith("get")){
				fileName = methodName.substring(3);
				if(StringUtils.isNullBlank(fileName)) continue;
			} else {
				continue;
			}
			copyProperty(target,resource, fileName);
		}
	}
	
	private static void copyProperty(Object target,Object resource, String propertyName){
		String fieldName = propertyName;
		String getMethodName = "get"+fieldName;
		String setMethodName = "set"+fieldName;
		try {
			Object obj = resource.getClass().getMethod(getMethodName).invoke(resource);
			Class targetFieldClazz = target.getClass().getMethod(getMethodName).getReturnType();
			//相同类型的
			if(targetFieldClazz.isInstance(obj)){
				target.getClass().getMethod(setMethodName, obj.getClass()).invoke(target, obj);
			//目标对象为String类型的
			} else if(targetFieldClazz.getName().equals(String.class.getName())
					&& obj instanceof Date ){
				if(obj instanceof Date){
					SimpleDateFormat format = new SimpleDateFormat(Constant.dateFormat);
					String value = format.format((Date)obj);
					target.getClass().getMethod(setMethodName, String.class).invoke(target, value);
				} else {
					target.getClass().getMethod(setMethodName, String.class).invoke(target, obj.toString());
				}
			} else {
				//无法判断的不复制
			}
		//忽略一切错误，错误的不复制
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) { 
		} 
	}
}
