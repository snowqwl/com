package com.ht.dc.trans.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FrameBean {
	public ArrayList obj;

	public FrameBean() {
		this.obj = new ArrayList();
	}

	public int addObj(FrameBean bean) {
		if (this.obj == null) {
			return 0;
		}
		this.obj.add(bean);
		return 1;
	}

	public String toXmlString() throws Exception {
		StringBuffer sb = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<BEAN>");
		Class c = getClass();
		Field[] properties = c.getDeclaredFields();
		for (int i = 0; i < properties.length; i++) {
			if (properties[i].getModifiers() == 2) {
				properties[i].setAccessible(true);
				Object tmp = properties[i].get(this);
				String strTmp = "";
				if (tmp != null) {
					if ((tmp instanceof String))
						strTmp = (String) tmp;
					else if ((tmp instanceof Double))
						strTmp = String.valueOf(tmp);
					else if ((tmp instanceof Long)) {
						strTmp = String.valueOf(tmp);
					}
				}
				if ((strTmp != null) && (!"".equals(strTmp))) {
					String name = properties[i].getName().toUpperCase();
					sb.append("<").append(name).append("><![CDATA[").append(
							strTmp).append("]]></").append(name).append(">");
				}
			}
		}
		if ((this.obj != null) && (this.obj.size() > 0)) {
			int size = this.obj.size();
			sb.append("<LIST>");
			for (int j = 0; j < size; j++) {
				FrameBean bean = (FrameBean) this.obj.get(j);
				sb.append(bean.getXmlBodyString());
			}
			sb.append("</LIST>");
		}
		sb.append("</BEAN>");
		return sb.toString();
	}

	public String getXmlBodyString() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<BEAN>");
		Class c = getClass();
		Field[] properties = c.getDeclaredFields();
		for (int i = 0; i < properties.length; i++) {
			if (properties[i].getModifiers() == 2) {
				properties[i].setAccessible(true);
				Object tmp = properties[i].get(this);
				String strTmp = "";
				if (tmp != null) {
					if ((tmp instanceof String))
						strTmp = (String) tmp;
					else if ((tmp instanceof Long)) {
						strTmp = String.valueOf(tmp);
					}
				}
				if ((strTmp != null) && (!"".equals(strTmp))) {
					String name = properties[i].getName().toUpperCase();
					sb.append("<").append(name).append("><![CDATA[").append(
							strTmp).append("]]></").append(name).append(">");
				}
			}
		}
		sb.append("</BEAN>");

		return sb.toString();
	}
}
