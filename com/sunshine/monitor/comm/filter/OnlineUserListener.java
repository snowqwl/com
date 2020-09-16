package com.sunshine.monitor.comm.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.bean.UserSession;
import com.sunshine.monitor.system.manager.bean.SysUser;

public class OnlineUserListener
		implements
			HttpSessionListener, HttpSessionAttributeListener, Filter {
	
	private Logger log = LoggerFactory.getLogger(OnlineUserListener.class);
	/**
	 * 用户Session对象
	 */
	public static final String USER_SESSION_NAME = "userSession";

	/**
	 * 在线人数
	 */
	public static AtomicInteger lineCount = new AtomicInteger(0);
	
	private final ReentrantLock lock = new ReentrantLock();
	
	public final static String LISTENER_NAME = "_login";
	
	private static volatile Map<String, SysUser> users = new ConcurrentHashMap<String, SysUser>();
	private static volatile Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();
	
	/**
	 * 需要人工添加属性_login,触发此方法
	 */
	@Override
	public void attributeAdded(HttpSessionBindingEvent hbe) {
		if(LISTENER_NAME.equals(hbe.getName())){
			// 用户登录成功后，将HttpSession存储到一个map
			HttpSession session = hbe.getSession();
			//System.out.println(hbe.getName() + "-HttpSessionAttributeListener(attributeAdded)...........");
			SysUser user = (SysUser) hbe.getValue();
			String yhdh = user.getYhdh();
			final ReentrantLock _lock = this.lock;
			_lock.lock();
			try {
				boolean flag = users.containsKey(yhdh);
				users.put(yhdh, user);
				sessions.put(yhdh, session);
				if(!flag){
					lineCount.addAndGet(1); // 累加器
				}
				log.info(yhdh + "-HttpSessionAttributeListener(attributeAdded)..........." + lineCount.intValue());
			} finally {
				_lock.unlock();
			}
		}
		
	}
	
	/**
	 * 需要人工删除属性_login,触发此方法
	 */
	@Override
	public void attributeRemoved(HttpSessionBindingEvent hbe) {
		log.info(hbe.getName()+"进入attributeRemoved。。。。。。。。。。。。");
		if(LISTENER_NAME.equals(hbe.getName())){
			log.info(hbe.getName() + "-HttpSessionAttributeListener(attributeRemoved)...........");
			SysUser user = (SysUser) hbe.getValue();
			if(user == null)
				return;
			String yhdh = user.getYhdh();
			final ReentrantLock _lock = this.lock;
			_lock.lock();
			try{
				if(users.containsKey(yhdh)){
					users.remove(user.getYhdh());
					sessions.remove(user.getYhdh());
					lineCount.decrementAndGet();
					log.info(yhdh + "-HttpSessionAttributeListener(attributeRemoved)..........." + lineCount.intValue());
				}
			} finally {
				_lock.unlock();
			}
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent hbe) {
		
	}
	
	/**
	 * 会话创建时执行
	 */
	@Override
	public void sessionCreated(HttpSessionEvent he) {
	}
	/**
	 * 会话消毁执行
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent he) {
		log.info(he.getSource()+ "-HttpSessionListener(sessionDestroyed)...........");
		HttpSession session = he.getSession();
		UserSession userSession = (UserSession)session.getAttribute(USER_SESSION_NAME);
		if(userSession != null){
			SysUser user = userSession.getSysuser();
			String yhdh = user.getYhdh();
			final ReentrantLock _lock = this.lock;
			_lock.lock();
			try{
				if(users.containsKey(yhdh)){
					users.remove(user.getYhdh());
					sessions.remove(user.getYhdh());
					lineCount.decrementAndGet();
					log.info(yhdh+ "-HttpSessionListener(sessionDestroyed)..........." + lineCount.intValue());
				}
			} finally {
				_lock.unlock();
			}
		}
	}

	public static Map<String, SysUser> getUsers() {
		return users;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		for(Entry<String, HttpSession> entry : sessions.entrySet()){
			HttpSession session = entry.getValue();
			if(session != null){
				if((System.currentTimeMillis() - session.getLastAccessedTime()) > session.getMaxInactiveInterval() * 1000){
					session.invalidate();
				}
			}
		}
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
