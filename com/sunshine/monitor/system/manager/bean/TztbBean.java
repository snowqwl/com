package com.sunshine.monitor.system.manager.bean;

/**
 * 首页-通知通报
 * @author liumeng 2016-5-31
 *
 */
public class TztbBean {
	private String id;
	private String title;
	private String lb;
	private String fileurl;
	private String content;
	private String cjr;
	private String cjsj;
	/**
	 * 文字显示的颜色，0表示黑色，1表示蓝色，2表示红色
	 */
	private String showcolor;
	private String kssj;
	private String jssj;
	
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	public String getJssj() {
		return jssj;
	}
	public void setJssj(String jssj) {
		this.jssj = jssj;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	public String getFileurl() {
		return fileurl;
	}
	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCjr() {
		return cjr;
	}
	public void setCjr(String cjr) {
		this.cjr = cjr;
	}
	public String getCjsj() {
		return cjsj;
	}
	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}
	public String getShowcolor() {
		return showcolor;
	}
	public void setShowcolor(String showcolor) {
		this.showcolor = showcolor;
	}
}
