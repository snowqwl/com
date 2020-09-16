package com.sunshine.monitor.comm.bean;

import java.io.Serializable;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * Super JavaBean
 * All of Application Beans inherit from Super JavaBean
 * Functions:Firt)JavaBean to XML Format
 *         second)JavaBean to JSON Format
 * SubClass add get or set Method
 * @author YANGYANG
 * 
 */
public class Entity implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	/**
	 * 将实体类的字段及值转化为XML字符串格式
	 * 需要xom.jar支持
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <root>
	 * <attribute type="typeName">value1</attribute>
	 * <attribute type="typeName">value2</attribute>
	 * </root>
	 * @return
	 */
	public String objectToXML() {
		XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setRootName("root");
		return xmlSerializer.write(JSONObject.fromObject(this));
	}

	/**
	 * 将实体类的字段及值转化为JSON字符串格式
	 * {"Attribute1":value1,"Attribute2":value2}
	 */
	@Override
	public String toString() {
		return JSONObject.fromObject(this).toString();
	}

	/**
	 * 将实体类的字段及值转化为JSON字符串格式
	 * {"Attribute1":value1,"Attribute2":value2}
	 */
	public String objectToJson() {
		return JSONObject.fromObject(this).toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}