package com.sunshine.monitor.system.analysis.bean;

public class NightCarCondition {
 
	// 卡口数
	private String kdbhs;
	 
	// 查询开始时间
	private String kssj;
	 
	// 查询结束时间
	private String jssj;
	 
	// 分析类型(常规、连续、指定日期)
	private String analysistype;
	
	// 连续天数
	private int continueDaycount;
	
	// 指定日期数
	private String days;

	public String getKdbhs() {
		return kdbhs;
	}

	public void setKdbhs(String kdbhs) {
		this.kdbhs = kdbhs;
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

	public String getAnalysistype() {
		return analysistype;
	}

	public void setAnalysistype(String analysistype) {
		this.analysistype = analysistype;
	}

	public int getContinueDaycount() {
		return continueDaycount;
	}

	public void setContinueDaycount(int continueDaycount) {
		this.continueDaycount = continueDaycount;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}
	
}
 
