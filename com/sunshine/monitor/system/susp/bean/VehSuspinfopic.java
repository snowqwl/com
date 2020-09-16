package com.sunshine.monitor.system.susp.bean;

/**
 * 布控图片
 * @author OUYANG
 *
 */
public class VehSuspinfopic {

	// 序号(对应ORACLE:SEQ_PICREC_XH)
	private Long xh ;
	
	// 布控序号
	private String bkxh ;
	
	// 图片数据
	private byte[] zjwj;
	
	// 图片路径
	private String zjlx;
	
	// 车辆布控申请表
	private byte[] clbksqb;
	
	// 车辆布控申请表路径
	private String clbksqblj;
	
	// 立案决定书
	private byte[] lajds;
	
	// 立案决定书路径
	private String lajdslj;
	
	// 移交承诺书
	private byte[] yjcns;
	
	// 移交承诺书路径
	private String yjcnslj;
	

	public byte[] getClbksqb() {
		return clbksqb;
	}

	public void setClbksqb(byte[] clbksqb) {
		this.clbksqb = clbksqb;
	}

	public String getClbksqblj() {
		return clbksqblj;
	}

	public void setClbksqblj(String clbksqblj) {
		this.clbksqblj = clbksqblj;
	}

	public byte[] getLajds() {
		return lajds;
	}

	public void setLajds(byte[] lajds) {
		this.lajds = lajds;
	}

	public String getLajdslj() {
		return lajdslj;
	}

	public void setLajdslj(String lajdslj) {
		this.lajdslj = lajdslj;
	}

	public byte[] getYjcns() {
		return yjcns;
	}

	public void setYjcns(byte[] yjcns) {
		this.yjcns = yjcns;
	}

	public String getYjcnslj() {
		return yjcnslj;
	}

	public void setYjcnslj(String yjcnslj) {
		this.yjcnslj = yjcnslj;
	}

	public Long getXh() {
		return xh;
	}

	public void setXh(Long xh) {
		this.xh = xh;
	}

	public String getBkxh() {
		return bkxh;
	}

	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}

	public byte[] getZjwj() {
		return zjwj;
	}

	public void setZjwj(byte[] zjwj) {
		this.zjwj = zjwj;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}
}
