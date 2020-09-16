package com.easymap.dao.load;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import com.easymap.listeners.InitListener;
import com.easymap.pool.DBConnection;
import com.easymap.pool.SqlProperties;

@SuppressWarnings("unchecked")
public class LoadDao {
	
	
	private SqlProperties property = null;
	private Element easyxml;
	private Map map;

	public LoadDao(SqlProperties property) {
		this.property = property;
		this.easyxml = createRootElement();
		this.map = ((Map) this.property.MODULE.get("load"));
	}

	public Document getResponseDocument() {
		Connection conn = null;
		Document document = new Document();
		try {
			conn = DBConnection.getJDBCConnection();
			this.easyxml.addContent(createVersion());
			this.easyxml.addContent(createTrackServer());
			this.easyxml.addContent(createStyle(conn));
			this.easyxml.addContent(createPoliceType(conn));
			this.easyxml.addContent(createLocalType(conn));
			this.easyxml.addContent(createAlarmType(conn));
		} catch (Exception e) {
			this.easyxml = createRootElement();
			this.easyxml.addContent(createErrElement(e.getMessage()));
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		document.addContent(this.easyxml);
		return document;
	}

	private Element createAlarmType(Connection paramConnection) {
		Element element1 = createResponseElement();
		String str1 = "getAlarmType";
		element1.setAttribute("key", str1);
		String str2 = this.map.get(str1).toString();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = paramConnection.prepareStatement(str2);
			rs = ps.executeQuery();
			if (rs != null) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int i = rsmd.getColumnCount();
				while (rs.next()) {
					Element element2 = new Element("item");
					for (int j = 0; j < i; ++j) {
						String str3 = rsmd.getColumnName(j + 1);
						element2.setAttribute(str3, (rs.getString(str3) == null) ? "" : rs.getString(str3));
					}
					element1.addContent(element2);
				}
			}
		} catch (SQLException localSQLException) {
			localSQLException.printStackTrace();
		}
		return element1;
	}

	private Element createVersion() throws Exception {
		Element element = createResponseElement();
		try {
			element.setAttribute("key", "getVersion");
			String str = InitListener.configProperties.getProperty("gpsversion");
			element.setAttribute("value", str);
		} catch (Exception localException) {
			throw new Exception("");
		}
		return element;
	}

	private Element createTrackServer() throws Exception {
		Element element = createResponseElement();
		try {
			element.setAttribute("key", "getTrackServer");
			String ip = InitListener.configProperties.getProperty("trackserver.ip").toString();
			String port = InitListener.configProperties.getProperty("gpstcp.port").toString();
			String controlPort = InitListener.configProperties.getProperty("gpsudp.controlPort").toString();
			String dataPort = InitListener.configProperties.getProperty("gpsudp.dataPort").toString();
			element.setAttribute("trackServerIP", ip);
			element.setAttribute("tcpPort", port);
			element.setAttribute("udpControlPort", controlPort);
			element.setAttribute("udpDataPort", dataPort);
		} catch (Exception localException) {
			throw new Exception("�����켣�������");
		}
		return element;
	}

	
	@SuppressWarnings("unused")
	private Element createTree(Element paramElement, Connection paramConnection)throws Exception {
		Element element1 = createResponseElement();
		String key = paramElement.getAttributeValue("key");
		String str2 = this.map.get(key).toString();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			List list = paramElement.getChildren("param");
			ps = paramConnection.prepareStatement(str2);
			Element element2;
			Object localObject;
			for (int i = 0; i < list.size(); ++i) {
				element2 = (Element) list.get(i);
				localObject = (Attribute) element2.getAttributes().get(0);
				ps.setString(i + 1,((Attribute) localObject).getValue());
			}
			rs = ps.executeQuery();
			if (rs != null) {
				Map map = new HashMap();
				while (rs.next()) {
					element2 = new Element("item");
					localObject = rs.getString("orgname").trim();
					String str3 = rs.getString("orgid").trim();
					element2.setAttribute("orgname", (String) localObject);
					element2.setAttribute("orgid", str3);
					map.put(str3, element2);
					String str4 = rs.getString("parentid");
					Element element3 = (Element) map.get(str4);
					if (element3 == null)
						element1.addContent(element2);
					else
						element3.addContent(element2);
				}
			}
		} catch (SQLException e) {
			throw new Exception("创建响应XML失败");
		}
		return element1;
	}

	private Element createStyle(Connection paramConnection) throws Exception {
		Element element1 = createResponseElement();
		element1.setAttribute("key", "gpsStyle");
		PreparedStatement ps = null;
		ResultSet rs = null;
		String str1 = this.map.get("getStyle").toString();
		try {
			ps = paramConnection.prepareStatement(str1);
			rs = ps.executeQuery();
			Map map = new HashMap();
			while (rs.next()) {
				Element element2 = new Element("item");
				int i = rs.getInt("GPSSTYLEID");
				String str2 = rs.getString("TYPENAME");
				int j = rs.getInt("ORDERINDEX");
				int k = rs.getInt("PARENTID");
				element2.setAttribute("GPSSTYLEID", String.valueOf(i));
				element2.setAttribute("TYPENAME", str2);
				element2.setAttribute("ORDERINDEX", String.valueOf((j == 0) ? "" : Integer.valueOf(j)));
				element2.setAttribute("PARENTID", String.valueOf((k == 0) ? "" : Integer.valueOf(k)));
				Element element3 = (Element) map.get(Integer.valueOf(k));
				if (element3 == null)
					element1.addContent(element2);
				else
					element3.addContent(element2);
				map.put(Integer.valueOf(i), element2);
			}
		} catch (SQLException e) {
			throw new Exception("获取接入类型失败");
		}
		return element1;
	}

	private Element createPoliceType(Connection conn)throws Exception {
		
		Element element1 = createResponseElement();
		element1.setAttribute("key", "gpsPoliceType");
		PreparedStatement ps = null;
		ResultSet rs = null;
		String str1 = this.map.get("getPoliceType").toString();
		try {
			ps = conn.prepareStatement(str1);
			rs = ps.executeQuery();
			Map map = new HashMap();
			while (rs.next()) {
				Element element2 = new Element("item");
				int i = rs.getInt("POLICETYPEID");
				String str2 = rs.getString("POLICETYPENAME");
				int j = rs.getInt("ORDERINDEX");
				int k = rs.getInt("PARENTID");
				element2.setAttribute("POLICETYPEID", String.valueOf(i));
				element2.setAttribute("POLICETYPENAME", str2);
				element2.setAttribute("ORDERINDEX", String.valueOf((j == 0) ? "" : Integer.valueOf(j)));
				element2.setAttribute("PARENTID", String.valueOf((k == 0) ? "" : Integer.valueOf(k)));
				Element element3 = (Element) map.get(Integer.valueOf(k));
				if (element3 == null)
					element1.addContent(element2);
				else
					element3.addContent(element2);
				map.put(Integer.valueOf(i), element2);
			}
		} catch (SQLException e) {
			throw new Exception("获取警种类型失败");
		}
		return element1;
	}

	private Element createLocalType(Connection conn)throws Exception {
		Element element1 = createResponseElement();
		element1.setAttribute("key", "gpsLocalType");
		PreparedStatement ps = null;
		ResultSet rs = null;
		String str1 = this.map.get("getLocalType").toString();
		try {
			ps = conn.prepareStatement(str1);
			rs = ps.executeQuery();
			Map map = new HashMap();
			while (rs.next()) {
				Element element2 = new Element("item");
				int i = rs.getInt("LOCALTYPEID");
				String str2 = rs.getString("LOCALTYPENAME");
				int j = rs.getInt("ORDERINDEX");
				int k = rs.getInt("PARENTID");
				element2.setAttribute("LOCALTYPEID", String.valueOf(i));
				element2.setAttribute("LOCALTYPENAME", str2);
				element2.setAttribute("ORDERINDEX", String.valueOf((j == 0) ? "" : Integer.valueOf(j)));
				element2.setAttribute("PARENTID", String.valueOf((k == 0) ? "" : Integer.valueOf(k)));
				Element element3 = (Element) map.get(Integer.valueOf(k));
				if (element3 == null)
					element1.addContent(element2);
				else
					element3.addContent(element2);
				map.put(Integer.valueOf(i), element2);
			}
		} catch (SQLException e) {
			throw new Exception("获取定位类型失败");
		}
		return element1;
	}

	private Element createRootElement() {
		Element element = new Element("easyxml");
		return element;
	}

	private Element createResponseElement() {
		Element element = new Element("response");
		return element;
	}

	private Element createErrElement(String paramString) {
		Element element = new Element("error");
		element.setAttribute("text", paramString);
		return element;
	}
}

/*
 * Location:
 * C:\Users\Administrator\Desktop\gps产品\gpsServer.ear\gpsServer.war\WEB
 * -INF\lib\gpsServer.V2.0.5.201108301200\ Qualified Name:
 * com.easymap.dao.LoadDao JD-Core Version: 0.5.4
 */