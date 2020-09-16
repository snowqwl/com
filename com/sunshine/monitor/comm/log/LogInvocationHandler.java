package com.sunshine.monitor.comm.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.http.HttpServletRequest;

/**
 * 新增日志动态代理 
 * @author liumeng
 * 2016-4-21
 */
public class LogInvocationHandler implements InvocationHandler{
	 /**
	  * 调用真实对象某个方法时接收的参数
	  */
	 private Object target;
	 
	 
	 private LogHandlerParam bean;
	 
	 private HttpServletRequest request;
	 
	 private LogHandlerInter ihandle;
	 
	 public LogInvocationHandler(LogHandlerInter ihandle){
		 this.ihandle = ihandle;
	 }
	  
	 public Object bind(Object target,LogHandlerParam bean,HttpServletRequest request) {  
		 this.target = target;
		 this.bean = bean;
		 this.request = request;
        //取得代理对象  
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),  
                target.getClass().getInterfaces(), this);   
    }  
	 
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result=null;  
		//如果方法不同，则不执行
		if(bean.getOperationMethod()==null || !bean.getOperationMethod().equals(method.getName())){
			return 0;
		}
		
        //超前处理
		ihandle.doPreHandle(args, bean, request);
		result=method.invoke(target, args);  //insertRole
		//延后处理
        ihandle.doAfterHandle(args, bean, request);
        
        return result;  
	}

}
