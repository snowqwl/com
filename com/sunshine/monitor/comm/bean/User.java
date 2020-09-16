package com.sunshine.monitor.comm.bean;

/**
 * 系统用户表
 * @author JCBK-OUYANG 2012/12/26
 *
 */
public class User extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户编号
	 */
	private String yhdh;
	/**
	 * 用户名称
	 */
	private String yhmc;
	/**
	 * 用户密码(128位MD5加密)
	 */
	private String mm;
	/**
	 * 身份证
	 */
	private String sfzmhm;
	/**
	 * 警号
	 */
	private String jh;
	/**
	 * 管理部门
	 */
	private String glbm;
	/**
	 * IP开始段
	 */
	private String ipks;
	/**
	 * IP结束段
	 */
	private String ipjs;
	/**
	 * 帐号有效日期
	 */
	private String zhyxq;
	/**
	 * 密码有效期
	 */
	private String mmyxq;
	/**
	 * 状态，0-无效，1-有效
	 */
	private String zt;
	/**
	 * 权限类型，1-角色，2-非角色
	 */
	private String qxms;
	/**
	 * 备注
	 */
	private String bz;
	/**
	 * 社区
	 */
	private String sq;
	/**
	 * 联系电话1
	 */
	private String lxdh1;
	/**
	 * 联系电话2
	 */
	private String lxdh2;
	/**
	 * 联系电话3
	 */
	private String lxdh3;
	/**
	 * 联系传真
	 */
	private String lxcz;
	/**
	 * 电子邮箱
	 */
	private String dzyx;
	/**
	 * 联系地址
	 */
	private String lxdz;
	/**
	 * 办公地址
	 */
	private String bgdz;
	/**
	 * 外部用户主键
	 */
	private String syyhdh;
	/**
	 * 电子证书：0-普通
	 */
	private String dzzs;
	/**
	 * 电子证书信息
	 */
	private String dzzsxx;
	/**
	 * 更新时间
	 */
	private String gxsj;
	
	//-----------------
	//部门名称
	public String bmmc;
	//菜单编号
	public String cxdh;
	//角色
	public String roles;
	//ip
	public String ip;
	
	public String yzm;

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
}
