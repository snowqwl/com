package com.sunshine.monitor.comm.maintain.sms;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;

import com.sunshine.monitor.comm.maintain.AbstractHttpSmsTransmitter;
import com.sunshine.monitor.comm.maintain.util.HttpClientUtil;
import com.sunshine.monitor.comm.maintain.util.NameValuePairUtil;

/**
 * HTTP GET Request
 * 
 * @author Administrator
 * 
 */
public class GetSmsTransmitter extends AbstractHttpSmsTransmitter {

	public GetSmsTransmitter() {
		super();
	}

	@Override
	public String sendSms(Map map) {
		NameValuePairUtil nvp = (NameValuePairUtil) map;
		String reUrl = nvp.getMethodUrl(getUrl());
		HttpClient httpClient = HttpClientUtil.getHttpClient();
		return HttpClientUtil.getMethodDataResult(httpClient, reUrl);
	}
}