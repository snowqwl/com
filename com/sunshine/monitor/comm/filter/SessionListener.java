package com.sunshine.monitor.comm.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.sunshine.monitor.system.manager.bean.SysUser;

/**
 * session属性监听,用于统计在线用户数,同一账户登录踢出在线用户
 * 对于直接关闭浏览器的操作等待session失效之后,才能做到清除.
 * @date 2016-6-1
 * @author licheng 
 *
 */
public class SessionListener implements HttpSessionAttributeListener{

	/**
	 * 定义监听的session属性名
	 */
	public final static String LISTENER_NAME = "_login";	
	public final static String LISTENER_COUNT = "_count";
	
	/**
	 * 定义存储客户登录用户的集合
	 */
	private static Map<String, SysUser> users = new HashMap<String, SysUser>();
	
	/**
	 * 定义存储客户登录session的集合
	 */
	private static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();
	
	/**
	 * 定义存储在线用户数的集合
	 */
	private static Map<String, Integer> counts = new HashMap<String, Integer>();
	
	/**
	 * 加入session时的监听方法
	 * 
	 * @param HttpSessionBindingEvent
	 * 				session事件
	 */
	public void attributeAdded(HttpSessionBindingEvent sbe) {
		if(LISTENER_NAME.equals(sbe.getName())){
			SysUser s = (SysUser) sbe.getValue();
			/*if(users.containsKey(s.getYhdh())) {
				HttpSession session = sessions.get(s.getYhdh());
				session.removeAttribute(LISTENER_NAME);
				session.invalidate();
			}*/ 
			sessions.put(s.getYhdh(), sbe.getSession());
			users.put(s.getYhdh(), s);
		}
		Integer count = users.size();
		counts.put(LISTENER_COUNT, count);
	}
	
	/**
	 * session失效时的监听方法
	 * 
	 * @param HttpSessionBindingEvent
	 * 				session事件
	 */
	public void attributeRemoved(HttpSessionBindingEvent sbe) {
		if(LISTENER_NAME.equals(sbe.getName())){
			// 从session集合中减去一个
			SysUser s = (SysUser) sbe.getValue();
			sessions.remove(s.getYhdh());
			users.remove(s.getYhdh());
			counts.put(LISTENER_COUNT, users.size());
		}
	}
	
	/**
	 * session覆盖时的监听方法
	 * 
	 * @param HttpSessionBindingEvent
	 * 				session事件
	 */
	public void attributeReplaced(HttpSessionBindingEvent sbe) {
		
	}
	
	public static Map<String, SysUser> getUsers() {
		return users;
	}
	
	public static Map<String, HttpSession> getSessions() {
		return sessions;
	}
	
	public static Map<String, Integer> getCounts() {
		return counts;
	}
	
}
