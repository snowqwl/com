package com.sunshine.monitor.comm.context;

import java.util.EventObject;

/**
 * Abstract Event Object 
 * @author OUYANG 2014/4/16
 *
 */
public abstract class AbstractApplicationEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	/** System time when the event happened */
	private final long timestamp;
	
	/**
	 * Create a new AbstractApplicationEvent.
	 */
	public AbstractApplicationEvent(Object source){
		super(source);
		this.timestamp = System.currentTimeMillis();
	}
	
	/**
	 * Return the system time in milliseconds when the event happened.
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}
}
