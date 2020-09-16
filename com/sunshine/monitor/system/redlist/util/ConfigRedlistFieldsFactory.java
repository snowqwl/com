package com.sunshine.monitor.system.redlist.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.monitor.comm.util.Common;
/**
 * 读取导入红名单Excel字段描述
 * @author OUYANG 2013/7/23
 *
 */
public class ConfigRedlistFieldsFactory {
	
	private static ConfigRedlistFieldsFactory configRedlist = null ;
	
	private Logger logger = LoggerFactory.getLogger(ConfigRedlistFieldsFactory.class);
	
	// 属性文件名
	private String property= "../fieldscfg.properties";
	
	private String _path = "" ;
	
	private String[] arys;
	
	private static final int len = 10;
	
	private Properties p = new Properties();
		
	private ConfigRedlistFieldsFactory(){
		
		_path = this.getClass().getResource(property).getPath();
		
		try {
			InputStream inStream = new FileInputStream(_path);
			p.load(inStream);
			inStream.close();
			String counts = p.getProperty("counts");
			int lenght = 0;
			if(!"".equals(counts) && counts.length() > 0) {
				lenght = Integer.parseInt(counts);
				arys = new String[lenght];
			} else {
				arys = new String[len];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ConfigRedlistFieldsFactory getConfigRedlist() {
		if(configRedlist == null) {
			configRedlist = new ConfigRedlistFieldsFactory();
		}
		return configRedlist;
	}
	
	public String[] getFieldsArray(){
		if("".equals(_path)) {
			logger.error("未初始化字段描述属性文件!");
			return null ;
		}
		String fields = p.getProperty("fields") ;
		String[] temp = fields.split(",") ;
		for(int i = 0; i < temp.length; i++) {
			String t = Common.formatColName(temp[i]);
			arys[i] = t;
		}
		return arys;
	}
	
	
	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args) {
		
		ConfigRedlistFieldsFactory.getConfigRedlist().getFieldsArray();
	}
	
}
