package com.sunshine.monitor.system.manager.bean;

import com.sunshine.monitor.comm.bean.Entity;
import javax.xml.bind.annotation.XmlElement;

public class SysUser extends Entity{

	private static final long serialVersionUID = 11L;
	private String yhdh;
	private String yhmc;
	private String mm;
	private String sfzmhm;
	private String jh;
	private String glbm;
	private String ipks;
	private String ipjs;
	private String zhyxq;
	private String mmyxq;
	private String zt;
	private String qxms;
	private String bz;
	private String sq;
	private String lxdh1;
	private String lxdh2;
	private String lxdh3;
	private String lxcz;
	private String dzyx;
	private String lxdz;
	private String bgdz;
	private String syyhdh;
	private String dzzs;
	private String dzzsxx;
	private String gxsj;
	private String mm1;
	public String bmmc;
	public String cxdh;
	public String roles;
	public String rolemc;
	public String ip;
	public String yzm;
	public String authority;//È¨ÏÞ£º0-¹ÜÀíÔ±
	public String yhid;

	public String getRolemc() {
		return rolemc;
	}

	public void setRolemc(String rolemc) {
		this.rolemc = rolemc;
	}
	
	@XmlElement(name="用户名")
	public String getYhdh() {
		return this.yhdh;
	}

	public void setYhdh(String yhdh) {
		this.yhdh = yhdh;
	}

	public String getYhmc() {
		return this.yhmc;
	}

	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}

	public String getMm() {
		return this.mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	public String getSfzmhm() {
		return this.sfzmhm;
	}

	public void setSfzmhm(String sfzmhm) {
		this.sfzmhm = sfzmhm;
	}

	public String getJh() {
		return this.jh;
	}

	public void setJh(String jh) {
		this.jh = jh;
	}

	public String getGlbm() {
		return this.glbm;
	}

	public void setGlbm(String glbm) {
		this.glbm = glbm;
	}

	public String getIpks() {
		return this.ipks;
	}

	public void setIpks(String ipks) {
		this.ipks = ipks;
	}

	public String getIpjs() {
		return this.ipjs;
	}

	public void setIpjs(String ipjs) {
		this.ipjs = ipjs;
	}

	public String getZhyxq() {
		return this.zhyxq;
	}

	public void setZhyxq(String zhyxq) {
		this.zhyxq = zhyxq;
	}

	public String getMmyxq() {
		return this.mmyxq;
	}

	public void setMmyxq(String mmyxq) {
		this.mmyxq = mmyxq;
	}

	public String getZt() {
		return this.zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getQxms() {
		return this.qxms;
	}

	public void setQxms(String qxms) {
		this.qxms = qxms;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getSq() {
		return this.sq;
	}

	public void setSq(String sq) {
		this.sq = sq;
	}

	public String getLxdh1() {
		return this.lxdh1;
	}

	public void setLxdh1(String lxdh1) {
		this.lxdh1 = lxdh1;
	}

	public String getLxdh2() {
		return this.lxdh2;
	}

	public void setLxdh2(String lxdh2) {
		this.lxdh2 = lxdh2;
	}

	public String getLxdh3() {
		return this.lxdh3;
	}

	public void setLxdh3(String lxdh3) {
		this.lxdh3 = lxdh3;
	}

	public String getLxcz() {
		return this.lxcz;
	}

	public void setLxcz(String lxcz) {
		this.lxcz = lxcz;
	}

	public String getDzyx() {
		return this.dzyx;
	}

	public void setDzyx(String dzyx) {
		this.dzyx = dzyx;
	}

	public String getLxdz() {
		return this.lxdz;
	}

	public void setLxdz(String lxdz) {
		this.lxdz = lxdz;
	}

	public String getBgdz() {
		return this.bgdz;
	}

	public void setBgdz(String bgdz) {
		this.bgdz = bgdz;
	}

	public String getSyyhdh() {
		return this.syyhdh;
	}

	public void setSyyhdh(String syyhdh) {
		this.syyhdh = syyhdh;
	}

	public String getDzzs() {
		return this.dzzs;
	}

	public void setDzzs(String dzzs) {
		this.dzzs = dzzs;
	}

	public String getDzzsxx() {
		return this.dzzsxx;
	}

	public void setDzzsxx(String dzzsxx) {
		this.dzzsxx = dzzsxx;
	}

	public String getGxsj() {
		return this.gxsj;
	}

	public void setGxsj(String gxsj) {
		this.gxsj = gxsj;
	}

	public String getBmmc() {
		return this.bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getCxdh() {
		return this.cxdh;
	}

	public void setCxdh(String cxdh) {
		this.cxdh = cxdh;
	}

	public String getRoles() {
		return this.roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getYzm() {
		return this.yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

	public String getMm1() {
		return mm1;
	}

	public void setMm1(String mm1) {
		this.mm1 = mm1;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		
		return "userName:"+this.yhmc + "-yhdh:" + this.yhdh + "-sfz:" + this.sfzmhm + "-glbm:" +this.glbm;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
}
