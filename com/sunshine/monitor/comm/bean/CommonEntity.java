package com.sunshine.monitor.comm.bean;

import java.io.Serializable;

/**
* 机动车模型图片-配置文件
* 对应文件 config/common.properties
* @author liumeng 2016-9-19 
*/
public class CommonEntity implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	private String ip;
	private String filename;
	private String username;
	private String password;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
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
