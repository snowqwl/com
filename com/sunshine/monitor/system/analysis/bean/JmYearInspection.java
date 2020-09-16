package com.sunshine.monitor.system.analysis.bean;


import com.sunshine.monitor.comm.bean.Entity;

/**
 * 报废未年检黑名单库
 * @author JCBK-PANYONGS 2014/06/09
 *
 */
public class JmYearInspection extends Entity {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 车辆类型
	 */
	private String cllx;
	
	/**
	 * 车辆类型名称
	 */
	private String cllxmc;
	
	/**
	 * 发动机号
	 */
	private String fdjh;
	
	/**
	 * 机动车ID
	 */
	private String jdcid;
	
	/**
	 * 制造厂名称
	 */
	private String zzcmc;
	
	/**
	 * 身份证件号码
	 */
	private String sfzjhm;
	
	/**
	 * 住址行政区划
	 */
	private String zhzxzqh;
	

	/**
	 * 暂住证/居留证号
	 */
	private String zzz;
	
	/**
	 * 暂住行政区划
	 */
	private String zzxzqh;
	
	/**
	 * 暂住详细地址
	 */
	private String zzxxdz;
	
	/**
	 * 定检日期
	 */
	private String djrq;
	
	/**
	 * 身份证件种类
	 */
	private String sfzjzl;
	
	/**
	 * 住址详址
	 */
	private String zzxz;
	
	/**
	 * 车身颜色
	 */
	private String csys;
	
	/**
	 * 车身颜色名称
	 */
	private String csysmc;
	
	/**
	 * 登记证书编号
	 */
	private String djzsbh;
	
	/**
	 * 号牌号码
	 */
	private String hphm;
	
	/**
	 * 号牌种类
	 */
	private String hpzl;
	
	/**
	 * 号牌种类名称
	 */
	private String hpzlmc;
	
	/**
	 * 号牌颜色
	 */
	private String hpys;
	
	/**
	 * 号牌颜色名称
	 */
	private String hpysmc;
	
	/**
	 * 车辆品牌
	 */
	private String clpp;
	
	/**
	 * 车辆型号
	 */
	private String clxh;
	
	/**
	 * 车辆识别代号
	 */
	private String clsbdh;
	
	/**
	 * 机动车所有人
	 */
	private String jdcsyr;
	
	/**
	 * 初次登记日期
	 */
	private String ccdjrq;
	
	/**
	 * 发牌机关
	 */
	private String fpjg;
	
	/**
	 * 发牌机关名称
	 */
	private String fpjgmc;
	
	/**
	 * 机动车状态
	 */
	private String jdczt;
	
	/**
	 * 国产/进口
	 */
	private String gcjk;
	
	/**
	 * 图片1
	 */
	private String tp1;
	
	/**
	 * 图片2
	 */
	private String tp2;
	
	/**
	 * 图片3
	 */
	private String tp3;
	
	/**
	 * 特征图片
	 */
	private String tztp;

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getCllxmc() {
		return cllxmc;
	}

	public void setCllxmc(String cllxmc) {
		this.cllxmc = cllxmc;
	}

	public String getFdjh() {
		return fdjh;
	}

	public void setFdjh(String fdjh) {
		this.fdjh = fdjh;
	}

	public String getJdcid() {
		return jdcid;
	}

	public void setJdcid(String jdcid) {
		this.jdcid = jdcid;
	}

	public String getZzcmc() {
		return zzcmc;
	}

	public void setZzcmc(String zzcmc) {
		this.zzcmc = zzcmc;
	}

	public String getSfzjhm() {
		return sfzjhm;
	}

	public void setSfzjhm(String sfzjhm) {
		this.sfzjhm = sfzjhm;
	}

	public String getZzxzqh() {
		return zzxzqh;
	}

	public void setZzxzqh(String zzxzqh) {
		this.zzxzqh = zzxzqh;
	}

	public String getZzz() {
		return zzz;
	}

	public void setZzz(String zzz) {
		this.zzz = zzz;
	}

	public String getZzxxdz() {
		return zzxxdz;
	}

	public void setZzxxdz(String zzxxdz) {
		this.zzxxdz = zzxxdz;
	}

	public String getDjrq() {
		return djrq;
	}

	public void setDjrq(String djrq) {
		this.djrq = djrq;
	}

	public String getSfzjzl() {
		return sfzjzl;
	}

	public void setSfzjzl(String sfzjzl) {
		this.sfzjzl = sfzjzl;
	}

	public String getZzxz() {
		return zzxz;
	}

	public void setZzxz(String zzxz) {
		this.zzxz = zzxz;
	}

	public String getCsys() {
		return csys;
	}

	public void setCsys(String csys) {
		this.csys = csys;
	}

	public String getCsysmc() {
		return csysmc;
	}

	public void setCsysmc(String csysmc) {
		this.csysmc = csysmc;
	}

	public String getDjzsbh() {
		return djzsbh;
	}

	public void setDjzsbh(String djzsbh) {
		this.djzsbh = djzsbh;
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

	public String getHpzlmc() {
		return hpzlmc;
	}

	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
	}

	public String getHpys() {
		return hpys;
	}

	public void setHpys(String hpys) {
		this.hpys = hpys;
	}

	public String getHpysmc() {
		return hpysmc;
	}

	public void setHpysmc(String hpysmc) {
		this.hpysmc = hpysmc;
	}

	public String getClpp() {
		return clpp;
	}

	public void setClpp(String clpp) {
		this.clpp = clpp;
	}

	public String getClxh() {
		return clxh;
	}

	public void setClxh(String clxh) {
		this.clxh = clxh;
	}

	public String getClsbdh() {
		return clsbdh;
	}

	public void setClsbdh(String clsbdh) {
		this.clsbdh = clsbdh;
	}
	
	public String getZhzxzqh() {
		return zhzxzqh;
	}

	public void setZhzxzqh(String zhzxzqh) {
		this.zhzxzqh = zhzxzqh;
	}

	public String getJdcsyr() {
		return jdcsyr;
	}

	public void setJdcsyr(String jdcsyr) {
		this.jdcsyr = jdcsyr;
	}

	public String getCcdjrq() {
		return ccdjrq;
	}

	public void setCcdjrq(String ccdjrq) {
		this.ccdjrq = ccdjrq;
	}

	public String getFpjg() {
		return fpjg;
	}

	public void setFpjg(String fpjg) {
		this.fpjg = fpjg;
	}

	public String getFpjgmc() {
		return fpjgmc;
	}

	public void setFpjgmc(String fpjgmc) {
		this.fpjgmc = fpjgmc;
	}

	public String getJdczt() {
		return jdczt;
	}

	public void setJdczt(String jdczt) {
		this.jdczt = jdczt;
	}

	public String getGcjk() {
		return gcjk;
	}

	public void setGcjk(String gcjk) {
		this.gcjk = gcjk;
	}

	public String getTp1() {
		return tp1;
	}

	public void setTp1(String tp1) {
		this.tp1 = tp1;
	}

	public String getTp2() {
		return tp2;
	}

	public void setTp2(String tp2) {
		this.tp2 = tp2;
	}

	public String getTp3() {
		return tp3;
	}

	public void setTp3(String tp3) {
		this.tp3 = tp3;
	}

	public String getTztp() {
		return tztp;
	}

	public void setTztp(String tztp) {
		this.tztp = tztp;
	}
	
	
}
