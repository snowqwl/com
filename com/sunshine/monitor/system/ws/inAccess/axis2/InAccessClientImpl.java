package com.sunshine.monitor.system.ws.inAccess.axis2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.system.manager.bean.CodeUrl;
import com.sunshine.monitor.system.ws.VehPassrecEntity;
import com.sunshine.monitor.system.ws.inAccess.InAccessClient;
import com.sunshine.monitor.system.ws.util.Dom4JUtils;
import com.sunshine.monitor.system.ws.util.NotDefPaserException;
import com.sunshine.monitor.system.ws.util.XmlBuild;

public class InAccessClientImpl implements InAccessClient{
	private String target;
	private CodeUrl codeUrl;
	private Logger debugLog = LoggerFactory.getLogger("debugLogger");
	private MappingInvocationHandler mappingInvocationHandler;

	public InAccessClientImpl(CodeUrl codeUrl){
		this.codeUrl = codeUrl;
		String target = String.format(
				"http://%1$s:%2$s/%3$s/services/InAccess", 
					codeUrl.getUrl(),
					codeUrl.getPort(),
					codeUrl.getContext());
		setTarget(target);
		
	}
	
	public List<VehPassrecEntity> executes(String systemType, String businessType,
			String sn, XmlBuild.InAccessXmlBuild build) 
			throws AxisFault, DocumentException, NotDefPaserException {
		RPCServiceClient client = new RPCServiceClient();
		Options ops = client.getOptions();
		EndpointReference targetEPR = 
			new EndpointReference(this.target);
		ops.setTo(targetEPR);
		QName qName = new QName(wsdlspace,"executes");
		
		String[] param = new String[]{
				systemType,businessType, sn, build.build()
		};
		
		Object[] results = client.invokeBlocking(qName, param, returnType);
		debugLog.debug(" InAccess WebService result ： "+results[0]);
		return converter(results[0].toString());
	}

	public void setTarget(String target) {
		this.target = target ;
	}
	
	private List<VehPassrecEntity> converter(String resultXml)
			throws DocumentException, NotDefPaserException {
		List<VehPassrecEntity> resultList = new ArrayList<VehPassrecEntity>();
		Document doucment = DocumentHelper.parseText(resultXml);
		Element root = doucment.getRootElement();
		String count = root.element("head").element("count").getTextTrim();
		if ("0".equals(count) || count == null) {
			return resultList;
		}
		Iterator<Element> it = root.element("body").elementIterator("data");
		while (it.hasNext()) {
			Element data = it.next();
			VehPassrecEntity entity = new VehPassrecEntity();
			autoMapping(data, entity);
			entity.setCity(codeUrl.getJdmc());
			if(mappingInvocationHandler != null)
				mappingInvocationHandler.invoke(this,data,entity);
			else
				autoMapping(data,entity);
			resultList.add(entity);
		}
		return resultList;
	}

	public void autoMapping(Element data, VehPassrecEntity entity)
			throws NotDefPaserException {
		Iterator<Element> it = data.elementIterator();
		Class clazz = entity.getClass();
		while (it.hasNext()) {
			Element element = it.next();
			String elName = element.getName();
			try {
				Field field = clazz.getDeclaredField(elName);
				Method method = clazz.getMethod("set"
						+ StringUtils.capitalize(elName), field.getType());
				method.invoke(entity, 
						Dom4JUtils.getValueTrim(element, field.getType()));
				/*
				 * NoSuchFieldException SecurityException NoSuchMethodException
				 * 认为没有对应映射字段或者方法
				 */
			} catch (NoSuchFieldException e) {
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
	}

	public MappingInvocationHandler getMappingInvocationHandler() {
		return mappingInvocationHandler;
	}

	public void setMappingInvocationHandler(
			MappingInvocationHandler mappingInvocationHandler) {
		this.mappingInvocationHandler = mappingInvocationHandler;
	}




	public interface MappingInvocationHandler {
		public VehPassrecEntity invoke(InAccessClientImpl client, 
				Element data, VehPassrecEntity entity) 
				throws NotDefPaserException ;
	}
}
