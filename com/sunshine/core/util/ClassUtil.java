package com.sunshine.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public final class ClassUtil{
	
	private ClassUtil(){		
	}
	
	/**
	 * 判断带“,”的字符串是否相互包含
	 * @param param1
	 * @param param2
	 * @return
	 */
	public static boolean isInvolve(String param1,String param2){
		
		if(param1!=null&&!param2.equals("")&&param2!=null&&!param1.equals("")){
			appendSeparator(param1);
			appendSeparator(param2);
			String[] params1 = param1.split(",");
			String[] params2 = param2.split(",");
			for (int i = 0; i < params1.length; i++) {
				for (int j = 0; j < params2.length; j++) {
					if(params1[i].equals(params2[j])) return true;
				}
			}
		}
		return false;
	}
	/**
	 * 去除首尾“,”
	 * @param str
	 * @return
	 */
	public static String appendSeparator(String str){
		if(str!=null&&!"".equals(str)){
			if(str.startsWith(","))
				str = str.substring(1, str.length());
			if(str.endsWith(","))
				str = str.substring(0, str.length()-1);
		}
		return str;
	}
	
	
	public static Object invokeMethod(Object classInstance,String methodName){
		if (classInstance == null || methodName==null)
			throw new IllegalArgumentException(methodName + " doesn't exist");
		
		Method method = lookupMethod(classInstance.getClass().getMethods(), methodName);
		if (method == null)
			throw new IllegalArgumentException(methodName + " is invoked error");		
		try{			
			return method.invoke(classInstance);
		}catch(Exception e){
			throw new IllegalArgumentException(e);
		}	
	}
	
	public static Object invokeMethod(Object classInstance,String methodName,Object... obj){
		if (classInstance == null || methodName==null)
			throw new IllegalArgumentException(methodName + " doesn't exist");
		
		Method method = lookupMethod(classInstance.getClass().getMethods(), methodName);
		if (method == null)
			throw new IllegalArgumentException(methodName + " is invoked error");		
		try{			
			return method.invoke(classInstance,obj);
		}catch(Exception e){
			throw new IllegalArgumentException(e);
		}
	}
	
	public static Object invokegetMethod(Object classInstance,String methodName){
		if (classInstance == null || methodName==null)
			throw new IllegalArgumentException(methodName + " doesn't exist");
		
		Method method = lookupMethod(classInstance.getClass().getMethods(), methodName);
		if (method == null)
			throw new IllegalArgumentException(methodName + " is invoked error");		
		try{			
			return method.invoke(classInstance);
		}catch(Exception e){
			throw new IllegalArgumentException(e);
		}
	}
	
	public static Method lookupMethod(Method[] methods,String methodName){		
		for(Method method:methods){
			if(method.getName().equalsIgnoreCase(methodName))
				return method;
		}
		return null;
	} 

	public static Method lookupMethod(Object object,String methodName){		
		for(Method method:object.getClass().getDeclaredMethods()){
			if(method.getName().equalsIgnoreCase(methodName))
				return method;
		}
		return null;
	} 

	public static boolean isEmpty(Collection<?> collection){
		if(collection==null || collection.isEmpty())
			return true;
		return false;
	}

	public static boolean isEmpty(String string){
		return string == null || "".equals(string) 
		       || "null".equalsIgnoreCase(string);
	}
	
	public static boolean isEmpty(Object obj){
		return obj == null || isEmpty(obj.toString());
	}
	
	public static boolean isEmpty(Map<?,?> collection){
		if(collection==null || collection.values().isEmpty())
			return true;
		return false;
	}

	public static boolean isEmpty(Object[] objs){
		if(objs==null || objs.length == 0)
			return true;
		for(Object obj : objs){
			if(obj == null)
				return true;
		}			
		return false;
	}
	
	public static Object getInstance(String className){
		try {
			Class<? extends Object> clazz = Class.forName(className);
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("can not instance class:" + className);
		}
	}

	public static Class getClass(String className){
		try {
			return Class.forName(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void printOutValues(Object object){
		Method[] methods = object.getClass().getDeclaredMethods();
        for(Method method : methods){
       	    if(method.getName().startsWith("get")){
       		    try{
       			    Object obj = method.invoke(object);
       			    if(obj != null)
       	                System.out.println(method.getName() + "=" + obj);
       		    }catch(Exception e){
       			    e.printStackTrace();
       		    }
       	    }
        }
	} 

	public static void copyBean(Object source, Object target){
		if (source == null || target == null)
			return;

		Method[] methods1 = source.getClass().getMethods();
		for (Method getMethod : methods1) {
			if (getMethod.getName().startsWith("get") || getMethod.getName().startsWith("is")) {
				String property = null;
				if (getMethod.getName().startsWith("get"))
					property = getMethod.getName().substring(3);
				else
					property = getMethod.getName().substring(2);
				
				Method setMethod = lookupMethod(target.getClass().getMethods(), "set" + property);
				if (setMethod != null)
					try {
						setMethod.invoke(target, getMethod.invoke(source));
					} catch (Exception e) {
						//e.printStackTrace();
					}
			}
		}
	}
	
	public static Class getSuperClassGenricType(Class clazz, int index){   
		Type genType = clazz.getGenericSuperclass();  
		
		if (genType instanceof ParameterizedType == false)   
	        return null;   
   		 	  
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		
		if (index >= params.length || index < 0)       
		 	return null;   
		
		if (params[index] instanceof Class == false) {
			System.out.println(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");   
		 	return null;   
		}   
		return (Class) params[index];   
    }
	
	public static int getInt(String number){
		if(isEmpty(number))
			return 0;
		return Integer.parseInt(number);
	}
	
	/**
	 * 判断属性（方法）有没有对应的值
	 * @param classObject
	 * @param fieldName=属性名字
	 * @param object
	 * @return
	 */
	public static boolean CompareClassMethod(Object classObject,String fieldName,Object object){
		if(null==classObject||null==fieldName)
			throw new IllegalArgumentException("classObject or fieldName is Null");
		Object returnObj=null;
		String methodsName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
		try {
			Method method = lookupMethod(classObject.getClass().getMethods(), methodsName);
			if(null==method)
				return false;
			//读取name字段
			returnObj=method.invoke(classObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj.equals(object);
	}
	
	/**
	 * 取得类的所有方法（包括私有包括父类的方法）
	 * @param clazz
	 * @param list
	 */
	public static void getMethodToAll(Class clazz,List<Method> list){
		if (null==clazz.getSuperclass())  
            return;
		Method[] methods=clazz.getDeclaredMethods();
        for (Method method : methods) {
			list.add(method);
		}
        getMethodToAll(clazz.getSuperclass(),list);
	}
	public static Method getMethodbyName(Object object,String name){
		List<Method> list=new ArrayList<Method>();
		getMethodToAll(object.getClass(),list);
		Method methodTemp=null;
		for (Method method : list) {
			if(method.getName().equals(name)){
				methodTemp=method;
				break;
			}
		}
		return methodTemp;
	}
	public static boolean CompareClassAttribute(Object classObject,String attribute,Object object){
		Object returnObj=null;
		try {
			Field field = getFiledbyName(classObject,attribute);
			if(null==field)
				return false;
			field.setAccessible(true);
			//读取name字段
			returnObj=field.get(classObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj.equals(object);
	}
	
	public static Field getFiledbyName(Object object,String name){
		List<Field> list=new ArrayList<Field>();
		getFieldToAll(object.getClass(),list);
		Field fieldTemp=null;
		for (Field field : list) {
			if(field.getName().equals(name)){
				fieldTemp=field;
				break;
			}
		}
		return fieldTemp;
	}
	
	public static void getFieldToAll(Class clazz,List<Field> list){
		if (null==clazz.getSuperclass())  
            return;
        Field[] fields=clazz.getDeclaredFields();
        for (Field field : fields) {
			list.add(field);
		}
        getFieldToAll(clazz.getSuperclass(),list);
	}
	
	/**
	 * 根据正则表达式找到方法集合
	 * @param clazz
	 * @param regex
	 * @return
	 */
	public static List<Method> findMethodByPattern(Class clazz,String regex){
		Method[] ms = clazz.getMethods();
		List<Method> mList = new ArrayList<Method>();
		for(Method m : ms){
			if(Pattern.matches(regex, m.getName())){
				mList.add(m);
			}
		}
		return mList;
	}

}