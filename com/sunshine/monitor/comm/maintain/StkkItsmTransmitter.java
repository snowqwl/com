package com.sunshine.monitor.comm.maintain;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.core.util.SimpleXMLUtil;
import com.sunshine.monitor.comm.maintain.util.HttpClientUtil;
import com.sunshine.monitor.comm.util.Common;



public final class StkkItsmTransmitter implements AlarmTransmitter{
	/**
	 * flag
	 */
	private static final String FLAG = "itsm" ;
	
	private static Logger logger = LoggerFactory.getLogger(StkkItsmTransmitter.class);
	/**
	 * KYISTM interface
	 * 
	 */
	//正式运维接口
	private static final String KY_ITSM_URL="http://10.40.29.25/gayw/autoIncidentServlet" ;
	
	//测试运维接口
	//private static final String KY_ITSM_URL="http://10.40.29.26/gayw/autoIncidentServlet" ;

	public String getFlag() {
		return FLAG;
	}

	public String send(String[] targets, MaintainBean alarm) {
		//Equipment id

		//内容
		String content = alarm.getContent();
		//Alarm Grade
		//Business type
		String businessModule = alarm.getBusinessModule() ;
		//Event type
		String eventType= alarm.getEventType() ;
		//Title
		String title = alarm.getTitle() ;
		//联系人
		String linkMan = alarm.getLinkMan() ;
		//手机号
		String contact = alarm.getMobilePhone() ;
		//Area
		String area = alarm.getArea();
		
		
		
		//Manual post parameters
		NameValuePair[] datas = new NameValuePair[10];
		datas[0] = new NameValuePair("BUSINESSMODULE",businessModule);
		datas[1] = new NameValuePair("TYPE",eventType);
		datas[2] = new NameValuePair("TITLE","系统发送故障"+Common.getNow());
		datas[3] = new NameValuePair("DESCRIPTION",content);
		datas[4] = new NameValuePair("PROCESSLEVEL","01");
		datas[5] = new NameValuePair("SOURCE","3");
		datas[6] = new NameValuePair("LINKMAN",linkMan);
		datas[7] = new NameValuePair("CONTACT",contact);
		datas[8] = new NameValuePair("URGENCY","3");
		datas[9] = new NameValuePair("INCIDENTUNIQUEID",Common.getNow() + linkMan);
			
		
		HttpClient httpclient = HttpClientUtil.getHttpClient();
		httpclient.setConnectionTimeout(5000);
		httpclient.setTimeout(5000);
		String result = HttpClientUtil.postMethodDataResult(httpclient,KY_ITSM_URL,datas,"UTF-8");
		//Check data
		if(result==null || "".equals(result)) {
			logger.error("ITSM interface recive Exception ...");
			return "2";	
		}
		//XML data
		Document document = SimpleXMLUtil.string2Doc(result);
		Element root = document.getRootElement();
		Element codeElement = (Element)root.getChildren("code").get(0);
		String code =codeElement.getText();
		Element msgElement = (Element)root.getChildren("message").get(0);
		String message =msgElement.getText();
		if("0".equals(code)){
			logger.info("Result is number 0 : excute success " );
			
			return "0";
		}else{
			logger.error("Resulst is not number 0 : excute failure " );
			logger.error("excute failure message ---> " + message);
			
			return "1";
		}
	}
}
