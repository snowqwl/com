package com.sunshine.monitor.comm.vehicle.bean;

import java.io.Serializable;

public class Vehicle implements Serializable, Cloneable {
	private static final long serialVersionUID = -8946191463341655203L;

	private static final String[] PATTERN = new String[] { "yyyyMMddHHmmss",
			"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss" };

	/**
	 * 号牌号码
	 */
	private String hphm;
	
	/**
	 * 号牌种类
	 */
	private String hpzl;

	/**
	 * 车辆类型
	 */
	private String cllx;
	
	/**
	 * 车辆型号
	 */
	private String clxh;
	
	/**
	 * 机动车所有人
	 */
	private String syr;
	
	/**
	 * 车身颜色
	 */
	private String csys;
	
	/**
	 * 车辆品牌
	 */
	private String clpp1;
	
	/**
	 * 手机
	 */
	private String lxdh;
	
	/**
	 * 发动机号
	 */
	private String fdjh;
	
	/**
	 * 车辆识别代号
	 */
	private String clsbdh;
	
	/**
	 * 住所详细地址
	 */
	private String zsxxdz;
	
	/**
	 * 发证机关
	 */
	private String fzjg;
	
	/**
	 * 机动车状状态
	 */
	private String zt;
	/**
	 * 机动车状状态-翻译名称
	 */
	private String ztmc;
	
	/**
	 * 检验有效期止
	 */
	private String yxqz;
	
	/**
	 * 强制报废期止
	 */
	private String qzbfqz;
	/**
	 * 机动车车辆模型-正面照
	 */
	private String front;
	/**
	 * 机动车车辆模型-背面照
	 */
	private String back;
	/**
	 * 机动车车辆模型-侧面照
	 */
	private String profile;
	
	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getYxqz() {
		return yxqz;
	}

	public void setYxqz(String yxqz) {
		this.yxqz = yxqz;
	}

	public String getQzbfqz() {
		return qzbfqz;
	}

	public void setQzbfqz(String qzbfqz) {
		this.qzbfqz = qzbfqz;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getSyr() {
		return syr;
	}

	public void setSyr(String syr) {
		this.syr = syr;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getClxh() {
		return clxh;
	}

	public void setClxh(String clxh) {
		this.clxh = clxh;
	}
	public String getCsys() {
		return csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getClpp1() {
		return clpp1;
	}

	public void setClpp1(String clpp1) {
		this.clpp1 = clpp1;
	}

	public String getFdjh() {
		return fdjh;
	}

	public void setFdjh(String fdjh) {
		this.fdjh = fdjh;
	}

	public String getClsbdh() {
		return clsbdh;
	}

	public void setClsbdh(String clsbdh) {
		this.clsbdh = clsbdh;
	}

	public String getZsxxdz() {
		return zsxxdz;
	}

	public void setZsxxdz(String zsxxdz) {
		this.zsxxdz = zsxxdz;
	}

	public String getFzjg() {
		return fzjg;
	}

	public void setFzjg(String fzjg) {
		this.fzjg = fzjg;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
