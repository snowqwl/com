package com.sunshine.monitor.system.analysis.bean;
/**
 * 指定日期规则
 * @author Administrator
 *
 */
public class DaysNightCarReg extends NightCarRegAbstrat {
 
	// 指定天分析
	private String days;
	 
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getAnalysisType() {
		
		return NightCarAnalysisType.DAY_REG.getCode();
	}
	 
}
 
