package com.sunshine.monitor.system.ws.SuspService.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VehSuspinfoEntity")
@XmlRootElement(name = "vehSuspinfoEntity")
public class VehSuspinfoEntity implements Serializable {

	@XmlTransient
	private static final long serialVersionUID = -524250985716144536L;

	private String bkxh;

	/**
	 * 原始编号
	 */
	private String ysbh;

	/**
	 * 号牌号码
	 */
	private String hphm;

	/**
	 * 号牌种类
	 */
	private String hpzl;
	
	private String hpzlmc;

	/**
	 * 布控大类
	 */
	private String bkdl;
	
	private String bkdlmc;
	/**
	 * 布控类别
	 */
	private String bklb;
	
	private String bklbmc;
	/**
	 * 布控启始时间
	 */
	private String bkqssj;
	/**
	 * 布控结束时间
	 */
	private String bkjzsj;
	/**
	 * 简要案情
	 */
	private String jyaq;
	/**
	 * 布范围类型:1=本地布控,2=联动布控
	 */
	private String bkfwlx;
	
	private String bkfwlxmc;
	/**
	 * 布控范围
	 */
	private String bkfw;
	
	private String bkfwmc;
	/**
	 * 布控级别:1=一级,2=二级,3=三级,4=四级
	 */
	private String bkjb;
	
	private String bkjbmc;
	/**
	 * 布控性质:1=公开,0=秘密
	 */
	private String bkxz;
	
	private String bkxzmc;
	/**
	 * 涉枪涉爆,0:无,1:是,2:未知
	 */
	private String sqsb;
	
	private String sqsbmc;
	/**
	 * 报警预案,0:不拦截,1:拦截
	 */
	private String bjya;
	
	private String bjyamc;
	/**
	 * 四位二进制数 报警方式,第一位表示是否界面报警，第二位表示是否声音报警，第三位表示是否LED大屏报警，第四位表示是否短信报警,1是0否
	 */
	private String bjfs;
	
	private String bjfsmc;
	/**
	 * 短信接收号码
	 */
	private String dxjshm = "";
	/**
	 * 申请人
	 */
	private String lar;
	/**
	 * 申请单位
	 */
	private String ladw;
	/**
	 * 申请单位联系电话
	 */
	private String ladwlxdh;
	/**
	 * 车辆品牌
	 */
	private String clpp;
	/**
	 * 号牌颜色
	 */
	private String hpys;
	
	private String hpysmc;
	/**
	 * 车辆型号
	 */
	private String clxh;
	/**
	 * 车辆类型
	 */
	private String cllx;
	
	private String cllxmc;
	/**
	 * 车身颜色
	 */
	private String csys;
	
	private String csysmc;
	/**
	 * 车辆识别代号
	 */
	private String clsbdh;
	/**
	 * 发动机号
	 */
	private String fdjh;
	/**
	 * 车辆特征
	 */
	private String cltz;
	/**
	 * 车辆所有人
	 */
	private String clsyr;
	/**
	 * 车辆所有人电话
	 */
	private String syrlxdh;
	/**
	 * 所有人详细地址
	 */
	private String syrxxdz;
	/**
	 * 布控人
	 */
	private String bkr;
	/**
	 * 布控人名称
	 */
	private String bkrmc;
	/**
	 * 布控机关
	 */
	private String bkjg;
	/**
	 * 布控机关名称
	 */
	private String bkjgmc;
	/**
	 * 布控申请机关联系电话
	 */
	private String bkjglxdh;
	/**
	 * 布控时间
	 */
	private String bksj;
	/**
	 * 布控申请人
	 */
	private String cxsqr;
	/**
	 * 撤控申请人名称
	 */
	private String cxsqrmc;
	/**
	 * 撤控申请单位
	 */
	private String cxsqdw;
	/**
	 * 撤控申请单位名称
	 */
	private String cxsqdwmc;
	/**
	 * 撤销申请时间
	 */
	private String cxsqsj;
	/**
	 * 撤控原因代码,01:已过期,02:已处理,03:无效信息,04:错误信息,05:未通过审核,99:其它原因
	 */
	private String ckyydm;
	
	private String ckyydmmc;
	/**
	 * 撤控原因描述
	 */
	private String ckyyms;

	/**
	 * 业务状态
	 */
	private String ywzt;
	
	private String ywztmc;
	/**
	 * 记录状态 0:初始,1:布控,2:撤控
	 */
	private String jlzt;
	
	private String jlztmc;

	/**
	 * 信息来源 0:本地录入,1:批量转入,2:联动转入,5:情报平台转入,6:警综系统
	 */
	private String xxly;
	
	private String xxlymc;
	/**
	 * 布控平台
	 */
	private String bkpt;

	/**
	 * 报警状态
	 */
	private String bjzt;
	
	private String bjztmc;
	/**
	 * 报警时间
	 */
	private String bjsj;
	/**
	 * 是否模糊布控标记 0:不是 1:是
	 */
	private String mhbkbj;
	/**
	 * 撤控申请人警号
	 */
	private String cxsqrjh;
	/**
	 * 布控人警号
	 */
	private String bkrjh;

