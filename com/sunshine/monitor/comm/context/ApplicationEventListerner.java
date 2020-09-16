package com.sunshine.monitor.comm.context;

import java.util.EventListener;

/**
 * Super EventListerner Object, All of subclass overwrite actionPerformed of
 * method. When event is triggered, method of actionPerformed is will called.
 * 
 * @author OUYANG 2014/4/16
 * 
 */
public interface ApplicationEventListerner extends EventListener {

	/**
	 * SubClass implements this method
	 */
	public void actionPerformed(AbstractApplicationEvent e);

}
