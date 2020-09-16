package com.sunshine.monitor.system.zdcars.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class CarS extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//唯一id
	private Long id;
	//批次号
	private String arrayId;
	//号牌号码
	private String hphm;
	//号牌种类
	private String hpzl;
	//号牌坐标
	private String hpzb;
	//过车时间
	private String gcsj;
	//设备编号
	private String sbbh;
	//卡点编号
	private String kkbh;
	//方向编号
	private String fxbh;
	//车道编号
	private String cdbh;
	//图片拍摄方向
	private String zpfx;
	//特写图片坐标值
	private String zbz;
	//拍摄来源
	private String psly;
	//识别类型
	private String sbly;
	//图片1
	private String zp1;
	//图片2
	private String zp2;
	//图片3
	private String zp3;
	//图片处理优先等级
	private String yxdj;
	//写入时间 
	private String xrsj;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getArrayId() {
		return arrayId;
	}
	public void setArrayId(String arrayId) {
		this.arrayId = arrayId;
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
	public String getHpzb() {
		return hpzb;
	}
	public void setHpzb(String hpzb) {
		this.hpzb = hpzb;
	}
	public String getGcsj() {
		return gcsj;
	}
	public void setGcsj(String gcsj) {
		this.gcsj = gcsj;
	}
	public String getSbbh() {
		return sbbh;
	}
	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}
	public String getKkbh() {
		return kkbh;
	}
	public void setKkbh(String kkbh) {
		this.kkbh = kkbh;
	}
	public String getFxbh() {
		return fxbh;
	}
	public void setFxbh(String fxbh) {
		this.fxbh = fxbh;
	}
	public String getCdbh() {
		return cdbh;
	}
	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
	}
	public String getZpfx() {
		return zpfx;
	}
	public void setZpfx(String zpfx) {
		this.zpfx = zpfx;
	}
	public String getZbz() {
		return zbz;
	}
	public void setZbz(String zbz) {
		this.zbz = zbz;
	}
	public String getPsly() {
		return psly;
	}
	public void setPsly(String psly) {
		this.psly = psly;
	}
	public String getSbly() {
		return sbly;
	}
	public void setSbly(String sbly) {
		this.sbly = sbly;
	}
	public String getZp1() {
		return zp1;
	}
	public void setZp1(String zp1) {
		this.zp1 = zp1;
	}
	public String getZp2() {
		return zp2;
	}
	public void setZp2(String zp2) {
		this.zp2 = zp2;
	}
	public String getZp3() {
		return zp3;
	}
	public void setZp3(String zp3) {
		this.zp3 = zp3;
	}
	public String getYxdj() {
		return yxdj;
	}
	public void setYxdj(String yxdj) {
		this.yxdj = yxdj;
	}
	public String getXrsj() {
		return xrsj;
	}
	public void setXrsj(String xrsj) {
		this.xrsj = xrsj;
	}
	
	
	
}
