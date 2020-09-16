package com.sunshine.monitor.comm.bean;

/**
 * 目录(主菜单)与子菜单关系表
 * @author JCBK-OUYANG 2012/12/26
 *
 */
public class Menu extends Entity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单编号
	 */
	private String id;
	/**
	 * 菜单名称
	 */
	private String name;
	/**
	 * 有效地址(访问URL)
	 */
	private String ymdz;
	/**
	 * 顺序号（前台显示序顺）
	 */
	private Long sxh;
	/**
	 * 级别（实现多级别菜单）
	 */
	private String cxsx;
	/**
	 * 备注
	 */
	private String bz;
	/**
	 * 有效路径
	 */
	private String lj;
	
	/**
	 * 父菜单编号
	 */
	private String pid;
	
	private String lb;
	
	/**
	 * 是否弹出菜单功能
	 */
	private String ispop;
	
	public String getIspop() {
		return ispop;
	}

	public void setIspop(String ispop) {
		this.ispop = ispop;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYmdz() {
		return this.ymdz;
	}

	public void setYmdz(String ymdz) {
		this.ymdz = ymdz;
	}

	public Long getSxh() {
		return this.sxh;
	}

	public void setSxh(Long sxh) {
		this.sxh = sxh;
	}

	public String getCxsx() {
		return this.cxsx;
	}

	public void setCxsx(String cxsx) {
		this.cxsx = cxsx;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getLj() {
		return this.lj;
	}

	public void setLj(String lj) {
		this.lj = lj;
	}

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}
}
