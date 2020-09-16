package com.sunshine.monitor.system.manager.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sunshine.monitor.system.manager.dao.SystemDao;


@Component
@Aspect
public class ManagerFilter {
	
	
	@Autowired
	private SystemDao systemDao;
		
	public int getOrder() {
		return 1;
	}
   
	@Pointcut("execution(* com.sunshine.monitor.system.manager.service.impl..*.*(..)) && @annotation(com.sunshine.monitor.system.manager.aspect.ManagerFilterAnnotation)")
	public void ManagerPointCut(){
		
	}
	
	/**
	 * 将系统新增属性（用户，字典，部门）加载到内存中
	 * @param jp
	 * @param managerFilterAnnotation
	 */
	@After("ManagerPointCut() && @annotation(managerFilterAnnotation)")
	public void doFilterManager(JoinPoint jp,
			ManagerFilterAnnotation managerFilterAnnotation) {
		try {
			Method method = systemDao.getClass().getMethod(managerFilterAnnotation.method(),managerFilterAnnotation.clazz());
			Object[] args = jp.getArgs();
			for (Object o : args) {
				if (o.getClass().equals(managerFilterAnnotation.clazz())) {
					method.invoke(systemDao,o);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
