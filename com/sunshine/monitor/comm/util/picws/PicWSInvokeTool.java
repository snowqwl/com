package com.sunshine.monitor.comm.util.picws;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.veh.bean.VehPassrec;
import com.sunshine.monitor.system.veh.bean.VehRealpass;

public class PicWSInvokeTool {
	
	private static int THREAD_NUM = 10;//长沙图片请求线程数量最大设置为10个
	
	public static String getValue(JSONObject json, String key, String defaultStr){
		String v1 = null;
		try {
			if(json.containsKey(key)){
				v1 = json.getString(key);
				if(v1==null || "".equals(v1) || v1.length()==0){
					return defaultStr;
				} else {
					return URLDecoder.decode(v1,"utf-8");
				}
			}
			//System.out.println(defaultStr +"__" +URLDecoder.decode((v1==null)?"":v1,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return defaultStr;
	}
	
	public static JSONObject getPicJsonObject(Object obj, String kdbh){
		JSONObject tpJsonObject = new JSONObject();
		if(kdbh != null && kdbh.startsWith("4301")){
			if(obj != null ){
				if(obj instanceof VehRealpass){
					VehRealpass vr = (VehRealpass)obj;
					tpJsonObject = getPicfromWs(vr.getTp1(),vr.getTp2(),vr.getTp3(), vr.getBy1());
				} else if(obj instanceof VehAlarmrec){
					VehAlarmrec va = (VehAlarmrec)obj;
					tpJsonObject = getPicfromWs(va.getTp1(),va.getTp2(),va.getTp3(), va.getBy1());
				} else if(obj instanceof VehPassrec){
					VehPassrec va = (VehPassrec)obj;
					tpJsonObject = getPicfromWs(va.getTp1(),va.getTp2(),va.getTp3(), va.getBy1());
				}
			}
		}
		return tpJsonObject;
	}
	
	/**
	 * <2016-5-19> Licheng 解决由响应速度造成的过车图片顺序乱的问题，在方法中新增一个参数by1，作为唯一标识
	 * @param tp1
	 * @param tp2
	 * @param tp3
	 * @param by1
	 * @return
	 */
	public static JSONObject getPicfromWs(String tp1, String tp2, String tp3, String by1){
		JSONObject json = new JSONObject();
		HttpClient httpclient = null;
		if(by1==null){
			by1="";
		}
		try {
			String content = null;
			URL url = null;
			if(by1!=null && !"".equals(by1)){
				url = new URL(
						"http://10.142.54.32:9080/picws/PicServlet.do?tp1="
								+ URLEncoder.encode(tp1, "UTF-8") + "&by1="
								+ URLEncoder.encode(by1, "UTF-8"));
			}else {
				url = new URL(
						"http://10.142.54.32:9080/picws/PicServlet.do?tp1="
								+ URLEncoder.encode(tp1, "UTF-8"));
			}
			
			httpclient = new DefaultHttpClient();
			// 请求超时
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            // 读取超时
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			
		    //这里的超时单位是毫秒。这里的http.socket.timeout相当于SO_TIMEOUT  
/*			httpclient.getParams().setIntParameter("http.socket.timeout",1500);  
			URLConnection httpconn = url.openConnection();
		    httpconn.setConnectTimeout(1500);
		    httpconn.setReadTimeout(1500); */
		 
			HttpPost httpPost = new HttpPost(url.toURI());
			httpPost.setHeader("Content-type", "text/html;charset=utf-8");
			httpPost.setHeader("Accept-Charset", "utf-8");
			long start = System.currentTimeMillis();
			HttpResponse httpResponse = httpclient.execute(httpPost);
			long end = System.currentTimeMillis();
			System.out.println("ChangSha picture request takes time is " + (end - start) + "MS");
			StatusLine sl = httpResponse.getStatusLine();
			if (sl.getStatusCode() >= 300) {
				throw new Exception("应用服务器连接异常【" + sl.getStatusCode() + "】!");
			}
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				content = EntityUtils.toString(httpEntity);
			}
			httpclient.getConnectionManager().shutdown();
			//System.out.println("缉查布控接收:" + content);
			return JSONObject.fromObject(content);
		} catch (Exception e) {
			e.printStackTrace();
			httpclient.getConnectionManager().shutdown();
		}
		return json;
	}
	
	/**
	 * 多线程获取长沙图片
	 */
	public List<Map<String, String>> getPicMil(List<Map<String, Object>> list){
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		ExecutorService service = null;
		try {	
			int poolSize = list.size();
			if(poolSize == 1){
				Map<String, String> params = new HashMap<String, String>();
				params.put("tp1",  list.get(0).get("tp1").toString()==null?"":list.get(0).get("tp1").toString());
				result.add(new HResultCall(params).call());
			}else{
				if(poolSize > 10) {
					poolSize = THREAD_NUM;
				}
				service = Executors.newFixedThreadPool(poolSize);
				List<Future> futureList = new Vector<Future>();
				for (Map<String, Object> m : list) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("tp1", m.get("tp1")==null?"":m.get("tp1").toString());
					Future f1 = service.submit(new HResultCall(params));
					futureList.add(f1);					
				}
				for(Future f1 : futureList){
					Map<String,String> data = (Map<String,String>)f1.get();
					if(data==null || data.size()==0){
						continue;
					}
					result.add(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(service!=null){
				service.shutdown();
			}
		}
		return result;
	}
	
	/**
	 * 线程查询长沙过车图片
	 * @author 
	 * @param <T>
	 */
	class HResultCall implements Callable<Map<String, String>>{
		private Map<String, String> params ;
		public HResultCall(Map<String, String> params){
			this.params = params;
		}		
		@SuppressWarnings("unchecked")
		@Override
		public Map<String, String> call() throws Exception {
			Map<String, String> result = new HashMap<String, String>();
			JSONObject jsonObj = PicWSInvokeTool.getPicfromWs(params.get("tp1"),"","","");
			if(jsonObj.containsKey("tp1")){
				result.put(params.get("tp1").toString(), URLDecoder.decode(jsonObj.getString("tp1"),"utf-8"));
			}
			return result;
		}
	}
}
