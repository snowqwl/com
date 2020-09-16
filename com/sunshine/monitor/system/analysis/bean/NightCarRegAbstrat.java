package com.sunshine.monitor.system.analysis.bean;

/**
 * 昼伏夜出规则
 * @author Administrator
 *
 */
public abstract class NightCarRegAbstrat {
 
	// 规则序号
	protected String gzxh;
	
	// 规则名称
	protected String gzName;
	
	// 昼开始时间
	protected String daykssj;
	
	// 昼结束时间
	protected String dayJssj;
	 
	// 上一天夜开始时间
	protected String preNightKssj;
	 
	// 上一天夜结束时间
	protected String preNightJssj;
	 
	// 下一天夜开始时间
	protected String nextNightKssj;
	 
	// 下一天夜结束时间
	protected String nextNightJssj;
	 
	protected abstract String getAnalysisType();

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
}
 
