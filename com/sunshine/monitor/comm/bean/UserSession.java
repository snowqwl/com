package com.sunshine.monitor.comm.bean;

import java.util.List;

import com.sunshine.monitor.system.manager.bean.Department;
import com.sunshine.monitor.system.manager.bean.SysUser;
import com.sunshine.monitor.system.manager.bean.Syspara;

public class UserSession extends Entity {
	private static final long serialVersionUID = 1L;
	private SysUser sysuser;
	private Department department;
	private String xtree;
	private String tjnf;
	private String tjlx;
	private String tjksrq;
	private String tjjsrq;
	private String fzjg;
	private boolean isProvinceDpt;
	private List<Syspara> syspara;

	public String getXtree() {
		return this.xtree;
	}

	public void setXtree(String xtree) {
		this.xtree = xtree;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public SysUser getSysuser() {
		return sysuser;
	}

	public void setSysuser(SysUser sysuser) {
		this.sysuser = sysuser;
	}

	public String getTjjsrq() {
		return this.tjjsrq;
	}

	public void setTjjsrq(String tjjsrq) {
		this.tjjsrq = tjjsrq;
	}

	public String getTjksrq() {
		return this.tjksrq;
	}

	public void setTjksrq(String tjksrq) {
		this.tjksrq = tjksrq;
	}

	public String getTjlx() {
		return this.tjlx;
	}

	public void setTjlx(String tjlx) {
		this.tjlx = tjlx;
	}

	public String getTjnf() {
		return this.tjnf;
	}

	public void setTjnf(String tjnf) {
		this.tjnf = tjnf;
	}

	public String getFzjg() {
		return this.fzjg;
	}

	public void setFzjg(String fzjg) {
		this.fzjg = fzjg;
	}
	
	public boolean getIsProvinceDpt() {
		return this.isProvinceDpt;
	}
	
	public void setIsProvinceDpt(boolean isProvinceDpt) {
		this.isProvinceDpt = isProvinceDpt;
	}

	public List<Syspara> getSyspara() {
		return this.syspara;
	}

	public void setSyspara(List<Syspara> syspara) {
		this.syspara = syspara;
	}
}
