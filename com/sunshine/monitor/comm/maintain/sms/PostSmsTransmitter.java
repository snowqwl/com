package com.sunshine.monitor.comm.maintain.sms;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;

import com.sunshine.monitor.comm.maintain.AbstractHttpSmsTransmitter;
import com.sunshine.monitor.comm.maintain.util.HttpClientUtil;
import com.sunshine.monitor.comm.maintain.util.NameValuePairUtil;

/**
 * HTTP POST Request
 * 
 * @author Administrator
 * 
 */
public class PostSmsTransmitter extends AbstractHttpSmsTransmitter {

	public PostSmsTransmitter() {
		super();
	}

	public String sendSms(Map map) {
		NameValuePairUtil nvpu = (NameValuePairUtil) map;
		NameValuePair[] nvp = nvpu.getNameValuePair();
		HttpClient httpClient = HttpClientUtil.getHttpClient();
		return HttpClientUtil.postMethodDataResult(httpClient,
				getUrl(), nvp, getCharset());
	}
}
