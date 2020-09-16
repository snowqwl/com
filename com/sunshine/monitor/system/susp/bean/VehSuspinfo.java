package com.sunshine.monitor.system.susp.bean;

import javax.xml.bind.annotation.XmlElement;

import com.sunshine.monitor.comm.bean.Entity;

/**
 * 布控
 * @author JCBK-OUYANG 2012/12/18
 *
 */
public class VehSuspinfo extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 布控序号
	 */
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
	
	/**
	 * 布控大类
	 */
	private String bkdl;
	/**
	 * 布控类别
	 */
	private String bklb;
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
	/**
	 * 布控范围
	 */
	private String bkfw;
	/**
	 * 布控级别:1=一级,2=二级,3=三级,4=四级
	 */
	private String bkjb;
	/**
	 * 布控性质:1=公开,0=秘密
	 */
	private String bkxz;
	/**
	 * 涉枪涉爆,0:无,1:是,2:未知
	 */
	private String sqsb;
	/**
	 * 报警预案,0:不拦截,1:拦截  
	 */
	private String bjya;
	/**
	 * 四位二进制数
	 * 报警方式,第一位表示是否界面报警，第二位表示是否声音报警，第三位表示是否LED大屏报警，第四位表示是否短信报警,1是0否
	 */
	private String bjfs;
	/**
	 * 短信接收号码
	 */
	private String dxjshm="";
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
	/**
	 * 车辆型号
	 */
	private String clxh;
	/**
	 * 车辆类型
	 */
	private String cllx;
	/**
	 * 车身颜色
	 */
	private String csys;
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
	/**
	 * 撤控原因描述
	 */
	private String ckyyms;
	
	/**
	 * 业务状态
	 */
	private String ywzt;
	/**
	 * 记录状态  0:初始,1:布控,2:撤控
	 */
	private String jlzt;
	/**
	 * 更新时间
	 */
	private String gxsj;
	/**
	 * 信息来源 0:本地录入,1:批量转入,2:联动转入,5:情报平台转入,6:警综系统
	 */
	private String xxly;
	/**
	 * 布控平台
	 */
	private String bkpt;
	/**
	 * 备用字段
	 */
	private String by1;
	private String by2;
	private String by3;
	private String by4;
	private String by5;
	private String bz;
	/**
	 * 报警状态
	 */
	private String bjzt;
	/**
	 * 报警时间
	 */
	private String bjsj;
	/**
	 * 是否模糊布控标记  0:不是 1:是
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
	
	/**
	 * 是否拦截成功
	 */
	private String sfljcg;
	
	/**
	 * 下级短信提醒内容
	 */
	private String xjdx;
	
	//==================
	private String zjlx;
	public Long picxh=0l;
	public String hpzlmc = "";
	public String hpysmc = "";
	public String cllxmc = "";
	public String csysmc = "";
	public String bkjbmc = "";
	public String bkdlmc = "";
	public String bklbmc = "";
	public String ywztmc = "";
	public String bjztmc = "";
	public String bjyamc = "";
	public String xxlymc = "";
	public String sqsbmc = "";
	public String bkxzmc = "";
	public String bjfsmc = "";
	public String ckyydmmc = "";
	public String bkfwmc = "";
	public String kssj = "";
	public String jssj = "";
	// ----布控人单位警种----
	public String bkrdwjz;
	
	
	//===================
	//提醒上级领导的手机号码信息
	private String user;
	private String lxdh;
	
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getBkdlmc() {
		return this.bkdlmc;
	}

	public String getSfljcg() {
		return sfljcg;
	}

	public void setSfljcg(String sfljcg) {
		this.sfljcg = sfljcg;
	}

	public String getBkrdwjz() {
		return bkrdwjz;
	}

	public void setBkrdwjz(String bkrdwjz) {
		this.bkrdwjz = bkrdwjz;
	}

	public void setBkdlmc(String bkdlmc) {
		this.bkdlmc = bkdlmc;
	}
	
	@XmlElement(name="布控序号")
	public String getBkxh() {
		return this.bkxh;
	}

	public void setBkxh(String bkxh) {
		this.bkxh = bkxh;
	}

	public String getYsbh() {
		return this.ysbh;
	}

	public void setYsbh(String ysbh) {
		this.ysbh = ysbh;
	}
	
	@XmlElement(name="号牌号码")
	public String getHphm() {
		return this.hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}
	
	@XmlElement(name="号牌种类")
	public String getHpzl() {
		return this.hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}
	
	@XmlElement(name="布控大类")
	public String getBkdl() {
		return this.bkdl;
	}

	public void setBkdl(String bkdl) {
		this.bkdl = bkdl;
	}
	
	@XmlElement(name="布控类别")
	public String getBklb() {
		return this.bklb;
	}

	public void setBklb(String bklb) {
		this.bklb = bklb;
	}

	public String getBkqssj() {
		return this.bkqssj;
	}

	public void setBkqssj(String bkqssj) {
		this.bkqssj = bkqssj;
	}

	public String getBkjzsj() {
		return this.bkjzsj;
	}

	public void setBkjzsj(String bkjzsj) {
		this.bkjzsj = bkjzsj;
	}

	public String getJyaq() {
		return this.jyaq;
	}

	public void setJyaq(String jyaq) {
		this.jyaq = jyaq;
	}
	
	@XmlElement(name="布控范围类型")
	public String getBkfwlx() {
		return this.bkfwlx;
	}

	public void setBkfwlx(String bkfwlx) {
		this.bkfwlx = bkfwlx;
	}

	public String getBkfw() {
		return this.bkfw;
	}

	public void setBkfw(String bkfw) {
		this.bkfw = bkfw;
	}

	public String getBkjb() {
		return this.bkjb;
	}

	public void setBkjb(String bkjb) {
		this.bkjb = bkjb;
	}

	public String getBkxz() {
		return this.bkxz;
	}

	public void setBkxz(String bkxz) {
		this.bkxz = bkxz;
	}

	public String getSqsb() {
		return this.sqsb;
	}

	public void setSqsb(String sqsb) {
		this.sqsb = sqsb;
	}

	public String getBjya() {
		return this.bjya;
	}

	public void setBjya(String bjya) {
		this.bjya = bjya;
	}

	public String getBjfs() {
		return this.bjfs;
	}

	public void setBjfs(String bjfs) {
		this.bjfs = bjfs;
	}

	public String getDxjshm() {
		return this.dxjshm;
	}

	public void setDxjshm(String dxjshm) {
		this.dxjshm = dxjshm;
	}

	public String getLar() {
		return this.lar;
	}

	public void setLar(String lar) {
		this.lar = lar;
	}

	public String getLadw() {
		return this.ladw;
	}

	public void setLadw(String ladw) {
		this.ladw = ladw;
	}

	public String getLadwlxdh() {
		return this.ladwlxdh;
	}

	public void setLadwlxdh(String ladwlxdh) {
		this.ladwlxdh = ladwlxdh;
	}

	public String getClpp() {
		return this.clpp;
	}

	public void setClpp(String clpp) {
		this.clpp = clpp;
	}

	public String getHpys() {
		return this.hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public String getClxh() {
		return this.clxh;
	}

	public void setClxh(String clxh) {
		this.clxh = clxh;
	}

	public String getCllx() {
		return this.cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getCsys() {
		return this.csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getClsbdh() {
		return this.clsbdh;
	}

	public void setClsbdh(String clsbdh) {
		this.clsbdh = clsbdh;
	}

	public String getFdjh() {
		return this.fdjh;
	}

	public void setFdjh(String fdjh) {
		this.fdjh = fdjh;
	}

	public String getCltz() {
		return this.cltz;
	}

	public void setCltz(String cltz) {
		this.cltz = cltz;
	}

	public String getClsyr() {
		return this.clsyr;
	}

	public void setClsyr(String clsyr) {
		this.clsyr = clsyr;
	}

	public String getSyrlxdh() {
		return this.syrlxdh;
	}

	public void setSyrlxdh(String syrlxdh) {
		this.syrlxdh = syrlxdh;
	}

	public String getSyrxxdz() {
		return this.syrxxdz;
	}

	public void setSyrxxdz(String syrxxdz) {
		this.syrxxdz = syrxxdz;
	}

	public String getBkr() {
		return this.bkr;
	}

	public void setBkr(String bkr) {
		this.bkr = bkr;
	}

	public String getBkrmc() {
		return this.bkrmc;
	}

	public void setBkrmc(String bkrmc) {
		this.bkrmc = bkrmc;
	}

	public String getBkjg() {
		return this.bkjg;
	}

	public void setBkjg(String bkjg) {
		this.bkjg = bkjg;
	}

	public String getBkjgmc() {
		return this.bkjgmc;
	}

	public void setBkjgmc(String bkjgmc) {
		this.bkjgmc = bkjgmc;
	}

	public String getBkjglxdh() {
		return this.bkjglxdh;
	}

	public void setBkjglxdh(String bkjglxdh) {
		this.bkjglxdh = bkjglxdh;
	}

	public String getBksj() {
		return this.bksj;
	}

	public void setBksj(String bksj) {
		this.bksj = bksj;
	}

	public String getCxsqr() {
		return this.cxsqr;
	}

	public void setCxsqr(String cxsqr) {
		this.cxsqr = cxsqr;
	}

	public String getCxsqrmc() {
		return this.cxsqrmc;
	}

	public void setCxsqrmc(String cxsqrmc) {
		this.cxsqrmc = cxsqrmc;
	}

	public String getCxsqdw() {
		return this.cxsqdw;
	}

	public void setCxsqdw(String cxsqdw) {
		this.cxsqdw = cxsqdw;
	}

	public String getCxsqdwmc() {
		return this.cxsqdwmc;
	}

	public void setCxsqdwmc(String cxsqdwmc) {
		this.cxsqdwmc = cxsqdwmc;
	}

	public String getCxsqsj() {
		return this.cxsqsj;
	}

	public void setCxsqsj(String cxsqsj) {
		this.cxsqsj = cxsqsj;
	}

	public String getCkyydm() {
		return this.ckyydm;
	}

	public void setCkyydm(String ckyydm) {
		this.ckyydm = ckyydm;
	}

	public String getCkyyms() {
		return this.ckyyms;
	}

	public void setCkyyms(String ckyyms) {
		this.ckyyms = ckyyms;
	}

	public String getYwzt() {
		return this.ywzt;
	}

	public void setYwzt(String ywzt) {
		this.ywzt = ywzt;
	}

	public String getJlzt() {
		return this.jlzt;
	}

	public void setJlzt(String jlzt) {
		this.jlzt = jlzt;
	}

	public String getGxsj() {
		return this.gxsj;
	}

	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}

	public String getXxly() {
		return this.xxly;
	}

	public void setXxly(String xxly) {
		this.xxly = xxly;
	}

	public String getBkpt() {
		return this.bkpt;
	}

	public void setBkpt(String bkpt) {
		this.bkpt = bkpt;
	}

	public String getBy1() {
		return this.by1;
	}

	public void setBy1(String by1) {
		this.by1 = by1;
	}

	public String getBy2() {
		return this.by2;
	}

	public void setBy2(String by2) {
		this.by2 = by2;
	}

	public String getBy3() {
		return this.by3;
	}

	public void setBy3(String by3) {
		this.by3 = by3;
	}

	public String getBy4() {
		return this.by4;
	}

	public void setBy4(String by4) {
		this.by4 = by4;
	}

	public String getBy5() {
		return this.by5;
	}

	public void setBy5(String by5) {
		this.by5 = by5;
	}

	public String getBjzt() {
		return this.bjzt;
	}

	public void setBjzt(String bjzt) {
		this.bjzt = bjzt;
	}

	public String getBjsj() {
		return this.bjsj;
	}

	public void setBjsj(String bjsj) {
		this.bjsj = bjsj;
	}

	public String getMhbkbj() {
		return this.mhbkbj;
	}

	public void setMhbkbj(String mhbkbj) {
		this.mhbkbj = mhbkbj;
	}

	public String getCxsqrjh() {
		return this.cxsqrjh;
	}

	public void setCxsqrjh(String cxsqrjh) {
		this.cxsqrjh = cxsqrjh;
	}

	public String getBkrjh() {
		return this.bkrjh;
	}

	public void setBkrjh(String bkrjh) {
		this.bkrjh = bkrjh;
	}

	public String getZjlx() {
		return this.zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public Long getPicxh() {
		return this.picxh;
	}

	public void setPicxh(Long picxh) {
		this.picxh = picxh;
	}

	public String getHpzlmc() {
		return this.hpzlmc;
	}

	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
	}

	public String getHpysmc() {
		return this.hpysmc;
	}

	public void setHpysmc(String hpysmc) {
		this.hpysmc = hpysmc;
	}

	public String getCllxmc() {
		return this.cllxmc;
	}

	public void setCllxmc(String cllxmc) {
		this.cllxmc = cllxmc;
	}

	public String getCsysmc() {
		return this.csysmc;
	}

	public void setCsysmc(String csysmc) {
		this.csysmc = csysmc;
	}

	public String getBkjbmc() {
		return this.bkjbmc;
	}

	public void setBkjbmc(String bkjbmc) {
		this.bkjbmc = bkjbmc;
	}

	public String getBklbmc() {
		return this.bklbmc;
	}

	public void setBklbmc(String bklbmc) {
		this.bklbmc = bklbmc;
	}

	public String getYwztmc() {
		return this.ywztmc;
	}

	public void setYwztmc(String ywztmc) {
		this.ywztmc = ywztmc;
	}

	public String getBjztmc() {
		return this.bjztmc;
	}

	public void setBjztmc(String bjztmc) {
		this.bjztmc = bjztmc;
	}

	public String getBjyamc() {
		return this.bjyamc;
	}

	public void setBjyamc(String bjyamc) {
		this.bjyamc = bjyamc;
	}

	public String getXxlymc() {
		return this.xxlymc;
	}

	public void setXxlymc(String xxlymc) {
		this.xxlymc = xxlymc;
	}

	public String getSqsbmc() {
		return this.sqsbmc;
	}

	public void setSqsbmc(String sqsbmc) {
		this.sqsbmc = sqsbmc;
	}

	public String getBkxzmc() {
		return this.bkxzmc;
	}

	public void setBkxzmc(String bkxzmc) {
		this.bkxzmc = bkxzmc;
	}

	public String getBjfsmc() {
		return this.bjfsmc;
	}

	public void setBjfsmc(String bjfsmc) {
		this.bjfsmc = bjfsmc;
	}

	public String getCkyydmmc() {
		return this.ckyydmmc;
	}

	public void setCkyydmmc(String ckyydmmc) {
		this.ckyydmmc = ckyydmmc;
	}

	public String getBkfwmc() {
		return this.bkfwmc;
	}

	public void setBkfwmc(String bkfwmc) {
		this.bkfwmc = bkfwmc;
	}
	
	@XmlElement(name="开始时间")
	public String getKssj() {
		return this.kssj;
	}

	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	
	@XmlElement(name="结束时间")
	public String getJssj() {
		return this.jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getXjdx() {
		return xjdx;
	}

	public void setXjdx(String xjdx) {
		this.xjdx = xjdx;
	}
	
}