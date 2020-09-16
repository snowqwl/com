package com.sunshine.monitor.comm.util;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyUtil {
	private final static Logger LOG = Logger.getLogger(PropertyUtil.class);

	private static Map<Object, Object> settings = new HashMap<Object, Object>();

	public static void load(String name) {
		if (!isLoaded(name)) {
			Properties props = new Properties();

			URL propsUrl = Thread.currentThread().getContextClassLoader().getResource(name + ".properties");

			if (propsUrl == null) {
				throw new IllegalStateException(name + ".properties missing");
			}

			// Load settings
			try {
				props.load(propsUrl.openStream());
			} catch (IOException e) {
				throw new RuntimeException("Could not load " + name + ".properties:" + e);
			}
			LOG.debug(" Load properties file successed");

			settings.put(name, props);
		}
	}

	public static String get(String key) {
		for (Iterator<Object> iter = settings.keySet().iterator(); iter.hasNext();) {
			String propName = (String) iter.next();
			Properties props = (Properties) settings.get(propName);
			String value = (String) props.get(key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	public static String get(String propName, String key) {
		if (settings.get(propName) != null) {
			return ((Properties) settings.get(propName)).getProperty(key);
		}
		return null;
	}

	public static void clear() {
		settings.clear();

	}

	public static void reload() {
		Collection<Object> tempNames = new HashSet<Object>();
		tempNames.addAll(settings.keySet());
		settings.clear();

		for (Iterator<Object> iter = tempNames.iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			load(name);
		}
	}

	public static Map<?, ?> toMap() {
		Map<Object, Object> rtn = new HashMap<Object, Object>();
		for (Iterator<?> iterator = settings.keySet().iterator(); iterator.hasNext();) {
			Properties properties = (Properties) iterator.next();
			for (Iterator<?> iterator2 = properties.keySet().iterator(); iterator2.hasNext();) {
				String key = (String) iterator2.next();
				String value = properties.getProperty(key);
				rtn.put(key, value);

			}
		}
		return rtn;
	}

	private static boolean isLoaded(String name) {
		return settings.keySet().contains(name);
	}

	public static void main(String[] args) {
		PropertyUtil.load("interface");
		PropertyUtil.load("interface");
		PropertyUtil.reload();
		String path = PropertyUtil.get("outbound1backuppath");
		System.out.println(path);
	}
}
