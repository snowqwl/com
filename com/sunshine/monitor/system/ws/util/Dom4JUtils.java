package com.sunshine.monitor.system.ws.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class Dom4JUtils {
	Long i = 0l;
	private static Map<String,Parse> configMap = new HashMap<String,Parse>();
	
	private Dom4JUtils(){}
	
	static{
		configMap.put("long", LongParse.INSTANCE);
		configMap.put(Long.class.getName(), LongParse.INSTANCE);
		configMap.put(String.class.getName(), StringParse.INSTANCE);
	}
	
	public static <T> T getValueTrim(Element el, Class<T> clazz) 
				throws NotDefPaserException {
		String text = el.getTextTrim();
		Parse parse = configMap.get(clazz.getName());
		if(parse == null) {
			throw new NotDefPaserException(
				Dom4JUtils.class.getName()+"沒有找到对应的转换器 sting to "+
				clazz.getName());
		}
		T value = configMap.get(clazz.getName()).<T>execute(text);
		return value;
	}
	
	public static boolean isBlank(String text){
		return StringUtils.isBlank(text) || "null".equals(text);
	}

	public interface Parse{
		<T> T execute(String text);
	}
	
	public static class LongParse implements Parse{
		public static LongParse INSTANCE = new LongParse();
		
		private LongParse(){}

		public Long execute(String text) {
			if(isBlank(text)) return 0l;
			return Long.parseLong(text);
		}
	}
	
	public static class StringParse implements Parse{
		public static StringParse INSTANCE = new StringParse();
		
		private StringParse(){}

		public String execute(String text) {
			if(isBlank(text)) return "";
			return text;
		}
		
	}
}
