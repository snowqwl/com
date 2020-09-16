package com.sunshine.monitor.comm.maintain;

import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;

import com.sunshine.monitor.comm.maintain.util.HttpClientUtil;


public class StkkSMSTransmitter implements SMSTransmitter {

	private String SEND_URL = "http://sms.gd/UserSendServToOA?suser=tzakkjkpt&spwd=111111&telno=";
	
	private String ENCODE_CHARSET = "gb2312";
	
	public String send(String mobile, String content) {
		HttpClient httpclient = HttpClientUtil.getHttpClient();
		StringBuffer strurl = new StringBuffer(SEND_URL);
		strurl.append(mobile);
		strurl.append("&scontent=");
		strurl.append(transformStrtoUTF(content));
		return HttpClientUtil.getMethodDataResult(httpclient, strurl.toString());
	}

	public void send(String[] mobile, String content) {
		for (String string : mobile) {
			send(string, content);
		}
	}
	
	/**
	 * 编码
	 * @param msg
	 * @return
	 */
	private String transformStrtoUTF(String msg){
		String temp = null ;
		try {
			temp = java.net.URLEncoder.encode(msg, ENCODE_CHARSET) ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp ;
	}
}
