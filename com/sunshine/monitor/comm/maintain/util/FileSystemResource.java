package com.sunshine.monitor.comm.maintain.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 资源文件系统加载器
 * 【注意】 读取CLASSPATH路径下文件,不以"/"开头
 *      如：com/sunshine/test.properties
 * @author OUYANG
 * @version V1.0.1
 * @since 2015/07/09
 */
public abstract class FileSystemResource {

	/**
	 * 以流方式读取属性文件
	 * @param fname
	 * @return
	 */
	public static InputStream getInputStream(String fname) {
		InputStream input = null;
		String file = resoveFileName(fname);
		// 直接从CLASSPATH读取属性文件
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null)
			input = classLoader.getResourceAsStream(file);
		return input;
	}

	/**
	 * 读取属性文件
	 * @param fname
	 * @return
	 */
	public static Properties getProperty(String fname) {
		Properties property = null;
		InputStream input = null;
		try {
			input = getInputStream(fname);
			property = new Properties();
			if(input == null)
				throw new Exception("文件流加载失败!");
			property.load(input);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return property;
	}
	
	/**
	 * 解析文件，去掉以"/"开头
	 * @param fname
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static String resoveFileName(String fname)
			throws IllegalArgumentException {
		if (fname != null && !"".equals(fname)){
			if(fname.startsWith("/")){
				return fname.substring(1);
			}
			return fname;
		}
		throw new IllegalArgumentException("文件名为空!");
	}
}
