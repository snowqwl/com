package com.easymap.util;

import static com.easymap.util.Tools.getSafeObj;
import static com.easymap.util.Tools.getSafeStr;
import static com.easymap.util.Tools.isNotEmpty;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;

public class ElementUtils {

	public static Element getResultElements(ResultSet rs) {
		Element items = new Element("items");
		try {
			if (rs != null) {
				ResultSetMetaData rsm = rs.getMetaData();
				while (rs.next()) {
					Element item = new Element("item");
					for (int i1 = 0, size = rsm.getColumnCount(); i1 < size; ++i1) {
						String result = rsm.getColumnName(i1 + 1);
						Object obj = rs.getObject(result);
						item.setAttribute(result, obj == null ? "": obj.toString());
					}
					items.addContent(item);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return items;
	}
	public static Element getResultElements(List<Map<String,Object>> querylist) {
		Element items = new Element("items");
		for (Map<String, Object> map2 : querylist) {
			Set<Entry<String,Object>> entrySet = map2.entrySet();
			Element item = new Element("item");
			for (Entry<String, Object> entry : entrySet) {
				if(isNotEmpty(entry))
					item.setAttribute(getSafeStr(entry.getKey()), getSafeObj(entry.getValue()));
			}
			items.addContent(item);
		}
		return items;
	}
	public static Document responseDocument(Element items) {
		Document document = new Document();
		Element response = new Element("response");
		response.addContent(items);
		document.addContent(response);
		return document;
	}

}
