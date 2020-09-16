package com.sunshine.monitor.comm.maintain;

import java.util.Map;

/**
 * 短息发送接口，短信发送后，执行回调方法获取错误信息
 * 
 * @author Administrator
 * @see com.sunshine.monitor.monitor.comm.SmsTransmitterCallBack
 * @since V1.0.1
 */
public interface ISmsTransmitter {

	/**
	 * Short Message Send
	 * 
	 * @param <K>
	 * @param <V>
	 * @param call
	 * @return
	 */
	boolean send(Map map, SmsTransmitterCallBack call) throws Exception;

	/**
	 * Short Message excute sended then call back
	 * 
	 */
	interface SmsTransmitterCallBack {
		/**
		 * The parameter map is passed from client call method
		 * 
		 * @param map
		 */
		void doCallBack(Map map) throws Exception;
	}
}
