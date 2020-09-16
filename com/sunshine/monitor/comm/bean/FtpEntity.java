package com.sunshine.monitor.comm.bean;

import java.io.Serializable;

/**
* FTP-配置文件
* 对应文件 config/common.properties
* @author liumeng 2016-10-27 
*/
public class FtpEntity implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	private String url;
	private int port;
	private String encoding;
	private String username;
	private String password;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
}