package com.easymap.pool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SqlProperties {
	
	public static ClassLoader LOADER;
	
	public Map<String,Map<String,String>> MODULE=new HashMap<String,Map<String,String>>();

	public void loadConfig(String paramString) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(paramString));
			StringBuffer sb = null;
			String key = null;
			Map<String,String> map = null;
			String str;
			while ((str = br.readLine()) != null) {
				str = str.trim();
				int i = str.length();
				if (i < 3)
					continue;
				int j = str.charAt(0);
				if (j == 35)
					continue;
				if (j == 91) {
					if (key != null)
						this.MODULE.put(key, map);
					key = str.substring(1, i - 1);
					map = new HashMap<String,String>();
				}
				if (str.charAt(i - 1) != ';') {
					if (sb == null)
						sb = new StringBuffer(str);
					else
						sb.append(str);
					sb.append(' ');
				}
				str = str.substring(0, i - 1);
				int k;
				if (sb != null) {
					sb.append(str);
					k = sb.indexOf("=");
					if (k != -1)
						map.put(sb.substring(0,k), sb.substring(k + 1));
					sb = null;
				} else {
					k = str.indexOf('=');
					if (k != -1)
						map.put(str.substring(0, k), str.substring(k + 1));
				}
			}
			if (key != null)
				this.MODULE.put(key, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getSql(String paramString1, String paramString2) {
		return this.MODULE.get(paramString1).get(paramString2); 
	}
}