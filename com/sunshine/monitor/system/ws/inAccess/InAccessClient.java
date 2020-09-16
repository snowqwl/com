package com.sunshine.monitor.system.ws.inAccess;

import java.util.List;

import com.sunshine.monitor.system.ws.VehPassrecEntity;
import com.sunshine.monitor.system.ws.util.XmlBuild;

/**
 * 对应webservice 远程server InAccess 的客戶端
 * @author lifenghu
 *
 */
public interface InAccessClient {
	final String wsdlspace = "http://webservice.jcbk.ht.com";
	final Class[] returnType = {String.class};
	/**
	 * server InAccess 下 executes 方法調用
	 * @return 返回业务类型
	 */
	List<VehPassrecEntity> executes(String SystemType, String BusinessType,
			String sn, XmlBuild.InAccessXmlBuild build) throws Exception;
	
	/**
	 * 设置服务访问目标url
	 * @param target
	 */
	void setTarget(String target);
}
