package com.sunshine.monitor.system.sign.bean;

import com.sunshine.monitor.comm.bean.Entity;
/**
 * 指挥中心签到实体
 * @author oy
 *
 */
public class BKSign extends Entity{

	private static final long serialVersionUID = 1L;

	private String id ;
	
	/**
	 * 签到时间
	 */
	private String qdkssj;
	
	/**
	 * 签出时间
	 */
	private String qdjssj;
	
	/**
	 * 签到人
	 */
	private String qdr;
	
	/**
	 * 签到单位
	 */
	private String dwdm;
	
	private String jjr;
	
	/**
	 * 布撤控审批数
	 */
	private Long bcksps;
	
	/**
	 * 预警签收数
	 */
	private Long yjqss;
	
	/**
	 * 指令下达数
	 */
	private Long zlxds;
	
	/**
	 * 指令返馈数
	 */
	private Long zlfks;
	/**
	 * 备注
	 */
	private String bz;
	
	
	
	
	public String qdrmc ;
	public String dwdmmc;
	public String jjrPass;
	public String jjrmc;
	public String zbrmc;
	public String zbrbh;
	public String getZbrbh() {
		return zbrbh;
	}

	public void setZbrbh(String zbrbh) {
		this.zbrbh = zbrbh;
	}

	public String zbrdh;
	
	
	public String getZbrmc() {
		return zbrmc;
	}

	public void setZbrmc(String zbrmc) {
		this.zbrmc = zbrmc;
	}

	public String getZbrdh() {
		return zbrdh;
	}

	public void setZbrdh(String zbrdh) {
		this.zbrdh = zbrdh;
	}

	public String getJjrmc() {
		return jjrmc;
	}

	public void setJjrmc(String jjrmc) {
		this.jjrmc = jjrmc;
	}

	public String getJjrPass() {
		return jjrPass;
	}

	public void setJjrPass(String jjrPass) {
		this.jjrPass = jjrPass;
	}
	
	public String getJjr() {
		return jjr;
	}

	public void setJjr(String jjr) {
		this.jjr = jjr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getQdkssj() {
		return qdkssj;
	}

	public void setQdkssj(String qdkssj) {
		this.qdkssj = qdkssj;
	}

	public String getQdjssj() {
		return qdjssj;
	}

	public void setQdjssj(String qdjssj) {
		this.qdjssj = qdjssj;
	}

	public String getQdr() {
		return qdr;
	}

	public void setQdr(String qdr) {
		this.qdr = qdr;
	}

	public String getDwdm() {
		return dwdm;
	}

	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}

	public Long getBcksps() {
		return bcksps;
	}

	public void setBcksps(Long bcksps) {
		this.bcksps = bcksps;
	}

	public Long getYjqss() {
		return yjqss;
	}

	public void setYjqss(Long yjqss) {
		this.yjqss = yjqss;
	}

	public Long getZlxds() {
		return zlxds;
	}

	public void setZlxds(Long zlxds) {
		this.zlxds = zlxds;
	}

	public Long getZlfks() {
		return zlfks;
	}

	public void setZlfks(Long zlfks) {
		this.zlfks = zlfks;
	}

	public String getQdrmc() {
		return qdrmc;
	}

	public void setQdrmc(String qdrmc) {
		this.qdrmc = qdrmc;
	}

	public String getDwdmmc() {
		return dwdmmc;
	}

	public void setDwdmmc(String dwdmmc) {
		this.dwdmmc = dwdmmc;
	}
}
