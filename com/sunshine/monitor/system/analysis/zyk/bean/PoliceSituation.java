package com.sunshine.monitor.system.analysis.zyk.bean;

public class PoliceSituation {
	/**
	 * @主键
	 */
	private String id; 
	/**
	 * @警情号(关联ID备用)
	 */
	private String jqh;
	/**
	 * @公安编号
	 */
	private String gabh;
	/**
	 * @案件编号
	 */
	private String ajbh;
	/**
	 * @警情名称
	 */
	private String jqmc;
	/**
	 * @信息来源 - 0表示人工，1表示导入
	 */
	private String xxly;
	/**
	 * @行政区划
	 */
	private String xzqh;
	/**
	 * @号牌号码
	 */
	private String hphm;
	/**
	 * @号牌种类
	 */
	private String hpzl;
	/**
	 * @号牌种类
	 */
	private String hpzlmc;
	public String getHpzlmc() {
		return hpzlmc;
	}
	public void setHpzlmc(String hpzlmc) {
		this.hpzlmc = hpzlmc;
	}
	/**
	 * @录入人
	 */
	private String lrr;
	/**
	 * @录入人名称
	 */
	private String lrrmc;
	/**
	 * @录入时间
	 */
	private String lrsj;
	/**
	 * @描述
	 */
	private String description; 
	/**
	 * @图片路径
	 */
	private String tplj; 
	/**
	 * @备用字段 1
	 */
	private String by1; 
	/**
	 * @备用字段2
	 */
	private String by2; 
	/**
	 * @备用字段3
	 */
	private String by3;
	private String kssj;
	private String jssj;
	public String getLrrmc() {
		return lrrmc;
	}
	public void setLrrmc(String lrrmc) {
		this.lrrmc = lrrmc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJqh() {
		return jqh;
	}
	public void setJqh(String jqh) {
		this.jqh = jqh;
	}
	public String getGabh() {
		return gabh;
	}
	public void setGabh(String gabh) {
		this.gabh = gabh;
	}
	public String getAjbh() {
		return ajbh;
	}
	public void setAjbh(String ajbh) {
		this.ajbh = ajbh;
	}
	public String getJqmc() {
		return jqmc;
	}
	public void setJqmc(String jqmc) {
		this.jqmc = jqmc;
	}
	public String getXxly() {
		return xxly;
	}
	public void setXxly(String xxly) {
		this.xxly = xxly;
	}
	public String getXzqh() {
		return xzqh;
	}
	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
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
	public String getLrr() {
		return lrr;
	}
	public void setLrr(String lrr) {
		this.lrr = lrr;
	}
	public String getLrsj() {
		return lrsj;
	}
	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTplj() {
		return tplj;
	}
	public void setTplj(String tplj) {
		this.tplj = tplj;
	}
	public String getBy1() {
		return by1;
	}
	public void setBy1(String by1) {
		this.by1 = by1;
	}
	public String getBy2() {
		return by2;
	}
	public void setBy2(String by2) {
		this.by2 = by2;
	}
	public String getBy3() {
		return by3;
	}
	public void setBy3(String by3) {
		this.by3 = by3;
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
