package com.sunshine.monitor.comm.maintain;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.maintain.enums.SmsCodeEnum;
import com.sunshine.monitor.comm.maintain.util.FileSystemResource;
import com.sunshine.monitor.comm.maintain.util.NameValuePairUtil;

/**
 * Http Request implements request method is post or get
 * 
 * @author Administrator
 */
public abstract class AbstractHttpSmsTransmitter implements ISmsTransmitter {

	private static String url;
	private static String charset;

	/**
	 * constant variable
	 */
	private static final String KEY_URL = "url";
	private static final String KEY_CHARASET = "charaset";

	/**
	 * http request parameters
	 */
	private static Map<String, String> paras;
	/**
	 * basic parameters
	 */
	private static Map<String, String> basics;

	/**
	 * property file
	 */
	public static String fileName = "sms.properties";

	/**
	 * property file key prefix
	 */
	public static final String SMS_PREFIX = "sms.";

	private static final String SMS_PARA_PREFIX = "sms.para.";

	private static final String SMS_BASIC_PREFIX = "sms.basic.";

	private static final Logger log = LoggerFactory
			.getLogger(AbstractHttpSmsTransmitter.class);

	static {
		init();
		log.info("SMS接口配置信息加载成功!");
	}

	public static void flushMemery() {
		init();
		log.info("SMS接口配置信息刷新成功!");
	}

	/**
	 * Implements by defualt
	 */
	@Override
	public final boolean send(Map map, SmsTransmitterCallBack call) throws Exception{
		Map tmap = new NameValuePairUtil();
		tmap.putAll(map);
		// Add basic information
		tmap.putAll(paras);
		// Excute short message send
		String rdata = "";
		try {
			rdata = sendSms(tmap);
			System.out.println("手机号码:"+tmap.get("mobile")+",发送内容:"+tmap.get("msg_content")+",返回码:"+rdata);
		} catch (Exception e) {
			rdata = e.getMessage();
			e.printStackTrace();
		}
		// Analysis reautn data
		boolean flag = isSuccess(rdata);
		// Add send status
		map.put("status", String.valueOf(flag));
		if (!flag)
			map.put("errorMsg", "错误码:" + rdata + ":"
					+ SmsCodeEnum.getDesc(rdata));
		if (call != null) {
			call.doCallBack(map);
		}
		return flag;
	}

	/**
	 * Sub class implement
	 * 
	 * @return
	 */
	public abstract String sendSms(Map map);

	/**
	 * Checking url property and url value is http protocol
	 * 
	 * @return
	 */
	public String getUrl() {
		if (url == null) {
			if (basics.containsKey(KEY_URL))
				url = basics.get(KEY_URL);
			if (url == null || "".equals(url))
				throw new IllegalArgumentException(
						"sms.properties属性文件未配置属性sms.basic.url!");
			if (!url.toLowerCase().startsWith("http://"))
				throw new IllegalArgumentException(
						"sms.properties属性文件属性sms.basic.url是不正确HTTP协议!");
		}
		return url;
	}

	/**
	 * Get Charaset
	 * 
	 * @return
	 */
	public String getCharset() {
		if (charset == null) {
			if (basics.containsKey(KEY_CHARASET))
				charset = basics.get(KEY_CHARASET);
			if (charset == null || "".equals(charset))
				throw new IllegalArgumentException(
						"sms.properties属性文件未配置属性sms.basic.charaset!");
		}
		return charset;
	}

	/**
	 * Check reault
	 * 
	 * @param resultData
	 * @return
	 */
	private boolean isSuccess(String resultData) {
		if (resultData == null || "".equals(resultData)) {
			log.info("短息返回结果异常!");
			return false;
		}
		if (resultData.startsWith("0#")) {
			return true;
		} else {
			log.error("短息接口返回错误码:" + resultData + ",错误信息："
					+ SmsCodeEnum.getDesc(resultData));
		}
		return false;
	}

	/**
	 * init
	 */
	private static void init() {
		paras = new HashMap<String, String>();
		basics = new HashMap<String, String>();
		Properties property = FileSystemResource.getProperty(fileName);
		Enumeration<Object> enums = property.keys();
		while (enums.hasMoreElements()) {
			String key = (String) enums.nextElement();
			if (key != null && key.startsWith(SMS_PREFIX)) {
				if (key.startsWith(SMS_PARA_PREFIX))
					paras.put(key.substring(9).toLowerCase(), property
							.getProperty(key));
				if (key.startsWith(SMS_BASIC_PREFIX))
					basics.put(key.substring(10).toLowerCase(), property
							.getProperty(key));
			}
		}
	}
}
