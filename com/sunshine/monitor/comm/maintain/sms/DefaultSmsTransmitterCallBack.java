package com.sunshine.monitor.comm.maintain.sms;

import java.util.Map;

import com.sunshine.monitor.comm.maintain.ISmsTransmitter.SmsTransmitterCallBack;
/**
 * @author Administrator
 *
 */
public class DefaultSmsTransmitterCallBack implements SmsTransmitterCallBack {

	private boolean status ;
	
	private String errorMsg ;
	
	private String mobile;
	
	@Override
	public void doCallBack(Map map) throws Exception {
		if(map.containsKey("status"))
			this.status = Boolean.valueOf((String)map.get("status"));
		if(!Boolean.valueOf(status))
			this.errorMsg = (String)map.get("errorMsg");
		if(!Boolean.valueOf(status))
			this.mobile = (String)map.get("mobile");
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public String getMobile() {
		return mobile;
	}
}
