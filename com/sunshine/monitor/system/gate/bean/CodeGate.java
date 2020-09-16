package com.sunshine.monitor.system.gate.bean;

import com.sunshine.monitor.comm.bean.Entity;

public class CodeGate extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  
	private String kdbh;
	private String kdmc;
	private String kklx;
	private String dwdm;
	private String gabh;
	private String lxdh;
	private String lxdz;
    private String jklj;
	private String kkjd;
	private String kkwd;
	private String gxsj;
	private String bz;
    private String sflj;
    private String sfbjkk;
	private String kklx2;	
	private String kkdz;	
	private String kkxz;	
	private String sftgtzcp;	
	private String sfjbspgn;	
	private String sfjgkk;	
	private String sfwfzp;	
	private String sfcs;
	private String kkzt;
	private String bjcs;
	
	// 对应全国卡口备案新增字段
	private String qgkdbh;
	// 行政区划
	private String xzqh;
	// 行政区划名称
	private String xzqhmc;
	// 道路类型
	private String dllx;
	// 道路代码
	private String dldm;
	// 路口号/公里数
	private String lkh;
	// 道路米数
	private String dlms;
	// 道路名称
	private String dlmc;
	// 创建时间
	private String cjsj;
	// 是否纳入考核
	private String sfkh;
	// 数据上传模式
	private String sjscms;
	// 卡口所在位置（1-公安网，2-专网）
	private String kkwz;
	// 设备供应商
	private String sbgys;
	// 上传软件开发商
	private String rjkfs;

	private String dwdmmc;
	private String bjcsmc;
	private String kkxzmc;
	private String kkztmc;
	private String kklxmc;

	private String directs;//方向
	
	/**
	 * 湖南新增字段
	 * @return
	 */

	private String sxfxjclx;
	private String xxfxjclx;
	private String sxfxbm;
	private String xxfxbm;
	private String kkjc;
	private String yjczlx;
	private String sybm;
	private String bdms = "0";  //比对模式：默认为0
	private String tztpwz;
	private String gctpwz;
	private String spsj;
	private String jyw;
	private String csbj;
	private String bjcsbj;
	private String sfjbtztp;
	
	private String sybmmc;

	public String getKdbh() {
		return kdbh;
	}

	public void setKdbh(String kdbh) {
		this.kdbh = kdbh;
	}

	public String getKdmc() {
		return kdmc;
	}

	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}

	public String getKklx() {
		return kklx;
	}

	public void setKklx(String kklx) {
		this.kklx = kklx;
	}

	public String getDwdm() {
		return dwdm;
	}

	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}
	public String getGabh() {
		return gabh;
	}
	public void setGabh(String gabh) {
		this.gabh = gabh;
	}
	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getLxdz() {
		return lxdz;
	}

	public void setLxdz(String lxdz) {
		this.lxdz = lxdz;
	}

	public String getJklj() {
		return jklj;
	}

	public void setJklj(String jklj) {
		this.jklj = jklj;
	}

	public String getKkjd() {
		return kkjd;
	}

	public void setKkjd(String kkjd) {
		this.kkjd = kkjd;
	}

	public String getKkwd() {
		return kkwd;
	}

	public void setKkwd(String kkwd) {
		this.kkwd = kkwd;
	}

	public String getGxsj() {
		return gxsj;
	}

	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getSflj() {
		return sflj;
	}

	public void setSflj(String sflj) {
		this.sflj = sflj;
	}

	public String getSfbjkk() {
		return sfbjkk;
	}

	public void setSfbjkk(String sfbjkk) {
		this.sfbjkk = sfbjkk;
	}

	public String getKklx2() {
		return kklx2;
	}

	public void setKklx2(String kklx2) {
		this.kklx2 = kklx2;
	}

	public String getKkdz() {
		return kkdz;
	}

	public void setKkdz(String kkdz) {
		this.kkdz = kkdz;
	}

	public String getKkxz() {
		return kkxz;
	}

	public void setKkxz(String kkxz) {
		this.kkxz = kkxz;
	}

	public String getSftgtzcp() {
		return sftgtzcp;
	}

	public void setSftgtzcp(String sftgtzcp) {
		this.sftgtzcp = sftgtzcp;
	}

	public String getSfjbspgn() {
		return sfjbspgn;
	}

	public void setSfjbspgn(String sfjbspgn) {
		this.sfjbspgn = sfjbspgn;
	}

	public String getSfjgkk() {
		return sfjgkk;
	}

	public void setSfjgkk(String sfjgkk) {
		this.sfjgkk = sfjgkk;
	}

	public String getSfwfzp() {
		return sfwfzp;
	}

	public void setSfwfzp(String sfwfzp) {
		this.sfwfzp = sfwfzp;
	}

	public String getSfcs() {
		return sfcs;
	}

	public void setSfcs(String sfcs) {
		this.sfcs = sfcs;
	}

	public String getKkzt() {
		return kkzt;
	}

	public void setKkzt(String kkzt) {
		this.kkzt = kkzt;
	}

	public String getBjcs() {
		return bjcs;
	}

	public void setBjcs(String bjcs) {
		this.bjcs = bjcs;
	}

	public String getDirects() {
		return directs;
	}

	public void setDirects(String directs) {
		this.directs = directs;
	}

	public String getSxfxjclx() {
		return sxfxjclx;
	}

	public void setSxfxjclx(String sxfxjclx) {
		this.sxfxjclx = sxfxjclx;
	}

	public String getXxfxjclx() {
		return xxfxjclx;
	}

	public void setXxfxjclx(String xxfxjclx) {
		this.xxfxjclx = xxfxjclx;
	}

	public String getSxfxbm() {
		return sxfxbm;
	}

	public void setSxfxbm(String sxfxbm) {
		this.sxfxbm = sxfxbm;
	}

	public String getXxfxbm() {
		return xxfxbm;
	}

	public void setXxfxbm(String xxfxbm) {
		this.xxfxbm = xxfxbm;
	}

	public String getKkjc() {
		return kkjc;
	}

	public void setKkjc(String kkjc) {
		this.kkjc = kkjc;
	}

	public String getYjczlx() {
		return yjczlx;
	}

	public void setYjczlx(String yjczlx) {
		this.yjczlx = yjczlx;
	}

	public String getSybm() {
		return sybm;
	}

	public void setSybm(String sybm) {
		this.sybm = sybm;
	}

	public String getBdms() {
		return bdms;
	}

	public void setBdms(String bdms) {
		this.bdms = bdms;
	}

	public String getTztpwz() {
		return tztpwz;
	}

	public void setTztpwz(String tztpwz) {
		this.tztpwz = tztpwz;
	}

	public String getGctpwz() {
		return gctpwz;
	}

	public void setGctpwz(String gctpwz) {
		this.gctpwz = gctpwz;
	}

	public String getSpsj() {
		return spsj;
	}

	public void setSpsj(String spsj) {
		this.spsj = spsj;
	}

	public String getJyw() {
		return jyw;
	}

	public void setJyw(String jyw) {
		this.jyw = jyw;
	}

	public String getBjcsbj() {
		return bjcsbj;
	}

	public void setBjcsbj(String bjcsbj) {
		this.bjcsbj = bjcsbj;
	}

	public String getCsbj() {
		return csbj;
	}

	public void setCsbj(String csbj) {
		this.csbj = csbj;
	}

	public String getQgkdbh() {
		return qgkdbh;
	}

	public void setQgkdbh(String qgkdbh) {
		this.qgkdbh = qgkdbh;
	}

	public String getXzqh() {
		return xzqh;
	}

	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}

	public String getXzqhmc() {
		return xzqhmc;
	}

	public void setXzqhmc(String xzqhmc) {
		this.xzqhmc = xzqhmc;
	}

	public String getDllx() {
		return dllx;
	}

	public void setDllx(String dllx) {
		this.dllx = dllx;
	}

	public String getDldm() {
		return dldm;
	}

	public void setDldm(String dldm) {
		this.dldm = dldm;
	}

	public String getLkh() {
		return lkh;
	}

	public void setLkh(String lkh) {
		this.lkh = lkh;
	}

	public String getDlms() {
		return dlms;
	}

	public void setDlms(String dlms) {
		this.dlms = dlms;
	}

	public String getDlmc() {
		return dlmc;
	}

	public void setDlmc(String dlmc) {
		this.dlmc = dlmc;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getSfkh() {
		return sfkh;
	}

	public void setSfkh(String sfkh) {
		this.sfkh = sfkh;
	}

	public String getSjscms() {
		return sjscms;
	}

	public void setSjscms(String sjscms) {
		this.sjscms = sjscms;
	}

	public String getKkwz() {
		return kkwz;
	}

	public void setKkwz(String kkwz) {
		this.kkwz = kkwz;
	}

	public String getSbgys() {
		return sbgys;
	}

	public void setSbgys(String sbgys) {
		this.sbgys = sbgys;
	}

	public String getRjkfs() {
		return rjkfs;
	}

	public void setRjkfs(String rjkfs) {
		this.rjkfs = rjkfs;
	}

	public String getDwdmmc() {
		return dwdmmc;
	}

	public void setDwdmmc(String dwdmmc) {
		this.dwdmmc = dwdmmc;
	}

	public String getBjcsmc() {
		return bjcsmc;
	}

	public void setBjcsmc(String bjcsmc) {
		this.bjcsmc = bjcsmc;
	}

	public String getKkxzmc() {
		return kkxzmc;
	}

	public void setKkxzmc(String kkxzmc) {
		this.kkxzmc = kkxzmc;
	}

	public String getKkztmc() {
		return kkztmc;
	}

	public void setKkztmc(String kkztmc) {
		this.kkztmc = kkztmc;
	}

	public String getKklxmc() {
		return kklxmc;
	}

	public void setKklxmc(String kklxmc) {
		this.kklxmc = kklxmc;
	}

	public String getSfjbtztp() {
		return sfjbtztp;
	}

	public void setSfjbtztp(String sfjbtztp) {
		this.sfjbtztp = sfjbtztp;
	}

	public String getSybmmc() {
		return sybmmc;
	}

	public void setSybmmc(String sybmmc) {
		this.sybmmc = sybmmc;
	}

	
}
