package com.sunshine.monitor.comm.maintain;


/**
 * alarm transmitter,maybe sms,email,voice... etc.
 * @author afunms
 * @version sourceview4.0 2010.4.9
 */
public interface AlarmTransmitter {
	public String getFlag();
	
	public String send(String[] targets,MaintainBean alarm);	
}
