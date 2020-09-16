package com.sunshine.monitor.system.manager.quartz;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis2.AxisFault;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.sunshine.monitor.comm.bean.StatSystem;
import com.sunshine.monitor.comm.util.Ping;
import com.sunshine.monitor.comm.util.SpringApplicationContext;
import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.manager.dao.SystemDao;
import com.sunshine.monitor.system.manager.dao.UrlDao;
import com.sunshine.monitor.system.ws.ConnectionService.CitySituationService;
import com.sunshine.monitor.system.ws.util.AddHeaderInterceptor;

public class CityReport {

	/**
	 * 定时监测各地市的连接情况
	 * 
	 * @author panyongs
	 * 
	 */

	public void setSurvey() throws AxisFault{
		UrlDao urlDao = (UrlDao) SpringApplicationContext
				.getStaticApplicationContext().getBean("urlDao");
		SystemDao systemDao = (SystemDao) SpringApplicationContext
				.getStaticApplicationContext().getBean("systemDao");

		// 判断操作系统类型
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name").toLowerCase();
		String ostype;
		if (os.startsWith("win")) {
			// windows操作系统
			ostype = "1";
		} else {
			// Linux操作系统
			ostype = "2";
		}
		List<CodeUrl> lc =null;
		try {
			lc = urlDao.getCodeUrls();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		for (CodeUrl cu : lc) {
			StatSystem ss = new StatSystem();
			ss.setDwdm(cu.getDwdm());
			ss.setDwmc(cu.getJdmc());
			// 应用服务器状态，ping IP地址
			String jq = Ping.test(cu.getUrl(), ostype);
			ss.setJq(jq);
			if (!"1".equals(jq)) {
				ss.setXt("-1");
				ss.setJr("-1");
				systemDao.updateStatSystem(ss);
			} else {
				// 应用程序状态，调用webservice接口
				try {
					JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
					factory.setServiceClass(CitySituationService.class);
					// Request Informations
					factory.getOutInterceptors().add(
							new LoggingOutInterceptor());
					// Response Informations
					factory.getInInterceptors().add(new LoggingInInterceptor());
					factory.getOutInterceptors()
							.add(new AddHeaderInterceptor());
					String wsurl = "http://" + cu.getUrl() + ":" + cu.getPort()
							+ "/" + cu.getContext()
							+ "/service/CitySituationService";
					factory.setAddress(wsurl);
					CitySituationService service = (CitySituationService) factory
							.create();

					Client proxy = ClientProxy.getClient(service);
					HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
					HTTPClientPolicy policy = new HTTPClientPolicy();
					// 设置连接超时 3000ms
					policy.setConnectionTimeout(3000);
					// 设置请求超时
					policy.setReceiveTimeout(30000);
					conduit.setClient(policy);

					String s = service.cityResponse();
					ss.setXt(s);
				} catch (Exception ex) {
					if (ex instanceof WebServiceException) {
						WebServiceException e1 = (WebServiceException) ex;
						String msg2 = e1.getCause().getMessage();
						if (msg2.indexOf("Read timed out") != -1) {
							ss.setXt("-1");
						} else if (msg2.indexOf("404: Not Found") != -1) {
							ss.setXt("0");
						} else if (msg2.indexOf("503: Service Unavailable") != -1) {
							ss.setXt("-2");
						} else if (msg2.indexOf("认证错误") != -1) {
							ss.setXt("-2");
						} else {
							ss.setXt("-9");
						}
					} else {
						ss.setXt("-9");
					}
				} catch(NoSuchMethodError er){
					System.out.println("**********************");
					ss.setXt("0");
					//er.printStackTrace();
				}
				
				try {
					// 接入状态，调用webservice接口
					if ((cu.getBz() != null) && (cu.getBz().length() > 1)) {
						String url = "http://" + cu.getBz()
								+ "/jcbktrans/services/hole?wsdl";
						HttpURLConnection conn = null;
						boolean isConn = false;
						try{
							conn = (HttpURLConnection) new URL(url).openConnection();
							int code = conn.getResponseCode();
							if(code == 200)isConn = true;
						}catch(Throwable t ){
							if(conn != null){
								try{
								conn.disconnect();
								}catch(Throwable t1){}
							}
						}
					/*	Service service = new Service();
						Call call = (Call) service.createCall();
						call.setTargetEndpointAddress(url);
						call.setOperationName(url,new QName("querySyncTime"));
						call.setReturnType(XMLType.XSD_STRING);
						call.setUseSOAPAction(true);
						call.setTimeout(3000);
						call.setSOAPActionURI("http://www.wj.com/Rpc");
						String resultxml = (String) call.invoke(new Object[0]);
						if ((resultxml != null) && (resultxml.length() > 1)
								&& (resultxml.substring(0, 2).equals("20")))*/
						if(isConn)
							ss.setJr("1");
						else
							ss.setJr("-2");

					} else {
						ss.setJr("-9");
					}
					systemDao.updateStatSystem(ss);

				} catch (Exception e) {
					ss.setJr("-1");
					systemDao.updateStatSystem(ss);
				}
			}
		}
	}
}
