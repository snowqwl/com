package com.sunshine.monitor.system.ws.outAccess.transform;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

public class DataXMLToBean {
	
	public static <T> T transform(String xml,Class<T> clazz) throws Exception{
		
		if (clazz == null) {
			return null;
		}
		T obj = clazz.newInstance();
		xml = xml.trim(); 
		Document doc = DocumentHelper.parseText(xml);
		List<Node> list = doc.selectNodes("Parameters/InfoSet/Field");
		for (Iterator<Node> it = list.iterator(); it.hasNext();) {
			Node node = it.next();
			Node name = node.selectSingleNode("Name");
			Node value = node.selectSingleNode("Value");
			String key = name.getText().trim().toLowerCase();
			try{
				Method mtd = clazz.getMethod("set"+StringUtils.capitalize(key),
						new Class[] { String.class });
				mtd.invoke(obj, new Object[] { value.getText().trim() });
			}catch(NoSuchMethodException e){
				// 没有找到set方法的不处理
			}
		}
		return obj;
	}
}
