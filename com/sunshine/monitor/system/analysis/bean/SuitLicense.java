package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class SuitLicense extends Entity {
	private static final long serialVersionUID = -2529428641419302619L;

	// 过车序号1
	private String gcxh1;

	// 过车序号2
	private String gxcxh2;

	// 号牌种类
	private String hpzl;

	// 号牌号码
	private String hphm;
	
	//号牌颜色
	private String hpys;

	// 卡口1
	private String kdbh1;

	// 卡口2
	private String kdbh2;

	// 方向1
	private String fxbh1;

	// 方向2
	private String fxbh2;

	// 过车时间1
	private String gcsj1;

	// 过车时间2
	private String gcsj2;

	// 过车图片1
	private String gctp1;

	// 过车图片2
	private String gctp2;

	// 分析时间
	private String fxsj;

	// 状态
	private String zt;
	
	//直线距离
	private String zxjl;
	 
	//时间阀值
	private String sjfz;
	
	//计算速度
	private String jssd;

	public String getJssd() {
		return jssd;
	}

	public void setJssd(String jssd) {
		this.jssd = jssd;
	}

	public String getGcxh1() {
		return gcxh1;
	}

	public void setGcxh1(String gcxh1) {
		this.gcxh1 = gcxh1;
	}

	public String getZxjl() {
		return zxjl;
	}

	public void setZxjl(String zxjl) {
		this.zxjl = zxjl;
	}

	public String getSjfz() {
		return sjfz;
	}

	public void setSjfz(String sjfz) {
		this.sjfz = sjfz;
	}

	public String getGxcxh2() {
		return gxcxh2;
	}

	public void setGxcxh2(String gxcxh2) {
		this.gxcxh2 = gxcxh2;
	}

	public String getFxsj() {
		return fxsj;
	}

	public void setFxsj(String fxsj) {
		this.fxsj = fxsj;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getKdbh1() {
		return kdbh1;
	}

	public void setKdbh1(String kdbh1) {
		this.kdbh1 = kdbh1;
	}

	public String getKdbh2() {
		return kdbh2;
	}

	public void setKdbh2(String kdbh2) {
		this.kdbh2 = kdbh2;
	}

	public String getFxbh1() {
		return fxbh1;
	}

	public void setFxbh1(String fxbh1) {
		this.fxbh1 = fxbh1;
	}

	public String getFxbh2() {
		return fxbh2;
	}

	public void setFxbh2(String fxbh2) {
		this.fxbh2 = fxbh2;
	}

	public String getGcsj1() {
		return gcsj1;
	}

	public void setGcsj1(String gcsj1) {
		this.gcsj1 = gcsj1;
	}

	public String getGcsj2() {
		return gcsj2;
	}

	public void setGcsj2(String gcsj2) {
		this.gcsj2 = gcsj2;
	}

	public String getGctp1() {
		return gctp1;
	}

	public void setGctp1(String gctp1) {
		this.gctp1 = gctp1;
	}

	public String getGctp2() {
		return gctp2;
	}

	public void setGctp2(String gctp2) {
		this.gctp2 = gctp2;
	}

	public String getHpys() {
		return hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

}
