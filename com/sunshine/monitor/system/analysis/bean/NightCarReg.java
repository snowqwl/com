package com.sunshine.monitor.system.analysis.bean;

import com.sunshine.monitor.comm.bean.Entity;
/**
 * 规则实体对应数据库表
 *
 */
public class NightCarReg extends Entity{

	private static final long serialVersionUID = 1L;

	// 规则序号
	private String gzxh;
	
	// 规则名称
	private String gzName;
	
	// 昼开始时间
	private String daykssj;
	
	// 昼结束时间
	private String dayJssj;
	 
	// 上一天夜开始时间
	private String preNightKssj;
	 
	// 上一天夜结束时间
	private String preNightJssj;
	 
	// 下一天夜开始时间
	private String nextNightKssj;
	 
	// 下一天夜结束时间
	private String nextNightJssj;
	
	// 是否有效
	private String valid;


	public String getGzxh() {
		return gzxh;
	}

	public void setGzxh(String gzxh) {
		this.gzxh = gzxh;
	}

	public String getGzName() {
		return gzName;
	}

	public void setGzName(String gzName) {
		this.gzName = gzName;
	}

	public String getDaykssj() {
		return daykssj;
	}

	public void setDaykssj(String daykssj) {
		this.daykssj = daykssj;
	}

	public String getDayJssj() {
		return dayJssj;
	}

	public void setDayJssj(String dayJssj) {
		this.dayJssj = dayJssj;
	}

	public String getPreNightKssj() {
		return preNightKssj;
	}

	public void setPreNightKssj(String preNightKssj) {
		this.preNightKssj = preNightKssj;
	}

	public String getPreNightJssj() {
		return preNightJssj;
	}

	public void setPreNightJssj(String preNightJssj) {
		this.preNightJssj = preNightJssj;
	}

	public String getNextNightKssj() {
		return nextNightKssj;
	}

	public void setNextNightKssj(String nextNightKssj) {
		this.nextNightKssj = nextNightKssj;
	}

	public String getNextNightJssj() {
		return nextNightJssj;
	}

	public void setNextNightJssj(String nextNightJssj) {
		this.nextNightJssj = nextNightJssj;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}
}
