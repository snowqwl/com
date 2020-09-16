package com.sunshine.monitor.system.ws.ClkkbkFeedBackService.bean;

import com.sunshine.monitor.comm.bean.Entity;

/**
 * 于大情報交換返回信息
 * @author lifenghu
 *
 */
public class TransFeedback extends Entity {
	private static final long serialVersionUID = -4876836989558201646L;
	
	private String xh;
	private String ywxh;
	private String lb;
	private String xxly;
	private String fknr;
	private String fksj;
	private String bz;
	private String csjg;

	public String getXh()
	{
		return this.xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getYwxh() {
		return this.ywxh;
	}
	public void setYwxh(String ywxh) {
		this.ywxh = ywxh;
	}
	public String getLb() {
		return this.lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	public String getXxly() {
		return this.xxly;
	}
	public void setXxly(String xxly) {
		this.xxly = xxly;
	}
	public String getFknr() {
		return this.fknr;
	}
	public void setFknr(String fknr) {
		this.fknr = fknr;
	}
	public String getFksj() {
		return this.fksj;
	}
	public void setFksj(String fksj) {
		this.fksj = fksj;
	}
	public String getBz() {
		return this.bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getCsjg() {
		return this.csjg;
	}
	public void setCsjg(String csjg) {
		this.csjg = csjg;
	}
}
