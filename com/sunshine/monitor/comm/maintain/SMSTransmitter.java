package com.sunshine.monitor.comm.maintain;


/**
 * 发送短信接口
 * @author JiangKunpeng
 *
 */
public interface SMSTransmitter {
	
	/**
	 * 发送短信
	 * @param mobile		手机号码
	 * @param content		短信内容
	 */
	public String send(String mobile,String content);
	
	/**
	 * 发送短信
	 * @param mobile		手机号码
	 * @param content		短信内容
	 */
	public void send(String[] mobile,String content);
	
}
