package com.sunshine.monitor.comm.bean;

import java.io.Serializable;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * it资源平台
 * 对应文件 config/itresource.properties
 * @author liumeng
 * 2015-8-28 
 */
public class ITresource implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private String ip;
	private String port;
	private String userid;
	private String password;
	private String doStr;
	private String projectname;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDoStr() {
		return doStr;
	}
	public void setDoStr(String doStr) {
		this.doStr = doStr;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	
}