	public String getBkxh() {
		return bkxh;
	}

	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}

	public String getYsbh() {
		return ysbh;
	}

	public void setYsbh(String ysbh) {
		this.ysbh = ysbh;
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

	public String getBkdl() {
		return bkdl;
	}

	public void setBkdl(String bkdl) {
		this.bkdl = bkdl;
	}

	public String getBklb() {
		return bklb;
	}

	public void setBklb(String bklb) {
		this.bklb = bklb;
	}

	public String getBkqssj() {
		return bkqssj;
	}

	public void setBkqssj(String bkqssj) {
		this.bkqssj = bkqssj;
	}

	public String getBkjzsj() {
		return bkjzsj;
	}

	public void setBkjzsj(String bkjzsj) {
		this.bkjzsj = bkjzsj;
	}

	public String getJyaq() {
		return jyaq;
	}

	public void setJyaq(String jyaq) {
		this.jyaq = jyaq;
	}

	public String getBkfwlx() {
		return bkfwlx;
	}

	public void setBkfwlx(String bkfwlx) {
		this.bkfwlx = bkfwlx;
	}

	public String getBkfw() {
		return bkfw;
	}

	public void setBkfw(String bkfw) {
		this.bkfw = bkfw;
	}

	public String getBkjb() {
		return bkjb;
	}

	public void setBkjb(String bkjb) {
		this.bkjb = bkjb;
	}

	public String getBkxz() {
		return bkxz;
	}

	public void setBkxz(String bkxz) {
		this.bkxz = bkxz;
	}

	public String getSqsb() {
		return sqsb;
	}

	public void setSqsb(String sqsb) {
		this.sqsb = sqsb;
	}

	public String getBjya() {
		return bjya;
	}

	public void setBjya(String bjya) {
		this.bjya = bjya;
	}

	public String getBjfs() {
		return bjfs;
	}

	public void setBjfs(String bjfs) {
		this.bjfs = bjfs;
	}

	public String getDxjshm() {
		return dxjshm;
	}

	public void setDxjshm(String dxjshm) {
		this.dxjshm = dxjshm;
	}

	public String getLar() {
		return lar;
	}

	public void setLar(String lar) {
		this.lar = lar;
	}

	public String getLadw() {
		return ladw;
	}

	public void setLadw(String ladw) {
		this.ladw = ladw;
	}

	public String getLadwlxdh() {
		return ladwlxdh;
	}

	public void setLadwlxdh(String ladwlxdh) {
		this.ladwlxdh = ladwlxdh;
	}

	public String getClpp() {
		return clpp;
	}

	public void setClpp(String clpp) {
		this.clpp = clpp;
	}

	public String getHpys() {
		return hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public String getClxh() {
		return clxh;
	}

	public void setClxh(String clxh) {
		this.clxh = clxh;
	}

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getCsys() {
		return csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getClsbdh() {
		return clsbdh;
	}

	public void setClsbdh(String clsbdh) {
		this.clsbdh = clsbdh;
	}

	public String getFdjh() {
		return fdjh;
	}

	public void setFdjh(String fdjh) {
		this.fdjh = fdjh;
	}

	public String getCltz() {
		return cltz;
	}

	public void setCltz(String cltz) {
		this.cltz = cltz;
	}

	public String getClsyr() {
		return clsyr;
	}

	public void setClsyr(String clsyr) {
		this.clsyr = clsyr;
	}

	public String getSyrlxdh() {
		return syrlxdh;
	}

	public void setSyrlxdh(String syrlxdh) {
		this.syrlxdh = syrlxdh;
	}

	public String getSyrxxdz() {
		return syrxxdz;
	}

	public void setSyrxxdz(String syrxxdz) {
		this.syrxxdz = syrxxdz;
	}

	public String getBkr() {
		return bkr;
	}

	public void setBkr(String bkr) {
		this.bkr = bkr;
	}

	public String getBkrmc() {
		return bkrmc;
	}

	public void setBkrmc(String bkrmc) {
		this.bkrmc = bkrmc;
	}

	public String getBkjg() {
		return bkjg;
	}

	public void setBkjg(String bkjg) {
		this.bkjg = bkjg;
	}

	public String getBkjgmc() {
		return bkjgmc;
	}

	public void setBkjgmc(String bkjgmc) {
		this.bkjgmc = bkjgmc;
	}

	public String getBkjglxdh() {
		return bkjglxdh;
	}

	public void setBkjglxdh(String bkjglxdh) {
		this.bkjglxdh = bkjglxdh;
	}

	public String getBksj() {
		return bksj;
	}

	public void setBksj(String bksj) {
		this.bksj = bksj;
	}

	public String getCxsqr() {
		return cxsqr;
	}

	public void setCxsqr(String cxsqr) {
		this.cxsqr = cxsqr;
	}

	public String getCxsqrmc() {
		return cxsqrmc;
	}

	public void setCxsqrmc(String cxsqrmc) {
		this.cxsqrmc = cxsqrmc;
	}

	public String getCxsqdw() {
		return cxsqdw;
	}

	public void setCxsqdw(String cxsqdw) {
		this.cxsqdw = cxsqdw;
	}

	public String getCxsqdwmc() {
		return cxsqdwmc;
	}

	public void setCxsqdwmc(String cxsqdwmc) {
		this.cxsqdwmc = cxsqdwmc;
	}

	public String getCxsqsj() {
		return cxsqsj;
	}

	public void setCxsqsj(String cxsqsj) {
		this.cxsqsj = cxsqsj;
	}

	public String getCkyydm() {
		return ckyydm;
	}

	public void setCkyydm(String ckyydm) {
		this.ckyydm = ckyydm;
	}

	public String getCkyyms() {
		return ckyyms;
	}

	public void setCkyyms(String ckyyms) {
		this.ckyyms = ckyyms;
	}

	public String getYwzt() {
		return ywzt;
	}

	public void setYwzt(String ywzt) {
		this.ywzt = ywzt;
	}

	public String getJlzt() {
		return jlzt;
	}

	public void setJlzt(String jlzt) {
		this.jlzt = jlzt;
	}

	public String getXxly() {
		return xxly;
	}

	public void setXxly(String xxly) {
		this.xxly = xxly;
	}

	public String getBkpt() {
		return bkpt;
	}

	public void setBkpt(String bkpt) {
		this.bkpt = bkpt;
	}

	public String getBjzt() {
		return bjzt;
	}

	public void setBjzt(String bjzt) {
		this.bjzt = bjzt;
	}

	public String getBjsj() {
		return bjsj;
	}

	public void setBjsj(String bjsj) {
		this.bjsj = bjsj;
	}

	public String getMhbkbj() {
		return mhbkbj;
	}

	public void setMhbkbj(String mhbkbj) {
		this.mhbkbj = mhbkbj;
	}

	public String getCxsqrjh() {
		return cxsqrjh;
	}

	public void setCxsqrjh(String cxsqrjh) {
		this.cxsqrjh = cxsqrjh;
	}

	public String getBkrjh() {
		return bkrjh;
	}

	public void setBkrjh(String bkrjh) {
		this.bkrjh = bkrjh;
	}

	public String getHpzlmc() {
		return hpzlmc;
	}

	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
	}

	public String getBkdlmc() {
		return bkdlmc;
	}

	public void setBkdlmc(String bkdlmc) {
		this.bkdlmc = bkdlmc;
	}

	public String getBklbmc() {
		return bklbmc;
	}

	public void setBklbmc(String bklbmc) {
		this.bklbmc = bklbmc;
	}

	public String getBkfwlxmc() {
		return bkfwlxmc;
	}

	public void setBkfwlxmc(String bkfwlxmc) {
		this.bkfwlxmc = bkfwlxmc;
	}

	public String getBkfwmc() {
		return bkfwmc;
	}

	public void setBkfwmc(String bkfwmc) {
		this.bkfwmc = bkfwmc;
	}

	public String getBkjbmc() {
		return bkjbmc;
	}

	public void setBkjbmc(String bkjbmc) {
		this.bkjbmc = bkjbmc;
	}

	public String getBkxzmc() {
		return bkxzmc;
	}

	public void setBkxzmc(String bkxzmc) {
		this.bkxzmc = bkxzmc;
	}

	public String getSqsbmc() {
		return sqsbmc;
	}

	public void setSqsbmc(String sqsbmc) {
		this.sqsbmc = sqsbmc;
	}

	public String getBjyamc() {
		return bjyamc;
	}

	public void setBjyamc(String bjyamc) {
		this.bjyamc = bjyamc;
	}

	public String getBjfsmc() {
		return bjfsmc;
	}

	public void setBjfsmc(String bjfsmc) {
		this.bjfsmc = bjfsmc;
	}

	public String getHpysmc() {
		return hpysmc;
	}

	public void setHpysmc(String hpysmc) {
		this.hpysmc = hpysmc;
	}

	public String getCllxmc() {
		return cllxmc;
	}

	public void setCllxmc(String cllxmc) {
		this.cllxmc = cllxmc;
	}

	public String getCsysmc() {
		return csysmc;
	}

	public void setCsysmc(String csysmc) {
		this.csysmc = csysmc;
	}

	public String getYwztmc() {
		return ywztmc;
	}

	public void setYwztmc(String ywztmc) {
		this.ywztmc = ywztmc;
	}

	public String getJlztmc() {
		return jlztmc;
	}

	public void setJlztmc(String jlztmc) {
		this.jlztmc = jlztmc;
	}

	public String getXxlymc() {
		return xxlymc;
	}

	public void setXxlymc(String xxlymc) {
		this.xxlymc = xxlymc;
	}

	public String getBjztmc() {
		return bjztmc;
	}

	public void setBjztmc(String bjztmc) {
		this.bjztmc = bjztmc;
	}

	public String getCkyydmmc() {
		return ckyydmmc;
	}

	public void setCkyydmmc(String ckyydmmc) {
		this.ckyydmmc = ckyydmmc;
	}

	
}
