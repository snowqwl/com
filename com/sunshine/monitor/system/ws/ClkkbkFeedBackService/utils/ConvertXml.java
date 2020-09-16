package com.sunshine.monitor.system.ws.ClkkbkFeedBackService.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import com.sunshine.monitor.system.alarm.bean.VehAlarmHandled;
import com.sunshine.monitor.system.alarm.bean.VehAlarmrec;
import com.sunshine.monitor.system.ws.ClkkbkFeedBackService.bean.TransFeedback;

public class ConvertXml {
	private ConvertXml(){}
	
	public static String getAlarmXml(VehAlarmrec bean) {
		StringBuffer sb = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\" UTF-8\"?>");
		sb.append("<Parameters>");
		sb.append("<InfoSet>");
		sb.append("<Field><Name>Bjxh</Name><Value>" + bean.getBjxh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Bjlx</Name><Value>" + bean.getBjlx()
				+ "</Value></Field>");
		sb.append("<Field><Name>Bkxh</Name><Value>" + bean.getBkxh()
				+ "</Value></Field>");
		String tmp = "";
		char[] cs = bean.getBjsj().toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if ((cs[i] != '-') && (cs[i] != ' ')) {
				tmp = tmp + cs[i];
			}
		}
		sb.append("<Field><Name>Bjsj</Name><Value>" + tmp + "</Value></Field>");
		sb.append("<Field><Name>Bjdwdm</Name><Value>" + bean.getBjdwdm()
				+ "</Value></Field>");
		sb.append("<Field><Name>Bjdwmc</Name><Value>" + bean.getBjdwmc()
				+ "</Value></Field>");
		sb.append("<Field><Name>Bjdwlxdh</Name><Value>" + bean.getBjdwlxdh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Hphm</Name><Value>" + bean.getHphm()
				+ "</Value></Field>");
		sb.append("<Field><Name>Hpzl</Name><Value>" + bean.getHpzl()
				+ "</Value></Field>");
		sb.append("<Field><Name>Gcxh</Name><Value>" + bean.getGcxh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Sbbh</Name><Value>" + bean.getSbbh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Sbmc</Name><Value>" + bean.getSbmc()
				+ "</Value></Field>");
		sb.append("<Field><Name>Kdbh</Name><Value>" + bean.getKdbh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Kdmc</Name><Value>" + bean.getKdmc()
				+ "</Value></Field>");
		sb.append("<Field><Name>Fxbh</Name><Value>" + bean.getFxbh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Fxmc</Name><Value>" + bean.getFxmc()
				+ "</Value></Field>");
		sb.append("<Field><Name>Cllx</Name><Value>" + bean.getCllx()
				+ "</Value></Field>");
		sb.append("<Field><Name>Clsd</Name><Value>" + bean.getClsd()
				+ "</Value></Field>");
		sb.append("<Field><Name>Hpys</Name><Value>" + bean.getHpys()
				+ "</Value></Field>");
		sb.append("<Field><Name>Cwhphm</Name><Value>" + bean.getCwhphm()
				+ "</Value></Field>");
		sb.append("<Field><Name>Cwhpys</Name><Value>" + bean.getCwhpys()
				+ "</Value></Field>");
		sb.append("<Field><Name>Hpyz</Name><Value>" + bean.getHpyz()
				+ "</Value></Field>");
		sb.append("<Field><Name>Cdbh</Name><Value>" + bean.getCdbh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Clwx</Name><Value>" + bean.getClwx()
				+ "</Value></Field>");
		sb.append("<Field><Name>Csys</Name><Value>" + bean.getCsys()
				+ "</Value></Field>");
		sb.append("<Field><Name>Tp1</Name><Value>" + bean.getTp1()
				+ "</Value></Field>");
		sb.append("<Field><Name>Tp2</Name><Value>" + bean.getTp2()
				+ "</Value></Field>");
		sb.append("<Field><Name>Tp3</Name><Value>" + bean.getTp3()
				+ "</Value></Field>");
		sb.append("<Field><Name>Qrr</Name><Value>" + bean.getQrr()
				+ "</Value></Field>");
		sb.append("<Field><Name>Qrrjh</Name><Value>" + bean.getQrrjh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Qrdwdm</Name><Value>" + bean.getQrdwdm()
				+ "</Value></Field>");
		sb.append("<Field><Name>Qrdwdmmc</Name><Value>" + bean.getQrdwdmmc()
				+ "</Value></Field>");
		sb.append("<Field><Name>Qrdwlxdh</Name><Value>" + bean.getQrdwlxdh()
				+ "</Value></Field>");
		tmp = "";
		cs = bean.getQrsj().toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if ((cs[i] != '-') && (cs[i] != ' ')) {
				tmp = tmp + cs[i];
			}
		}
		sb.append("<Field><Name>Qrsj</Name><Value>" + tmp + "</Value></Field>");
		sb.append("<Field><Name>Qrzt</Name><Value>" + bean.getQrzt()
				+ "</Value></Field>");
		sb.append("<Field><Name>Qrjg</Name><Value>" + bean.getQrjg()
				+ "</Value></Field>");
		sb.append("</InfoSet>");
		sb.append("</Parameters>");
		return sb.toString();
	}

	public static String getHandledXml(VehAlarmHandled bean) {
		StringBuffer sb = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\" UTF-8\"?>");
		sb.append("<Parameters>");
		sb.append("<InfoSet>");
		sb.append("<Field><Name>Fkbh</Name><Value>" + bean.getFkbh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Bkxh</Name><Value>" + bean.getBkxh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Bjxh</Name><Value>" + bean.getBjxh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Gcxh</Name><Value>" + bean.getGcxh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Lrr</Name><Value>" + bean.getLrr()
				+ "</Value></Field>");
		sb.append("<Field><Name>Lrrjh</Name><Value>" + bean.getLrrjh()
				+ "</Value></Field>");
		sb.append("<Field><Name>Lrdwdm</Name><Value>" + bean.getLrdwdm()
				+ "</Value></Field>");
		sb.append("<Field><Name>Lrdwmc</Name><Value>" + bean.getLrdwmc()
				+ "</Value></Field>");
		String tmp = "";
		char[] cs = bean.getLrsj().toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if ((cs[i] != '-') && (cs[i] != ' ')) {
				tmp = tmp + cs[i];
			}
		}
		sb.append("<Field><Name>Lrsj</Name><Value>" + tmp + "</Value></Field>");
		sb.append("<Field><Name>Ddr</Name><Value>" + bean.getDdr()
				+ "</Value></Field>");
		sb.append("<Field><Name>Xbr</Name><Value>" + bean.getXbr()
				+ "</Value></Field>");
		sb.append("<Field><Name>Sflj</Name><Value>" + bean.getSflj()
				+ "</Value></Field>");

		sb.append("<Field><Name>Wljdyy</Name><Value>" + bean.getWljdyy()
				+ "</Value></Field>");
		sb.append("<Field><Name>Clgc</Name><Value>" + bean.getClgc()
				+ "</Value></Field>");
		tmp = "";
		cs = bean.getBjsj().toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if ((cs[i] != '-') && (cs[i] != ' ')) {
				tmp = tmp + cs[i];
			}
		}
		sb.append("<Field><Name>Bjsj</Name><Value>" + tmp + "</Value></Field>");
		sb.append("<Field><Name>Bjdwdm</Name><Value>" + bean.getBjdwdm()
				+ "</Value></Field>");
		sb.append("<Field><Name>Bjdwmc</Name><Value>" + bean.getBjdwmc()
				+ "</Value></Field>");
		sb.append("<Field><Name>Bjdwlxdh</Name><Value>" + bean.getBjdwlxdh()
				+ "</Value></Field>");
		sb.append("</InfoSet>");
		sb.append("</Parameters>");
		return sb.toString();
	}
	
	public static TransFeedback getTransFeedback(String returnXml, String type) {
		TransFeedback feedback = new TransFeedback();
		if ((returnXml == null) || ("".equals(returnXml))) {
			feedback.setCsjg("0");
		} else {
			try {
				Document doc = DocumentHelper.parseText(returnXml);
				Node result_node = doc
						.selectSingleNode("/ResultSet/ReturnValue");
				if ((result_node != null) && (result_node.getText() != null)
						&& (!"".equals(result_node.getText()))) {
					if ("000".equals(result_node.getText()))
						feedback.setCsjg("1");
					else
						feedback.setCsjg("0");
				} else {
					feedback.setCsjg("0");
				}
			} catch (Exception e) {
				e.printStackTrace();
				feedback.setCsjg("0");
			}
		}
		feedback.setFknr(returnXml);
		feedback.setLb(type);
		feedback.setXxly("5");
		feedback.setBz("");
		return feedback;
	}
}
