package com.sunshine.monitor.system.activemq.bean;


public class AlarmhandledMessage extends TransBean {
	private static final long serialVersionUID = 3946892832530910547L;
	private String type;
    private TransAlarmHandled alarmHandled;
	private String csxh;
    private String csdw;
    private String jsdw;
    private Double csbj;
    private String cssj;
    private String ywxh;
    private String sn;
   
	public AlarmhandledMessage() {
		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public TransAlarmHandled getAlarmHandled() {
		return alarmHandled;
	}
	public void setAlarmHandled(TransAlarmHandled alarmHandled) {
		this.alarmHandled = alarmHandled;
	}
	public String getCsxh() {
		return csxh;
	}
	public void setCsxh(String csxh) {
		this.csxh = csxh;
	}
	public String getCsdw() {
		return csdw;
	}
	public void setCsdw(String csdw) {
		this.csdw = csdw;
	}
	public String getJsdw() {
		return jsdw;
	}
	public void setJsdw(String jsdw) {
		this.jsdw = jsdw;
	}
	public Double getCsbj() {
		return csbj;
	}
	public void setCsbj(Double csbj) {
		this.csbj = csbj;
	}
	public String getCssj() {
		return cssj;
	}
	public void setCssj(String cssj) {
		this.cssj = cssj;
	}
	public String getYwxh() {
		return ywxh;
	}
	public void setYwxh(String ywxh) {
		this.ywxh = ywxh;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	   
   
}
