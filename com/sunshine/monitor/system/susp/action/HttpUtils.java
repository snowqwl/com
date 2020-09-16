package com.sunshine.monitor.system.susp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class HttpUtils {

	public Map callService(Map map ,String url) {
		Map result=null;
		List<BasicNameValuePair> list=putReParamToHttpP(map);
		result=sendHttpClient(list,url);
		return result;
	}
	
	/**
	 * 通过HttpClient接口 httpclient请求接口方法  
	 */
	public  Map<String, Object> sendHttpClient( List<BasicNameValuePair> params,String url){
		Map<String, Object> result = new HashMap<String, Object>();
		String sendPostResult = "";
		try {
//			String url = "http://" + endpoint+"business/picsearchLogic.php" ;
			//建立HttpPost对象
			HttpPost httppost = new HttpPost(url);
			HttpResponse responseSend;
			if (params == null){
				params = new ArrayList<BasicNameValuePair>();
			}
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			responseSend = new DefaultHttpClient().execute(httppost);
			sendPostResult = EntityUtils.toString(responseSend.getEntity());
			} 
		catch (Exception e) {
				e.printStackTrace();
				if (e.getMessage().contains("cannot be cast to java.util.Map")){
					result.put("data", sendPostResult);
					return result;
				}else {
					e.printStackTrace();
					result.put("code", "接口请求失败");
					result.put("state", "error");
					return result;
			}
		}
		result = (Map<String, Object>) JSON.parse(sendPostResult);
		return result;
	}
	
	/**
	 * 将request值放入
	 */
	public  List<BasicNameValuePair> putReParamToHttpP(Map<String, Object> conditions){
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		for (String key : conditions.keySet()) {
			System.out.println("key= "+ key + " and value= " + conditions.get(key));
			params.add(new BasicNameValuePair(key, conditions.get(key).toString()));
		}
		return params;
		}

	
}
