package com.sunshine.monitor.comm.maintain;

import java.util.HashMap;
import java.util.Map;

import com.sunshine.monitor.comm.maintain.ISmsTransmitter.SmsTransmitterCallBack;
import com.sunshine.monitor.comm.maintain.sms.GetSmsTransmitter;
import com.sunshine.monitor.comm.maintain.sms.PostSmsTransmitter;

public class SmsFacade {
	
	private static ISmsTransmitter iSMSTransmitter;
	
	/**
	 * HTTP POST REQUEST
	 * @param mobile
	 * @param content
	 * @param call
	 * @return
	 */
	public static boolean sendAndPostSms(String mobile, String content, SmsTransmitterCallBack call)throws Exception{
		Map<String, String> nmap = new HashMap<String, String>();
		nmap.put("mobile", mobile);
		nmap.put("msg_content", content);
		iSMSTransmitter = new PostSmsTransmitter();
		return iSMSTransmitter.send(nmap, call);
	}
	
	/**
	 * HTTP GET REQUEST
	 * @param mobile
	 * @param content
	 * @param call
	 * @return
	 */
	public static boolean sendAndGetSms(String mobile, String content, SmsTransmitterCallBack call) throws Exception{
		Map<String, String> nmap = new HashMap<String, String>();
		nmap.put("mobile", mobile);
		nmap.put("msg_content", content);
		iSMSTransmitter = new GetSmsTransmitter();
		return iSMSTransmitter.send(nmap,call);
	}
	
	/**
	 * Flush memery parameters
	 */
	public static void flushMemery(){
		PostSmsTransmitter.flushMemery();
	}
}
