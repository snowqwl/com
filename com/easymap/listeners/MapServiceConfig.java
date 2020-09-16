package com.easymap.listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class MapServiceConfig {
	
	private static Logger logger = LoggerFactory.getLogger(MapServiceConfig.class);
	private List<Layer> layers = new ArrayList<Layer>();

	@SuppressWarnings("unchecked")
	public void loadConfig(InputStream paramInputStream) {
		try {
			InputSource input = new InputSource(paramInputStream);
			SAXBuilder sax = new SAXBuilder();
			Document localDocument = sax.build(input);
			Element element1 = localDocument.getRootElement();
			List list = element1.getChildren();
			for (int i = 0; i < list.size(); ++i) {
				Layer layer = new Layer();
				Element localElement2 = (Element) list.get(i);
				layer.setUrl(localElement2.getAttributeValue("url"));
				layer.setTable(localElement2.getAttributeValue("table"));
				layer.setCode(localElement2.getChildText("code"));
				layer.setName(localElement2.getChildText("name"));
				layer.setShape(localElement2.getChildText("shape"));
				this.layers.add(layer);
			}
		} catch (JDOMException e) {
			logger.error(e.getMessage());
		} catch (IOException io) {
			logger.error(io.getMessage());
		}
	}

	public List<Layer> getLayers() {
		return this.layers;
	}
}

/*
 * Location:
 * C:\Users\Administrator\Desktop\gps产品\gpsServer.ear\gpsServer.war\WEB
 * -INF\lib\gpsServer.V2.0.5.201108301200\ Qualified Name:
 * com.easymap.rulesmanager.model.MapServiceConfig JD-Core Version: 0.5.4
 */