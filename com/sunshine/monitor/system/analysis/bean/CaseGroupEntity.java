package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.bean.Entity;

/**
 * 专案信息实体类
 *
 */
public class CaseGroupEntity extends Entity {

	private static final long serialVersionUID = 1L;
	
	//专案编号(六位行政代码+10位序列号SEQ_YP_ZAXX)
	private String zabh;
	//专案名称
	private String zamc;
	//专案案情描述
	private String aqms;
	//专案开始时间
	private String zakssj;
	//专案终止时间
	private String zajssj;
	//专案负责人警号
	private String fzrjh;
	//专案负责人姓名
	private String fzrxm;
	//专案类型
	private String zalx;
	//录入时间
	private String lrsj;
	//所属行政区划
	private String xzqh;
	//行政区划名称
	private String xzqhmc;
	//备注
	private String bz;
	//关联案件
    private String glaj;
    
    private String kssj;
    
    private String jssj;
    
	public String getZabh() {
		return zabh;
	}
	public void setZabh(String zabh) {
		this.zabh = zabh;
	}
	public String getZamc() {
		return zamc;
	}
	public void setZamc(String zamc) {
		this.zamc = zamc;
	}
	public String getAqms() {
		return aqms;
	}
	public void setAqms(String aqms) {
		this.aqms = aqms;
	}
	public String getFzrjh() {
		return fzrjh;
	}
	public void setFzrjh(String fzrjh) {
		this.fzrjh = fzrjh;
	}
	public String getFzrxm() {
		return fzrxm;
	}
	public void setFzrxm(String fzrxm) {
		this.fzrxm = fzrxm;
	}
	public String getZalx() {
		return zalx;
	}
	public void setZalx(String zalx) {
		this.zalx = zalx;
	}
	public String getLrsj() {
		return lrsj;
	}
	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
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
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getGlaj() {
		return glaj;
	}
	public void setGlaj(String glaj) {
		this.glaj = glaj;
	}
	public String getZakssj() {
		return zakssj;
	}
	public void setZakssj(String zakssj) {
		this.zakssj = zakssj;
	}
	public String getZajssj() {
		return zajssj;
	}
	public void setZajssj(String zajssj) {
		this.zajssj = zajssj;
	}
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

}
